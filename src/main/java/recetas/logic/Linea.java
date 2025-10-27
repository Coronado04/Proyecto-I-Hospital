package recetas.logic;

import java.time.LocalDate;

public class Linea {

    private int numero; // número de la línea en BD
    private Medicamento medicamento;
    private int cantidad;
    private String indicaciones;
    private int duracionDias;

    // Nueva propiedad: fecha de la receta (para agrupaciones en dashboard)
    private LocalDate fechaReceta;

    public Linea() {}

    public Linea(int numero, Medicamento medicamento, int cantidad, String indicaciones, int duracionDias) {
        this.numero = numero;
        this.medicamento = medicamento;
        this.cantidad = cantidad;
        this.indicaciones = indicaciones;
        this.duracionDias = duracionDias;
    }
    public Linea(Medicamento medicamento, int cantidad, String indicaciones, int duracionDias) {
        this.medicamento = medicamento;
        this.cantidad = cantidad;
        this.indicaciones = indicaciones;
        this.duracionDias = duracionDias;
    }
    // Getters y setters
    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public Medicamento getMedicamento() { return medicamento; }
    public void setMedicamento(Medicamento medicamento) { this.medicamento = medicamento; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public String getIndicaciones() { return indicaciones; }
    public void setIndicaciones(String indicaciones) { this.indicaciones = indicaciones; }

    public int getDuracionDias() { return duracionDias; }
    public void setDuracionDias(int duracionDias) { this.duracionDias = duracionDias; }

    public LocalDate getFechaReceta() { return fechaReceta; }
    public void setFechaReceta(LocalDate fechaReceta) { this.fechaReceta = fechaReceta; }

    @Override
    public String toString() {
        String med = (medicamento != null) ? medicamento.getNombre() : "Sin med";
        String pres = (medicamento != null) ? medicamento.getPresentacion() : "";
        return "Línea #" + numero + ": " + med + " " + pres +
                " x" + cantidad + " [" + indicaciones + ", " + duracionDias + " días]";
    }
}