package BaseDatos;

import android.provider.BaseColumns;

/**
 *
 *
 */

/*Se implementó la interfaz BaseColumns con el fin de agregar una columna extra que se recomienda tenga toda tabla*/

public class DataBaseManager {
    //Clases internas que definen los contenidos de las tablas.
    public static class UsuarioEntry implements BaseColumns {
        public static final String TABLE_NAME = "Usuarios";

        public static final String NOMBRE = "nombre";
        public static final String CLAVE = "clave";
    }


    //Creacion de Tablas en variables constantes
    public static final String T_USUARIO =
            "CREATE TABLE " + UsuarioEntry.TABLE_NAME + "("
                    + UsuarioEntry.NOMBRE + " TEXT PRIMARY KEY,"
                    + UsuarioEntry.CLAVE + " TEXT NOT NULL);";


    //Para eliminar tablas
    public static final String DT_USUARIO = "DROP TABLE IF EXISTS " + UsuarioEntry.TABLE_NAME;





}
