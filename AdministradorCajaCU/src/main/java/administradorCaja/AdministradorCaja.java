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

/**
 * Componente principal y Fachada (Facade) del subsistema de administración de caja.
 * Coordina las llamadas entre las vistas de la interfaz y los objetos de negocio (BOs),
 * sirviendo como único punto de entrada para gestionar aperturas, cierres, cajeros,
 * supervisores y control de deudas.
 * * @author Jesus Manuel Martinez Cortez
 */
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

    /**
     * Constructor que inicializa y acopla las dependencias de los objetos de negocio
     * y los objetos de acceso a datos (DAOs) requeridos por el componente.
     * * @param vDAO   Instancia para el acceso a datos de ventas.
     * @param cDAO   Instancia para el acceso a datos de cortes de caja.
     * @param cajDAO Instancia para el acceso a datos de cajeros.
     * @param adeDAO Instancia para el acceso a datos de adeudos.
     * @param supDAO Instancia para el acceso a datos de supervisores.
     */
    public AdministradorCaja(IVentaDAO vDAO, ICorteCajaDAO cDAO,
                             ICajeroDAO cajDAO, IAdeudoDAO adeDAO, ISupervisorDAO supDAO) {
        if (vDAO != null && cDAO != null && cajDAO != null && adeDAO != null && supDAO != null) {
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
    }

    /**
     * Obtiene una lista de cajeros aplicando filtros por nombre, turno y estado de deudas.
     * * @param n Nombre parcial o completo del cajero.
     * @param t Turno de trabajo específico o "Todos".
     * @param d Estado de la deuda o "Todos".
     * @return Lista de objetos DTO con los cajeros que cumplen los criterios.
     */
    @Override
    public List<cajeroDTO> obtenerCajerosFiltrados(String n, String t, String d) {
        if (this.gestionCajerosBO == null) {
            return new ArrayList<>();
        }
        return this.gestionCajerosBO.obtenerCajerosFiltrados(n, t, d);
    }

    /**
     * Registra un nuevo cajero en el sistema a través del objeto de negocio.
     * * @param dto Objeto con los datos del cajero a registrar.
     * @return true si el registro fue exitoso, false en caso contrario.
     */
    @Override
    public boolean registrarCajero(cajeroDTO dto) {
        if (dto == null || this.gestionCajerosBO == null) {
            return false;
        }
        return this.gestionCajerosBO.registrarCajero(dto);
    }

    /**
     * Modifica los datos de un cajero existente en el sistema.
     * * @param dto Objeto con los datos actualizados del cajero.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    @Override
    public boolean editarCajero(cajeroDTO dto) {
        if (dto == null || this.gestionCajerosBO == null) {
            return false;
        }
        return this.gestionCajerosBO.editarCajero(dto);
    }

    /**
     * Elimina a un cajero del sistema por medio de su identificador único.
     * * @param idCajero ID único del cajero a eliminar.
     * @return true si fue eliminado, false si tiene deudas o no se pudo completar.
     */
    @Override
    public boolean eliminarCajero(int idCajero) {
        if (idCajero <= 0 || this.gestionCajerosBO == null) {
            return false;
        }
        return this.gestionCajerosBO.eliminarCajero(idCajero);
    }

    /**
     * Consulta el catálogo completo de cajeros registrados en el sistema sin filtros.
     * * @return Lista de objetos DTO con todos los cajeros del sistema.
     */
    @Override
    public List<cajeroDTO> consultarCajeros() {
        return obtenerCajerosFiltrados("", "Todos", "Todos");
    }

    /**
     * Registra la apertura de un turno de caja, validando que el cajero asignado
     * no posea un turno activo de manera simultánea.
     * * @param apertura Objeto DTO con los datos de inicio del turno.
     * @return true si la apertura fue autorizada y guardada, false si ya hay una activa.
     */
    @Override
    public boolean registrarApertura(aperturaCajaDTO apertura) {
        if (apertura == null || this.corteCajaDAO == null || this.aperturaBO == null) {
            return false;
        }
        if (this.corteCajaDAO.tieneAperturaActiva(apertura.getIdCajero())) {
            return false;
        }
        return this.aperturaBO.registrarFondolnicial(apertura);
    }

    /**
     * Filtra y obtiene los cajeros que actualmente se encuentran con una caja abierta.
     * * @return Lista de objetos DTO de cajeros con turnos activos.
     */
    @Override
    public List<cajeroDTO> consultarCajerosConTurnoActivo() {
        if (this.corteCajaDAO == null) {
            return new ArrayList<>();
        }
        List<Integer> idsActivos = this.corteCajaDAO.obtenerIdsCajerosConCajaAbierta();
        if (idsActivos == null || idsActivos.isEmpty()) {
            return new ArrayList<>();
        }

        return consultarCajeros().stream()
                .filter(c -> c != null && idsActivos.contains(c.getIdCajero()))
                .collect(Collectors.toList());
    }

    /**
     * Recupera el ID del supervisor que autorizó la apertura del turno vigente del cajero.
     * * @param idCajero ID único del cajero operando la caja.
     * @return El identificador del supervisor, o null si no se encuentra registro.
     */
    @Override
    public Integer obtenerIdSupervisorDeAperturaActiva(int idCajero) {
        if (idCajero <= 0 || this.corteCajaDAO == null) {
            return null;
        }
        Document caja = this.corteCajaDAO.consultarUltimaCaja(idCajero);
        return (caja != null) ? caja.getInteger("idSupervisor") : null;
    }

    /**
     * Consulta el nombre completo de un supervisor a partir de su ID único.
     * * @param idSupervisor ID único del supervisor.
     * @return Cadena de texto con el nombre completo, o "No encontrado".
     */
    @Override
    public String obtenerNombreSupervisorPorId(int idSupervisor) {
        if (idSupervisor <= 0 || this.supervisorDAO == null) {
            return "No encontrado";
        }
        var entidad = this.supervisorDAO.consultarPorId(idSupervisor);
        return (entidad != null) ? entidad.getNombreCompleto() : "No encontrado";
    }

    /**
     * Consulta la lista general de todos los supervisores registrados.
     * * @return Lista de objetos DTO con todos los supervisores.
     */
    @Override
    public List<supervisorDTO> consultarSupervisores() {
        if (this.gestionSupervisoresBO == null) {
            return new ArrayList<>();
        }
        return this.gestionSupervisoresBO.obtenerTodosLosSupervisores("");
    }

    /**
     * Filtra la lista de supervisores basándose en coincidencias por su nombre.
     * * @param nombre Texto o nombre parcial del supervisor.
     * @return Lista de objetos DTO que coinciden con el filtro.
     */
    @Override
    public List<supervisorDTO> obtenerSupervisoresFiltrados(String nombre) {
        if (this.gestionSupervisoresBO == null) {
            return new ArrayList<>();
        }
        return this.gestionSupervisoresBO.obtenerSupervisoresFiltrados(nombre);
    }

    /**
     * Registra un nuevo supervisor en el sistema de base de datos.
     * * @param dto Objeto DTO con los datos del supervisor.
     * @return true si el registro concluyó exitosamente, false en caso contrario.
     */
    @Override
    public boolean registrarSupervisor(supervisorDTO dto) {
        if (dto == null || this.gestionSupervisoresBO == null) {
            return false;
        }
        return this.gestionSupervisoresBO.registrarSupervisor(dto);
    }

    /**
     * Modifica los datos de identificación de un supervisor existente.
     * * @param dto Objeto DTO con los cambios del supervisor.
     * @return true si se actualizó el registro, false en caso contrario.
     */
    @Override
    public boolean editarSupervisor(supervisorDTO dto) {
        if (dto == null || this.gestionSupervisoresBO == null) {
            return false;
        }
        return this.gestionSupervisoresBO.editarSupervisor(dto);
    }

    /**
     * Remueve el registro de un supervisor mediante su ID único.
     * * @param idSupervisor ID único del supervisor a dar de baja.
     * @return true si la eliminación fue correcta, false en caso contrario.
     */
    @Override
    public boolean eliminarSupervisor(int idSupervisor) {
        if (idSupervisor <= 0 || this.gestionSupervisoresBO == null) {
            return false;
        }
        return this.gestionSupervisoresBO.eliminarSupervisor(idSupervisor);
    }

    /**
     * Genera un desglose financiero de las ventas acumuladas del cajero desde la fecha
     * en que abrió su turno actual hasta el momento de la consulta.
     * * @param idCajero    ID único del cajero.
     * @param fechaActual Fecha del corte de control del sistema.
     * @return DTO resumenVentasDTO con la suma de los totales por método de pago.
     */
    @Override
    public resumenVentasDTO generarResumenVentasTurno(int idCajero, Date fechaActual) {
        if (idCajero <= 0 || this.corteCajaDAO == null || this.generadorResumenBO == null) {
            return new resumenVentasDTO(0.0, 0.0, 0.0, 0.0, 0.0);
        }
        Document caja = this.corteCajaDAO.consultarUltimaCaja(idCajero);

        if (caja != null && "ABIERTA".equalsIgnoreCase(caja.getString("estado"))) {
            Date fechaInicio = caja.getDate("fechaApertura");
            return this.generadorResumenBO.generarResumenVentasTurno(idCajero, fechaInicio);
        }

        return new resumenVentasDTO(0.0, 0.0, 0.0, 0.0, 0.0);
    }

    /**
     * Obtiene el monto global esperado por el sistema para un cajero en específico.
     * * @param idEmpleado ID del cajero a evaluar.
     * @return Suma de las transacciones registradas en formato double.
     */
    @Override
    public double obtenerVentasTotalesPorCajero(int idEmpleado) {
        resumenVentasDTO resumen = generarResumenVentasTurno(idEmpleado, new Date());
        return (resumen != null) ? resumen.getTotalEsperadoSistema() : 0.0;
    }

    /**
     * Calcula la diferencia monetaria entre lo ingresado en el sistema y lo contado.
     * * @param totalEsperado Cantidad calculada por el sistema de ventas.
     * @param totalReal     Cantidad física declarada.
     * @return Valor double de la diferencia (negativo indica faltante).
     */
    @Override
    public double calcularDiferencia(double totalEsperado, double totalReal) {
        if (this.corteCajaBO == null) {
            return 0.0;
        }
        return this.corteCajaBO.calcularDiferencia(totalEsperado, totalReal);
    }

    /**
     * Evalúa el estado que le corresponde al corte de caja según su balance.
     * * @param diferencia Margen de diferencia calculado.
     * @return Cadena de texto con el estado descriptivo.
     */
    @Override
    public String evaluarEstadoCorte(double diferencia) {
        if (this.corteCajaBO == null) {
            return "Vigente";
        }
        return this.corteCajaBO.evaluarEstadoCorte(diferencia);
    }

    /**
     * Guarda el registro definitivo del corte de caja en la base de datos. Si se
     * detecta un faltante monetario, genera de forma automática un registro de adeudo
     * y actualiza el balance acumulado del cajero.
     * * @param datosCorte    DTO con la información general y totales del corte.
     * @param listaDesgloses Colección de desgloses de efectivo y terminales bancarias.
     * @return true si la transacción e inserciones concluyeron con éxito, false en caso contrario.
     */
    @Override
    public boolean guardarNuevoCorte(corteCajaDTO datosCorte, List<desgloseDTO> listaDesgloses) {
        if (datosCorte == null || this.corteCajaBO == null || this.adeudoBO == null || this.cajeroDAO == null) {
            return false;
        }

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

    /**
     * Consulta el historial de los cortes de caja almacenados dentro de un rango de fechas.
     * * @param fechaInicio Límite de inicio para la consulta.
     * @param fechaFin    Límite de fin para la consulta.
     * @return Lista de objetos DTO de cortes de caja encontrados.
     */
    @Override
    public List<corteCajaDTO> consultarCortesRealizados(Date fechaInicio, Date fechaFin) {
        if (this.corteCajaBO == null) {
            return new ArrayList<>();
        }
        return this.corteCajaBO.consultarCortesRealizados(fechaInicio, fechaFin);
    }

    /**
     * Elimina permanentemente un documento de corte de caja de la base de datos.
     * * @param idCorte ID único de la caja o corte a eliminar.
     * @return true si el registro fue borrado físicamente, false en caso contrario.
     */
    @Override
    public boolean eliminarCorteFisico(int idCorte) {
        if (idCorte <= 0 || this.corteCajaDAO == null) {
            return false;
        }
        return this.corteCajaDAO.eliminarCorte(idCorte);
    }

    /**
     * Aplica una cancelación lógica a un corte, cambiando su estado a "Cancelado" e
     * inyectando los motivos. Si el corte tenía deudas asociadas, revierte el saldo
     * restándolo de la cuenta del cajero para mantener el balance limpio.
     * * @param corte  Objeto DTO con los datos del corte a cancelar.
     * @param motivo Justificación detallada de la cancelación del movimiento.
     * @return true si la cancelación lógica y la reversión concluyeron con éxito, false si falló.
     */
    @Override
    public boolean cancelarCorteLogico(corteCajaDTO corte, String motivo) {
        if (corte == null || motivo == null || this.corteCajaDAO == null || this.adeudoBO == null || this.cajeroDAO == null) {
            return false;
        }

        boolean cancelado = this.corteCajaDAO.actualizarEstadoCorte(corte.getIdCaja(), "Cancelado", motivo);

        if (cancelado && corte.getDiferencia() < 0) {
            double deudaARevertir = Math.abs(corte.getDiferencia());
            this.adeudoBO.liquidarAdeudo(corte.getIdCajero(), deudaARevertir);
            this.cajeroDAO.actualizarAdeudoAcumulado(corte.getIdCajero(), -deudaARevertir);
        }

        return cancelado;
    }

    /**
     * Procesa la liquidación o abonos monetarios al saldo de deudas de un cajero,
     * restando el importe de su cuenta y de su balance global.
     * * @param idCajero    ID único del cajero que realiza el pago.
     * @param montoPagado Cantidad de dinero a abonar.
     * @return true si la actualización de pago en las colecciones fue correcta, false de lo contrario.
     */
    @Override
    public boolean liquidarAdeudo(int idCajero, double montoPagado) {
        if (idCajero <= 0 || montoPagado <= 0 || this.adeudoBO == null || this.cajeroDAO == null) {
            return false;
        }

        boolean liquidado = this.adeudoBO.liquidarAdeudo(idCajero, montoPagado);
        if (liquidado) {
            this.cajeroDAO.actualizarAdeudoAcumulado(idCajero, -montoPagado);
        }
        return liquidado;
    }
}