package com.kukinet.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kukinet.pkr.Player;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class ConsoleClient extends WebSocketClient {

    private Map<Integer, PlayerDto> table;
    private Map<String, Integer> buttons;
    private Map<String, Integer> bets;
    private int pot;
    private PlayerDto player;
    private int seat;
    private List<String> communityCards;
//    private int nextPlayerSeat;
    private int activePlayerIndex;
//    private Map<String, Integer> bets;


//    ConsoleClient c = new ConsoleClient( new URI( "ws://localhost:4444" )); // more about drafts here: http://github.com/TooTallNate/Java-WebSocket/wiki/Drafts


//    public ConsoleClient( URI serverUri , Draft draft ) {
//        super( serverUri, draft );
//    }
//    public ConsoleClient( URI serverUri, Map<String, String> httpHeaders ) {
//        super(serverUri, httpHeaders);
//    }
    public ConsoleClient(URI serverURI) {
        super( serverURI );
        table = new HashMap<>();
        buttons = new HashMap<>();
        bets = new HashMap<>();
        pot = 0;
        activePlayerIndex = 0;
        communityCards = new ArrayList<>();
    }

//    private void nextPlayer(){
//
//    }

    private Integer getPlayerSeatByName(String name){
        for (Map.Entry<Integer, PlayerDto> entry: table.entrySet()){
            if (entry.getValue().getName().equals(name)){
                return entry.getKey();
            }
        }
        return null;
    }


    private PlayerDto getPlayerDtoByName(String name){
        for (Map.Entry<Integer, PlayerDto> entry: table.entrySet()){
            if (entry.getValue().getName().equals(name)){
                return table.get(entry.getKey());
            }
        }
        return null;
    }

    public void parseTableStateJSON(JsonObject tableStateJSON){

        // build the table hashmap
        for (int i=0; i<4; i++) {
            // player
            JsonObject playerJSON = tableStateJSON.getAsJsonObject(String.valueOf(i));
            //String playerName = playerJSON.get("name").getAsString();
            PlayerDto jsonPlayer = new PlayerDto(playerJSON);
            // if me
            if (jsonPlayer.getName().equals(USERNAME)){
                // set ctx player if not exists
                if (player == null){

                    player = jsonPlayer;
                    seat = i;
                    System.out.println("set player in context to " + player.getName());

                    // override XX cards with my real cards
                } else {
//                    System.out.println("json c1 = " + jsonPlayer.getStrHole1());
//                    System.out.println("me   c1 = " + player.getStrHole1());

                    if (jsonPlayer.getStrHole1().equals("XX")){
                        jsonPlayer.setStrHole1(player.getStrHole1());
                    }
                    if (jsonPlayer.getStrHole2().equals("XX")){
                        jsonPlayer.setStrHole2(player.getStrHole2());
                    }
                }
            }
            table.put(i, jsonPlayer);
        }

//        // update ctx player on start
//        if (player == null) {
//            player = getPlayerDtoByName(USERNAME);
//            System.out.println("clint ctx player set.");
//
//        }

//        // inject my hole cards if im in play
//        PlayerDto me = getPlayerDtoByName(USERNAME);
//        if (me.getStrHole1().equals("XX")){
//            me.setStrHole1(player.getStrHole1());
//        }
//        if (me.getStrHole2().equals("XX")){
//            me.setStrHole2(player.getStrHole2());
//        }

//        me.setStrHole2(player.getStrHole2());
//
//
//                // if me
//            if (playerName.equals(USERNAME)){
//                if (player == null){
//                    player = new PlayerDto(playerJSON);
//                    table.put(i, player);
//                }
//            } else {
//                table.put(i, new PlayerDto(playerJSON));
//            }
//        }
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
        // table update JSON
        if (message.length() > 300){
            System.out.println( "incoming table update." );
            JsonObject jsonMessage = new Gson().fromJson(message, JsonObject.class);
            //table = Utils.parseTableStateJSON(jsonMessage);
            parseTableStateJSON(jsonMessage);
            drawTable();
        }
        // bets update JSON
        if (message.contains("ante")){
            JsonObject jsonMessage = new Gson().fromJson(message, JsonObject.class);
            bets = Utils.parseBetsJSON(jsonMessage);
        }
        // buttons update JSON
        if (message.contains("sbPosition")){
            JsonObject jsonMessage = new Gson().fromJson(message, JsonObject.class);
            buttons = Utils.parseButtonsJSON(jsonMessage);
            drawTable();
        }
        // update ctx player with my hole cards
        if (message.contains("cards")){
            JsonObject jsonMessage = new Gson().fromJson(message, JsonObject.class);
            player.setStrHole1(jsonMessage.get("card1").getAsString());
            player.setStrHole2(jsonMessage.get("card2").getAsString());
        }
        // update community cards
        if (message.contains("community")){
            JsonObject jsonMessage = new Gson().fromJson(message, JsonObject.class);
            communityCards.add(jsonMessage.get("flop1").getAsString());
            communityCards.add(jsonMessage.get("flop2").getAsString());
            communityCards.add(jsonMessage.get("flop3").getAsString());
        }

        // player move update JSON
        // {"type":"playermove","seat":2,"player":{"name":"fff","startingStack":10000,"effectiveStack":10000,"commited":0,"inGame":true,"inHand":false,"strHole1":"00","strHole2":"00"},"command":"fold","value":0}
        if (message.contains("playermove")){
            JsonObject jsonMessage = new Gson().fromJson(message, JsonObject.class);

            Integer seat = jsonMessage.get("seat").getAsInt();
            String command = jsonMessage.get("command").getAsString();
            Integer value = jsonMessage.get("value").getAsInt();
            pot = jsonMessage.get("pot").getAsInt();

            // get nested player json and construct a new playerDto
            JsonObject playerJSON = jsonMessage.get("player").getAsJsonObject();
            Gson gson = new Gson();
            PlayerDto activePlayerJSON = gson.fromJson(playerJSON, PlayerDto.class);

            // if me and not folding, inject my holecards into the object
            if (activePlayerJSON.getName().equals(player.getName()) && !command.equals("fold")){
                activePlayerJSON.setStrHole1(player.getStrHole1());
                activePlayerJSON.setStrHole2(player.getStrHole2());
            }

            // update table with the new player data
            table.put(seat, activePlayerJSON);
            drawTable();

            PlayerDto p = table.get(seat);
            if (command.equals("sb")){
                System.out.println( p.getName() + " post sb (" + value + ")." );
            }
            if (command.equals("bb")){
                System.out.println( p.getName() + " post bb (" + value + ")." );
            }
            if (command.equals("call")){
                System.out.println( p.getName() + " call " + value + "." );
            }
            if (command.equals("raise")){
                System.out.println( p.getName() + " raise to " + value + "." );
            }
            if (command.equals("fold")){
                System.out.println( p.getName() + " fold." );
            }
        }

        // wait for player - update JSON
        if (message.contains("waitaction")){
            JsonObject jsonMessage = new Gson().fromJson(message, JsonObject.class);
            String playerName = jsonMessage.get("player").getAsString();
            activePlayerIndex = getPlayerSeatByName(playerName);
            drawTable();
            // my turn
            if (playerName.equals(player.getName())){
                play();
            }
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
        // accepts comma-del string and convert to json
        Scanner scanner = new Scanner(System.in);
        System.out.println("your turn: [examples: bet,30 call,30 raise,60 check,0 fold,0]");
        String userAction = scanner.nextLine();
        String action  = userAction.split(",")[0];
        String amount = userAction.split(",")[1];
        JsonObject actionJSON = new JsonObject();
        actionJSON.addProperty("action", action);
        actionJSON.addProperty("amount", Integer.valueOf(amount));
        send(actionJSON.toString());
    }


    private void clearScreen(){
        //System.out.print(ConsoleColors.CLS);
        //System.out.print("\033\143");
//        for (int i=0; i<24; i++){
//            System.out.println();
//        }

                    System.out.println("##########################  TODO: CLEAR SCREEN  ################################");
    }

    private void drawTable(){
        // exit if no table yet
//        if (table == null) return;
//        if (table.get(0) == null) return;
        clearScreen();
        System.out.println(ConsoleColors.CYAN_BOLD + "name" + "\t" + "efctv" + "/" + "start" + "\t" + "commit" + ConsoleColors.RESET);

        for (int i=0; i<4; i++){
            String color = ConsoleColors.RESET;

            // highlight active player
            if (i == activePlayerIndex){
                color = ConsoleColors.WHITE_BOLD;
                // bold me
                if (seat == activePlayerIndex){
                    color += ConsoleColors.BLINK_BOLD;
                }
            }

            // blinks me

            PlayerDto p = table.get(i);
            System.out.println(color + p.getName() + "\t" + ConsoleColors.RESET +
                    p.getEffectiveStack() + "/" +
                    p.getStartingStack() + "\t" +
                    p.commited() + "\t\t" +
                    drawButtons(buttons, i) + "\t\t" +
                    p.getStrHole1() + " " + p.getStrHole2() );

        }
        System.out.println();

        // print community cards
        if (!communityCards.isEmpty()){
            System.out.println(ConsoleColors.PURPLE_BOLD + "FLOP: " + communityCards.get(0) + " " +
                    communityCards.get(1) + " " +
                    communityCards.get(2) + ConsoleColors.RESET) ;
        }

        System.out.println(ConsoleColors.YELLOW_BOLD + "pot: " + pot + ConsoleColors.RESET);
        System.out.println();


    }
    private static String drawButtons(Map<String ,Integer> buttons, int i) {

        // game start
        if (buttons == null){
            return "    ";
        }
        if (buttons.get("sbPosition")== null){
            return "    ";
        }

        if (buttons.get("sbPosition").equals(i)){
            return "[SB]";
        }
        if (buttons.get("bbPosition").equals(i)){
            return "[BB]";
        }
        if (buttons.get("dealerPosition").equals(i)){
            return "[DL]";
        }
        return "----";
    }

    // global. used by client to recognize whoami
    public static String USERNAME;

    public static void main( String[] args ) throws URISyntaxException {
        String command  = args[0].split(",")[0];
//        String username = args[0].split(",")[1];
        USERNAME = args[0].split(",")[1];

        String password = args[0].split(",")[2];

        ConsoleClient c = new ConsoleClient( new URI( "ws://localhost:4444" )); // more about drafts here: http://github.com/TooTallNate/Java-WebSocket/wiki/Drafts
        c.connect();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        c.send("login" + "," + USERNAME + "," + password);
        c.send("register" + "," + USERNAME + ",testGame");

//        if (args[0].split(",").length==4 ){
//            String gamename = args[0].split(",")[3];
//            c.send("start-game" + "," + gamename);
//        }
    }
}
