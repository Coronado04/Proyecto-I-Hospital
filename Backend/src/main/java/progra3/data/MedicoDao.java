package progra3.data;

import progra3.logic.Medico;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicoDao {
    Database db;
    public MedicoDao(){
        db = Database.instance();
    }

    public void create(Medico m) throws SQLException {
        String sql = "INSERT INTO Medico(id, clave, nombre, especialidad) VALUES (?,?,?,?)";
        try (PreparedStatement ps = db.prepareStatement(sql)) {
            ps.setString(1, m.getId());
            ps.setString(2, m.getClave());
            ps.setString(3, m.getNombre());
            ps.setString(4, m.getEspecialidad());
            int count = ps.executeUpdate();
            if(count==0){
                throw new SQLException("Medico ya existe");
            }
        }
    }

    public Medico read(String id) throws Exception{
        String sql = "SELECT id, nombre, especialidad, clave FROM Medico WHERE id = ?";
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, id);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return from(rs);
                } else {
                    throw new Exception ("Medico no Existe");
                }
            }
        }
    }

    public void update(Medico m)throws Exception{
        String sql="UPDATE Medico SET clave=?, nombre=?, especialidad=? WHERE id=?";
        try (PreparedStatement ps = db.prepareStatement(sql)) {
            ps.setString(1, m.getClave());
            ps.setString(2, m.getNombre());
            ps.setString(3, m.getEspecialidad());
            ps.setString(4, m.getId());
            int count = ps.executeUpdate();
            if(count==0){
                throw new Exception("Medico no existe");
            }
        }
    }
    public void delete(Medico m)throws Exception{
        String sql="DELETE FROM Medico WHERE id=?";
        try (PreparedStatement stm=db.prepareStatement(sql)) {
            stm.setString(1, m.getId());
            int count = stm.executeUpdate();
            if(count==0){
                throw new Exception("Medico no existe");
            }
        }
    }

    /**
     * Si filtro.getId() está presente, devuelve el registro con ese id (lista con 0/1 elementos).
     * Si no, hace búsqueda por nombre (LIKE).
     */
    public List<Medico> findByNombre(Medico filtro){
        List<Medico> resultado= new ArrayList<>();

        // Si se busca por id exacto
        if (filtro != null && filtro.getId() != null && !filtro.getId().trim().isEmpty()) {
            try {
                Medico m = read(filtro.getId().trim());
                resultado.add(m);
            } catch (Exception ex) {
                // no existe -> devolver lista vacía
            }
            return resultado;
        }

        // Búsqueda por nombre (LIKE) por compatibilidad
        String sql = "SELECT id, nombre, especialidad, clave FROM Medico WHERE nombre LIKE ? ORDER BY nombre";
        String nombreFiltro = "";
        if (filtro != null && filtro.getNombre() != null) {
            nombreFiltro = filtro.getNombre().trim();
        }
        try (PreparedStatement stm = db.prepareStatement(sql)) {
            stm.setString(1, "%" + nombreFiltro + "%");
            try (ResultSet rs = stm.executeQuery()) {
                while(rs.next()){
                    resultado.add(from(rs));
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return resultado;
    }

    private Medico from(ResultSet rs) throws SQLException {
        Medico m = new Medico();
        m.setId(rs.getString("id"));
        m.setNombre(rs.getString("nombre"));
        m.setEspecialidad(rs.getString("especialidad"));
        m.setClave(rs.getString("clave"));
        return m;
    }
}