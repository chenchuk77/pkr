package com.kukinet.pkr;

// example json source to build an object
// {"handType":5,"handRank":2,"value":20482,"handName":"straight"}
public class Hand {

    public Hand() {
    }

    private int handType;
    private int handRank;
    private int value;
    private String handName;


    public int getHandType() {
        return handType;
    }

    public int getHandRank() {
        return handRank;
    }

    public int getValue() {
        return value;
    }

    public String getHandName() {
        return handName;
    }

    public void setHandType(int handType) {
        this.handType = handType;
    }

    public void setHandRank(int handRank) {
        this.handRank = handRank;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setName(String handName) {
        this.handName = handName;
    }
}