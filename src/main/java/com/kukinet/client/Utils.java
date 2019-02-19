package com.kukinet.client;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class Utils {


    // example data:
    // {"0":{"name":"ddd","startingStack":10000,"effectiveStack":9970,"holeCard1":{"suit":"s","rank":"Q"},"holeCard2":{"suit":"c","rank":"T"},"commited":30,"inGame":true,"inHand":true},"1":{"name":"eee","startingStack":10000,"effectiveStack":9940,"holeCard1":{"suit":"h","rank":"_4"},"holeCard2":{"suit":"s","rank":"_8"},"commited":60,"inGame":true,"inHand":true},"2":{"name":"fff","startingStack":10000,"effectiveStack":10000,"holeCard1":{"suit":"s","rank":"_2"},"holeCard2":{"suit":"d","rank":"_7"},"commited":0,"inGame":true,"inHand":true},"3":{"name":"iii","startingStack":10000,"effectiveStack":10000,"holeCard1":{"suit":"s","rank":"_4"},"holeCard2":{"suit":"c","rank":"_9"},"commited":0,"inGame":true,"inHand":true}}
    public static Map<Integer, PlayerDto> parseTableWithHandsJSON(JsonObject tableStateJSON){
        Map<Integer, PlayerDto> map = new HashMap<>();
        for (int i=0; i<4; i++){
            // player
            JsonObject playerJSON = tableStateJSON.getAsJsonObject(String.valueOf(i));
//            JsonObject holeCard1 = playerJSON.getAsJsonObject("holeCard1");
//            String hole1rank = holeCard1.get("rank").getAsString();
//            hole1rank = hole1rank.replace("_","");
//            String hole1suit = holeCard1.get("suit").getAsString();
//            String hc1 = hole1rank + hole1suit;
//
//            JsonObject holeCard2 = playerJSON.getAsJsonObject("holeCard2");
//            String hole2rank = holeCard2.get("rank").getAsString();
//            hole2rank = hole2rank.replace("_","");
//            String hole2suit = holeCard2.get("suit").getAsString();
//            String hc2 = hole2rank + hole2suit;

//            map.put(i, new PlayerDto(playerJSON, hc1, hc2));
            map.put(i, new PlayerDto(playerJSON));
        }
        return map;
    }



    // example data:
    // {"0":{"name":"ddd","startingStack":10000,"effectiveStack":10000,"commited":0,"inGame":true,"inHand":true},"1":{"name":"eee","startingStack":10000,"effectiveStack":10000,"commited":0,"inGame":true,"inHand":true},"2":{"name":"fff","startingStack":10000,"effectiveStack":10000,"commited":0,"inGame":true,"inHand":true},"3":{"name":"iii","startingStack":10000,"effectiveStack":10000,"commited":0,"inGame":true,"inHand":true}}
    public static Map<Integer, PlayerDto> parseTableStateJSON(JsonObject tableStateJSON){
        Map<Integer, PlayerDto> map = new HashMap<>();
        for (int i=0; i<4; i++){
            // player
            JsonObject playerJSON = tableStateJSON.getAsJsonObject(String.valueOf(i));
            map.put(i, new PlayerDto(playerJSON));
        }
        return map;
    }

    // example data:
    // {"sbPosition":0,"bbPosition":1,"dealerPosition":3}
    public static Map<String, Integer> parseButtonsJSON(JsonObject buttonsJSON){
        Map<String, Integer> map = new HashMap<>();
        Integer sbPosition = buttonsJSON.get("sbPosition").getAsInt();
        Integer bbPosition = buttonsJSON.get("bbPosition").getAsInt();
        Integer dealerPosition = buttonsJSON.get("dealerPosition").getAsInt();
        map.put("sbPosition", sbPosition);
        map.put("bbPosition", bbPosition);
        map.put("dealerPosition", dealerPosition);
        return map;
    }

    // example data:
    // {"ante":0,"sb":30,"bb":60}
    public static Map<String, Integer> parseBetsJSON(JsonObject betsJSON){
        Map<String, Integer> map = new HashMap<>();
        Integer ante = betsJSON.get("ante").getAsInt();
        Integer sb = betsJSON.get("sb").getAsInt();
        Integer bb = betsJSON.get("bb").getAsInt();
        map.put("ante", ante);
        map.put("sb", sb);
        map.put("bb", bb);
        return map;
    }

    public static String addButtons(){
        return null;
    }

//    public static Map<String, Integer> parsePlayerMoveJSON(JsonObject commandJSON) {
//    }


//        for(Player p: map.values()){
//
//            System.out.println(p.getName());
//        }
//
////            System.out.println(jsonObject.getAsJsonObject("0").get("name"));
//    } catch (
//    FileNotFoundException e) {
//        e.printStackTrace();
//    }

//    private String displaySuits() {
//        ArrayList<Card> cards = hand.getCards();
//        StringBuilder suits = new StringBuilder();
//        for (int i = 0; i < cards.size(); i++) {
//            Card card = cards[i]; suits.append(" ");
//            suits.append(card.getRank());
//            switch (card.getSuit()) {
//                case SPADE:
//                    suits.append((char)'\u2660');
//                    break;
//                case DIAMOND:
//                    suits.append((char)'\u2666');
//                    break;
//                case CLUB:
//                    suits.append((char)'\u2663');
//                    break;
//                case HEART: suits.append((char)'\u2764');
//                break;
//            }
//        }
//        return suits.toString(); }
//
}
