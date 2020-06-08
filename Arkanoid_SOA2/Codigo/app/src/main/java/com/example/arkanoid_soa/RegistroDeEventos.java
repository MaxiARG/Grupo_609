package com.example.arkanoid_soa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.Business.GameGlobalData;

import java.util.Map;

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
       // SharedPreferences.Editor editorSP = sp.edit();
        textView = (TextView)findViewById(R.id.textViewLogs);
        //editText.getText();
        textView.setText("");
        textView.setText("\nEventros Registrados:\n");
        Map<String, ?> allEntries = sp.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            textView.append(entry.getKey()+": "+entry.getValue().toString());
        }

      //  editorSP.putString("ASD","asd");
     //   editorSP.apply();//para guardar los cambios

        //String obtenido = sp.getString("ASD", "ValorPorDefectoSiNoEncuentroNada");
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
