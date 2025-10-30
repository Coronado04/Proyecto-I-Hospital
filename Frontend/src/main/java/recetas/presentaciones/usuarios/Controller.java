package recetas.presentaciones.usuarios;

import progra3.logic.Usuario;
import recetas.logic.Service;
import recetas.logic.SocketListener;
import recetas.presentaciones.UsuariosThreadListener;

import javax.swing.*;
import java.util.*;

/**
 * Controller de Usuarios:
 * - mantiene pendingMessages (origin -> list of messages)
 * - mantiene pendingIds (set de remitentes con mensajes pendientes) para que la vista marque filas
 * - al llegar deliver_Mensaje() guarda el mensaje pendiente y marca la fila; NO muestra diálogo inmediato
 * - al pulsar "Recibir" se muestran los mensajes pendientes para la fila seleccionada
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

        // Inyectar el supplier para que la vista pueda pintar filas con mensajes pendientes
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
        // limpiar pendientes de logout
        pendingMessages.remove(idUsuario);
        pendingIds.remove(idUsuario);
        view.repaintTable();
    }

    @Override
    public void deliver_Mensaje(String origen, String mensaje) {
        // Guardar mensaje en la cola de pendientes y marcar remitente
        pendingMessages.computeIfAbsent(origen, k -> new ArrayList<>()).add(mensaje);
        pendingIds.add(origen);

        // Asegurarnos de que el remitente aparece en la lista de usuarios
        boolean existe = model.getList().stream().anyMatch(u -> u.getId() != null && u.getId().equals(origen));
        if (!existe) {
            Usuario u = new Usuario();
            u.setId(origen);
            model.agregarUsuario(u);
        }

        // Pedir a la vista que repinte para mostrar la marca visual
        view.repaintTable();
        // NO mostrar diálogo aquí: el usuario debe seleccionar la fila y pulsar "Recibir"
    }

    // ---- Métodos para botones (NO @Override) ----
    public void enviar(int row) throws Exception {
        Usuario destino = model.getList().get(row);
        String mensaje = JOptionPane.showInputDialog(view.getPanelExterno(),
                "Mensaje para " + destino.getId() + ":");
        if (mensaje == null || mensaje.trim().isEmpty()) return;

        // Usar API de Service que sincroniza el stream correctamente
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

        // quitar la marca visual y refrescar la vista
        pendingIds.remove(id);
        view.repaintTable();
    }
}