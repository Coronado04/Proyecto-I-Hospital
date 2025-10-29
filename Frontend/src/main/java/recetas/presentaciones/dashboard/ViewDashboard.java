package recetas.presentaciones.dashboard;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import progra3.logic.Medicamento;
import recetas.Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class ViewDashboard implements PropertyChangeListener {
    private JComboBox desdeAnio;
    private JComboBox desdeMes;
    private JComboBox hastaAnio;
    private JComboBox hastaMes;
    private JComboBox medicamentos;
    private JButton selectUno;
    private JButton selectAll;
    private JTable list;
    private JButton quitarUno;
    private JButton quitarAll;
    private JLabel desdeLbl;
    private JLabel hastaLbl;
    private JLabel medicamentoLbl;
    private JPanel graficoMedicamentos;
    private JPanel panel;
    private JPanel graficoRecetas;

    public JPanel getPanel() {
        return panel;
    }

    public ViewDashboard() {
        selectUno.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                try{
                    Medicamento med = (Medicamento) medicamentos.getSelectedItem();
                    if(!validate())
                        throw new Exception("Las fechas son incorrectas");
                    controller.add(med);
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Informacion",  JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        selectAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                try{
                    if(!validate())
                        throw new Exception("Las fechas son incorrectas");
                    controller.addAll();
                } catch (Exception ex){
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Informacion",  JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        quitarUno.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                try{
                    if(list.getSelectedRow()==1)
                        throw new Exception("Se debe seleccionar un medicamento");
                    if(!validate())
                        throw new Exception("Las fechas son incorrectas");
                    String v = (String) list.getValueAt(list.getSelectedRow(), 0);
                    controller.delete(v);
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Informacion",  JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        quitarAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(list.getRowCount()==0)
                        throw new Exception("No hay medicamento a eliminar");
                    controller.deleteAll();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        desdeAnio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(validate()){
                        actualizarCB();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        desdeMes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(validate()) {
                        actualizarCB();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        hastaAnio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(validate()) {
                        actualizarCB();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        hastaMes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(validate()) {
                        actualizarCB();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                if(validate())
                    controller.actualizarDatos();
            }
        });


    }

    private Model model;
    private Controller controller;

    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    private boolean validate() {
        boolean valid = true;
        String aux = (String)desdeAnio.getSelectedItem();
        int aInicio = Integer.parseInt(aux);
        aux = (String) hastaAnio.getSelectedItem();
        int aFin = Integer.parseInt(aux);
        int mInicio = desdeMes.getSelectedIndex() + 1;
        int mFin = hastaMes.getSelectedIndex() + 1;

        if (mFin - mInicio + (aFin-aInicio) * 12 + 1 < 1) {
            valid = false;
            desdeLbl.setBorder(Application.BORDER_ERROR);
            desdeLbl.setToolTipText("Fecha mayor a la final");
            hastaLbl.setBorder(Application.BORDER_ERROR);
            hastaLbl.setToolTipText("Fecha menor a la inicial");

            list.setModel(model.getTableModel());
            JFreeChart chart = ChartFactory.createLineChart("Ventas por mes","Mes","Ventas",
                    new DefaultCategoryDataset(), PlotOrientation.VERTICAL,true,true,false);
            ChartPanel chartPanel = new ChartPanel(chart);
            graficoMedicamentos.removeAll();
            graficoMedicamentos.setLayout(new BorderLayout());
            graficoMedicamentos.add(chartPanel);

        } else {
            desdeLbl.setBorder(null);
            desdeLbl.setToolTipText(null);
            hastaLbl.setBorder(null);
            hastaLbl.setToolTipText(null);
        }
        return valid;

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt){
        switch (evt.getPropertyName()){
            case Model.LIST:
                list.setModel(model.getTableModel());
                list.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                list.setRowHeight(30);
                break;

            case Model.MEDICAMENTOS:
                medicamentos.setModel(new DefaultComboBoxModel(model.getList().toArray()));
                break;
            case Model.DATOS:
                list.setModel(model.getTableModel());
                JFreeChart chart = ChartFactory.createLineChart("Medicamentos", "Mes", "Cantidad",
                        model.getDataset(), PlotOrientation.VERTICAL,true,true,false);
                CategoryPlot plot = (CategoryPlot) chart.getPlot();
                LineAndShapeRenderer renderer = new LineAndShapeRenderer(true, true);
                plot.setRenderer(renderer);
                ChartPanel chartPanel = new ChartPanel(chart);
                graficoMedicamentos.removeAll();
                graficoMedicamentos.setLayout(new BorderLayout());
                graficoMedicamentos.add(chartPanel);
                controller.actualizarGraficoRecetas();

                break;

        }
        this.panel.revalidate();
    }
    public void actualizarCB(){
        String anioD = (String)desdeAnio.getSelectedItem();
        String anioH = (String)hastaAnio.getSelectedItem();
        int mesD = desdeMes.getSelectedIndex()+1;
        int mesH = hastaMes.getSelectedIndex()+1;
        controller.actualizarComboBoxes(anioD,anioH,mesD,mesH);
        controller.actualizaColumnas();
    }
    public JPanel getGraficoRecetas() {
        return graficoRecetas;
    }

}
