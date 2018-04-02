package BaseDatos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;


import static BaseDatos.DataBaseManager.DT_USUARIO;
import static BaseDatos.DataBaseManager.T_USUARIO;


public class DbHelperControl extends SQLiteOpenHelper {
         private SQLiteDatabase db;

        // Si cambia el esquema de base de datos, debe incrementar la versión de la base de datos.
        private static final String DATABASE_NAME = "DbControl";
        private static final int DATABASE_VERSION = 3;

        //CONSTRUCTOR
        public DbHelperControl(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        //ABRIR LA BASE DE DATOS EN MODO LECTURA
        public void openReadableDB(){
            getReadableDatabase();
        }

        //ABRE LA BASE DE DATOS EN MODO LECTURA/ESCRITURA
        public void openWriteableDB(){
            getWritableDatabase();
        }

        //CERRAR LA BASE DE DATOS
        public void closeDB(){
            if(db!=null){
                db.close();
            }
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            //Si no existe la base de datos entonces la crea y ejecuta los siguientes comandos
            db.execSQL(T_USUARIO);

            //se insertan los datos de las tablas
            mockDataUsuario(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if ( newVersion > oldVersion )
            {
                //eliminamos las tablas
                db.execSQL(DT_USUARIO);

                onCreate(db);
            }
        }

        //CREACION DE DATOS FICTICIOS
        private void mockDataUsuario(SQLiteDatabase db) {
            mockUsuario(db, new Usuario("admin","admin"));
            mockUsuario(db, new Usuario("aamarilla","aamarilla"));
        }


        //PARA LA INSERCION DE LOS DATOS FICTICIOS
        private long mockUsuario(SQLiteDatabase db, Usuario usuario) {
            return db.insert(DataBaseManager.UsuarioEntry.TABLE_NAME, null,usuario.toContentValues());
        }


        //COMPROBAR UN USUARIO


        //OBTENER TODOS LOS CLIENTES ORDENADOS
       /* public Cursor getClientes() {

            //String [] fields = new String[]{ClienteEntry.NOMBRE, ClienteEntry.APELLIDO};
            //String orderBy = ClienteEntry.NOMBRE;
            return getReadableDatabase().rawQuery("SELECT id AS _id, nombre || ', ' || apellido as nombre_apellido, " +
                    "cedula ,direccion, telefono FROM Cliente ORDER by _id;", null);
        }*/

    }

