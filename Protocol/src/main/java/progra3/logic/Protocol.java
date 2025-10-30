package progra3.logic;

public class Protocol {
    public static final String SERVER = "localhost";
    public static final int PORT = 1234;

    // -------- MEDICO --------
    public static final int MEDICO_CREATE = 101;
    public static final int MEDICO_READ = 102;
    public static final int MEDICO_UPDATE = 103;
    public static final int MEDICO_DELETE = 104;
    public static final int MEDICO_SEARCH = 105;
    public static final int MEDICO_FIND_ALL = 106;

    // -------- PACIENTE --------
    public static final int PACIENTE_CREATE = 201;
    public static final int PACIENTE_READ = 202;
    public static final int PACIENTE_UPDATE = 203;
    public static final int PACIENTE_DELETE = 204;
    public static final int PACIENTE_SEARCH = 205;
    public static final int PACIENTE_FIND_ALL = 206;

    // -------- MEDICAMENTO --------
    public static final int MEDICAMENTO_CREATE = 301;
    public static final int MEDICAMENTO_READ = 302;
    public static final int MEDICAMENTO_UPDATE = 303;
    public static final int MEDICAMENTO_DELETE = 304;
    public static final int MEDICAMENTO_SEARCH = 305;
    public static final int MEDICAMENTO_FIND_ALL = 306;

    // -------- FARMACEUTA --------
    public static final int FARMACEUTA_CREATE = 401;
    public static final int FARMACEUTA_READ = 402;
    public static final int FARMACEUTA_UPDATE = 403;
    public static final int FARMACEUTA_DELETE = 404;
    public static final int FARMACEUTA_SEARCH = 405;
    public static final int FARMACEUTA_FIND_ALL = 406;

    // -------- LINEA --------
    public static final int LINEA_CREATE = 501;
    public static final int LINEA_READ = 502;
    public static final int LINEA_UPDATE = 503;
    public static final int LINEA_DELETE = 504;
    public static final int LINEA_SEARCH = 505;

    // -------- RECETA --------
    public static final int RECETA_CREATE = 601;
    public static final int RECETA_READ = 602;
    public static final int RECETA_UPDATE = 603;
    public static final int RECETA_DELETE = 604;
    public static final int RECETA_SEARCH = 605;
    public static final int RECETA_FIND_ALL = 606;

    // -------- USUARIO --------
    public static final int USUARIO_CREATE = 701;
    public static final int USUARIO_READ = 702;
    public static final int USUARIO_UPDATE = 703;
    public static final int USUARIO_DELETE = 704;
    public static final int USUARIO_SEARCH = 705;
    public static final int USUARIO_FIND_ALL = 706;
    public static final int USUARIO_LOGIN = 707;
    public static final int USUARIO_CAMBIAR_CLAVE = 708;

    public static final int DELIVER_LOGIN = 800;
    public static final int DELIVER_LOGOUT = 801;

    // -------- MENSAJES ENTRE USUARIOS --------
    public static final int USUARIO_MENSAJE = 709;   // Cliente -> Servidor
    public static final int DELIVER_MENSAJE = 802;   // Servidor -> Cliente

    public static final int SYNC=97;
    public static final int ASYNC=98;
    public static final int DISCONNECT = 99;

    public static final int ERROR_NO_ERROR = 0;
    public static final int ERROR_ERROR = 1;
}
