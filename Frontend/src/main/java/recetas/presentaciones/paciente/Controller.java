package recetas.presentaciones.paciente;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import progra3.logic.Paciente;
import recetas.Application;
import recetas.logic.Service;
import recetas.presentaciones.Refresher;
import recetas.presentaciones.ThreadListener;

import java.util.List;

public class Controller implements ThreadListener {
    private ViewPaciente view;
    private Model model;

    Refresher refresher;

    public void search(Paciente filter){
        model.setFilter(filter);
        model.setMode(Application.MODE_CREATE);
        model.setCurrent(new Paciente());
        model.setListaPaciente(Service.instance().search(model.getFilter()));
    }
    public Controller(ViewPaciente view, Model model) {
        model.init(Service.instance().search(new Paciente()));
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);

        refresher = new Refresher(this);
        refresher.start();
    }
    public void create(Paciente e) throws  Exception{
        Service.instance().create(e);
        model.setCurrent(new Paciente());
        model.setListaPaciente(Service.instance().findAllPaciente());
    }

    public void save(Paciente e) throws  Exception{
        switch (model.getMode()){
            case Application.MODE_CREATE:
                Service.instance().create(e);
                break;
            case Application.MODE_EDIT:
                Service.instance().update(e);
        }
        model.setFilter(new Paciente());
        search(model.getFilter());
    }

    public void read(String id) throws Exception {
        Paciente e = new Paciente();
       e.setId(id);
        try {
            model.setCurrent(Service.instance().read(e));
        } catch (Exception ex) {
            Paciente b = new Paciente();
            b.setId(id);
            model.setCurrent(b);
            throw ex;
        }
    }
    public void clear(){
        model.setMode(Application.MODE_CREATE);
        model.setCurrent(new Paciente());
    }
    public void delete()throws Exception{
        Service.instance().delete(model.getCurrent());
        search(model.getFilter());
    }
    public void edit(int row){
        Paciente e = model.getListaPaciente().get(row);
        try {
            model.setMode(Application.MODE_EDIT);
            model.setCurrent(Service.instance().read(e));
        } catch (Exception ex) {}
    }

    public void print() throws Exception{
        if(model.getListaPaciente().isEmpty()){
            throw new Exception("Lista vacía");
        }
        String file = "pacientes.pdf";
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdf = new PdfDocument(writer);

        Document document = new Document(pdf);
        document.setMargins(20, 20, 20, 20);

        Table header = new Table(1);
        header.setWidth(400);
        header.setHorizontalAlignment(HorizontalAlignment.CENTER);
        header.addCell(getCell(new Paragraph("Listado de Pacientes").setFont(font).setBold().setFontSize(20f), TextAlignment.CENTER,false));
        document.add(header);

        document.add(new Paragraph(""));document.add(new Paragraph(""));

        Color bkg = ColorConstants.RED;
        Color frg= ColorConstants.WHITE;
        Table body = new Table(4);
        body.setWidth(400);
        body.setHorizontalAlignment(HorizontalAlignment.CENTER);
        body.addCell(getCell(new Paragraph("Id").setBackgroundColor(bkg).setFontColor(frg),TextAlignment.CENTER,true));
        body.addCell(getCell(new Paragraph("Nombre").setBackgroundColor(bkg).setFontColor(frg),TextAlignment.CENTER,true));
        body.addCell(getCell(new Paragraph("Fecha de Nacimiento").setBackgroundColor(bkg).setFontColor(frg),TextAlignment.CENTER,true));
        body.addCell(getCell(new Paragraph("Telefono:").setBackgroundColor(bkg).setFontColor(frg),TextAlignment.CENTER,true));

        for(Paciente e : model.getListaPaciente()){
            body.addCell(getCell(new Paragraph(e.getId()),TextAlignment.CENTER,true));
            body.addCell(getCell(new Paragraph(e.getNombre()),TextAlignment.CENTER,true));
            body.addCell(getCell(new Paragraph(e.getFechaNacimiento().toString()),TextAlignment.CENTER,true));
            body.addCell(getCell(new Paragraph(e.getNumero()),TextAlignment.CENTER,true));
        }
        document.add(body);
        document.close();


    }

    private Cell getCell(Paragraph paragraph, TextAlignment alignment, boolean hasBorder) {
        Cell cell = new Cell().add(paragraph);
        cell.setPadding(0);
        cell.setTextAlignment(alignment);
        if(!hasBorder) cell.setBorder(Border.NO_BORDER);
        return cell;
    }

    // Selecciona un paciente de la lista por posición
    public void agregarPacienteEnPos(int pos) throws Exception {
        if (pos < 0 || pos >= model.getListaPaciente().size()) {
            throw new Exception("Posición inválida.");
        }
        Paciente seleccionado = model.getListaPaciente().get(pos);
        model.setCurrent(seleccionado);
        model.addPropertyChangeListener(evt -> {
            if ("current".equals(evt.getPropertyName())) {
                System.out.println("La propiedad 'current' ha cambiado: " + evt.getNewValue());
            }
        });
    }

    // Selecciona un paciente a partir de un filtro
    public void add(Paciente filter) throws Exception {
        model.setFilter(filter);
        List<Paciente> resultados = Service.instance().search(model.getFilter());

        if (resultados.isEmpty()) {
            throw new Exception("No se encontró ningún paciente con esos datos.");
        }

        // Por ahora tomo el primero (podrías mostrar lista en vista para que usuario elija)
        Paciente seleccionado = resultados.get(0);
        model.setCurrent(seleccionado);
        model.addPropertyChangeListener(evt -> {
            if ("current".equals(evt.getPropertyName())) {
                System.out.println("La propiedad 'current' ha cambiado: " + evt.getNewValue());
            }
        });
    }
    @Override
    public void refresh() {
        try {
            model.setListaPaciente(Service.instance().search(model.getFilter()));
        } catch (Exception e) {}
    }

    public void stop(){
        refresher.stop();
    }



}
