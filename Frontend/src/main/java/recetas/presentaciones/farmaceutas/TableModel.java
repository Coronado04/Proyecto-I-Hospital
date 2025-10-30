package recetas.presentaciones.farmaceutas;


import progra3.logic.Farmaceuta;
import recetas.presentaciones.AbstractTableModel;

import java.util.List;

public class TableModel extends AbstractTableModel<Farmaceuta> implements javax.swing.table.TableModel {
    public TableModel(int[] cols, List<Farmaceuta> rows) {
        super(cols, rows);
    }


    public static final int ID = 0;
    public static final int NOMBRE = 1;
    @Override
    protected void initColNames() {
        colNames = new String[2];
        colNames[ID] = "Id";
        colNames[NOMBRE] = "Nombre";
    }
    @Override
    protected Object getPropetyAt(Object obj, int col) {
        if (!(obj instanceof Farmaceuta)) return "";
        Farmaceuta e = (Farmaceuta) obj;
        switch (cols[col]) {
            case ID:
                return e.getId();
            case NOMBRE:
                return e.getNombre();
            default:
                return "";
        }
    }

}