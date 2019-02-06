package com.kukinet.pkr;

import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

    private ConnectionManager connectionManager;
    private String name;
    private String price;
//    private Map<Player, WebSocket> players;
    private List<Player> players;

    Logger logger = LoggerFactory.getLogger(Game.class);

    public Game(String name, String price, ConnectionManager connectionManager){
        this.name=name;
        this.price=price;
        this.connectionManager = connectionManager;
//        this.players=new HashMap<>();
        this.players=new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public List<Player> getPlayers() {
//        return players;
//    }
public List<Player> getPlayers() {
    return players;
}

//    public void setPlayers(<Player,WebSocket> players) {
//        this.players = players;
//    }
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

//    public void addTestPlayers(){ IF USE - CHANGE LIST TO HASHMAP
//        players.add(new Player("test0"));
//        players.add(new Player("test1"));
//        players.add(new Player("test2"));
//        players.add(new Player("test3"));
//        players.add(new Player("test4"));
//        players.add(new Player("test5"));
//        players.add(new Player("test6"));
//    }

//    // create a player from user
//    public void addPlayer(User user, WebSocket connection) {
////        String s = user.getName();
////        Player p = new Player(s);
////        players.add(p);
//
//        players.put(new Player(user.getName()), connection);
//    }


    public void addPlayer(User user) {
//        String s = user.getName();
        Player player = new Player(user.getName());
        players.add(player);
        connectionManager.setPlayer(user, player);
        //return player;
//        players.add(new Player(user.getName()));
    }


    public void start(){
        logger.info("starting game: {}, with {} players.", name, players.size());

        Table table = new Table(players, connectionManager);

        for (Player player: players){
            player.setTable(table);
        }
        table.playRound();

    }
}
