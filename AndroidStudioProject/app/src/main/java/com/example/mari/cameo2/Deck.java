package com.example.mari.cameo2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;
    private int[] cardsImages;
    private int curCard;

    public Deck(){
        initializeDeck();
    }

    // Initialize Deck
    public void initializeDeck(){
        curCard = 0;
        cards = null;
        cards = new ArrayList<>(54);

        for(int i = 0; i < 13; i++) {
            cards.add(new Card('s', i + 1));
            cards.add(new Card('c', i + 1));
            cards.add(new Card('h', i + 1));
            cards.add(new Card('d', i + 1));
        }
        cards.add(new Card('j', 1)); // black
        cards.add(new Card('j', 2)); // red
    }

    public int size(){
        return cards.size();
    }

    public void shuffleDeck(){
        Collections.shuffle(this.cards);
    }

    public Card drawCard()
    {
        Card temp = cards.get(0);
        cards.remove(0);
        if (cards.size() == 0)
        {
            // TODO; game end
        }
        return temp;
    }
}
