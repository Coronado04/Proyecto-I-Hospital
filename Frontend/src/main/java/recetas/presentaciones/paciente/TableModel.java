package recetas.presentaciones.paciente;

import recetas.logic.Paciente;
import recetas.presentaciones.AbstractTableModel;

import java.util.List;

public class TableModel extends AbstractTableModel<Paciente> implements javax.swing.table.TableModel {
    public TableModel(int[] cols, List<Paciente> rows) {
        super(cols, rows);
    }
    public static final int ID = 0;
    public static final int NOMBRE = 1;
    public static final int FechaDeNacimiento = 2;
    public static final int Telefono = 3;

    @Override
    protected void initColNames() {
        colNames = new String[4];
        colNames[ID] = "Id";
        colNames[NOMBRE] = "Nombre";
        colNames[FechaDeNacimiento] = "Fecha de Nacimiento";
        colNames[Telefono] = "Telefono";
    }
    @Override
    protected Object getPropetyAt(Paciente e, int col) {
        switch (cols[col]) {
            case ID:
                return e.getId();
            case NOMBRE:
                return e.getNombre();
            case FechaDeNacimiento:
                return e.getFechaNacimiento();
                case Telefono:
                    return e.getNumero();
            default:
                return "";
        }
    }





}