package Interfaces;

import AdministradorCajaDTOs.*;

import java.util.Date;
import java.util.List;

public interface INegocioCorte {

    resumenVentasDTO generarResumenVentasTurno(int idCajero, Date fechaActual);
    List<corteCajaDTO> consultarCortesRealizados(Date fechaInicio, Date fechaFin);
    double obtenerVentasTotalesPorCajero(int idEmpleado);
    List<cajeroDTO> consultarCajeros();
    boolean registrarApertura(aperturaCajaDTO apertura);
    List<supervisorDTO> consultarSupervisores();
}