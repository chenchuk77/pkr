package com.kukinet.client.profiles;

import com.google.gson.JsonObject;
import com.kukinet.client.ConsoleClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

public class BotPassivePlayer extends ConsoleClient {

    public BotPassivePlayer(URI serverURI, String username) {
        super( serverURI );
        this.username = username;
        initHand();
    }



    @Override
    public void play(){
        JsonObject actionJSON = new JsonObject();

        // accepts comma-del string and convert to json
//        Scanner scanner = new Scanner(System.in);
        System.out.println("player (class:" + this.getClass().getSimpleName() + ")");
        System.out.println("valid commands: " + validOptions.toString());
        System.out.println("valid amounts: " + optionAmounts.toString());

        String userAction;
        String action = "";
        int amount = 0;

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // this player checking whenever is possible
        if (validOptions.contains("check")){
            System.out.println("im passive, im checking.");
            action="check";
            amount = 0;
            // and calls any bet
        } else if (validOptions.contains("call")){
            System.out.println("im shulman, i call.");
            action = "call";
            amount = optionAmounts.get("call");

        }


//        if (userAction.contains(",")) {
//            action = userAction.split(",")[0];
//            amount = Integer.valueOf(userAction.split(",")[1]);
//        } else {
//            action  = userAction;
//            amount = 0;
//        }

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

//            String action  = userAction.split(",")[0];
//            String amount = userAction.split(",")[1];
//            actionJSON.addProperty("action", action);
//            actionJSON.addProperty("amount", Integer.valueOf(amount));
        }
        send(actionJSON.toString());
    }
}
