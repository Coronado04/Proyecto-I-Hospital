package recetas.presentaciones.Preescribir;

import recetas.logic.*;


import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.List;

public class Controller {
    private ViewPreescribir view;
    private Model model;

    public Controller(ViewPreescribir view, Model model, Usuario u) {
        model.init(
                Service.instance().search(new Receta()),      // lista de recetas
                Service.instance().searchPaciente(new Paciente()),    // lista de pacientes
                Service.instance().search(new Medico()),      // lista de médicos
                Service.instance().searchMedicamento(new Medicamento()),
                Service.instance().searchLinea(new Linea())// lista de medicamentos


        );
        String idMed = u.getId();
        Medico auxMedico = new Medico();
        auxMedico.setId(idMed);
        List<Medico> actual = Service.instance().searchMedico(auxMedico);


        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
        setCurrentMedico(actual.getFirst());
    }


    public void setCurrentPaciente(Paciente p) {
        model.setCurrentPaciente(p);
    }

    public void setCurrentMedico(Medico m) {
        model.setCurrentMedico(m);
    }

    public void setCurrentMedicamento(Medicamento m) {
        model.setCurrentMedicamento(m);
    }


    public void setCurrentLinea(int row) {
        model.setCurrentLinea(model.getCurrentReceta().getDetalles().get(row));
    }


    public void addMedicamento(Medicamento m, int cantidad, String indicaciones, int dias) throws Exception {
        if (m == null) throw new Exception("Debe seleccionar un medicamento.");
        if (cantidad <= 0) throw new Exception("La cantidad debe ser mayor a 0.");
        if (dias <= 0) throw new Exception("La duración debe ser mayor a 0 días.");

        Linea detalle = new Linea(m, cantidad, indicaciones, dias);
        model.getCurrentReceta().agregarDetalle(detalle);
        model.updateModel();
    }

    public void delete() throws Exception{
        if(!model.getCurrentLinea().equals(new Linea()))
            model.deleteLinea();
        else throw new Exception("No se seleccionó un medicamento.");
    }

    public Linea getCurrentLinea(){
        return model.getCurrentLinea();
    }







    // --- Limpiar receta actual ---
    public void clearReceta() {
        model.setCurrentReceta(new Receta());
        model.updateModel();
    }

    // --- Búsquedas ---
    public void searchPaciente(Paciente filtro) throws Exception {
        model.setPacientes(Service.instance().searchPaciente(filtro));
        model.updateModel();
    }

    public void searchMedicamento(Medicamento filtro) throws Exception {
        model.setMedicamentos(Service.instance().searchMedicamento(filtro));
        model.updateModel();
    }

    public void saveReceta(LocalDate fechaRetiro) throws Exception {

        Receta receta = model.getCurrentReceta();
        receta.setEstado(Receta.Estado.CONFECCIONADA);
        receta.setFechaRetiro(fechaRetiro);
        receta.setPaciente(model.getCurrentPaciente());
        receta.setMedico(model.getCurrentMedico());
        receta.setFechaConfeccion(LocalDate.now());

        if (model.getCurrentPaciente() == null) JOptionPane.showMessageDialog(view.getPanel(), "Paciente no seleccionado", "",  JOptionPane.ERROR_MESSAGE);
        if (model.getCurrentMedico() == null) JOptionPane.showMessageDialog(view.getPanel(), "Médico no seleccionado", "",  JOptionPane.ERROR_MESSAGE);
        if (model.getCurrentReceta().getDetalles().isEmpty()) JOptionPane.showMessageDialog(view.getPanel(), "Seleccione al menos un medicamento", "",  JOptionPane.ERROR_MESSAGE);

        // Generar un id único (ej: REC-001)
        receta.setIdReceta("REC-" + Service.instance().getNextRecetaId());

        Service.instance().create(receta);
        model.updateModel();
        clearReceta();


    }
}

