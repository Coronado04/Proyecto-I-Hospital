package recetas.presentaciones.farmaceutas;

import recetas.logic.Farmaceuta;
import recetas.presentaciones.AbstractTableModel;

import java.util.List;

public class TableModel extends AbstractTableModel<Farmaceuta> implements javax.swing.table.TableModel {
    public TableModel(int[] cols, List<Farmaceuta> rows) {
        super(cols, rows);
    }


    public static final int ID = 0;
    public static final int NOMBRE = 1;
    public static final int ESPECIALIDAD = 2;
    @Override
    protected void initColNames() {
        colNames = new String[4];
        colNames[ID] = "Id";
        colNames[NOMBRE] = "Nombre";
        colNames[ESPECIALIDAD] = "Especialidad";
    }
    @Override
    protected Object getPropetyAt(Farmaceuta e, int col) {
        switch (cols[col]) {
            case ID:
                return e.getId();
            case NOMBRE:
                return e.getNombre();
            case ESPECIALIDAD:
                return "farmaceuta";
            default:
                return "";
        }
    }

}
