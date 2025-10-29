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
        if (is.readInt() == Protocol.ERROR_NO_ERROR) return (Medicamento) is.readObject();
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
    public void create(Linea l, String idReceta) throws Exception { //*****************************
        if (idReceta == null || idReceta.isEmpty()) {
            throw new Exception("La Línea debe pertenecer a una receta existente");
        }
        lineaDao.create(l, idReceta);
    }

    public Linea read(Linea e) throws Exception {
        os.writeInt(Protocol.LINEA_READ);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) return (Linea) is.readObject();
        else throw new Exception("LINEA NO EXISTE");
    }

    public void delete(Linea e) throws Exception {
        os.writeInt(Protocol.LINEA_DELETE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("LINEA NO EXISTE");
    }

    public List<Linea> search(Linea e) {
        try {
            os.writeInt(Protocol.LINEA_SEARCH);
            os.writeObject(e);
            os.flush();
            if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                return (List<Linea>) is.readObject();
            }
            else return List.of();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // =========== RECETAS ==============
    public void create(Receta e) throws Exception { //***************************************
        Database db = Database.instance();
        String idMedico = (e.getMedico() != null) ? e.getMedico().getId() : null;
        String idPaciente = (e.getPaciente() != null) ? e.getPaciente().getId() : null;

        try {
            db.setAutoCommit(false);
            recetaDao.create(e, idMedico, idPaciente);
            String idRecetaGenerada = e.getIdReceta();
            if (idRecetaGenerada == null || idRecetaGenerada.trim().isEmpty()) {
                throw new Exception("No se pudo obtener el nÃºmero de receta generado por la base de datos.");
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
        os.writeInt(Protocol.RECETA_READ);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) return (Receta) is.readObject();
        else throw new Exception("RECETA NO EXISTE");
    }

    public void update(Receta e) throws Exception {
        os.writeInt(Protocol.RECETA_UPDATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("RECETA NO EXISTE");
    }

    public List<Receta> findAllRecetas() { //************************************
        try {
            os.writeInt(Protocol.RECETA_FIND_ALL);
            os.writeObject(new Paciente());
            os.flush();
            if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                return (List<Receta>) is.readObject();
            } else {
                return new ArrayList<>();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void delete(Receta e) throws Exception {
        os.writeInt(Protocol.RECETA_DELETE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("RECETA NO EXISTE");
    }

    public List<Receta> search(Receta e) {
        try {
            // os e is deben ser ObjectOutputStream / ObjectInputStream asociados al socket
            os.writeInt(Protocol.RECETA_SEARCH); // enviar código de operación
            os.writeObject(e);                   // enviar filtro (puede ser null)
            os.flush();

            int status = is.readInt();           // leer código de respuesta
            if (status == Protocol.ERROR_NO_ERROR) {
                return (List<Receta>) is.readObject(); // recibir la lista de recetas
            } else {
                // el servidor devolvió un error, puedes leer un mensaje si lo envía
                // String msg = (String) is.readObject();
                return List.of();
            }
        } catch (Exception ex) {
            // Manejo de excepción: loguear y, si quieres, fallback a llamada local
            ex.printStackTrace();
            // Opcional: fallback local si el DAO está disponible:
            // try { return recetaDao.findByNombre(e); } catch(Exception e2) { return new ArrayList<>(); }
            throw new RuntimeException(ex);
        }
    }

    // ============ USUARIO ============
    public void create(Usuario e) throws Exception {
        os.writeInt(Protocol.USUARIO_CREATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("USUARIO DUPLICADO");
    }

    public Usuario read(Usuario e) throws Exception {
        os.writeInt(Protocol.USUARIO_READ);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) return (Usuario) is.readObject();
        else throw new Exception("USUARIO NO EXISTE");
    }

    public void update(Usuario e) throws Exception {
        os.writeInt(Protocol.USUARIO_UPDATE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("USUARIO NO EXISTE");
    }

    public List<Usuario> findAllUsuarios() {
        try {
            os.writeInt(Protocol.USUARIO_FIND_ALL);
            os.writeObject(new Paciente());
            os.flush();
            if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                return (List<Usuario>) is.readObject();
            } else {
                return new ArrayList<>();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void delete(Usuario e) throws Exception {
        os.writeInt(Protocol.USUARIO_DELETE);
        os.writeObject(e);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR) {}
        else throw new Exception("USUARIO NO EXISTE");
    }

    public List<Usuario> search(Usuario e) {
        try {
            os.writeInt(Protocol.USUARIO_SEARCH);
            os.writeObject(e);
            os.flush();
            if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                return (List<Usuario>) is.readObject();
            }
            else return List.of();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
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
