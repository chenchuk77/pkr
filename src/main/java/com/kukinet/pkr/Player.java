package com.kukinet.pkr;

import com.kukinet.cards.Card;
import com.google.gson.JsonObject;

import java.util.Objects;

public class Player {

    // will not be serialized with gson
    private transient Table table;
    private transient boolean waitForAction;
    private transient Card holeCard1;
    private transient Card holeCard2;
    private transient Hand hand;
    private transient ActionCommand actionCommand;

    private String name;
    private int chips;
    private int commited;
    private boolean isChecking;
    private int position;

    private boolean inGame;
    private boolean inHand;

    // string representation to be sent in JSON updates
    private String strHole1;
    private String strHole2;
    public String getStrHole1() {return strHole1;}
    public void setStrHole1(String strHole1) {this.strHole1 = strHole1;}
    public String getStrHole2() {return strHole2;}
    public void setStrHole2(String strHole2) {this.strHole2 = strHole2;}

    public String getHoleCards(){
        return this.holeCard1.toString() + this.holeCard2.toString();
    }
//    private List<Card> pocketCards;

    public Player(){}


    // {
    // "0":
    // {"name":"ddd","startingStack":10000,"chips":10000,"commited":0,"inGame":true,"inHand":true},
    // "1":
    // {"name":"eee","startingStack":10000,"chips":10000,"commited":0,"inGame":true,"inHand":true},"2":{"name":"fff","startingStack":10000,"chips":10000,"commited":0,"inGame":true,"inHand":true},"3":{"name":"iii","startingStack":10000,"chips":10000,"commited":0,"inGame":true,"inHand":true}}

    // for java console client to create a player from the received json
    public Player(JsonObject jsonObject){
        this.name = jsonObject.get("name").getAsString();
//        this.startingStack = jsonObject.get("startingStack").getAsInt();
        this.chips = jsonObject.get("chips").getAsInt();
        this.commited = jsonObject.get("commited").getAsInt();
        this.inGame = jsonObject.get("inGame").getAsBoolean();
        this.inHand = jsonObject.get("inHand").getAsBoolean();
    }


    //jsonObject

    public Player(String name){
        this.name=name;
        this.isChecking=false;
        this.waitForAction=false;
        this.actionCommand=null;
//        this.startingStack=10000;
        this.chips =10000;
        this.inGame = true;
        this.inHand = true;
        muckCards();
    }







    // TODO: clarify whats going on here
    public void muckCards(){
        this.hand=null;
        this.holeCard1=null;
        this.holeCard1=null;
        this.strHole1 = "XX";
        this.strHole2 = "XX";
    }
    public String getPlayerState(){
        return this.getName() + ":" +
                this.inGame + ":" +
                this.inHand + ":" +
//                this.startingStack + ":" +
                this.chips + ":" +
                this.commited;

    }

//    data='players=[ddd,false,10000,10000,0,eee,false,10000,10000,0,fff,false,10000,10000,0,iii,false,10000,10000,0,],buttons=302,ante=0,sb=0,bb=0'
//
//    var players = data.split(',')[0].split('=')[1].split()


//    public void setPocketCards(List<Card> pocketCards) {
//        this.pocketCards = pocketCards;
//    }

    public Card getHoleCard1() {return holeCard1;}
    public Card getHoleCard2() {return holeCard2;}
    public String getName() {return name;}
    public Table getTable() {return table;}
    public void setHoleCard1(Card holeCard1) { this.holeCard1 = holeCard1; }
    public void setHoleCard2(Card holeCard2) { this.holeCard2 = holeCard2; }
    public void setTable(Table table) {this.table = table;}
    public void setSeatPosition(int position) {this.position = position;}
    public int getSeatPosition() {return this.position;}

    public void setInGame(boolean inGame) {this.inGame = inGame;}
    public boolean inHand(){ return this.inHand; }
    public boolean inGame(){ return this.inGame; }
    public int commited(){ return this.commited; }
    public boolean isWaitForAction() {return waitForAction;}
    public void setWaitForAction(boolean waitForAction) {this.waitForAction = waitForAction;}
    public ActionCommand getActionCommand() {return actionCommand;}
    public void setActionCommand(ActionCommand actionCommand) {this.actionCommand = actionCommand;}
    public void setCommited(int commited) {this.commited = commited;}
    public void setHand(Hand hand) {this.hand = hand;}
    public Hand getHand() {return hand;}

    public boolean isChecking() {return isChecking;}
    public void setChecking(boolean checking) {isChecking = checking;}

//    public int getStartingStack() {return startingStack;}
//    public void setStartingStack(int startingStack) {this.startingStack = startingStack;}
    public int getChips() {return chips;}
    public void setChips(int chips) {this.chips = chips;}

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
        if (amount <= chips){
            chips = chips - amount;
//            commited = commited + amount;
            return amount;
        } else {
            // all-in
            call(chips);
        }
        return 0 ;
    }
    public int bet(int amount){
        if (amount <= chips){
            chips = chips - amount;
//            commited = commited + amount;
            return amount;
        } else {
            // all-in
            bet(chips);
        }
        return 0 ;
    }
    public int raise(int amount){
        if (amount <= chips){
            chips = chips - amount;
//            commited = commited + amount; // Pot will create Bet instead of the player
            return amount;
        } else {
            // all-in
            raise(chips);
        }
        return 0 ;
    }
    public int postSmallBlind(int sb){
        if (sb <= chips){
            chips = chips - sb;
//            commited = commited + sb;
            return sb;
        } else {
            // all-in
            postSmallBlind(chips);
        }

        return 0 ;
    }
    public int postBigBlind(int bb){
        if (bb <= chips){
            chips = chips - bb;
//            commited = commited + bb;
            return bb;
        } else {
            // all-in
            postBigBlind(chips);
        }

        return 0 ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Player player = (Player) o;
//        return chips == player.chips &&
//                commited == player.commited &&
//                inHand == player.inHand &&
//                Objects.equals(name, player.name);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(name, chips, commited, inHand);
//    }

    public void sitout() {
        this.inHand = false;

    }

}