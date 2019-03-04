package com.kukinet.pkr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* bet represent a player commitment to the pot in a single hand.
if Player p1 raise to 60, a Bet b1 created. if p2 reraise to 60 and p1 calls,
then the b1 got updated to 60 (total 60 commited by p1). we also got b2 from p2.
handrank will be set later upon hand evaluation.
if this hand wins, then ONLY the commited value will be taken from each player (allow main/side pot)
and the bet will be removed, allowing to check other bets ...
in the end, each bet is evaluated and split between players
*/
public class Bet {
    private int amount;
    private Player player;
//    private int handValue;
    private Logger logger = LoggerFactory.getLogger(Bet.class);

    public Bet(int amount, Player player){

        logger.warn("bet created for player {} with amount {}", player.getName(), amount);

        this.player=player;
//        this.player.setCommited(commited);
        this.amount=amount;

    }
    // associates a hand with a bet. if this hand wins
//    public void setHandValue(int handValue){this.handValue = handValue;}
//    public int getHandValue() {return handValue;}
    public Player getPlayer() {return player;}


    @Override
    public String toString() {
        return "Bet{" +
                "player=" + player.getName() +
                ",amount=" + player.commited() +
                '}';
    }

    public int getAmount() {
        return amount;
    }
}