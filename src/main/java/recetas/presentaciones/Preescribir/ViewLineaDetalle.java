package recetas.presentaciones.Preescribir;

import recetas.Application;
import recetas.logic.Linea;
import recetas.logic.Medicamento;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ViewLineaDetalle extends JDialog implements PropertyChangeListener {
    private JPanel panel1;
    private JLabel Cantidad;
    private JSpinner spinnerCantidad;
    private JButton Guardar;
    private JLabel Duracion;
    private JSpinner spinnerDuracion;
    private JButton Cancelar;
    private JLabel Indicaciones;
    private JTextArea TextIndica;

    private Model model;
    private Controller controller;
    private Medicamento medicamento;

    public ViewLineaDetalle(Model model, Controller controller, Medicamento medicamento) {
        super();
        this.model = model;
        this.controller = controller;
        this.medicamento = medicamento;


        model.addPropertyChangeListener(this);

        initComponents();


        if (medicamento != null) {
            setTitle(medicamento.getNombre() == null ? "Detalle medicamento" : medicamento.getNombre());
            model.setCurrentMedicamento(medicamento);
        }
    }



    private Linea linea;

    public ViewLineaDetalle(Model model, Controller controller, Linea linea) {
        super();
        this.model = model;
        this.controller = controller;
        this.linea = linea;
        this.medicamento = linea.getMedicamento();

        model.addPropertyChangeListener(this);
        initComponents();

        spinnerCantidad.setValue(linea.getCantidad());
        spinnerDuracion.setValue(linea.getDuracionDias());
        TextIndica.setText(linea.getIndicaciones());


        setTitle(medicamento.getNombre());
    }





    private void initComponents() {

        spinnerCantidad.setModel(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
        spinnerDuracion.setModel(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
        TextIndica.setLineWrap(true);

        Guardar.addActionListener(e -> onGuardar());
        Cancelar.addActionListener(e -> onCancel());

        setContentPane(panel1);
        setModal(true);
        getRootPane().setDefaultButton(Guardar);


        panel1.registerKeyboardAction(ev -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onGuardar() {
        try {
            int cantidad = ((Number) spinnerCantidad.getValue()).intValue();
            int dias = ((Number) spinnerDuracion.getValue()).intValue();
            String indicaciones = TextIndica.getText().trim();
            if (indicaciones.isEmpty()) indicaciones = "Tomar según indicación médica";

            if (linea != null) {

                linea.setCantidad(cantidad);
                linea.setDuracionDias(dias);
                linea.setIndicaciones(indicaciones);
                model.updateModel();
            } else {

                Medicamento med = model.getCurrentMedicamento() != null ? model.getCurrentMedicamento() : this.medicamento;
                if (med == null) throw new Exception("No hay medicamento seleccionado.");
                controller.addMedicamento(med, cantidad, indicaciones, dias);
            }



            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panel1, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void onCancel() {
        dispose();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
