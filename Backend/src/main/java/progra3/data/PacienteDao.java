package progra3.data;

import progra3.logic.Paciente;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class PacienteDao {
    Database db;

    public PacienteDao() {
        db = Database.instance();
    }

    public void create(Paciente p) throws Exception {
        String sql = "INSERT INTO Paciente(id, nombre, fechaNacimiento, telefono) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, p.getId());
            stm.setString(2, p.getNombre());
            if (p.getFechaNacimiento() != null) {
                stm.setDate(3, java.sql.Date.valueOf(p.getFechaNacimiento()));
            } else {
                stm.setNull(3, Types.DATE);
            }
            stm.setString(4, p.getNumero()); // mapa al campo telefono en BD
            int count = stm.executeUpdate();
            if (count == 0) {
                throw new Exception("Paciente ya existe");
            }
        }
    }

    public Paciente read(String id) throws Exception {
        String sql = "SELECT p.id, p.nombre, p.fechaNacimiento, p.telefono FROM Paciente p WHERE p.id = ?";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, id);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return from(rs);
                } else {
                    throw new Exception("Paciente no existe");
                }
            }
        }
    }

    public void update(Paciente p) throws Exception {
        String sql = "UPDATE Paciente SET nombre=?, fechaNacimiento=?, telefono=? WHERE id=?";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, p.getNombre());
            if (p.getFechaNacimiento() != null) {
                stm.setDate(2, java.sql.Date.valueOf(p.getFechaNacimiento()));
            } else {
                stm.setNull(2, Types.DATE);
            }
            stm.setString(3, p.getNumero()); // telefono en BD
            stm.setString(4, p.getId());
            int count = stm.executeUpdate();
            if (count == 0) {
                throw new Exception("Paciente no existe");
            }
        }
    }

    public void delete(Paciente p) throws Exception {
        String sql = "DELETE FROM Paciente WHERE id=?";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, p.getId());
            int count = stm.executeUpdate();
            if (count == 0) {
                throw new Exception("Paciente no existe");
            }
        }
    }

    /**
     * Si filtro.getId() está presente devuelve ese paciente (lista 0/1).
     * Si no, hace búsqueda por nombre LIKE.
     */
    public List<Paciente> findByNombre(Paciente filtro) {
        List<Paciente> resultado = new ArrayList<>();

        if (filtro != null && filtro.getId() != null && !filtro.getId().trim().isEmpty()) {
            try {
                Paciente p = read(filtro.getId().trim());
                resultado.add(p);
            } catch (Exception ex) {
                // no existe -> lista vacía
            }
            return resultado;
        }

        String sql = "SELECT p.id, p.nombre, p.fechaNacimiento, p.telefono FROM Paciente p WHERE p.nombre LIKE ? ORDER BY p.nombre";
        String nombreFiltro = "";
        if (filtro != null && filtro.getNombre() != null) {
            nombreFiltro = filtro.getNombre().trim();
        }
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, "%" + nombreFiltro + "%");
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    Paciente p = from(rs);
                    if (p != null) resultado.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    private Paciente from(ResultSet rs) {
        try {
            Paciente p = new Paciente();
            p.setId(rs.getString("id"));
            p.setNombre(rs.getString("nombre"));
            java.sql.Date sqlDate = rs.getDate("fechaNacimiento");
            if (sqlDate != null) p.setFechaNacimiento(sqlDate.toLocalDate());
            p.setNumero(rs.getString("telefono")); // lectura desde columna telefono
            return p;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}