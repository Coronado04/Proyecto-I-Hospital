package progra3.pos.data;

import recetas.logic.Medicamento;

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
            // codigo en BD es INT: validar que el valor sea numérico
            try {
                int codigo = Integer.parseInt(m.getCodigo().trim());
                stm.setInt(1, codigo);
            } catch (NumberFormatException ex) {
                throw new Exception("Código de medicamento inválido: debe ser un número entero");
            }
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
            try {
                stm.setInt(1, Integer.parseInt(codigo.trim()));
            } catch (NumberFormatException ex) {
                throw new Exception("Código inválido");
            }
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
            try {
                stm.setInt(3, Integer.parseInt(m.getCodigo().trim()));
            } catch (NumberFormatException ex) {
                throw new Exception("Código inválido");
            }
            int count = stm.executeUpdate();
            if (count == 0) {
                throw new Exception("Medicamento no existe");
            }
        }
    }

    public void delete(Medicamento m) throws Exception {
        String sql = "DELETE FROM Medicamento WHERE codigo=?";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            try {
                stm.setInt(1, Integer.parseInt(m.getCodigo().trim()));
            } catch (NumberFormatException ex) {
                throw new Exception("Código inválido");
            }
            int count = stm.executeUpdate();
            if (count == 0) {
                throw new Exception("Medicamento no existe");
            }
        }
    }

    public List<Medicamento> findByNombre(Medicamento filtro) {
        List<Medicamento> resultado = new ArrayList<>();
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

    // mapper sin alias (usar los nombres reales de columnas)
    public Medicamento from(ResultSet rs) {
        try {
            Medicamento m = new Medicamento();
            // obtener codigo como string (mysql permite getString sobre columna INT)
            String codigo = rs.getString("codigo");
            m.setCodigo(codigo != null ? codigo : "");
            m.setNombre(rs.getString("nombre"));
            m.setPresentacion(rs.getString("presentacion"));
            return m;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
