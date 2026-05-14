package org.example;


import DTOs.SucursalDTO;
import Interfaces.ISucursalesBO;
import Interfaces.ISucursalesDAO;
import Mappers.SucursalMapper;
import goorderpersistencia.PersistenciaException;
import goorderpersistencia.SucursalesDAO;
import java.util.ArrayList;
import java.util.List;

public class SucursalesBO implements ISucursalesBO{

    private ISucursalesDAO sucursalDAO;

    public SucursalesBO( ) {
        this.sucursalDAO = new SucursalesDAO();
    }

    @Override
    public List<GoOrderDTO.SucursalDTO> consultarSucursales() throws NegocioException {
        try {
            List<SucursalDTO> sucursalesPersistencia = sucursalDAO.consultarSucursales();

            if(sucursalesPersistencia.isEmpty()){
                throw new NegocioException("No hay sucursales disponibles");
            }

            List<GoOrderDTO.SucursalDTO> sucursalesNegocio = new ArrayList<>();
            for(SucursalDTO sP : sucursalesPersistencia) {
                sucursalesNegocio.add(SucursalMapper.toNegocio(sP));
            }

            return sucursalesNegocio;

        } catch(PersistenciaException e) {
            throw new NegocioException("No fue posible consultar las sucursales.");
        }
    }
}