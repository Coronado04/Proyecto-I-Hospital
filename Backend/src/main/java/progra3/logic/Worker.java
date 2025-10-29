package progra3.logic;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

/**
 * Worker servidor: atiende las peticiones del cliente siguiendo el protocolo.
 *
 * Versión corregida: eliminado el catch(ClassNotFoundException) redundante que
 * producía el error de compilación "Exception 'ClassNotFoundException' is never thrown".
 *
 * Comportamiento:
 * - crea ObjectOutputStream primero y flush() para evitar deadlocks.
 * - responde con ERROR ante fallos por operación y continua atendiendo.
 * - en errores fatales (EOF/SocketException/Throwable fuera del switch) cierra la conexión.
 */
public class Worker implements Runnable {
    private final Server srv;
    private final Socket s;
    private final Service service;
    private ObjectOutputStream os;
    private ObjectInputStream is;

    private volatile boolean continuar = false;
    private Thread thread;

    public Worker(Server srv, Socket s, Service service) {
        this.srv = srv;
        this.s = s;
        this.service = service;

        try {
            // Crear ObjectOutputStream primero y flush() para evitar deadlocks.
            this.os = new ObjectOutputStream(s.getOutputStream());
            this.os.flush();
            this.is = new ObjectInputStream(s.getInputStream());
        } catch (IOException ex) {
            System.err.println("Worker: error creando streams: " + ex.getMessage());
            ex.printStackTrace();
            // No dejamos el worker en estado inconsistente: cerrar recursos y salir.
            closeSilently();
        }
    }

    public synchronized void start() {
        if (continuar) return;
        if (s == null || s.isClosed()) return;
        continuar = true;
        thread = new Thread(this, "Worker-" + s.getRemoteSocketAddress());
        thread.setDaemon(true);
        thread.start();
        System.out.println("Worker atendiendo peticiones...");
    }

    public synchronized void stop() {
        if (!continuar) return;
        continuar = false;
        closeSilently();
        System.out.println("Conexion cerrada...");
    }

    @Override
    public void run() {
        if (is == null || os == null) return;

        try {
            while (continuar && !s.isClosed()) {
                int method;
                try {
                    method = is.readInt();
                } catch (EOFException eof) {
                    // cliente cerró conexión de forma limpia -> salir
                    System.out.println("Cliente cerró la conexión: " + s.getRemoteSocketAddress());
                    break;
                } catch (SocketException se) {
                    System.out.println("Socket cerrado: " + se.getMessage());
                    break;
                }

                System.out.println("Operacion: " + method);

                try {
                    switch (method) {
                        //----MEDICO-----
                        case Protocol.MEDICO_CREATE:
                            try {
                                service.create((Medico) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        case Protocol.MEDICO_READ:
                            try {
                                Medico e = service.read((Medico) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(e);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        case Protocol.MEDICO_UPDATE:
                            try {
                                service.update((Medico) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        case Protocol.MEDICO_DELETE:
                            try {
                                service.delete((Medico) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        case Protocol.MEDICO_SEARCH:
                            try {
                                List<Medico> le = service.search((Medico) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(le);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        case Protocol.MEDICO_FIND_ALL:
                            try {
                                List<Medico> le = service.findAll();
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(le);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        //----PACIENTE-----
                        case Protocol.PACIENTE_CREATE:
                            try {
                                service.create((Paciente) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        case Protocol.PACIENTE_READ:
                            try {
                                Paciente e = service.read((Paciente) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(e);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        case Protocol.PACIENTE_UPDATE:
                            try {
                                service.update((Paciente) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        case Protocol.PACIENTE_DELETE:
                            try {
                                service.delete((Paciente) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        case Protocol.PACIENTE_SEARCH:
                            try {
                                List<Paciente> le = service.search((Paciente) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(le);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        case Protocol.PACIENTE_FIND_ALL:
                            try {
                                List<Paciente> le = service.findAllPaciente();
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(le);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        //----MEDICAMENTO-----
                        case Protocol.MEDICAMENTO_CREATE:
                            try {
                                service.create((Medicamento) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        case Protocol.MEDICAMENTO_READ:
                            try {
                                Medicamento e = service.read((Medicamento) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(e);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        case Protocol.MEDICAMENTO_UPDATE:
                            try {
                                service.update((Medicamento) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        case Protocol.MEDICAMENTO_DELETE:
                            try {
                                service.delete((Medicamento) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        case Protocol.MEDICAMENTO_SEARCH:
                            try {
                                List<Medicamento> le = service.search((Medicamento) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(le);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        case Protocol.MEDICAMENTO_FIND_ALL:
                            try {
                                List<Medicamento> le = service.findAllMedicamentos();
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(le);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        //----FARMACEUTA-----
                        case Protocol.FARMACEUTA_CREATE:
                            try {
                                service.create((Farmaceuta) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        case Protocol.FARMACEUTA_READ:
                            try {
                                Farmaceuta e = service.read((Farmaceuta) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(e);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        case Protocol.FARMACEUTA_UPDATE:
                            try {
                                service.update((Farmaceuta) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        case Protocol.FARMACEUTA_DELETE:
                            try {
                                service.delete((Farmaceuta) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        case Protocol.FARMACEUTA_SEARCH:
                            try {
                                List<Farmaceuta> le = service.search((Farmaceuta) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(le);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        //----LINEA-----
                        case Protocol.LINEA_CREATE:
                            try {
                                Object[] data = (Object[]) is.readObject();
                                if (data == null || data.length < 2) throw new IllegalArgumentException("Payload LINEA_CREATE inválido");
                                Linea linea = (Linea) data[0];
                                String idReceta = (String) data[1];
                                service.create(linea, idReceta);
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        case Protocol.LINEA_READ:
                            try {
                                Linea e = service.read((Linea) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(e);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        case Protocol.LINEA_UPDATE:
                            try {
                                service.update((Linea) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        case Protocol.LINEA_DELETE:
                            try {
                                service.delete((Linea) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        case Protocol.LINEA_SEARCH:
                            try {
                                List<Linea> le = service.search((Linea) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(le);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        //------RECETA-----
                        case Protocol.RECETA_CREATE:
                            try {
                                service.create((Receta) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        case Protocol.RECETA_READ:
                            try {
                                Receta e = service.read((Receta) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(e);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        case Protocol.RECETA_UPDATE:
                            try {
                                service.update((Receta) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        case Protocol.RECETA_DELETE:
                            try {
                                service.delete((Receta) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        case Protocol.RECETA_SEARCH:
                            try {
                                List<Receta> le = service.search((Receta) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(le);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        case Protocol.RECETA_FIND_ALL:
                            try {
                                List<Receta> le = service.findAllRecetas();
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(le);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        case Protocol.USUARIO_CREATE:
                            try {
                                service.create((Usuario) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        case Protocol.USUARIO_READ:
                            try {
                                Usuario e = service.read((Usuario) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(e);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        case Protocol.USUARIO_UPDATE:
                            try {
                                service.update((Usuario) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        case Protocol.USUARIO_DELETE:
                            try {
                                service.delete((Usuario) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        case Protocol.USUARIO_SEARCH:
                            try {
                                List<Usuario> le = service.search((Usuario) is.readObject());
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(le);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        case Protocol.USUARIO_FIND_ALL:
                            try {
                                List<Usuario> le = service.findAllUsuarios();
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(le);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;

                        case Protocol.USUARIO_LOGIN: {
                            try {
                                String id = (String) is.readObject();
                                String clave = (String) is.readObject();
                                Usuario u = Sesion.login(id, clave);
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.writeObject(u);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;
                        }

                        case Protocol.USUARIO_CAMBIAR_CLAVE: {
                            try {
                                String id = (String) is.readObject();
                                String oldClave = (String) is.readObject();
                                String nuevaClave = (String) is.readObject();
                                // validar credenciales actuales antes de actualizar
                                Sesion.login(id, oldClave);
                                Sesion.actualizarClave(id, nuevaClave);
                                os.writeInt(Protocol.ERROR_NO_ERROR);
                                os.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                safeWriteErrorAndFlush();
                            }
                            break;
                        }

                        case Protocol.DISCONNECT:
                            // cliente solicita desconexión
                            stop();
                            srv.remove(this);
                            break;

                        default:
                            // opcode desconocido
                            os.writeInt(Protocol.ERROR_ERROR);
                            os.flush();
                            break;
                    } // fin switch

                } catch (Exception outerEx) {
                    // Capturamos excepciones de más alto nivel que puedan surgir
                    outerEx.printStackTrace();
                    safeWriteErrorAndFlush();
                    // en este caso sí cerramos para evitar bucle infinito en fallos graves
                    System.out.println("Error crítico en Worker, cerrando conexión con " + s.getRemoteSocketAddress());
                    stop();
                    srv.remove(this);
                    break;
                }
            } // fin while
        } catch (Throwable t) {
            // Capturamos cualquier throwable no manejado para evitar que escape del hilo
            t.printStackTrace();
        } finally {
            closeSilently();
        }
    }

    /**
     * Intenta escribir el código de error y hacer flush; ignora excepciones al intentar notificar.
     */
    private void safeWriteErrorAndFlush() {
        try {
            if (os != null) {
                os.writeInt(Protocol.ERROR_ERROR);
                os.flush();
            }
        } catch (IOException ignore) { /* no podemos hacer más */ }
    }

    private void closeSilently() {
        try {
            if (is != null) {
                try { is.close(); } catch (IOException ignore) {}
                is = null;
            }
            if (os != null) {
                try { os.close(); } catch (IOException ignore) {}
                os = null;
            }
            if (s != null && !s.isClosed()) {
                try { s.close(); } catch (IOException ignore) {}
            }
        } catch (Exception ignore) {
        }
    }
}