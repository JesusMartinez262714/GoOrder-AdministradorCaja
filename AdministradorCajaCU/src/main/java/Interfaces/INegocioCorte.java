package Interfaces;

import AdministradorCajaDTOs.*;
import java.util.Date;
import java.util.List;

public interface INegocioCorte {
    List<cajeroDTO> obtenerCajerosFiltrados(String nombre, String turno, String deudaStatus);
    boolean registrarCajero(cajeroDTO dto);
    boolean editarCajero(cajeroDTO dto);
    boolean eliminarCajero(int idCajero);
    boolean registrarApertura(aperturaCajaDTO apertura);
    List<supervisorDTO> consultarSupervisores();
    List<cajeroDTO> consultarCajeros(); // Consulta simple para ComboBox
    List<supervisorDTO> obtenerSupervisoresFiltrados(String nombre);
    boolean registrarSupervisor(supervisorDTO dto);

    boolean editarSupervisor(supervisorDTO dto);

    boolean eliminarSupervisor(int idSupervisor);

    resumenVentasDTO generarResumenVentasTurno(int idCajero, Date fechaActual);
    double obtenerVentasTotalesPorCajero(int idEmpleado);

    double calcularDiferencia(double totalEsperado, double totalReal);
    String evaluarEstadoCorte(double diferencia);
    boolean guardarNuevoCorte(corteCajaDTO datosCorte, List<desgloseDTO> listaDesgloses);
    List<corteCajaDTO> consultarCortesRealizados(Date fechaInicio, Date fechaFin);
    Integer obtenerIdSupervisorDeAperturaActiva(int idCajero);


    String obtenerNombreSupervisorPorId(int idSupervisor);

    List<cajeroDTO> consultarCajerosConTurnoActivo();

    boolean eliminarCorteFisico(int idCorte);
    boolean cancelarCorteLogico(corteCajaDTO corte, String motivo);

    boolean liquidarAdeudo(int idCajero, double montoPagado);
}