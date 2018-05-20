package com.example.alberto.appcontrol;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import static config.ApiConfig.COOKIE;
import static config.ApiConfig.GENERIC_ERROR;
import static config.ApiConfig.PROYECTOS;
import static config.ApiConfig.SPRINT;
import static config.ApiConfig.TAREAS;
import static config.ApiConfig.USUARIOS;

public class TareaForm extends AppCompatActivity {
    String opcion;
    private CoordinatorLayout coordinatorLayout;
    private String[] clientes;
    private String[] proyectos;
    private String[] sprints;

    private JSONArray respJSONUsuarios;
    private JSONArray respJSONProyectos;
    private JSONArray respJSONSprints;
    private String data;
    int identificadorTarea;


    EditText descripcionT;
    EditText tiempoT;
    JSONObject dataImported;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.tarea_form);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout2);

        descripcionT = (EditText) findViewById(R.id.descripcion_tarea);
        tiempoT = (EditText) findViewById(R.id.tiempo_tarea);

        progress = new ProgressDialog(this);
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);
        try {


            opcion = getIntent().getStringExtra("opcion");
            Log.e("ServicioRest", opcion);
            if (opcion.equals("editar")) {
                data = getIntent().getStringExtra("data");
                Log.e("ServicioRest", data);

                dataImported = new JSONObject(data);

                String descripcion = dataImported.getString("descripcion");
                String tiempo = dataImported.getString("tiempoEstimado");
                identificadorTarea = dataImported.getInt("idTarea");


                descripcionT.setText(descripcion);
                tiempoT.setText(tiempo);

            }


        } catch (Exception ex) {

            Log.e("ServicioRest", "Error!", ex);
        }


        Button fab = findViewById(R.id.aceptar_tarea);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (opcion.equals("editar")) {
                    TareaWSActualizar tarea = new TareaWSActualizar();
                    tarea.execute();
                } else {
                    TareaWSInsertar tarea = new TareaWSInsertar();
                    tarea.execute();
                }
            }
        });

        TareaWSListarUsuarios tarea = new TareaWSListarUsuarios();
        tarea.execute();
        TareaWSListarProyectos tarea2 = new TareaWSListarProyectos();
        tarea2.execute();

        TareaWSListarSprints tarea3 = new TareaWSListarSprints();
        tarea3.execute();
    }

    //Tarea As�ncrona para llamar al WS de inserci�n en segundo plano
    private class TareaWSInsertar extends AsyncTask<String, Integer, Boolean> {

        protected Boolean doInBackground(String... params) {
            showActivityIndicator();
            boolean resul = true;

            DefaultHttpClient httpClient = new DefaultHttpClient();
            httpClient.setCookieStore(COOKIE);

            HttpPost post = new HttpPost(TAREAS);
            post.setHeader("content-type", "application/json");


            Spinner proyectos = (Spinner) findViewById(R.id.proyecto_spinner);
            Long proyectoItem = proyectos.getSelectedItemId();

            Spinner usuarios = (Spinner) findViewById(R.id.usuarios_spiner);
            Long usuarioItem = usuarios.getSelectedItemId();

            Spinner sprint = (Spinner) findViewById(R.id.sprint_spinner);
            Long sprintItem = sprint.getSelectedItemId();

            try {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();

                dato.put("descripcion", descripcionT.getText());
                dato.put("tiempoEstimado", descripcionT.getText());
                dato.put("idProyecto", respJSONProyectos.getJSONObject(proyectoItem.intValue()));
                dato.put("idSprint", respJSONSprints.getJSONObject(sprintItem.intValue()));
                dato.put("idUsuario", respJSONUsuarios.getJSONObject(usuarioItem.intValue()));

                StringEntity entity = new StringEntity(dato.toString());
                Log.e("ServicioRest", entity.toString());


                post.setEntity(entity);
                HttpResponse resp = httpClient.execute(post);
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {
            hideActivityIndicator();
            if (result) {
                finish();
            } else {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, GENERIC_ERROR, Snackbar.LENGTH_LONG);

                snackbar.show();
            }
        }


    }

    //Tarea As�ncrona para llamar al WS de actualizaci�n en segundo plano
    private class TareaWSActualizar extends AsyncTask<String, Integer, Boolean> {

        protected Boolean doInBackground(String... params) {
            showActivityIndicator();
            boolean resul = true;

            DefaultHttpClient httpClient = new DefaultHttpClient();
            httpClient.setCookieStore(COOKIE);
            HttpPut put = new HttpPut(TAREAS + "/" + identificadorTarea);

            Log.e("ServicioRest", TAREAS + "/" + identificadorTarea);
            put.setHeader("content-type", "application/json");
            Spinner proyectos = (Spinner) findViewById(R.id.proyecto_spinner);
            Long proyectoItem = proyectos.getSelectedItemId();

            Spinner usuarios = (Spinner) findViewById(R.id.usuarios_spiner);
            Long usuarioItem = usuarios.getSelectedItemId();

            Spinner sprint = (Spinner) findViewById(R.id.sprint_spinner);
            Long sprintItem = sprint.getSelectedItemId();

            try {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();


                dato.put("descripcion", descripcionT.getText());
                dato.put("tiempoEstimado", descripcionT.getText());
                dato.put("idProyecto", respJSONProyectos.getJSONObject(proyectoItem.intValue()));
                dato.put("idSprint", respJSONSprints.getJSONObject(sprintItem.intValue()));
                dato.put("idUsuario", respJSONUsuarios.getJSONObject(usuarioItem.intValue()));
                dato.put("idTarea", identificadorTarea);

                StringEntity entity = new StringEntity(dato.toString());
                Log.e("enviado", dato.toString());
                put.setEntity(entity);
                HttpResponse resp = httpClient.execute(put);
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {
            hideActivityIndicator();
            if (result) {
                finish();
            } else {
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


    //Tarea As�ncrona para llamar al WS de listado en segundo plano
    private class TareaWSListarUsuarios extends AsyncTask<String, Integer, Boolean> {
        private int savedID = 0;


        protected Boolean doInBackground(String... params) {
            showActivityIndicator();
            boolean resul = true;

            DefaultHttpClient httpClient = new DefaultHttpClient();
            httpClient.setCookieStore(COOKIE);

            HttpGet del =
                    new HttpGet(USUARIOS);

            del.setHeader("content-type", "application/json");

            try {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                respJSONUsuarios = new JSONArray(respStr);

                clientes = new String[respJSONUsuarios.length()];

                for (int i = 0; i < respJSONUsuarios.length(); i++) {
                    JSONObject obj = respJSONUsuarios.getJSONObject(i);

                    Log.e("ServicioRest", obj.toString());


                    String cod = obj.getString("codigoUser");
                    int identi = obj.getInt("idUsuario");

                    clientes[i] = identi + " " + cod;

                    try {
                        if (obj.toString().equals(dataImported.getString("idUsuario"))) {
                            savedID = i;
                        }
                    } catch (Exception ex) {
                    }

                }
            } catch (Exception ex) {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, GENERIC_ERROR, Snackbar.LENGTH_LONG);

                snackbar.show();

                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {
            hideActivityIndicator();
            if (result) {

                Spinner spinner = (Spinner) findViewById(R.id.usuarios_spiner);

                spinner.setAdapter(new ArrayAdapter<String>(TareaForm.this, android.R.layout.simple_spinner_item, clientes));

                spinner.setSelection(savedID);

            } else {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, GENERIC_ERROR, Snackbar.LENGTH_LONG);

                snackbar.show();
            }
        }
    }

    //Tarea As�ncrona para llamar al WS de listado en segundo plano
    private class TareaWSListarProyectos extends AsyncTask<String, Integer, Boolean> {
        private int savedID = 0;


        protected Boolean doInBackground(String... params) {
            showActivityIndicator();
            boolean resul = true;

            DefaultHttpClient httpClient = new DefaultHttpClient();
            httpClient.setCookieStore(COOKIE);

            HttpGet del =
                    new HttpGet(PROYECTOS);

            del.setHeader("content-type", "application/json");

            try {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                respJSONProyectos = new JSONArray(respStr);

                proyectos = new String[respJSONProyectos.length()];

                for (int i = 0; i < respJSONProyectos.length(); i++) {
                    JSONObject obj = respJSONProyectos.getJSONObject(i);

                    Log.e("ServicioRest", obj.toString());


                    String cod = obj.getString("codigoProyecto");
                    String id = obj.getString("idProyecto");

                    proyectos[i] = id + " " + cod;

                    try {
                        if (obj.toString().equals(dataImported.getString("idProyecto"))) {
                            savedID = i;
                        }
                    } catch (Exception ex) {
                    }

                }
            } catch (Exception ex) {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, GENERIC_ERROR, Snackbar.LENGTH_LONG);

                snackbar.show();

                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {
            hideActivityIndicator();
            if (result) {

                Spinner spinner = (Spinner) findViewById(R.id.proyecto_spinner);

                spinner.setAdapter(new ArrayAdapter<String>(TareaForm.this, android.R.layout.simple_spinner_item, proyectos));

                spinner.setSelection(savedID);

            } else {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, GENERIC_ERROR, Snackbar.LENGTH_LONG);

                snackbar.show();
            }
        }
    }

    //Tarea As�ncrona para llamar al WS de listado en segundo plano
    private class TareaWSListarSprints extends AsyncTask<String, Integer, Boolean> {
        private int savedID = 0;


        protected Boolean doInBackground(String... params) {
            showActivityIndicator();
            boolean resul = true;

            DefaultHttpClient httpClient = new DefaultHttpClient();
            httpClient.setCookieStore(COOKIE);

            HttpGet del =
                    new HttpGet(SPRINT);

            del.setHeader("content-type", "application/json");

            try {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                respJSONSprints = new JSONArray(respStr);

                sprints = new String[respJSONSprints.length()];

                for (int i = 0; i < respJSONSprints.length(); i++) {
                    JSONObject obj = respJSONSprints.getJSONObject(i);

                    Log.e("ServicioRest", obj.toString());


                    String cod = obj.getString("idSprint");


                    sprints[i] = cod;
                    try {
                        if (obj.toString().equals(dataImported.getString("idProyecto"))) {
                            savedID = i;
                        }
                    } catch (Exception ex) {
                    }


                }
            } catch (Exception ex) {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, GENERIC_ERROR, Snackbar.LENGTH_LONG);

                snackbar.show();

                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {
            hideActivityIndicator();
            if (result) {

                Spinner spinner = (Spinner) findViewById(R.id.sprint_spinner);

                spinner.setAdapter(new ArrayAdapter<String>(TareaForm.this, android.R.layout.simple_spinner_item, sprints));

                spinner.setSelection(savedID);

            } else {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, GENERIC_ERROR, Snackbar.LENGTH_LONG);

                snackbar.show();
            }
        }
    }


}