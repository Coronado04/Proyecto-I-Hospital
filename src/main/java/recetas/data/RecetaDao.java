package recetas.data;

import recetas.logic.Medico;
import recetas.logic.Paciente;
import recetas.logic.Receta;
import java.time.LocalDate;
import java.sql.PreparedStatement;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class RecetaDao {
    Database db;


    public RecetaDao() {
        db = Database.instance();
    }


    public void create(Receta e, String idMedico, String idPaciente) throws Exception {
        String sql = "INSERT INTO Receta (idReceta, medico, paciente, fechaConfeccion, fechaRetiro, estado) " +
                "VALUES (?,?,?,?,?,?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getIdReceta());
        stm.setString(2, idMedico);
        stm.setString(3, idPaciente);
        LocalDate fc = e.getFechaConfeccion();
        LocalDate fr = e.getFechaRetiro();
        if (fc != null) stm.setDate(4, java.sql.Date.valueOf(fc)); else stm.setNull(4, Types.DATE);
        if (fr != null) stm.setDate(5, java.sql.Date.valueOf(fr)); else stm.setNull(5, Types.DATE);
        stm.setString(6, (e.getEstado() != null) ? e.getEstado().name() : null);


        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Error al crear la receta");
        }
    }



    public Receta read(String idReceta) throws Exception {
        String sql = "SELECT * FROM Receta r " +
                "INNER JOIN Medico me ON r.medico = me.cedula " +
                "INNER JOIN Paciente pa ON r.paciente = pa.cedula " +
                "WHERE r.idReceta = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, idReceta);
        ResultSet rs = db.executeQuery(stm);


        if (rs.next()) {
            return from(rs, "r", "me", "pa");
        } else {
            throw new Exception("Receta no existe");
        }
    }


    public void update(Receta r, String idReceta) throws Exception {
        String sql = "UPDATE Receta SET medico=?, paciente=?, fechaConfeccion=?, fechaRetiro=?, estado=? WHERE idReceta=?";
        PreparedStatement stm = db.prepareStatement(sql);

        stm.setString(1, (r.getMedico() != null ? "" : null)); // placeholder si no las manejas aquí
        stm.setString(2, (r.getPaciente() != null ? "" : null)); // placeholder
        LocalDate fc = r.getFechaConfeccion();
        LocalDate fr = r.getFechaRetiro();
        if (fc != null) stm.setDate(3, java.sql.Date.valueOf(fc)); else stm.setNull(3, Types.DATE);
        if (fr != null) stm.setDate(4, java.sql.Date.valueOf(fr)); else stm.setNull(4, Types.DATE);

        stm.setString(5, (r.getEstado() != null) ? r.getEstado().name() : null);
        stm.setString(6, idReceta);


        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Receta no existe");
        }
    }


    public void delete(String idReceta) throws Exception {
        String sql = "DELETE FROM Receta WHERE idReceta = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, idReceta);
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Receta no existe");
        }
    }


    public List<Receta> findByNombre(Receta filtro) {
        List<Receta> resultado = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Receta r " +
                    "INNER JOIN Medico me ON r.medico = me.cedula " +
                    "INNER JOIN Paciente pa ON r.paciente = pa.cedula " +
                    "WHERE pa.nombre LIKE ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, "%" + filtro.getPaciente().getNombre() + "%");
            ResultSet rs = db.executeQuery(stm);


            while (rs.next()) {
                resultado.add(from(rs, "r", "me", "pa"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }


    private Receta from(ResultSet rs, String aliasRec, String aliasMed, String aliasPac) throws SQLException {
        Receta r = new Receta();
        r.setIdReceta(rs.getString(aliasRec + ".idReceta"));


        Date d1 = rs.getDate(aliasRec + ".fechaConfeccion");
        Date d2 = rs.getDate(aliasRec + ".fechaRetiro");
        if (d1 != null) r.setFechaConfeccion(((java.sql.Date) d1).toLocalDate());
        if (d2 != null) r.setFechaRetiro(((java.sql.Date) d2).toLocalDate());


        String est = rs.getString(aliasRec + ".estado");
        if (est != null) {
            try {
                r.setEstado(Receta.Estado.valueOf(est.trim().toUpperCase(java.util.Locale.ROOT)));
            } catch (IllegalArgumentException ignored) {}
        }



        Paciente p = new Paciente();
        try { p.setNombre(rs.getString(aliasPac + ".nombre")); } catch (Exception ignored) {}
        r.setPaciente(p);


        Medico m = new Medico();
        try { m.setNombre(rs.getString(aliasMed + ".nombre")); } catch (Exception ignored) {}
        r.setMedico(m);


        return r;
    }



}
