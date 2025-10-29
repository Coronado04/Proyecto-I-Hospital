package recetas.presentaciones.logIn;

import progra3.logic.Usuario;
import recetas.presentaciones.AbstractModel;

public class Model extends AbstractModel {
    Usuario current;

    public void setUsuario(Usuario usuario) {
        this.current = usuario;
    }
}
