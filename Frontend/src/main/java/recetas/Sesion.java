package recetas;

import progra3.logic.Usuario;

/**
 *
 * Clase de sesión simple para la aplicación frontend.
 * - Mantiene el usuario logueado (static).
 * - Provee isLoggedIn(), getUsuario(), setUsuario() y logout().
 *
 * Asegúrate de que el controlador de login (recetas.presentaciones.logIn.Controller)
 * invoque Sesion.setUsuario(usuarioAutenticado) cuando el login sea exitoso.
 */
public class Sesion {

    private static Usuario usuario;

    private Sesion() { /* singleton estático: no instanciable */ }

    public static boolean isLoggedIn() {
        return usuario != null;
    }

    public static Usuario getUsuario() {
        return usuario;
    }

    public static void setUsuario(Usuario u) {
        usuario = u;
    }

    public static void logout() {
        usuario = null;
    }
}