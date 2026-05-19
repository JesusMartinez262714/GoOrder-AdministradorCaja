package AdministradorCajaPresentacion;

import AdministradorCajaPersistencia.DAOs.*;
import AdministradorCajaPersistencia.Interfaces.*;
import AdministradorCajaPresentacion.Control.Control;
import Interfaces.INegocioCorte;
import Interfaces.IVentaDAO;
import administradorCaja.AdministradorCaja;
import com.mongodb.client.MongoDatabase;
import goorderpersistencia.ventaDAO;
import Main.conexionMONGODB;
import javax.swing.*;

/**
 * Clase principal que sirve como punto de entrada para el administrador de caja.
 * Se encarga de configurar el aspecto visual, inicializar la conexión a la base de
 * datos MongoDB, armar las capas de software y lanzar la pantalla inicial.
 * * @author Jesus Manuel Martinez Cortez
 */
public class Main {

    /**
     * Método de arranque del sistema. Configura el entorno gráfico de Swing,
     * verifica la disponibilidad de la base de datos y arranca el flujo principal.
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Error al configurar el aspecto visual (Look and Feel).");
        }

        try {
            MongoDatabase db = conexionMONGODB.getBaseDatos();

            if (db != null) {
                IVentaDAO vDAO = new ventaDAO(db);
                ICorteCajaDAO cDAO = new corteCajaDAO(db);
                ICajeroDAO cajDAO = new cajeroDAO(db);
                IAdeudoDAO adeDAO = new adeudoDAO(db);
                ISupervisorDAO supDAO = new supervisorDAO(db);

                INegocioCorte negocio = new AdministradorCaja(vDAO, cDAO, cajDAO, adeDAO, supDAO);
                Control control = new Control(negocio);

                SwingUtilities.invokeLater(() -> control.iniciarFlujoResumen());

                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    try {
                        conexionMONGODB.cerrarConexion();
                    } catch (Exception e) {
                        System.err.println("Error al cerrar la conexión de MongoDB en el shutdown hook.");
                    }
                }));

            } else {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null,
                        "No se pudo conectar a MongoDB. Asegúrate de que el servicio esté activo.",
                        "Error de Conexión",
                        JOptionPane.ERROR_MESSAGE));
            }
        } catch (Exception ex) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null,
                    "Ocurrió un error inesperado al inicializar el sistema: " + ex.getMessage(),
                    "Error Crítico",
                    JOptionPane.ERROR_MESSAGE));
        }
    }
}