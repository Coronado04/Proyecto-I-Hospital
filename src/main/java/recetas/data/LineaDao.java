package recetas.data;

import recetas.logic.Linea;
import recetas.logic.Medicamento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LineaDao {
    Database db;

    public LineaDao() {
        db = Database.instance();
    }

    public void create(Linea l, String idReceta) throws Exception {
        String sql = "INSERT INTO Linea (receta, medicamento, cantidad, indicaciones, duracionDias) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, idReceta);
            stm.setString(2, l.getMedicamento() != null ? l.getMedicamento().getCodigo() : null);
            stm.setInt(3, l.getCantidad());
            stm.setString(4, l.getIndicaciones());
            stm.setInt(5, l.getDuracionDias());

            int count = stm.executeUpdate();
            if (count == 0) {
                throw new Exception("No se pudo crear la línea.");
            }
        }
    }

    public Linea read(int numero) throws Exception {
        String sql = "SELECT l.numero, l.cantidad, l.indicaciones, l.duracionDias, " +
                "m.codigo AS med_codigo, m.nombre AS med_nombre, m.presentacion AS med_presentacion " +
                "FROM Linea l " +
                "LEFT JOIN Medicamento m ON l.medicamento = m.codigo " +
                "WHERE l.numero = ?";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setInt(1, numero);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return from(rs);
                } else {
                    throw new Exception("Línea no existe");
                }
            }
        }
    }

    public void update(Linea l, int numero) throws Exception {
        String sql = "UPDATE Linea SET cantidad=?, indicaciones=?, duracionDias=?, medicamento=? WHERE numero=?";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setInt(1, l.getCantidad());
            stm.setString(2, l.getIndicaciones());
            stm.setInt(3, l.getDuracionDias());
            stm.setString(4, l.getMedicamento() != null ? l.getMedicamento().getCodigo() : null);
            stm.setInt(5, numero);

            int count = stm.executeUpdate();
            if (count == 0) {
                throw new Exception("Línea no existe");
            }
        }
    }

    public void delete(int numero) throws Exception {
        String sql = "DELETE FROM Linea WHERE numero = ?";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setInt(1, numero);
            int count = stm.executeUpdate();
            if (count == 0) {
                throw new Exception("Línea no existe");
            }
        }
    }

    public List<Linea> findByNombre(Linea filtro) {
        List<Linea> resultado = new ArrayList<>();

        String nombreFiltro = "";
        if (filtro != null && filtro.getMedicamento() != null && filtro.getMedicamento().getNombre() != null) {
            nombreFiltro = filtro.getMedicamento().getNombre().trim();
        }

        String sql = "SELECT l.numero, l.cantidad, l.indicaciones, l.duracionDias, " +
                "m.codigo AS med_codigo, m.nombre AS med_nombre, m.presentacion AS med_presentacion " +
                "FROM Linea l " +
                "LEFT JOIN Medicamento m ON l.medicamento = m.codigo " +
                "WHERE m.nombre LIKE ? " +
                "ORDER BY l.numero";

        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, "%" + nombreFiltro + "%");
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    Linea linea = from(rs);
                    if (linea != null) resultado.add(linea);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultado;
    }

    private Linea from(ResultSet rs) throws SQLException {
        Linea l = new Linea();
        Medicamento m = new Medicamento();

        // Llenar medicamento (puede quedar con campos null si no hay medicamento)
        m.setCodigo(rs.getString("med_codigo"));
        m.setNombre(rs.getString("med_nombre"));
        m.setPresentacion(rs.getString("med_presentacion"));

        // Llenar linea
        try {
            l.setNumero(rs.getInt("numero"));
        } catch (SQLException ignored) { }
        try {
            l.setCantidad(rs.getInt("cantidad"));
        } catch (SQLException ignored) { }
        try {
            l.setIndicaciones(rs.getString("indicaciones"));
        } catch (SQLException ignored) { }
        try {
            l.setDuracionDias(rs.getInt("duracionDias"));
        } catch (SQLException ignored) { }

        l.setMedicamento(m);
        return l;
    }
}