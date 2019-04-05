package com.kukinet.client;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ConsoleClient extends WebSocketClient implements PlayerStrategy {

    protected String username;
    protected Map<Integer, PlayerDto> table;
    protected Map<String, Integer> buttons;
    protected Map<String, Integer> bets;
    protected int pot;
    protected PlayerDto player;
    protected int seat;
    protected List<String> communityCards;

    // for drawing client action buttons
    protected List<String> validOptions;
    protected Map<String, Integer> optionAmounts;

    //    protected int nextPlayerSeat;
    protected int activePlayerIndex;
    protected String statusMessage;

    protected final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    public ConsoleClient(URI serverURI) {
        super( serverURI );
        initHand();
    }

    public ConsoleClient(URI serverURI, String username) {
        super(serverURI);
        this.username = username;
    }

    protected void initHand(){
        System.out.println("init client for a new hand.");
        // table will be refreshed after every "new hand"
        player = null;
        table = new HashMap<>();
        buttons = new HashMap<>();
        bets = new HashMap<>();
        pot = 0;
        activePlayerIndex = 0;
        communityCards = new ArrayList<>();
        statusMessage = "";
    }

    protected Integer getPlayerSeatByName(String name){
        for (Map.Entry<Integer, PlayerDto> entry: table.entrySet()){
            if (entry.getValue().getName().equals(name)){
                return entry.getKey();
            }
        }
        return null;
    }

    protected PlayerDto getPlayerDtoByName(String name){
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
            //System.out.println("USERNAME=" + username);
            if (jsonPlayer.getName().equals(username)){
                // set ctx player if not exists
                if (player == null){
                    player = jsonPlayer;
                    seat = i;
                    System.out.println("set player in context to " + player.getName());
                // override XX cards with my real cards
                } else {
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
    }

    @Override
    public void onOpen( ServerHandshake handshakedata ) {
        send("hello from java client.");
        System.out.println( "client connected." );
    }

    @Override
    public void onMessage( String message ) {
        System.out.println( "received: " + message );
        // table update JSON
        if (message.length() > 300){
            JsonObject jsonMessage = new Gson().fromJson(message, JsonObject.class);
            parseTableStateJSON(jsonMessage);
            System.out.println( "ctx table updated." );
            drawTable();
        }
        // bets update JSON
        if (message.contains("ante")){
            JsonObject betsJSON = new Gson().fromJson(message, JsonObject.class);
            bets.put("ante", betsJSON.get("ante").getAsInt());
            bets.put("sb", betsJSON.get("sb").getAsInt());
            bets.put("bb", betsJSON.get("bb").getAsInt());
            System.out.println( "ctx bets updated." );
            drawTable();
        }
        // buttons update JSON
        if (message.contains("sbPosition")){
            JsonObject buttonsJSON = new Gson().fromJson(message, JsonObject.class);
            buttons.put("sbPosition", buttonsJSON.get("sbPosition").getAsInt());
            buttons.put("bbPosition", buttonsJSON.get("bbPosition").getAsInt());
            buttons.put("dealerPosition", buttonsJSON.get("dealerPosition").getAsInt());
            System.out.println( "ctx buttons updated." );
            drawTable();
        }
        // update ctx player with my hole cards
        if (message.contains("cards")){
            JsonObject cardsJSON = new Gson().fromJson(message, JsonObject.class);
            player.setStrHole1(cardsJSON.get("card1").getAsString());
            player.setStrHole2(cardsJSON.get("card2").getAsString());
            System.out.println( "ctx cards updated." );
            drawTable();
        }
        // update community cards
        if (message.contains("community")){
            communityCards = new ArrayList<>();
            JsonObject communityCardsJSON = new Gson().fromJson(message, JsonObject.class);
            communityCards.add(communityCardsJSON.get("flop1").getAsString());
            communityCards.add(communityCardsJSON.get("flop2").getAsString());
            communityCards.add(communityCardsJSON.get("flop3").getAsString());
            if (communityCardsJSON.has("turn")){
                communityCards.add(communityCardsJSON.get("turn").getAsString());
            }
            if (communityCardsJSON.has("river")){
                communityCards.add(communityCardsJSON.get("river").getAsString());
            }
            System.out.println( "ctx community-cards updated." );
            drawTable();

        }
        if (message.contains("potshare")) {
            JsonObject potShareJSON = new Gson().fromJson(message, JsonObject.class);
            System.out.println("potshare update received.");
            potShareJSON.remove("type");
            JsonElement je = potShareJSON;
            Type mapType = new TypeToken<Map<String, Integer>>() {}.getType();
            Map<String,Integer> potShareMap = new Gson().fromJson(je, mapType);
            System.out.println( "pot share xxxxx received." + potShareMap.toString());
            setStatusMessage("winners: " + potShareMap.toString());
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
      //    received: {"type":"waitaction","player":"fff","options":["fold","call","raise","allin"],"optionAmounts":{"call":60,"max_raise":10000,"min_raise":0,"allin":10000}}

                // extract valid commands
                JsonElement optionsArrayJSON = jsonMessage.get("options");
                Type listType = new TypeToken<List<String>>() {}.getType();
                validOptions = new Gson().fromJson(optionsArrayJSON, listType);

                // extract valid ranges (chips amount for call/raise)
                JsonElement optionsAmountsJSON = jsonMessage.get("optionAmounts");
                Type mapType = new TypeToken<Map<String, Integer>>() {}.getType();
                optionAmounts = new Gson().fromJson(optionsAmountsJSON, mapType);
                play();
            }
        }

        if (message.contains("status")) {
            System.out.println("status update received.");
            JsonObject statusJSON = new Gson().fromJson(message, JsonObject.class);
            statusMessage = statusJSON.get("value").getAsString();
            setStatusMessage(statusMessage);
        }


        // update ctx player with my hole cards
        if (message.contains("new hand")){
            System.out.println( "starting a new hand");
            initHand();
        }

    }

    @Override
    public void onClose( int code, String reason, boolean remote ) {
        System.out.println( "Connection closed by " + ( remote ? "remote peer" : "us" ) + " Code: " + code + " Reason: " + reason );
    }

    @Override
    public void onError( Exception ex ) {
        ex.printStackTrace();
        // if the error is fatal then onClose will be called additionally
    }

    @Override
    public void play(){
        JsonObject actionJSON = new JsonObject();

        // accepts comma-del string and convert to json
        Scanner scanner = new Scanner(System.in);
        System.out.println("your turn: [examples: bet,30 call,30 raise,60 check,0 fold,0]");
        System.out.println("valid commands: " + validOptions.toString());
        System.out.println("valid amounts: " + optionAmounts.toString());

        String userAction = scanner.nextLine();
        String action;
        int amount;

        if (userAction.contains(",")) {
            action = userAction.split(",")[0];
            amount = Integer.valueOf(userAction.split(",")[1]);
        } else {
            action  = userAction;
            amount = 0;
        }

        if (action.equals("call") || action.equals("c")){
            actionJSON.addProperty("action", "call");
            actionJSON.addProperty("amount", optionAmounts.get("call"));
        } else if (action.equals("fold") || action.equals("f")){
            actionJSON.addProperty("action", "fold");
            actionJSON.addProperty("amount", 0);
        } else if (action.equals("raise") || action.equals("r")){
            actionJSON.addProperty("action", "raise");
            actionJSON.addProperty("amount", amount);
        } else if (action.equals("bet") || action.equals("b")){
            actionJSON.addProperty("action", "bet");
            actionJSON.addProperty("amount", amount);
        } else if (action.equals("check") || action.equals("k")){
            actionJSON.addProperty("action", "check");
            actionJSON.addProperty("amount", 0);
        } else if (action.equals("allin") || action.equals("a")){
            actionJSON.addProperty("action", "allin");
            actionJSON.addProperty("amount", optionAmounts.get("allin"));
        } else {
            System.out.println("unknown command, assume hacking.");
            actionJSON.addProperty("action", action);
            actionJSON.addProperty("amount", amount);
        }
        send(actionJSON.toString());
    }


    protected void clearScreen(){
        //System.out.print(ConsoleColors.CLS);
        //System.out.print("\033\143");
        for (int i=0; i<24; i++){
            System.out.println();
        }
//                    System.out.println("##########################  TODO: CLEAR SCREEN  ################################");
    }

    protected void drawTable(){
        // exit if no table yet
//        if (table == null) return;
//        if (table.get(0) == null) return;
        clearScreen();
//        System.out.println(ConsoleColors.CYAN_BOLD + "name" + "\t" + "efctv" + "/" + "start" + "\t" + "commit" + ConsoleColors.RESET);

        System.out.println(ConsoleColors.CYAN_BOLD + "name" + "\t" + "chips" + "\t" + "commited" + "pos" + "\t" + ConsoleColors.RESET);

        for (int i=0; i<4; i++){
            String color = ConsoleColors.RESET;
            //String cards = "";
            // highlight active player
            if (i == activePlayerIndex){
                color = ConsoleColors.GREEN_BOLD;
                // bold me
                if (seat == activePlayerIndex){
                    color = ConsoleColors.GREEN_BOLD + ConsoleColors.BLINK_BOLD;
                   // cards =

                }
            }
            PlayerDto p = table.get(i);
            if (!p.inHand()){color = ConsoleColors.RED;}

            // elvis checking if its me (if player in update is ctx player), to show cards
            System.out.println(color +
                    p.getName() + "\t" +
                    p.getChips() + "\t" +
                 //   drawButtons(i) + "\t" +
                    (p.commited() == 0 ? " " : p.commited()) + "\t\t" +
                    drawButtons(i) + "\t\t" + ConsoleColors.RESET +
                    ((p.getName().equals(player.getName())) ?
                            suitedCard(player.getStrHole1()) + " " + suitedCard(player.getStrHole2()) :
                            suitedCard(p.getStrHole1()) + " " + suitedCard(p.getStrHole2())));
        }
        System.out.println();

        // print community cards
        if (!communityCards.isEmpty()){
//            System.out.println("----- community cards: " + communityCards.size());

            String ccards = suitedCard(communityCards.get(0)) + "   " +
                            suitedCard(communityCards.get(1)) + "   " +
                            suitedCard(communityCards.get(2)) + "     " ;
            if (communityCards.size()>3) {
                ccards += suitedCard(communityCards.get(3)) + "     " ;
            }
            if (communityCards.size()>4) {
                ccards += suitedCard(communityCards.get(4));
            }

            System.out.println(ConsoleColors.PURPLE_BOLD + "flop : " + ccards + ConsoleColors.RESET);
        }

        System.out.println(ConsoleColors.YELLOW_BOLD + "pot  : " + pot + ConsoleColors.RESET);
        System.out.println();

        System.out.println(ConsoleColors.WHITE_BOLD_BRIGHT + statusMessage);
        System.out.println(ConsoleColors.RESET);
        System.out.println();

    }

    protected static final String SUIT_SPADE = "\u2660";
    protected static final String SUIT_CLUB = "\u2663";
    protected static final String SUIT_HEART = "\u2665";
    protected static final String SUIT_DIAMOND = "\u2666";
    //protected static final String CARD_BACK = "\U1f0a0";


//    protected String cardsOfS

    public String suitedCard(String textCard){
        if (textCard.endsWith("s")){
            //return textCard.replace("c", SUIT_SPADE);
            return ConsoleColors.CARD_FG_WHITE + textCard.charAt(0) + SUIT_SPADE + ConsoleColors.RESET;
        }
        if (textCard.endsWith("c")){
//            return textCard.replace("c", SUIT_CLUB);
            return ConsoleColors.CARD_FG_GREEN + textCard.charAt(0) + SUIT_CLUB + ConsoleColors.RESET;

        }
        if (textCard.endsWith("h")){
//            return textCard.replace("c", SUIT_HEART);
            return ConsoleColors.CARD_FG_RED + textCard.charAt(0) + SUIT_HEART + ConsoleColors.RESET;

        }
        if (textCard.endsWith("d")){
//            return textCard.replace("c", SUIT_DIAMOND);
            return ConsoleColors.CARD_FG_BLUE + textCard.charAt(0) + SUIT_DIAMOND + ConsoleColors.RESET;

        }
        return textCard;

    }


    protected String drawButtons(int seatIndex) {
//        System.out.println("---------- drawButtons called for seat: " + seatIndex);

        if (buttons == null) {
//            System.out.println("---------- is null");
            return "  ";

        }
        if (buttons.get("sbPosition") == null) {
//            System.out.println("---------- buttons.get() returns null");
            return "  ";
        }
        if (buttons.get("sbPosition") == seatIndex) {
            return "SB";
        }
        if (buttons.get("bbPosition") == seatIndex) {
            return "BB";
        }
        if (buttons.get("dealerPosition") == seatIndex) {
            return "* ";
        }
        return "    ";
    }

    protected void setStatusMessage(String message){
        statusMessage = message;
        drawTable();
    }


    // global. used by client to recognize whoami
//    zzzzzzzzzzzzzzpublic static String USERNAME;

//    public static void main( String[] args ) throws URISyntaxException {
//        String command  = args[0].split(",")[0];
////        String username = args[0].split(",")[1];
//        USERNAME = args[0].split(",")[1];
//
//        String password = args[0].split(",")[2];
//        String gameName = args[0].split(",")[3];
//
//        ConsoleClient c = new ConsoleClient( new URI( "ws://localhost:4444" )); // more about drafts here: http://github.com/TooTallNate/Java-WebSocket/wiki/Drafts
//        c.setConnectionLostTimeout(0);
//        c.connect();
//
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        c.send("login" + "," + USERNAME + "," + password);
//
//        // register to testgame if 'testGame' keyword specified
//        if (gameName.equals("testGame")){
//            c.send("register" + "," + USERNAME + ",testGame");
//
//        }
//        if (gameName.equals("noop")){
//            c.send("statusrequest");
//        }
//
//    }
}
