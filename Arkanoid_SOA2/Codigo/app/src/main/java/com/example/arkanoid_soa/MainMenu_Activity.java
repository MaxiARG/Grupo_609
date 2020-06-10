package com.example.arkanoid_soa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.Business.GameGlobalData;

public class MainMenu_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_);

    }
    public void StartGameButton(View view){
        GameGlobalData.guardarEvento(getBaseContext(), GameGlobalData.fechaHora(), "Inicia Juego\n");
        GameGlobalData.enviarEvento(getBaseContext(), "Inicia-Juego", "El juego ha comenzado");
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
