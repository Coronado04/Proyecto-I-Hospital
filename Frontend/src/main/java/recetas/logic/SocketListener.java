package recetas.logic;

import progra3.logic.Protocol;
import recetas.Sesion;
import recetas.presentaciones.ThreadListener;
import recetas.presentaciones.UsuariosThreadListener;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketListener {
    private UsuariosThreadListener listener;

    String sid;
    Socket as;
    ObjectOutputStream aos;
    ObjectInputStream ais;

    public SocketListener(UsuariosThreadListener listener, String sid) throws Exception {
        this.listener = listener;
        as= new Socket (Protocol.SERVER, Protocol.PORT);
        this.sid=sid;
        aos = new ObjectOutputStream(as.getOutputStream());
        ais = new ObjectInputStream(as.getInputStream());
        aos.writeInt(Protocol.ASYNC);
        aos.writeObject(sid);
        aos.writeObject(Sesion.getUsuario().getId());
        aos.flush();
    }

    private boolean condition = true;
    private Thread t;

    public void start() {
        t = new Thread(new Runnable() {
            public void run() {listen();}
        });
        condition= true;
        t.start();
    }
    public void stop() {
        condition = false;
        try { as.close(); } catch (Exception ignored) {}
    }

    public void listen() {
        int method;
        while (condition) {
            try {
                method = ais.readInt();
                switch (method) {
                    case Protocol.DELIVER_LOGIN:
                        try {
                            final String message = (String) ais.readObject();
                          SwingUtilities.invokeLater(() -> {
                                try {
                                    listener.deliver_Login(message);
                                } catch (Throwable ex) {
                                 ex.printStackTrace();
                                }
                            });
                        } catch (ClassNotFoundException ex) { ex.printStackTrace(); }
                        break;
                    case Protocol.DELIVER_LOGOUT:
                        try {
                            final String message = (String) ais.readObject();
                            SwingUtilities.invokeLater(() -> {
                                try {
                                    listener.deliver_Logout(message);
                                } catch (Throwable ex) { ex.printStackTrace(); }
                            });
                        } catch (ClassNotFoundException ex) { ex.printStackTrace(); }
                        break;
                    case Protocol.DELIVER_MENSAJE:
                        try {
                            final String origen = (String) ais.readObject();
                            final String texto = (String) ais.readObject();
                           SwingUtilities.invokeLater(() -> {
                                try {
                                    listener.deliver_Mensaje(origen, texto);
                                } catch (Throwable ex) { ex.printStackTrace(); }
                            });
                        } catch (ClassNotFoundException ex) { ex.printStackTrace(); }
                        break;

                    default:
                        break;
                }
            } catch(IOException ex){
                condition = false;
            }
        }
        try {
            as.shutdownOutput();
            as.close();
        } catch (IOException e) {}
    }
}