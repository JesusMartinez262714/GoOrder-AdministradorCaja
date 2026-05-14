package administradorCaja;

import AdministradorCajaDTOs.*;
import AdministradorCajaNegocio.BOs.*;
import AdministradorCajaNegocio.Interfaces.*;
import AdministradorCajaPersistencia.Entitys.aperturaCaja;
import AdministradorCajaPersistencia.Entitys.supervisor;
import AdministradorCajaPersistencia.Interfaces.*;
import Interfaces.INegocioCorte;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class AdministradorCaja implements INegocioCorte {

    private IGeneradorResumenBO generadorResumenBO;
    private IGestionCajerosBO gestionCajerosBO;
    private IGestionSupervisoresBO gestionSupervisoresBO;
    private ICorteCajaBO corteCajaBO;
    private IAperturaBO aperturaBO;

    private IAperturaCajaDAO aperturaDAO;
    private ISupervisorDAO supervisorDAO;
    private ICorteCajaDAO corteCajaDAO;

    public AdministradorCaja(IVentaDAO vDAO, ICorteCajaDAO cDAO, IDesgloseMontosDAO dDAO,
                             IAperturaCajaDAO aDAO, ICajeroDAO cajDAO, IAdeudoDAO adeDAO,
                             ISupervisorDAO supDAO) {

        this.generadorResumenBO = new GeneradorResumenBO(vDAO);
        this.corteCajaBO = new corteCajaBO(cDAO, dDAO, cajDAO);
        this.aperturaBO = new aperturaBO(aDAO);
        this.gestionCajerosBO = new GestionCajerosBO(cajDAO, adeDAO);
        this.gestionSupervisoresBO = new GestionSupervisoresBO(supDAO);

        this.aperturaDAO = aDAO;
        this.supervisorDAO = supDAO;
        this.corteCajaDAO = cDAO;
    }



    @Override
    public List<cajeroDTO> obtenerCajerosFiltrados(String n, String t, String d) {
        return gestionCajerosBO.obtenerCajerosFiltrados(n, t, d);
    }

    @Override
    public boolean registrarCajero(cajeroDTO dto) { return gestionCajerosBO.registrarCajero(dto); }

    @Override
    public boolean editarCajero(cajeroDTO dto) { return gestionCajerosBO.editarCajero(dto); }

    @Override
    public boolean eliminarCajero(int idCajero) { return gestionCajerosBO.eliminarCajero(idCajero); }

    @Override
    public List<cajeroDTO> consultarCajeros() { return obtenerCajerosFiltrados("", "Todos", "Todos"); }



    @Override
    public boolean registrarApertura(aperturaCajaDTO apertura) {
        if (aperturaDAO.tieneAperturaActiva(apertura.getIdCajero())) {
            JOptionPane.showMessageDialog(null,
                    "El cajero ya tiene un turno activo.\nDebe cerrarlo antes de abrir uno nuevo.",
                    "Caja ya abierta", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return aperturaBO.registrarFondolnicial(apertura);
    }

    @Override
    public List<cajeroDTO> consultarCajerosConTurnoActivo() {
        List<Integer> idsActivos = aperturaDAO.obtenerIdsCajerosConCajaAbierta();
        if (idsActivos == null || idsActivos.isEmpty()) return new ArrayList<>();

        return consultarCajeros().stream()
                .filter(c -> idsActivos.contains(c.getIdCajero()))
                .collect(Collectors.toList());
    }

    @Override
    public Integer obtenerIdSupervisorDeAperturaActiva(int idCajero) {
        aperturaCaja apertura = aperturaDAO.consultarUltimaApertura(idCajero);
        return (apertura != null) ? apertura.getIdSupervisor() : null;
    }



    @Override
    public String obtenerNombreSupervisorPorId(int idSupervisor) {
        supervisor entidad = supervisorDAO.consultarPorId(idSupervisor);
        return (entidad != null) ? entidad.getNombreCompleto() : "No encontrado";
    }

    @Override
    public List<supervisorDTO> consultarSupervisores() { return gestionSupervisoresBO.obtenerTodosLosSupervisores(""); }

    @Override
    public List<supervisorDTO> obtenerSupervisoresFiltrados(String nombre) { return gestionSupervisoresBO.obtenerSupervisoresFiltrados(nombre); }

    @Override
    public boolean registrarSupervisor(supervisorDTO dto) { return gestionSupervisoresBO.registrarSupervisor(dto); }

    @Override
    public boolean editarSupervisor(supervisorDTO dto) { return gestionSupervisoresBO.editarSupervisor(dto); }

    @Override
    public boolean eliminarSupervisor(int idSupervisor) { return gestionSupervisoresBO.eliminarSupervisor(idSupervisor); }



    @Override
    public resumenVentasDTO generarResumenVentasTurno(int idCajero, Date fechaActual) {
        return generadorResumenBO.generarResumenVentasTurno(idCajero, fechaActual);
    }

    @Override
    public double obtenerVentasTotalesPorCajero(int idEmpleado) {
        resumenVentasDTO resumen = generarResumenVentasTurno(idEmpleado, new Date());
        return (resumen != null) ? resumen.getTotalEsperadoSistema() : 0.0;
    }


    @Override
    public double calcularDiferencia(double totalEsperado, double totalReal) {
        return corteCajaBO.calcularDiferencia(totalEsperado, totalReal);
    }

    @Override
    public String evaluarEstadoCorte(double diferencia) {
        return corteCajaBO.evaluarEstadoCorte(diferencia);
    }

    @Override
    public boolean guardarNuevoCorte(corteCajaDTO datosCorte, List<desgloseDTO> listaDesgloses) {
        aperturaCaja aperturaActiva = aperturaDAO.consultarUltimaApertura(datosCorte.getIdCajero());
        if (aperturaActiva != null) {
            datosCorte.setIdApertura(aperturaActiva.getIdApertura());
        }

        boolean guardado = corteCajaBO.guardarNuevoCorte(datosCorte, listaDesgloses);

        if (guardado) {
            aperturaDAO.vincularCorteAApertura(datosCorte.getIdApertura(), 1);
        }

        return guardado;
    }

    @Override
    public List<corteCajaDTO> consultarCortesRealizados(Date fechaInicio, Date fechaFin) {
        List<corteCajaDTO> listaCortes = corteCajaBO.consultarCortesRealizados(fechaInicio, fechaFin);

        if (listaCortes != null) {
            for (corteCajaDTO corte : listaCortes) {
                Date fechaAp = aperturaDAO.obtenerFechaAperturaPorId(corte.getIdApertura());
                if (fechaAp != null) {
                    corte.setFechaApertura(fechaAp);
                }
            }
        }
        return listaCortes;
    }

    @Override
    public boolean eliminarCorteFisico(int idCorte) {
        return corteCajaDAO.eliminarCorte(idCorte);
    }

    @Override
    public boolean cancelarCorteLogico(int idCorte) {
        return corteCajaDAO.actualizarEstadoCorte(idCorte, "Cancelado");
    }
}