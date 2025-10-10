package recetas.presentaciones.logIn;

import javax.swing.*;
import java.awt.*;

public class ViewlogIn extends JDialog {
    private JPanel panel1;
    private JLabel Id;
    private JLabel Clave;
    private JTextField IdUsuario;
    private JPasswordField Contrasena;
    private JButton Ingresar;
    private JButton Cancelar;
    private JButton Cambiar;

    public ViewlogIn(Frame parent) {
        super(parent, "Login", true); // modal
        setContentPane(panel1);
        setSize(400, 200);
        setLocationRelativeTo(parent);
    }

    // Getters para el controlador
    public JTextField getIdUsuario() {
        return IdUsuario;
    }

    public JPasswordField getContraseña() {
        return Contrasena;
    }

    public JButton getIngresar() {
        return Ingresar;
    }

    public JButton getCancelar() {
        return Cancelar;
    }

    public JButton getCambiar() {
        return Cambiar;
    }
}

