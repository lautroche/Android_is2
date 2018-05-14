package config;

import android.preference.PreferenceActivity;

public class ApiConfig {
    public static final String BASE = "http://192.168.0.2:8080/IS2/webresources/";
    public static final String AUTH = "http://192.168.0.2:8080/IS2/";

    public static final String USUARIOS = BASE + "com.control.rest.usuarios";
    public static final String TAREAS = BASE + "com.control.rest.tarea";
    public static final String PROYECTOS = BASE + "com.control.rest.proyecto";
    public static final String SPRINT = BASE + "com.control.rest.sprint";

    public static final String LOGIN = AUTH + "ValidarServlet";

    public static String CODEUSER=null;
    public static org.apache.http.Header[] HEADER=null;
}
