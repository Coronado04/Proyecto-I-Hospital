package recetas.presentaciones.Preescribir;

import progra3.logic.*;
import recetas.Application;
import recetas.logic.*;
import recetas.presentaciones.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.List;

public class Model extends AbstractModel {
    private Receta currentReceta;
    private List<Receta> listaReceta;
    private Receta filter;
    private int mode;
    private Linea currentLinea;
    private List<Linea> listaLinea;

    private List<Paciente> pacientes;
    private Paciente currentPaciente;

    private List<Medico> medicos;
    private Medico currentMedico;

    private List<Medicamento> medicamentos;
    private Medicamento currentMedicamento;



    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
        firePropertyChange(LISTRECETA);
        firePropertyChange(FILTER);
        firePropertyChange(LISTPACIENTE);
        firePropertyChange(LISTMEDICO);
        firePropertyChange(LISTMEDICAMENTO);
        firePropertyChange(CURRENTPACIENTE);
        firePropertyChange(CURRENTMEDICO);
        firePropertyChange(CURRENTMEDICAMENTO);
        firePropertyChange(CURRENTLINEA);
        firePropertyChange(LISTLINEA);
    }

    // Constantes de propiedades
    public static final String CURRENT = "current";
    public static final String LISTRECETA = "listaReceta";
    public static final String FILTER = "filter";
    public static final String LISTPACIENTE = "listaPaciente";
    public static final String LISTMEDICO = "listaMedico";
    public static final String LISTMEDICAMENTO = "listaMedicamento";
    public static final String CURRENTPACIENTE = "currentPaciente";
    public static final String CURRENTMEDICO = "currentMedico";
    public static final String CURRENTMEDICAMENTO = "currentMedicamento";
    public static final String CURRENTLINEA = "currentLinea";
    public static final String LISTLINEA = "listaLinea";

    public Model() {
        currentReceta = new Receta();
        this.mode = Application.MODE_CREATE;
        this.filter = new Receta();
    }

    public void init(List<Receta> listaReceta, List<Paciente> pacientes, List<Medico> medicos, List<Medicamento> medicamentos, List<Linea> lineas) {
        this.listaReceta = listaReceta;
        this.pacientes = pacientes;
        this.medicos = medicos;
        this.medicamentos = medicamentos;
        this.filter = new Receta();
        this.listaLinea = lineas;
    }
    public void initB(List<Receta> listaReceta, List<Paciente> pacientes, List<Medicamento> medicamentos) {
        this.listaReceta = listaReceta;
        this.pacientes = pacientes;
        this.medicamentos = medicamentos;
        this.filter = new Receta();
    }

    // --- Getters y Setters públicos ---
    public Receta getCurrentReceta() { return currentReceta; }
    public void setCurrentReceta(Receta currentReceta) { this.currentReceta = currentReceta; }


    public List<Paciente> getPacientes() { return pacientes; }
    public void setPacientes(List<Paciente> pacientes) { this.pacientes = pacientes; }

    public Paciente getCurrentPaciente() { return currentPaciente; }
    public void setCurrentPaciente(Paciente currentPaciente) {
        Paciente old = this.currentPaciente;
        this.currentPaciente = currentPaciente;
        firePropertyChange(CURRENTPACIENTE);
    }

    public Medico getCurrentMedico() { return currentMedico; }
    public void setCurrentMedico(Medico currentMedico) { this.currentMedico = currentMedico; }

    public List<Medicamento> getMedicamentos() { return medicamentos; }
    public void setMedicamentos(List<Medicamento> medicamentos) { this.medicamentos = medicamentos; }

    public Medicamento getCurrentMedicamento() { return currentMedicamento; }

    public void setCurrentMedicamento(Medicamento currentMedicamento) {
        Medicamento old = this.currentMedicamento;
        this.currentMedicamento = currentMedicamento;
        firePropertyChange(CURRENTMEDICAMENTO);
    }

    public Linea getCurrentLinea() { return currentLinea; }

    public void setCurrentLinea(Linea currentLinea) {
        Linea old = this.currentLinea;
        this.currentLinea = currentLinea;
        firePropertyChange(CURRENTLINEA);
    }

    public void deleteLinea(){
        currentReceta.deleteLinea(currentLinea);
        updateModel();
        setCurrentLinea(new Linea());
    }

    public int getMode() { return mode; }
    public void setMode(int mode) { this.mode = mode; }

    public Receta getFilter() { return filter; }
    public void setFilter(Receta filter) { this.filter = filter; }

    // --- Notificación a la vista ---
    public void updateModel() {
        firePropertyChange(LISTRECETA);
        firePropertyChange(FILTER);
        firePropertyChange(CURRENT);
        firePropertyChange(LISTPACIENTE);
        firePropertyChange(LISTMEDICO);
        firePropertyChange(LISTMEDICAMENTO);
        firePropertyChange(CURRENTPACIENTE);
        firePropertyChange(CURRENTMEDICO);
        firePropertyChange(CURRENTMEDICAMENTO);
        firePropertyChange(LISTLINEA);
    }
}

