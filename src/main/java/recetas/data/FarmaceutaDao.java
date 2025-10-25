    package recetas.data;

    import recetas.logic.Farmaceuta;

    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.util.ArrayList;
    import java.util.List;


    public class FarmaceutaDao {
        Database db;

        public FarmaceutaDao(){
            db=Database.instance();
        }

        public void create(Farmaceuta f) throws Exception {
            String sql="insert into Farmaceuta (id,clave,nombre) values (?,?,?)";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1,f.getId());
            stm.setString(2,f.getClave());
            stm.setString(3,f.getNombre());
            int count=db.executeUpdate(stm);
            if(count==0){
                throw new Exception("Farmaceuta ya existe");
            }
        }

        public Farmaceuta read(String id) throws Exception{
            String sql="select * from Farmaceuta where id=?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, id);
            ResultSet rs =  db.executeQuery(stm);
            Farmaceuta f;
            if (rs.next()) {
                f= from(rs);
                return f;
            }
            else{
                throw new Exception ("Farmaceuta no Existe");
            }
        }

        public void update(Farmaceuta f) throws Exception{
            String sql="update farmaceuta set clave=?,nombre=? "+
                    "where id=?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1,f.getClave());
            stm.setString(2, f.getNombre());
            stm.setString(3,f.getId());
            int count=db.executeUpdate(stm);
    //        if (count==0){
    //            throw new Exception("Farmaceuta ya existe");
    //        }
            if (count==0){
                throw new Exception("Farmaceuta no existe");
            }
        }

        public void delete(Farmaceuta f) throws Exception{
            String sql="delete from Farmaceuta where id=?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1, f.getId());
            int count=db.executeUpdate(stm);
            if (count==0){
                throw new Exception("Farmaceuta no existe");
            }
        }

        public List<Farmaceuta> findByNombre(Farmaceuta filtro){
            List<Farmaceuta> resultado = new ArrayList<Farmaceuta>();
            try {
                String sql="select * from Farmaceuta where nombre like ?";
                PreparedStatement stm = db.prepareStatement(sql);
                stm.setString(1, "%"+filtro.getNombre()+"%");
                ResultSet rs =  db.executeQuery(stm);
                Farmaceuta f;
                while (rs.next()) {
                    f= from(rs);
                    resultado.add(f);
                }
            } catch (SQLException ex) {  }
            return resultado;
        }

//        private Farmaceuta from(ResultSet rs, String alias){
//
//            try {
//                Farmaceuta f= new Farmaceuta();
//                f.setId(rs.getString(alias + ".id"));
//                f.setNombre(rs.getString(alias + ".nombre"));
//                f.setClave(rs.getString(alias + ".clave"));
//                return f;
//            } catch (SQLException ex) {
//                return null;
//            }
//        }
private Farmaceuta from(ResultSet rs) throws SQLException {
    Farmaceuta f = new Farmaceuta();
    f.setId(rs.getString("id"));
    f.setNombre(rs.getString("nombre"));
    f.setClave(rs.getString("clave"));
    return f;
}

    }
