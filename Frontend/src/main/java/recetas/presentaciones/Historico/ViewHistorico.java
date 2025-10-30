package recetas.presentaciones.Historico;


import progra3.logic.Paciente;
import progra3.logic.Receta;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ViewHistorico implements PropertyChangeListener {
    private JTextField IDPacienteText;
    private JButton buscar;
    private JTable table1;
    private JButton mostrarDetalles;
    private JPanel panel;
    private Model model;
    private Controller controller;

    public Model getModel() {
        return model;
    }
    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }
    public Controller getController() {
        return controller;
    }
    public void setController(Controller controller) {
        this.controller = controller;
    }
    public JPanel getPanel() {
        return panel;
    }
    public ViewHistorico() {
        buscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String texto = IDPacienteText.getText().trim();
                    Receta filter = new Receta();
                    if (!texto.isEmpty()) {
                        Paciente p = new Paciente();
                        p.setId(texto);
                        filter.setPaciente(p);
                    }

                    controller.search(filter);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(),
                            "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });



        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table1.getSelectedRow();
                controller.edit(row);
            }
        });
        mostrarDetalles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (model.getCurrent() == null) throw new Exception("Seleccione una receta");

                    int[] cols = {
                            DetalleTableModel.MEDICAMENTO,
                            DetalleTableModel.PRESENTACION,
                            DetalleTableModel.CANTIDAD,
                            DetalleTableModel.INDICACIONES,
                            DetalleTableModel.DURACION
                    };

                    DetalleTableModel tableModel = new DetalleTableModel(cols, model.getCurrent().getDetalles());
                    JTable detallesTable = new JTable(tableModel);
                    detallesTable.setRowHeight(25);

                    JScrollPane scroll = new JScrollPane(detallesTable);
                    scroll.setPreferredSize(new java.awt.Dimension(600, 200));

                    JOptionPane.showMessageDialog(
                            panel,
                            scroll,
                            "Detalles de la receta " + model.getCurrent().getIdReceta(),
                            JOptionPane.INFORMATION_MESSAGE
                    );

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(),
                            "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });


    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.LIST:
                int[] cols = {TableModel.IDRECETA,TableModel.IDPACIENTE,TableModel.NOMBREPACIENTE,TableModel.NOMBREMEDICO,TableModel.ESTADO};
                table1.setModel(new TableModel(cols, model.getListaReceta()));
                break;
        }
        panel.revalidate();
        panel.repaint();
    }
}
