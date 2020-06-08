package com.example.arkanoid_soa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.Business.GameGlobalData;

import java.util.Map;

public class MainMenu_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_);
        Toast.makeText(this, "Conexion exitosa", Toast.LENGTH_LONG).show();
    }



    public void StartGameButton(View view){
        Intent intent = new Intent(this, GameLoop.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        return;
    }

    public void abrirRegistroDeEventos(View view){
        Intent intent = new Intent(this, RegistroDeEventos.class);
        startActivity(intent);
    }


}
