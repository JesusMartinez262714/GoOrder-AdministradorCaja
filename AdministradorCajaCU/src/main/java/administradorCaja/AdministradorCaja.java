package administradorCaja;
import AdministradorCajaDTOs.resumenVentasDTO;
import AdministradorCajaDTOs.ventaDTO;
import AdministradorCajaNegocio.BOs.GeneradorResumenBO;
import AdministradorCajaNegocio.Interfaces.IGeneradorResumenBO;
import AdministradorCajaPersistencia.Interfaces.IVentaDAO;
import Interfaces.INegocioCorte;

import java.util.Date;
import java.util.List;


public class AdministradorCaja implements INegocioCorte {

    private IGeneradorResumenBO generadorResumenBO;
    private IVentaDAO ventaDAO;


    public AdministradorCaja(IVentaDAO ventaDAO) {
        this.ventaDAO = ventaDAO;
        // Inicializamos el BO encargado de los resúmenes
        this.generadorResumenBO = new GeneradorResumenBO(ventaDAO);
    }


    @Override
    public resumenVentasDTO generarResumenVentasTurno(int idCajero, Date fechaActual) {
        List<ventaDTO> ventas = generadorResumenBO.extraerVentasPorCajero(idCajero, fechaActual);

        return generadorResumenBO.calcularTotalesPorMetodo(ventas);
    }


}