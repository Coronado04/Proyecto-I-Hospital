package recetas.presentaciones.medicamentos;

import recetas.Application;
import recetas.logic.Medicamento;
import recetas.presentaciones.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {
    private Medicamento current;
    private List<Medicamento> listaMedicamento;
    private int mode;
    private Medicamento filter;

    public static final String CURRENT = "current";
    public static final String LIST = "listaMedicamento";
    public static final String FILTER="filter";

    public Model() {
        current=new Medicamento();
        listaMedicamento=new ArrayList<Medicamento>();
        this.mode= Application.MODE_CREATE;
        this.filter=new Medicamento();
    }

    public void init(List<Medicamento> listaMedicamento){
        this.listaMedicamento=listaMedicamento;
        this.current=new Medicamento();
        this.filter=new Medicamento();
        this.mode= Application.MODE_CREATE;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
        firePropertyChange(LIST);
        firePropertyChange(FILTER);
    }

    public Medicamento getCurrent() {
        return current;
    }

    public void setCurrent(Medicamento current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }
    public List<Medicamento> getListaMedicamento() {
        return listaMedicamento;
    }

    public void setListaMedicamento(List<Medicamento> listaMedicamento) {
        this.listaMedicamento = listaMedicamento;
        firePropertyChange(LIST);
    }
    public int getMode() {
        return mode;
    }
    public void setMode(int mode) {
        this.mode = mode;
    }
    public Medicamento getFilter() {
        return filter;
    }
    public void setFilter(Medicamento filter) {
        this.filter = filter;
    }





}