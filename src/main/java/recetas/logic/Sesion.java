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
        String sql =
                "SELECT id, nombre, clave, rol, 1 AS pri FROM Usuario WHERE id=? AND clave=? " +
                        "UNION ALL " +
                        "SELECT id, nombre, clave, 'medico' AS rol, 2 AS pri FROM Medico WHERE id=? AND clave=? " +
                        "UNION ALL " +
                        "SELECT id, nombre, clave, 'farmaceuta' AS rol, 3 AS pri FROM Farmaceuta WHERE id=? AND clave=? " +
                        // Usa una de estas según tu motor de BD:
                        // "ORDER BY pri LIMIT 1";                   // MySQL / PostgreSQL
                        // "ORDER BY pri OFFSET 0 ROWS FETCH FIRST 1 ROWS ONLY"; // Oracle / DB2 (12c+)
                        "ORDER BY pri LIMIT 1"; // ← Ajusta esta línea a tu motor

        PreparedStatement stm = db.prepareStatement(sql);
        int i = 1;
        stm.setString(i++, id);
        stm.setString(i++, clave);
        stm.setString(i++, id);
        stm.setString(i++, clave);
        stm.setString(i++, id);
        stm.setString(i++, clave);

        ResultSet rs = db.executeQuery(stm);

        if (rs.next()) {
            Usuario u = new Usuario();
            u.setId(rs.getString("id"));
            u.setNombre(rs.getString("nombre"));
            u.setClave(rs.getString("clave")); // Idealmente NO devolver la clave
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
        String[] tablas = { "Usuario", "Medico", "Farmaceuta" }; // misma prioridad que en login
        int updated = 0;

        for (String tabla : tablas) {
            String sql = "UPDATE " + tabla + " SET clave=? WHERE id=?";
            try (PreparedStatement stm = db.prepareStatement(sql)) {
                stm.setString(1, nuevaClave); // <-- sin comillas en el SQL
                stm.setString(2, id);
                updated = db.executeUpdate(stm);
            }
            if (updated > 0) break; // ya lo encontramos y actualizamos
        }

        if (updated == 0) {
            throw new Exception("No se pudo actualizar la clave (no existe en Usuario/Medico/Farmaceuta).");
        }
    }

}
