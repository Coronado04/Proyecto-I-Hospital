package recetas.data;

import recetas.logic.Medico;

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
    public void create(Medico m)throws SQLException {
        String sql="insert into medico(id, clave, nombre, especialidad) values(?,?,?,?)";
        PreparedStatement ps = db.prepareStatement(sql);
        ps.setString(1, m.getId());
        ps.setString(2, m.getClave());
        ps.setString(3, m.getNombre());
        ps.setString(4, m.getEspecialidad());
        int count = db.executeUpdate(ps);
            if(count==0){
                throw new SQLException("Medico ya existe");
            }
    }
    public Medico read(String id)throws Exception{
        String sql="select * from Medico m where m.id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs =  db.executeQuery(stm);
        Medico m;

        if (rs.next()) {
            m= from(rs,"m");
            return m;
        }
        else{
            throw new Exception ("Medico no Existe");
        }
    }

    public void update(Medico m)throws Exception{
        String sql="update Medico m set m.clave=?, m.nombre=?, m.especialidad=? where m.id=?";
        PreparedStatement ps = db.prepareStatement(sql);
        ps.setString(1, m.getClave());
        ps.setString(2, m.getNombre());
        ps.setString(3, m.getEspecialidad());
        ps.setString(4, m.getId());
        int count = db.executeUpdate(ps);
        if(count==0){
            throw new Exception("Medico no existe");
        }
    }
    public void delete(Medico m)throws Exception{
        String sql="delete from Medico where id=?";
        PreparedStatement stm=db.prepareStatement(sql);
        stm.setString(1, m.getId());
        int count = db.executeUpdate(stm);
        if(count==0){
            throw new Exception("Medico no existe");
        }
    }
    public List<Medico> findByNombre(Medico filtro){
        List<Medico> resultado= new ArrayList<Medico>();
        try{
            String sql="select * from Medico m where m.nombre=?";
            PreparedStatement stm=db.prepareStatement(sql);
            stm.setString(1, filtro.getNombre());
            ResultSet rs = db.executeQuery(stm);
            Medico m;
            while(rs.next()){
                m= from(rs,"m");
                resultado.add(m);
            }
        }catch(SQLException e){}
        return resultado;
    }
    private Medico from(ResultSet rs,String alias) throws SQLException {
        Medico m = new Medico();
        m.setId(rs.getString("id"));
        m.setNombre(rs.getString("nombre"));
        m.setEspecialidad(rs.getString("especialidad"));
        m.setClave(rs.getString("clave"));
        return m;
    }
}
