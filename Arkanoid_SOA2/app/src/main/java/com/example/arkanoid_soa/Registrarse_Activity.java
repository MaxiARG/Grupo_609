package com.example.arkanoid_soa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Registrarse_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse_);
    }

    //Hace el webService y espera una respuesta
    public void solicitarRegistro(View view){
        Toast.makeText(this,"Falta implementar",Toast.LENGTH_LONG).show();
    }
}
