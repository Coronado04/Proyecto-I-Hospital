package recetas.presentaciones.Historico;

import progra3.logic.Receta;
import recetas.presentaciones.AbstractTableModel;

import java.util.List;

public class TableModel extends AbstractTableModel<Receta> implements javax.swing.table.TableModel {
    public TableModel(int[] cols, List<Receta> rows) {
        super(cols, rows);
    }

    public static final int IDRECETA = 0;
    public static final int IDPACIENTE = 1;
    public static final int NOMBREPACIENTE = 2;
    public static final int NOMBREMEDICO = 3;
    public static final int ESTADO = 4;

    @Override
    protected Object getPropetyAt(Object obj, int col) {
        if (!(obj instanceof Receta)) return "";
        Receta e = (Receta) obj;
        switch (cols[col]) {
            case IDRECETA:
                return e.getIdReceta();
            case NOMBREPACIENTE:
                return e.getPaciente().getNombre();
            case IDPACIENTE:
                return e.getPaciente().getId();
            case NOMBREMEDICO:
                return e.getMedico().getNombre();
            case ESTADO:
                return e.getEstado();
            default:
                return "";
        }
    }

    @Override
    protected void initColNames() {
        colNames = new String[5];
        colNames[IDRECETA] = "ID Receta";
        colNames[IDPACIENTE] = "ID Paciente";
        colNames[NOMBREPACIENTE] = "Nombre Paciente";
        colNames[NOMBREMEDICO] = "Nombre Médico";
        colNames[ESTADO] = "Estado";
    }
}