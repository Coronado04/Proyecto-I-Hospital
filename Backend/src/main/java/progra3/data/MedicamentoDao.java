package progra3.data;

import progra3.logic.Medicamento;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoDao {
    Database db;

    public MedicamentoDao() {
        db = Database.instance();
    }

    public void create(Medicamento m) throws Exception {
        String sql = "INSERT INTO Medicamento (codigo, nombre, presentacion) VALUES (?,?,?)";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, m.getCodigo() != null ? m.getCodigo().trim() : null);
            stm.setString(2, m.getNombre());
            stm.setString(3, m.getPresentacion());
            int count = stm.executeUpdate();
            if (count == 0) {
                throw new Exception("Medicamento ya existe");
            }
        }
    }

    public Medicamento read(String codigo) throws Exception {
        String sql = "SELECT codigo, nombre, presentacion FROM Medicamento WHERE codigo = ?";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, codigo != null ? codigo.trim() : null);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return from(rs);
                } else {
                    throw new Exception("Medicamento no Existe");
                }
            }
        }
    }

    public void update(Medicamento m) throws Exception {
        String sql = "UPDATE Medicamento SET nombre=?, presentacion=? WHERE codigo=?";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, m.getNombre());
            stm.setString(2, m.getPresentacion());
            stm.setString(3, m.getCodigo() != null ? m.getCodigo().trim() : null);
            int count = stm.executeUpdate();
            if (count == 0) {
                throw new Exception("Medicamento no existe");
            }
        }
    }

    public void delete(Medicamento m) throws Exception {
        String sql = "DELETE FROM Medicamento WHERE codigo=?";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, m.getCodigo() != null ? m.getCodigo().trim() : null);
            int count = stm.executeUpdate();
            if (count == 0) {
                throw new Exception("Medicamento no existe");
            }
        }
    }

    public List<Medicamento> findByNombre(Medicamento filtro) {
        List<Medicamento> resultado = new ArrayList<>();
        if (filtro != null && filtro.getCodigo() != null && !filtro.getCodigo().trim().isEmpty()) {
            try {
                Medicamento m = read(filtro.getCodigo().trim());
                resultado.add(m);
            } catch (Exception ex) {
            }
            return resultado;
        }

        String nombreFiltro = "";
        if (filtro != null && filtro.getNombre() != null) {
            nombreFiltro = filtro.getNombre().trim();
        }
        String sql = "SELECT codigo, nombre, presentacion FROM Medicamento WHERE nombre LIKE ? ORDER BY nombre";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, "%" + nombreFiltro + "%");
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    Medicamento m = from(rs);
                    if (m != null) resultado.add(m);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return resultado;
    }

    public Medicamento from(ResultSet rs) {
        try {
            Medicamento m = new Medicamento();
            m.setCodigo(rs.getString("codigo"));
            m.setNombre(rs.getString("nombre"));
            m.setPresentacion(rs.getString("presentacion"));
            return m;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}