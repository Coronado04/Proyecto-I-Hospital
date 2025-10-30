package recetas.presentaciones.paciente;

import com.github.lgooddatepicker.components.DatePicker;
import progra3.logic.Paciente;
import recetas.Application;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

public class ViewPaciente implements PropertyChangeListener {
    private JPanel panel;
    private JTextField idPaciente;
    private DatePicker FechaNacimiento;
    private JTextField NombrePaciente;
    private JTextField TelefonoPaciente;
    private JButton guardar;
    private JButton borrar;
    private JButton limpiar;
    private JTextField NombreBusqueda;
    private JButton buscar;
    private JButton reporte;
    private JTable table1;
    private JTextField idBusqueda;

    private boolean validate() {
        boolean valid = true;
        if (idPaciente.getText().isEmpty()) {
            valid = false;
            idPaciente.setBackground(Application.BACKGROUND_ERROR);
            idPaciente.setToolTipText("Codigo requerido");
        }else {
            idPaciente.setBackground(Color.WHITE);
            idPaciente.setToolTipText(null);
        }
        if (NombrePaciente.getText().isEmpty()) {
            valid = false;
            NombrePaciente.setBackground(Application.BACKGROUND_ERROR);
            NombrePaciente.setToolTipText("Nombre requerido");
        }else{
            NombrePaciente.setBackground(Color.WHITE);
            NombrePaciente.setToolTipText(null);
        }
        if(TelefonoPaciente.getText().isEmpty()) {
            valid = false;
            TelefonoPaciente.setBackground(Application.BACKGROUND_ERROR);
            TelefonoPaciente.setToolTipText("Telefono requerido");
        }else{
            TelefonoPaciente.setBackground(Color.WHITE);
            TelefonoPaciente.setToolTipText(null);
        }
        if(FechaNacimiento.getText().isEmpty()) {
            valid = false;
            FechaNacimiento.setBackground(Application.BACKGROUND_ERROR);
            FechaNacimiento.setToolTipText("Fecha requerido");
        }
        else{
            FechaNacimiento.setBackground(Color.WHITE);
            FechaNacimiento.setToolTipText(null);
        }
        return valid;
    }
    public Paciente take() {
        Paciente e = new Paciente();
        e.setId(idPaciente.getText());
        e.setNumero(TelefonoPaciente.getText());
        e.setNombre(NombrePaciente.getText());
        e.setFechaNacimiento(FechaNacimiento.getDate());
        return e;
    }

    public JPanel getPanel() {
        return panel;
    }
    private Controller controller;
    private Model model;

    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public ViewPaciente() {
        guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validate()) {
                    Paciente n = take();
                    try {
                        controller.save(n);
                        JOptionPane.showMessageDialog(panel, "REGISTRO APLICADO", "", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panel, "ID EXISTENTE", "Error", JOptionPane.ERROR_MESSAGE);
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
                try{
                    controller.delete();
                    JOptionPane.showMessageDialog(panel, "REGISTRO BORRADO", "", JOptionPane.INFORMATION_MESSAGE);
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        buscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    Paciente filter = new Paciente();
                    filter.setNombre(NombreBusqueda.getText());
                    filter.setId(idBusqueda.getText());
                    controller.search(filter);
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        reporte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    controller.print();
                    if(Desktop.isDesktopSupported()){
                        File file = new File("pacientes.pdf");
                        Desktop.getDesktop().open(file);
                    }
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Model.LIST:
                int[] cols = {TableModel.ID, TableModel.NOMBRE,
                        TableModel.FechaDeNacimiento, TableModel.Telefono};
                table1.setModel(new TableModel(cols,model.getListaPaciente()));
                break;
            case Model.CURRENT:
                idPaciente.setText(model.getCurrent().getId());
                NombrePaciente.setText(model.getCurrent().getNombre());
                FechaNacimiento.setDate(model.getCurrent().getFechaNacimiento());
                TelefonoPaciente.setText(model.getCurrent().getNumero());

                if (model.getMode() == Application.MODE_EDIT) {
                    idPaciente.setEnabled(false);
                    borrar.setEnabled(true);
                } else {
                    idPaciente.setEnabled(true);
                    borrar.setEnabled(false);
                    table1.clearSelection();
                }

                idPaciente.setBackground(null);
                idPaciente.setToolTipText(null);
                NombrePaciente.setBackground(null);
                NombrePaciente.setToolTipText(null);
                FechaNacimiento.setBackground(null);
                FechaNacimiento.setToolTipText(null);
                TelefonoPaciente.setBackground(null);
                TelefonoPaciente.setToolTipText(null);
                break;
            case Model.FILTER:
                NombrePaciente.setText(model.getFilter().getNombre());
                break;
        }
        this.panel.revalidate();
    }



}
