package administradorCaja;

import AdministradorCajaDTOs.cajeroDTO;
import AdministradorCajaDTOs.corteCajaDTO;
import AdministradorCajaDTOs.resumenVentasDTO;
import AdministradorCajaDTOs.ventaDTO;
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
        ICorteCajaDAO corteDAO = new CorteCajaDAOMock();
        return corteDAO.obtenerHistorial(fechaInicio, fechaFin);
    }



    @Override
    public List<cajeroDTO> consultarCajeros() {
        return MockDatabase.getCajeros();
    }


    @Override
    public double obtenerVentasTotalesPorCajero(int idEmpleado) {
        List<ventaDTO> ventas = generadorResumenBO.extraerVentasPorCajero(idEmpleado, new Date());

        return ventas.stream()
                .mapToDouble(ventaDTO::getMontoTotal)
                .sum();
    }
}