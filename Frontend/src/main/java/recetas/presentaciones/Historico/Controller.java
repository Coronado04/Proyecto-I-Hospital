package recetas.presentaciones.Historico;

import recetas.Application;
import recetas.logic.Paciente;
import recetas.logic.Receta;
import recetas.logic.Service;

public class Controller {
    private ViewHistorico viewHistorico;
    private Model model;


    public Controller(ViewHistorico viewHistorico, Model model) {
        model.init(Service.instance().search(new Receta()));
        this.viewHistorico = viewHistorico;
        this.model = model;
        viewHistorico.setController(this);
        viewHistorico.setModel(model);
    }
    public void search(Receta filter){
        model.setFilter(filter);
        model.setMode(Application.MODE_CREATE);
        model.setCurrent(new Receta());
        model.setListaReceta(Service.instance().search(model.getFilter()));
    }



    public void save(Receta e) throws  Exception{
        switch (model.getMode()){
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
    public void edit(int row){
        Receta e = model.getListaReceta().get(row);
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
        model.setCurrent(new Receta());
    }
}
