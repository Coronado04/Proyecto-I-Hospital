package recetas.presentaciones.dashboard;

import org.jfree.chart.labels.StandardPieSectionLabelGenerator;

import progra3.logic.Linea;
import progra3.logic.Medicamento;
import progra3.logic.Receta;
import recetas.logic.Service;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;


import javax.swing.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.plot.PiePlot;

public class Controller {
    private Model model;
    private ViewDashboard viewDashboard;

    public Controller(ViewDashboard viewDashboard, Model model) {
        model.init(Service.instance().findAllMedicamentos());
        this.viewDashboard = viewDashboard;
        this.model = model;
        viewDashboard.setController(this);
        viewDashboard.setModel(model);
    }

    public void add(Medicamento m) throws Exception {
        for(Medicamento med : model.getMedicamentos())
            if(Objects.equals(med.getCodigo(), m.getCodigo()))
                throw new Exception("Medicamento ya existente");
        model.getMedicamentos().add(m);
        List<String> vector = new ArrayList<>();
        Collections.addAll(vector, model.getFilas());
        vector.add(m.getNombre());
        String[] nuevo = new String[vector.size()];
        nuevo = vector.toArray(nuevo);
        model.setFilas(nuevo);
        actualizarDatos();
    }

    public void addAll() throws Exception {
        model.setMedicamentos(new ArrayList<>(model.getList()));
        List<String> vector = new ArrayList<>();

        for (Medicamento med : model.getMedicamentos()) {
            vector.add(med.getNombre());
        }

        String[] nuevo = new String[vector.size()];
        nuevo = vector.toArray(nuevo);
        model.setFilas(nuevo);
        actualizarDatos();

    }

    public void delete(String nom) throws Exception {
        for(Medicamento med : model.getMedicamentos())
            if(Objects.equals(med.getNombre(), nom)){
                model.getMedicamentos().remove(med);
                List<String> vector = new ArrayList<>();
                Collections.addAll(vector, model.getFilas());
                vector.remove(med.getNombre());
                String[] nuevo = new String[vector.size()];
                nuevo = vector.toArray(nuevo);
                model.setFilas(nuevo);
                actualizarDatos();
                return;
            }
    }

    public void deleteAll(){
        model.setMedicamentos(new ArrayList<>());
        model.setFilas(new String[]{});
        actualizarDatos();
    }

    public void actualizarComboBoxes(String anioDes, String anioHas, int mesDes, int mesHas) {
        setAnnioDesde(anioDes);
        setAnioHasta(anioHas);
        setMesDesde(mesDes);
        setMesHasta(mesHas);
    }

    public void setAnnioDesde(String anioDes) {
        model.getRango().setAnioInicio(Integer.parseInt(anioDes));
    }

    public void setAnioHasta(String anioHas) {
        model.getRango().setAnioFin(Integer.parseInt(anioHas));
    }

    public void setMesDesde(int mesDes) {
        model.getRango().setMesInicio(mesDes);
    }

    public void setMesHasta(int mesHas) {
        model.getRango().setMesFin(mesHas);
    }

    public void actualizaColumnas(){
        List<String> vector = new ArrayList<>();
        int anioAux = model.getRango().getAnioInicio(), mesAux = model.getRango().getMesInicio(),
                anioFin =  model.getRango().getAnioFin(), mesFin =  model.getRango().getMesFin();

        while(true){
            if(mesAux == mesFin && anioAux == anioFin)
                break;

            vector.add(""+anioAux+'-'+mesAux);
            mesAux++;
            if(mesAux == 13){
                anioAux++;
                mesAux = 1;
            }
        }
        vector.add(""+anioFin+'-'+mesFin);
        String[] nuevo = new String[vector.size()];
        nuevo = vector.toArray(nuevo);
        model.setColumnas(nuevo);
        actualizarDatos();
    }

    public void actualizarDatos(){
        int anio;
        int mes;
        String[] medicamentos = model.getFilas();
        int[][] matriz = new int[medicamentos.length][model.getColumnas().length];

        for(int i = 0; i < medicamentos.length; i++){
            int contColumnas = 0;
            anio = model.getRango().getAnioInicio();
            mes = model.getRango().getMesInicio();

            while(anio <= model.getRango().getAnioFin()){
                int sumTotal = 0;

                for(Receta receta : Service.instance().search(new Receta())){
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate fecha = receta.getFechaConfeccion();

                    if(fecha.getMonthValue() == mes && fecha.getYear() == anio){
                        for (Linea l : receta.getDetalles()){
                            if (Objects.equals(l.getMedicamento().getNombre(), medicamentos[i])){
                                sumTotal += l.getCantidad();
                            }
                        }
                    }
                }


                matriz[i][contColumnas++] = sumTotal;

                mes++;
                if(mes == 13){
                    mes = 1;
                    anio++;
                }
                if(contColumnas >= model.getColumnas().length){
                    break;
                }
            }
        }


        model.setDatos(matriz);

    }
    public void actualizarGraficoRecetas() {

        int anioInicio = model.getRango().getAnioInicio();
        int mesInicio = model.getRango().getMesInicio();
        int anioFin = model.getRango().getAnioFin();
        int mesFin = model.getRango().getMesFin();

        // Contar las recetas por estado
        Map<Receta.Estado, Integer> estadoCount = new HashMap<>();
        int totalRecetas = 0;

        for (Receta.Estado estado : Receta.Estado.values()) {
            estadoCount.put(estado, 0);
        }

        for (Receta receta : Service.instance().findAllRecetas()) {
            LocalDate fecha = receta.getFechaConfeccion();
            if ((fecha.getYear() > anioInicio || (fecha.getYear() == anioInicio && fecha.getMonthValue() >= mesInicio))
                    && (fecha.getYear() < anioFin || (fecha.getYear() == anioFin && fecha.getMonthValue() <= mesFin))) {
                estadoCount.put(receta.getEstado(), estadoCount.get(receta.getEstado()) + 1);
                totalRecetas++;
            }
        }

        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<Receta.Estado, Integer> entry : estadoCount.entrySet()) {
            int cantidad = entry.getValue();
            double porcentaje = (totalRecetas > 0) ? (cantidad * 100.0) / totalRecetas : 0;

            String etiqueta = entry.getKey().toString() + "=" + cantidad + " (" + String.format("%.2f", porcentaje) + "%)";
            dataset.setValue(entry.getKey().toString(), cantidad);
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Estados de Recetas",
                dataset,
                true,
                true,
                false
        );

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint(Receta.Estado.CONFECCIONADA.ordinal(), Color.RED);
        plot.setSectionPaint(Receta.Estado.PROCESO.ordinal(), Color.YELLOW);
        plot.setSectionPaint(Receta.Estado.LISTA.ordinal(), Color.ORANGE);
        plot.setSectionPaint(Receta.Estado.ENTREGADA.ordinal(), Color.GREEN);

        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}={1} ({2})"));

        ChartPanel chartPanel = new ChartPanel(chart);
        viewDashboard.getGraficoRecetas().removeAll();
        viewDashboard.getGraficoRecetas().setLayout(new BorderLayout());
        viewDashboard.getGraficoRecetas().add(chartPanel, BorderLayout.CENTER);
        viewDashboard.getGraficoRecetas().revalidate();
        viewDashboard.getGraficoRecetas().repaint();
    }

}
