package Mappers;

import GoOrderDTO.ProductoSeleccionadoDTO;

import java.util.ArrayList;
import java.util.List;

public class CarritoMapper {

    /**
     * Convierte de Persistencia a Negocio
     */
    public static GoOrderDTO.CarritoDTO toNegocio(DTOs.CarritoDTO cP) {
        if (cP == null) return null;

        GoOrderDTO.CarritoDTO cN = new GoOrderDTO.CarritoDTO();
        cN.setSubTotal(cP.getSubTotal());
        cN.setDescuento(cP.getDescuento());
        cN.setTotal(cP.getTotal());


        if (cP.getProductos() != null) {
            List<ProductoSeleccionadoDTO> listaProductos = new ArrayList<>();

        }

        return cN;
    }

    /**
     * Convierte de Negocio a Persistencia
     */
    public static DTOs.CarritoDTO toPersistencia(GoOrderDTO.CarritoDTO cN) {
        if (cN == null) return null;

        DTOs.CarritoDTO cP = new DTOs.CarritoDTO();
        cP.setSubTotal(cN.getSubTotal());
        cP.setDescuento(cN.getDescuento());
        cP.setTotal(cN.getTotal());


        return cP;
    }
}