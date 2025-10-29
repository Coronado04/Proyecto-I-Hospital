package recetas.presentaciones.Despacho;


import progra3.logic.Paciente;
import progra3.logic.Receta;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ViewDespacho implements PropertyChangeListener {
    private JTextField BuscarIdText;
    private JTable table1;
    private JButton proceso;
    private JButton alistar;
    private JButton entregar;
    private JPanel panel1;
    private Model model;
    private Controller controller;

    public JPanel getPanel1() {
        return panel1;
    }

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

    public ViewDespacho() {
        BuscarIdText.getDocument().addDocumentListener(new DocumentListener() {
        @Override
         public void insertUpdate(DocumentEvent e) { updatePacienteList(); }
         @Override
       public void removeUpdate(DocumentEvent e) { updatePacienteList(); }
         @Override
         public void changedUpdate(DocumentEvent e) { }
         private void updatePacienteList() {
            try {
                String idPaciente = BuscarIdText.getText().trim();
                Receta filter = new Receta();

                if (!idPaciente.isEmpty()) {
                    Paciente p = new Paciente();
                    p.setId(idPaciente);
                    filter.setPaciente(p);
                }
                controller.searchId(filter);




            } catch(Exception ex) {
                JOptionPane.showMessageDialog(panel1, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
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
        proceso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.marcarEnProceso();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel1, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        alistar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.marcarLista();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel1, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        entregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.marcarEntregada();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel1, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.LIST:
                int[] cols = {
                        TableModel.IDRECETA,
                        TableModel.IDPACIENTE,
                        TableModel.NOMBREPACIENTE,
                        TableModel.FECHARETIRO,
                        TableModel.ESTADO
                };
                table1.setModel(new TableModel(cols, model.getListaRecetas()));
                break;

            case Model.CURRENT:
                Receta r = model.getCurrentReceta();
                if (r == null) {
                    proceso.setEnabled(false);
                    alistar.setEnabled(false);
                    entregar.setEnabled(false);
                } else {
                    proceso.setEnabled(r.getEstado() == Receta.Estado.CONFECCIONADA);
                    alistar.setEnabled(r.getEstado() == Receta.Estado.PROCESO);
                    entregar.setEnabled(r.getEstado() == Receta.Estado.LISTA);
                }
                break;
        }
        panel1.revalidate();
        panel1.repaint();
    }

}
