package recetas.presentaciones.logIn;

import progra3.logic.Sesion;
import progra3.logic.Usuario;

import javax.swing.*;

public class Controller {
    private Model model;
    private ViewlogIn view;

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


            Usuario u = Sesion.login(user, pass); // ← este método debe hacer la consulta SQL

            model.setUsuario(u);
            Sesion.setUsuario(u);

            JOptionPane.showMessageDialog(view,
                    "Bienvenido " + u.getNombre() + " (" + u.getRol() + ")");

            view.dispose();


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

            // 🔹 Verificar usuario existente
            Usuario u = Sesion.login(user, oldPass);
            if (u == null) {
                JOptionPane.showMessageDialog(view, "Usuario o clave incorrectos.");
                return;
            }

            String newPass = JOptionPane.showInputDialog(view, "Ingrese la nueva clave:");
            if (newPass == null || newPass.isEmpty()) return;


            Sesion.actualizarClave(u.getId(), newPass);

            u.setClave(newPass);
            model.setUsuario(u);
            Sesion.setUsuario(u);

            JOptionPane.showMessageDialog(view, "Clave cambiada con éxito.");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error al cambiar la clave: " + ex.getMessage());
        }
    }
}


