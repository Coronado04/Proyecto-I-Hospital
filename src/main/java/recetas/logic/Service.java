package recetas.logic;

import recetas.data.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    private Service() {
        try{
            medicoDao = new MedicoDao();
            pacienteDao = new PacienteDao();
            farmaceutaDao = new FarmaceutaDao();
            lineaDao = new LineaDao();
            medicamentoDao = new MedicamentoDao();
        } catch (Exception e) {
            System.out.println(e);
        }

         //   data.setAdmin(new Usuario("Administrador", "admin", "1234", "admin"));

    }

    public void stop(){
        try{
            Database.instance().close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
   /* public Usuario getAdmin() {
        return data.getAdmin();
    }*/

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

    //============Paciente===========
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
        medicamentoDao.read(e.getCodigo());
    }

    public void update(Medicamento e) throws Exception {
        medicamentoDao.update(e);
    }

    public List<Medicamento> findAllMedicamentos() {return medicamentoDao.findByNombre(new Medicamento());}

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
        farmaceutaDao.read(e.getId());
    }

    public void update(Farmaceuta e) throws Exception {
        farmaceutaDao.update(e);
    }


    public void delete(Farmaceuta e) throws Exception {
        farmaceutaDao.delete(e);
    }
    public List<Farmaceuta> search(Farmaceuta e){
        farmaceutaDao.findByNombre(e);
    }
//===========LINEA==============

    public Linea read(Linea e) throws Exception {
        Linea result = data.getLineas().stream()
                .filter(i -> i.getMedicamento().getCodigo().equals(e.getMedicamento().getCodigo()))
                .findFirst()
                .orElse(null);
        if (result != null) {
            return result;
        } else {
            throw new Exception("Linea no existe");
        }
    }



    public void delete(Linea e) throws Exception {
        boolean removed = data.getLineas().removeIf(m -> m.getMedicamento().getCodigo().equals(e.getMedicamento().getCodigo()));
        if (!removed) {
            throw new Exception("No se encontró la Linea a eliminar");
        }
    }

    public List<Linea> search(Linea e) {
        return data.getLineas().stream()
                .filter(l -> l.getMedicamento().getNombre().contains(e.getMedicamento().getNombre()))
                .sorted(Comparator.comparing(l -> l.getMedicamento().getNombre()))
                .collect(Collectors.toList());
    }
    public List<Linea> searchLinea(Linea e){
        String indicaciones = e.getIndicaciones() == null ? "" : e.getIndicaciones().trim();

        return data.getLineas().stream()
                .filter(i -> (indicaciones.isEmpty() || i.getIndicaciones().contains(indicaciones)))
                .sorted(Comparator.comparing(Linea::getIndicaciones))
                .collect(Collectors.toList());

    }
//==========RECETAS==============
public void create(Receta e) throws Exception {
    Receta result = data.getRecetas().stream()
            .filter(i -> i.getIdReceta().equals(e.getIdReceta()))
            .findFirst()
            .orElse(null);
    if (result == null) {
        data.getRecetas().add(e);
    } else {
        throw new Exception("Receta ya existe");
    }
}

    public Receta read(Receta e) throws Exception {
        Receta result = data.getRecetas().stream()
                .filter(i -> i.getIdReceta().equals(e.getIdReceta()))
                .findFirst()
                .orElse(null);
        if (result != null) {
            return result;
        } else {
            throw new Exception("Receta no existe");
        }
    }

    public void update(Receta e) throws Exception {
        Receta result = this.read(e);
        data.getRecetas().remove(result);
        data.getRecetas().add(e);
    }

    public List<Receta> findAllRecetas() {
        return data.getRecetas();
    }

    public void delete(Receta e) throws Exception {
        boolean removed = data.getRecetas().removeIf(r -> r.getIdReceta().equals(e.getIdReceta()));
        if (!removed) {
            throw new Exception("No se encontró la receta a eliminar");
        }
    }


    public List<Receta> search(Receta e) {
        String nombreFiltro = (e.getPaciente() != null && e.getPaciente().getNombre() != null)
                ? e.getPaciente().getNombre().trim()
                : "";
        String idFiltro = (e.getPaciente() != null && e.getPaciente().getId() != null)
                ? e.getPaciente().getId().trim()
                : "";

        return data.getRecetas().stream()
                .filter(r -> r.getPaciente() != null)
                .filter(r ->
                        (nombreFiltro.isEmpty() || r.getPaciente().getNombre().toLowerCase().contains(nombreFiltro.toLowerCase())) &&
                                (idFiltro.isEmpty() || r.getPaciente().getId().toLowerCase().contains(idFiltro.toLowerCase()))
                )
                .sorted(Comparator.comparing(r -> r.getPaciente().getNombre()))
                .collect(Collectors.toList());
    }
    public List<Receta> searchId(Receta filtro) {
        LocalDate hoy = LocalDate.now();

        return data.getRecetas().stream()
                .filter(r -> {
                    // opcional: si se quiere filtrar también por paciente
                    if (filtro.getPaciente() != null && filtro.getPaciente().getId() != null) {
                        return r.getPaciente().getId().contains(filtro.getPaciente().getId());
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    public int getNextRecetaId() {
        if (data.getRecetas().isEmpty()) {
            return 1;
        }
        return data.getRecetas().stream()
                .map(r -> r.getIdReceta().replace("REC-", ""))
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0) + 1;
    }


}