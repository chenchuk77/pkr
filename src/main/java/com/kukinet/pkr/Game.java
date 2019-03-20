package com.kukinet.pkr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

public class Game implements Runnable {
    Logger logger = LoggerFactory.getLogger(Game.class);
    private ConnectionManager connectionManager;
    private String name;
    private String price;
    private List<Player> players;


    public Game(String name, String price, ConnectionManager connectionManager){
        this.name=name;
        this.price=price;
        this.connectionManager = connectionManager;
        this.players=new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Player> getPlayers() {
    return players;
}

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


    // create a new player for a given user during registration.
    public void addPlayer(User user) {
        Player player = new Player(user.getName());
        players.add(player);
        connectionManager.setPlayer(user, player);
    }


//    public void start(){
//        logger.info("starting game: {}, with {} players.", name, players.size());
//        Table table = new Table(players, connectionManager);
//        // TODO: why player need to know his table, can be many
//        for (Player player: players){
//            player.setTable(table);
//        }
//        table.playRound();
//    }

    @Override
    public void run() {
        if (players.size() < 4){
            try {
                logger.info("cant start {}. only {} players... waiting 20 sec", name, players.size());
                Thread.sleep(5000);
                logger.info("cant start {}. only {} players... waiting 15 sec", name, players.size());
                Thread.sleep(5000);
                logger.info("cant start {}. only {} players... waiting 10 sec", name, players.size());
                Thread.sleep(5000);
                logger.info("cant start {}. only {} players... waiting 5 sec", name, players.size());
                Thread.sleep(5000);
                logger.info("continue...");



            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.info("starting game: {}, with {} players.", name, players.size());
        Table table = new Table(players, connectionManager);
        // TODO: why player need to know his table, can be many
        for (Player player: players){
            player.setTable(table);
        }
        //table.playRound();
        table.gameLoop();

    }
}
