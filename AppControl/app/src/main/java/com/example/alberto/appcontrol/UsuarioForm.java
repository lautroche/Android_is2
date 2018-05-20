package com.example.alberto.appcontrol;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import static config.ApiConfig.GENERIC_ERROR;
import static config.ApiConfig.USUARIOS;

public class UsuarioForm extends AppCompatActivity {
    String opcion;
    private CoordinatorLayout coordinatorLayout;

    EditText codT ;
    EditText correoT ;
    EditText identiT ;
    EditText modPassT;
    EditText empresaT;

    EditText passT ;
    EditText telefonoT;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.usuario_form);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout2);

         codT = (EditText) findViewById(R.id.codigoUser);
         correoT = (EditText) findViewById(R.id.correoUser);
         identiT = (EditText) findViewById(R.id.idUsuario);
         modPassT = (EditText) findViewById(R.id.modifcarPassword);
         empresaT = (EditText) findViewById(R.id.nombreEmpresa);

         passT = (EditText) findViewById(R.id.passwordUser);
         telefonoT = (EditText) findViewById(R.id.telefonoUser);
        modPassT.setText("true");
        progress = new ProgressDialog(this);
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);
        try {



            opcion = getIntent().getStringExtra("opcion");
            Log.e("ServicioRest",opcion);
            if(opcion.equals("editar")) {
                String data = getIntent().getStringExtra("data");
                Log.e("ServicioRest",data);

                JSONObject respJSON = new JSONObject(data);

                String cod = respJSON.getString("codigoUser");
                String correo = respJSON.getString("correoUser");
                int identi = respJSON.getInt("idUsuario");
                try {
                    String modPass = respJSON.getString("modifcarPassword");
                    modPassT.setText(modPass);
                }catch (Exception ex){
                    modPassT.setText("");
                }

                String empresa = respJSON.getString("nombreEmpresa");
                String pass = respJSON.getString("passwordUser");

                String telefono = respJSON.getString("telefonoUser");

                codT.setText(cod);
                correoT.setText(correo);
                identiT.setText(identi+"");
                empresaT.setText(empresa);
                passT.setText(pass);
                telefonoT.setText(telefono);

           }

            identiT.setEnabled(false);
        }catch (Exception ex){

            Log.e("ServicioRest","Error!", ex);
        }


        Button fab = findViewById(R.id.aceptar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(opcion.equals("editar")) {
                    TareaWSActualizar tarea = new TareaWSActualizar();
                    tarea.execute();
                }else{
                    TareaWSInsertar tarea = new TareaWSInsertar();
                    tarea.execute();
                }
            }
        });


    }
    //Tarea As�ncrona para llamar al WS de inserci�n en segundo plano
    private class TareaWSInsertar extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {
            showActivityIndicator();
            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new HttpPost(USUARIOS);
            post.setHeader("content-type", "application/json");

            try
            {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();

                dato.put("codigoUser", codT.getText());
                dato.put("correoUser", correoT.getText());
             //   dato.put("idUsuario", Integer.parseInt(identiT.getText().toString()));
                dato.put("modifcarPassword", modPassT.getText());
                dato.put("nombreEmpresa",  empresaT.getText());
                dato.put("passwordUser", passT.getText());
                dato.put("telefonoUser", telefonoT.getText());

                StringEntity entity = new StringEntity(dato.toString());
                Log.e("ServicioRest",entity.toString());

                post.setEntity(entity);
                HttpResponse resp = httpClient.execute(post);
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {
            hideActivityIndicator();
            if (result)
            {
                finish();
            }else{
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, GENERIC_ERROR, Snackbar.LENGTH_LONG);

                snackbar.show();
            }
        }


    }

    //Tarea As�ncrona para llamar al WS de actualizaci�n en segundo plano
    private class TareaWSActualizar extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {
            showActivityIndicator();
            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpPut put = new HttpPut(USUARIOS+"/"+identiT.getText().toString());

            Log.e("ServicioRest",USUARIOS+"/"+identiT.getText().toString());
            put.setHeader("content-type", "application/json");

            try
            {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();

                dato.put("codigoUser", codT.getText());
                dato.put("correoUser", correoT.getText());
                dato.put("idUsuario", Integer.parseInt(identiT.getText().toString()));
                dato.put("modifcarPassword", modPassT.getText());
                dato.put("nombreEmpresa",  empresaT.getText());
                dato.put("passwordUser", passT.getText());
                dato.put("telefonoUser", telefonoT.getText());

                StringEntity entity = new StringEntity(dato.toString());
                Log.e("ServicioRest",entity.toString());

                put.setEntity(entity);
                HttpResponse resp = httpClient.execute(put);
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {
            hideActivityIndicator();
            if (result)
            {
                finish();
            }else{
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, GENERIC_ERROR, Snackbar.LENGTH_LONG);

                snackbar.show();
            }
        }
    }

    //metodo usado para mostrar el loading dentro de la activdad
    public void showActivityIndicator() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progress.setTitle("Cargando");
                progress.setMessage("Por favor, aguarde");
                progress.show();
            }
        });
    }
    //metodo usado para cerrar el loading dentro de la activdad
    public void hideActivityIndicator() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progress.dismiss();
            }
        });

    }



}