package recetas.presentaciones.Historico;

import progra3.logic.Receta;
import recetas.Application;
import recetas.logic.Service;
import recetas.presentaciones.Refresher;
import recetas.presentaciones.ThreadListener;

public class Controller implements ThreadListener {
    private ViewHistorico viewHistorico;
    private Model model;

    Refresher refresher;


    public Controller(ViewHistorico viewHistorico, Model model) {
        model.init(Service.instance().search(new Receta()));
        this.viewHistorico = viewHistorico;
        this.model = model;
        viewHistorico.setController(this);
        viewHistorico.setModel(model);

        refresher = new Refresher(this);
        refresher.start();
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

    @Override
    public void refresh() {
        try {
            model.setListaReceta(Service.instance().search(model.getFilter()));
        } catch (Exception e) {}
    }

    public void stop(){
        refresher.stop();
    }
}
