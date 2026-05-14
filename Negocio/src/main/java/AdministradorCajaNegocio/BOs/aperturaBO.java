package AdministradorCajaNegocio.BOs;

import AdministradorCajaDTOs.aperturaCajaDTO;
import AdministradorCajaNegocio.Interfaces.IAperturaBO;
import AdministradorCajaNegocio.Mappers.AperturaMapper;
import AdministradorCajaPersistencia.Interfaces.IAperturaCajaDAO;

public class aperturaBO implements IAperturaBO {
    private IAperturaCajaDAO AperturaDAO;

    public aperturaBO(IAperturaCajaDAO dao) {
        this.AperturaDAO = dao;
    }

    @Override
    public boolean registrarFondolnicial(aperturaCajaDTO apertura) {
        return AperturaDAO.insertarApertura(AperturaMapper.dtoToEntity(apertura));
    }
}