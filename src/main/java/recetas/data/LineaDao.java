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
        String sql = "INSERT INTO Linea (cantidad, indicaciones, duracionDias, medicamento, receta) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setInt(1, l.getCantidad());
        stm.setString(2, l.getIndicaciones());
        stm.setInt(3, l.getDuracionDias());
        stm.setString(4, l.getMedicamento().getCodigo());
        stm.setString(5, idReceta);

        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("No se pudo crear la línea.");
        }
    }


    public Linea read(int numero) throws Exception {
        String sql = "SELECT * FROM Linea l " +
                "INNER JOIN Medicamento m ON l.medicamento = m.codigo " +
                "WHERE l.numero = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setInt(1, numero);
        ResultSet rs = db.executeQuery(stm);

        if (rs.next()) {
            return from(rs, "l", "m");
        } else {
            throw new Exception("Línea no existe");
        }
    }

    public void update(Linea l, int numero) throws Exception {
        String sql = "UPDATE Linea SET cantidad=?, indicaciones=?, duracionDias=?, medicamento=? WHERE numero=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setInt(1, l.getCantidad());
        stm.setString(2, l.getIndicaciones());
        stm.setInt(3, l.getDuracionDias());
        stm.setString(4, l.getMedicamento().getCodigo());
        stm.setInt(5, numero);

        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Línea no existe");
        }
    }

    public void delete(int numero) throws Exception {
        String sql = "DELETE FROM Linea WHERE numero = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setInt(1, numero);
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Línea no existe");
        }
    }

    public List<Linea> findByNombre(Linea filtro) {
        List<Linea> resultado = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Linea l " +
                    "INNER JOIN Medicamento m ON l.medicamento = m.codigo " +
                    "WHERE m.nombre LIKE ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, "%" + filtro.getMedicamento().getNombre() + "%");
            ResultSet rs = db.executeQuery(stm);

            while (rs.next()) {
                resultado.add(from(rs, "l", "m"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    private Linea from(ResultSet rs, String aliasLinea, String aliasMed) throws SQLException {
        Linea l = new Linea();
        Medicamento m = new Medicamento();

        m.setCodigo(rs.getString(aliasMed + ".codigo"));
        m.setNombre(rs.getString(aliasMed + ".nombre"));
        m.setPresentacion(rs.getString(aliasMed + ".presentacion"));

        l.setMedicamento(m);
        l.setCantidad(rs.getInt(aliasLinea + ".cantidad"));
        l.setIndicaciones(rs.getString(aliasLinea + ".indicaciones"));
        l.setDuracionDias(rs.getInt(aliasLinea + ".duracionDias"));
        return l;
    }
}
