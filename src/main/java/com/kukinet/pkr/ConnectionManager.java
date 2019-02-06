package com.kukinet.pkr;

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

    private static int TCP_PORT = 4444;
    Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

    @Autowired
    private LobbyManager lobbyManager;

    @Autowired
    private LoginService loginService;

    private Map<WebSocket, Connection> connections;



//    private Set<WebSocket> conns;

    public ConnectionManager() {
        super(new InetSocketAddress(TCP_PORT));
//        conns = new HashSet<>();
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
//        conns.add(ws);
//        connections.add(ws, new Connection(ws));
        connections.put(ws, new Connection(ws));

        logger.info("New connection from {}.", ws.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onClose(WebSocket ws, int code, String reason, boolean remote) {
//        conns.remove(ws);

        connections.remove(ws);
        logger.warn("close {} from {}, code:{} reason:{} ",
                ws, ws.getRemoteSocketAddress().getAddress().getHostAddress(), code, reason);
    }



    @Override
    public void onMessage(WebSocket ws, String message) {
        if (message.startsWith("login")){
            logger.info("login command accepted.");
            String cmd = message.split(",")[0];
            String username = message.split(",")[1];
            String password = message.split(",")[2];
            User user = loginService.login(username, password);
            if (user != null){
                connections.put(ws, new Connection(user, ws));
//                lobbyManager.loginUser(user, ws);
                lobbyManager.loginUser(user);
            } else {
                logger.warn("no such user: {}.", username);
            }
        }
//        if (message.startsWith("logout")){
//            logger.info("logout command accepted.");
//            String cmd = message.split(",")[0];
//            String username = message.split(",")[1];
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

        if (message.startsWith("create-game")){
            logger.info("create-game command accepted from {}.", getUserName(ws));
            String cmd = message.split(",")[0];
            String name = message.split(",")[1];
            String price = message.split(",")[2];
            lobbyManager.createGame(name, price, this);
        }
        if (message.startsWith("start-game")){
            logger.info("start-game command accepted from {}.", getUserName(ws));
            String cmd = message.split(",")[0];
            String name = message.split(",")[1];
            lobbyManager.startGame(name);
        }

//        if (message.startsWith("call")){
//            logger.info("call command accepted.");
//            String commnd = message.split(",")[0];
//            String amount = message.split(",")[1];
//            lobbyManager.call(amount);
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

//        logger.info("Message from {}: {} ", ws , message);
//        logger.info("Message from {}: {} ", lobbyManager.getUsername(ws) , message);
//        for (WebSocket sock : conns) {
//            sock.send(message);
//        }
    }

    @Override
    public void onError(WebSocket ws, Exception ex) {
        ex.printStackTrace();
        if (ws != null) {
//            conns.remove(ws);
            connections.remove(ws);
            // do some thing if required
        }
//        logger.error("ERROR from {}.", ws.getRemoteSocketAddress().getAddress().getHostAddress());
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
//            Player p = c.getPlayer();
//
//            if (p.equals(t)) {
//                //if (e.getValue().getPlayer().equals(player)) {
//                WebSocket key = e.getKey();
//                return key;
//            }
        }
//        return null;
    }

    public WebSocket getPlayerConnetion(Player player){
        for (Map.Entry<WebSocket, Connection> e : connections.entrySet()) {
            Connection c = e.getValue();
                    Player p = c.getPlayer();

            if (p.equals(player)) {
                //if (e.getValue().getPlayer().equals(player)) {
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


