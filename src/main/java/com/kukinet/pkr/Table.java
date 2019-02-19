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
//    private List<Player> players;
    private ConnectionManager connectionManager;


    public Table(List<Player> players, ConnectionManager connectionManager){
        this.connectionManager = connectionManager;
        //this.players = players;


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
//            System.out.println("error, illegal number of players to seat.");
            logger.warn("illegal number of players to seat ({}).", players.size());
        }
        //TODO - add this
        //Collections.shuffle(players);

        // seat the players from 0-9 (or 0-4 in testing)
        this.seats = new HashMap<>();
        for (int i=0; i<MAX_PLAYERS; i++){
            seats.put(i, players.get(i));
        }
    }

//    public List<Player> getPlayers() {
//        return players;
//    }

//    public int[] getButtonsPosition(){
//        return new int[]{ sbPosition, bbPosition, dealerPosition };
//    }





//TEMP::




//
//
//    // wait for player
//    public void waitPlayerCommand(Player player){
//        player.setWaitForAction(true);
//        logger.info("waiting for move from {}.", player.getName());
//
//        // block until action command accepted
//        for (int i=0; i<ACTION_WAIT_TIME_SEC; i++){
//            // no command received from client
//            if (player.getActionCommand() == null) {
//                try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
//            } else {
//                ActionCommand cmd = player.getActionCommand();
//                if (cmd.getAction().equals("move-right")){
//                    player.moveRight();}
//                player.setWaitForAction(false);
//                player.setActionCommand(null);
//                return;
//            }
//        }
//        // if no command received in time
//        logger.info("no response in {} from {}.", ACTION_WAIT_TIME_SEC,player.getName());
//        player.setWaitForAction(false);
//        player.setActionCommand(null);
//        player.doNothing();
//











//TEMP











    private int seatOf(String name){
        for (Map.Entry<Integer, Player> entry: seats.entrySet()){
            if (entry.getValue().getName().equals(name)){
                return entry.getKey();
            }
        }
        return 99;
    }








        // wait for player, but notify all
    public void waitPlayerCommand(Player player){
        JsonObject requestPlayerAction = new JsonObject();

        requestPlayerAction.addProperty("command", "waitaction");
        requestPlayerAction.addProperty("player", player.getName());
        //alert(player, requestPlayerAction.toString());
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
                if (cmd.getAction().equals("check")){
                    check(player);
                }
                ActionCommand cmd = player.getActionCommand();
                if (cmd.getAction().equals("fold")){
                    fold(player);
                }
                if (cmd.getAction().equals("call")){
                    call(player, cmd.getAmount());
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


//        while(player.isWaitForAction()){
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            ACTION_WAIT_TIME_SEC
//
//                    if elapsed make fold cmd
//                    break
//        }
//        player.setWaitForAction(false);


        //alertAll(requestPlayerAction.toString());
//        connectionManager.getPlayerConnetion(player).send("waitaction");
    }
//    public void fold(String playerName, int requestedAmount)   {
//        if (activePlayer.getName().equals(playerName)) {
//            int amount = activePlayer.call(Integer.valueOf(requestedAmount));
//        }
//    }
    public void fold(Player player)   {
        player.fold();
        logger.info("player {} fold.", player.getName());
        alertAll(playerMove("fold", 0));


//        JsonObject playerMoveUpdate = new JsonObject();
//        playerMoveUpdate.addProperty("command", "fold");
//        playerMoveUpdate.addProperty("amount", 0);
//        playerMoveUpdate.addProperty("player", player.getName());
//        alertAll(playerMoveUpdate.toString());

//        if (activePlayer.getName().equals(playerName)) {
//            int amount = activePlayer.call(Integer.valueOf(requestedAmount));
//        }
    }
//    public void call(String playerName, int requestedAmount){
//        if (activePlayer.getName().equals(playerName)){
//            int amount = activePlayer.call(requestedAmount);
//
//            JsonObject playerMoveUpdate = new JsonObject();
//            playerMoveUpdate.addProperty("command", "call");
//            playerMoveUpdate.addProperty("amount", amount);
//            playerMoveUpdate.addProperty("player", playerName);
//            logger.info("call,{} of {} handled.", requestedAmount, playerName);
//            alertAll(playerMoveUpdate.toString());
//        }
//    }

    public void call(Player player, int requestedAmount){
        int amount = player.call(requestedAmount);
        logger.info("player {} call {}.", player.getName(), amount);
        pot += amount;
        alertAll(playerMove("call", amount));

//        JsonObject playerMoveUpdate = new JsonObject();
//        playerMoveUpdate.addProperty("command", "call");
//        playerMoveUpdate.addProperty("amount", amount);
//        playerMoveUpdate.addProperty("player", player.getName());
//        alertAll(playerMoveUpdate.toString());
    }
    public void raise(Player player, int requestedAmount){
        int amount = player.raise(requestedAmount);
        logger.info("player {} raise {}.", player.getName(), amount);
        pot += amount;
        alertAll(playerMove("raise", amount));

//        JsonObject playerMoveUpdate = new JsonObject();
//        playerMoveUpdate.addProperty("command", "raise");
//        playerMoveUpdate.addProperty("amount", amount);
//        playerMoveUpdate.addProperty("player", player.getName());
//        alertAll(playerMoveUpdate.toString());
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

//    private int playersInHand(){
//        int count = 0;
//        for (Player p: players){
//            if (p.inHand()) count++;
//        }
//        return count;
//    }


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



    private void initGame(){
        newLevel(0, 30, 60);
        initHand();
    }

    private void initHand(){
        moveButtons();
        this.bet = 0;
        this.pot = 0;
        initBettingRound();
    }

    private void initBettingRound(String bettingRound){
        this.bettingRound = bettingRound;
        activePlayer = seats.get(sbPosition);
        raiser = new Player(); // just for not null, overridden by bb anyway

    }
    // default round
    private void initBettingRound(){
        initBettingRound("preflop");
    }

    private void newLevel(int ante, int sb, int bb){
        this.ante = ante;
        this.sb = sb;
        this.bb = bb;
    }
    private void moveButtons(){
        if (sbPosition == MAX_PLAYERS-1) sbPosition = 0; else sbPosition ++;
        if (bbPosition == MAX_PLAYERS-1) bbPosition = 0; else bbPosition ++;
        if (dealerPosition== MAX_PLAYERS-1) dealerPosition = 0; else dealerPosition ++;
        // TODO: fix empty seats .... if (seats.get(activePlayer)){
    }

    private void dealFlop(){
        communityCards = new ArrayList<>();
        Card f1 = deck.pop();
        Card f2 = deck.pop();
        Card f3 = deck.pop();
        communityCards.add(f1);
        communityCards.add(f2);
        communityCards.add(f3);

        JsonObject communityCards = new JsonObject();
        communityCards.addProperty("type", "community");
        communityCards.addProperty("flop1", f1.toString());
        communityCards.addProperty("flop2", f2.toString());
        communityCards.addProperty("flop3", f3.toString());
        alertAll(communityCards.toString());

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

       // Map<Integer, Player> seats;

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

    public String playerMove(String command, int value){
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


//    Gson gsonBuilder = new GsonBuilder().create();
//        String json = gsonBuilder.toJson(seats);
//        return json ;
//    }


//    public String getTableState(){
//        StringBuilder playersState = new StringBuilder();
//        playersState.append("players=[");
//        //for (Player activePlayer: players){
//        for (Player activePlayer: seats.values()){
//
//            playersState.append(activePlayer.getPlayerState()).append(".");
//        }
//        playersState.setLength(playersState.length() - 1); // remove last dot
//
//        playersState.append("]").append(",")
//                .append("buttons=").append(sbPosition).append(bbPosition).append(dealerPosition).append(",")
//                .append("ante=").append(ante).append(",")
//                .append("sb=").append(sb).append(",")
//                .append("bb=").append(bb);
//
//
//        return playersState.toString();
//    }


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
                    dealHands();
                    // preflop
                    logger.warn("preflop round starts.");
                    while (!activePlayer.equals(raiser)) {
                        logger.warn("raiser is {}/{}, activePlayer is {}/{}", seatOf(raiser.getName()), raiser.getName(), seatOf(activePlayer.getName()), activePlayer.getName()  );
                        getActionFromPlayer();
                        activePlayer = nextPlayer();
                    }
                    dealFlop();
                    logger.warn("flop round starts.");
                    // flop
                    initBettingRound("flop");


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

//                pot = activePlayer.postSmallBlind(sb);
//                alertAll(playerMove("sb", 30));
//                alertAll(buttonsPos());
//                activePlayer = nextPlayer();
//                pot += activePlayer.postBigBlind(bb);
//                alertAll(playerMove("bb", 60));
//                alertAll(buttonsPos());
//
//
//                raiser = activePlayer;
//                alertAll(buttonsPos());
//                activePlayer = nextPlayer();
//                waitPlayerCommand(activePlayer);
//                activePlayer = nextPlayer();
//                waitPlayerCommand(activePlayer);



    // 1 action
    private void getActionFromPlayer(){
        // force post sb
        if (activePlayer.equals(seats.get(sbPosition)) && pot == 0){
            pot += activePlayer.postSmallBlind(sb);
            alertAll(playerMove("sb", sb));
           raiser = activePlayer;
        // force post bb
        } else if (activePlayer.equals(seats.get(bbPosition)) && pot <= sb){
            pot += activePlayer.postBigBlind(bb);
            alertAll(playerMove("bb", bb));
            raiser = activePlayer;

            // block until response from client
        } else {
            waitPlayerCommand(activePlayer);
        }




    }



//    public void playRound(){
//        try {
//            logger.debug("playRound() called");
//            alertAll(seats());
//            logger.warn("------------------------------------");
//
//            deck = new Deck();
//            initHand();               // set sb, bb, dealer buttons
//            alertAll(betAmounts());
//            alertAll(buttonsPos());
//            pot = activePlayer.postSmallBlind(sb);
//            alertAll(playerMove("sb", 30));
////            alertAll(activePlayer.getName() + "," + sb + ",sb");
//            alertAll(buttonsPos());
//            alertAll(seats());
//            activePlayer = nextPlayer();
//            pot += activePlayer.postBigBlind(bb);
//            alertAll(playerMove("bb", 60));
//
////            alertAll(activePlayer.getName() + "," + bb + ",bb");
//            alertAll(buttonsPos());
//            alertAll(seats());
//            raiser = activePlayer;
//            dealHands();
//            alertAll(buttonsPos());
//            alertAll(seats());
//            activePlayer = nextPlayer();
//            waitPlayerCommand(activePlayer);
//            activePlayer = nextPlayer();
//            waitPlayerCommand(activePlayer);
//
//
//        } catch ( Exception e){
//            e.printStackTrace();
//        }



//        while (!activePlayer.equals(raiser)) {
//            if (activePlayer.inHand() && activePlayer.commited() < bet){
//                Player.waitAction()
//                fold()
//                Player.fold
//                inHand = false
//                call(30)
//                com +30
//                raise(100)
//                com +100
//                raiser = activePlayer
//            }
//            nexrPlayer()
//        }
//        pot = collect all bets
//        Bet = 0
//        DealTurn()
//        firstPlayer
//        // true when this activePlayer should call/bet
//        while (activePlayer.inHand() && activePlayer.commited() < maxRaise){
//
//        }


//    }

}
