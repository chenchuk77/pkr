package com.kukinet.client;

import com.google.gson.JsonObject;

// client lightweight representation of a player
// should behave like a javascript client
//
public class PlayerDto {

    private String name;
//    private int startingStack;
    private int chips;
    private int commited;
    private boolean inGame;
    private boolean inHand;
    private String strHole1;
    private String strHole2;

//    public PlayerDto(JsonObject jsonObject, String hc1, String hc2){
//        this.name = jsonObject.get("name").getAsString();
//        this.startingStack = jsonObject.get("startingStack").getAsInt();
//        this.chips = jsonObject.get("chips").getAsInt();
//        this.commited = jsonObject.get("commited").getAsInt();
//        this.inGame = jsonObject.get("inGame").getAsBoolean();
//        this.inHand = jsonObject.get("inHand").getAsBoolean();
//        this.strHole1 = hc1;
//        this.strHole2 = hc2;
//    }
//public PlayerDto(JsonObject jsonObject){
//    this.name = jsonObject.get("name").getAsString();
//    this.startingStack = jsonObject.get("startingStack").getAsInt();
//    this.chips = jsonObject.get("chips").getAsInt();
//    this.commited = jsonObject.get("commited").getAsInt();
//    this.inGame = jsonObject.get("inGame").getAsBoolean();
//    this.inHand = jsonObject.get("inHand").getAsBoolean();
//    this.strHole1 = "XX";
//    this.strHole2 = "XX";
//}

    public PlayerDto(JsonObject jsonObject){
        this.name = jsonObject.get("name").getAsString();
//        this.startingStack = jsonObject.get("startingStack").getAsInt();
        this.chips = jsonObject.get("chips").getAsInt();
        this.commited = jsonObject.get("commited").getAsInt();
        this.inGame = jsonObject.get("inGame").getAsBoolean();
        this.inHand = jsonObject.get("inHand").getAsBoolean();
        this.strHole1 = jsonObject.get("strHole1").getAsString();
        this.strHole2 = jsonObject.get("strHole2").getAsString();
    }

//    public void updateFromJson(JsonObject jsonObject){
//
//    }
//

    public int commited(){ return this.commited; }
//    public int getStartingStack() {return startingStack;}
//    public void setStartingStack(int startingStack) {this.startingStack = startingStack;}
    public int getChips() {return chips;}
    public void setChips(int chips) {this.chips = chips;}
    public String getName() {return name;}
    public boolean inHand(){ return this.inHand; }
    public boolean inGame(){ return this.inGame; }
    public void setInGame(boolean inGame) {this.inGame = inGame;}
    public void setStrHole1(String strHole1) {this.strHole1 = strHole1;}
    public void setStrHole2(String strHole2) {this.strHole2 = strHole2;}
    public void setInHand(boolean inHand) {


        this.inHand = inHand;
        if (!inHand){
            System.out.println("im not inHand ! ,my cards were: " + strHole1 + " - " + strHole2);

            this.strHole1 = "00";
            this.strHole2 = "00";
            System.out.println("im not inHand ! ,my cards now : " + strHole1 + " - " + strHole2);

        }
    }

    public String getStrHole1() {
        if (strHole1.equals("00")) return "  ";
        //if (strHole1.equals("XX")) return "XX";
        if (strHole1.equals("XX")) return "\u2680 ";

        return strHole1;
    }

    public String getStrHole2() {
        if (strHole2.equals("00")) return "  ";
//        if (strHole2.equals("XX")) return "XX";
        if (strHole2.equals("XX")) return "\u2680 ";
        return strHole2;
    }
}
