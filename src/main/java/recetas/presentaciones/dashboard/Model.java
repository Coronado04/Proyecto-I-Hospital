package recetas.presentaciones.dashboard;

import org.jfree.data.category.DefaultCategoryDataset;
import recetas.logic.Medicamento;
import recetas.presentaciones.AbstractModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {
    private List<Medicamento> list;
    private List<Medicamento> medicamentos;
    private Rango rango;
    private String[] columnas;
    private String[] filas;
    private int[][] datos;

    public static final String LIST = "list";
    public static final String MEDICAMENTOS = "medicamentos";
    public static final String DATOS = "datos";

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(LIST);
        firePropertyChange(MEDICAMENTOS);
        firePropertyChange(DATOS);
    }

    public Model() {}

    void init(List<Medicamento> lista){
        this.list = lista;
        this.medicamentos = new ArrayList<>();
        this.rango = new Rango(1, 2020, 2020, 1);
        this.columnas = new String[]{"2020-1"};
        this.filas = new String[]{};
        this.datos = new int[][]{};
    }

    public List<Medicamento> getList(){
        return list;
    }

    public void setList(List<Medicamento> list){
        this.list = list;
        firePropertyChange(LIST);
    }

    public List<Medicamento> getMedicamentos(){
        return medicamentos;
    }

    public void setMedicamentos(List<Medicamento> medicamentos){
        this.medicamentos = medicamentos;
        firePropertyChange(MEDICAMENTOS);
    }

    public Rango getRango(){
        return rango;
    }

    public String[] getColumnas(){
        return columnas;
    }

    public void setColumnas(String[] columnas){
        this.columnas = columnas;
    }

    public String[] getFilas(){
        return filas;
    }

    public void setFilas(String[] filas){
        this.filas = filas;
    }

    public void setDatos(int[][] datos){
        this.datos = datos;
        firePropertyChange(DATOS);
    }

    public DefaultCategoryDataset getDataset(){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for(int fila = 0; fila < filas.length; fila++){
            String medicamento = filas[fila];
            for(int columna = 0; columna < columnas.length; columna++){
                String mes = columnas[columna];
                int valor = datos[fila][columna];
                dataset.addValue(valor, medicamento, mes);
            }
        }

        return dataset;

    }

    public TableModel getTableModel(){
        return new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return filas.length;
            }

            @Override
            public int getColumnCount() {
                return columnas.length+1;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                if(columnIndex == 0)
                    return filas[rowIndex];
                return datos[rowIndex][columnIndex-1];
            }

            public String getColumnName(int column) {
                if(column == 0)
                    return "Medicamento";
                return columnas[column-1];
            }
        };
    }

}
