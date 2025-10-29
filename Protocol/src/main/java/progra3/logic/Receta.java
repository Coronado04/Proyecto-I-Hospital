package progra3.logic;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Receta implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Estado { CONFECCIONADA, PROCESO, LISTA, ENTREGADA }

    private String idReceta;
    private Medico medico;
    private Paciente paciente;
    private List<Linea> detalles = new ArrayList<>();
    private LocalDate fechaConfeccion;
    private LocalDate fechaRetiro;
    private Estado estado;

    public Receta() {
        idReceta = "";
        this.detalles = new ArrayList<>();
    }

    public Receta(String idReceta, Medico medico, Paciente paciente, LocalDate fechaRetiro) {
        this.idReceta = idReceta;
        this.medico = medico;
        this.paciente = paciente;
        this.fechaConfeccion = LocalDate.now();
        this.fechaRetiro = fechaRetiro;
        this.estado = Estado.CONFECCIONADA;
        this.detalles = new ArrayList<>();
    }
    public void agregarDetalle(Linea detalle) {
        if (detalle == null) return;
        if (detalles == null) detalles = new ArrayList<>();
        detalles.add(detalle);
    }

    public void eliminarDetalle(Linea detalle) {
        if (detalles != null) detalles.remove(detalle);
    }

    public void modificarDetalle(int index, Linea nuevoDetalle) {
        if (detalles != null && index >= 0 && index < detalles.size()) {
            detalles.set(index, nuevoDetalle);
        }
    }

    public String getIdReceta() { return idReceta; }
    public Medico getMedico() { return medico; }
    public Paciente getPaciente() { return paciente; }
    public List<Linea> getDetalles() {
        if (detalles == null) detalles = new ArrayList<>();
        return detalles;
    }
    public LocalDate getFechaConfeccion() { return fechaConfeccion; }
    public LocalDate getFechaRetiro() { return fechaRetiro; }
    public Estado getEstado() { return estado; }

    public void deleteLinea(Linea e){
        if (detalles != null) detalles.remove(e);
    }

    public void setDetalles(List<Linea> detalles) {
        if (detalles == null) {
            this.detalles = new ArrayList<>();
        } else {
            this.detalles = detalles;
        }
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        String nombrePaciente = (paciente != null) ? paciente.getNombre() : "Sin paciente";
        return "Receta #" + idReceta + " - Paciente: " + nombrePaciente + " - Estado: " + estado;
    }
    public void setFechaConfeccion(LocalDate fechaConfeccion) {
        this.fechaConfeccion = fechaConfeccion;
    }
    public void setFechaRetiro(LocalDate fechaRetiro) {
        this.fechaRetiro = fechaRetiro;
    }
    public void setIdReceta(String idReceta) {
        this.idReceta = idReceta;
    }
    public void setMedico(Medico medico) {
        this.medico = medico;
    }
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
}