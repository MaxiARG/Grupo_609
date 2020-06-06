package com.example.arkanoid_soa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servicios.Body_registrarse;

public class Registrarse_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse_);
        TextView info = (TextView) findViewById(R.id.info);
        info.setText("");
    }

    //Hace el webService y espera una respuesta
    public void solicitarRegistro(View view){
       // Toast.makeText(this,"Falta implementar",Toast.LENGTH_LONG).show();
        boolean todoOK = true;
        Body_registrarse br = new Body_registrarse();
        br.setEnv("DEV");
        EditText inputNombre =(EditText)findViewById(R.id.inputNombre);
        EditText  inputApellido=(EditText)findViewById(R.id.inputApellido);
        EditText  inputEmail=(EditText)findViewById(R.id.inputEmail);
        EditText inputPassword=(EditText)findViewById(R.id.inputPassword);
        EditText  inputComision=(EditText)findViewById(R.id.inpuComision);
        EditText inputDNI=(EditText)findViewById(R.id.inputDNI);
        EditText  inputGroup=(EditText)findViewById(R.id.inputGroup);
        TextView info = (TextView) findViewById(R.id.info);

        String nombre=inputNombre.getText().toString().trim();
        String apellido = inputApellido.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String pass = inputPassword.getText().toString().trim();
        String dni = inputDNI.getText().toString().trim();
        String grupo = inputGroup.getText().toString().trim();
        String comision = inputComision.getText().toString().trim();

        todoOK = NotEmpty(nombre) && NotEmpty(apellido)&& NotEmpty(email) && NotEmpty(pass)
                && NotEmpty(dni)&& NotEmpty(grupo) &&NotEmpty(comision) ;

        todoOK = todoOK && pass.length()>=8 && nombre.length()>=6 && apellido.length()>=6 && dni.length()>=7;
        todoOK = todoOK && grupo.length()==3 && comision.length()>=4;


        if(todoOK){
            //enviar peticion de registro
            System.out.println("DATOS CORRECTOS PARA ENVIAR");
            info.setText("");
            info.setText("Datos Validados Correctamente");
            todoOK = false;
        }else{
            System.out.println("DATOS NOOOOOO CORRECTOS PARA ENVIAR");
            info.setText("");
            info.setText("Los Datos ingresados son Invalidos.");
            todoOK = true;
            inputNombre.setText("");
            inputApellido.setText("");
            inputEmail.setText("");
            inputPassword.setText("");
            inputComision.setText("");
            inputDNI.setText("");
            inputGroup.setText("");

        }
    }

    private boolean NotEmpty(String s){
        if(s!=null || s.trim().length()>0)
            return true;
        return false;
    }
}
