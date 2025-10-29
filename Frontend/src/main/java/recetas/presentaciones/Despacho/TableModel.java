package recetas.presentaciones.Despacho;

import recetas.logic.Receta;
import recetas.presentaciones.AbstractTableModel;

import java.util.List;

public class TableModel extends AbstractTableModel<Receta> implements javax.swing.table.TableModel{
    public TableModel(int[] cols, List<Receta> rows) {
        super(cols, rows);
    }

    public static final int IDRECETA = 0;
    public static final int IDPACIENTE = 1;
    public static final int NOMBREPACIENTE = 2;
    public static final int FECHARETIRO = 3;
    public static final int ESTADO = 4;

    @Override
    protected Object getPropetyAt(Receta e, int col) {
        switch (cols[col]) {
            case IDRECETA:
                return e.getIdReceta();
            case IDPACIENTE:
                return e.getPaciente().getId();
            case NOMBREPACIENTE:
                return e.getPaciente().getNombre();
            case FECHARETIRO:
                return e.getFechaRetiro();
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
        colNames[FECHARETIRO] = "Fecha de retiro";
        colNames[ESTADO] = "Estado";
    }

}
