package recetas.presentaciones.Historico;

import recetas.Application;
import recetas.logic.Medicamento;
import recetas.logic.Receta;
import recetas.presentaciones.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {
    private Receta current;
    private List<Receta> listaReceta;
    private int mode;
    private Receta filter;

    public static final String CURRENT = "current";
    public static final String LIST = "listaReceta";
    public static final String FILTER="filter";

    public Model() {
        current=new Receta();
        listaReceta=new ArrayList<Receta>();
        this.mode= Application.MODE_CREATE;
        this.filter=new Receta();
    }
    public void init(List<Receta> listaReceta){
        this.listaReceta=listaReceta;
        this.current=new Receta();
        this.filter=new Receta();
        this.mode= Application.MODE_CREATE;
    }
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
        firePropertyChange(LIST);
        firePropertyChange(FILTER);
    }
    public Receta getCurrent() {
        return current;
    }

    public void setCurrent(Receta current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }
    public List<Receta> getListaReceta() {
        return listaReceta;
    }
    public void setListaReceta(List<Receta> listaReceta) {
        this.listaReceta = listaReceta;
        firePropertyChange(LIST);
    }
    public int getMode() {
        return mode;
    }
    public void setMode(int mode) {
        this.mode = mode;
    }
    public Receta getFilter() {
        return filter;
    }
    public void setFilter(Receta filter) {
        this.filter = filter;
    }
}
