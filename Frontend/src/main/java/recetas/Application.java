package recetas;
import progra3.logic.Sesion;
import progra3.logic.Usuario;
import recetas.logic.Service;

import recetas.presentaciones.Despacho.ViewDespacho;
import recetas.presentaciones.Preescribir.ViewPreescribir;
import recetas.presentaciones.acercaDe.ViewacercaDe;
import recetas.presentaciones.dashboard.ViewDashboard;
import recetas.presentaciones.farmaceutas.ViewFarmaceutas;
import recetas.presentaciones.Historico.ViewHistorico;
import recetas.presentaciones.logIn.Model;
import recetas.presentaciones.logIn.ViewlogIn;
import recetas.presentaciones.medicamentos.ViewMedicamentos;
import recetas.presentaciones.medicos.ViewMedicos;
import recetas.presentaciones.paciente.ViewPaciente;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

//Sebastian Trejos Artavia 402630787
//Kevin López Guerrero 118890906
//Luis Coronado Benavides 402660049
//Segunda Parte del proyecto

public class Application {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        catch (Exception ex) {}
        doLogin();

        if(Sesion.isLoggedIn()){
            doRun();
        }
    }

    private static void doLogin(){
        Model model = new Model();
        ViewlogIn view = new ViewlogIn(null);
        recetas.presentaciones.logIn.Controller controller=new recetas.presentaciones.logIn.Controller(model, view);
        view.setVisible(true);
    }

    private static void doRun() {
        ventana = new JFrame();
        JTabbedPane tabbedPane = new JTabbedPane();
        ventana.setContentPane(tabbedPane);

        ventana.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
            }
        });


        Service.instance();
        ventana.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                Service.instance().stop();
            }
        });
        Usuario actual = Sesion.getUsuario();

        ViewMedicos viewMedicos = new ViewMedicos();
        recetas.presentaciones.medicos.Model modelMedicos = new recetas.presentaciones.medicos.Model();
        recetas.presentaciones.medicos.Controller controllerMedicos = new recetas.presentaciones.medicos.Controller(viewMedicos, modelMedicos);

        ViewPaciente viewPaciente = new ViewPaciente();
        recetas.presentaciones.paciente.Model modelPaciente = new recetas.presentaciones.paciente.Model();
        recetas.presentaciones.paciente.Controller controllerPacientes = new recetas.presentaciones.paciente.Controller(viewPaciente, modelPaciente);

        ViewMedicamentos viewMedicamentos = new ViewMedicamentos();
        recetas.presentaciones.medicamentos.Model modelMedicamentos = new recetas.presentaciones.medicamentos.Model();
        recetas.presentaciones.medicamentos.Controller controllerMedicamentos = new recetas.presentaciones.medicamentos.Controller(viewMedicamentos, modelMedicamentos);

        ViewFarmaceutas viewFarmaceutas = new ViewFarmaceutas();
        recetas.presentaciones.farmaceutas.Model modelFarmaceuta = new recetas.presentaciones.farmaceutas.Model();
        recetas.presentaciones.farmaceutas.Controller controller = new recetas.presentaciones.farmaceutas.Controller(viewFarmaceutas, modelFarmaceuta);

        ViewHistorico viewHistorico = new ViewHistorico();
        recetas.presentaciones.Historico.Model modelHistorico = new recetas.presentaciones.Historico.Model();
        recetas.presentaciones.Historico.Controller controllerHistorico = new recetas.presentaciones.Historico.Controller(viewHistorico,modelHistorico);

        ViewDashboard viewDashboard = new ViewDashboard();
        recetas.presentaciones.dashboard.Model modelDashboard = new recetas.presentaciones.dashboard.Model();
        recetas.presentaciones.dashboard.Controller controllerDashboard = new recetas.presentaciones.dashboard.Controller(viewDashboard, modelDashboard);

        ViewDespacho viewDespacho = new ViewDespacho();
        recetas.presentaciones.Despacho.Model modelDespacho = new recetas.presentaciones.Despacho.Model();
        recetas.presentaciones.Despacho.Controller controllerDespacho = new recetas.presentaciones.Despacho.Controller(viewDespacho, modelDespacho);

        ViewacercaDe viewacercaDe = new ViewacercaDe();

        ImageIcon iconMedicos = new ImageIcon(Application.class.getResource("/icons/doctor.png"));
        ImageIcon iconFarmaceuta = new ImageIcon(Application.class.getResource("/icons/farmaceutico.png"));
        ImageIcon iconPacientes = new ImageIcon(Application.class.getResource("/icons/pacientes.png"));
        ImageIcon iconMedicamentos = new ImageIcon(Application.class.getResource("/icons/prescripcionIcono.png"));
        ImageIcon iconDashboard = new ImageIcon(Application.class.getResource("/icons/dashboard.png"));
        ImageIcon iconAcercaDe = new ImageIcon(Application.class.getResource("/icons/acercaDe.png"));
        ImageIcon iconPreescribir = new ImageIcon(Application.class.getResource("/icons/prescripcion.png"));
        ImageIcon iconHistorico = new ImageIcon(Application.class.getResource("/icons/Historial.png"));
        ImageIcon iconDespacho = new ImageIcon(Application.class.getResource("/icons/despacho.png"));


        switch (actual.getRol()) {


            case "medico":
                ViewPreescribir viewPreescribir = new ViewPreescribir();
                recetas.presentaciones.Preescribir.Model modelPreescribir = new recetas.presentaciones.Preescribir.Model();
                recetas.presentaciones.Preescribir.Controller controllerPreescribir = new recetas.presentaciones.Preescribir.Controller(viewPreescribir, modelPreescribir, actual);
                tabbedPane.addTab("Preescribir",iconPreescribir, viewPreescribir.getPanel());
                tabbedPane.addTab("Dashboard",iconDashboard, viewDashboard.getPanel());
                tabbedPane.addTab("Historico",iconHistorico, viewHistorico.getPanel());
                tabbedPane.addTab("Acerca de...",iconAcercaDe, viewacercaDe.getPanelAcercade());
                break;

            case "farmaceuta":
                tabbedPane.addTab("Despacho",iconDespacho, viewDespacho.getPanel1());
                tabbedPane.addTab("Dashboard",iconDashboard, viewDashboard.getPanel());
                tabbedPane.addTab("Historico",iconHistorico, viewHistorico.getPanel());
                tabbedPane.addTab("Acerca de...",iconAcercaDe, viewacercaDe.getPanelAcercade());
                break;


            case "admin":
                tabbedPane.addTab("Medicos",iconMedicos,viewMedicos.getPanel());
                tabbedPane.addTab("Farmaceutas",iconFarmaceuta, viewFarmaceutas.getPanel());
                tabbedPane.addTab("Pacientes",iconPacientes, viewPaciente.getPanel());
                tabbedPane.addTab("Medicamentos",iconMedicamentos, viewMedicamentos.getPanel());
                tabbedPane.addTab("Dashboard",iconDashboard, viewDashboard.getPanel());
                tabbedPane.addTab("Historico",iconHistorico, viewHistorico.getPanel());
                tabbedPane.addTab("Acerca de...",iconAcercaDe, viewacercaDe.getPanelAcercade());

                break;

            default:

        }


        System.out.println("Usuario logueado: " + actual);


        ventana.setSize(1200, 600);
        ventana.setResizable(false);
        ventana.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ventana.setTitle("RECETAS");
        ventana.setVisible(true);
    }

    public static JFrame ventana;

    public static final Color BACKGROUND_ERROR = new Color(255, 102, 102);
    public final static int MODE_CREATE=1;
    public final static int MODE_EDIT=2;

    public static Border BORDER_ERROR = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.RED);
}

