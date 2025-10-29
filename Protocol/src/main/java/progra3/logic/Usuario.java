package progra3.logic;

import java.io.Serializable;

public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String nombre;
    private String clave;
    private String rol;

    public Usuario() { this("", "", "", ""); }
    public Usuario(String id, String nombre, String clave, String rol) {
        this.id = id; this.nombre = nombre; this.clave = clave; this.rol = rol;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    @Override
    public String toString() { return nombre; }
}