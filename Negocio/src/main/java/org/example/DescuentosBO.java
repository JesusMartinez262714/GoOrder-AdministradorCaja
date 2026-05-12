package org.example;

import GoOrderDTO.CodigoDescuentoDTO;
import Interfaces.IDescuentosBO;
import Interfaces.IDescuentosDAO;
import Mappers.CodigoDescuentoMapper;
import goorderpersistencia.PersistenciaException;

/**
 *
 * @author juanl
 */
public class DescuentosBO implements IDescuentosBO{

    private IDescuentosDAO descuentosDAO;

    public DescuentosBO(IDescuentosDAO descuentosDAO) {
        this.descuentosDAO = descuentosDAO;
    }

    @Override
    public CodigoDescuentoDTO BuscarDescuento(String codigo) throws NegocioException {
        try {
            DTOs.CodigoDescuentoDTO descuentoPersistencia = descuentosDAO.BuscarDescuento(codigo);

            return CodigoDescuentoMapper.toNegocio(descuentoPersistencia);

        } catch(PersistenciaException ex) {
            throw new NegocioException("No se pudo consultar el codigo");
        }
    }

    @Override
    public CodigoDescuentoDTO CambiarEstadoDescuento(String codigo) throws NegocioException {
        try {
            DTOs.CodigoDescuentoDTO descuentoActualizado = descuentosDAO.cambiarEstado(codigo);

            return CodigoDescuentoMapper.toNegocio(descuentoActualizado);

        } catch(PersistenciaException ex) {
            throw new NegocioException("No se pudo cambiar estado el codigo");
        }
    }
}