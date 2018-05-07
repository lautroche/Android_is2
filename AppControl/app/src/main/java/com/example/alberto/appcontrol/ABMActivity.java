package com.example.alberto.appcontrol;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
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

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import static config.ApiConfig.USUARIOS;


/**
 * A login screen that offers login via email/password.
 */
public class ABMActivity extends AppCompatActivity {
    ListView listView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.listHome);

        // Defined Array values to show in ListView
        String[] values = new String[] {
                "Usuarios",
                "Tareas"
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
                int itemPosition     = position;

                // ListView Clicked item value
               // String  itemValue    = (String) listView.getItemAtPosition(position);

                switch (position) {
                    case 0:
                        Intent i = new Intent(ABMActivity.this, UsuariosActivity.class);
                        startActivity(i);
                        break;
                    case 1:
                        Intent i2 = new Intent(ABMActivity.this, TareasActivity.class);
                        startActivity(i2);
                        break;
                    default:
                        break;
                }

            }

        });
    }

}
