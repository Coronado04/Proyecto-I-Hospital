package progra3.data;

import progra3.logic.Paciente;
import progra3.logic.Receta;
import progra3.logic.Medico;

import java.sql.Date;
import java.time.LocalDate;
import java.sql.PreparedStatement;
import java.sql.*;
import java.util.*;
import java.util.ArrayList;
import java.util.List;

public class RecetaDao {
    Database db;

    public RecetaDao() {
        db = Database.instance();
    }

    public void create(Receta e, String idMedico, String idPaciente) throws Exception {
        String sql = "INSERT INTO Receta (medico, paciente, fechaConfeccion, fechaRetiro, estado) " +
                "VALUES (?,?,?,?,?)";
        try (PreparedStatement stm = db.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (idMedico != null && !idMedico.isEmpty()) stm.setString(1, idMedico); else stm.setNull(1, Types.VARCHAR);
            if (idPaciente != null && !idPaciente.isEmpty()) stm.setString(2, idPaciente); else stm.setNull(2, Types.VARCHAR);
            LocalDate fc = e.getFechaConfeccion();
            LocalDate fr = e.getFechaRetiro();
            if (fc != null) stm.setDate(3, Date.valueOf(fc)); else stm.setNull(3, Types.DATE);
            if (fr != null) stm.setDate(4, Date.valueOf(fr)); else stm.setNull(4, Types.DATE);

            stm.setString(5, (e.getEstado() != null) ? e.getEstado().name() : null);

            int affected = stm.executeUpdate();
            if (affected == 0) {
                throw new Exception("Error al crear la receta");
            }

            try (ResultSet gk = stm.getGeneratedKeys()) {
                if (gk != null && gk.next()) {
                    int numero = gk.getInt(1);
                    e.setIdReceta(String.valueOf(numero));
                } else {
                    throw new Exception("No se obtuvo la clave generada para la receta");
                }
            }
        }
    }

    public Receta read(String idReceta) throws Exception {
        String sql = "SELECT r.*, me.id AS me_id, me.nombre AS me_nombre, pa.id AS pa_id, pa.nombre AS pa_nombre " +
                "FROM Receta r " +
                "LEFT JOIN Medico me ON r.medico = me.id " +
                "LEFT JOIN Paciente pa ON r.paciente = pa.id " +
                "WHERE r.numero = ?";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setInt(1, Integer.parseInt(idReceta));
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    Receta r = from(rs);
                    try {
                        LineaDao lineaDao = new LineaDao();
                        r.setDetalles(lineaDao.findByReceta(r.getIdReceta()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return r;
                } else {
                    throw new Exception("Receta no existe");
                }
            }
        }
    }

    public void update(Receta r, String idReceta) throws Exception {
        String sql = "UPDATE Receta SET medico=?, paciente=?, fechaConfeccion=?, fechaRetiro=?, estado=? WHERE numero=?";
        try (PreparedStatement stm = db.prepareStatement(sql)) {

            if (r.getMedico() != null && r.getMedico().getId() != null && !r.getMedico().getId().isEmpty())
                stm.setString(1, r.getMedico().getId());
            else
                stm.setNull(1, Types.VARCHAR);

            if (r.getPaciente() != null && r.getPaciente().getId() != null && !r.getPaciente().getId().isEmpty())
                stm.setString(2, r.getPaciente().getId());
            else
                stm.setNull(2, Types.VARCHAR);

            LocalDate fc = r.getFechaConfeccion();
            LocalDate fr = r.getFechaRetiro();
            if (fc != null) stm.setDate(3, Date.valueOf(fc)); else stm.setNull(3, Types.DATE);
            if (fr != null) stm.setDate(4, Date.valueOf(fr)); else stm.setNull(4, Types.DATE);

            stm.setString(5, (r.getEstado() != null) ? r.getEstado().name() : null);
            stm.setInt(6, Integer.parseInt(idReceta));

            int count = stm.executeUpdate();
            if (count == 0) {
                throw new Exception("Receta no existe");
            }
        }
    }

    public void delete(String idReceta) throws Exception {
        String sql = "DELETE FROM Receta WHERE numero = ?";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setInt(1, Integer.parseInt(idReceta));
            int count = stm.executeUpdate();
            if (count == 0) {
                throw new Exception("Receta no existe");
            }
        }
    }

    public List<Receta> findByNombre(Receta filtro) {
        List<Receta> resultado = new ArrayList<>();
        try {
            String sqlBase = "SELECT r.*, me.nombre AS me_nombre, pa.nombre AS pa_nombre " +
                    "FROM Receta r " +
                    "LEFT JOIN Medico me ON r.medico = me.id " +
                    "LEFT JOIN Paciente pa ON r.paciente = pa.id";
            if (filtro != null && filtro.getPaciente() != null) {
                String pacienteId = filtro.getPaciente().getId();
                String pacienteNombre = filtro.getPaciente().getNombre();

                if (pacienteId != null && !pacienteId.trim().isEmpty()) {
                    String sql = sqlBase + " WHERE r.paciente = ? ORDER BY r.fechaConfeccion DESC";
                    try (PreparedStatement stm = db.prepareStatement(sql)) {
                        stm.setString(1, pacienteId.trim());
                        try (ResultSet rs = stm.executeQuery()) {
                            while (rs.next()) {
                                Receta r = from(rs);
                                try {
                                    LineaDao lineaDao = new LineaDao();
                                    r.setDetalles(lineaDao.findByReceta(r.getIdReceta()));
                                } catch (Exception ex) { ex.printStackTrace(); }
                                resultado.add(r);
                            }
                        }
                    }
                    return resultado;
                }
                if (pacienteNombre != null && !pacienteNombre.trim().isEmpty()) {
                    String sql = sqlBase + " WHERE pa.nombre LIKE ? ORDER BY r.fechaConfeccion DESC";
                    try (PreparedStatement stm = db.prepareStatement(sql)) {
                        stm.setString(1, "%" + pacienteNombre.trim() + "%");
                        try (ResultSet rs = stm.executeQuery()) {
                            while (rs.next()) {
                                Receta r = from(rs);
                                try {
                                    LineaDao lineaDao = new LineaDao();
                                    r.setDetalles(lineaDao.findByReceta(r.getIdReceta()));
                                } catch (Exception ex) { ex.printStackTrace(); }
                                resultado.add(r);
                            }
                        }
                    }
                    return resultado;
                }
            }
            String sql = sqlBase + " ORDER BY r.fechaConfeccion DESC";
            try (PreparedStatement stm = db.prepareStatement(sql);
                 ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    Receta r = from(rs);
                    try {
                        LineaDao lineaDao = new LineaDao();
                        r.setDetalles(lineaDao.findByReceta(r.getIdReceta()));
                    } catch (Exception ex) { ex.printStackTrace(); }
                    resultado.add(r);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    private Receta from(ResultSet rs) throws SQLException {
        Receta r = new Receta();

        int numero = rs.getInt("numero");
        r.setIdReceta(String.valueOf(numero));

        Date d1 = rs.getDate("fechaConfeccion");
        Date d2 = rs.getDate("fechaRetiro");
        if (d1 != null) r.setFechaConfeccion(((java.sql.Date) d1).toLocalDate());
        if (d2 != null) r.setFechaRetiro(((java.sql.Date) d2).toLocalDate());

        String est = rs.getString("estado");
        if (est != null) {
            try {
                r.setEstado(Receta.Estado.valueOf(est.trim().toUpperCase(java.util.Locale.ROOT)));
            } catch (IllegalArgumentException ignored) {}
        }

        Paciente p = new Paciente();
        try { p.setId(rs.getString("paciente")); } catch (Exception ignored) {}
        try { p.setNombre(rs.getString("pa_nombre")); } catch (Exception ignored) {}
        if (p.getId() != null || p.getNombre() != null) r.setPaciente(p);

        Medico m = new Medico();
        try { m.setId(rs.getString("medico")); } catch (Exception ignored) {}
        try { m.setNombre(rs.getString("me_nombre")); } catch (Exception ignored) {}
        if (m.getId() != null || m.getNombre() != null) r.setMedico(m);

        return r;
    }
}