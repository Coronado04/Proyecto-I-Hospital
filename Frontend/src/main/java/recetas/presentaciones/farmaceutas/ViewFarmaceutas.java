package recetas.presentaciones.farmaceutas;

import progra3.logic.Farmaceuta;
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

public class ViewFarmaceutas implements PropertyChangeListener {
    private JPanel farmaceuta;
    private JPanel Busqueda;
    private JPanel Listado;
    private JTextField IdFar;
    private JTextField NombreBusqueda;
    private JButton Buscar;
    private JButton reporte;
    private JTable table1;
    private JButton Guardar;
    private JButton Limpiar;
    private JButton Borrar;
    private JLabel Nombre;
    private JTextField NombreFar;
    private JPanel panel;
    private JTextField idBusqueda;

    private boolean validate() {
        boolean valid = true;
        if (IdFar.getText().isEmpty()) {
            valid = false;
            IdFar.setBackground(Application.BACKGROUND_ERROR);
            IdFar.setToolTipText("Codigo requerido");
        }else {
            IdFar.setBackground(Color.WHITE);
            IdFar.setToolTipText(null);
        }
        if (NombreFar.getText().isEmpty()) {
            valid = false;
            NombreFar.setBackground(Application.BACKGROUND_ERROR);
            NombreFar.setToolTipText("Nombre requerido");
        }else{
            NombreFar.setBackground(Color.WHITE);
            NombreFar.setToolTipText(null);
        }
        return valid;
    }

    public Farmaceuta take() {
        Farmaceuta e = new Farmaceuta();
        e.setId(IdFar.getText());
        e.setClave(IdFar.getText());
        e.setNombre(NombreFar.getText());
        return e;
    }

    public JPanel getPanel() {
        return panel;
    }

    private Controller controller;
    private Model model;

    public void setController(Controller controller) {this.controller = controller;}

    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }
    public ViewFarmaceutas(){

        Guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validate()) {
                    Farmaceuta n = take();
                    try {
                        controller.save(n);  // nuevo
                        JOptionPane.showMessageDialog(panel, "REGISTRO CREADO", "", JOptionPane.INFORMATION_MESSAGE);

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panel, "ID EXISTENTE", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        Buscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nom = NombreBusqueda.getText().trim();
                    String id = idBusqueda.getText().trim();

                    Farmaceuta filter = new Farmaceuta();
                    filter.setId(id);
                    filter.setNombre(nom);

                    controller.search(filter);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(),
                            "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });


        Limpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.clear();
            }
        });
        Borrar.addActionListener(new ActionListener() {
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
        Buscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Farmaceuta filter = new Farmaceuta();
                    filter.setId(NombreBusqueda.getText().trim());
                    filter.setNombre(NombreBusqueda.getText().trim());
                    controller.search(filter);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(),
                            "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });


        reporte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    controller.print();
                    if(Desktop.isDesktopSupported()){
                        File file = new File("farmaceutas.pdf");
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
                int[] cols = {TableModel.ID, TableModel.NOMBRE};
                table1.setModel(new TableModel(cols,model.getListaFarmaceutas()));
                break;
            case Model.CURRENT:
                IdFar.setText(model.getCurrent().getId());
                NombreFar.setText(model.getCurrent().getNombre());
                //EspecialidadFar.setText(model.getCurrent().getEspecialidad());

                if (model.getMode() == Application.MODE_EDIT) {
                    IdFar.setEnabled(false);
                    Borrar.setEnabled(true);

                } else {
                    IdFar.setEnabled(true);
                    Borrar.setEnabled(false);
                    table1.clearSelection();
                }

                IdFar.setBackground(null);
                IdFar.setToolTipText(null);
                NombreFar.setBackground(null);
                NombreFar.setToolTipText(null);

                break;
            case Model.FILTER:
                NombreFar.setText(model.getFilter().getNombre());
                break;


        }
        this.panel.revalidate();
    }

}
