package recetas.presentaciones.usuarios;

import progra3.logic.Usuario;
import recetas.presentaciones.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {
    private List<Usuario> list;

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(LIST);
    }

    public Model() {}

    public void init(){
        list = new ArrayList<Usuario>();
    }

    public List<Usuario> getList() {
        return list;
    }

    public void setList(List<Usuario> list){
        this.list = list;
        firePropertyChange(LIST);
    }

    public void agregarUsuario(Usuario u){
        list.add(u);
        firePropertyChange(LIST);
    }

    public void quitarUsuario(Usuario u){
        list.remove(u);
        firePropertyChange(LIST);
    }

    public static final String LIST="list";

}
