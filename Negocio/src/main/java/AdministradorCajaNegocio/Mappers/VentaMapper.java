package AdministradorCajaNegocio.Mappers;

import AdministradorCajaDTOs.ventaDTO;
import Entitys.venta;

/**
 * Clase encargada de transformar los datos de las ventas entre la capa de
 * persistencia (Entidades) y la capa de negocio o presentación (DTOs).
 * Permite traspasar la información de los montos y métodos de pago de cada transacción.
 * * @author Jesus Manuel Martinez Cortez
 */
public class VentaMapper {

    /**
     * Convierte una entidad venta que proviene de la base de datos a un objeto
     * ventaDTO listo para ser procesado y sumado en el cálculo de totales del turno.
     * * @param entity Objeto entidad de tipo venta que proviene de la persistencia.
     * @return El objeto ventaDTO con los datos de la transacción mapeados, o null si la entidad es nula.
     */
    public static ventaDTO entityToDTO(venta entity) {
        if (entity == null) {
            return null;
        }
        return new ventaDTO(
                entity.getIdVenta(),
                entity.getMontoTotal(),
                entity.getIdMetodoPago(),
                entity.getIdCajero()
        );
    }
}