package com.kukinet.pkr;

import org.java_websocket.WebSocket;

public class Connection {

    private User user;
    private Player player;
    private WebSocket webSocket;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public void setWebSocket(WebSocket webSocket) {
        this.webSocket = webSocket;
    }

    public Connection(User user, Player player, WebSocket webSocket) {
        this.user = user;
        this.player = player;
        this.webSocket = webSocket;
    }
    public Connection(WebSocket webSocket) {
        this.user = null;
        this.player = null;
        this.webSocket = webSocket;
    }
    public Connection(User user, WebSocket webSocket) {
        this.user = user;
        this.player = null;
        this.webSocket = webSocket;
    }



}
