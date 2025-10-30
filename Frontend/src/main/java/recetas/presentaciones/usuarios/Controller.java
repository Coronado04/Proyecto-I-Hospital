package recetas.presentaciones.usuarios;

import progra3.logic.Usuario;
import recetas.logic.Service;
import recetas.logic.SocketListener;
import recetas.presentaciones.UsuariosThreadListener;

import javax.swing.*;
import java.io.ObjectOutputStream;

public class Controller implements UsuariosThreadListener {
    private ViewUsuarios view;
    private Model model;
    private SocketListener socketListener;

    public Controller(ViewUsuarios view, Model model) {
        model.init();
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);

        try {
            // Service.instance().getSid() debe existir (ver Service más abajo)
            socketListener = new SocketListener(this, Service.instance().getSid());
            socketListener.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void refresh() {}

    @Override
    public void stop() {}

    @Override
    public void deliver_Login(String idUsuario) {
        Usuario u = new Usuario();
        u.setId(idUsuario);
        model.agregarUsuario(u);
    }

    @Override
    public void deliver_Logout(String idUsuario) {
        Usuario u = new Usuario();
        u.setId(idUsuario);
        model.quitarUsuario(u);
    }

    @Override
    public void deliver_Mensaje(String origen, String mensaje) {
        JOptionPane.showMessageDialog(view.getPanelExterno(),
                "Mensaje de " + origen + ":\n" + mensaje,
                "Nuevo mensaje", JOptionPane.INFORMATION_MESSAGE);
    }

    // ---- Métodos para botones (NO @Override) ----
    public void enviar(int row) throws Exception {
        Usuario destino = model.getList().get(row);
        String mensaje = JOptionPane.showInputDialog(view.getPanelExterno(),
                "Mensaje para " + destino.getId() + ":");
        if (mensaje == null || mensaje.trim().isEmpty()) return;

        ObjectOutputStream os = Service.instance().getOutputStream();
        if (os == null) throw new Exception("No conectado al servidor.");

        synchronized (Service.instance()) {
            os.writeInt(progra3.logic.Protocol.USUARIO_MENSAJE);
            os.writeObject(Service.instance().getCurrentUserId());
            os.writeObject(destino.getId());
            os.writeObject(mensaje);
            os.flush();
        }
    }

    public void recibir(int row) throws Exception {
        JOptionPane.showMessageDialog(view.getPanelExterno(),
                "Este botón puede servir para futuras funciones (historial, etc).",
                "Recibir", JOptionPane.INFORMATION_MESSAGE);
    }
}


