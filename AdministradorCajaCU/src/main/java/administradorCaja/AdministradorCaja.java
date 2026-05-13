package administradorCaja;

import AdministradorCajaDTOs.*;
import AdministradorCajaNegocio.BOs.GeneradorResumenBO;
import AdministradorCajaNegocio.Interfaces.IGeneradorResumenBO;
import AdministradorCajaPersistencia.Interfaces.ICorteCajaDAO;
import AdministradorCajaPersistencia.Interfaces.IVentaDAO;
import AdministradorCajaPersistencia.mocks.CorteCajaDAOMock;
import AdministradorCajaPersistencia.mocks.MockDatabase;
import Interfaces.INegocioCorte;

import java.util.Date;
import java.util.List;

public class AdministradorCaja implements INegocioCorte {

    private IGeneradorResumenBO generadorResumenBO;
    private IVentaDAO ventaDAO;

    public AdministradorCaja(IVentaDAO ventaDAO) {
        this.ventaDAO = ventaDAO;
        this.generadorResumenBO = new GeneradorResumenBO(ventaDAO);
    }

    @Override
    public resumenVentasDTO generarResumenVentasTurno(int idCajero, Date fechaActual) {
        List<ventaDTO> ventas = generadorResumenBO.extraerVentasPorCajero(idCajero, fechaActual);
        return generadorResumenBO.calcularTotalesPorMetodo(ventas);
    }

    @Override
    public List<corteCajaDTO> consultarCortesRealizados(Date fechaInicio, Date fechaFin) {
        // TODO (BD): Reemplazar el Mock por el DAO real.
        // Cambiar "new CorteCajaDAOMock()" por la clase que haga de la bd
        ICorteCajaDAO corteDAO = new CorteCajaDAOMock();
        return corteDAO.obtenerHistorial(fechaInicio, fechaFin);
    }



    @Override
    public List<cajeroDTO> consultarCajeros() {
        // TODO (BD): Eliminar MockDatabase.
        // 1. Instanciar ICajeroDAO.
        // 2. Retornar cajeroDAO.consultarTodosLosCajeros();
        return MockDatabase.getCajeros();
    }


    @Override
    public double obtenerVentasTotalesPorCajero(int idEmpleado) {
        List<ventaDTO> ventas = generadorResumenBO.extraerVentasPorCajero(idEmpleado, new Date());

        return ventas.stream()
                .mapToDouble(ventaDTO::getMontoTotal)
                .sum();
    }

    @Override
    public boolean registrarApertura(aperturaCajaDTO apertura) {
        // TODO (BD): Conectar con la base de datos.
        // 1. Instanciar IAperturaCajaDAO.
        // 2. Retornar aperturaCajaDAO.insertarApertura(apertura);
        System.out.println("Caja abierta para: " + apertura.getNombreCajero() + " con $" + apertura.getMontoInicial());
        return true;
    }

    @Override
    public List<supervisorDTO> consultarSupervisores() {
        // TODO (BD): Eliminar MockDatabase.
        // 1. Instanciar ISupervisorDAO.
        // 2. Retornar supervisorDAO.consultarTodosLosSupervisores();
        return MockDatabase.getSupervisores();
    }
}