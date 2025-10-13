package recetas.logic;

import recetas.data.Database;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Sesion {
    private static Usuario usuario;
    private static Database db = Database.instance();

    public static void setUsuario(Usuario usuario) {
        Sesion.usuario = usuario;
    }

    public static Usuario getUsuario() {
        return usuario;
    }

    public static boolean isLoggedIn() {
        return usuario != null;
    }

    // ==========================================================
    // LOGIN DESDE BASE DE DATOS
    // ==========================================================
    public static Usuario login(String id, String clave) throws Exception {
        String sql = "SELECT * FROM usuario WHERE id=? AND clave=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        stm.setString(2, clave);

        ResultSet rs = db.executeQuery(stm);

        if (rs.next()) {
            Usuario u = new Usuario();
            u.setId(rs.getString("id"));
            u.setNombre(rs.getString("nombre"));
            u.setClave(rs.getString("clave"));
            u.setRol(rs.getString("rol"));

            usuario = u;
            return u;
        } else {
            throw new Exception("Usuario o clave incorrectos");
        }
    }

    // ==========================================================
    // CAMBIAR CLAVE EN LA BASE DE DATOS
    // ==========================================================
    public static void actualizarClave(String id, String nuevaClave) throws Exception {
        String sql = "UPDATE usuario SET clave=? WHERE id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, nuevaClave);
        stm.setString(2, id);

        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("No se pudo actualizar la clave (usuario no encontrado).");
        }
    }
}
