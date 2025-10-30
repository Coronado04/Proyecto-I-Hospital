package recetas.logic;

import progra3.logic.*;
import recetas.Sesion;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Service {
    private static Service theInstance;
    private String sid = java.util.UUID.randomUUID().toString();

    public String getSid() {
        return sid;
    }
    public static Service instance() {
        if (theInstance == null) theInstance = new Service();
        return theInstance;
    }

    // Streams del socket al backend (deben inyectarse desde el Controller al hacer login)
    private transient ObjectOutputStream os;
    private transient ObjectInputStream is;

    // Lock para serializar todas las operaciones sobre os/is
    private final Object commLock = new Object();

    private Service() {
        // no crear socket aquí; será inyectado por el Controller
    }

    /**
     * Inyectar los streams (una única vez) después de crear la conexión en el Controller.
     */
    public synchronized void setStreams(ObjectOutputStream os, ObjectInputStream is) {
        this.os = os;
        this.is = is;
    }

    private void ensureConnected() throws Exception {
        if (os == null || is == null) {
            throw new Exception("No conectado al servidor (streams no inicializados).");
        }
    }

    private void disconnect() throws Exception {
        ensureConnected();
        synchronized (commLock) {
            os.writeInt(Protocol.DISCONNECT);
            os.flush();
        }
        // no cerramos streams/sockets aquí: quién los creó (Controller) los debe cerrar
    }

    public void stop() {
        try {
            disconnect();
        } catch (Exception e) {
            System.err.println("Error al desconectar del servidor: " + e.getMessage());
        }
    }

    // ----------------- Helpers -----------------
    private RuntimeException wrapSocketException(Exception ex) {
        if (ex instanceof SocketException) {
            return new RuntimeException("Conexión con el servidor perdida: " + ex.getMessage(), ex);
        } else {
            return new RuntimeException(ex);
        }
    }

    // =============== Medico ===============
    public void create(Medico e) throws Exception {
        ensureConnected();
        try {
            synchronized (commLock) {
                os.writeInt(Protocol.MEDICO_CREATE);
                os.writeObject(e);
                os.flush();
                if (is.readInt() != Protocol.ERROR_NO_ERROR) throw new Exception("MEDICO DUPLICADO");
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    public Medico read(Medico e) throws Exception {
        ensureConnected();
        try {
            synchronized (commLock) {
                os.writeInt(Protocol.MEDICO_READ);
                os.writeObject(e);
                os.flush();
                if (is.readInt() == Protocol.ERROR_NO_ERROR) return (Medico) is.readObject();
                else throw new Exception("MEDICO NO EXISTE");
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    public void update(Medico e) throws Exception {
        ensureConnected();
        try {
            synchronized (commLock) {
                os.writeInt(Protocol.MEDICO_UPDATE);
                os.writeObject(e);
                os.flush();
                if (is.readInt() != Protocol.ERROR_NO_ERROR) throw new Exception("MEDICO NO EXISTE");
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    public List<Medico> findAll() throws Exception {
        ensureConnected();
        try {
            synchronized (commLock) {
                os.writeInt(Protocol.MEDICO_FIND_ALL);
                os.flush();
                if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                    return (List<Medico>) is.readObject();
                } else return new ArrayList<>();
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    public void delete(Medico e) throws Exception {
        ensureConnected();
        try {
            synchronized (commLock) {
                os.writeInt(Protocol.MEDICO_DELETE);
                os.writeObject(e);
                os.flush();
                if (is.readInt() != Protocol.ERROR_NO_ERROR) throw new Exception("MEDICO NO EXISTE");
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    public List<Medico> search(Medico e) {
        try {
            ensureConnected();
            synchronized (commLock) {
                os.writeInt(Protocol.MEDICO_SEARCH);
                os.writeObject(e);
                os.flush();
                if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                    return (List<Medico>) is.readObject();
                } else return List.of();
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    // ============= Paciente ============
    public void create(Paciente e) throws Exception {
        ensureConnected();
        try {
            synchronized (commLock) {
                os.writeInt(Protocol.PACIENTE_CREATE);
                os.writeObject(e);
                os.flush();
                if (is.readInt() != Protocol.ERROR_NO_ERROR) throw new Exception("PACIENTE DUPLICADO");
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    public Paciente read(Paciente e) throws Exception {
        ensureConnected();
        try {
            synchronized (commLock) {
                os.writeInt(Protocol.PACIENTE_READ);
                os.writeObject(e);
                os.flush();
                if (is.readInt() == Protocol.ERROR_NO_ERROR) return (Paciente) is.readObject();
                else throw new Exception("PACIENTE NO EXISTE");
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    public void update(Paciente e) throws Exception {
        ensureConnected();
        try {
            synchronized (commLock) {
                os.writeInt(Protocol.PACIENTE_UPDATE);
                os.writeObject(e);
                os.flush();
                if (is.readInt() != Protocol.ERROR_NO_ERROR) throw new Exception("PACIENTE NO EXISTE");
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    public List<Paciente> findAllPaciente() {
        try {
            ensureConnected();
            synchronized (commLock) {
                os.writeInt(Protocol.PACIENTE_FIND_ALL);
                os.flush();
                if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                    return (List<Paciente>) is.readObject();
                } else {
                    return new ArrayList<>();
                }
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    public void delete(Paciente e) throws Exception {
        ensureConnected();
        try {
            synchronized (commLock) {
                os.writeInt(Protocol.PACIENTE_DELETE);
                os.writeObject(e);
                os.flush();
                if (is.readInt() != Protocol.ERROR_NO_ERROR) throw new Exception("PACIENTE NO EXISTE");
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    public List<Paciente> search(Paciente e) {
        try {
            ensureConnected();
            synchronized (commLock) {
                os.writeInt(Protocol.PACIENTE_SEARCH);
                os.writeObject(e);
                os.flush();
                if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                    return (List<Paciente>) is.readObject();
                } else return List.of();
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    // =============== Medicamentos ===============
    public void create(Medicamento e) throws Exception {
        ensureConnected();
        try {
            synchronized (commLock) {
                os.writeInt(Protocol.MEDICAMENTO_CREATE);
                os.writeObject(e);
                os.flush();
                if (is.readInt() != Protocol.ERROR_NO_ERROR) throw new Exception("MEDICAMENTO DUPLICADO");
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    public Medicamento read(Medicamento e) throws Exception {
        ensureConnected();
        try {
            synchronized (commLock) {
                os.writeInt(Protocol.MEDICAMENTO_READ);
                os.writeObject(e);
                os.flush();
                if (is.readInt() == Protocol.ERROR_NO_ERROR) return (Medicamento) is.readObject();
                else throw new Exception("MEDICAMENTO NO EXISTE");
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    public void update(Medicamento e) throws Exception {
        ensureConnected();
        try {
            synchronized (commLock) {
                os.writeInt(Protocol.MEDICAMENTO_UPDATE);
                os.writeObject(e);
                os.flush();
                if (is.readInt() != Protocol.ERROR_NO_ERROR) throw new Exception("MEDICAMENTO NO EXISTE");
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    public List<Medicamento> findAllMedicamentos() {
        try {
            ensureConnected();
            synchronized (commLock) {
                os.writeInt(Protocol.MEDICAMENTO_FIND_ALL);
                os.flush();
                if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                    return (List<Medicamento>) is.readObject();
                } else {
                    return new ArrayList<>();
                }
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    public void delete(Medicamento e) throws Exception {
        ensureConnected();
        try {
            synchronized (commLock) {
                os.writeInt(Protocol.MEDICAMENTO_DELETE);
                os.writeObject(e);
                os.flush();
                if (is.readInt() != Protocol.ERROR_NO_ERROR) throw new Exception("MEDICAMENTO NO EXISTE");
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    public List<Medicamento> search(Medicamento e) {
        try {
            ensureConnected();
            synchronized (commLock) {
                os.writeInt(Protocol.MEDICAMENTO_SEARCH);
                os.writeObject(e);
                os.flush();
                if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                    return (List<Medicamento>) is.readObject();
                } else return List.of();
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    // ============= Farmaceuta ===============
    public void create(Farmaceuta e) throws Exception {
        ensureConnected();
        try {
            synchronized (commLock) {
                os.writeInt(Protocol.FARMACEUTA_CREATE);
                os.writeObject(e);
                os.flush();
                if (is.readInt() != Protocol.ERROR_NO_ERROR) throw new Exception("FARMACEUTA DUPLICADO");
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    public Farmaceuta read(Farmaceuta e) throws Exception {
        ensureConnected();
        try {
            synchronized (commLock) {
                os.writeInt(Protocol.FARMACEUTA_READ);
                os.writeObject(e);
                os.flush();
                if (is.readInt() == Protocol.ERROR_NO_ERROR) return (Farmaceuta) is.readObject();
                else throw new Exception("FARMACEUTA NO EXISTE");
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    public void update(Farmaceuta e) throws Exception {
        ensureConnected();
        try {
            synchronized (commLock) {
                os.writeInt(Protocol.FARMACEUTA_UPDATE);
                os.writeObject(e);
                os.flush();
                if (is.readInt() != Protocol.ERROR_NO_ERROR) throw new Exception("FARMACEUTA NO EXISTE");
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    public void delete(Farmaceuta e) throws Exception {
        ensureConnected();
        try {
            synchronized (commLock) {
                os.writeInt(Protocol.FARMACEUTA_DELETE);
                os.writeObject(e);
                os.flush();
                if (is.readInt() != Protocol.ERROR_NO_ERROR) throw new Exception("FARMACEUTA NO EXISTE");
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    public List<Farmaceuta> search(Farmaceuta e) {
        try {
            ensureConnected();
            synchronized (commLock) {
                os.writeInt(Protocol.FARMACEUTA_SEARCH);
                os.writeObject(e);
                os.flush();
                if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                    return (List<Farmaceuta>) is.readObject();
                } else return List.of();
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    // ============ LINEA ==============
    public void create(Linea l, String idReceta) throws Exception {
        ensureConnected();
        if (idReceta == null || idReceta.isEmpty()) {
            throw new Exception("La línea debe pertenecer a una receta existente");
        }
        try {
            synchronized (commLock) {
                os.writeInt(Protocol.LINEA_CREATE);
                Object[] payload = new Object[]{l, idReceta};
                os.writeObject(payload);
                os.flush();
                int status = is.readInt();
                if (status != Protocol.ERROR_NO_ERROR) throw new Exception("ERROR AL CREAR LÍNEA");
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    public Linea read(Linea e) throws Exception {
        ensureConnected();
        try {
            synchronized (commLock) {
                os.writeInt(Protocol.LINEA_READ);
                os.writeObject(e);
                os.flush();
                if (is.readInt() == Protocol.ERROR_NO_ERROR) return (Linea) is.readObject();
                else throw new Exception("LINEA NO EXISTE");
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    public void delete(Linea e) throws Exception {
        ensureConnected();
        try {
            synchronized (commLock) {
                os.writeInt(Protocol.LINEA_DELETE);
                os.writeObject(e);
                os.flush();
                if (is.readInt() != Protocol.ERROR_NO_ERROR) throw new Exception("LINEA NO EXISTE");
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    public List<Linea> search(Linea e) {
        try {
            ensureConnected();
            synchronized (commLock) {
                os.writeInt(Protocol.LINEA_SEARCH);
                os.writeObject(e);
                os.flush();
                if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                    return (List<Linea>) is.readObject();
                } else return List.of();
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    public void update(Linea e) throws Exception {
        ensureConnected();
        try {
            synchronized (commLock) {
                os.writeInt(Protocol.LINEA_UPDATE);
                os.writeObject(e);
                os.flush();
                if (is.readInt() != Protocol.ERROR_NO_ERROR) throw new Exception("LINEA NO EXISTE");
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    // =========== RECETAS ==============
    public void create(Receta e) throws Exception {
        ensureConnected();
        try {
            synchronized (commLock) {
                os.writeInt(Protocol.RECETA_CREATE);
                os.writeObject(e);
                os.flush();
                if (is.readInt() != Protocol.ERROR_NO_ERROR) throw new Exception("RECETA DUPLICADA");
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    public Receta read(Receta e) throws Exception {
        ensureConnected();
        try {
            synchronized (commLock) {
                os.writeInt(Protocol.RECETA_READ);
                os.writeObject(e);
                os.flush();
                if (is.readInt() == Protocol.ERROR_NO_ERROR) return (Receta) is.readObject();
                else throw new Exception("RECETA NO EXISTE");
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    public void update(Receta e) throws Exception {
        ensureConnected();
        try {
            synchronized (commLock) {
                os.writeInt(Protocol.RECETA_UPDATE);
                os.writeObject(e);
                os.flush();
                if (is.readInt() != Protocol.ERROR_NO_ERROR) throw new Exception("RECETA NO EXISTE");
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    public List<Receta> findAllRecetas() {
        try {
            ensureConnected();
            synchronized (commLock) {
                os.writeInt(Protocol.RECETA_FIND_ALL);
                os.flush();
                if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                    return (List<Receta>) is.readObject();
                } else {
                    return new ArrayList<>();
                }
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    public void delete(Receta e) throws Exception {
        ensureConnected();
        try {
            synchronized (commLock) {
                os.writeInt(Protocol.RECETA_DELETE);
                os.writeObject(e);
                os.flush();
                if (is.readInt() != Protocol.ERROR_NO_ERROR) throw new Exception("RECETA NO EXISTE");
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    public List<Receta> search(Receta e) {
        try {
            ensureConnected();
            synchronized (commLock) {
                os.writeInt(Protocol.RECETA_SEARCH);
                os.writeObject(e);
                os.flush();
                int status = is.readInt();
                if (status == Protocol.ERROR_NO_ERROR) {
                    return (List<Receta>) is.readObject();
                } else {
                    return List.of();
                }
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    // ============ USUARIO ============
    public void create(Usuario e) throws Exception {
        ensureConnected();
        try {
            synchronized (commLock) {
                os.writeInt(Protocol.USUARIO_CREATE);
                os.writeObject(e);
                os.flush();
                if (is.readInt() != Protocol.ERROR_NO_ERROR) throw new Exception("USUARIO DUPLICADO");
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    public Usuario read(Usuario e) throws Exception {
        ensureConnected();
        try {
            synchronized (commLock) {
                os.writeInt(Protocol.USUARIO_READ);
                os.writeObject(e);
                os.flush();
                if (is.readInt() == Protocol.ERROR_NO_ERROR) return (Usuario) is.readObject();
                else throw new Exception("USUARIO NO EXISTE");
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    public void update(Usuario e) throws Exception {
        ensureConnected();
        try {
            synchronized (commLock) {
                os.writeInt(Protocol.USUARIO_UPDATE);
                os.writeObject(e);
                os.flush();
                if (is.readInt() != Protocol.ERROR_NO_ERROR) throw new Exception("USUARIO NO EXISTE");
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    public List<Usuario> findAllUsuarios() {
        try {
            ensureConnected();
            synchronized (commLock) {
                os.writeInt(Protocol.USUARIO_FIND_ALL);
                os.flush();
                if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                    return (List<Usuario>) is.readObject();
                } else {
                    return new ArrayList<>();
                }
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    public void delete(Usuario e) throws Exception {
        ensureConnected();
        try {
            synchronized (commLock) {
                os.writeInt(Protocol.USUARIO_DELETE);
                os.writeObject(e);
                os.flush();
                if (is.readInt() != Protocol.ERROR_NO_ERROR) throw new Exception("USUARIO NO EXISTE");
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }

    public List<Usuario> search(Usuario e) {
        try {
            ensureConnected();
            synchronized (commLock) {
                os.writeInt(Protocol.USUARIO_SEARCH);
                os.writeObject(e);
                os.flush();
                if (is.readInt() == Protocol.ERROR_NO_ERROR) {
                    return (List<Usuario>) is.readObject();
                } else return List.of();
            }
        } catch (Exception ex) {
            throw wrapSocketException(ex);
        }
    }
    public ObjectOutputStream getOutputStream() { return os; }

    public String getCurrentUserId() {
        return Sesion.getUsuario() != null ? Sesion.getUsuario().getId() : "anon";
    }
}