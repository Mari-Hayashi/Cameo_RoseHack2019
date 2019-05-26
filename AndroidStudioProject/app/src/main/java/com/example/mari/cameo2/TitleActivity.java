package com.example.mari.cameo2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class TitleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

    }

    public void exit(View view ){
        finish();
    }

    public void start(View view){
            Intent intent = new Intent(this,GameActivity.class);
            startActivity(intent);
    }
}
