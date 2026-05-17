package administradorCaja;

import AdministradorCajaDTOs.*;
import AdministradorCajaNegocio.BOs.*;
import AdministradorCajaNegocio.Interfaces.*;
import AdministradorCajaPersistencia.Interfaces.*;
import Interfaces.INegocioCorte;
import Interfaces.IVentaDAO;
import org.bson.Document;

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
    private adeudoBO adeudoBO;

    private ISupervisorDAO supervisorDAO;
    private ICorteCajaDAO corteCajaDAO;

    public AdministradorCaja(IVentaDAO vDAO, ICorteCajaDAO cDAO, IDesgloseMontosDAO dDAO,
                             ICajeroDAO cajDAO, IAdeudoDAO adeDAO, ISupervisorDAO supDAO) {

        this.generadorResumenBO = new GeneradorResumenBO(vDAO);
        this.corteCajaBO = new corteCajaBO(cDAO, dDAO, cajDAO);
        this.aperturaBO = new aperturaBO(cDAO); // <- Corregido: Ahora le pasamos cDAO
        this.gestionCajerosBO = new GestionCajerosBO(cajDAO, adeDAO);
        this.gestionSupervisoresBO = new GestionSupervisoresBO(supDAO);
        this.adeudoBO = new adeudoBO(adeDAO);

        this.supervisorDAO = supDAO;
        this.corteCajaDAO = cDAO;
    }

    @Override
    public List<cajeroDTO> obtenerCajerosFiltrados(String n, String t, String d) {
        return gestionCajerosBO.obtenerCajerosFiltrados(n, t, d);
    }

    @Override
    public boolean registrarCajero(cajeroDTO dto) {
        return gestionCajerosBO.registrarCajero(dto);
    }

    @Override
    public boolean editarCajero(cajeroDTO dto) {
        return gestionCajerosBO.editarCajero(dto);
    }

    @Override
    public boolean eliminarCajero(int idCajero) {
        return gestionCajerosBO.eliminarCajero(idCajero);
    }

    @Override
    public List<cajeroDTO> consultarCajeros() {
        return obtenerCajerosFiltrados("", "Todos", "Todos");
    }

    @Override
    public boolean registrarApertura(aperturaCajaDTO apertura) {
        if (corteCajaDAO.tieneAperturaActiva(apertura.getIdCajero())) {
            return false;
        }
        return aperturaBO.registrarFondolnicial(apertura);
    }

    @Override
    public List<cajeroDTO> consultarCajerosConTurnoActivo() {
        List<Integer> idsActivos = corteCajaDAO.obtenerIdsCajerosConCajaAbierta();
        if (idsActivos == null || idsActivos.isEmpty()) return new ArrayList<>();

        return consultarCajeros().stream()
                .filter(c -> idsActivos.contains(c.getIdCajero()))
                .collect(Collectors.toList());
    }

    @Override
    public Integer obtenerIdSupervisorDeAperturaActiva(int idCajero) {
        Document caja = corteCajaDAO.consultarUltimaCaja(idCajero);
        return (caja != null) ? caja.getInteger("idSupervisor") : null;
    }

    @Override
    public String obtenerNombreSupervisorPorId(int idSupervisor) {
        // Este se queda igual, usa su propio DAO
        var entidad = supervisorDAO.consultarPorId(idSupervisor);
        return (entidad != null) ? entidad.getNombreCompleto() : "No encontrado";
    }

    @Override
    public List<supervisorDTO> consultarSupervisores() {
        return gestionSupervisoresBO.obtenerTodosLosSupervisores("");
    }

    @Override
    public List<supervisorDTO> obtenerSupervisoresFiltrados(String nombre) {
        return gestionSupervisoresBO.obtenerSupervisoresFiltrados(nombre);
    }

    @Override
    public boolean registrarSupervisor(supervisorDTO dto) {
        return gestionSupervisoresBO.registrarSupervisor(dto);
    }

    @Override
    public boolean editarSupervisor(supervisorDTO dto) {
        return gestionSupervisoresBO.editarSupervisor(dto);
    }

    @Override
    public boolean eliminarSupervisor(int idSupervisor) {
        return gestionSupervisoresBO.eliminarSupervisor(idSupervisor);
    }

    @Override
    public resumenVentasDTO generarResumenVentasTurno(int idCajero, Date fechaActual) {
        Document caja = corteCajaDAO.consultarUltimaCaja(idCajero);
        Date fechaInicio = (caja != null) ? caja.getDate("fechaApertura") : fechaActual;
        return generadorResumenBO.generarResumenVentasTurno(idCajero, fechaInicio);
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
        boolean guardado = corteCajaBO.guardarNuevoCorte(datosCorte, listaDesgloses);

        if (guardado) {
            if (datosCorte.getDiferencia() < 0) {
                double faltante = Math.abs(datosCorte.getDiferencia());
                adeudoBO.registrarAdeudoFaltante(datosCorte.getIdCajero(), faltante);
            }
        }
        return guardado;
    }

    @Override
    public List<corteCajaDTO> consultarCortesRealizados(Date fechaInicio, Date fechaFin) {
        return corteCajaBO.consultarCortesRealizados(fechaInicio, fechaFin);
    }

    @Override
    public boolean eliminarCorteFisico(int idCorte) {
        return corteCajaDAO.eliminarCorte(idCorte);
    }

    @Override
    public boolean cancelarCorteLogico(int idCorte) {
        return corteCajaDAO.actualizarEstadoCorte(idCorte, "Cancelado");
    }

    @Override
    public boolean liquidarAdeudo(int idCajero, double montoPagado) {
        return adeudoBO.liquidarAdeudo(idCajero, montoPagado);
    }
}