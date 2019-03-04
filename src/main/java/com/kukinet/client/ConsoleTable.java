package com.kukinet.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kukinet.client.PlayerDto;
import com.kukinet.client.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ConsoleTable {
// {"0":{"name":"ddd","startingStack":10000,"effectiveStack":10000,"commited":0,"inGame":true,"inHand":true},"1":{"name":"eee","startingStack":10000,"effectiveStack":10000,"commited":0,"inGame":true,"inHand":true},"2":{"name":"fff","startingStack":10000,"effectiveStack":10000,"commited":0,"inGame":true,"inHand":true},"3":{"name":"iii","startingStack":10000,"effectiveStack":10000,"commited":0,"inGame":true,"inHand":true}}

    public static void main(String[] args) {
//        Map<Integer, Player> map = new HashMap<>();

        Gson gson = new Gson();
        File jsonFileTable = Paths.get("/home/lumos/dev/SpringWebSocket/src/main/java/com/kukinet/client/table_test.json").toFile();
        File jsonFileTableHands = Paths.get("/home/lumos/dev/SpringWebSocket/src/main/java/com/kukinet/client/table-hands_test.json").toFile();
        File jsonFileButtons = Paths.get("/home/lumos/dev/SpringWebSocket/src/main/java/com/kukinet/client/buttons_test.json").toFile();
        File jsonFileBets = Paths.get("/home/lumos/dev/SpringWebSocket/src/main/java/com/kukinet/client/bets_test.json").toFile();
        JsonObject jsonObject;
        try {

//            // hashmap of players
//            jsonObject = gson.fromJson(new FileReader(jsonFileTable), JsonObject.class);
//            Map<Integer, PlayerDto> table = Utils.parseTableStateJSON(jsonObject);

            // hashmap of players
            jsonObject = gson.fromJson(new FileReader(jsonFileTableHands), JsonObject.class);
            Map<Integer, PlayerDto> table = Utils.parseTableWithHandsJSON(jsonObject);

            jsonObject = gson.fromJson(new FileReader(jsonFileButtons), JsonObject.class);
            Map<String, Integer> buttons = Utils.parseButtonsJSON(jsonObject);

            jsonObject = gson.fromJson(new FileReader(jsonFileBets), JsonObject.class);
            Map<String, Integer> bets = Utils.parseBetsJSON(jsonObject);


            //
//            for (int i=0; i<4; i++){
//                // player
//                JsonObject playerJSON = jsonObject.getAsJsonObject(String.valueOf(i));
//                map.put(i, new Player(playerJSON));
//            }
//            //{"name":"ddd","startingStack":10000,"effectiveStack":10000,"commited":0,"inGame":true,"inHand":true}
//            //System.out.println(jsonObject.get("0"));
//


//            /*
//            System.out.println("name" + "\t" + "efctv" + "/" + "start" + "\t" + "commit");
//
//            for (int i=0; i<4; i++){
//                PlayerDto p = table.get(i);
//                System.out.println(p.getName() + "\t\t" +
//                        p.getChips() + "/" +
//                        p.getStartingStack() + "\t" +
//                        p.commited() + "\t\t" +
//                        drawButtons(buttons, i) + "\t\t" +
//                        p.getStrHole1() + p.getStrHole2());
//            }*/












//            for(Player p: table.values()){
//
//                System.out.println(p.getName() + "\t\t" + p.getChips() + "/" +
//                        p.getStartingStack() + "\t" + p.commited() );
//            }
//
////            System.out.println(jsonObject.getAsJsonObject("0").get("name"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

//    private static String drawButtons(Map<String ,Integer> buttons, int i) {
//
//        if (buttons.get("sbPosition").equals(i)){
//            return "[SB]";
//        }
//        if (buttons.get("bbPosition").equals(i)){
//            return "[BB]";
//        }
//        if (buttons.get("dealerPosition").equals(i)){
//            return "[DL]";
//        }
//        return "----";
//    }


}
