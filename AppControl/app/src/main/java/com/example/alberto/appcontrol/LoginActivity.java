package com.example.alberto.appcontrol;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import BaseDatos.DbHelperControl;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener {

    EditText etUsuario;
    EditText etContrasenha;
    Button btAceptar;
    Cursor query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DbHelperControl db = new DbHelperControl(this);
        db.openReadableDB();    //Se abre la BD

        etUsuario = (EditText) findViewById(R.id.et_usuario);
        etContrasenha = (EditText) findViewById(R.id.et_contrasenha);

        btAceptar = (Button) findViewById(R.id.bt_aceptar);
        btAceptar.setOnClickListener(this);

        db.closeDB(); //Cerramos la BD
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_aceptar:
                DbHelperControl helper = new DbHelperControl(this);
                SQLiteDatabase dataBase = helper.getWritableDatabase();


                String user = etUsuario.getText().toString();
                String password = etContrasenha.getText().toString();

                if (user.isEmpty() && password.isEmpty()) {
                    //Si usuario y contraseña vacíos muestra mensaje
                    Toast.makeText(getApplicationContext(), "Campos Usuario y Contraseña vacíos", Toast.LENGTH_SHORT).show();
                } else {
                    //Consulta si existe usuario y contraseña en la base de datos
                    query = dataBase.rawQuery("SELECT codigo_user, password_user FROM usuarios WHERE " +
                            "codigo_user = '" + user + "' AND password_user = '" + password + "'", null);

                    //preguntamos si la variable query no está vacía
                    if (query.moveToFirst()) {
                        String u = query.getString(0); //extraemos el usuario en la pos 0
                        String p = query.getString(1); //extraemos la contraseña en la pos 1

                        if (user.equals(u) && password.equals(p)) {
                            Intent intent = new Intent(LoginActivity.this, Clientes.class);
                            startActivity(intent);

                            clearTextBox(); //limpiamos las las cajas de texto

                            String usuario = etUsuario.getText().toString();

                            //Comentario que aparece al iniciar la app
                            Toast.makeText(getApplicationContext(), "Bienvenido " + usuario, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Usuario o Contraseña inválidos", Toast.LENGTH_SHORT).show();
                        clearTextBox(); //limpiamos las las cajas de texto

                    }
                }
                break;
        }
    }

    public void clearTextBox(){
            etUsuario.setText("");
            etContrasenha.setText("");
        }
}
