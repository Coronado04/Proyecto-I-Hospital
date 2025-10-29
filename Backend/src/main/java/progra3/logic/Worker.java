package progra3.logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class Worker {
    Server srv;
    Socket s;
    Service service;
    ObjectOutputStream os;
    ObjectInputStream is;

    public Worker(Server srv, Socket s, Service service) {
        try{
            this.srv=srv;
            this.s=s;
            os = new ObjectOutputStream(s.getOutputStream());
            is = new ObjectInputStream(s.getInputStream());
            this.service=service;
        } catch (IOException ex) { System.out.println(ex); }
    }

    boolean continuar;
    public void start(){
        try {
            System.out.println("Worker atendiendo peticiones...");
            Thread t = new Thread(new Runnable(){
                public void run(){
                    listen();
                }
            });
            continuar = true;
            t.start();
        } catch (Exception ex) { }
    }
    
    public void stop(){
        continuar=false;
        System.out.println("Conexion cerrada...");
    }
    
    public void listen(){
        int method;
        while (continuar) {
            try {
                method = is.readInt();
                System.out.println("Operacion: "+method);
                switch(method) {
                    //----MEDICO-----
                    case Protocol.MEDICO_CREATE:
                        try {
                            service.create((Medico)is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) { os.writeInt(Protocol.ERROR_ERROR); }
                        break;
                    case Protocol.MEDICO_READ:
                        try {
                            Medico e=service.read((Medico)is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(e);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.MEDICO_UPDATE:
                        try {
                            service.update((Medico) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.MEDICO_DELETE:
                        try {
                            service.delete((Medico)is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.MEDICO_SEARCH:
                        try {
                            List<Medico> le=service.search((Medico)is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(le);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.MEDICO_FIND_ALL:
                        try {
                            List<Medico> le=service.findAll();
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(le);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    //----PACIENTE-----
                    case Protocol.PACIENTE_CREATE:
                        try {
                            service.create((Paciente)is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) { os.writeInt(Protocol.ERROR_ERROR); }
                        break;
                    case Protocol.PACIENTE_READ:
                        try {
                            Paciente e=service.read((Paciente)is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(e);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.PACIENTE_UPDATE:
                        try {
                            service.update((Paciente) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.PACIENTE_DELETE:
                        try {
                            service.delete((Paciente) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.PACIENTE_SEARCH:
                        try {
                            List<Paciente> le=service.search((Paciente) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(le);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.PACIENTE_FIND_ALL:
                        try {
                            List<Paciente> le=service.findAllPaciente();
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(le);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                        //----MEDICAMENTO-----
                    case Protocol.MEDICAMENTO_CREATE:
                        try {
                            service.create((Medicamento) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) { os.writeInt(Protocol.ERROR_ERROR); }
                        break;
                    case Protocol.MEDICAMENTO_READ:
                        try {
                            Medicamento e=service.read((Medicamento)is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(e);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.MEDICAMENTO_UPDATE:
                        try {
                            service.update((Medicamento) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.MEDICAMENTO_DELETE:
                        try {
                            service.delete((Medicamento) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.MEDICAMENTO_SEARCH:
                        try {
                            List<Medicamento> le=service.search((Medicamento)is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(le);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.MEDICAMENTO_FIND_ALL:
                        try {
                            List<Medicamento> le=service.findAllMedicamentos();
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(le);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                        //----FARMACEUTA-----
                    case Protocol.FARMACEUTA_CREATE:
                        try {
                            service.create((Farmaceuta) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) { os.writeInt(Protocol.ERROR_ERROR); }
                        break;
                    case Protocol.FARMACEUTA_READ:
                        try {
                            Farmaceuta e=service.read((Farmaceuta) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(e);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.FARMACEUTA_UPDATE:
                        try {
                            service.update((Farmaceuta) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.FARMACEUTA_DELETE:
                        try {
                            service.delete((Farmaceuta) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.FARMACEUTA_SEARCH:
                        try {
                            List<Farmaceuta> le=service.search((Farmaceuta) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(le);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                        //----LINEA-----
                    case Protocol.LINEA_CREATE:
                        try {
                            Object[] data = (Object[]) is.readObject();  // ← leer el arreglo enviado
                            Linea linea = (Linea) data[0];
                            String idReceta = (String) data[1];

                            service.create(linea, idReceta);

                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.LINEA_READ:
                        try {
                            Linea e=service.read((Linea)is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(e);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.LINEA_UPDATE:
                        try {
                            service.update((Linea) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.LINEA_DELETE:
                        try {
                            service.delete((Linea)is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.LINEA_SEARCH:
                        try {
                            List<Linea> le=service.search((Linea)is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(le);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                        //------RECETA-----
                    case Protocol.RECETA_CREATE:
                        try {
                            service.create((Receta)is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) { os.writeInt(Protocol.ERROR_ERROR); }
                        break;
                    case Protocol.RECETA_READ:
                        try {
                            Receta e=service.read((Receta)is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(e);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.RECETA_UPDATE:
                        try {
                            service.update((Receta) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.RECETA_DELETE:
                        try {
                            service.delete((Receta) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.RECETA_SEARCH:
                        try {
                            List<Receta> le=service.search((Receta) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(le);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.RECETA_FIND_ALL:
                        try {
                            List<Receta> le=service.findAllRecetas();
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(le);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.USUARIO_CREATE:
                        try {
                            service.create((Usuario)is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) { os.writeInt(Protocol.ERROR_ERROR); }
                        break;
                    case Protocol.USUARIO_READ:
                        try {
                            Usuario e=service.read((Usuario) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(e);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.USUARIO_UPDATE:
                        try {
                            service.update((Usuario) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.USUARIO_DELETE:
                        try {
                            service.delete((Usuario) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.USUARIO_SEARCH:
                        try {
                            List<Usuario> le=service.search((Usuario) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(le);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.USUARIO_FIND_ALL:
                        try {
                            List<Usuario> le=service.findAllUsuarios();
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(le);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.USUARIO_LOGIN: {
                        try {   String id = (String) is.readObject();
                        String clave = (String) is.readObject();

                            Usuario u = Sesion.login(id, clave);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(u);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        os.flush();
                        break;
                    }

                    case Protocol.USUARIO_CAMBIAR_CLAVE: {
                        try {  String id = (String) is.readObject();
                        String oldClave = (String) is.readObject();
                        String nuevaClave = (String) is.readObject();

                            Usuario u = Sesion.login(id, oldClave);
                            Sesion.actualizarClave(id, nuevaClave);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        os.flush();
                        break;
                    }

                    case Protocol.DISCONNECT:
                        stop();
                        srv.remove(this);
                        break;
                }
                os.flush();
            } catch (IOException e) {
                stop();
            }
        }
    }

}
