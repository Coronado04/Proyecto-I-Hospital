package recetas.presentaciones.medicos;
import recetas.Application;
import recetas.logic.Medico;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

public class ViewMedicos implements PropertyChangeListener {
    private JPanel panel;
    private JTextField IdMedico;
    private JTextField EspecialidadMedico;
    private JTextField NombreMedico;
    private JButton guardar;
    private JButton borrar;
    private JButton limpiar;
    private JTextField NombreBusqueda;
    private JButton buscar;
    private JButton reporte;
    private JTable table1;
    private JLabel idLabel;
    private JLabel EspecialidadLabel;
    private JLabel NombreLabel;
    private JTextField idBusqueda;

    private boolean validate() {
        boolean valid = true;
        if (IdMedico.getText().isEmpty()) {
            valid = false;
            IdMedico.setBackground(Application.BACKGROUND_ERROR);
            IdMedico.setToolTipText("Codigo requerido");
        }else {
            IdMedico.setBackground(Color.WHITE);
            IdMedico.setToolTipText(null);
        }
        if (NombreMedico.getText().isEmpty()) {
            valid = false;
            NombreMedico.setBackground(Application.BACKGROUND_ERROR);
            NombreMedico.setToolTipText("Nombre requerido");
        }else{
            NombreMedico.setBackground(Color.WHITE);
            NombreMedico.setToolTipText(null);
        }
        if(EspecialidadMedico.getText().isEmpty()) {
            valid = false;
            EspecialidadMedico.setBackground(Application.BACKGROUND_ERROR);
            EspecialidadMedico.setToolTipText("Especialidad requerido");
        }else{
            EspecialidadMedico.setBackground(Color.WHITE);
            EspecialidadMedico.setToolTipText(null);
        }
        return valid;
    }

    public Medico take() {
        Medico e = new Medico();
        e.setId(IdMedico.getText().trim());
        e.setEspecialidad(EspecialidadMedico.getText().trim());
        e.setNombre(NombreMedico.getText().trim());
        e.setClave(IdMedico.getText().trim());
        return e;
    }

    public JPanel getPanel() {
        return panel;
    }

    private Controller controller;
    private Model model;

    void setController(Controller controller) {this.controller = controller;}

    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }
    public ViewMedicos(){

        guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validate()) {
                    Medico n = take();
                    try {
                        controller.save(n);
                        JOptionPane.showMessageDialog(panel, "Datos guardados exitosamente", "", JOptionPane.INFORMATION_MESSAGE);
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
                    Medico filter = new Medico();
                    String nombre = NombreBusqueda.getText();
                    String id = idBusqueda.getText();
                    filter.setNombre(nombre != null ? nombre.trim() : "");
                    filter.setId(id != null ? id.trim() : "");
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
                        File file = new File("medicos.pdf");
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
                int[] cols = {TableModel.ID,TableModel.NOMBRE, TableModel.ESPECIALIDAD};
                table1.setModel(new TableModel(cols,model.getListaMedico()));
                break;
            case Model.CURRENT:
                IdMedico.setText(model.getCurrent().getId());
                NombreMedico.setText(model.getCurrent().getNombre());
                EspecialidadMedico.setText(model.getCurrent().getEspecialidad());

                if (model.getMode() == Application.MODE_EDIT) {
                    IdMedico.setEnabled(false);
                    borrar.setEnabled(true);
                } else {
                    IdMedico.setEnabled(true);
                    borrar.setEnabled(false);
                    table1.clearSelection();
                }

                IdMedico.setBackground(null);
                IdMedico.setToolTipText(null);
                NombreMedico.setBackground(null);
                NombreMedico.setToolTipText(null);
                EspecialidadMedico.setBackground(null);
                EspecialidadMedico.setToolTipText(null);

                break;
            case Model.FILTER:
                buscar.setText(model.getFilter().getNombre());
                break;
        }
        this.panel.revalidate();
    }
}
