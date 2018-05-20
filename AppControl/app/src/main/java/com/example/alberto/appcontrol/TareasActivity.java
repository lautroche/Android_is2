package com.example.alberto.appcontrol;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static config.ApiConfig.CODEUSER;
import static config.ApiConfig.COOKIE;
import static config.ApiConfig.GENERIC_ERROR;
import static config.ApiConfig.TAREAS;


public class TareasActivity extends Activity {

private ProgressDialog progress;

	private TextView lblResultado;
	private ListView lstTareas;
	private CoordinatorLayout coordinatorLayout;
	private String[] tareas;
	private JSONArray respJSON;
	final CharSequence  opciones[] = new CharSequence[] {"eliminar", "editar"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tareas);
		coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
		FloatingActionButton fab = findViewById(R.id.fab);

		lblResultado = (TextView)findViewById(R.id.lblResultado);
        lstTareas = (ListView)findViewById(R.id.lstTareas);
		//se crea el loading para la activdad
		progress = new ProgressDialog(this);
		progress.setCanceledOnTouchOutside(false);
		progress.setCancelable(false);

//se lis por defecto
		Boolean ok = getIntent().getBooleanExtra("byCode",false);
		if(ok ){
			if(CODEUSER==null){

				Intent intent = new Intent(TareasActivity.this, LoginActivity.class);
				finish();  //Kill the activity from which you will go to next activity
				startActivity(intent);
			}else{
				fab.setImageResource(android.R.drawable.ic_popup_reminder);
			}
		}

		TareaWSListar tarea = new TareaWSListar();
		tarea.execute();

// Inflate header view
		ViewGroup headerView = (ViewGroup)getLayoutInflater().inflate(R.layout.header_tareas, lstTareas,false);
		// Add header view to the ListView
		lstTareas.addHeaderView(headerView);

		lstTareas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
									final long id) {



				AlertDialog.Builder builder = new AlertDialog.Builder(TareasActivity.this);
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
								Intent intent = new Intent(TareasActivity.this, TareaForm.class);
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

		fab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				String byCode="";
				try {
					Boolean ok = getIntent().getBooleanExtra("byCode",false);
					if(ok){
						AlertDialog.Builder builder = new AlertDialog.Builder(TareasActivity.this);
						builder.setTitle("Recordatorio");
						builder.setMessage("Quiere agregar un recoradotorio para estas actividades??");

						// add the buttons
						builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								try {
									AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

									Intent notificationIntent = new Intent(TareasActivity.this, AlarmReceiver.class);
									PendingIntent broadcast = PendingIntent.getBroadcast(TareasActivity.this, 5, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

									List<String> fechas = new ArrayList<String>();
									for(int i=0; i<respJSON.length(); i++)
									{
										JSONObject obj = respJSON.getJSONObject(i);

										Log.e("ServicioRest", obj.toString());


										int idTarea = obj.getInt("idTarea");


										String idSprint = obj.getJSONObject("idSprint").getString("fechaFin");
										boolean retval = fechas.contains(idSprint);

										if (retval != true) {
											fechas.add(idSprint);
										}


									}

									for(int i=0; i<fechas.size(); i++) {
										Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(fechas.get(i));
										Calendar cal = Calendar.getInstance();
										cal.setTime(date);
										cal.add(Calendar.DATE, -1);
										cal.set(Calendar.HOUR_OF_DAY, 8);
									//	cal.set(Calendar.MINUTE,23);

                                        Log.e("ServicioRest",cal.toString());
										alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
									}
								}catch (Exception e){
                                    Log.e("ServicioRest", e.toString());
								}
							}
						});

						builder.setNegativeButton("Cancel", null);

						// create and show the alert dialog
						AlertDialog dialog = builder.create();
						dialog.show();
					}else{
						Intent intent = new Intent(TareasActivity.this, TareaForm.class);
						intent.putExtra("opcion", "agregar");

						startActivityForResult(intent,1000);
					}
				}catch (Exception e){
					Log.e("ServicioRest", e.toString());
				}

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
			httpClient.setCookieStore(COOKIE);
			String toSend=TAREAS;
			String byCode="";
	    	try {
				Boolean ok = getIntent().getBooleanExtra("byCode",false);
				if(ok){
					byCode="/byCode/"+CODEUSER;
				}
			}catch (Exception e){

			}
			HttpGet del =
					new HttpGet(toSend+byCode);

			del.setHeader("content-type", "application/json");



			try
	        {
	        	HttpResponse resp = httpClient.execute(del);
				String respStr = EntityUtils.toString(resp.getEntity());

				respJSON = new JSONArray(respStr);

				tareas = new String[respJSON.length()];
	        			
	        	for(int i=0; i<respJSON.length(); i++)
	        	{
	        		JSONObject obj = respJSON.getJSONObject(i);

					Log.e("ServicioRest", obj.toString());


					int idTarea = obj.getInt("idTarea");

		        	String descripcion = obj.getString("descripcion");
					String tiempoEstimado = obj.getString("tiempoEstimado");
					String idProyecto = obj.getJSONObject("idProyecto").getString("codigoProyecto");
					String idSprint = obj.getJSONObject("idSprint").getString("idSprint");
					String idUsuario = obj.getJSONObject("idUsuario").getString("codigoUser");

					tareas[i] = "" + idTarea + "__" + descripcion + "__" + tiempoEstimado+ "__" + idProyecto+ "__" + idSprint+ "__" + idUsuario;
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
		    	//Rellenamos la lista con los nombres de los tareas
	    		//Rellenamos la lista con los resultados


				LstViewTareasAdapter adapter=new LstViewTareasAdapter(TareasActivity.this,R.layout.rowlayout_tareas,R.id.id,tareas);
				// Bind data to the ListView
				lstTareas.setAdapter(adapter);

	        	/*ArrayAdapter<String> adaptador =
	        		    new ArrayAdapter<String>(TareasActivity.this,
	        		        android.R.layout.simple_list_item_1, tareas);
	        		 
	        	lstTareas.setAdapter(adaptador);*/
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
				DefaultHttpClient httpClient = new DefaultHttpClient();
				httpClient.setCookieStore(COOKIE);
			String id = params[0];


			JSONObject obj = respJSON.getJSONObject(Integer.parseInt(id));


				Log.e("doInBackground",obj.getInt("idTarea")+"");
			HttpDelete del =
					new HttpDelete(TAREAS +"/"+ obj.getInt("idTarea"));

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
						.make(coordinatorLayout, "Tareas eliminado", Snackbar.LENGTH_LONG);

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
