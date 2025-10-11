package recetas.data;

import recetas.logic.Linea;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LineaDao {
    Database db;

    public LineaDao(){
        db = Database.instance();
    }

    public void create(Linea l) throws Exception{
        String sql="insert into Linea (medicamento,cantidad,indicaciones,duracionDias) values (?,?,?,?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1,l.getMedicamento().getCodigo());
        stm.setInt(2,l.getCantidad());
        stm.setString(3,l.getIndicaciones());
        stm.setInt(4,l.getDuracionDias());
    }



}
