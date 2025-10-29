package recetas.presentaciones.medicamentos;


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
import progra3.logic.Medicamento;
import recetas.Application;

import recetas.logic.Service;

public class Controller {
    private ViewMedicamentos view;
    private Model model;

    public Controller(ViewMedicamentos view, Model model) {
        model.init(Service.instance().search(new Medicamento()));
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
    }

    public void search(Medicamento filter){
        model.setFilter(filter);
        model.setMode(Application.MODE_CREATE);
        model.setCurrent(new Medicamento());
        model.setListaMedicamento(Service.instance().search(model.getFilter()));
    }

    public void save(Medicamento e) throws  Exception{
        switch (model.getMode()){
            case Application.MODE_CREATE:
                Service.instance().create(e);
                break;
            case Application.MODE_EDIT:
                Service.instance().update(e);
                break;
        }
        model.setFilter(new Medicamento());
        search(model.getFilter());
    }

    public void edit(int row){
        Medicamento e = model.getListaMedicamento().get(row);
        try {
            model.setMode(Application.MODE_EDIT);
            model.setCurrent(Service.instance().read(e));
        } catch (Exception ex) {}
    }

    public void delete()throws Exception{
        Service.instance().delete(model.getCurrent());
        search(model.getFilter());
    }

    public void clear(){
        model.setMode(Application.MODE_CREATE);
        model.setCurrent(new Medicamento());
    }

    public void print() throws Exception{
        if(model.getListaMedicamento().isEmpty()){
            throw new Exception("Lista vacía");
        }
        String file = "medicamentos.pdf";
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdf = new PdfDocument(writer);

        Document document = new Document(pdf);
        document.setMargins(20, 20, 20, 20);

        Table header = new Table(1);
        header.setWidth(400);
        header.setHorizontalAlignment(HorizontalAlignment.CENTER);
        header.addCell(getCell(new Paragraph("Listado de Medicamentos").setFont(font).setBold().setFontSize(20f), TextAlignment.CENTER,false));
        document.add(header);

        document.add(new Paragraph(""));document.add(new Paragraph(""));

        Color bkg = ColorConstants.RED;
        Color frg= ColorConstants.WHITE;
        Table body = new Table(3);
        body.setWidth(400);
        body.setHorizontalAlignment(HorizontalAlignment.CENTER);
        body.addCell(getCell(new Paragraph("Código").setBackgroundColor(bkg).setFontColor(frg),TextAlignment.CENTER,true));
        body.addCell(getCell(new Paragraph("Nombre").setBackgroundColor(bkg).setFontColor(frg),TextAlignment.CENTER,true));
        body.addCell(getCell(new Paragraph("Presentación").setBackgroundColor(bkg).setFontColor(frg),TextAlignment.CENTER,true));

        for(Medicamento e: model.getListaMedicamento()){
            body.addCell(getCell(new Paragraph(e.getCodigo()),TextAlignment.CENTER,true));
            body.addCell(getCell(new Paragraph(e.getNombre()),TextAlignment.CENTER,true));
            body.addCell(getCell(new Paragraph(e.getPresentacion()),TextAlignment.CENTER,true));
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
