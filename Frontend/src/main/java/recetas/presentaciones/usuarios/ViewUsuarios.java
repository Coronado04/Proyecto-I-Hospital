package recetas.presentaciones.usuarios;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ViewUsuarios implements PropertyChangeListener {
    private JPanel panelExterno;
    private JPanel panelInterno;
    private JTable table1;
    private JButton enviar;
    private JButton recibir;
    private Controller controller;
    private Model model;

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
        enviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    TableModel tm = (TableModel) table1.getModel();
                    boolean[] seleccionado = tm.getSeleccionado();

                    for (int i = 0; i < seleccionado.length; i++) {
                        if (seleccionado[i]) controller.enviar(i);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panelExterno, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        recibir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(table1.getSelectedRow()!=-1)
                        controller.recibir(table1.getSelectedRow());
                    else
                        throw new Exception("Debe seleccionar un usuario.");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panelExterno, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        int[] cols = {TableModel.ID, TableModel.MENSAJE};
        table1.setModel(new TableModel(cols, model.getList()));
        table1.setRowHeight(30);
        this.panelExterno.revalidate();
    }
}
