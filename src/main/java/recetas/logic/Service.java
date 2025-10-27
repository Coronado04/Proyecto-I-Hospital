package recetas.logic;

import recetas.data.*;

import java.sql.SQLException;
import java.util.List;

public class Service {
    private static Service theInstance;

    public static Service instance() {
        if (theInstance == null) theInstance = new Service();
        return theInstance;
    }

    private MedicoDao medicoDao;
    private PacienteDao pacienteDao;
    private FarmaceutaDao farmaceutaDao;
    private LineaDao lineaDao;
    private MedicamentoDao medicamentoDao;
    private RecetaDao recetaDao;
    private UsuarioDao usuarioDao;

    private Service() {
        try{
            medicoDao = new MedicoDao();
            pacienteDao = new PacienteDao();
            farmaceutaDao = new FarmaceutaDao();
            lineaDao = new LineaDao();
            medicamentoDao = new MedicamentoDao();
            recetaDao = new RecetaDao();
            usuarioDao = new UsuarioDao();
            // Si quieres crear un usuario admin por defecto, hazlo explícitamente con usuarioDao.create(...)
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void stop(){
        try{
            Database.instance().close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // =============== Medico ===============
    public void create(Medico e) throws Exception {
        medicoDao.create(e);
    }

    public Medico read(Medico e) throws Exception {
        return medicoDao.read(e.getId());
    }

    public void update(Medico e) throws Exception {
        medicoDao.update(e);
    }

    public List<Medico> findAll() {
        Medico filtro = new Medico();
        filtro.setNombre("");
        return medicoDao.findByNombre(filtro);
    }

    public void delete(Medico e) throws Exception {
        medicoDao.delete(e);
    }

    public List<Medico> search(Medico e) {
        return medicoDao.findByNombre(e);
    }

    // ============= Paciente ============
    public void create(Paciente e) throws Exception {
        pacienteDao.create(e);
    }
    public Paciente read(Paciente e) throws Exception {
        return pacienteDao.read(e.getId());
    }

    public void update(Paciente e) throws Exception {
        pacienteDao.update(e);
    }

    public List<Paciente> findAllPaciente() {
        Paciente filtro = new Paciente();
        filtro.setNombre("");
        return pacienteDao.findByNombre(filtro);
    }
    public void delete(Paciente e) throws Exception {
        pacienteDao.delete(e);
    }
    public List<Paciente> search(Paciente e) {
        return pacienteDao.findByNombre(e);
    }

    // =============== Medicamentos ===============
    public void create(Medicamento e) throws Exception {
        medicamentoDao.create(e);
    }

    public Medicamento read(Medicamento e) throws Exception {
        return medicamentoDao.read(e.getCodigo());
    }

    public void update(Medicamento e) throws Exception {
        medicamentoDao.update(e);
    }

    public List<Medicamento> findAllMedicamentos() {
        return medicamentoDao.findByNombre(new Medicamento());
    }

    public void delete(Medicamento e) throws Exception {
        medicamentoDao.delete(e);
    }
    public List<Medicamento> search(Medicamento e) {
        return medicamentoDao.findByNombre(e);
    }

    // ============= Farmaceuta ===============
    public void create(Farmaceuta e) throws Exception {
        farmaceutaDao.create(e);
    }

    public Farmaceuta read(Farmaceuta e) throws Exception {
        return farmaceutaDao.read(e.getId());
    }

    public void update(Farmaceuta e) throws Exception {
        farmaceutaDao.update(e);
    }

    public void delete(Farmaceuta e) throws Exception {
        farmaceutaDao.delete(e);
    }
    public List<Farmaceuta> search(Farmaceuta e){
        return farmaceutaDao.findByNombre(e);
    }

    // ============ LINEA ==============
    public void create(Linea l, String idReceta) throws Exception {
        if (idReceta == null || idReceta.isEmpty()) {
            throw new Exception("La línea debe pertenecer a una receta existente");
        }
        lineaDao.create(l, idReceta);
    }

    public Linea read(Linea e) throws Exception {
        return lineaDao.read(e.getNumero());
    }

    public void delete(Linea e) throws Exception {
        lineaDao.delete(e.getNumero());
    }

    public List<Linea> search(Linea e) {
        return lineaDao.findByNombre(e);
    }

    // =========== RECETAS ==============
    /**
     * Crea una Receta y sus Lineas en una única transacción:
     *  - Inserta la receta (RecetaDao.create) -> la DAO debe asignar el número generado a e.setIdReceta(...)
     *  - Inserta todas las Lineas asociadas con lineaDao.create(linea, idRecetaGenerada)
     */
    public void create(Receta e) throws Exception {
        Database db = Database.instance();
        // obtener ids relacionados
        String idMedico   = (e.getMedico()   != null) ? e.getMedico().getId()   : null;
        String idPaciente = (e.getPaciente() != null) ? e.getPaciente().getId() : null;

        try {
            // Iniciar transacción
            db.setAutoCommit(false);

            // 1) Crear receta (la DAO debe asignar la PK generada dentro de e.setIdReceta(...))
            recetaDao.create(e, idMedico, idPaciente);

            // 2) Obtener idReceta generado
            String idRecetaGenerada = e.getIdReceta();
            if (idRecetaGenerada == null || idRecetaGenerada.trim().isEmpty()) {
                throw new Exception("No se pudo obtener el número de receta generado por la base de datos.");
            }

            // 3) Insertar líneas asociadas (si existen)
            if (e.getDetalles() != null) {
                for (Linea linea : e.getDetalles()) {
                    lineaDao.create(linea, idRecetaGenerada);
                }
            }

            // 4) Commit
            db.commit();
        } catch (Exception ex) {
            // Rollback ante cualquier fallo
            db.rollback();
            throw ex;
        } finally {
            // Restaurar autocommit true
            try {
                db.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public Receta read(Receta e) throws Exception {
        return recetaDao.read(e.getIdReceta());
    }

    public void update(Receta e) throws Exception {
        recetaDao.update(e, e.getIdReceta());
    }

    public List<Receta> findAllRecetas() {
        Receta filtro = new Receta();
        Paciente p = new Paciente();
        p.setNombre("");
        filtro.setPaciente(p);
        return recetaDao.findByNombre(filtro);
    }

    public void delete(Receta e) throws Exception {
        recetaDao.delete(e.getIdReceta());
    }

    public List<Receta> search(Receta e) {
        return recetaDao.findByNombre(e);
    }

    // ============ USUARIO ============
    public void create(Usuario e) throws Exception {
        usuarioDao.create(e);
    }

    public Usuario read(Usuario e) throws Exception {
        return usuarioDao.read(e.getId());
    }

    public void update(Usuario e) throws Exception {
        usuarioDao.update(e);
    }

    public List<Usuario> findAllUsuarios() {
        Usuario filtro = new Usuario();
        filtro.setNombre("");
        return usuarioDao.findByNombre(filtro);
    }

    public void delete(Usuario e) throws Exception {
        usuarioDao.delete(e);
    }

    public List<Usuario> search(Usuario e) {
        return usuarioDao.findByNombre(e);
    }
}

//No estoy seguro de esta polla
//    public int getNextRecetaId() {
//        if (data.getRecetas().isEmpty()) {
//            return 1;
//        }
//        return data.getRecetas().stream()
//                .map(r -> r.getIdReceta().replace("REC-", ""))
//                .mapToInt(Integer::parseInt)
//                .max()
//                .orElse(0) + 1;
//    }

