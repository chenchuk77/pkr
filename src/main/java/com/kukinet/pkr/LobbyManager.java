package com.kukinet.pkr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class LobbyManager {



//    private Map<User, WebSocket> connections;
    private List<User> loggedInUsers;



    private List<Game> games;
    //private List<User> users;

    @Autowired
    private ConnectionManager connectionManager;

    Logger logger = LoggerFactory.getLogger(LobbyManager.class);

    public LobbyManager(){
//        connections = new HashMap<>();
        loggedInUsers = new ArrayList<>();
        games = new ArrayList<>();
        logger.info("lobby-manager initialized.");
    }


    @PostConstruct
    public void init(){
        Game testGame = new Game("testGame", "100", connectionManager);
        //testGame.addTestPlayers();
        games.add(testGame);

    }

//    public String getUsername(WebSocket conn){
//        for (Map.Entry<User, WebSocket> entry : connections.entrySet()) {
//            if (entry.getValue().equals(conn)) {
//                return entry.getKey().getName();
//            }
//        }
//        return null;
//    }

    public void loginUser(User user) {
        loggedInUsers.add(user);
        logger.info("user {} logged-in.", user.getName());
        logger.debug("total logged-in users: {}.", loggedInUsers.size());

    }
//    public void loginUser(User user, WebSocket conn) {
//        connections.put(user, conn);
//        logger.info("user {} logged-in, total {} users.", user.getName(), connections.size());
//    }

    private User getUserByName(String name){
        for (User user: loggedInUsers){
            if (user.getName().equals(name)) return user;
        }
        return null;
    }
//    private User getUserByName(String name){
//        for (User user: connections.keySet()){
//            if (user.getName().equals(name)) return user;
//        }
//        return null;
//    }
    private Game getGameByName(String name){
        for (Game game: games){
            if (game.getName().equals(name)) return game;
        }
        return null;
    }

//    public void registerUser(String username, String gamename){
//        User user = getUserByName(username);
//        Game game = getGameByName(gamename);
//        game.addPlayer(user, connections.get(user));
//        logger.info("user {} registered to game {}.", user.getName(), game.getName());
//    }
    public void registerUser(String username, String gamename){
        User user = getUserByName(username);
        Game game = getGameByName(gamename);
        game.addPlayer(user);
        logger.info("user {} registered to game {}.", user.getName(), game.getName());
    }
//
//    public void call(int amount){
//        User user = getUserByName(username);
//        Game game = getGameByName(gamename);
//        game.addPlayer(user, connections.get(user));
//        logger.info("user {} registered to game {}.", user.getName(), game.getName());
//    }


    public Game createGame(String name, String price, ConnectionManager connectionManager){
        Game game = new Game(name, price, connectionManager);
        games.add(game);
        return game;
    }


    public void startGame(String name){
        for (Game game: games){
            if (game.getName().equals(name)){
                game.start();
                logger.info("game {} started.", game.getName());
            }
        }
    }


}
