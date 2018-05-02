package com.example.alberto.appcontrol;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import static config.ApiConfig.USUARIOS;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener {

    AutoCompleteTextView etUsuario;
    EditText etContrasenha;
    Button btAceptar;
    Cursor query;
    private CoordinatorLayout coordinatorLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout3);

        progressBar = (ProgressBar) findViewById(R.id.login_progress);

        etUsuario = (AutoCompleteTextView) findViewById(R.id.etUsuario);
        etContrasenha = (EditText) findViewById(R.id.etContrasenha);

        btAceptar = (Button) findViewById(R.id.bt_aceptar);
        btAceptar.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_aceptar:
                btAceptar.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                try {
                     String user = etUsuario.getText().toString();
                     String password = etContrasenha.getText().toString();

                     if(user.isEmpty()||password.isEmpty()) {

                         Snackbar snackbar = Snackbar
                                 .make(coordinatorLayout, "Por favor introduzca todos los datos", Snackbar.LENGTH_LONG);

                         snackbar.show();
                     }else{
                         Intent i = new Intent(LoginActivity.this, UsuariosActivity.class);
                         startActivity(i);
                     }

                    btAceptar.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }catch (Exception ex){
                    Log.e("ServicioRest","Error!", ex);

                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Por favor introduzca todos los datos", Snackbar.LENGTH_LONG);

                    snackbar.show();
                    btAceptar.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }


        }
    }

    public void clearTextBox(){
            etUsuario.setText("");
            etContrasenha.setText("");
        }

    private class TareaLogin extends AsyncTask<String,Integer,Boolean> {



        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del =
                    new HttpGet(USUARIOS);

            del.setHeader("content-type", "application/json");

            try
            {

            }
            catch(Exception ex)
            {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Welcome to AndroidHive", Snackbar.LENGTH_LONG);

                snackbar.show();

                Log.e("ServicioRest","Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {

            }else{
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Welcome to AndroidHive", Snackbar.LENGTH_LONG);

                snackbar.show();
            }
        }
    }
}
