package AdministradorCajaNegocio.BOs;

import AdministradorCajaDTOs.aperturaCajaDTO;
import AdministradorCajaNegocio.Interfaces.IAperturaBO;
import AdministradorCajaPersistencia.Interfaces.ICorteCajaDAO;

/**
 * Clase de lógica de negocio encargada de gestionar los procesos de apertura de caja.
 * Sirve para validar y enviar los datos del fondo fijo inicial hacia la capa de persistencia.
 * * @author Jesus Manuel Martinez Cortez
 */
public class aperturaBO implements IAperturaBO {

    private ICorteCajaDAO corteCajaDAO;

    /**
     * Constructor que recibe el DAO de cortes de caja para conectar la capa de negocio
     * con el acceso a la base de datos.
     * * @param dao Instancia del objeto de acceso a datos para el control de la caja.
     */
    public aperturaBO(ICorteCajaDAO dao) {
        this.corteCajaDAO = dao;
    }

    /**
     * Envía los datos de la nueva apertura hacia la base de datos para registrar
     * el fondo inicial en caja, el cajero asignado y el supervisor encargado.
     * * @param apertura Objeto DTO que contiene la información del turno que se va a abrir.
     * @return true si la apertura se registró correctamente en la base de datos, false en caso contrario.
     */
    @Override
    public boolean registrarFondolnicial(aperturaCajaDTO apertura) {
        if (apertura == null) {
            return false;
        }
        return corteCajaDAO.registrarApertura(
                apertura.getIdCajero(),
                apertura.getMontoInicial(),
                apertura.getIdSupervisor()
        );
    }
}