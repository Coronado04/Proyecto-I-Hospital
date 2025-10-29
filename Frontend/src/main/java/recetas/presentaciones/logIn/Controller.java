package recetas.presentaciones.logIn;

import progra3.logic.Protocol;
import progra3.logic.Usuario;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Controller {
    private Model model;
    private ViewlogIn view;

    private ObjectOutputStream os;
    private ObjectInputStream is;

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

            // 🔹 Enviar solicitud de login al servidor
            os.writeInt(Protocol.USUARIO_LOGIN);
            os.writeObject(user);
            os.writeObject(pass);
            os.flush();

            // 🔹 Leer respuesta
            int status = is.readInt();
            if (status == Protocol.ERROR_NO_ERROR) {
                Usuario u = (Usuario) is.readObject();
                model.setUsuario(u);

                JOptionPane.showMessageDialog(view,
                        "Bienvenido " + u.getNombre() + " (" + u.getRol() + ")");
                view.dispose();
            } else {
                throw new Exception("Usuario o clave incorrectos.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

            // 🔹 Enviar solicitud al servidor
            os.writeInt(Protocol.USUARIO_CAMBIAR_CLAVE);
            os.writeObject(user);
            os.writeObject(oldPass);
            os.writeObject(newPass);
            os.flush();

            int status = is.readInt();
            if (status == Protocol.ERROR_NO_ERROR) {
                JOptionPane.showMessageDialog(view, "Clave cambiada con éxito.");
            } else {
                throw new Exception("Usuario o clave incorrectos o error al cambiar la clave.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error al cambiar la clave: " + ex.getMessage());
        }
    }

}


