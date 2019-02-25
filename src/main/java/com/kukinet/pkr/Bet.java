package com.kukinet.pkr;

/* bet represent a player commitment to the pot in a single hand.
if Player p1 raise to 60, a Bet b1 created. if p2 reraise to 60 and p1 calls,
then the b1 got updated to 60 (total 60 commited by p1). we also got b2 from p2.
handrank will be set later upon hand evaluation.
if this hand wins, then ONLY the commited value will be taken from each player (allow main/side pot)
and the bet will be removed, allowing to check other bets ...
in the end, each bet is evaluated and split between players
*/
public class Bet {
    private int commited;
    private Player player;
    private int handRank;

    public Bet(int commited, Player p){
        this.player=p;
        this.commited=commited;
    }
    // associates a hand with a bet. if this hand wins
    public void setHandRank(int handRank){this.handRank=handRank;}
    public int getHandRank() {return handRank;}
    public Player getPlayer() {return player;}
}