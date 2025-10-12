package recetas.data;

import recetas.logic.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao {
    Database db;

    public UsuarioDao() {
        db = Database.instance();
    }

    public void create(Usuario u) throws Exception {
        String sql = "INSERT INTO Usuario (id, nombre, clave, rol) VALUES (?,?,?,?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, u.getId());
        stm.setString(2, u.getNombre());
        stm.setString(3, u.getClave());
        stm.setString(4, u.getRol());

        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Error al crear el usuario");
        }
    }

    public Usuario read(String id) throws Exception {
        String sql = "SELECT * FROM Usuario u WHERE u.id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs = db.executeQuery(stm);

        if (rs.next()) {
            return from(rs, "u");
        } else {
            throw new Exception("Usuario no existe");
        }
    }

    public void update(Usuario u) throws Exception {
        String sql = "UPDATE Usuario SET nombre=?, clave=?, rol=? WHERE id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, u.getNombre());
        stm.setString(2, u.getClave());
        stm.setString(3, u.getRol());
        stm.setString(4, u.getId());

        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Usuario no existe");
        }
    }

    public void delete(Usuario u) throws Exception {
        String sql = "DELETE FROM Usuario WHERE id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, u.getId());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Usuario no existe");
        }
    }

    public List<Usuario> findByNombre(Usuario filtro) {
        List<Usuario> resultado = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Usuario u WHERE u.nombre LIKE ? ORDER BY u.nombre ASC";
            PreparedStatement stm = db.prepareStatement(sql);
            String nombre = (filtro.getNombre() != null) ? filtro.getNombre() : "";
            stm.setString(1, "%" + nombre + "%");
            ResultSet rs = db.executeQuery(stm);

            while (rs.next()) {
                resultado.add(from(rs, "u"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public List<Usuario> findAll() {
        List<Usuario> resultado = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Usuario u ORDER BY u.nombre ASC";
            PreparedStatement stm = db.prepareStatement(sql);
            ResultSet rs = db.executeQuery(stm);
            while (rs.next()) {
                resultado.add(from(rs, "u"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    // ------------ Mapper ------------
    private Usuario from(ResultSet rs, String alias) throws SQLException {
        String id     = rs.getString(alias + ".id");
        String nombre = rs.getString(alias + ".nombre");
        String clave  = rs.getString(alias + ".clave");
        String rol    = rs.getString(alias + ".rol");

        return new Usuario(nombre, id, clave, rol);
    }
}

