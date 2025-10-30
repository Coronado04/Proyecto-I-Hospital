package recetas.presentaciones.usuarios;

import progra3.logic.Usuario;
import recetas.presentaciones.AbstractTableModel;
import java.util.List;

public class TableModel extends AbstractTableModel implements javax.swing.table.TableModel {

    public static final int ID = 0;
    public static final int MENSAJE = 1; // será el checkbox

    private boolean[] seleccionado; // 🔹 nuevo

    public TableModel(int[] cols, List<Usuario> rows) {
        super(cols, rows);
        seleccionado = new boolean[rows.size()];
    }

    @Override
    protected Object getPropetyAt(Object o, int col) {
        Usuario u = (Usuario) o;
        switch (cols[col]) {
            case ID: return u.getId();
            case MENSAJE: return seleccionado[rows.indexOf(u)];
            default: return "";
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (cols[columnIndex] == MENSAJE) {
            seleccionado[rowIndex] = (Boolean) aValue;
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return cols[col] == MENSAJE;
    }

    @Override
    public Class<?> getColumnClass(int col) {
        return (cols[col] == MENSAJE) ? Boolean.class : String.class;
    }

    public boolean[] getSeleccionado() {
        return seleccionado;
    }

    @Override
    protected void initColNames() {
        colNames = new String[]{"Usuario", "Seleccionar"};
    }
}