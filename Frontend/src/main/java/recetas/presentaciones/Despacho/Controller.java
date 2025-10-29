package recetas.presentaciones.Despacho;

import recetas.Application;
import recetas.logic.Receta;
import recetas.logic.Service;

public class Controller {
    protected ViewDespacho viewDespacho;
    private Model model;

    public Controller(ViewDespacho viewDespacho, Model model) {
        model.init(Service.instance().search(new Receta()));
        this.viewDespacho = viewDespacho;
        this.model = model;
        viewDespacho.setController(this);
        viewDespacho.setModel(model);
    }
    public void search(Receta filter){
        model.setFilter(filter);
        model.setMode(Application.MODE_CREATE);
        model.setCurrentReceta(new Receta());
        model.setListaRecetas(Service.instance().search(model.getFilter()));
    }
    public void searchId(Receta filter){
        model.setFilter(filter);
        model.setMode(Application.MODE_CREATE);
        model.setCurrentReceta(new Receta());
        model.setListaRecetas(Service.instance().search(model.getFilter()));
    }
    public void save(Receta e) throws Exception {
        switch(model.getMode()){
            case Application.MODE_CREATE:
                Service.instance().create(e);
                break;
            case Application.MODE_EDIT:
                Service.instance().update(e);
                break;
        }
        model.setFilter(new Receta());
        search(model.getFilter());
    }
    public void edit(int row) {
        Receta e = model.getListaRecetas().get(row);
        try{
            model.setMode(Application.MODE_EDIT);
            model.setCurrentReceta(Service.instance().read(e));
        } catch (Exception ex) {}
    }

    public void clear(){
        model.setMode(Application.MODE_CREATE);
        model.setCurrentReceta(new Receta());
    }

    public void delete()throws Exception{
        Service.instance().delete(model.getCurrentReceta());
        search(model.getFilter());
    }
    public void marcarEnProceso() throws Exception {
        Receta r = model.getCurrentReceta();
        if (r == null) throw new Exception("Debe seleccionar una receta.");
        if (r.getEstado() != Receta.Estado.CONFECCIONADA)
            throw new Exception("Solo se pueden poner en Proceso recetas Confeccionadas.");

        r.setEstado(Receta.Estado.PROCESO);
        Service.instance().update(r);
        search(model.getFilter());
    }

    public void marcarLista() throws Exception {
        Receta r = model.getCurrentReceta();
        if (r == null) throw new Exception("Debe seleccionar una receta.");
        if (r.getEstado() != Receta.Estado.PROCESO)
            throw new Exception("Solo se pueden poner en Lista recetas en Proceso.");

        r.setEstado(Receta.Estado.LISTA);
        Service.instance().update(r);
        search(model.getFilter());
    }

    public void marcarEntregada() throws Exception {
        Receta r = model.getCurrentReceta();
        if (r == null) throw new Exception("Debe seleccionar una receta.");
        if (r.getEstado() != Receta.Estado.LISTA)
            throw new Exception("Solo se pueden Entregar recetas en Lista.");

        r.setEstado(Receta.Estado.ENTREGADA);
        Service.instance().update(r);
        search(model.getFilter());
    }

}
