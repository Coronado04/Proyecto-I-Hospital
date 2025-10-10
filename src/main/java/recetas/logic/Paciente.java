package recetas.logic;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlID;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import recetas.data.LocalDateAdapter;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;


@XmlAccessorType(XmlAccessType.FIELD)
public class Paciente {
    @XmlID
    private String id;
    private String nombre;
    @XmlJavaTypeAdapter(value=LocalDateAdapter.class)
    private LocalDate fechaNacimiento;

    private String numero;


    public Paciente() {this("", "", null, "");}

    public Paciente(String id, String nombre, LocalDate fechaNacimiento, String numero) {
        this.id = id;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.numero = numero;
    }

    public String getId() {
        return id;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNumero() {
        return numero;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Paciente paciente = (Paciente) o;
        return Objects.equals(id, paciente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return nombre;
    }

}
