package recetas.data;

import recetas.logic.Medicamento;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoDao {
    Database db;

    public MedicamentoDao() {
        db= Database.instance();
    }

    public void create(Medicamento m) throws Exception{
        String sql="insert into Medicamento (codigo, nombre, presentacion) values(?,?,?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, m.getCodigo());
        stm.setString(2, m.getNombre());
        stm.setString(3, m.getPresentacion());
        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("Medicamento ya existe");
        }
    }

    public Medicamento read(String codigo) throws Exception{
        String sql="select * from Medicamento m where m.id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, codigo);
        ResultSet rs =  db.executeQuery(stm);
        Medicamento m;
        if (rs.next()) {
            m= from(rs,"m");
            return m;
        }
        else{
            throw new Exception ("Medicamento no Existe");
        }
    }

    public void update(Medicamento m) throws Exception{
        String sql="update Medicamento set nombre=?,presentacion=? where codigo=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, m.getNombre());
        stm.setString(2, m.getPresentacion());
        stm.setString(3, m.getCodigo());
        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("Medicamento no existe");
        }
    }

    public void delete(Medicamento m) throws Exception{
        String sql="delete from Medicamento where codigo=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, m.getCodigo());
        int count=db.executeUpdate(stm);
        if (count==0){
            throw new Exception("Medicamento no existe");
        }
    }

    public List<Medicamento> findByNombre(Medicamento filtro){
        List<Medicamento> resultado = new ArrayList<Medicamento>();
        try {
            String sql="select * from Medicamento m where nombre like ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, "%"+filtro.getNombre()+"%");
            ResultSet rs =  db.executeQuery(stm);
            Medicamento m;
            while (rs.next()) {
                m= from(rs,"m");
                resultado.add(m);
            }
        } catch (SQLException ex) {  }
        return resultado;
    }

    private Medicamento from(ResultSet rs, String alias){
        try {
            Medicamento m= new Medicamento();
            m.setCodigo(rs.getString(alias + ".codigo"));
            m.setNombre(rs.getString(alias + ".nombre"));
            m.setPresentacion(rs.getString(alias + ".presentacion"));
            return m;
        } catch (SQLException ex) {
            return null;
        }
    }

}
