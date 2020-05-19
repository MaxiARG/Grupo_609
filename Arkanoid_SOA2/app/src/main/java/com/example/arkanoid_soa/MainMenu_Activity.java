package com.example.arkanoid_soa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainMenu_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_);
    }



    public void StartGameButton(View view){
        Intent intent = new Intent(this, GameLoop.class);
        System.out.println("StartGameButton Llamado");
        startActivity(intent);
    }
}
