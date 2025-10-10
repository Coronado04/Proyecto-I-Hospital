package recetas.presentaciones.logIn;

import recetas.logic.Sesion;
import recetas.logic.Usuario;
import recetas.logic.Medico;
import recetas.logic.Farmaceuta;

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

    private void login() {

        try {
            String user = view.getIdUsuario().getText();
            String pass = new String(view.getContraseña().getPassword());

            Usuario u = Sesion.login(user, pass);

            model.setUsuario(u);
            Sesion.setUsuario(u);

            JOptionPane.showMessageDialog(view,
                    "Bienvenido " + u.getNombre() + " (" + u.getRol() + ")");


            view.dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cambiarClave() {
        try {
            String user = JOptionPane.showInputDialog(view, "Ingrese su usuario:");
            if (user == null || user.isEmpty()) {
                return;
            }

            String oldPass = JOptionPane.showInputDialog(view, "Ingrese su clave actual:");
            if (oldPass == null || oldPass.isEmpty()) {
                return;
            }


            Data data = XmlPersister.instance().load();


            if (data.getAdmin().getId().equalsIgnoreCase(user) && data.getAdmin().getClave().equals(oldPass)) {
                String newPass = JOptionPane.showInputDialog(view, "Ingrese la nueva clave:");
                if (newPass != null && !newPass.isEmpty()) {
                    data.getAdmin().setClave(newPass); // actualizar en memoria

                    // 🔹 Guardar cambios en el XML
                    XmlPersister.instance().store(data);

                    Usuario u = new Usuario(data.getAdmin().getNombre(), data.getAdmin().getId(), data.getAdmin().getClave(), "admin");
                    model.setUsuario(u);
                    Sesion.setUsuario(u);

                    JOptionPane.showMessageDialog(view, "Clave cambiada con éxito.");
                }
                return;
            }


            // ====== Buscar en médicos ======
            for (Medico m : data.getMedicos()) {
                if (m.getId().equalsIgnoreCase(user) && m.getClave().equals(oldPass)) {
                    String newPass = JOptionPane.showInputDialog(view, "Ingrese la nueva clave:");
                    if (newPass != null && !newPass.isEmpty()) {
                        m.setClave(newPass); // actualizar en memoria

                        // 🔹 Guardar cambios en el XML
                        XmlPersister.instance().store(data);

                        Usuario u = new Usuario(m.getNombre(), m.getId(), m.getClave(), "medico");
                        model.setUsuario(u);
                        Sesion.setUsuario(u);

                        JOptionPane.showMessageDialog(view, "Clave cambiada con éxito.");
                    }
                    return;
                }
            }

            // ====== Buscar en farmaceutas ======
            for (Farmaceuta f : data.getFarmaceutas()) {
                if (f.getId().equalsIgnoreCase(user) && f.getClave().equals(oldPass)) {
                    String newPass = JOptionPane.showInputDialog(view, "Ingrese la nueva clave:");
                    if (newPass != null && !newPass.isEmpty()) {
                        f.setClave(newPass); // actualizar en memoria

                        XmlPersister.instance().store(data);

                        Usuario u = new Usuario(f.getNombre(), f.getId(), f.getClave(), "farmaceuta");
                        model.setUsuario(u);
                        Sesion.setUsuario(u);

                        JOptionPane.showMessageDialog(view, "Clave cambiada con éxito.");
                    }
                    return;
                }
            }

            JOptionPane.showMessageDialog(view, "Usuario o clave incorrectos.");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error al cambiar la clave: " + ex.getMessage());
        }
    }



}

