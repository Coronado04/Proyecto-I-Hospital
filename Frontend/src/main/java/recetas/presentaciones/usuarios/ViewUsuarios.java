package recetas.presentaciones.usuarios;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;
import java.util.function.Supplier;
import progra3.logic.Usuario;

public class ViewUsuarios implements PropertyChangeListener {
    private JPanel panelExterno;
    private JPanel panelInterno;
    private JTable table1;
    private JButton enviar;
    private JButton recibir;
    private Controller controller;
    private Model model;

     private Supplier<Set<String>> pendingIdsSupplier = () -> java.util.Collections.emptySet();

    public JPanel getPanelExterno() {
        return panelExterno;
    }
    public JPanel getPanelInterno() {
        return panelInterno;
    }
    public void setController(Controller controller) {
        this.controller = controller;
    }
    public Controller getController() {
        return controller;
    }
    public Model getModel() {
        return model;
    }
    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }
    public ViewUsuarios(){
        enviar.addActionListener(e -> {
            try {
                TableModel tm = (TableModel) table1.getModel();
                boolean[] seleccionado = tm.getSeleccionado();

                for (int i = 0; i < seleccionado.length; i++) {
                    if (seleccionado[i]) controller.enviar(i);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panelExterno, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        recibir.addActionListener(e -> {
            try {
                if(table1.getSelectedRow()!=-1)
                    controller.recibir(table1.getSelectedRow());
                else
                    throw new Exception("Debe seleccionar un usuario.");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panelExterno, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        int[] cols = {TableModel.ID, TableModel.MENSAJE};
        table1.setModel(new TableModel(cols, model.getList()));
        table1.setRowHeight(30);

        table1.getColumnModel().getColumn(0).setCellRenderer(new PendingRenderer());
        this.panelExterno.revalidate();
    }


    public void setPendingIdsSupplier(Supplier<Set<String>> supplier) {
        this.pendingIdsSupplier = supplier != null ? supplier : () -> java.util.Collections.emptySet();
        if (table1 != null) {
            table1.getColumnModel().getColumn(0).setCellRenderer(new PendingRenderer());
            table1.repaint();
        }
    }

    public void repaintTable() {
        if (table1 != null) table1.repaint();
    }

    private class PendingRenderer extends DefaultTableCellRenderer {
        private final Color PENDING_BG = new Color(173, 216, 230); // light blue

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            try {
                Object idVal = table.getValueAt(row, 0);
                String id = idVal != null ? idVal.toString() : null;
                Set<String> pending = pendingIdsSupplier.get();
                if (id != null && pending != null && pending.contains(id)) {
                    c.setBackground(isSelected ? c.getBackground() : PENDING_BG);
                } else {
                    if (isSelected) {
                        c.setBackground(table.getSelectionBackground());
                    } else {
                        c.setBackground(table.getBackground());
                    }
                }
            } catch (Exception ex) {
              if (!isSelected) c.setBackground(table.getBackground());
            }
            return c;
        }
    }
}