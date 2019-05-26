package com.example.mari.cameo2;

public class Card {
    private char suit;
    private int num;
    private int drawabeReference;
    private int[] cardsImages = {
            R.drawable.black_joker,
            R.drawable.c1, R.drawable.c2, R.drawable.c3, R.drawable.c4,
            R.drawable.c5, R.drawable.c6, R.drawable.c7, R.drawable.c8,
            R.drawable.c9, R.drawable.c10, R.drawable.c11, R.drawable.c12, R.drawable.c13,
            R.drawable.h1, R.drawable.h2, R.drawable.h3, R.drawable.h4,
            R.drawable.h5, R.drawable.h6, R.drawable.h7, R.drawable.h8,
            R.drawable.h9, R.drawable.h10, R.drawable.h11, R.drawable.h12, R.drawable.h13,
            R.drawable.d1, R.drawable.d2, R.drawable.d3, R.drawable.d4,
            R.drawable.d5, R.drawable.d6, R.drawable.d7, R.drawable.d8,
            R.drawable.d9, R.drawable.d10, R.drawable.d11, R.drawable.d12, R.drawable.d13,
            R.drawable.s1, R.drawable.s2, R.drawable.s3, R.drawable.s4,
            R.drawable.s5, R.drawable.s6, R.drawable.s7, R.drawable.s8,
            R.drawable.s9, R.drawable.s10, R.drawable.s11, R.drawable.s12, R.drawable.s13,
            R.drawable.red_joker
    };

    public char getSuit() {
        return suit;
    }

    public int getNum() {
        return num;
    }
    public int getImage() { return drawabeReference; }

    public void setNum(int num) {
        this.num = num;
    }

    public Card(){}
    public Card(char letter, int number) {
        this.suit = letter;
        this.num = number;
        if (letter == 'c')
        {
            drawabeReference = cardsImages[number];
        }
        else if (letter == 'h')
        {
            drawabeReference = cardsImages[number + 13];
        }
        else if (letter == 'd')
        {
            drawabeReference = cardsImages[number + 26];
        }
        else if (letter == 's')
        {
            drawabeReference = cardsImages[number + 39];
        }
        else // jorker
        {
            if (number == 1)
            {
                drawabeReference = cardsImages[0];
                this.num = 0;
            }
            else if (number == 2)
            {
                drawabeReference = cardsImages[53];
                this.num = 0;
            }
        }
    }
}
