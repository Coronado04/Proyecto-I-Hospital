package recetas.logic;

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

    private Data data;

    private Service() {
        try{
            data = XmlPersister.instance().load();
        } catch (Exception e) {
            data = new Data();

         //   data.setAdmin(new Usuario("Administrador", "admin", "1234", "admin"));
        }
    }

    public void stop(){
        try{
            XmlPersister.instance().store(data);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public Usuario getAdmin() {
        return data.getAdmin();
    }

    // =============== Medico ===============
    public void create(Medico e) throws Exception {
        Medico result = data.getMedicos().stream()
                .filter(i -> i.getId().equals(e.getId()))
                .findFirst()
                .orElse(null);
        if (result == null) {
            data.getMedicos().add(e);
        } else {
            throw new Exception("Medico ya existe");
        }
    }

    public Medico read(Medico e) throws Exception {
        Medico result = data.getMedicos().stream()
                .filter(i -> i.getId().equals(e.getId()))
                .findFirst()
                .orElse(null);
        if (result != null) {
            return result;
        } else {
            throw new Exception("Medico no existe");
        }
    }

    public void update(Medico e) throws Exception {
        Medico result;
        try{
            result = this.read(e);
            data.getMedicos().remove(result);
            data.getMedicos().add(e);
        } catch (Exception ex) {
            throw new Exception("Medico no existe");
        }
    }

    public List<Medico> findAll() {
        return data.getMedicos();
    }

    public void delete(Medico e) throws Exception {
        boolean removed = data.getMedicos().removeIf(m -> m.getId().equals(e.getId()));
        if (!removed) {
            throw new Exception("No se encontró el médico a eliminar");
        }
    }

    public List<Medico> search(Medico e) {
        return data.getMedicos().stream()
                .filter(i -> i.getNombre().contains(e.getNombre()) && i.getId().contains(e.getId()))
                .sorted(Comparator.comparing(Medico::getNombre))
                .collect(Collectors.toList());
    }

    public List<Medico> searchMedico(Medico e) {
        String idFiltro = e.getId() == null ? "" : e.getId().trim();

        return data.getMedicos().stream()
                .filter(i -> (idFiltro.isEmpty() || i.getId().contains(idFiltro)))
                        .sorted(Comparator.comparing(Medico::getId))
                .collect(Collectors.toList());
    }


    //============Paciente===========
    public void create(Paciente e) throws Exception {
        Paciente result = data.getPacientes().stream()
                .filter(i -> i.getId().equals(e.getId()))
                .findFirst()
                .orElse(null);
        if (result == null) {
            data.getPacientes().add(e);
        } else {
            throw new Exception("Paciente ya existe");
        }
    }
    public Paciente read(Paciente e) throws Exception {
        Paciente result = data.getPacientes().stream()
                .filter(i -> i.getId().equals(e.getId()))
                .findFirst()
                .orElse(null);
        if (result != null) {
            return result;
        } else {
            throw new Exception("Paciente no existe");
        }
    }

    public void update(Paciente e) throws Exception {
        Paciente result;
        try{
            result = this.read(e);
            data.getPacientes().remove(result);
            data.getPacientes().add(e);
        } catch (Exception ex) {
            throw new Exception("Paciente no existe");
        }
    }

    public List<Paciente> findAllPaciente() {
        return data.getPacientes();
    }
    public void delete(Paciente e) throws Exception {
        boolean removed = data.getPacientes().removeIf(m -> m.getId().equals(e.getId()));
        if (!removed) {
            throw new Exception("No se encontró el paciente a eliminar");
        }
    }
    public List<Paciente> search(Paciente e) {
        return data.getPacientes().stream()
                .filter(i -> i.getNombre().contains(e.getNombre()) && i.getId().contains(e.getId()))
                .sorted(Comparator.comparing(Paciente::getNombre))
                .collect(Collectors.toList());
    }
    public List<Paciente> searchPaciente(Paciente e) {
        String nombreFiltro = e.getNombre() == null ? "" : e.getNombre().trim();
        String idFiltro = e.getId() == null ? "" : e.getId().trim();

        return data.getPacientes().stream()
                .filter(i -> (nombreFiltro.isEmpty() || i.getNombre().contains(nombreFiltro)) &&
                        (idFiltro.isEmpty() || i.getId().contains(idFiltro)))
                .sorted(Comparator.comparing(Paciente::getNombre))
                .collect(Collectors.toList());
    }


    // =============== Medicamentos ===============
    public void create(Medicamento e) throws Exception {
        Medicamento result = data.getMedicamentos().stream()
                .filter(i -> i.getCodigo().equals(e.getCodigo()))
                .findFirst()
                .orElse(null);
        if (result == null) {
            data.getMedicamentos().add(e);
        } else {
            throw new Exception("Medicamento ya existe");
        }
    }

    public Medicamento read(Medicamento e) throws Exception {
        Medicamento result = data.getMedicamentos().stream()
                .filter(i -> i.getCodigo().equals(e.getCodigo()))
                .findFirst()
                .orElse(null);
        if (result != null) {
            return result;
        } else {
            throw new Exception("Medicamento no existe");
        }
    }

    public void update(Medicamento e) throws Exception {
        Medicamento result;
        try{
            result = this.read(e);
            data.getMedicamentos().remove(result);
            data.getMedicamentos().add(e);
        } catch (Exception ex) {
            throw new Exception("Medicamento no existe");
        }
    }

    public List<Medicamento> findAllMedicamentos() {return data.getMedicamentos();}

    public void delete(Medicamento e) throws Exception {
        boolean removed = data.getMedicamentos().removeIf(m -> m.getCodigo().equals(e.getCodigo()));
        if (!removed) {
            throw new Exception("No se encontró el Medicamento a eliminar");
        }
    }
    public List<Medicamento> search(Medicamento e) {
        return data.getMedicamentos().stream()
                .filter(i -> i.getPresentacion().contains(e.getPresentacion()) && i.getCodigo().contains(e.getCodigo()))
                .sorted(Comparator.comparing(Medicamento::getPresentacion))
                .collect(Collectors.toList());
    }
    public List<Medicamento> searchMedicamento(Medicamento e) {
        String nombreFiltro = e.getPresentacion() == null ? "" : e.getPresentacion().trim();
        String idFiltro = e.getCodigo() == null ? "" : e.getCodigo().trim();

        return data.getMedicamentos().stream()
                .filter(i -> (nombreFiltro.isEmpty() || i.getPresentacion().contains(nombreFiltro)) &&
                (idFiltro.isEmpty() ||  i.getCodigo().contains(idFiltro)))
                .sorted(Comparator.comparing(Medicamento::getCodigo))
                .collect(Collectors.toList());
    }


    // ============= Farmaceuta ===============
    public void create(Farmaceuta e) throws Exception {
        Farmaceuta result = data.getFarmaceutas().stream()
                .filter(i -> i.getId().equals(e.getId()))
                .findFirst()
                .orElse(null);
        if (result == null) {
            data.getFarmaceutas().add(e);
        } else {
            throw new Exception("Farmaceuta ya existe");
        }
    }

    public Farmaceuta read(Farmaceuta e) throws Exception {
        Farmaceuta result = data.getFarmaceutas().stream()
                .filter(i -> i.getId().equals(e.getId()))
                .findFirst()
                .orElse(null);
        if (result != null) {
            return result;
        } else {
            throw new Exception("Medico no existe");
        }
    }

    public void update(Farmaceuta e) throws Exception {
        Farmaceuta result;
        try{
            result = this.read(e);
            data.getFarmaceutas().remove(result);
            data.getFarmaceutas().add(e);
        } catch (Exception ex) {
            throw new Exception("Farmaceuta no existe");
        }
    }


    public void delete(Farmaceuta e) throws Exception {
        boolean removed = data.getFarmaceutas().removeIf(m -> m.getId().equals(e.getId()));
        if (!removed) {
            throw new Exception("No se encontró el Farmaceuta a eliminar");
        }
    }
    public List<Farmaceuta> search(Farmaceuta e){
        return data.getFarmaceutas().stream()
                .filter(i -> i.getNombre().contains(e.getNombre()) && i.getId().contains(e.getId()))
                .sorted(Comparator.comparing(Farmaceuta::getNombre))
                .collect(Collectors.toList());
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