package com.example.arkanoid_soa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Perdiste_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perdiste_);
    }

    public void A_MenuPrincipal(View view){
        Intent intent = new Intent(this, MainMenu_Activity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
