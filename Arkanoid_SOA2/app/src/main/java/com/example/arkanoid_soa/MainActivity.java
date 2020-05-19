package com.example.arkanoid_soa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void registrarse(View view){
        System.out.println("Presionaste Registrarse");
        Intent intent = new Intent(this, Registrarse_Activity.class);
        startActivity(intent);
    }

    public void ingresar(View view){
        Intent intent = new Intent(this, MainMenu_Activity.class);
        System.out.println("Presionaste Ingresar");
        startActivity(intent);
    }

}
