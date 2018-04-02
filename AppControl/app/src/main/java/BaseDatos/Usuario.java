package BaseDatos;

import android.content.ContentValues;

import BaseDatos.DataBaseManager.UsuarioEntry;

/**
 *
 *
 */
 
//Entidad Usuario
public class Usuario {
    private String nombre;
    private String clave;

    //Constructor de la clase
    public Usuario(String nombre, String clave){
        this.nombre = nombre;
        this.clave = clave;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public String getNombre(){
        return nombre;
    }

    public void setclave(String clave){
        this.clave = clave;
    }

    public String getclave(){
        return clave;
    }


    public ContentValues toContentValues() {
        //objeto de tipo ContentValues, almacena los valores de los atributos de los objetos.
        ContentValues cv = new ContentValues();

        cv.put(UsuarioEntry.NOMBRE, nombre);
        cv.put(UsuarioEntry.CLAVE, clave);

        return cv;
    }
}
