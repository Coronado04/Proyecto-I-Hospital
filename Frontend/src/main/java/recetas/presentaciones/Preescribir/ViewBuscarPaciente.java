package recetas.presentaciones.Preescribir;

import progra3.logic.Paciente;
import recetas.presentaciones.paciente.TableModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumnModel;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class ViewBuscarPaciente extends JDialog implements PropertyChangeListener {

    private JComboBox Filtro;
    private JTextField dato;
    private JTable table1;
    private JButton okButton;
    private JButton cancelarButton;
    private JPanel panel1;

    private Model model;
    private Controller controller;

    public JPanel getPanel() {
        return panel1;
    }

    public ViewBuscarPaciente(Model model, Controller controller) {
        super();
        this.model = model;
        this.controller = controller;

        Filtro.removeAllItems();
        Filtro.addItem("Nombre");
        Filtro.addItem("Id");
        Filtro.setSelectedIndex(0);

        this.model.addPropertyChangeListener(this);

        setContentPane(panel1);
        setModal(true);
        getRootPane().setDefaultButton(okButton);

        actualizarLista();

        okButton.addActionListener(e -> onOK());
        cancelarButton.addActionListener(e -> onCancel());

        dato.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { updatePacienteList(); }
            @Override
            public void removeUpdate(DocumentEvent e) { updatePacienteList(); }
            @Override
            public void changedUpdate(DocumentEvent e) { }

            private void updatePacienteList() {
                try {
                    Paciente filtro = new Paciente();
                    String texto = dato.getText().trim();
                    String opcion = (Filtro.getSelectedItem() == null) ? "Nombre" : Filtro.getSelectedItem().toString();

                    if ("Id".equalsIgnoreCase(opcion)) {
                        filtro.setId(texto);
                        filtro.setNombre("");
                    } else {
                        filtro.setNombre(texto);
                        filtro.setId("");
                    }

                    controller.searchPaciente(filtro);
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
        if (Model.LISTPACIENTE.equals(evt.getPropertyName())) {
            actualizarLista();
        }
    }

    private void actualizarLista() {
        int[] cols = {
                TableModel.ID,
                TableModel.NOMBRE,
                TableModel.FechaDeNacimiento,
                TableModel.Telefono
        };
        List<Paciente> pacientes = model.getPacientes();
        table1.setModel(new TableModel(cols, pacientes));
        table1.setRowHeight(30);

        TableColumnModel columnModel = table1.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(170);
        columnModel.getColumn(2).setPreferredWidth(120);
        columnModel.getColumn(3).setPreferredWidth(110);
    }

    private void onOK() {
        try {
            int pos = table1.getSelectedRow();
            if (pos == -1) throw new Exception("Debe seleccionar un paciente");

            Paciente seleccionado = model.getPacientes().get(pos);
            controller.setCurrentPaciente(seleccionado);
            model.updateModel();

            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel1, e.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void onCancel() {
        try {
            controller.searchPaciente(new Paciente());
        } catch (Exception ignored) { }
        dispose();
    }
}