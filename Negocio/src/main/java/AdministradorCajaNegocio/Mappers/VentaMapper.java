package AdministradorCajaNegocio.Mappers;

import AdministradorCajaDTOs.ventaDTO;
import Entitys.venta;

public class VentaMapper {
    public static ventaDTO entityToDTO(venta entity) {
        if (entity == null) return null;
        return new ventaDTO(
                entity.getIdVenta(),
                entity.getMontoTotal(),
                entity.getIdMetodoPago(),
                entity.getIdCajero()
        );
    }
}