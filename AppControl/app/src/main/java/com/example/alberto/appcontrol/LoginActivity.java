package com.example.alberto.appcontrol;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static config.ApiConfig.GENERIC_ERROR;
import static config.ApiConfig.HEADER;
import static config.ApiConfig.LOGIN;
import static config.ApiConfig.COOKIE;
import static config.ApiConfig.CODEUSER;


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
    String user;
    String password;

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
                     user = etUsuario.getText().toString();
                     password = etContrasenha.getText().toString();

                     if(user.isEmpty()||password.isEmpty()) {

                         Snackbar snackbar = Snackbar
                                 .make(coordinatorLayout, "Por favor introduzca todos los datos", Snackbar.LENGTH_LONG);

                         snackbar.show();
                     }else{

                         TareaLogin tarea = new TareaLogin();
                         tarea.execute();




                         CODEUSER = user;

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

            DefaultHttpClient httpClient = new DefaultHttpClient();





            HttpPost post = new HttpPost(LOGIN);
            post.setHeader("content-type", "application/x-www-form-urlencoded");

            try
            {
                 COOKIE = httpClient.getCookieStore();
                /* This is how to declare HashMap */
                HashMap<String, String> hmap = new HashMap<String, String>();

                /*Adding elements to HashMap*/
                hmap.put("txtNombre", user);
                hmap.put("txtPass", password);

                StringEntity entity = new StringEntity( getDataString(hmap));
                post.setEntity(entity);
                HttpResponse resp = httpClient.execute(post);
                String respStr = EntityUtils.toString(resp.getEntity());
                HEADER = resp.getAllHeaders();
                Log.e("resp",respStr);




                JSONObject respuesta = new JSONObject(respStr);





                    String status = respuesta.getString("status");
                   if(!status.equals("ok")){
                       resul =false;
                   }


            }
            catch(Exception ex)
            {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, GENERIC_ERROR, Snackbar.LENGTH_LONG);

                snackbar.show();

                Log.e("ServicioRest","Error!", ex);
                resul = false;
            }
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpClient.getConnectionManager().shutdown();
            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                Intent i = new Intent(LoginActivity.this, ABMActivity.class);
                finish();
                startActivity(i);

            }else{
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, GENERIC_ERROR, Snackbar.LENGTH_LONG);

                snackbar.show();
            }

        }
    }
    private String getDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }
}
