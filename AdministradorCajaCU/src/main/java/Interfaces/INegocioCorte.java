package Interfaces;

import AdministradorCajaDTOs.cajeroDTO;
import AdministradorCajaDTOs.corteCajaDTO;
import AdministradorCajaDTOs.resumenVentasDTO;
import java.util.Date;
import java.util.List;

public interface INegocioCorte {

    resumenVentasDTO generarResumenVentasTurno(int idCajero, Date fechaActual);
    List<corteCajaDTO> consultarCortesRealizados(Date fechaInicio, Date fechaFin);
    double obtenerVentasTotalesPorCajero(int idEmpleado);
    List<cajeroDTO> consultarCajeros();

}