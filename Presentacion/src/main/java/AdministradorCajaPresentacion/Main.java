package AdministradorCajaPresentacion;

import AdministradorCajaPersistencia.Interfaces.IVentaDAO;
import AdministradorCajaPersistencia.mocks.VentaDAOMock;
import AdministradorCajaPresentacion.Control.Control;
import Interfaces.INegocioCorte;
import administradorCaja.AdministradorCaja;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo establecer el aspecto visual del sistema.");
        }
        IVentaDAO ventaDAO = new VentaDAOMock();
        INegocioCorte negocio = new AdministradorCaja(ventaDAO);
        Control control = new Control(negocio);
        control.iniciarFlujoResumen();
    }
}