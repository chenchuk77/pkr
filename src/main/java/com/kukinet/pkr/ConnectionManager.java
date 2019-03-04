package com.kukinet.pkr;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;

@Component
public class ConnectionManager extends WebSocketServer {
    Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
    private static int TCP_PORT = 4444;
    private Map<WebSocket, Connection> connections;

    @Autowired
    private LobbyManager lobbyManager;

    @Autowired
    private LoginService loginService;

    public ConnectionManager() {
        super(new InetSocketAddress(TCP_PORT));
        connections = new HashMap<>();
        this.start();
    }

//    private void removeConnection(){
//        Iterator<Map<WebSocket, Connection>> iter = connections.iterator();
//        while (iter.hasNext()) {
//            if (iter.next().get("x").equals("y"))
//                iter.remove();
//        }
//
//    }

    @Override
    public void onOpen(WebSocket ws, ClientHandshake handshake) {
        connections.put(ws, new Connection(ws));
        logger.info("New connection from {}.", ws.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onClose(WebSocket ws, int code, String reason, boolean remote) {
        connections.remove(ws);
        logger.warn("close {} from {}, code:{} reason:{} ",
                ws, ws.getRemoteSocketAddress().getAddress().getHostAddress(), code, reason);
    }

    @Override
    public void onMessage(WebSocket ws, String message) {
        if (message.contains("action")){
            Gson gson = new Gson();
            ActionCommand cmd = gson.fromJson(message, ActionCommand.class);
            Player player = connections.get(ws).getPlayer();
            logger.info("action JSON received from {}", player.getName());
            if (player.isWaitForAction()){
                player.setActionCommand(cmd);
                logger.info("{} action set for {}", cmd.getAction(), player.getName());
            } else {
                logger.warn("{} action received from {}, but player is not ready.", cmd.getAction(), player.getName());
            }
        }
        if (message.startsWith("login")){
            logger.info("login command accepted.");
            String cmd = message.split(",")[0];
            String username = message.split(",")[1];
            String password = message.split(",")[2];
            User user = loginService.login(username, password);
            if (user != null){
                connections.put(ws, new Connection(user, ws));
                lobbyManager.loginUser(user);
            } else {
                logger.warn("no such user: {}.", username);
            }
        }
//        if (status.startsWith("logout")){
//            logger.info("logout command accepted.");
//            String cmd = status.split(",")[0];
//            String username = status.split(",")[1];
//            User user = loginService.login(username, password);
//            if (user != null){
//                connections.put(ws, new Connection(user, ws));
////                lobbyManager.loginUser(user, ws);
//                lobbyManager.loginUser(user);
//            } else {
//                logger.warn("no such user: {}.", username);
//            }
//        }
//
        if (message.startsWith("register")){
            logger.info("register command accepted from {}.", getUserName(ws));
            String cmd = message.split(",")[0];
            String username = message.split(",")[1];
            String gamename = message.split(",")[2];
            lobbyManager.registerUser(username, gamename);
        }

//        if (status.startsWith("create-game")){
//            logger.info("create-game command accepted from {}.", getUserName(ws));
//            String cmd = status.split(",")[0];
//            String name = status.split(",")[1];
//            String price = status.split(",")[2];
//            lobbyManager.createGame(name, price, this);
//        }
//        if (status.startsWith("start-game")){
//            logger.info("start-game command accepted from {}.", getUserName(ws));
//            String cmd = status.split(",")[0];
//            String name = status.split(",")[1];
//            lobbyManager.startGame(name);
//        }

//        // notification mechanism
//        if (status.startsWith("action")){
//            logger.info("actionCommand accepted.");   /// action,call,30
//            String action = status.split(",")[1];
//            String amount = status.split(",")[2];
//            Player player = connections.get(ws).getPlayer();
//            if (player.isWaitForAction()){
//                ActionCommand cmd = new ActionCommand();
//                cmd.setAction(action);
//                cmd.setAmount(Integer.valueOf(amount));
//                player.setActionCommand(cmd);
//                logger.info("{} action set for {}", action, player.getName());
//            } else {
//                logger.warn("{} action received from {}, but player is not ready.", action, player.getName());
//            }
//        }

        if (message.startsWith("stop-server")){
            logger.info("stop-server command accepted from {}.", getUserName(ws));
            String cmd = message.split(",")[0];
            try {
                this.stop();
                logger.info("server stopped successfully.");

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(WebSocket ws, Exception ex) {
        ex.printStackTrace();
        if (ws != null) {
            connections.remove(ws);
            // do some thing if required
        }
        logger.error("ERROR");
    }

    @Override
    public void onStart() {
        logger.info("WebSocker server listens on tcp port {}.", TCP_PORT);
    }



    public void setPlayer(User user, Player player) {
        for (Map.Entry<WebSocket, Connection> e : connections.entrySet()) {

            Connection c = e.getValue();
            if (c.getUser().equals(user)){
                c.setPlayer(player);
            }
        }
    }

    public WebSocket getPlayerConnetion(Player player){
        for (Map.Entry<WebSocket, Connection> e : connections.entrySet()) {
            Connection c = e.getValue();
            Player p = c.getPlayer();
            if (p.equals(player)) {
                WebSocket key = e.getKey();
                return key;
            }
        }
        return null;
    }
    public String getUserName(WebSocket webSocket){
        return connections.get(webSocket).getUser().getName();
    }
}


