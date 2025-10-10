package recetas.presentaciones.medicamentos;

import recetas.logic.Medicamento;
import recetas.presentaciones.AbstractTableModel;

import java.util.List;

public class TableModel extends AbstractTableModel<Medicamento> implements javax.swing.table.TableModel {
    public TableModel(int[] cols, List<Medicamento> rows) {
        super(cols, rows);
    }


    public static final int CODIGO = 0;
    public static final int NOMBRE = 1;
    public static final int PRESENTACION = 2;
    @Override
    protected void initColNames() {
        colNames = new String[4];
        colNames[CODIGO] = "Codigo";
        colNames[NOMBRE] = "Nombre";
        colNames[PRESENTACION] = "Presentacion";
    }
    @Override
    protected Object getPropetyAt(Medicamento e, int col) {
        switch (cols[col]) {
            case CODIGO:
                return e.getCodigo();
            case NOMBRE:
                return e.getNombre();
            case PRESENTACION:
                return e.getPresentacion();
            default:
                return "";
        }
    }

}