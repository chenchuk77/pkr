package com.kukinet.pkr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Component
public class LobbyManager {
    Logger logger = LoggerFactory.getLogger(LobbyManager.class);
    private List<User> loggedInUsers;
    private List<Game> games;

    @Autowired
    private ConnectionManager connectionManager;

    public LobbyManager(){
        loggedInUsers = new ArrayList<>();
        games = new ArrayList<>();
        logger.info("lobby-manager initialized.");
    }


    @PostConstruct
    public void init(){
        Game testGame = new Game("testGame", "100", connectionManager);
        games.add(testGame);
        startGame("testGame");
    }

    public void loginUser(User user) {
        loggedInUsers.add(user);
        logger.info("user {} logged-in.", user.getName());
        logger.debug("total logged-in users: {}.", loggedInUsers.size());

    }

    private User getUserByName(String name){
        for (User user: loggedInUsers){
            if (user.getName().equals(name)) return user;
        }
        return null;
    }

    private Game getGameByName(String name){
        for (Game game: games){
            if (game.getName().equals(name)) return game;
        }
        return null;
    }

    public void registerUser(String username, String gamename){
        User user = getUserByName(username);
        Game game = getGameByName(gamename);
        game.addPlayer(user);
        logger.info("user {} registered to game {}.", user.getName(), game.getName());
    }

    public Game createGame(String name, String price, ConnectionManager connectionManager){
        Game game = new Game(name, price, connectionManager);
        games.add(game);
        return game;
    }

    // game scheduler
    public void startGame(String name){
        for (Game game: games){
            if (game.getName().equals(name)){


                int delay = 12;
                ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
                logger.info("scheduling {} to start in {} seconds.", name, delay);
                scheduler.schedule(game, delay, TimeUnit.SECONDS);
                scheduler.shutdown(); // TODO: check if necessary
            }
        }
    }
}
