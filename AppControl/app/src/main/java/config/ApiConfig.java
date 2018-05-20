package config;

import android.preference.PreferenceActivity;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

import java.util.List;

public class ApiConfig {
    public static final String BASE = "http://192.168.0.2:8080/IS2/webresources/";
    public static final String AUTH = "http://192.168.0.2:8080/IS2/";

    public static final String USUARIOS = BASE + "com.control.rest.usuarios";
    public static final String TAREAS = BASE + "com.control.rest.tarea";
    public static final String PROYECTOS = BASE + "com.control.rest.proyecto";
    public static final String SPRINT = BASE + "com.control.rest.sprint";

    public static final String LOGIN = AUTH + "ValidarServlet";
    public static final String LOGOUT = AUTH + "CerrarSesionServlet";


    public static final String GENERIC_ERROR = "Ha ocurrido un error al relizar la consulta";
    public static String CODEUSER=null;
    public static org.apache.http.Header[] HEADER=null;

    public static CookieStore COOKIE;
}
