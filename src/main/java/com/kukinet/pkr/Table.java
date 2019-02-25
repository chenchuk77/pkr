package com.kukinet.pkr;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.kukinet.cards.Card;
import com.kukinet.cards.Deck;
import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table {
    private Logger logger = LoggerFactory.getLogger(Table.class);
    private static final int ACTION_WAIT_TIME_SEC = 240;
//    private static final int ACTION_WAIT_TIME_SEC = 30;
    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 4;

    private int roundTime = 10 * 60; // 10 minutes for round

    // will be changed dynamically
    private int ante;
    private int sb;
    private int bb;

    private int bet;
    private int pot;
    private boolean raisedPot;

    private int sbPosition;
    private int bbPosition;
    private int dealerPosition;

    private int index;
    private Player activePlayer;
    private Player raiser;
    private String bettingRound;
    private List<Card> communityCards;

    private Deck deck;

    private Map<Integer, Player> seats;
    private ConnectionManager connectionManager;

    // a table ready to play game
    public Table(List<Player> players, ConnectionManager connectionManager){
        this.connectionManager = connectionManager;
        seatPlayers(players);
        // setting values, initHand() will set the sb to 0, bb to 1, dealer to 8 (9p)
        // setting values, initHand() will set the sb to 0, bb to 1, dealer to 3 (4p)
        this.sbPosition = players.size()-1;
        this.bbPosition = 0;
        this.dealerPosition = players.size()-2;
        this.index = 0;
    }

    private void seatPlayers(List<Player> players){
        if (players.size() < MIN_PLAYERS || players.size() > MAX_PLAYERS){
            logger.warn("illegal number of players to seat ({}).", players.size());
        }
        // TODO - add this
        // Collections.shuffle(players);
        // seat the players from 0-9 (or 0-4 in testing)
        this.seats = new HashMap<>();
        for (int i=0; i<MAX_PLAYERS; i++){
            seats.put(i, players.get(i));
        }
    }


    private int seatOf(String name){
        for (Map.Entry<Integer, Player> entry: seats.entrySet()){
            if (entry.getValue().getName().equals(name)){
                return entry.getKey();
            }
        }
        return 99;
    }








    // wait for player, and build a command from the activePlayer. execute and notify all
    public void waitPlayerCommand(Player player){
        JsonObject requestPlayerAction = new JsonObject();
        requestPlayerAction.addProperty("command", "waitaction");
        requestPlayerAction.addProperty("player", player.getName());
        alertAll(requestPlayerAction.toString());
        player.setWaitForAction(true);
        logger.info("waiting for player {} to act.", player.getName());
        // block until action command accepted
        for (int i=0; i<ACTION_WAIT_TIME_SEC; i++){
            // no command received from client
            if (player.getActionCommand() == null) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                ActionCommand cmd = player.getActionCommand();
                if (cmd.getAction().equals("fold")){
                    fold(player);
                }
                if (cmd.getAction().equals("check")){
                    check(player);
                    player.setChecking(true);
                    raiser = player; // no a raiser but opens the round
                }
                if (cmd.getAction().equals("call")){
                    //call(player, cmd.getAmount());
                    call(player);
                }
                if (cmd.getAction().equals("bet")){
                    bet(player, cmd.getAmount());
                    raiser = player; // no a raiser but opens the round
                }
                if (cmd.getAction().equals("raise")){
                    raise(player, cmd.getAmount());
                    raiser = player;
                }
                player.setWaitForAction(false);
                player.setActionCommand(null);
                return;
            }
        }

        // if no command received in time
        logger.info("no response within {} from player {}, folding and sitout.", ACTION_WAIT_TIME_SEC,player.getName());
        player.setWaitForAction(false);
        player.setActionCommand(null);
        fold(player);
        player.sitout();
    }


    // --------------- player action commands ----------------
    public void fold(Player player)   {
        player.fold();
        logger.info("player {} fold.", player.getName());
        alertAll(sendPlayerMove("fold", 0));
    }

    public void check(Player player){
        logger.info("player {} check.", player.getName());
        alertAll(sendPlayerMove("check", 0));
    }

    // used if no call value provided
    public void call(Player player){
        int callValue = raiser.commited() - player.commited();
        call(player, callValue);
    }
    public void call(Player player, int requestedAmount){
        int amount = player.call(requestedAmount);
        logger.info("player {} call {}.", player.getName(), amount);
        pot += amount;
        alertAll(sendPlayerMove("call", amount));
    }
    public void bet(Player player, int requestedAmount){
        int amount = player.bet(requestedAmount);
        logger.info("player {} bet {}.", player.getName(), amount);
        pot += amount;
        raiser = player;
        raisedPot = true;
        alertAll(sendPlayerMove("call", amount));
    }

    public void raise(Player player, int requestedAmount){
        int amount = player.raise(requestedAmount);
        logger.info("player {} raise {}.", player.getName(), amount);
        pot += amount;
        raiser = player;
        raisedPot = true;
        alertAll(sendPlayerMove("raise", amount));
    }


    private int playersInGame(){
        int count = 0;
        for (Map.Entry<Integer, Player> entry: seats.entrySet()){
            if (entry.getValue().inGame()) count ++;
        }
        return count;
    }
    private int playersInHand(){
        int count = 0;
        for (Map.Entry<Integer, Player> entry: seats.entrySet()){
            if (entry.getValue().inHand()) count ++;
        }
        return count;
    }

    // pointing to next player to act
    private Player nextPlayer(){

        if (index == MAX_PLAYERS-1) {
            index = 0;
        } else {
            index++;
        }
        Player p = seats.get(index);

        // recursively look for the next inHand player
        if (!p.inHand()){
            p = nextPlayer();
        }
        return p;
    }


    // --------------------- Game loop -------------------------

    private void initGame(){
        newLevel(0, 30, 60);
        //initHand();
    }
    void newLevel(int ante, int sb, int bb){
        this.ante = ante;
        this.sb = sb;
        this.bb = bb;
    }

    private void initHand(){
        moveButtons();
        this.bet = 0;
        this.pot = 0;
        initBettingRound("preflop");
    }

    // starting from sb (if sb not in hand, goto the next inhand player)
    private void setFirstPlayerToAct(){
        int fp = sbPosition;
        for (int i=0; i<MAX_PLAYERS; i++){
            // after seat8 comes seat0
            if (fp + i >=MAX_PLAYERS ){
                fp = fp + i - MAX_PLAYERS;
            }
            activePlayer = seats.get(fp);
            if (activePlayer.inGame() && activePlayer.inHand()){
                return;
            }
        }
    }

//    private void setFirstPlayerToMainPotOnly(){
//        int count = 0;
//        for (Map.Entry<Integer, Player> entry: seats.entrySet()){
//            if (entry.getValue().commited();
//        }
//        return count;
//
//    }

    private boolean allPlayerChecked(){
        for (Player p: seats.values()){
            if (p.inHand()){
                if (!p.isChecking()){
                    return false;
                }
            }
        }
        logger.info("all players checked.");
        return true;
    }


    private void initBettingRound(String bettingRound){
        this.bettingRound = bettingRound;
//        activePlayer = seats.get(sbPosition);
        setFirstPlayerToAct();
        for (Player p: seats.values()){
            p.setCommited(0);
            p.setChecking(false);
        }
        raiser = new Player(); // just for not null, overridden by bb anyway
        raisedPot = true;      // will be false by bb to allow complete round

        alertAll(seats()); // update all clients

    }

//    private
    private void moveButtons(){
        if (sbPosition == MAX_PLAYERS-1) sbPosition = 0; else sbPosition ++;
        if (bbPosition == MAX_PLAYERS-1) bbPosition = 0; else bbPosition ++;
        if (dealerPosition== MAX_PLAYERS-1) dealerPosition = 0; else dealerPosition ++;
        // TODO: fix empty seats .... if (seats.get(activePlayer)){
    }
    private void dealTurn(){
        Card turn = deck.pop();
        communityCards.add(turn);
        sendCommunityCardsUpdate();
//        JsonObject communityCardsJSON = new JsonObject();
//        communityCardsJSON.addProperty("type", "community");
//        communityCardsJSON.addProperty("flop1", communityCards.get(0).toString());
//        communityCardsJSON.addProperty("flop2", communityCards.get(1).toString());
//        communityCardsJSON.addProperty("flop3", communityCards.get(2).toString());
//        communityCardsJSON.addProperty("turn", turn.toString());
//        alertAll(communityCardsJSON.toString());

    }
    private void dealRiver(){
        Card river = deck.pop();
        communityCards.add(river);
        sendCommunityCardsUpdate();

//        JsonObject communityCardsJSON = new JsonObject();
//        communityCardsJSON.addProperty("type", "community");
//        communityCardsJSON.addProperty("flop1", communityCards.get(0).toString());
//        communityCardsJSON.addProperty("flop2", communityCards.get(1).toString());
//        communityCardsJSON.addProperty("flop3", communityCards.get(2).toString());
//        communityCardsJSON.addProperty("turn", communityCards.get(3).toString());
//        communityCardsJSON.addProperty("river", river.toString());
//        alertAll(communityCardsJSON.toString());

    }

    private void dealFlop(){
        communityCards = new ArrayList<>();
        Card f1 = deck.pop();
        Card f2 = deck.pop();
        Card f3 = deck.pop();
        communityCards.add(f1); // 0
        communityCards.add(f2); // 1
        communityCards.add(f3); // 2
        logger.info("flop cards: {} {} {}", f1.toString(), f2.toString(), f3.toString());

        sendCommunityCardsUpdate();
//        JsonObject communityCardsJSON = new JsonObject();
//        communityCardsJSON.addProperty("type", "community");
//        communityCardsJSON.addProperty("flop1", f1.toString());
//        communityCardsJSON.addProperty("flop2", f2.toString());
//        communityCardsJSON.addProperty("flop3", f3.toString());
//        alertAll(communityCardsJSON.toString());

    }

    private void sendCommunityCardsUpdate(){
        JsonObject communityCardsJSON = new JsonObject();
        communityCardsJSON.addProperty("type", "community");
        if (communityCards.size() >= 3 ){
            communityCardsJSON.addProperty("flop1", communityCards.get(0).toString());
            communityCardsJSON.addProperty("flop2", communityCards.get(1).toString());
            communityCardsJSON.addProperty("flop3", communityCards.get(2).toString());
        }
        if (communityCards.size() >= 4 ){
            communityCardsJSON.addProperty("turn", communityCards.get(3).toString());
        }
        if (communityCards.size() == 5 ){
            communityCardsJSON.addProperty("river", communityCards.get(4).toString());
        }
        alertAll(communityCardsJSON.toString());
    }

    private void dealHands(){
        logger.info("dealing cards...");
        for (Player player: seats.values()){
            if (player.inGame()){
                Card c1 = deck.pop();
                Card c2 = deck.pop();
                player.setHoleCard1(c1);
                player.setHoleCard2(c2);
                JsonObject pocketCards = new JsonObject();
                pocketCards.addProperty("type", "cards");

                for (Map.Entry<Integer, Player> entry: seats.entrySet()){
                    if (entry.getValue().equals(player)){
                        pocketCards.addProperty("seat", entry.getKey());
                    }
                }
                pocketCards.addProperty("card1", c1.toString());
                pocketCards.addProperty("card2", c2.toString());
                connectionManager.getPlayerConnetion(player).send(pocketCards.toString());
            } else {
                logger.debug("skipping {} (not inGame).", player.getName());
                player.setHoleCard1(null);
                player.setHoleCard2(null);
            }
        }
    }


    public String seats(){
        Gson gsonBuilder = new GsonBuilder().create();
        String json = gsonBuilder.toJson(seats);
        return json ;
    }
    public String buttonsPos() {
        JsonObject buttons = new JsonObject();
        buttons.addProperty("sbPosition", sbPosition);
        buttons.addProperty("bbPosition", bbPosition);
        buttons.addProperty("dealerPosition", dealerPosition);
        return buttons.toString();
    }
    public String betAmounts() {
        JsonObject bets = new JsonObject();
        bets.addProperty("ante", ante);
        bets.addProperty("sb", sb);
        bets.addProperty("bb", bb);
        return bets.toString();
    }

    public String sendPlayerMove(String command, int value){
        JsonObject playerMove = new JsonObject();
        playerMove.addProperty("type", "playermove");
        for (Map.Entry<Integer, Player> entry: seats.entrySet()){
            if (entry.getValue().equals(activePlayer)){
                playerMove.addProperty("seat", entry.getKey());
            }
        }
        playerMove.add("player", new Gson().toJsonTree(activePlayer));
        playerMove.addProperty("command", command);
        playerMove.addProperty("value", value);
        playerMove.addProperty("pot", pot);
        return playerMove.toString();
    }



    // alertAll all connected players
    private void alert(Player player, String message){
        WebSocket ws = connectionManager.getPlayerConnetion(player);
        ws.send(message);
    }

    // alertAll all connected players
    private void alertAll(String message){
        //for (Player activePlayer : players) {
        for (Player player: seats.values()){
            WebSocket ws = connectionManager.getPlayerConnetion(player);
            ws.send(message);
        }
    }

    public void gameLoop(){
        try {
            initGame();

            // more than 1 player in game
            // (exit when there is a winner)
            while(playersInGame()>1) {

                // more than 1 player in hand ( unfolded )
                // (exit when all folds)
                while (playersInHand() > 1) {
                    logger.warn("new hand with {}/{} players (inhand/ingame).", playersInHand(), playersInGame());
                    deck = new Deck();

                    initHand();               // set sb, bb, dealer buttons, active-player, fake raiser
                    alertAll(seats());
                    alertAll(betAmounts());
                    alertAll(buttonsPos());

                    // dealing hands and betting preflop
                    dealHands();
                    // preflop
                    logger.warn("preflop round starts.");
                    while (!activePlayer.equals(raiser)) {
                        logger.info("preflop: raiser-{}/seat-{}, ap-{}/seat-{}, bb-{}/name-{}, raisedPot-{}", raiser.getName(), seatOf(raiser.getName()), activePlayer.getName(), seatOf(activePlayer.getName()), bbPosition, seats.get(bbPosition).getName(), raisedPot);
                        getActionFromPlayer();
                        activePlayer = nextPlayer();
                        // bb option. if no raise
                        if (isBigBlind(activePlayer) && !raisedPot){
                            logger.info("preflop (bb option, no raise yet): raiser-{}/seat-{}, ap-{}/seat-{}, bb-{}/name-{}, raisedPot-{}", raiser.getName(), seatOf(raiser.getName()), activePlayer.getName(), seatOf(activePlayer.getName()), bbPosition, seats.get(bbPosition).getName(), raisedPot);
                            getActionFromPlayer();
                            activePlayer = nextPlayer();
                            raisedPot = true;
                        }
                    }

                    // dealing flop and bet
                    dealFlop();
                    logger.warn("flop round starts.");
                    // flop
                    initBettingRound("flop");
                    while (!activePlayer.equals(raiser)) {
                        logger.info("flop: raiser-{}/seat-{}, ap-{}/seat-{}, bb-{}/name-{}, raisedPot-{}", raiser.getName(), seatOf(raiser.getName()), activePlayer.getName(), seatOf(activePlayer.getName()), bbPosition, seats.get(bbPosition).getName(), raisedPot);
                        getActionFromPlayer();
                        activePlayer = nextPlayer();
                        if (allPlayerChecked()){
                            logger.info("flop: all players checked, exit round");
                            break;
                        }
                        logger.info("flop: while loop end. ap={}, raiser={}",activePlayer.getName(),raiser.getName());

                    }

                    // dealing turn and bet
                    dealTurn();
                    logger.warn("turn round starts.");
                    // turn
                    initBettingRound("turn");
                    while (!activePlayer.equals(raiser)) {
                        logger.warn("raiser is {}/{}, activePlayer is {}/{}", seatOf(raiser.getName()), raiser.getName(), seatOf(activePlayer.getName()), activePlayer.getName()  );
                        getActionFromPlayer();
                        activePlayer = nextPlayer();
                        logger.warn("turn while loop. raiser is {}/{}, activePlayer is {}/{}", seatOf(raiser.getName()), raiser.getName(), seatOf(activePlayer.getName()), activePlayer.getName()  );

                    }

                    // dealing river and bet
                    dealTurn();
                    logger.warn("river round starts.");
                    // turn
                    initBettingRound("river");
                    while (!activePlayer.equals(raiser)) {
                        logger.warn("raiser is {}/{}, activePlayer is {}/{}", seatOf(raiser.getName()), raiser.getName(), seatOf(activePlayer.getName()), activePlayer.getName()  );
                        getActionFromPlayer();
                        activePlayer = nextPlayer();
                    }




                }
            }
        } catch ( Exception e){
                e.printStackTrace();
        }
    }

    private boolean isBigBlind(Player player) {
        return player == seats.get(bbPosition);
    }

    // get action (blocking) and broadcast the move
    private void getActionFromPlayer(){
        // force post sb
        if (activePlayer.equals(seats.get(sbPosition)) && pot == 0){
            pot += activePlayer.postSmallBlind(sb);
            alertAll(sendPlayerMove("sb", sb));
           raiser = activePlayer;
        // force post bb
        } else if (activePlayer.equals(seats.get(bbPosition)) && pot <= sb){
            pot += activePlayer.postBigBlind(bb);
            alertAll(sendPlayerMove("bb", bb));
            raiser = activePlayer;
            raisedPot = false;
            // block until response from client
        } else {
            waitPlayerCommand(activePlayer);
        }
    }


//    private int getMainPot(){
//
//        for (Player p: seats.values()){
//
//        }
//    }
}
