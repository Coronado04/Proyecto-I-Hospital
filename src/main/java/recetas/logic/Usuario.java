package recetas.logic;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlID;

@XmlAccessorType(XmlAccessType.FIELD)
public class Usuario {
    @XmlID
    protected String id;
    protected String nombre;
    protected String clave;
    protected String rol;

    public Usuario(){}

    public Usuario(String nombre, String id, String clave, String rol) {
        this.nombre = nombre;
        this.id = id;
        this.clave = clave;
        this.rol = rol;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }

    public String getRol() { return rol; }

    @Override
    public String toString(){
        return String.format("[%s] %s (ID: %s)", rol, nombre, id);
    }
}


