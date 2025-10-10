package recetas.presentaciones.Preescribir;

import recetas.logic.Medicamento;
import recetas.logic.Paciente;
import recetas.presentaciones.medicamentos.TableModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumnModel;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class ViewAgregaMedicamento extends JDialog implements PropertyChangeListener {
    private JComboBox filtro;
    private JTextField dato;
    private JTable table1;
    private JButton cancelarButton;
    private JButton okButton;
    private JPanel panel1;
    private ViewLineaDetalle viewLineaDetalle;

    private Model model;
    private Controller controller;

    public JPanel getPanel() {
        return panel1;
    }

    public ViewAgregaMedicamento(Model model, Controller controller) {
        super();
        this.model = model;
        this.controller = controller;
        filtro.removeAllItems();
        filtro.addItem("Codigo");
        filtro.addItem("Descripcion");
        filtro.setSelectedIndex(0);

        this.model.addPropertyChangeListener(this);

        setContentPane(panel1);
        setModal(true);
        getRootPane().setDefaultButton(okButton);

        actualizarLista();

        okButton.addActionListener(e -> {
            try {
                int pos = table1.getSelectedRow();
                if (pos == -1) throw new Exception("Debe seleccionar un medicamento");

                int modelRow = table1.convertRowIndexToModel(pos); // si hay sorter en la tabla
                Medicamento seleccionado = model.getMedicamentos().get(modelRow);


                controller.setCurrentMedicamento(seleccionado);

                viewLineaDetalle = new ViewLineaDetalle(model, controller, seleccionado);
                viewLineaDetalle.setTitle(seleccionado.getNombre() != null ? seleccionado.getNombre() : "Detalle medicamento");
                viewLineaDetalle.pack();
                viewLineaDetalle.setLocationRelativeTo(panel1);
                viewLineaDetalle.setVisible(true);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel1, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });


        cancelarButton.addActionListener(e -> onCancel());

        dato.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { updateMedicamentoList(); }
            @Override
            public void removeUpdate(DocumentEvent e) { updateMedicamentoList(); }
            @Override
            public void changedUpdate(DocumentEvent e) { }

            private void updateMedicamentoList() {
                try {
                    Medicamento Filtro = new Medicamento();
                    String texto = dato.getText().trim();
                    String opcion = (filtro.getSelectedItem() == null) ? "Codigo" : filtro.getSelectedItem().toString();

                    if ("Codigo".equalsIgnoreCase(opcion)) {
                        Filtro.setCodigo(texto);
                        Filtro.setPresentacion("");
                    } else {
                        Filtro.setPresentacion(texto);
                        Filtro.setCodigo("");
                    }

                    controller.searchMedicamento(Filtro);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel1, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { onCancel(); }
        });

        panel1.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Model.LISTMEDICAMENTO.equals(evt.getPropertyName())) {
            actualizarLista();
        }
    }

    private void actualizarLista() {
        int[] cols = {
                recetas.presentaciones.medicamentos.TableModel.CODIGO,
                recetas.presentaciones.medicamentos.TableModel.NOMBRE,
                recetas.presentaciones.medicamentos.TableModel.PRESENTACION,

        };
        List<Medicamento> medicamentos= model.getMedicamentos();
        table1.setModel(new TableModel(cols, medicamentos));
        table1.setRowHeight(30);

        TableColumnModel columnModel = table1.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(170);
        columnModel.getColumn(2).setPreferredWidth(120);

    }

    private void onCancel() {
        try {
            controller.searchMedicamento(new Medicamento());
        } catch (Exception ignored) { }
        dispose();
    }
}
