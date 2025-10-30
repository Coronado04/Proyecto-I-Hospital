package recetas.presentaciones;

public interface UsuariosThreadListener extends ThreadListener{
    void deliver_Login(String message);
    void deliver_Logout(String message);
    void deliver_Mensaje(String origen,String message);
}
