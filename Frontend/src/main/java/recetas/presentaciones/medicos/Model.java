package recetas.presentaciones.medicos;

import progra3.logic.Medico;
import recetas.Application;
import recetas.presentaciones.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {
    private Medico current;
    private List<Medico> listaMedico;
    private int mode;
    private Medico filter;

    public static final String CURRENT = "current";
    public static final String LIST = "listaMedico";
    public static final String FILTER="filter";

    public Model() {
        current=new Medico();
        listaMedico=new ArrayList<Medico>();
        this.mode= Application.MODE_CREATE;
        this.filter=new Medico();
    }
    public void init(List<Medico> listaMedico){
        this.listaMedico=listaMedico;
        this.current=new Medico();
        this.filter=new Medico();
        this.mode = Application.MODE_CREATE;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
        firePropertyChange(LIST);
        firePropertyChange(FILTER);
    }

    public Medico getCurrent() {
        return current;
    }

    public void setCurrent(Medico current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }
    public List<Medico> getListaMedico() {
        return listaMedico;
    }

    public void setListaMedico(List<Medico> listaMedico) {
        this.listaMedico = listaMedico;
        firePropertyChange(LIST);
    }
    public int getMode() {
        return mode;
    }
    public void setMode(int mode) {
        this.mode = mode;
    }
    public Medico getFilter() {
        return filter;
    }
    public void setFilter(Medico filter) {
        this.filter = filter;
    }


}
