package recetas.presentaciones.medicamentos;

import recetas.Application;
import recetas.logic.Medicamento;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

public class ViewMedicamentos implements PropertyChangeListener {
    private JPanel panel;
    private JTextField codigo;
    private JTextField nombre;
    private JTextField presentacion;
    private JButton guardar;
    private JButton limpiar;
    private JButton borrar;
    private JTextField codigoBuscar;
    private JTextField descripcionBuscar;
    private JButton buscar;
    private JButton reporte;
    private JTable table;

    public JPanel getPanel() {
        return panel;
    }

    public ViewMedicamentos() {

        guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validate()) {
                    Medicamento n = take();
                    try {
                        controller.save(n);
                        JOptionPane.showMessageDialog(panel, "Medicamento guardado exitosamente", "", JOptionPane.INFORMATION_MESSAGE);

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        limpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.clear();
            }
        });
        borrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.delete();
                    JOptionPane.showMessageDialog(panel, "REGISTRO BORRADO", "", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                controller.edit(row);
            }
        });
        buscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Medicamento filter = new Medicamento();

                    String codigoTxt = codigoBuscar.getText() != null ? codigoBuscar.getText().trim() : "";
                    String descTxt = descripcionBuscar.getText() != null ? descripcionBuscar.getText().trim() : "";

                    filter.setCodigo(codigoTxt.isEmpty() ? null : codigoTxt);
                    // La búsqueda por "descripcionBuscar" es en realidad por nombre
                    filter.setNombre(descTxt);

                    controller.search(filter);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            panel,
                            ex.getMessage(),
                            "Información",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
        });
        reporte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.print();
                    if (Desktop.isDesktopSupported()) {
                        File file = new File("medicamentos.pdf");
                        Desktop.getDesktop().open(file);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }


    private boolean validate() {
        boolean valid = true;
        if (codigo.getText().isEmpty()) {
            valid = false;
            codigo.setBackground(Application.BACKGROUND_ERROR);
            codigo.setToolTipText("Codigo requerido");
        } else {
            codigo.setBackground(Color.WHITE);
            codigo.setToolTipText(null);
        }
        if (nombre.getText().isEmpty()) {
            valid = false;
            nombre.setBackground(Application.BACKGROUND_ERROR);
            nombre.setToolTipText("Nombre requerido");
        } else {
            nombre.setBackground(Color.WHITE);
            nombre.setToolTipText(null);
        }
        if (presentacion.getText().isEmpty()) {
            valid = false;
            presentacion.setBackground(Application.BACKGROUND_ERROR);
            presentacion.setToolTipText("Presentacion requerida");
        } else {
            presentacion.setBackground(Color.WHITE);
            presentacion.setToolTipText(null);
        }
        return valid;
    }

    public Medicamento take() {
        Medicamento e = new Medicamento();
        e.setCodigo(codigo.getText().trim());
        e.setNombre(nombre.getText().trim());
        e.setPresentacion(presentacion.getText().trim());
        return e;
    }


    private Controller controller;
    private Model model;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.LIST:
                int[] cols = {TableModel.CODIGO, TableModel.NOMBRE, TableModel.PRESENTACION};
                table.setModel(new TableModel(cols, model.getListaMedicamento()));
                break;
            case Model.CURRENT:
                codigo.setText(model.getCurrent().getCodigo());
                nombre.setText(model.getCurrent().getNombre());
                presentacion.setText(model.getCurrent().getPresentacion());

                if (model.getMode() == Application.MODE_EDIT) {
                    codigo.setEnabled(false);
                    borrar.setEnabled(true);
                } else {
                    codigo.setEnabled(true);
                    borrar.setEnabled(false);
                    table.clearSelection();
                }

                codigo.setBackground(null);
                codigo.setToolTipText(null);
                nombre.setBackground(null);
                nombre.setToolTipText(null);
                presentacion.setBackground(null);
                presentacion.setToolTipText(null);
                break;
            case Model.FILTER:
                // mostrar el nombre en el campo de descripción de filtro
                descripcionBuscar.setText(model.getFilter().getNombre() != null ? model.getFilter().getNombre() : "");
                break;
        }
        this.panel.revalidate();
    }

}