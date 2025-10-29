package recetas.presentaciones.medicos;


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
import recetas.Application;
import recetas.logic.Service;
import progra3.logic.Medico;

public class Controller {
    private ViewMedicos view;
    private Model model;

    public void search(Medico filter) throws Exception{
        model.setFilter(filter);
        model.setMode(Application.MODE_CREATE);
        model.setCurrent(new Medico());
        model.setListaMedico(Service.instance().search(model.getFilter()));
    }
    public Controller(ViewMedicos view, Model model) {
        model.init(Service.instance().search(new Medico()));
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
    }

    public void save(Medico e) throws Exception{
        switch (model.getMode()){
            case Application.MODE_CREATE:
                Service.instance().create(e);
                break;
            case Application.MODE_EDIT:
                Service.instance().update(e);
                break;
        }
        model.setFilter(new Medico());
        search(model.getFilter());
    }


    public void clear(){
        model.setMode(Application.MODE_CREATE);
        model.setCurrent(new Medico());
    }
    public void delete()throws Exception{
        Service.instance().delete(model.getCurrent());
        search(model.getFilter());
}
    public void edit(int row){
        Medico e = model.getListaMedico().get(row);
        try {
            model.setMode(Application.MODE_EDIT);
            model.setCurrent(Service.instance().read(e));
        } catch (Exception ex) {}
    }

    public void print() throws Exception{
        if(model.getListaMedico().isEmpty()){
            throw new Exception("Lista vacía");
        }
        String file = "medicos.pdf";
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdf = new PdfDocument(writer);

        Document document = new Document(pdf);
        document.setMargins(20, 20, 20, 20);

        Table header = new Table(1);
        header.setWidth(400);
        header.setHorizontalAlignment(HorizontalAlignment.CENTER);
        header.addCell(getCell(new Paragraph("Listado de Medicos").setFont(font).setBold().setFontSize(20f), TextAlignment.CENTER,false));
        document.add(header);

        document.add(new Paragraph(""));document.add(new Paragraph(""));

        Color bkg = ColorConstants.RED;
        Color frg= ColorConstants.WHITE;
        Table body = new Table(3);
        body.setWidth(400);
        body.setHorizontalAlignment(HorizontalAlignment.CENTER);
        body.addCell(getCell(new Paragraph("Id").setBackgroundColor(bkg).setFontColor(frg),TextAlignment.CENTER,true));
        body.addCell(getCell(new Paragraph("Nombre").setBackgroundColor(bkg).setFontColor(frg),TextAlignment.CENTER,true));
        body.addCell(getCell(new Paragraph("Especialidad").setBackgroundColor(bkg).setFontColor(frg),TextAlignment.CENTER,true));

        for(Medico e : model.getListaMedico()){
            body.addCell(getCell(new Paragraph(e.getId()),TextAlignment.CENTER,true));
            body.addCell(getCell(new Paragraph(e.getNombre()),TextAlignment.CENTER,true));
            body.addCell(getCell(new Paragraph(e.getEspecialidad()),TextAlignment.CENTER,true));
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



}
