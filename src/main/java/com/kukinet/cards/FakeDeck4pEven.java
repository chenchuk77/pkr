package com.kukinet.cards;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class FakeDeck4pEven implements Serializable, Deck {

    private static final long serialVersionUID = 2463644121163649891L;
    private List<Card> cards;

    public FakeDeck4pEven() {
        this(new Random());
    }

    public FakeDeck4pEven(Random random) {
        createDeck();

    }

    private void createDeck() {
        // dealing 4 players any hand, then board is AAAAK which is always equal among all players
        cards = new ArrayList<Card>();
        // player 1-4 (8 cards)
        cards.add(new Card(CardSuitEnum.c, CardRankEnum._2));
        cards.add(new Card(CardSuitEnum.c, CardRankEnum._3));
        cards.add(new Card(CardSuitEnum.c, CardRankEnum._4));
        cards.add(new Card(CardSuitEnum.c, CardRankEnum._5));
        cards.add(new Card(CardSuitEnum.c, CardRankEnum._6));
        cards.add(new Card(CardSuitEnum.c, CardRankEnum._7));
        cards.add(new Card(CardSuitEnum.c, CardRankEnum._8));
        cards.add(new Card(CardSuitEnum.c, CardRankEnum._9));

        // muck pre flop
        cards.add(new Card(CardSuitEnum.d, CardRankEnum._2));
        // flop (AAA)
        cards.add(new Card(CardSuitEnum.h, CardRankEnum.A));
        cards.add(new Card(CardSuitEnum.d, CardRankEnum.A));
        cards.add(new Card(CardSuitEnum.c, CardRankEnum.A));

        // muck pre turn
        cards.add(new Card(CardSuitEnum.d, CardRankEnum._3));
        // turn (A)
        cards.add(new Card(CardSuitEnum.s, CardRankEnum.A));

        // muck pre river
        cards.add(new Card(CardSuitEnum.d, CardRankEnum._4));
        // river (K)
        cards.add(new Card(CardSuitEnum.h, CardRankEnum.K));



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