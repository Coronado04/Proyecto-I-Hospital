package recetas.presentaciones.Historico;


import progra3.logic.Linea;
import recetas.presentaciones.AbstractTableModel;
import java.util.List;

public class DetalleTableModel extends AbstractTableModel<Linea> implements javax.swing.table.TableModel {

    public static final int MEDICAMENTO = 0;
    public static final int PRESENTACION = 1;
    public static final int CANTIDAD = 2;
    public static final int INDICACIONES = 3;
    public static final int DURACION = 4;

    public DetalleTableModel(int[] cols, List<Linea> rows) {
        super(cols, rows);
    }

    @Override
    protected Object getPropetyAt(Object obj, int col) {
        if (!(obj instanceof Linea)) return "";
        Linea linea = (Linea) obj;
        switch (cols[col]) {
            case MEDICAMENTO:
                return linea.getMedicamento().getNombre();
            case PRESENTACION:
                return linea.getMedicamento().getPresentacion();
            case CANTIDAD:
                return linea.getCantidad();
            case INDICACIONES:
                return linea.getIndicaciones();
            case DURACION:
                return linea.getDuracionDias();
            default:
                return "";
        }
    }

    @Override
    protected void initColNames() {
        colNames = new String[5];
        colNames[MEDICAMENTO] = "Medicamento";
        colNames[PRESENTACION] = "Presentación";
        colNames[CANTIDAD] = "Cantidad";
        colNames[INDICACIONES] = "Indicaciones";
        colNames[DURACION] = "Duración (días)";
    }
}