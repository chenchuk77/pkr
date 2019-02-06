package com.kukinet.pkr;

import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class Table {

    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 4;
    private Logger logger = LoggerFactory.getLogger(Table.class);



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
    private Player player;
    private Player raiser;

//    private List<Player> connections;
//    private Map<Player, WebSocket> connections;
    private List<Player> players;

    private ConnectionManager connectionManager;


    public List<Player> getPlayers() {
        return players;
    }
    public int[] getButtonsPosition(){
        return new int[]{ sbPosition, bbPosition, dealerPosition };
    }

    //public Table(Map<Player, WebSocket> connections){
    public Table(List<Player> players, ConnectionManager connectionManager){
        // create list of players
//        this.connections = connections;
//        this.players = new ArrayList<>(connections.keySet());
        this.connectionManager = connectionManager;
        this.players = players;

        seatPlayers();
        // setting values, initRound() will set the sb to 0, bb to 1, dealer to 8 (9p)
        // setting values, initRound() will set the sb to 0, bb to 1, dealer to 3 (4p)
        this.sbPosition = players.size()-1;
        this.bbPosition = 0;
        this.dealerPosition = players.size()-2;
//        this.sbPosition = 0;
//        this.bbPosition = 1;
//        this.dealerPosition = players.size()-1;
        this.index = 0;
    }

    public void waitPlayerCommand(Player player){
        connectionManager.getPlayerConnetion(player).send("request-player-action");
    }

    public void call(String playerName, String amount){
        if (player.getName().equals(playerName)){
            player.call(Integer.valueOf(amount));
            logger.info("call,{} of {} handled.", amount, playerName);

        }
    }


    private void seatPlayers(){
        if (players.size() < MIN_PLAYERS || players.size() > MAX_PLAYERS){
            System.out.println("error, illegal number of players to seat.");
        }
        //TODO - add this
        //Collections.shuffle(players);
    }

    private Player nextPlayer(){
        if (index == MAX_PLAYERS-1) {
            index = 0;
        } else {
            index++;
        }
        Player p = players.get(index);
//        return players.get(index);
        return p;
    }

    public void newLevel(int ante, int sb, int bb){
        this.ante = ante;
        this.sb = sb;
        this.bb = bb;

    }

    private void initRound(){
        moveButtons();
        bet = 0;
        pot = 0;
        player = players.get(sbPosition);
    }

    private void moveButtons(){
        if (sbPosition == MAX_PLAYERS-1) sbPosition = 0; else sbPosition ++;
        if (bbPosition == MAX_PLAYERS-1) bbPosition = 0; else bbPosition ++;
        if (dealerPosition== MAX_PLAYERS-1) dealerPosition = 0; else dealerPosition ++;

        // TODO: fix empty seats .... if (seats.get(player)){

    }



    // alert all connected players
    private void alert(String message){
        for (Player player : players) {

            WebSocket ws = connectionManager.getPlayerConnetion(player);

            ws.send(message);
        }


//        for (WebSocket ws: connections.values()){
//            ws.send(message);
//        }
    }

    public void playRound(){


        try {

            logger.debug("playRound().");
            newLevel(0, 30, 60);
            initRound();               // set sb, bb, dealer buttons
            player.postSmallBlind(sb);
            alert("player " + player.getName() + " post SB: " + sb);
            player = nextPlayer();
            bet = player.postBigBlind(bb);
            alert("player " + player.getName() + " post BB: " + bb);
            raiser = player;

            player = nextPlayer();
            waitPlayerCommand(player);

        } catch ( Exception e){
            e.printStackTrace();
        }



//        while (!player.equals(raiser)) {
//            if (player.inHand() && player.commited() < bet){
//                Player.waitAction()
//                fold()
//                Player.fold
//                inHand = false
//                call(30)
//                com +30
//                raise(100)
//                com +100
//                raiser = player
//            }
//            nexrPlayer()
//        }
//        pot = collect all bets
//        Bet = 0
//        DealTurn()
//        firstPlayer
//        // true when this player should call/bet
//        while (player.inHand() && player.commited() < maxRaise){
//
//        }


    }

}
