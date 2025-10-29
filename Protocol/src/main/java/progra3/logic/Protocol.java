package progra3.logic;

public class Protocol {
    public static final String SERVER = "localhost";
    public static final int PORT = 1234;

    public static final int MEDICO_CREATE=101;
    public static final int MEDICO_READ=102;
    public static final int MEDICO_UPDATE=103;
    public static final int MEDICO_DELETE=104;
    public static final int PRODUCTO_SEARCH=105;

    public static final int ERROR_NO_ERROR=0;
    public static final int ERROR_ERROR=1;

    public static final int DISCONNECT=99;
}
