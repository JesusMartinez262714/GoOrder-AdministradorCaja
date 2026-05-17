package AdministradorCajaPresentacion;


import AdministradorCajaPersistencia.DAOs.*;
import AdministradorCajaPersistencia.Interfaces.*;
import AdministradorCajaPresentacion.Control.Control;
import Interfaces.INegocioCorte;
import Interfaces.IVentaDAO;
import administradorCaja.AdministradorCaja;
import com.mongodb.client.MongoDatabase;
import goorderpersistencia.ventaDAO;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Error de Look and Feel.");
        }

        MongoDatabase db = mongoConexion.getDatabase();

        if (db != null) {
            IVentaDAO vDAO = new ventaDAO(db);
            ICorteCajaDAO cDAO = new corteCajaDAO(db);
            IDesgloseMontosDAO dDAO = new desgloseMontosDAO(db);
            IAperturaCajaDAO aDAO = new aperturaCajaDAO(db);

            ICajeroDAO cajDAO = new cajeroDAO(db);
            IAdeudoDAO adeDAO = new adeudoDAO(db);
            ISupervisorDAO supDAO = new supervisorDAO(db);


            INegocioCorte negocio = new AdministradorCaja(vDAO, cDAO, dDAO, aDAO, cajDAO, adeDAO, supDAO);
            Control control = new Control(negocio);

            SwingUtilities.invokeLater(() -> {
                control.iniciarFlujoResumen();
            });

            Runtime.getRuntime().addShutdownHook(new Thread(mongoConexion::cerrar));

        } else {
            JOptionPane.showMessageDialog(null,
                    "No se pudo conectar a MongoDB. Asegúrate de que el servicio esté activo.",
                    "Error de Conexión",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}