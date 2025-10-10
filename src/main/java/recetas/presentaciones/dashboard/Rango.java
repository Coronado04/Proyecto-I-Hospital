package recetas.presentaciones.dashboard;

public class Rango {
    private int mesInicio;
    private int anioInicio;
    private int mesFin;
    private int anioFin;

    public Rango(int mesInicio, int anioInicio, int mesFin, int anioFin) {
        this.mesInicio = mesInicio;
        this.anioInicio = anioInicio;
        this.mesFin = mesFin;
        this.anioFin = anioFin;
    }

    public int getMesInicio() {
        return mesInicio;
    }

    public void setMesInicio(int mesInicio) {
        this.mesInicio = mesInicio;
    }

    public int getAnioInicio() {
        return anioInicio;
    }

    public void setAnioInicio(int anioInicio) {
        this.anioInicio = anioInicio;
    }

    public int getMesFin() {
        return mesFin;
    }

    public void setMesFin(int mesFin) {
        this.mesFin = mesFin;
    }

    public int getAnioFin() {
        return anioFin;
    }

    public void setAnioFin(int anioFin) {
        this.anioFin = anioFin;
    }


}
