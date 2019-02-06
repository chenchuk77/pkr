package com.kukinet.pkr;

import java.util.Objects;

public class Player {

    private Table table;
    private String name;
    private int stack;
    private int commited;
    private boolean inHand;

    public Player(){}
    public Player(String name){
        this.name=name;
        this.stack=10000;
    }

    public String getName() {
        return name;
    }
    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public void init(){
        inHand = false;
        commited = 0;

    }

    public void fold(){
        inHand = false;
    }
    public void chk(){

    }
    public int call(int amount){
        if (amount <= stack ){
            stack = stack - amount;
            commited = commited + amount;
            return amount;
        } else {
            // all-in
            call(stack);
        }
        return 0 ;
    }
    public int raise(int amount){
        if (amount <= stack ){
            stack = stack - amount;
            commited = commited + amount;
            return amount;
        } else {
            // all-in
            raise(stack);
        }
        return 0 ;
    }
    public int postSmallBlind(int sb){
        if (sb <= stack ){
            stack = stack - sb;
            commited = commited + sb;
            return sb;
        } else {
            // all-in
            postSmallBlind(stack);
        }

        return 0 ;
    }
    public int postBigBlind(int bb){
        if (bb <= stack ){
            stack = stack - bb;
            commited = commited + bb;
            return bb;
        } else {
            // all-in
            postBigBlind(stack);
        }

        return 0 ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return stack == player.stack &&
                commited == player.commited &&
                inHand == player.inHand &&
                Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, stack, commited, inHand);
    }

    public boolean inHand(){ return this.inHand; }
    public int commited(){ return this.commited; }
}