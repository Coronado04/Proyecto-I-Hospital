package recetas.logic;



public class Linea {

    private Medicamento medicamento;
    private int cantidad;
    private String indicaciones;
    private int duracionDias;

    public Linea(){}
    public Linea(Medicamento medicamento, int cantidad, String indicaciones, int duracionDias) {
        this.medicamento = medicamento;
        this.cantidad = cantidad;
        this.indicaciones = indicaciones;
        this.duracionDias = duracionDias;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }

    public void setDuracionDias(int duracionDias) {
        this.duracionDias = duracionDias;
    }

    public Medicamento getMedicamento() { return medicamento; }
    public int getCantidad() { return cantidad; }
    public String getIndicaciones() { return indicaciones; }
    public int getDuracionDias() { return duracionDias; }

    @Override
    public String toString() {
        return medicamento.getNombre() + " " + medicamento.getPresentacion() +
                " x" + cantidad + " [" + indicaciones + ", " + duracionDias + " días]";
    }
}

