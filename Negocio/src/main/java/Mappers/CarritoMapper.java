package Mappers;

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
            List<GoOrderDTO.ProductoSeleccionadoDTO> productosN = new ArrayList<>();

            for (DTOs.ProductoSeleccionadoDTO pP : cP.getProductos()) {
                GoOrderDTO.ProductoSeleccionadoDTO pN = new GoOrderDTO.ProductoSeleccionadoDTO();
                pN.setNombre(pP.getNombre());
                pN.setCantidad(pP.getCantidad());
                pN.setImporte(pP.getImporte());
                pN.setPrecioActual(pP.getPrecioActual());

                if (pP.getProducto() != null) {
                    GoOrderDTO.ProductoDTO productoInternoN = new GoOrderDTO.ProductoDTO(
                            pP.getProducto().getNombre(),
                            pP.getProducto().getDescripcion(),
                            pP.getProducto().getPrecio(),
                            pP.getProducto().getImagen()
                    );
                    pN.setProducto(productoInternoN);
                }

                productosN.add(pN);
            }
            cN.setProductos(productosN);
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

        if (cN.getProductos() != null) {
            List<DTOs.ProductoSeleccionadoDTO> productosP = new ArrayList<>();

            for (GoOrderDTO.ProductoSeleccionadoDTO pN : cN.getProductos()) {
                DTOs.ProductoSeleccionadoDTO pP = new DTOs.ProductoSeleccionadoDTO();
                pP.setNombre(pN.getNombre());
                pP.setCantidad(pN.getCantidad());
                pP.setImporte(pN.getImporte());
                pP.setPrecioActual(pN.getPrecioActual());

                if (pN.getProducto() != null) {

                    DTOs.ProductoDTO productoInternoP = new DTOs.ProductoDTO(
                            pN.getProducto().getNombre(),
                            pN.getProducto().getDescripcion(),
                            pN.getProducto().getPrecio(),
                            pN.getProducto().getImagen()
                    );
                    pP.setProducto(productoInternoP);
                }

                productosP.add(pP);
            }
            cP.setProductos(productosP);
        }

        return cP;
    }
}