package recetas.data;

import jakarta.xml.bind.annotation.*;
import recetas.logic.*;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Data {


    @XmlElementWrapper(name = "pacientes")
    @XmlElement(name = "paciente")
    private List<Paciente> pacientes;

    @XmlElementWrapper(name = "medicos")
    @XmlElement(name = "medico")
    private List<Medico> medicos;

    @XmlElementWrapper(name = "farmaceutas")
    @XmlElement(name = "farmaceuta")
    private List<Farmaceuta> farmaceutas;

    @XmlElementWrapper(name = "medicamentos")
    @XmlElement(name = "medicamento")
    private List<Medicamento> medicamentos;

    @XmlElementWrapper(name="lineas")
    @XmlElement(name="linea")
    private List<Linea> lineas;

    @XmlElementWrapper(name="recetas")
    @XmlElement(name="receta")
    private List<Receta> recetas;

    @XmlElement(name="admin")
    private Usuario admin;

    public Data(){
        medicos = new ArrayList<>();
        pacientes = new ArrayList<>();
        medicamentos = new ArrayList<>();
        farmaceutas = new ArrayList<>();
        lineas = new ArrayList<>();
        recetas = new ArrayList<>();
        admin = new Usuario();
    }

    public List<Paciente> getPacientes() {
        return pacientes;
    }
    public List<Medico> getMedicos() {
        return medicos;
    }
    public List<Farmaceuta> getFarmaceutas() {
        return farmaceutas;
    }
    public List<Medicamento> getMedicamentos() {
        return medicamentos;
    }
    public List<Linea> getLineas() {return lineas;}
    public List<Receta> getRecetas() {return recetas;}
    public Usuario getAdmin() {return admin;}
    public void setAdmin(Usuario admin) {this.admin = admin;}



}

