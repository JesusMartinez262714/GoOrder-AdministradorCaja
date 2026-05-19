package administradorCaja;

import AdministradorCajaDTOs.*;
import AdministradorCajaNegocio.BOs.*;
import AdministradorCajaNegocio.Interfaces.*;
// 🔥 Se eliminó el import de la clase cajeroDAO que estaba causando el conflicto de nombres
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
    private ICajeroDAO cajeroDAO;

    public AdministradorCaja(IVentaDAO vDAO, ICorteCajaDAO cDAO,
                             ICajeroDAO cajDAO, IAdeudoDAO adeDAO, ISupervisorDAO supDAO) {

        this.generadorResumenBO = new GeneradorResumenBO(vDAO);
        this.corteCajaBO = new corteCajaBO(cDAO, cajDAO);
        this.aperturaBO = new aperturaBO(cDAO);
        this.gestionCajerosBO = new GestionCajerosBO(cajDAO, adeDAO);
        this.gestionSupervisoresBO = new GestionSupervisoresBO(supDAO);
        this.adeudoBO = new adeudoBO(adeDAO);

        this.supervisorDAO = supDAO;
        this.corteCajaDAO = cDAO;
        this.cajeroDAO = cajDAO;
    }

    @Override
    public List<cajeroDTO> obtenerCajerosFiltrados(String n, String t, String d) {
        return this.gestionCajerosBO.obtenerCajerosFiltrados(n, t, d);
    }

    @Override
    public boolean registrarCajero(cajeroDTO dto) {
        return this.gestionCajerosBO.registrarCajero(dto);
    }

    @Override
    public boolean editarCajero(cajeroDTO dto) {
        return this.gestionCajerosBO.editarCajero(dto);
    }

    @Override
    public boolean eliminarCajero(int idCajero) {
        return this.gestionCajerosBO.eliminarCajero(idCajero);
    }

    @Override
    public List<cajeroDTO> consultarCajeros() {
        return obtenerCajerosFiltrados("", "Todos", "Todos");
    }

    @Override
    public boolean registrarApertura(aperturaCajaDTO apertura) {
        if (this.corteCajaDAO.tieneAperturaActiva(apertura.getIdCajero())) {
            return false;
        }
        return this.aperturaBO.registrarFondolnicial(apertura);
    }

    @Override
    public List<cajeroDTO> consultarCajerosConTurnoActivo() {
        List<Integer> idsActivos = this.corteCajaDAO.obtenerIdsCajerosConCajaAbierta();
        if (idsActivos == null || idsActivos.isEmpty()) return new ArrayList<>();

        return consultarCajeros().stream()
                .filter(c -> idsActivos.contains(c.getIdCajero()))
                .collect(Collectors.toList());
    }

    @Override
    public Integer obtenerIdSupervisorDeAperturaActiva(int idCajero) {
        Document caja = this.corteCajaDAO.consultarUltimaCaja(idCajero);
        return (caja != null) ? caja.getInteger("idSupervisor") : null;
    }

    @Override
    public String obtenerNombreSupervisorPorId(int idSupervisor) {
        var entidad = this.supervisorDAO.consultarPorId(idSupervisor);
        return (entidad != null) ? entidad.getNombreCompleto() : "No encontrado";
    }

    @Override
    public List<supervisorDTO> consultarSupervisores() {
        return this.gestionSupervisoresBO.obtenerTodosLosSupervisores("");
    }

    @Override
    public List<supervisorDTO> obtenerSupervisoresFiltrados(String nombre) {
        return this.gestionSupervisoresBO.obtenerSupervisoresFiltrados(nombre);
    }

    @Override
    public boolean registrarSupervisor(supervisorDTO dto) {
        return this.gestionSupervisoresBO.registrarSupervisor(dto);
    }

    @Override
    public boolean editarSupervisor(supervisorDTO dto) {
        return this.gestionSupervisoresBO.editarSupervisor(dto);
    }

    @Override
    public boolean eliminarSupervisor(int idSupervisor) {
        return this.gestionSupervisoresBO.eliminarSupervisor(idSupervisor);
    }

    @Override
    public resumenVentasDTO generarResumenVentasTurno(int idCajero, Date fechaActual) {
        Document caja = this.corteCajaDAO.consultarUltimaCaja(idCajero);

        if (caja != null && "ABIERTA".equalsIgnoreCase(caja.getString("estado"))) {
            Date fechaInicio = caja.getDate("fechaApertura");
            return this.generadorResumenBO.generarResumenVentasTurno(idCajero, fechaInicio);
        }

        return new resumenVentasDTO(0.0, 0.0, 0.0, 0.0, 0.0);
    }

    @Override
    public double obtenerVentasTotalesPorCajero(int idEmpleado) {
        resumenVentasDTO resumen = generarResumenVentasTurno(idEmpleado, new Date());
        return (resumen != null) ? resumen.getTotalEsperadoSistema() : 0.0;
    }

    @Override
    public double calcularDiferencia(double totalEsperado, double totalReal) {
        return this.corteCajaBO.calcularDiferencia(totalEsperado, totalReal);
    }

    @Override
    public String evaluarEstadoCorte(double diferencia) {
        return this.corteCajaBO.evaluarEstadoCorte(diferencia);
    }

    @Override
    public boolean guardarNuevoCorte(corteCajaDTO datosCorte, List<desgloseDTO> listaDesgloses) {
        boolean guardado = this.corteCajaBO.guardarNuevoCorte(datosCorte, listaDesgloses);

        if (guardado) {
            if (datosCorte.getDiferencia() < 0) {
                double faltante = Math.abs(datosCorte.getDiferencia());
                this.adeudoBO.registrarAdeudoFaltante(datosCorte.getIdCajero(), faltante);
                this.cajeroDAO.actualizarAdeudoAcumulado(datosCorte.getIdCajero(), faltante);
            }
        }
        return guardado;
    }

    @Override
    public List<corteCajaDTO> consultarCortesRealizados(Date fechaInicio, Date fechaFin) {
        return this.corteCajaBO.consultarCortesRealizados(fechaInicio, fechaFin);
    }

    @Override
    public boolean eliminarCorteFisico(int idCorte) {
        return this.corteCajaDAO.eliminarCorte(idCorte);
    }


    @Override
    public boolean cancelarCorteLogico(corteCajaDTO corte, String motivo) {
        boolean cancelado = this.corteCajaDAO.actualizarEstadoCorte(corte.getIdCaja(), "Cancelado", motivo);

        if (cancelado && corte.getDiferencia() < 0) {
            double deudaARevertir = Math.abs(corte.getDiferencia());

            this.adeudoBO.liquidarAdeudo(corte.getIdCajero(), deudaARevertir);
            this.cajeroDAO.actualizarAdeudoAcumulado(corte.getIdCajero(), -deudaARevertir);
        }

        return cancelado;
    }

    @Override
    public boolean liquidarAdeudo(int idCajero, double montoPagado) {
        boolean liquidado = this.adeudoBO.liquidarAdeudo(idCajero, montoPagado);
        if (liquidado) {
            this.cajeroDAO.actualizarAdeudoAcumulado(idCajero, -montoPagado);
        }
        return liquidado;
    }
}