package com.kukinet.pkr;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Scanner;

public class ConsoleClient extends WebSocketClient {

    public ConsoleClient( URI serverUri , Draft draft ) {
        super( serverUri, draft );
    }

    public ConsoleClient( URI serverURI ) {
        super( serverURI );
    }

    public ConsoleClient( URI serverUri, Map<String, String> httpHeaders ) {
        super(serverUri, httpHeaders);
    }

    @Override
    public void onOpen( ServerHandshake handshakedata ) {
        send("hello from java client.");
        System.out.println( "client connected." );
        // if you plan to refuse connection based on ip or httpfields overload: onWebsocketHandshakeReceivedAsClient

    }

    @Override
    public void onMessage( String message ) {
        System.out.println( "received: " + message );
        if (message.startsWith("request-player-action")){
            play();
        }
    }

    @Override
    public void onClose( int code, String reason, boolean remote ) {
        // The codecodes are documented in class org.java_websocket.framing.CloseFrame
        System.out.println( "Connection closed by " + ( remote ? "remote peer" : "us" ) + " Code: " + code + " Reason: " + reason );
    }

    @Override
    public void onError( Exception ex ) {
        ex.printStackTrace();
        // if the error is fatal then onClose will be called additionally
    }

    private void play(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("your turn: [examples: bet,30 call,30 raise,60 check,0 fold,0]");
        String userAction = scanner.nextLine();
        send(userAction);
    }



    public static void main( String[] args ) throws URISyntaxException {
        String command  = args[0].split(",")[0];
        String username = args[0].split(",")[1];
        String password = args[0].split(",")[2];

        ConsoleClient c = new ConsoleClient( new URI( "ws://localhost:4444" )); // more about drafts here: http://github.com/TooTallNate/Java-WebSocket/wiki/Drafts
        c.connect();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        c.send("login" + "," + username + "," + password);
        c.send("register" + "," + username + ",testGame");

        if (args[0].split(",").length==4 ){
            String gamename = args[0].split(",")[3];
            c.send("start-game" + "," + gamename);
        }
    }
}
