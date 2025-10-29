package progra3.pos.data;

import recetas.logic.Farmaceuta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FarmaceutaDao {
    Database db;

    public FarmaceutaDao(){
        db = Database.instance();
    }

    public void create(Farmaceuta f) throws Exception {
        String sql = "INSERT INTO Farmaceuta (id, clave, nombre) VALUES (?,?,?)";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, f.getId());
            stm.setString(2, f.getClave());
            stm.setString(3, f.getNombre());
            int count = stm.executeUpdate();
            if (count == 0) {
                throw new Exception("Farmaceuta ya existe");
            }
        }
    }

    public Farmaceuta read(String id) throws Exception {
        String sql = "SELECT id, nombre, clave FROM Farmaceuta WHERE id = ?";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, id);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return from(rs);
                } else {
                    throw new Exception("Farmaceuta no existe");
                }
            }
        }
    }

    public void update(Farmaceuta f) throws Exception {
        String sql = "UPDATE Farmaceuta SET clave=?, nombre=? WHERE id=?";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, f.getClave());
            stm.setString(2, f.getNombre());
            stm.setString(3, f.getId());
            int count = stm.executeUpdate();
            if (count == 0) {
                throw new Exception("Farmaceuta no existe");
            }
        }
    }

    public void delete(Farmaceuta f) throws Exception {
        String sql = "DELETE FROM Farmaceuta WHERE id=?";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, f.getId());
            int count = stm.executeUpdate();
            if (count == 0) {
                throw new Exception("Farmaceuta no existe");
            }
        }
    }

    public List<Farmaceuta> findByNombre(Farmaceuta filtro) {
        List<Farmaceuta> resultado = new ArrayList<>();
        String nombreFiltro = (filtro != null && filtro.getNombre() != null) ? filtro.getNombre().trim() : "";
        String sql = "SELECT id, nombre, clave FROM Farmaceuta WHERE nombre LIKE ? ORDER BY nombre";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, "%" + nombreFiltro + "%");
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    resultado.add(from(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return resultado;
    }

    private Farmaceuta from(ResultSet rs) throws SQLException {
        Farmaceuta f = new Farmaceuta();
        f.setId(rs.getString("id"));
        f.setNombre(rs.getString("nombre"));
        f.setClave(rs.getString("clave"));
        return f;
    }
}
