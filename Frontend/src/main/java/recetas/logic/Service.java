package recetas.logic;


import progra3.logic.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Service {
    private static Service theInstance;

    public static Service instance() {
        if (theInstance == null) theInstance = new Service();
        return theInstance;
    }
    Socket s;
    ObjectOutputStream os;
    ObjectInputStream is;

    private Service() {
        try {
            s = new Socket(Protocol.SERVER, Protocol.PORT);
            os = new ObjectOutputStream(s.getOutputStream());
            is = new ObjectInputStream(s.getInputStream());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void disconnect() throws Exception {
        os.writeInt(Protocol.DISCONNECT);
        os.flush();
        s.shutdownOutput();
        s.close();
    }

    public void stop() {
        try {
            disconnect();
        } catch (Exception e) {
            System.exit(-1);
        }
    }

    // =============== Medico ===============
    public void create(Medico e) throws Exception {
        os.writeInt(Protocol.MEDICO_CREATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("MEDICO DUPLICADO");
    }

    public Medico read(Medico e) throws Exception {
        os.writeInt(Protocol.MEDICO_READ);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) return (Medico) is.readObject();
        else throw new Exception("MEDICO NO EXISTE");
    }

    public void update(Medico e) throws Exception {
        os.writeInt(Protocol.MEDICO_UPDATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("MEDICO NO EXISTE");
    }

    public List<Medico> findAll() throws Exception {
        os.writeInt(Protocol.MEDICO_FIND_ALL);
        os.writeObject(new Medico());
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {
            return (List<Medico>) is.readObject();
        } else throw new Exception("MEDICO NO EXISTE");
    }

    public void delete(Medico e) throws Exception {
        os.writeInt(Protocol.MEDICO_DELETE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("MEDICO NO EXISTE");
    }

    public List<Medico> search(Medico e) {
        try {
            os.writeInt(Protocol.MEDICO_SEARCH);
            os.writeObject(e);
            os.flush();
            if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                return (List<Medico>) is.readObject();
            }
            else return List.of();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // ============= Paciente ============
    public void create(Paciente e) throws Exception {
        os.writeInt(Protocol.PACIENTE_CREATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("PACIENTE DUPLICADO");
    }

    public Paciente read(Paciente e) throws Exception {
        os.writeInt(Protocol.PACIENTE_READ);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) return (Paciente) is.readObject();
        else throw new Exception("PACIENTE NO EXISTE");
    }

    public void update(Paciente e) throws Exception {
        os.writeInt(Protocol.PACIENTE_UPDATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("PACIENTE NO EXISTE");
    }

   public List<Paciente> findAllPaciente() {
       try {
           os.writeInt(Protocol.PACIENTE_FIND_ALL);
           os.writeObject(new Paciente());
           os.flush();
           if (is.readInt() == Protocol.ERROR_NO_ERROR) {
               return (List<Paciente>) is.readObject();
           } else {
               return new ArrayList<>();
           }
       } catch (Exception ex) {
           ex.printStackTrace();
           return new ArrayList<>();
       }
   }
    public void delete(Paciente e) throws Exception {
        os.writeInt(Protocol.PACIENTE_DELETE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("PACIENTE NO EXISTE");
    }

    public List<Paciente> search(Paciente e) {
        try {
            os.writeInt(Protocol.PACIENTE_SEARCH);
            os.writeObject(e);
            os.flush();
            if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                return (List<Paciente>) is.readObject();
            }
            else return List.of();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // =============== Medicamentos ===============
    public void create(Medicamento e) throws Exception {
        os.writeInt(Protocol.MEDICAMENTO_CREATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("MEDICAMENTO DUPLICADO");
    }

    public Medicamento read(Medicamento e) throws Exception {
        os.writeInt(Protocol.MEDICAMENTO_READ);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) return (Producto) is.readObject();
        else throw new Exception("MEDICAMENTO NO EXISTE");
    }

    public void update(Medicamento e) throws Exception {
        os.writeInt(Protocol.MEDICAMENTO_UPDATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("MEDICAMENTO NO EXISTE");
    }

    public List<Medicamento> findAllMedicamentos() {
        try {
            os.writeInt(Protocol.MEDICAMENTO_FIND_ALL);
            os.writeObject(new Medicamento());
            os.flush();
            if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                return (List<Medicamento>) is.readObject();
            } else {
                return new ArrayList<>();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void delete(Medicamento e) throws Exception {
        os.writeInt(Protocol.MEDICAMENTO_DELETE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("MEDICAMENTO NO EXISTE");
    }

    public List<Medicamento> search(Medicamento e) {
        try {
            os.writeInt(Protocol.MEDICAMENTO_SEARCH);
            os.writeObject(e);
            os.flush();
            if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                return (List<Medicamento>) is.readObject();
            }
            else return List.of();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // ============= Farmaceuta ===============
    public void create(Farmaceuta e) throws Exception {
        os.writeInt(Protocol.FARMACEUTA_CREATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("FARMACEUTA DUPLICADO");
    }

    public Farmaceuta read(Farmaceuta e) throws Exception {
        os.writeInt(Protocol.FARMACEUTA_READ);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) return (Farmaceuta) is.readObject();
        else throw new Exception("FARMACEUTA NO EXISTE");
    }

    public void update(Farmaceuta e) throws Exception {
        os.writeInt(Protocol.FARMACEUTA_UPDATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("FARMACEUTA NO EXISTE");
    }

    public void delete(Farmaceuta e) throws Exception {
        os.writeInt(Protocol.FARMACEUTA_DELETE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("FARMACEUTA NO EXISTE");
    }

    public List<Farmaceuta> search(Farmaceuta e) {
        try {
            os.writeInt(Protocol.FARMACEUTA_SEARCH);
            os.writeObject(e);
            os.flush();
            if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                return (List<Farmaceuta>) is.readObject();
            }
            else return List.of();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
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
    public void create(Receta e) throws Exception {
        Database db = Database.instance();
        String idMedico = (e.getMedico() != null) ? e.getMedico().getId() : null;
        String idPaciente = (e.getPaciente() != null) ? e.getPaciente().getId() : null;

        try {
            db.setAutoCommit(false);
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
        try {

            if (e != null && e.getPaciente() != null && e.getPaciente().getId() != null && !e.getPaciente().getId().trim().isEmpty()) {
                String idPaciente = e.getPaciente().getId().trim();
                try {
                    return recetaDao.findByPaciente(idPaciente);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return new ArrayList<>();
                }
            }
            return recetaDao.findByNombre(e);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
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
        try {
            if (e != null && e.getId() != null && !e.getId().trim().isEmpty()) {
                List<Usuario> resultado = new ArrayList<>();
                try {
                    Usuario u = usuarioDao.read(e.getId().trim());
                    if (u != null) resultado.add(u);
                } catch (Exception ex) {
                }
                return resultado;
            } else {
                return usuarioDao.findByNombre(e);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
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

