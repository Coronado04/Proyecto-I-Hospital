package recetas.presentaciones.usuarios;

import progra3.logic.Usuario;
import recetas.logic.Service;
import recetas.logic.SocketListener;
import recetas.presentaciones.UsuariosThreadListener;

import javax.swing.*;
import java.io.ObjectOutputStream;
import java.util.*;

/**
 * Controller actualizado para:
 * - recibir notificaciones de mensajes como "pendientes"
 * - marcar visualmente remitentes con mensajes pendientes
 * - permitir al usuario seleccionar el remitente y pulsar "Recibir" para ver el contenido
 */
public class Controller implements UsuariosThreadListener {
    private ViewUsuarios view;
    private Model model;
    private SocketListener socketListener;

    // Mensajes pendientes por remitente (origenId -> lista de mensajes)
    private final Map<String, List<String>> pendingMessages = new HashMap<>();
    // Conjunto de remitentes que tienen mensajes pendientes (para marcar UI)
    private final Set<String> pendingIds = new HashSet<>();

    public Controller(ViewUsuarios view, Model model) {
        model.init();
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);

        // Inyectar la referencia de pendingIds en la vista para que pueda renderizar filas marcadas
        view.setPendingIdsSupplier(() -> Collections.unmodifiableSet(pendingIds));

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
        // Evitar duplicados: si ya existe usuario con ese id, no lo agregamos
        boolean existe = model.getList().stream().anyMatch(u -> u.getId() != null && u.getId().equals(idUsuario));
        if (!existe) {
            Usuario u = new Usuario();
            u.setId(idUsuario);
            model.agregarUsuario(u);
        }
    }

    @Override
    public void deliver_Logout(String idUsuario) {
        // Eliminar usuario de la lista si existe
        model.getList().removeIf(u -> u.getId() != null && u.getId().equals(idUsuario));
        // Notificar cambio
        model.setList(model.getList());

        // limpiar mensajes pendientes de ese usuario (si corresponde)
        pendingMessages.remove(idUsuario);
        pendingIds.remove(idUsuario);
        view.repaintTable();
    }

    @Override
    public void deliver_Mensaje(String origen, String mensaje) {
        // <-- ya estamos en EDT porque SocketListener usa SwingUtilities.invokeLater
        // Guardar mensaje pendiente
        pendingMessages.computeIfAbsent(origen, k -> new ArrayList<>()).add(mensaje);
        pendingIds.add(origen);

        // Asegurarnos de que el remitente figura en la lista de usuarios (si no, añadirlo)
        boolean existe = model.getList().stream().anyMatch(u -> u.getId() != null && u.getId().equals(origen));
        if (!existe) {
            Usuario u = new Usuario();
            u.setId(origen);
            model.agregarUsuario(u);
        }

        // Notificar a la vista para que marque la fila correspondiente
        view.setPendingIdsSupplier(() -> Collections.unmodifiableSet(pendingIds));
        view.repaintTable();
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

        // No mostrar notificación local al remitente (el receptor verá la notificación)
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

        // Mostrar todos los mensajes pendientes en un solo diálogo (puedes adaptar formato)
        StringBuilder sb = new StringBuilder();
        for (String m : mensajes) {
            sb.append(m).append("\n\n");
        }
        JOptionPane.showMessageDialog(view.getPanelExterno(),
                "Mensaje(es) de " + id + ":\n\n" + sb.toString(),
                "Mensajes recibidos", JOptionPane.INFORMATION_MESSAGE);

        // quitar la marca visual y refrescar la vista
        pendingIds.remove(id);
        view.setPendingIdsSupplier(() -> Collections.unmodifiableSet(pendingIds));
        view.repaintTable();
    }
}