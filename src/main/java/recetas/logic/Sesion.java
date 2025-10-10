package recetas.logic;

import recetas.data.Data;
import recetas.data.XmlPersister;

public class Sesion {
    private static Usuario usuario;

    public static void setUsuario(Usuario usuario) {
        Sesion.usuario = usuario;
    }

    public static Usuario getUsuario() {
        return usuario;
    }

    public static boolean isLoggedIn() {
        return usuario != null;
    }


    public static Usuario login(String id, String clave) throws Exception {

        Data data = XmlPersister.instance().load();

        Usuario admin = data.getAdmin();

            if (admin.getId().equals(id) && admin.getClave().equals(clave)) {
                usuario = new Usuario(admin.getNombre(), admin.getId(), admin.getClave(), "admin");
                return usuario;
            }


        // Buscar en listas
        for (Medico m : data.getMedicos()) {
            if (m.getId().trim().equalsIgnoreCase(id.trim()) &&
                    m.getClave().trim().equals(clave.trim())) {
                usuario = new Usuario(m.getNombre(), m.getId(), m.getClave(), "medico");
                return usuario;
            }
        }

        for (Farmaceuta f : data.getFarmaceutas()) {
            if (f.getId().equals(id) && f.getClave().equals(clave)) {
                usuario = new Usuario(f.getNombre(), f.getId(), f.getClave(), "farmaceuta");
                return usuario;
            }
        }

        throw new Exception("Usuario o clave incorrectos");
    }
}