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
        this.setConnectionLostTimeout(0);
        this.start();
    }

    @Override
    public void onOpen(WebSocket ws, ClientHandshake handshake) {
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

        if (message.contains("statusrequest")){
//            logger.info("status request received from ws {}", ws);
//            Connection c = connections.get(ws);
//            logger.info("got conn {} for ws {} ", c, ws);


            Player player = connections.get(ws).getPlayer();
            if (player != null) {
                logger.info("status request received from {}", player.getName());
                player.getTable().updateClient(player);
            } else {
                logger.warn("status request received from unknown client");

            }
//
//            logger.info("got player {} from ws {} ", player, ws);
        }

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
                // allow user to login again from different device.
                if (connectionExistsFor(user)){
                    // save the current connection, and update it with the new websocket
                    Connection oldConnection = getConnection(user);
                    oldConnection.setWebSocket(ws);
                    // remove old entry and add back the new
                    removeConnection(user);
                    connections.put(ws, oldConnection);
                    logger.info("user: {} logged in from a different device, updating connection entry.", oldConnection.getPlayer().getName());
                // this is a new connection ( not updating existing one )
                } else {
                    connections.put(ws, new Connection(user, ws));
                    lobbyManager.loginUser(user);
                }
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

    public WebSocket getPlayerWebsocket(Player player){
        logger.warn("looking ws for player {}", player.getName());

        for (Map.Entry<WebSocket, Connection> e : connections.entrySet()) {
            //logger.warn("looping conn entries: {} - {}", e.getKey(), e.getValue());

            Connection c = e.getValue();

            Player p = c.getPlayer();
            //logger.warn("checking if {} == {} ? ", p.getName(), player.getName());

            if (p.equals(player)) {
                logger.warn("player {} found", player.getName());

                WebSocket key = e.getKey();
                logger.warn("ws={} ", key);
                //logger.warn("ws={} ", c.getWebSocket());
                return key;
            }
        }
        return null;
    }
    public String getUserName(WebSocket webSocket){
        return connections.get(webSocket).getUser().getName();
    }


    // check if entry exists
    private boolean connectionExistsFor(User user){
        Collection<Connection> conns = connections.values();
        for (Connection c: conns){
            if (c.getUser() != null && c.getUser().equals(user)) return true;
        }
        return false;
    }

    // return a player from a user, to build a new entry upon reconnect
    private Player getPlayer(User user){
        Player p = null;
        Collection<Connection> conns = connections.values();
        for (Connection c: conns){
            if (c.getUser() != null && c.getUser().equals(user)) {
                return c.getPlayer();
            }
        }
        return null;
    }

    // extract a connection entry for a given user
    private Connection getConnection(User user){
        for (Map.Entry<WebSocket, Connection> e : connections.entrySet()) {
            Connection c = e.getValue();
            if (c.getUser() != null && c.getUser().equals(user)) {
                return c;
            }
        }
        logger.warn("no connection for user {}", user.getName());
        return null;
    }


//    private void removeWeb(User user){
//        WebSocket keyToRemove = null;
//        for (Map.Entry<WebSocket, Connection> e : connections.entrySet()) {
//            //logger.warn("looping conn entries: {} - {}", e.getKey(), e.getValue());

            // remove existing entry
    private void removeConnection(User user){
        WebSocket keyToRemove = null;
        for (Map.Entry<WebSocket, Connection> e : connections.entrySet()) {
            //logger.warn("looping conn entries: {} - {}", e.getKey(), e.getValue());

            Connection c = e.getValue();
            if (c.getUser() != null && c.getUser().equals(user)) {
                logger.info("existing connection found for {}, tagging for removal.", user.getName());
                keyToRemove = e.getKey();
            }
        }
        connections.remove(keyToRemove);



//            Connection connToRemove = null;
//        // look for connection entry for this user
//        Collection<Connection> conns = connections.values();
//        for (Connection c: conns){
//            if (c.getUser() != null && c.getUser().equals(user)) {
//                logger.info("existing connection found for {}, tagging for removal.", user.getName());
//                connToRemove = c;
//            }
//        }
//        // if found: delete entry by value
//        if (connToRemove != null){
//            connections.values().remove(connToRemove);
//        }
    }

    // debug
    public String showConnections(){
        String rv = "";
        for (Map.Entry<WebSocket, Connection> e : connections.entrySet()){
            Connection c = e.getValue();
            WebSocket ws = e.getKey();
            rv += "ws-" + ws + "::{";
            rv += c.toString() + "}\n";
        }
        return rv;
    }

}


