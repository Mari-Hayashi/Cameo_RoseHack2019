package com.example.mari.cameo2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.os.CountDownTimer;
import android.view.View.OnClickListener;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;

import java.util.ArrayList;
import android.content.Intent;
import java.util.List;
import java.util.Random;


    enum state{DRAW1, CHOOSE1, SEETOP1, SEEBOTTOM1, GETNEW1,CAMEO1, DRAW2, CHOOSE2, SEETOP2, SEEBOTTOM2, GETNEW2, CAMEO2, LOOKING1, LOOKING2, GAMEEND}
public class GameActivity extends AppCompatActivity{
    state curState;
    Button button7;
    Button button9;
    Button button6;
    Button button8;
    Button btt_p1_c1;
    Button btt_p1_c2;
    Button btt_p1_c3;
    Button btt_p1_c4;
    Button btt_p1_c5;
    Button btt_p1_c6;
    Button btt_p2_c1;
    Button btt_p2_c2;
    Button btt_p2_c3;
    Button btt_p2_c4;
    Button btt_p2_c5;
    Button btt_p2_c6;
    Button btt_deck1;
    Button btt_deck2;
    Button btt_discard1;
    Button btt_discard2;

    TextView TimerText1;
    TextView TimerText2;

    public List<Card> p1cards;
    public List<Card> p2cards;

    private int NUM_INITIAL_CARD = 4;
    private int NUM_MAX_CARD = 6;

    private boolean Tapped;
    private Card CurCard;
    int Looking;
    private boolean Cameo; // true if player called cameo

    Deck deck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);

        Tapped = false;
        Cameo = false;
        curState = state.DRAW1;
        deck = new Deck();
        deck.shuffleDeck();
        DealCardsToPlayers();

        assignButtons();
        TimerText1.setText(R.string.draw);
        TimerText2.setText(R.string.wait);
    }

    // Button functions
    private void TopCard(int player, int loc){
        if (getcard(player,loc) == null) return;
        else if (CHOOSE(player)) switchCard(loc);
        else if (SEETOP(player)) {
            button(player,loc).setBackgroundResource(getcard(player,loc).getImage());
            Looking = player * 10 + loc;
            if (player == 1) curState = state.LOOKING1;
            else curState = state.LOOKING2;
            UpdateText();
        }
        else if (LOOKING(player) && Looking == player * 10 + loc){
            button(player,loc).setBackgroundResource(R.drawable.card);
            if (player == 1) turnChange(2);
            else turnChange(1);
        }
        else if (GETNEW(player)) {
            Card newCard = deck.drawCard();
            deck(player).setBackgroundResource(getcard(player,loc).getImage());
            setcard(player,loc,newCard);
            if (player == 1) turnChange(2);
            else turnChange(1);
        }
        else if (tapAble(player))
        {
            if (Tapped) return;
            Tapped = true;
            if (CurCard.getNum() == getcard(player,loc).getNum()){
                deck(player).setBackgroundResource(getcard(player,loc).getImage());
                button(player,loc).setBackgroundResource(R.drawable.emptycard);
                setcard(player,loc,null);
                if (DRAW(player)){
                    text(player).setText(R.string.draw);
                } else {
                    text(player).setText(R.string.wait);
                }
            } else {
                giveCard(player);
                if (DRAW(player)){
                    text(player).setText(R.string.wrong_draw);
                } else {
                    text(player).setText(R.string.wrong);
                }
            }
        }
    }
    private void BottomCard(int player, int loc){
        if (getcard(player,loc) == null) return;
        else if (CHOOSE(player)) switchCard(loc);
        else if (SEEBOTTOM(player)) {
            button(player,loc).setBackgroundResource(getcard(player,loc).getImage());
            Looking = player * 10 + loc;
            if (player == 1) curState = state.LOOKING1;
            else curState = state.LOOKING2;
            UpdateText();
        }
        else if (LOOKING(player) && Looking == player * 10 + loc){
            button(player,loc).setBackgroundResource(R.drawable.card);
            if (player == 1) turnChange(2);
            else turnChange(1);
        }
        else if (GETNEW(player)) {
            Card newCard = deck.drawCard();
            deck(player).setBackgroundResource(getcard(player,loc).getImage());
            setcard(player,loc,newCard);
            if (player == 1) turnChange(2);
            else turnChange(1);
        }
        else if (tapAble(player))
        {
            if (Tapped) return;
            Tapped = true;
            if (CurCard.getNum() == getcard(player,loc).getNum()){
                deck(player).setBackgroundResource(getcard(player,loc).getImage());
                button(player,loc).setBackgroundResource(R.drawable.emptycard);
                setcard(player,loc,null);
                if (DRAW(player)){
                    text(player).setText(R.string.draw);
                } else {
                    text(player).setText(R.string.wait);
                }
            } else {
                giveCard(player);
                if (DRAW(player)){
                    text(player).setText(R.string.wrong_draw);
                } else {
                    text(player).setText(R.string.wrong);
                }
            }
        }
    }
    private void DisplayCard(int player){
        if (CHOOSE(player)) action();
    }
    private void CameoButton(int player){
        if (DRAW(player)){
            if (player == 1){
                turnChange(2);
                curState = state.CAMEO1;
            } else if (player == 2){
                turnChange(1);
                curState = state.CAMEO2;
            }
            Cameo = true;
            UpdateText();
        }
    }
    private void DrawButton(int player){
        if (player == 1 && (DRAW(1) || curState == state.CAMEO2)) drawCard();
        else if (player == 2 && (DRAW(2) || curState == state.CAMEO1)) drawCard();
        //if (DRAW(player) || CAMEO(player - 1)) drawCard();
    }

    // VOID FUNCTIONS
    private void drawCard() {
        if (curState == state.DRAW1 || curState == state.CAMEO2){
            CurCard = deck.drawCard();
            btt_discard1.setBackgroundResource(CurCard.getImage());
            curState = state.CHOOSE1;
        }
        else if (curState == state.DRAW2 || curState == state.CAMEO1){
            CurCard = deck.drawCard();
            btt_discard2.setBackgroundResource(CurCard.getImage());
            curState = state.CHOOSE2;
        }
        UpdateText();
    }
    private void assignButtons() {
        button7 = findViewById(R.id.button7);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawButton(1);
            }
        });
        button9 = findViewById(R.id.button9);
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameoButton(1);
            }
        });
        button6 = findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameoButton(2);
            }
        });
        button8 = findViewById(R.id.button8);
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawButton(2);
            }
        });


        btt_p1_c1 = findViewById(R.id.btt_p1_c1);
        btt_p1_c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopCard(1,1);
            }
        });
        btt_p1_c2 = findViewById(R.id.btt_p1_c2);
        btt_p1_c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopCard(1,2);
            }
        });
        btt_p1_c3 = findViewById(R.id.btt_p1_c3);
        btt_p1_c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomCard(1,3);
            }
        });
        btt_p1_c4 = findViewById(R.id.btt_p1_c4);
        btt_p1_c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomCard(1,4);
            }
        });
        btt_p1_c5 = findViewById(R.id.btt_p1_c5);
        btt_p1_c5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopCard(1,5);
            }
        });
        btt_p1_c6 = findViewById(R.id.btt_p1_c6);
        btt_p1_c6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomCard(1,6);
            }
        });
        btt_p2_c1 = findViewById(R.id.btt_p2_c1);
        btt_p2_c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopCard(2,1);
            }
        });
        btt_p2_c2 = findViewById(R.id.btt_p2_c2);
        btt_p2_c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopCard(2,2);
            }
        });
        btt_p2_c3 = findViewById(R.id.btt_p2_c3);
        btt_p2_c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomCard(2,3);
            }
        });
        btt_p2_c4 = findViewById(R.id.btt_p2_c4);
        btt_p2_c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomCard(2,4);
            }
        });
        btt_p2_c6 = findViewById(R.id.btt_p2_c6);
        btt_p2_c6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomCard(2,6);
            }
        });
        btt_p2_c5 = findViewById(R.id.btt_p2_c5);
        btt_p2_c5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopCard(2,5);
            }
        });
        btt_discard1 = findViewById(R.id.btt_discard1);
        btt_discard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayCard(1);
            }
        });
        btt_discard2 = findViewById(R.id.btt_discard2);
        btt_discard2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayCard(2);
            }
        });
        TimerText1 = findViewById(R.id.timer1);
        TimerText2 = findViewById(R.id.timer2);
        btt_deck1 = findViewById(R.id.btt_deck1);
        btt_deck2 = findViewById(R.id.btt_deck2);
    }
    public void DealCardsToPlayers() {
        p1cards = new ArrayList<>(NUM_MAX_CARD);
        p2cards = new ArrayList<>(NUM_MAX_CARD);
        p1cards.add(null);
        p2cards.add(null);
        for (int i = 0; i < NUM_INITIAL_CARD; ++i)
        {
            p1cards.add(deck.drawCard());
            p2cards.add(deck.drawCard());
        }
        for (int i = NUM_INITIAL_CARD; i < NUM_MAX_CARD; ++i)
        {
            p1cards.add(null);
            p2cards.add(null);
        }
    }
    private void action(){
        if (curState == state.CHOOSE1){
            btt_deck2.setBackgroundResource(CurCard.getImage());
        }
        else if (curState == state.CHOOSE2){
            btt_deck1.setBackgroundResource(CurCard.getImage());
        }
        Tapped = false;
         switch(CurCard.getNum())
         {
             case 7:
             case 8:
                 if (curState == state.CHOOSE1) curState = state.SEEBOTTOM1;
                 if (curState == state.CHOOSE2) curState = state.SEEBOTTOM2;
                 break;
             case 9:
             case 10:
                 if (curState == state.CHOOSE1) curState = state.SEETOP1;
                 if (curState == state.CHOOSE2) curState = state.SEETOP2;
                 break;
             case 11:
             case 12:
                 if (curState == state.CHOOSE1) curState = state.GETNEW1;
                 if (curState == state.CHOOSE2) curState = state.GETNEW2;
                 break;
             default:
                 if (curState == state.CHOOSE1) {
                     turnChange(2);
                 }
                 if (curState == state.CHOOSE2) {
                     turnChange(1);
                 }

         }
        UpdateText();
    }
    private void switchCard(int loc) {
        if (curState == state.CHOOSE1) {
            Card change = p1cards.get(loc);
            btt_deck1.setBackgroundResource(change.getImage());
            btt_deck2.setBackgroundResource(change.getImage());
            p1cards.set(loc, CurCard);
            CurCard = change;
            turnChange(2);
        }
        else if (curState == state.CHOOSE2) {
            Card change = p2cards.get(loc);
            btt_deck2.setBackgroundResource(change.getImage());
            btt_deck1.setBackgroundResource(change.getImage());
            p2cards.set(loc, CurCard);
            CurCard = change;
            turnChange(1);
        }
        Tapped = false;
        UpdateText();
    }
    private void endGame(){
        curState = state.GAMEEND;
        btt_discard1.setBackgroundResource(R.drawable.emptycard);
        btt_discard2.setBackgroundResource(R.drawable.emptycard);
        btt_deck1.setBackgroundResource(R.drawable.emptycard);
        btt_deck2.setBackgroundResource(R.drawable.emptycard);

        if (p1cards.get(1) != null) btt_p1_c1.setBackgroundResource(p1cards.get(1).getImage());
        if (p1cards.get(2) != null) btt_p1_c2.setBackgroundResource(p1cards.get(2).getImage());
        if (p1cards.get(3) != null) btt_p1_c3.setBackgroundResource(p1cards.get(3).getImage());
        if (p1cards.get(4) != null) btt_p1_c4.setBackgroundResource(p1cards.get(4).getImage());
        if (p1cards.get(5) != null) btt_p1_c5.setBackgroundResource(p1cards.get(5).getImage());
        if (p1cards.get(6) != null) btt_p1_c6.setBackgroundResource(p1cards.get(6).getImage());
        int p1total = addCards(p1cards);

        if (p2cards.get(1) != null) btt_p2_c1.setBackgroundResource(p2cards.get(1).getImage());
        if (p2cards.get(2) != null) btt_p2_c2.setBackgroundResource(p2cards.get(2).getImage());
        if (p2cards.get(3) != null) btt_p2_c3.setBackgroundResource(p2cards.get(3).getImage());
        if (p2cards.get(4) != null) btt_p2_c4.setBackgroundResource(p2cards.get(4).getImage());
        if (p2cards.get(5) != null) btt_p2_c5.setBackgroundResource(p2cards.get(5).getImage());
        if (p2cards.get(6) != null) btt_p2_c6.setBackgroundResource(p2cards.get(6).getImage());
        int p2total = addCards(p2cards);

        if (p1total > p2total){
            TimerText1.setText(R.string.lost);
            TimerText2.setText(R.string.won);
        }
        else if (p2total > p1total){
            TimerText1.setText(R.string.won);
            TimerText2.setText(R.string.lost);
        }
        else {
            TimerText1.setText(R.string.tie);
            TimerText2.setText(R.string.tie);
        }
    }
    private void turnChange(int nextPlayer) {
        if (Cameo) {
            endGame();
            return;
        }

        if (nextPlayer == 1) {
            btt_discard2.setBackgroundResource(R.drawable.emptycard);
            curState = state.DRAW1;
            UpdateText();
        }
        else if (nextPlayer == 2){
            btt_discard1.setBackgroundResource(R.drawable.emptycard);
            curState = state.DRAW2;
            UpdateText();
        }
    }
    private void UpdateText() {
        switch (curState)
        {
            case DRAW1:
                if (!Tapped) TimerText1.setText(R.string.draw_pull);
                else TimerText1.setText(R.string.draw);
                TimerText2.setText(R.string.wait);
                break;
            case DRAW2:
                TimerText1.setText(R.string.wait);
                if (!Tapped) TimerText2.setText(R.string.draw_pull);
                else TimerText2.setText(R.string.draw);
                break;
            case CHOOSE1:
                TimerText1.setText(R.string.choose);
                TimerText2.setText(R.string.wait);
                break;
            case SEETOP1:
                TimerText1.setText(R.string.see_top);
                if (!Tapped) TimerText2.setText(R.string.tap);
                else TimerText2.setText(R.string.wait);
                break;
            case SEEBOTTOM1:
                TimerText1.setText(R.string.see_bottom);
                if (!Tapped) TimerText2.setText(R.string.tap);
                else TimerText2.setText(R.string.wait);
                break;
            case GETNEW1:
                TimerText1.setText(R.string.switch_card);
                if (!Tapped) TimerText2.setText(R.string.tap);
                else TimerText2.setText(R.string.wait);
                break;
            case CAMEO1:
                TimerText1.setText(R.string.wait);
                TimerText2.setText(R.string.cameo);
                break;
            case CHOOSE2:
                TimerText1.setText(R.string.wait);
                TimerText2.setText(R.string.choose);
                break;
            case SEETOP2:
                if (!Tapped) TimerText1.setText(R.string.tap);
                else TimerText1.setText(R.string.wait);
                TimerText2.setText(R.string.see_top);
                break;
            case SEEBOTTOM2:
                if (!Tapped) TimerText1.setText(R.string.tap);
                else TimerText1.setText(R.string.wait);
                TimerText2.setText(R.string.see_bottom);
                break;
            case GETNEW2:
                if (!Tapped) TimerText1.setText(R.string.tap);
                else TimerText1.setText(R.string.wait);
                TimerText2.setText(R.string.switch_card);
                break;
            case CAMEO2:
                TimerText1.setText(R.string.cameo);
                TimerText2.setText(R.string.wait);
                break;
            case LOOKING1:
                TimerText1.setText(R.string.looking);
                if (!Tapped) TimerText2.setText(R.string.tap);
                else TimerText2.setText(R.string.wait);
                break;
            case LOOKING2:
                if (!Tapped) TimerText1.setText(R.string.tap);
                else TimerText1.setText(R.string.wait);
                TimerText2.setText(R.string.looking);
                break;
            default:
                break;
        }
    }
    private void giveCard (int player){
        if (player == 1 && !haveEveryCard(1)){
            for (int i = 1; i <= NUM_MAX_CARD; ++i){
                if (p1cards.get(i) == null) {
                    p1cards.set(i, deck.drawCard());
                    button(1, i).setBackgroundResource(R.drawable.card);
                    return;
                }
            }
            return;
        }
        if (player == 2 && !haveEveryCard(2)){
            for (int i = 1; i <= NUM_MAX_CARD; ++i){
                if (p2cards.get(i) == null) {
                    p2cards.set(i, deck.drawCard());
                    button(2, i).setBackgroundResource(R.drawable.card);
                    return;
                }
            }
        }
    }

    //HELPER FUNCTIONS
    private int addCards(List<Card> list) {
        int total = 0;
        for (int i = 1; i <= NUM_MAX_CARD; ++i)
        {
            if (list.get(i) != null)
            {
                total += list.get(i).getNum();
                if (list.get(i).getNum() == 13) total -= 14;
            }
        }
        return total;
    }
    private boolean haveEveryCard(int Player){
        List<Card> tempList = new ArrayList<>();
        if (Player == 1) tempList = p1cards;
        else if (Player == 2) tempList = p2cards;

        for (int i = 1; i <= NUM_MAX_CARD; ++i){
            if (tempList.get(i) == null) return false;
        }
        return true;
    }
    private boolean tapAble (int tapPlayer){
        if (Tapped) return false;
        if (tapPlayer == 1){
            if (curState == state.DRAW1 || curState == state.CAMEO2 || curState == state.GETNEW2 || curState == state.SEEBOTTOM2 ||
                    curState == state.SEETOP2 || curState == state.LOOKING2){
                return true;
            }
            return false;
        }
        else if (tapPlayer == 2){
            if (curState == state.DRAW2 || curState == state.CAMEO1 || curState == state.GETNEW1 || curState == state.SEEBOTTOM1 ||
                    curState == state.SEETOP1 || curState == state.LOOKING1){
                return true;
            }
            return false;
        }
        else{
            return false;
        }
    }
    private Button button(int player, int loc) {
        if (player == 1) {
            switch (loc) {
                case 1:
                    return btt_p1_c1;
                case 2:
                    return btt_p1_c2;
                case 3:
                    return btt_p1_c3;
                case 4:
                    return btt_p1_c4;
                case 5:
                    return btt_p1_c5;
                case 6:
                    return btt_p1_c6;
            }
        }
        if (player == 2) {
            switch (loc) {
                case 1:
                    return btt_p2_c1;
                case 2:
                    return btt_p2_c2;
                case 3:
                    return btt_p2_c3;
                case 4:
                    return btt_p2_c4;
                case 5:
                    return btt_p2_c5;
                case 6:
                    return btt_p2_c6;
            }
        }
        return null;
    }
    private Button deck (int player){
        if (player == 1) return btt_deck1;
        else return btt_deck2;
    }
    private Card getcard(int player, int index){
        if (player == 1) return p1cards.get(index);
        else return p2cards.get(index);
    }
    private void setcard(int player, int index, Card card){
        if (player == 1) p1cards.set(index, card);
        else p2cards.set(index, card);
    }
    private TextView text(int player){
        if (player == 1) return TimerText1;
        else return TimerText2;
    }
    private boolean CHOOSE(int player){
        if (player == 1 && curState == state.CHOOSE1) return true;
        if (player == 2 && curState == state.CHOOSE2) return true;
        return false;
    }
    private boolean DRAW(int player){
        if (player == 1 && curState == state.DRAW1) return true;
        if (player == 2 && curState == state.DRAW2) return true;
        return false;
    }
    private boolean SEETOP(int player){
        if (player == 1 && curState == state.SEETOP1) return true;
        if (player == 2 && curState == state.SEETOP2) return true;
        return false;
    }
    private boolean SEEBOTTOM(int player){
        if (player == 1 && curState == state.SEEBOTTOM1) return true;
        if (player == 2 && curState == state.SEEBOTTOM2) return true;
        return false;
    }
    private boolean GETNEW(int player){
        if (player == 1 && curState == state.GETNEW1) return true;
        if (player == 2 && curState == state.GETNEW2) return true;
        return false;
    }
    private boolean CAMEO(int player){
        if (player == 1 && curState == state.CAMEO1) return true;
        else if (player == 0 && curState == state.CAMEO2) return true;
        return false;
    }
    private boolean LOOKING(int player){
        if (player == 1 && curState == state.LOOKING1) return true;
        if (player == 2 && curState == state.LOOKING2) return true;
        return false;
    }
}
