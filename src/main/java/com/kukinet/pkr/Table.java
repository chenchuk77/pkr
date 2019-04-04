package com.kukinet.pkr;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.kukinet.cards.Card;
import com.kukinet.cards.Deck;
import com.kukinet.cards.FakeDeck4pEven;
import com.kukinet.cards.RealDeck;
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
    //private static final int ACTION_WAIT_TIME_SEC = 30;
    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 4;

    private int handNumber;

    private int roundTime = 10 * 60; // 10 minutes for round

    // will be changed dynamically
    private int ante;
    private int sb;
    private int bb;

    private int bet;
//    private int pot;
    private Pot pot;
    private boolean bbHasOption;
    private boolean isCheckAllowed;

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
        this.handNumber = 0;
    }

    public String getBettingRound() {
        return bettingRound;
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
            Player player = players.get(i);
            player.setSeatPosition(i);
            seats.put(i, player);
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
    public void waitPlayerCommand(Player player) {
        CommandValidator validator = new CommandValidator(this, player, pot);
        alertAll(validator.getValidOptions());
        player.setWaitForAction(true);
        logger.info("waiting for player {} to act.", player.getName());

        // block until action command accepted
        for (int i = 0; i < ACTION_WAIT_TIME_SEC; i++) {
            // no command received from client
            if (player.getActionCommand() == null) {
                try {
                    //logger.error("waiting for {} ({}/{})", player.getName(), i, ACTION_WAIT_TIME_SEC);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            // command received from player
            } else {
                ActionCommand cmd = player.getActionCommand();
                logger.info("----- cmd received - validating cmd:{} amound:{}", cmd.getAction(), cmd.getAmount());
                cmd = validator.validate(cmd);
                logger.info("----- cmd post validation cmd:{} amound:{}", cmd.getAction(), cmd.getAmount());

                // if command is invalid goto next iter of for loop
                if (cmd.getAction().equals("invalid")) {
                    logger.info("invalid command received from {}.", player.getName());
                    alert(player, status("illegal move, please try again..."));
                    alertAll(validator.getValidOptions());
                    player.setActionCommand(null);
                    continue; // retry
                }

                // command is valid, need to choose correct action
                if (cmd.getAction().equals("fold")) {
                    fold(player);
                } else if (cmd.getAction().equals("check")) {
                    check(player);
                    player.setChecking(true);
                    raiser = player; // no a raiser but opens the round
                } else if (cmd.getAction().equals("call")) {
                    //call(player, cmd.getAmount());
                    call(player);
                } else if (cmd.getAction().equals("bet")) {
                    bet(player, cmd.getAmount());
                    raiser = player; // no a raiser but opens the round
                } else if (cmd.getAction().equals("raise")) {
                    raise(player, cmd.getAmount());
                    raiser = player;
                } else if (cmd.getAction().equals("allin")) {
                    raise(player, player.getChips() + player.commited());
                    raiser = player;
                }
                break; // exit after successful command execution
            }
        }
        // no command received im time, forcing check/fold.
        if (player.getActionCommand() == null) {
            if (validator.isCheckAllowed()) {
                logger.warn("player {} did not respond in time, assuming check.", player.getName());
                check(player);
            } else {
                logger.warn("player {} did not respond in time, assuming fold.", player.getName());
                fold(player);
                player.sitout(); // TODO: CHECK WHY fold is not enough
            }
        }
        // clear command from player, and allow continue the game
        player.setWaitForAction(false);
        player.setActionCommand(null);
    }

    // --------------- player action commands ----------------
    public void fold(Player player)   {
        player.fold();
        pot.removeBet(player);
        logger.debug("{}:{} folds.", seatOf(player.getName()), player.getName());
        alertAll(status(player.getName() + " fold."));
        alertAll(sendPlayerMove("fold", 0));
    }

    public void check(Player player){
        logger.debug("{}:{} checks.", seatOf(player.getName()), player.getName());
        alertAll(status(player.getName() + " check."));
        alertAll(sendPlayerMove("check", 0));
    }

    // used if no call value provided
    public void call(Player player){
        int callValue = raiser.commited() - player.commited();
        call(player, callValue);
    }

    public void call(Player player, int requestedAmount){
        int amount = player.call(requestedAmount);
        logger.debug("{}:{} calls {}.", seatOf(player.getName()), player.getName(), amount);
        pot.addBet(new Bet(amount, player));
        //pot += amount;
        alertAll(status(player.getName() + " call " + amount + "."));
        alertAll(sendPlayerMove("call", amount));
    }

    public void bet(Player player, int requestedAmount){

        this.isCheckAllowed = false;

        int amount = player.bet(requestedAmount);
        logger.debug("{}:{} bets {}.", seatOf(player.getName()), player.getName(), amount);
        pot.addBet(new Bet(amount, player));
        //pot += amount;
        raiser = player;
        bbHasOption = false;
//        logger.info("finish bet logic: ap {} raiser {}.", activePlayer.getName(), player.getName());
        alertAll(status(player.getName() + " bet " + amount + "."));
        alertAll(sendPlayerMove("bet", amount));
    }

    public void raise(Player player, int requestedAmount){

        if (pot.hasPlayerBet(player)){
            requestedAmount = requestedAmount - pot.getPlayerBetAmount(player);
//            logger.info("player {} will raise by {}.", player.getName(), requestedAmount);
        }

        this.isCheckAllowed = false;

        int amount = player.raise(requestedAmount);
//        logger.info("player {} raise {}.", player.getName(), amount);
//        logger.info("player {} raise {}.", player.getName(), player.commited());
//        logger.debug("{}:{} raises to {}.", seatOf(player.getName()), player.getName(), player.commited());

        pot.addBet(new Bet(amount, player));
//        pot += amount;
        raiser = player;
        bbHasOption = false;
//        alertAll(status(player.getName() + " raise to " + amount + "."));
        logger.debug("{}:{} raises to {}.", seatOf(player.getName()), player.getName(), player.commited());

        alertAll(status(player.getName() + " raise to " + player.commited() + "."));
//        alertAll(sendPlayerMove("raise", amount));
        alertAll(sendPlayerMove("raise", player.commited()));
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

    // if only 1 player reminded with chips - no need to bet (showdown anyway)
    private boolean betRoundNeeded(){
        int numOfPlayersWithChipsBehind = 0;
        for (Map.Entry<Integer, Player> entry: seats.entrySet()){
            if (entry.getValue().inHand() && entry.getValue().getChips() > 0 ) {
                numOfPlayersWithChipsBehind ++;
            }
        }
        return numOfPlayersWithChipsBehind > 1;
    }

    private void removeLosers(){
        int numOfPlayersWithChipsBehind = 0;
        for (Map.Entry<Integer, Player> entry: seats.entrySet()){
            if (entry.getValue().getChips() <= 0 ) {
                entry.getValue().setInGame(false);
                logger.info("player {} is out.", entry.getValue().getName());
            }
        }
    }

    // pointing to next player to act
    private Player nextPlayer(){

        int index = seatOf(activePlayer.getName());
        logger.info("nextplayer func: starting with ap:{} index:{}", activePlayer.getName(), index);
        if (index == MAX_PLAYERS-1) {
            index = 0;
        } else {
            index++;
        }
//        Player p = seats.get(index);
        activePlayer = seats.get(index);
        logger.info("checking player:{} ", activePlayer.getName());

        // recursively look for the next inHand player (skip if no chips)
//        if (!activePlayer.inHand() || activePlayer.getChips() <= 0){
        if (!activePlayer.inHand()){
        //while (!activePlayer.inHand()){
                logger.info("player:{} / chips:{} inhand:{} trying next...", activePlayer.getName(), activePlayer.getChips(), activePlayer.inHand());
            activePlayer = nextPlayer();
        }
        logger.info("ap found - player {} (inhand:{})", activePlayer.getName(), activePlayer.inHand());

        return activePlayer;
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

    private void endRound(){
        this.handNumber ++;
        this.communityCards = null;
        this.deck = new FakeDeck4pEven();
        this.pot = new Pot(new ArrayList<Player>(seats.values()));
        for (Player p: seats.values()){
            // remove losing players
            if (p.getChips() <= 0) p.setInGame(false);
            if (p.getChips() <= 0) p.setInHand(false);
            // clear hands
            if (p.inGame()){
                p.setInHand(true);
                p.muckCards();
            }
        }
//        private void initHand(){
//            this.handNumber ++;
//
////        alertAll(status("newhand");
//            this.communityCards = null;
//            this.realDeck = new RealDeck();
//            this.pot = new Pot(new ArrayList<Player>(seats.values()));
//            for (Player p: seats.values()){
//                if (p.inGame()){
//                    p.setInHand(true);
////                p.setHoleCard1(null);
////                p.setHoleCard2(null);
////                p.setHand(null);
//                    p.muckCards();
//                }
//            }

        moveButtons();
        this.bet = 0;
//        this.pot = 0;
        initBettingRound("preflop");
    }

    // starting from sb (if sb not in hand, goto the next inhand player)
    private void setFirstPlayerToAct(){
        int fp = sbPosition;
        logger.info("looking for starter , sb-{}.", sbPosition);

        for (int i=0; i<MAX_PLAYERS; i++){
            // after seat8 comes seat0
            if (fp + i >=MAX_PLAYERS ){
                fp = fp + i - MAX_PLAYERS;
            }
            activePlayer = seats.get(fp+i);
            logger.info("trying player-{} inhand-{}.", activePlayer.getName(),activePlayer.inHand());

            if (activePlayer.inGame() && activePlayer.inHand()){
                logger.info("find starter: player-{} .", activePlayer.getName());
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

//private void preparePlayersForNextHand(){
//    logger.info("preparing for next hand, clearing cards and remove losing players.");
//    for (Player p: seats.values()){
//        p.setHoleCard1(null);
//        p.setHoleCard2(null);
//
//        if (p.getChips() <= 0) p.setInGame(false);
//        if (p.inGame()) p.setInHand(true);
//
////            // the winner already handeled
////            if (! p.equals(activePlayer)){
////                p.setStartingStack(p.getChips());
////                logger.info("{} is not winner, start-{} eff-{}", p.getName(), p.getStartingStack(), p.getChips());
////            }
//        }
//    }


    private void initBettingRound(String bettingRound){
        this.bettingRound = bettingRound;
//        activePlayer = seats.get(sbPosition);
//        index = sbPosition;
        setFirstPlayerToAct();
        //preparePlayersForNextHand();
        for (Player p: seats.values()){
            p.setCommited(0);
            p.setChecking(false);
        }
        raiser = new Player(); // just for not null, overridden by bb anyway
        bbHasOption = false;      // will be false by bb to allow complete round

        alertAll(seats()); // update all clients

    }

//    private
    private void moveButtons(){
        if (sbPosition == MAX_PLAYERS-1) sbPosition = 0;
        else do sbPosition ++; while (!seats.get(sbPosition).inHand());

        if (bbPosition == MAX_PLAYERS-1) bbPosition = 0;
        else do bbPosition ++; while (!seats.get(bbPosition).inHand());

        if (dealerPosition== MAX_PLAYERS-1) dealerPosition = 0;
        else do dealerPosition ++; while (!seats.get(dealerPosition).inHand());
        // TODO: fix empty seats .... if (seats.get(activePlayer)){
    }

    private void dealTurn(){
        deck.pop(); // remove one card from the realDeck before dealing turn
        Card turn = deck.pop();
        communityCards.add(turn);
        logger.debug("turn: {}", turn.toString());
        sendCommunityCardsUpdate();
    }

    private void dealRiver(){
        deck.pop(); // remove one card from the realDeck before dealing river
        Card river = deck.pop();
        communityCards.add(river);
        logger.debug("river: {}", river.toString());
        sendCommunityCardsUpdate();
    }

    private void dealFlop(){
        deck.pop(); // remove one card from the realDeck before dealing flop
        communityCards = new ArrayList<>();
        Card f1 = deck.pop();
        Card f2 = deck.pop();
        Card f3 = deck.pop();
        communityCards.add(f1); // 0
        communityCards.add(f2); // 1
        communityCards.add(f3); // 2
        logger.debug("flop: {} {} {}", f1.toString(), f2.toString(), f3.toString());
        sendCommunityCardsUpdate();
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
        logger.debug("dealing hands.");
        for (Player player: seats.values()){ // deal first card for each player
            if (player.inGame()){
                Card c1 = deck.pop();
                player.setHoleCard1(c1);
                connectionManager.getPlayerWebsocket(player).send(setFirstPocketCard(player));
            } else {
                //logger.debug("skipping {} (not inGame).", player.getName());
                player.setHoleCard1(null);
                player.setHoleCard2(null);
            }
        }
        for (Player player: seats.values()){ // deal second card for each player
            if (player.inGame()){
                Card c2 = deck.pop();
                player.setHoleCard2(c2);
                connectionManager.getPlayerWebsocket(player).send(setWholePocketCards(player));
            } else {
                //logger.debug("skipping {} (not inGame).", player.getName());
                player.setHoleCard1(null);
                player.setHoleCard2(null);
            }
        }
        for (Player player: seats.values()){ // deal second card for each player
            if (player.inGame()){
                logger.debug("{}:{} dealt \t {} {}.", seatOf(player.getName()), player.getName(), player.getHoleCard1(), player.getHoleCard2());

            }
        }
            // LOG CARDS logger.debug("skipping {} (not inGame).", player.getName());

    }

    public String seats(){
        Gson gsonBuilder = new GsonBuilder().create();
        String json = gsonBuilder.toJson(seats);
        return json ;
    }

    public String potShareAllFolded() {

//        Gson gsonBuilder = new GsonBuilder().create();
//        String json = gsonBuilder.toJson(pot.singlePlayerWin());
//        return json;
        JsonObject potshare = new JsonObject();
        potshare.addProperty("type", "potshare");

        Map<Player,Integer> chipshare = pot.singlePlayerWin();
        for (Player p: chipshare.keySet()){
            potshare.addProperty("player", p.getName());
            potshare.addProperty("pot", chipshare.get(p));

        }
        return potshare.toString();
    }

//        JsonObject potshare = new JsonObject();
//        bets.addProperty("ante", ante);
//        bets.addProperty("sb", sb);
//        bets.addProperty("bb", bb);
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

    public String status(String statusMessage) {
        JsonObject messageJSON = new JsonObject();
        messageJSON.addProperty("type", "status");
        messageJSON.addProperty("value", statusMessage);
        return messageJSON.toString();
    }

    public String setWholePocketCards(Player player) {
        JsonObject pocketCardsJSON = new JsonObject();
        pocketCardsJSON.addProperty("type", "cards");

        for (Map.Entry<Integer, Player> entry: seats.entrySet()){
            if (entry.getValue().equals(player)){
                pocketCardsJSON.addProperty("seat", entry.getKey());
            }
        }
        pocketCardsJSON.addProperty("card1", player.getHoleCard1().toString());
        pocketCardsJSON.addProperty("card2", player.getHoleCard2().toString());
        return pocketCardsJSON.toString();
    }

    public String setFirstPocketCard(Player player) {
        JsonObject pocketCardsJSON = new JsonObject();
        pocketCardsJSON.addProperty("type", "cards");

        for (Map.Entry<Integer, Player> entry: seats.entrySet()){
            if (entry.getValue().equals(player)){
                pocketCardsJSON.addProperty("seat", entry.getKey());
            }
        }
        pocketCardsJSON.addProperty("card1", player.getHoleCard1().toString());
        return pocketCardsJSON.toString();
    }

    // creating a potshare JSON
    private String potShare() {
        // get the map of player->amount win
        Map<Player ,Integer> potShareMap = pot.splitPot();

//        for (Map.Entry<Player, Integer> entry: potShareMap.entrySet()){
//            logger.info("+++++ k={}", entry.getKey());
//            logger.info("+++++ v={}", entry.getValue());
//        }

        logger.info("--------in potshare. {}", potShareMap.keySet().toString());
        logger.info("--------in potshare. {}", potShareMap.values().toString());
        logger.info("--------in potshare. {}", potShareMap.values().toString());

        JsonObject potshareJSON = new JsonObject();
        potshareJSON.addProperty("type", "potshare");
        for (Map.Entry<Player, Integer> entry: potShareMap.entrySet()){
            logger.info("+++++ k={}", entry.getKey());
            logger.info("+++++ v={}", entry.getValue());
            potshareJSON.addProperty(entry.getKey().getName(), entry.getValue());
        }




//        for (Player p: potShareMap.keySet()){
//            logger.info("--------winner: {}", p.getName());
//            logger.info("--------value in map: {}", potShareMap.get(p));
//
//            potshareJSON.addProperty(p.getName(), potShareMap.get(p));
//        }
        return potshareJSON.toString();
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
//        playerMove.addProperty("pot", pot);
        playerMove.addProperty("pot", pot.getAllBets());
        return playerMove.toString();
    }

    public String showdown(){
        JsonObject showdownJSON = new JsonObject();
        showdownJSON.addProperty("type", "showdown");
        for (Map.Entry<Integer, Player> entry: seats.entrySet()){
            Player p = entry.getValue();
            if (p.inHand()){
                String hand = p.getHoleCards(); // as 4 digit string
                showdownJSON.addProperty(p.getName(), hand);
            }
        }
        return showdownJSON.toString();
    }

    public void rankHands(){
        logger.info("ranking hands...");

        // collect all community cards
        String communityCardsString = "";
        for (Card card: communityCards){
            communityCardsString += card.toString();
        }
        logger.info("communityCardsString: {}", communityCardsString);

        // append player hole cards and rank hand
        for (Player p: seats.values()){
            if (p.inHand()){
                String playerCardsString = communityCardsString + p.getHoleCards();
                Hand playerHand = Dealer.rankHand(playerCardsString);
                p.setHand(playerHand);

                // spacing for printing only
                String cardsWithSpaces = "";
                for (int i=0; i<playerCardsString.length(); i+=2){
                    cardsWithSpaces+=playerCardsString.substring(i,i+2) + " ";
                }


                logger.warn("{}:{} shows {} (value:{}) cards:{}",
                        seatOf(p.getName()),
                        p.getName(),
                        p.getHand().getHandName(),
                        p.getHand().getValue(),
                        cardsWithSpaces);
//                p.setHandValue
            }
        }
        logger.info("finish ranking hands.");

    }

//



    // alertAll all connected players
    private void alert(Player player, String message){
        WebSocket ws = connectionManager.getPlayerWebsocket(player);
        ws.send(message);
    }

    // alertAll all connected players
    private void alertAll(String message){
        logger.info("alerting all: {}", message);
        //for (Player activePlayer : players) {
        for (Player player: seats.values()){
            //logger.warn("player-{}, ws-{}", player.getName(), connectionManager.getPlayerWebsocket(player));
            WebSocket ws = connectionManager.getPlayerWebsocket(player);
            ws.send(message);
        }
    }

    public int getBigBlind(){
        return this.bb;
    }

    public void gameLoop(){
        boolean endRound = false;
        try {
            initGame();
            endRound();
            this.isCheckAllowed = true;

            // more than 1 player in hand ( unfolded ), exit when all folds
            while (playersInHand() > 1) {
//                logger.warn("new hand with {}/{} players (inhand/ingame).", playersInHand(), playersInGame());
                logger.trace("starting hand number {} with {}/{} players (inhand/ingame)", handNumber, playersInHand(), playersInGame());
                alertAll(status("new hand # " + handNumber));
                Thread.sleep(3000);
                alertAll(seats());
                alertAll(betAmounts());
                alertAll(buttonsPos());

                // dealing hands and betting preflop
                dealHands();
                // preflop
                logger.debug("preflop betting round started.");
                while (!activePlayer.equals(raiser) && playersInHand()>1) {
//                    logger.info("in hand players: {}",playersInHand());
//                    logger.info("preflop: raiser-{}/seat-{}, ap-{}/seat-{}, bb-{}/name-{}, bbHasOption-{}", raiser.getName(), seatOf(raiser.getName()), activePlayer.getName(), seatOf(activePlayer.getName()), bbPosition, seats.get(bbPosition).getName(), bbHasOption);
                    getActionFromPlayer();
                    activePlayer = nextPlayer();
                    // bb option. if no raise
                    if (isBigBlind(activePlayer) && bbHasOption && playersInHand() >= 2){
//                        logger.info("preflop: (bb option, no raise yet): raiser-{}/seat-{}, ap-{}/seat-{}, bb-{}/name-{}, bbHasOption-{}", raiser.getName(), seatOf(raiser.getName()), activePlayer.getName(), seatOf(activePlayer.getName()), bbPosition, seats.get(bbPosition).getName(), bbHasOption);
                        getActionFromPlayer();
                        if (activePlayer.isChecking()){
                            break;
                        }
                        activePlayer = nextPlayer();
                        bbHasOption = false;
                    }
                }
                if (allOtherFolded()){
                    activePlayer.setChips(activePlayer.getChips() + pot.getAllBets());
//                    logger.info("preflop: raiser-{}/seat-{} didnt get called", raiser.getName(), seatOf(raiser.getName()));
                    logger.debug("all players folded.");
                    alertAll(potShareAllFolded());
                    //alertAll(status(potShareAllFolded()));
                    Thread.sleep(3000);

                    endRound();
//                    logger.info("preflop: round end, no callers.");
                    continue;
                }
                pot.clearBetsIfAllCalled();
                pot.refundUncoveredBet();

                // dealing flop and bet
                this.isCheckAllowed = true;
                logger.debug("dealing flop.");
                alertAll(status("dealing flop."));
                dealFlop();
                Thread.sleep(3000);
                if (betRoundNeeded()){
                    initBettingRound("flop");
                    while (!activePlayer.equals(raiser)) {
                        logger.info("flop: raiser-{}/seat-{}, ap-{}/seat-{}, bb-{}/name-{}, bbHasOption-{}", raiser.getName(), seatOf(raiser.getName()), activePlayer.getName(), seatOf(activePlayer.getName()), bbPosition, seats.get(bbPosition).getName(), bbHasOption);
                        getActionFromPlayer();
                        activePlayer = nextPlayer();
                        logger.info("flop: ap is now {}/seat-{}",activePlayer.getName(), seatOf(activePlayer.getName()));
                        if (allPlayerChecked()){
                            logger.info("flop: all players checked, exit round");
                            break;
                        }
                    }
                }
                if (allOtherFolded()){
                    activePlayer.setChips(activePlayer.getChips() + pot.getAllBets());
                    logger.info("preflop: raiser-{}/seat-{} didnt get called", raiser.getName(), seatOf(raiser.getName()));
                    alertAll(potShareAllFolded());
//                    alertAll(status(potShareAllFolded()));
                    Thread.sleep(3000);

                    endRound();
                    logger.info("preflop: round end, no callers.");
                    continue;
                }
                pot.clearBetsIfAllCalled();
                pot.refundUncoveredBet();

                // dealing turn and bet
                this.isCheckAllowed = true;
                logger.debug("dealing turn.");

                alertAll(status("dealing turn"));
                dealTurn();
                Thread.sleep(3000);
                if (betRoundNeeded()){
                    initBettingRound("turn");
                    while (!activePlayer.equals(raiser)) {
                        logger.info("turn: raiser is {}/{}, activePlayer is {}/{}", seatOf(raiser.getName()), raiser.getName(), seatOf(activePlayer.getName()), activePlayer.getName()  );
                        getActionFromPlayer();
                        activePlayer = nextPlayer();
                        logger.info("turn: ap is now {}/seat-{}",activePlayer.getName(), seatOf(activePlayer.getName()));
                        if (allPlayerChecked()){
                            logger.info("turn: all players checked, exit round");
                            break;
                        }
                    }
                }
                if (allOtherFolded()){
                    activePlayer.setChips(activePlayer.getChips() + pot.getAllBets());
                    logger.info("preflop: raiser-{}/seat-{} didnt get called", raiser.getName(), seatOf(raiser.getName()));
                    logger.debug("all players folded.");
                    alertAll(potShareAllFolded());
//                    alertAll(status(potShareAllFolded()));
                    Thread.sleep(3000);

                    endRound();
                    logger.info("preflop: round end, no callers.");
//                    endRound();
                    continue;
                }
                pot.clearBetsIfAllCalled();
                pot.refundUncoveredBet();

                // dealing river and bet
                this.isCheckAllowed = true;
                alertAll(status("dealing river"));
                dealRiver();
                Thread.sleep(3000);

                if (betRoundNeeded()){
                    initBettingRound("river");
                    while (!activePlayer.equals(raiser)) {
                        logger.info("river: raiser is {}/{}, activePlayer is {}/{}", seatOf(raiser.getName()), raiser.getName(), seatOf(activePlayer.getName()), activePlayer.getName()  );
                        getActionFromPlayer();
                        activePlayer = nextPlayer();
                        logger.info("river: ap is now {}/seat-{}",activePlayer.getName(), seatOf(activePlayer.getName()));
                        if (allPlayerChecked()){
//                            logger.info("river: all players checked.");
                            logger.debug("all players checked.");

                            break;
                        }
                    }
                }
                // showdown because someone is allin // TODO: same as river while loop
//                    if (!betRoundNeeded()) {
                logger.debug("going to showdown");
                alertAll(status("going into showdown"));
                alertAll(showdown());
                rankHands();
                alertAll(potShare());
//                alertAll(status(potShare()));
                Thread.sleep(3000);


//                    }
                // if reach here, maybe some player down, exit the loop for checking ingame
                //removeLosers();
                //break;
                endRound();
            }
//            }
        } catch ( Exception e){
                e.printStackTrace();
        }
    }


//    private void showDown() { // TODO
//        logger.warn("------ SHOWDOWN");
//// collect all community cards
//        String communityCardsString = "";
//        for (Card card: communityCards){
//            communityCardsString += card.toString();
//        }
//        logger.warn("communityCardsString: {}", communityCardsString);
//
//        // append player hole cards and rank hand
//        for (Player p: seats.values()){
//            if (p.inHand()){
//                String playerCardsString = communityCardsString + p.getHoleCards();
//                Hand playerHand = Dealer.rankHand(playerCardsString);
//                logger.warn("player:{} has:{} (rank:{}) cards:{}", p.getName(), playerHand.getHandName(),
//                        playerHand.getHandValue(), playerCardsString);
//                pot.bets.getzzzzzzzzzzz
//                b.setHandValue(playerHand.getHandValue());
//
//            }
////            for (Bet b: pot.bets){ //// players maybe inhand but pot cleared !!!
//        }

//        // append player hole cards and rank hand
//        for (Bet b: pot.bets){
////            for (Bet b: pot.bets){ //// players maybe inhand but pot cleared !!!
//            String playerCardsString = communityCardsString + b.getPlayer().getHoleCards();
//            Hand playerHand = Dealer.rankHand(playerCardsString);
//            logger.warn("player:{} has:{} (rank:{}) cards:{}", b.getPlayer().getName(), playerHand.getHandName(),
//                    playerHand.getHandValue(), playerCardsString);
//            b.setHandValue(playerHand.getHandValue());
//        }




//    }

    private boolean allOtherFolded(){
        return playersInHand() == 1;
    }

    public boolean isBigBlind(Player player) {
        return player == seats.get(bbPosition);
    }

    // get action (blocking) and broadcast the move
    private void getActionFromPlayer(){
        // force post sb

//        boolean isCompleted = true;

        if (activePlayer.equals(seats.get(sbPosition)) && pot.getAllBets() == 0){
            int posted_sb = activePlayer.postSmallBlind(sb);
            logger.debug("{}:{} post small blind ({}).", seatOf(activePlayer.getName()), activePlayer.getName(), posted_sb);

            pot.addBet(new Bet(posted_sb, activePlayer));
//            pot.addBet(new Bet(activePlayer.postSmallBlind(sb), activePlayer));
            alertAll(sendPlayerMove("sb", sb));
            raiser = activePlayer;
        // force post bb
        } else if (activePlayer.equals(seats.get(bbPosition)) && pot.getAllBets() <= sb){
            int posted_bb = activePlayer.postBigBlind(bb);
            logger.debug("{}:{} post big blind ({}).", seatOf(activePlayer.getName()), activePlayer.getName(), posted_bb);
            pot.addBet(new Bet(posted_bb, activePlayer));
            alertAll(sendPlayerMove("bb", bb));
            raiser = activePlayer;
            bbHasOption = true;
            // block until response from client
        } else {
            waitPlayerCommand(activePlayer);
//            isCompleted = waitPlayerCommand(activePlayer);
        }

//        return isCompleted;
    }

    // reply to a client request for full update (after reconnect)
    public void updateClient(Player player) {
        logger.warn("sending full updates for player {}.", player.getName());
        alert(player, seats());
        alert(player, betAmounts());
        alert(player, buttonsPos());
        alert(player, setWholePocketCards(player));
    }
}
