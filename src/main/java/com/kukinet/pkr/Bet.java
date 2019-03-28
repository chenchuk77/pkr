package com.kukinet.pkr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* bet represent a player commitment to the pot in a single hand.
its being used inside a Pot object
*/
public class Bet {

    private int amount;
    private Player player;
    private Logger logger = LoggerFactory.getLogger(Bet.class);

    public Bet(int amount, Player player){
        logger.warn("bet created for player {} with amount {}", player.getName(), amount);
        this.player=player;
        this.amount=amount;

    }

    public Player getPlayer() {return player;}
    public void setAmount(int amount) {this.amount = amount;}
    public int getAmount() {return amount;}


    @Override
    public String toString() {
        return "Bet{" +
                "player=" + player.getName() +
                ",amount=" + player.commited() +
                '}';
    }
}