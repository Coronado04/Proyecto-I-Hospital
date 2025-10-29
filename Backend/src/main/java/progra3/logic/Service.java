package progra3.logic;

import progra3.data.*;

import java.util.List;

/**
 * Service (backend) - capa de negocio que delega en DAOs.
 * Esta versión NO abre sockets ni streams. Es llamada por Worker para
 * ejecutar operaciones solicitadas por el cliente.
 */
public class Service {

    private static Service theInstance;

    public static Service instance() {
        if (theInstance == null) theInstance = new Service();
        return theInstance;
    }

    private final MedicoDao medicoDao;
    private final PacienteDao pacienteDao;
    private final MedicamentoDao medicamentoDao;
    private final FarmaceutaDao farmaceutaDao;
    private final LineaDao lineaDao;
    private final RecetaDao recetaDao;
    private final UsuarioDao usuarioDao;

    private Service() {
        // inicializar DAOs (Database.instance() se crea dentro de los DAOs)
        medicoDao = new MedicoDao();
        pacienteDao = new PacienteDao();
        medicamentoDao = new MedicamentoDao();
        farmaceutaDao = new FarmaceutaDao();
        lineaDao = new LineaDao();
        recetaDao = new RecetaDao();
        usuarioDao = new UsuarioDao();
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

    public List<Medico> findAll() throws Exception {
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

    // =============== Medicamento ===============
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
        Medicamento filtro = new Medicamento();
        filtro.setNombre("");
        return medicamentoDao.findByNombre(filtro);
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

    public void update(Linea e) throws Exception {
        lineaDao.update(e, e.getNumero());
    }

    // =========== RECETAS ==============
    public void create(Receta e) throws Exception {
        // crear receta y sus líneas en una transacción (si tu DAO soporta transaccionalidad)
        Database db = Database.instance();
        try {
            db.setAutoCommit(false);
            String idMedico = (e.getMedico() != null) ? e.getMedico().getId() : null;
            String idPaciente = (e.getPaciente() != null) ? e.getPaciente().getId() : null;
            recetaDao.create(e, idMedico, idPaciente);
            String idRecetaGenerada = e.getIdReceta();
            if (idRecetaGenerada == null || idRecetaGenerada.trim().isEmpty()) {
                throw new Exception("No se pudo obtener el número de receta generado por la base de datos.");
            }
            if (e.getDetalles() != null) {
                for (Linea linea : e.getDetalles()) {
                    lineaDao.create(linea, idRecetaGenerada);
                }
            }
            db.commit();
        } catch (Exception ex) {
            db.rollback();
            throw ex;
        } finally {
            try { db.setAutoCommit(true); } catch (Exception ignore) {}
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