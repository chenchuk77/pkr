package com.kukinet.cards;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class RealDeck implements Serializable, Deck {

    private static final long serialVersionUID = 2463644121163649891L;

    private List<Card> cards;
    //private Random random;

    public RealDeck() {
        this(new Random());
    }

    public RealDeck(Random random) {
        //this.random = random;
        createDeck();
        shuffle();

    }

    private void shuffle(){
         Collections.shuffle(cards);
    }

    private void createDeck() {
        cards = new ArrayList<Card>();
        for (CardSuitEnum suit : CardSuitEnum.values()) {
            for (CardRankEnum rank : CardRankEnum.values()) {
                cards.add(new Card(suit, rank));
            }
        }
    }

    @Override
    public Card pop() {
        //return cards.remove(random.nextInt(cards.size()));
        return cards.remove(0);
    }

    @Override
    public String showDeck() {
        String s = "";
        for (Card card: cards){
            s += card + ", ";
        }
        return s;
    }

}