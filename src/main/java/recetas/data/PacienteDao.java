package recetas.data;

import recetas.logic.Paciente;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PacienteDao {
    Database db;

    public PacienteDao() {
        db=Database.instance();
    }
    public void create(Paciente p) throws Exception {
        String sql="insert into paciente(id,nombre,fechaNacimiento,telefono)"+
                "values(?,?,?,?)";
        PreparedStatement stm=db.prepareStatement(sql);
        stm.setString(1,p.getId());
        stm.setString(2,p.getNombre());
        stm.setDate(3, java.sql.Date.valueOf(p.getFechaNacimiento()));
        stm.setString(4,p.getNumero());
        int count=db.executeUpdate(stm);
        if(count==0){
            throw new Exception("Paciente ya existe");
        }
    }

    public Paciente read(String id) throws Exception {
        String sql="select * from Paciente p";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1,id);
        ResultSet rs = db.executeQuery(stm);
        Paciente p;
        if(rs.next()){
            p=from(rs,"p");
            return p;
        }else{
            throw new Exception("Paciente no existe");
        }
    }
        public void update(Paciente p) throws Exception {
            String sql="update paciente set nombre=?, fechaNacimiento=?, numero=? where id=?";
            PreparedStatement stm=db.prepareStatement(sql);
            stm.setString(1,p.getNombre());
            stm.setString(2,p.getFechaNacimiento().toString());
            stm.setString(3,p.getNumero());
            stm.setString(4,p.getId());
            int count=db.executeUpdate(stm);
            if(count==0){
                throw new Exception("Paciente no existe");
            }
        }
        public void delete(Paciente p) throws Exception {
            String sql="delete from paciente where id=?";
            PreparedStatement stm=db.prepareStatement(sql);
            stm.setString(1,p.getId());
            int count=db.executeUpdate(stm);
            if(count==0){
                throw new Exception("Paciente no existe");
            }
        }
        public List<Paciente> findByNombre(Paciente filtro){
            List<Paciente> resultado=new ArrayList<Paciente>();
               try{
                     String sql="select * from paciente p where p.nombre like ?";
                     PreparedStatement stm=db.prepareStatement(sql);
                     stm.setString(1,"%"+filtro.getNombre()+"%");
                     ResultSet rs=db.executeQuery(stm);
                     Paciente p;
                     while(rs.next()){
                          p=from(rs,"p");
                          resultado.add(p);
                     }
               }catch(SQLException e){
               }
            return resultado;
        }

    private Paciente from(ResultSet rs, String alias) throws SQLException {

        try{
            Paciente p=new Paciente();
            p.setId(rs.getString(alias+".id"));
            p.setNombre(rs.getString(alias+".nombre"));
            p.setFechaNacimiento(rs.getDate(alias+".fechaNacimiento").toLocalDate());
            p.setNumero(rs.getString(alias+".numero"));
            return p;
        }catch(SQLException ex){
            return null;
        }
    }

}
