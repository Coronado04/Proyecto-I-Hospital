package recetas.presentaciones.Despacho;

import recetas.Application;
import recetas.logic.Receta;
import recetas.presentaciones.AbstractModel;

import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {
    private Receta currentReceta;
    private List<Receta> listaRecetas;
    private int mode;
    private Receta filter;

    public static final String CURRENT = "currentReceta";
    public static final String LIST = "listaRecetas";
    public static final String FILTER="filter";

    public Model() {
     currentReceta = new Receta();
     listaRecetas =new ArrayList<Receta>();
     this.mode= Application.MODE_CREATE;
     this.filter=new Receta();
    }
    public void init(List<Receta> listaRecetas){
        this.listaRecetas=listaRecetas;
        this.currentReceta=new Receta();
        this.filter=new Receta();
        this.mode= Application.MODE_CREATE;
    }
    @Override
    public void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
        firePropertyChange(LIST);
        firePropertyChange(FILTER);
    }
    public Receta getCurrentReceta() {
        return currentReceta;
    }
    public void setCurrentReceta(Receta currentReceta) {
        this.currentReceta = currentReceta;
        firePropertyChange(CURRENT);
    }
    public List<Receta> getListaRecetas() {
        return listaRecetas;
    }
    public void setListaRecetas(List<Receta> listaRecetas) {
        this.listaRecetas = listaRecetas;
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
