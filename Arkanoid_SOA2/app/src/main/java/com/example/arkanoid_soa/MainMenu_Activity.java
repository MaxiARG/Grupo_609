package com.example.arkanoid_soa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainMenu_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_);
    }



    public void StartGameButton(View view){
        Intent intent = new Intent(this, GameLoop.class);
        startActivity(intent);
    }

    public void salir(View view){
        Toast.makeText(this,"Falta implementar",Toast.LENGTH_LONG).show();
    }

    public void verLogs(View view){
        Toast.makeText(this,"Falta implementar",Toast.LENGTH_LONG).show();
    }


    @Override
    public void onBackPressed() {
        return;
    }
}
