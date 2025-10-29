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
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, u.getId());
            stm.setString(2, u.getNombre());
            stm.setString(3, u.getClave());
            stm.setString(4, u.getRol());

            int count = stm.executeUpdate();
            if (count == 0) {
                throw new Exception("Error al crear el usuario");
            }
        }
    }

    public Usuario read(String id) throws Exception {
        String sql = "SELECT * FROM Usuario u WHERE u.id = ?";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, id);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return from(rs);
                } else {
                    throw new Exception("Usuario no existe");
                }
            }
        }
    }

    public void update(Usuario u) throws Exception {
        String sql = "UPDATE Usuario SET nombre=?, clave=?, rol=? WHERE id=?";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, u.getNombre());
            stm.setString(2, u.getClave());
            stm.setString(3, u.getRol());
            stm.setString(4, u.getId());

            int count = stm.executeUpdate();
            if (count == 0) {
                throw new Exception("Usuario no existe");
            }
        }
    }

    public void delete(Usuario u) throws Exception {
        String sql = "DELETE FROM Usuario WHERE id = ?";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, u.getId());
            int count = stm.executeUpdate();
            if (count == 0) {
                throw new Exception("Usuario no existe");
            }
        }
    }

    public List<Usuario> findByNombre(Usuario filtro) {
        List<Usuario> resultado = new ArrayList<>();
        String sql = "SELECT * FROM Usuario u WHERE u.nombre LIKE ? ORDER BY u.nombre ASC";
        String nombre = (filtro != null && filtro.getNombre() != null) ? filtro.getNombre() : "";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, "%" + nombre + "%");
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    resultado.add(from(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public List<Usuario> findAll() {
        List<Usuario> resultado = new ArrayList<>();
        String sql = "SELECT * FROM Usuario u ORDER BY u.nombre ASC";
        try (PreparedStatement stm = db.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                resultado.add(from(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    // ------------ Mapper ------------
    private Usuario from(ResultSet rs) throws SQLException {
        // leer por nombre de columna real (sin alias + '.')
        String id = rs.getString("id");
        String nombre = rs.getString("nombre");
        String clave = rs.getString("clave");
        String rol = rs.getString("rol");

        // conservar constructor original (nombre, id, clave, rol)
        return new Usuario(nombre, id, clave, rol);
    }
}

