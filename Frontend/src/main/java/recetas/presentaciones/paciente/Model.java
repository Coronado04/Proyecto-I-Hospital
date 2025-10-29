package recetas.presentaciones.paciente;

import progra3.logic.Paciente;
import recetas.Application;
import recetas.presentaciones.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {
    private Paciente current;
    private List<Paciente> listaPaciente;
    private int mode;
    private Paciente filter;

    public static final String CURRENT = "current";
    public static final String LIST = "listaPaciente";
    public static final String FILTER="filter";

    public Model() {
        current=new Paciente();
        listaPaciente=new ArrayList<Paciente>();
        this.mode= Application.MODE_CREATE;
        this.filter=new Paciente();
    }

    public void init(List<Paciente> listaPaciente){
        this.listaPaciente=listaPaciente;
        this.current=new Paciente();
        this.filter=new Paciente();
        this.mode= Application.MODE_CREATE;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
        firePropertyChange(LIST);
        firePropertyChange(FILTER);
    }
    public Paciente getCurrent() {
        return current;
    }
    public void setCurrent(Paciente current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }
    public List<Paciente> getListaPaciente() {
        return listaPaciente;
    }
    public void setListaPaciente(List<Paciente> listaPaciente) {
        this.listaPaciente = listaPaciente;
        firePropertyChange(LIST);
    }
    public int getMode() {
        return mode;
    }
    public void setMode(int mode) {
        this.mode = mode;
    }
    public Paciente getFilter() {
        return filter;
    }
    public void setFilter(Paciente filter) {
        this.filter = filter;
    }

}
