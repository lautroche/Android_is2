package com.example.alberto.appcontrol;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

import static config.ApiConfig.CODEUSER;
import static config.ApiConfig.LOGOUT;

import static config.ApiConfig.COOKIE;
import static config.ApiConfig.GENERIC_ERROR;
import static config.ApiConfig.HEADER;
import static config.ApiConfig.LOGIN;
import static config.ApiConfig.USUARIOS;


/**
 * A login screen that offers login via email/password.
 */
public class ABMActivity extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.listHome);

        // Defined Array values to show in ListView
        String[] values = new String[]{
                "Usuarios",
                "Tareas",
                "Tareas del " + CODEUSER
        };

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);


        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;
                Intent i = null;
                // ListView Clicked item value
                // String  itemValue    = (String) listView.getItemAtPosition(position);

                switch (position) {
                    case 0:
                        i = new Intent(ABMActivity.this, UsuariosActivity.class);
                        break;
                    case 1:
                        i = new Intent(ABMActivity.this, TareasActivity.class);

                        break;
                    case 2:
                        i = new Intent(ABMActivity.this, TareasActivity.class);
                        i.putExtra("byCode", true);
                        break;
                    default:
                        break;
                }
                startActivity(i);

            }

        });
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        AlertDialog.Builder builder = new AlertDialog.Builder(ABMActivity.this);
        builder.setTitle("Salir");
        builder.setMessage("Esta seguro que quiere salir de la app??");

        // add the buttons
        builder.setPositiveButton("Salir", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                try {

                    ABMActivity.TareaLogOut tarea = new ABMActivity.TareaLogOut();

                    tarea.execute();
                    CODEUSER=null;

                    finish();
                } catch (Exception e) {

                }
            }
        });

        builder.setNegativeButton("No", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private class TareaLogOut extends AsyncTask<String, Integer, Boolean> {


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            DefaultHttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new HttpPost(LOGOUT);
            post.setHeader("content-type", "application/x-www-form-urlencoded");

            try {
                COOKIE = httpClient.getCookieStore();
                /* This is how to declare HashMap */
                HashMap<String, String> hmap = new HashMap<String, String>();


            } catch (Exception ex) {


                Log.e("ServicioRest", "Error!", ex);

            }
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpClient.getConnectionManager().shutdown();
            return resul;
        }

        protected void onPostExecute(Boolean result) {
        }
    }

}
