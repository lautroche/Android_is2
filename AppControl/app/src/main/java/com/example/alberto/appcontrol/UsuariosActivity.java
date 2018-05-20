package com.example.alberto.appcontrol;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static config.ApiConfig.COOKIE;
import static config.ApiConfig.GENERIC_ERROR;
import static config.ApiConfig.USUARIOS;


public class UsuariosActivity extends Activity {

private ProgressDialog progress;

	private ListView lstClientes;
	private CoordinatorLayout coordinatorLayout;
	private String[] clientes;
	private JSONArray respJSON;
	final CharSequence  opciones[] = new CharSequence[] {"eliminar", "editar"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_usuarios);
		coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);


        lstClientes = (ListView)findViewById(R.id.lstClientes);
		//se crea el loading para la activdad
		progress = new ProgressDialog(this);
		progress.setCanceledOnTouchOutside(false);
		progress.setCancelable(false);

//se lis por defecto

				TareaWSListar tarea = new TareaWSListar();
				tarea.execute();
// Inflate header view
		ViewGroup headerView = (ViewGroup)getLayoutInflater().inflate(R.layout.header, lstClientes,false);
		// Add header view to the ListView
		lstClientes.addHeaderView(headerView);

		lstClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
									final long id) {



				AlertDialog.Builder builder = new AlertDialog.Builder(UsuariosActivity.this);
				builder.setTitle("Opciones");
				builder.setItems(opciones, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						switch(which) {
							case 0:
								TareaWSEliminar tarea = new TareaWSEliminar();
								tarea.execute(id+"");

								Toast.makeText(getApplicationContext(), id+"",
										Toast.LENGTH_SHORT).show();
								break;
							case 1:
								Intent intent = new Intent(UsuariosActivity.this, UsuarioForm.class);
								JSONObject obj=null;
								try {
									 obj = respJSON.getJSONObject(Integer.parseInt(id+""));
								}catch (Exception ex){}

								intent.putExtra("opcion", "editar");
								intent.putExtra("data", obj.toString());
								Log.e("objeto Pasar", obj.toString());
								Log.e("idObjeto", id+"");

								startActivityForResult(intent,1000);

								break;
							default:
								break;
						}
						Log.e("ServicioRest", which+"");
					}
				});
				builder.show();







			}
		});
		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(UsuariosActivity.this, UsuarioForm.class);
				intent.putExtra("opcion", "agregar");

				startActivityForResult(intent,1000);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	//Tarea As�ncrona para llamar al WS de listado en segundo plano
	private class TareaWSListar extends AsyncTask<String,Integer,Boolean> {


		 
	    protected Boolean doInBackground(String... params) {
			showActivityIndicator();
	    	boolean resul = true;

			DefaultHttpClient httpClient = new DefaultHttpClient();
			
			HttpGet del =
					new HttpGet(USUARIOS);

			del.setHeader("content-type", "application/json");
			httpClient.setCookieStore(COOKIE);
			try
	        {
	        	HttpResponse resp = httpClient.execute(del);
				String respStr = EntityUtils.toString(resp.getEntity());

				respJSON = new JSONArray(respStr);

				clientes = new String[respJSON.length()];
	        			
	        	for(int i=0; i<respJSON.length(); i++)
	        	{
	        		JSONObject obj = respJSON.getJSONObject(i);

					Log.e("ServicioRest", obj.toString());



					String cod = obj.getString("codigoUser");
		        	String correo = obj.getString("correoUser");
					int identi = obj.getInt("idUsuario");
					String empresa = obj.getString("nombreEmpresa");
					String telefono = obj.getString("telefonoUser");

	        		clientes[i] = "" + cod + "__" + correo + "__" + identi+ "__" + empresa+ "__" + telefono;
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
	 
	        return resul;
	    }
	    
	    protected void onPostExecute(Boolean result) {
			hideActivityIndicator();
	    	if (result)
	    	{
		    	//Rellenamos la lista con los nombres de los clientes
	    		//Rellenamos la lista con los resultados


				LstViewAdapter adapter=new LstViewAdapter(UsuariosActivity.this,R.layout.rowlayout,R.id.identi,clientes);
				// Bind data to the ListView
				lstClientes.setAdapter(adapter);

	        	/*ArrayAdapter<String> adaptador =
	        		    new ArrayAdapter<String>(UsuariosActivity.this,
	        		        android.R.layout.simple_list_item_1, clientes);
	        		 
	        	lstClientes.setAdapter(adaptador);*/
	    	}else{
				Snackbar snackbar = Snackbar
						.make(coordinatorLayout, GENERIC_ERROR, Snackbar.LENGTH_LONG);

				snackbar.show();
			}
	    }
	}

	//Tarea As�ncrona para llamar al WS de eliminaci�n en segundo plano
	private class TareaWSEliminar extends AsyncTask<String,Integer,Boolean> {

		protected Boolean doInBackground(String... params) {

			showActivityIndicator();
	    	boolean resul = true;
			try
			{
	    	HttpClient httpClient = new DefaultHttpClient();

			String id = params[0];


			JSONObject obj = respJSON.getJSONObject(Integer.parseInt(id));


				Log.e("doInBackground",obj.getInt("idUsuario")+"");
			HttpDelete del =
					new HttpDelete(USUARIOS +"/"+ obj.getInt("idUsuario"));

			del.setHeader("content__type", "application/json");


	        	HttpResponse resp = httpClient.execute(del);

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
				Snackbar snackbar = Snackbar
						.make(coordinatorLayout, "Usuarios eliminado", Snackbar.LENGTH_LONG);

				snackbar.show();

				TareaWSListar tarea = new TareaWSListar();
				tarea.execute();
			}else{
				Snackbar snackbar = Snackbar
						.make(coordinatorLayout, GENERIC_ERROR, Snackbar.LENGTH_LONG);

				snackbar.show();
			}
	    }
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		TareaWSListar tarea = new TareaWSListar();
		tarea.execute();
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
