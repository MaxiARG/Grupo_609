package com.example.arkanoid_soa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.Business.GameGlobalData;

import java.util.Map;
import java.util.TreeMap;

public class RegistroDeEventos extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_de_eventos);

        cargarScrollViewConLogs();
    }

    void cargarScrollViewConLogs (){
        SharedPreferences sp = getSharedPreferences(GameGlobalData.preferenciasLogs, MODE_PRIVATE);
        textView = (TextView)findViewById(R.id.textViewLogs);
        textView.setText("");
        textView.setText("\nEventros Registrados:\n");
        Map<String, ?> allEntries = sp.getAll();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            System.out.println(entry.getKey());
            textView.append(entry.getKey()+": "+entry.getValue().toString());
        }
    }


    public void A_MenuPrincipal(View view){
      GameGlobalData.limpiarLogs(getBaseContext());
      cargarScrollViewConLogs();
    }


}
