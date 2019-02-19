package com.kukinet.pkr;

import com.kukinet.cards.Card;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Objects;

public class Player {

    // will not be serialized with gson
    private transient Table table;
    private transient boolean waitForAction;
    private transient Card holeCard1;
    private transient Card holeCard2;

    private transient ActionCommand actionCommand;

    private String name;
    private int startingStack;
    private int effectiveStack;
    private int commited;

    private boolean inGame;
    private boolean inHand;

    // string representation to be sent in JSON updates
    private String strHole1;
    private String strHole2;
    public String getStrHole1() {return strHole1;}
    public void setStrHole1(String strHole1) {this.strHole1 = strHole1;}
    public String getStrHole2() {return strHole2;}
    public void setStrHole2(String strHole2) {this.strHole2 = strHole2;}

//    private List<Card> pocketCards;

    public Player(){}


    // {
    // "0":
    // {"name":"ddd","startingStack":10000,"effectiveStack":10000,"commited":0,"inGame":true,"inHand":true},
    // "1":
    // {"name":"eee","startingStack":10000,"effectiveStack":10000,"commited":0,"inGame":true,"inHand":true},"2":{"name":"fff","startingStack":10000,"effectiveStack":10000,"commited":0,"inGame":true,"inHand":true},"3":{"name":"iii","startingStack":10000,"effectiveStack":10000,"commited":0,"inGame":true,"inHand":true}}

    // for java console client to create a player from the received json
    public Player(JsonObject jsonObject){
        this.name = jsonObject.get("name").getAsString();
        this.startingStack = jsonObject.get("startingStack").getAsInt();
        this.effectiveStack = jsonObject.get("effectiveStack").getAsInt();
        this.commited = jsonObject.get("commited").getAsInt();
        this.inGame = jsonObject.get("inGame").getAsBoolean();
        this.inHand = jsonObject.get("inHand").getAsBoolean();

    }


    //jsonObject

    public Player(String name){
        this.name=name;
        this.waitForAction=false;
        this.actionCommand=null;
        this.startingStack=10000;
        this.effectiveStack=10000;
        this.holeCard1=null;
        this.holeCard1=null;
        this.inGame = true;
        this.inHand = true;
        this.strHole1 = "XX";
        this.strHole2 = "XX";

    }

    public String getPlayerState(){
        return this.getName() + ":" +
                this.inGame + ":" +
                this.inHand + ":" +
                this.startingStack + ":" +
                this.effectiveStack + ":" +
                this.commited;

    }

//    data='players=[ddd,false,10000,10000,0,eee,false,10000,10000,0,fff,false,10000,10000,0,iii,false,10000,10000,0,],buttons=302,ante=0,sb=0,bb=0'
//
//    var players = data.split(',')[0].split('=')[1].split()


//    public void setPocketCards(List<Card> pocketCards) {
//        this.pocketCards = pocketCards;
//    }

    public String getName() {return name;}
    public Table getTable() {return table;}
    public void setHoleCard1(Card holeCard1) { this.holeCard1 = holeCard1; }
    public void setHoleCard2(Card holeCard2) { this.holeCard2 = holeCard2; }
    public void setTable(Table table) {this.table = table;}
    public boolean inHand(){ return this.inHand; }
    public boolean inGame(){ return this.inGame; }
    public int commited(){ return this.commited; }
    public boolean isWaitForAction() {return waitForAction;}
    public void setWaitForAction(boolean waitForAction) {this.waitForAction = waitForAction;}
    public ActionCommand getActionCommand() {return actionCommand;}
    public void setActionCommand(ActionCommand actionCommand) {this.actionCommand = actionCommand;}

    public int getStartingStack() {return startingStack;}
    public void setStartingStack(int startingStack) {this.startingStack = startingStack;}
    public int getEffectiveStack() {return effectiveStack;}
    public void setEffectiveStack(int effectiveStack) {this.effectiveStack = effectiveStack;}

    public void init(){
        inHand = true;
        commited = 0;

    }

    public void setInHand(boolean inHand) {
        this.inHand = inHand;
        if (!inHand){
            this.strHole1 = "00";
            this.strHole2 = "00";
        }
        if (inHand){
            this.strHole1 = "XX";
            this.strHole2 = "XX";
        }

    }

    public void fold(){
        setInHand(false);
    }
    public void seatOut(){ this.inGame = false; }
    public void chk(){

    }
    public int call(int amount){
        if (amount <= effectiveStack ){
            effectiveStack = effectiveStack - amount;
            commited = commited + amount;
            return amount;
        } else {
            // all-in
            call(effectiveStack);
        }
        return 0 ;
    }
    public int raise(int amount){
        if (amount <= effectiveStack ){
            effectiveStack = effectiveStack - amount;
            commited = commited + amount;
            return amount;
        } else {
            // all-in
            raise(effectiveStack);
        }
        return 0 ;
    }
    public int postSmallBlind(int sb){
        if (sb <= effectiveStack ){
            effectiveStack = effectiveStack - sb;
            commited = commited + sb;
            return sb;
        } else {
            // all-in
            postSmallBlind(effectiveStack);
        }

        return 0 ;
    }
    public int postBigBlind(int bb){
        if (bb <= effectiveStack ){
            effectiveStack = effectiveStack - bb;
            commited = commited + bb;
            return bb;
        } else {
            // all-in
            postBigBlind(effectiveStack);
        }

        return 0 ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return effectiveStack == player.effectiveStack &&
                commited == player.commited &&
                inHand == player.inHand &&
                Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, effectiveStack, commited, inHand);
    }

    public void sitout() {
        this.inHand = false;

    }
}