package recetas.presentaciones.Preescribir;

import com.github.lgooddatepicker.components.DatePicker;
import recetas.Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ViewPreescribir implements PropertyChangeListener {
    private JPanel panel;
    private JButton buscaPaciente;
    private JButton agregarMedicamento;
    private JTable list;
    private JButton guardar;
    private JButton limpiar;
    private JButton descartaMedicamento;
    private JButton detalles;
    private JLabel Paciente;
    private DatePicker FechaRetiro;
    private ViewBuscarPaciente viewBuscarPaciente;
    private ViewAgregaMedicamento viewAgregaMedicamento;
    private ViewLineaDetalle viewLineaDetalle;

    private Controller controller;
    private Model model;

    public JPanel getPanel() {
        return panel;
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

    public ViewPreescribir() {
        buscaPaciente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    viewBuscarPaciente = new ViewBuscarPaciente(model, controller);
                    viewBuscarPaciente.setTitle("Buscar Paciente");
                    viewBuscarPaciente.pack();
                    viewBuscarPaciente.setLocationRelativeTo(panel);
                    viewBuscarPaciente.setVisible(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        agregarMedicamento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    viewAgregaMedicamento = new ViewAgregaMedicamento(model, controller);
                    viewAgregaMedicamento.setTitle("Agregar Medicamento");
                    viewAgregaMedicamento.pack();
                    viewAgregaMedicamento.setLocationRelativeTo(panel);
                    viewAgregaMedicamento.setVisible(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.setCurrentLinea(list.getSelectedRow());
            }
        });

        descartaMedicamento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    controller.delete();
                    if(controller.getCurrentLinea()==null)
                        JOptionPane.showMessageDialog(panel, "Medicamento no seleccionado", "",  JOptionPane.INFORMATION_MESSAGE);
                    else
                        JOptionPane.showMessageDialog(panel, "Medicamento descartado", "",  JOptionPane.INFORMATION_MESSAGE);
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error",  JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        limpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.clearReceta();
            }
        });

        detalles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(list.getSelectedRow()!=-1){
                        viewLineaDetalle = new ViewLineaDetalle(model, controller, model.getCurrentLinea());
                        viewLineaDetalle.setTitle(controller.getCurrentLinea().getMedicamento().getNombre());
                        viewLineaDetalle.pack();
                        viewLineaDetalle.setVisible(true);
                    }
                    else
                        throw new Exception("Debe seleccionar un medicamento.");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(validate()) {
                        controller.saveReceta(FechaRetiro.getDate());
                        JOptionPane.showMessageDialog(panel,
                                "Receta agregada exitosamente",
                                "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel,
                            ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });


    }

    private boolean validate() {
        boolean valid = true;
        if (FechaRetiro.getDate()==null) {
            valid = false;
            FechaRetiro.setBackground(Application.BACKGROUND_ERROR);
            FechaRetiro.setToolTipText("Fecha requerida");
        }else {
            FechaRetiro.setBackground(Color.WHITE);
            FechaRetiro.setToolTipText(null);
        }
        if (model.getCurrentPaciente()==null) {
            valid = false;
            Paciente.setBackground(Application.BACKGROUND_ERROR);
            Paciente.setToolTipText("Paciente requerido");
        }else{
            Paciente.setBackground(Color.WHITE);
            Paciente.setToolTipText(null);
        }
        if(model.getCurrentReceta().getDetalles().isEmpty()) {
            valid = false;
            JOptionPane.showMessageDialog(panel,
                    "Ingrese al menos un medicamento",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }else{

        }
        return valid;
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        switch (evt.getPropertyName()) {
            case Model.LISTLINEA:
                int[] cols ={TableModel.MEDICAMENTO,TableModel.PRESENTACION,TableModel.CANTIDAD,TableModel.INDICACIONES,TableModel.DURACION};
                list.setModel(new TableModel(cols, model.getCurrentReceta().getDetalles()));
                break;
            case Model.CURRENTPACIENTE:
                if (model.getCurrentPaciente() != null) {
                    Paciente.setText(model.getCurrentPaciente().getNombre());
                } else {
                    Paciente.setText("Seleccione un paciente");
                }

        }

        this.panel.revalidate();
        this.panel.repaint();

    }
}