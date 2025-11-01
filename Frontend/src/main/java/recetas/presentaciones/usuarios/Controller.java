package recetas.presentaciones.usuarios;

import progra3.logic.Usuario;
import recetas.logic.Service;
import recetas.logic.SocketListener;
import recetas.presentaciones.UsuariosThreadListener;

import javax.swing.*;
import java.util.*;

public class Controller implements UsuariosThreadListener {
    private ViewUsuarios view;
    private Model model;
    private SocketListener socketListener;

 private final Map<String, List<String>> pendingMessages = new HashMap<>();
   private final Set<String> pendingIds = new HashSet<>();

    public Controller(ViewUsuarios view, Model model) {
        model.init();
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);

       view.setPendingIdsSupplier(() -> Collections.unmodifiableSet(pendingIds));

        try {
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
        boolean existe = model.getList().stream().anyMatch(u -> u.getId() != null && u.getId().equals(idUsuario));
        if (!existe) {
            Usuario u = new Usuario();
            u.setId(idUsuario);
            model.agregarUsuario(u);
        }
    }

    @Override
    public void deliver_Logout(String idUsuario) {
        model.getList().removeIf(u -> u.getId() != null && u.getId().equals(idUsuario));
        model.setList(model.getList());
       pendingMessages.remove(idUsuario);
        pendingIds.remove(idUsuario);
        view.repaintTable();
    }

    @Override
    public void deliver_Mensaje(String origen, String mensaje) {
        pendingMessages.computeIfAbsent(origen, k -> new ArrayList<>()).add(mensaje);
        pendingIds.add(origen);

         boolean existe = model.getList().stream().anyMatch(u -> u.getId() != null && u.getId().equals(origen));
        if (!existe) {
            Usuario u = new Usuario();
            u.setId(origen);
            model.agregarUsuario(u);
        }

       view.repaintTable();
    }

    public void enviar(int row) throws Exception {
        Usuario destino = model.getList().get(row);
        String mensaje = JOptionPane.showInputDialog(view.getPanelExterno(),
                "Mensaje para " + destino.getId() + ":");
        if (mensaje == null || mensaje.trim().isEmpty()) return;

        Service.instance().sendUserMessage(destino.getId(), mensaje);
    }

    public void recibir(int row) throws Exception {
        if (row < 0 || row >= model.getList().size()) {
            throw new Exception("Debe seleccionar un usuario.");
        }
        Usuario u = model.getList().get(row);
        String id = u.getId();
        List<String> mensajes = pendingMessages.remove(id);
        if (mensajes == null || mensajes.isEmpty()) {
            JOptionPane.showMessageDialog(view.getPanelExterno(),
                    "No hay mensajes pendientes de " + id,
                    "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (String m : mensajes) {
            sb.append(m).append("\n\n");
        }
        JOptionPane.showMessageDialog(view.getPanelExterno(),
                "Mensaje(es) de " + id + ":\n\n" + sb.toString(),
                "Mensajes recibidos", JOptionPane.INFORMATION_MESSAGE);
        pendingIds.remove(id);
        view.repaintTable();
    }
}