package recetas.presentaciones.medicos;

import progra3.logic.Medico;
import recetas.presentaciones.AbstractTableModel;

import java.util.List;

public class TableModel extends AbstractTableModel<Medico> implements javax.swing.table.TableModel {
    public TableModel(int[] cols, List<Medico> rows) {
        super(cols, rows);
    }


    public static final int ID = 0;
    public static final int NOMBRE = 1;
    public static final int ESPECIALIDAD = 2;
    @Override
    protected void initColNames() {
        colNames = new String[3];
        colNames[ID] = "Id";
        colNames[NOMBRE] = "Nombre";
        colNames[ESPECIALIDAD] = "Especialidad";
    }
    @Override
    protected Object getPropetyAt(Object obj, int col) {
        // cast interno protegido
        if (!(obj instanceof Medico)) return "";
        Medico e = (Medico) obj;
        switch (cols[col]) {
            case ID:
                return e.getId();
            case NOMBRE:
                return e.getNombre();
            case ESPECIALIDAD:
                return e.getEspecialidad();
            default:
                return "";
        }
    }

}