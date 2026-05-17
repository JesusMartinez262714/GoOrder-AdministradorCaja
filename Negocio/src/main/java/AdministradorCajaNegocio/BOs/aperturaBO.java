package AdministradorCajaNegocio.BOs;

import AdministradorCajaDTOs.aperturaCajaDTO;
import AdministradorCajaNegocio.Interfaces.IAperturaBO;
import AdministradorCajaPersistencia.Interfaces.ICorteCajaDAO;

public class aperturaBO implements IAperturaBO {

    private ICorteCajaDAO corteCajaDAO;

    public aperturaBO(ICorteCajaDAO dao) {
        this.corteCajaDAO = dao;
    }

    @Override
    public boolean registrarFondolnicial(aperturaCajaDTO apertura) {
        return corteCajaDAO.registrarApertura(
                apertura.getIdCajero(),
                apertura.getMontoInicial(),
                apertura.getIdSupervisor()
        );
    }
}