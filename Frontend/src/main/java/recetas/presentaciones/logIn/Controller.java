package recetas.presentaciones.logIn;

import progra3.logic.Protocol;
import progra3.logic.Usuario;
import recetas.Sesion;
import recetas.logic.Service;

import javax.swing.*;
import java.awt.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.InetSocketAddress;

/**
 * Controller de login con timeouts para evitar bloqueo indefinido en la UI.
 */
public class Controller {
    private Model model;
    private ViewlogIn view;

    private ObjectOutputStream os;
    private ObjectInputStream is;

    // Timeouts (ms)
    private static final int TIMEOUT_CONNECT_MS = 5000;
    private static final int TIMEOUT_READ_MS = 7000;

    public void setStreams(ObjectOutputStream os, ObjectInputStream is) {
        this.os = os;
        this.is = is;
    }

    public Controller(Model model, ViewlogIn view) {
        this.model = model;
        this.view = view;
        initController();
    }

    private void initController() {
        view.getIngresar().addActionListener(e -> login());
        view.getCancelar().addActionListener(e -> System.exit(0));
        view.getCambiar().addActionListener(e -> cambiarClave());
    }

    // ==========================================================
    // LOGIN
    // ==========================================================
    private void login() {
        try {
            String user = view.getIdUsuario().getText().trim();
            String pass = new String(view.getContraseña().getPassword()).trim();

            if (user.isEmpty() || pass.isEmpty()) {
                throw new Exception("Debe ingresar usuario y contraseña.");
            }

            // Cambiar cursor a espera
            view.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            SwingWorker<Usuario, Void> worker = new SwingWorker<>() {
                private Socket socket = null;

                @Override
                protected Usuario doInBackground() throws Exception {
                    try {
                        // Si no hay streams todavía, crear la conexión ahora (esto se hace en background)
                        if (os == null || is == null) {
                            socket = new Socket();
                            socket.connect(new InetSocketAddress(Protocol.SERVER, Protocol.PORT), TIMEOUT_CONNECT_MS);
                            // establecer timeout de lectura para evitar bloqueos indefinidos en is.read*
                            socket.setSoTimeout(TIMEOUT_READ_MS);

                            os = new ObjectOutputStream(socket.getOutputStream());
                            os.flush(); // importante
                            is = new ObjectInputStream(socket.getInputStream());
                        } else {
                            // Si ya existían streams, no tenemos el socket para setSoTimeout;
                            // en ese caso no podemos garantizar timeout de lectura aquí.
                        }

                        // Enviar solicitud de login
                        os.writeInt(Protocol.USUARIO_LOGIN);
                        os.writeObject(user);
                        os.writeObject(pass);
                        os.flush();

                        // Leer respuesta (bloqueante pero con timeout si socket fue creado aquí)
                        int status = is.readInt();
                        if (status == Protocol.ERROR_NO_ERROR) {
                            Usuario u = (Usuario) is.readObject();
                            return u;
                        } else {
                            throw new Exception("Usuario o clave incorrectos.");
                        }
                    } catch (java.net.SocketTimeoutException ste) {
                        throw new Exception("Tiempo de espera agotado al comunicarse con el servidor.");
                    } catch (Exception ex) {
                        // cerrar socket si lo abrimos aquí
                        try {
                            if (socket != null && !socket.isClosed()) socket.close();
                        } catch (Exception ignore) {}
                        throw ex;
                    }
                }

                @Override
                protected void done() {
                    try {
                        // Restaurar cursor
                        view.setCursor(Cursor.getDefaultCursor());

                        Usuario u = get(); // lanza si doInBackground lanzó excepción

                        // Éxito: actualizar modelo y sesión y pasar streams al Service
                        model.setUsuario(u);
                        Sesion.setUsuario(u);
                        Service.instance().setStreams(os, is);

                        JOptionPane.showMessageDialog(view,
                                "Bienvenido " + u.getNombre() + " (" + u.getRol() + ")");

                        view.dispose();
                    } catch (Exception ex) {
                        // Excepciones vienen aquí (errores de conexión, auth, etc.)
                        Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                        String msg = cause.getMessage() != null ? cause.getMessage() : "Error durante el login";
                        JOptionPane.showMessageDialog(view, msg, "Error", JOptionPane.ERROR_MESSAGE);
                        // Asegurarse de restaurar cursor
                        view.setCursor(Cursor.getDefaultCursor());
                    }
                }
            };

            worker.execute();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            view.setCursor(Cursor.getDefaultCursor());
        }
    }

    // ==========================================================
    // CAMBIO DE CLAVE
    // ==========================================================
    private void cambiarClave() {
        try {
            String user = JOptionPane.showInputDialog(view, "Ingrese su usuario:");
            if (user == null || user.isEmpty()) return;

            String oldPass = JOptionPane.showInputDialog(view, "Ingrese su clave actual:");
            if (oldPass == null || oldPass.isEmpty()) return;

            String newPass = JOptionPane.showInputDialog(view, "Ingrese la nueva clave:");
            if (newPass == null || newPass.isEmpty()) return;

            view.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                private Socket socket = null;

                @Override
                protected Void doInBackground() throws Exception {
                    try {
                        if (os == null || is == null) {
                            socket = new Socket();
                            socket.connect(new InetSocketAddress(Protocol.SERVER, Protocol.PORT), TIMEOUT_CONNECT_MS);
                            socket.setSoTimeout(TIMEOUT_READ_MS);
                            os = new ObjectOutputStream(socket.getOutputStream());
                            os.flush();
                            is = new ObjectInputStream(socket.getInputStream());
                        }

                        os.writeInt(Protocol.USUARIO_CAMBIAR_CLAVE);
                        os.writeObject(user);
                        os.writeObject(oldPass);
                        os.writeObject(newPass);
                        os.flush();

                        int status = is.readInt();
                        if (status == Protocol.ERROR_NO_ERROR) {
                            return null;
                        } else {
                            throw new Exception("Usuario o clave incorrectos o error al cambiar la clave.");
                        }
                    } catch (java.net.SocketTimeoutException ste) {
                        throw new Exception("Tiempo de espera agotado al comunicarse con el servidor.");
                    } catch (Exception ex) {
                        try {
                            if (socket != null && !socket.isClosed()) socket.close();
                        } catch (Exception ignore) {}
                        throw ex;
                    }
                }

                @Override
                protected void done() {
                    view.setCursor(Cursor.getDefaultCursor());
                    try {
                        get();
                        JOptionPane.showMessageDialog(view, "Clave cambiada con éxito.");
                    } catch (Exception ex) {
                        Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                        JOptionPane.showMessageDialog(view, "Error al cambiar la clave: " + cause.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            };

            worker.execute();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error al cambiar la clave: " + ex.getMessage());
            view.setCursor(Cursor.getDefaultCursor());
        }
    }
}