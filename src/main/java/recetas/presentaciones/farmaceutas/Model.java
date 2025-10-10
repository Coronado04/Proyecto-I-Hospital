package recetas.presentaciones.farmaceutas;

import recetas.Application;
import recetas.logic.Farmaceuta;
import recetas.presentaciones.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {
    private Farmaceuta current;
    private List<Farmaceuta> listaFarmaceutas;
    private int mode;
    private Farmaceuta filter;

    public static final String CURRENT = "current";
    public static final String LIST = "listaFarmaceutas";
    public static final String FILTER="filter";

    public Model() {
        current=new Farmaceuta();
        listaFarmaceutas =new ArrayList<Farmaceuta>();
        this.mode= Application.MODE_CREATE;
        this.filter=new Farmaceuta();
    }

    public void init(List<Farmaceuta> listaFarmaceutas){
        this.listaFarmaceutas=listaFarmaceutas;
        this.current=new Farmaceuta();
        this.filter=new Farmaceuta();
        this.mode= Application.MODE_CREATE;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
        firePropertyChange(LIST);
        firePropertyChange(FILTER);
    }

    public Farmaceuta getCurrent() {
        return current;
    }

    public void setCurrent(Farmaceuta current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }
    public List<Farmaceuta> getListaFarmaceutas() {
        return listaFarmaceutas;
    }

    public void setListaFarmaceutas(List<Farmaceuta> listaFarmaceutas) {
        this.listaFarmaceutas = listaFarmaceutas;
        firePropertyChange(LIST);
    }
    public int getMode() {
        return mode;
    }
    public void setMode(int mode) {
        this.mode = mode;
    }
    public Farmaceuta getFilter() {
        return filter;
    }
    public void setFilter(Farmaceuta filter) {
        this.filter = filter;
    }




}