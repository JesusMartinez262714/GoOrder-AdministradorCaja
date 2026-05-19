package AdministradorCajaNegocio.Mappers;

import AdministradorCajaDTOs.desgloseDTO;
import AdministradorCajaPersistencia.Entitys.desgloseMontos;

/**
 * Clase encargada de transformar los datos de los desgloses de montos entre la capa de
 * persistencia (Entidades) y la capa de negocio o presentación (DTOs).
 * Mapea el dinero contado en el arqueo según el método de pago correspondiente.
 * * @author Jesus Manuel Martinez Cortez
 */
public class DesgloseMapper {

    /**
     * Convierte una entidad desgloseMontos que viene de la base de datos a un objeto
     * desgloseDTO listo para ser mostrado en la interfaz del ticket o la conciliación.
     * * @param entity Objeto entidad de tipo desgloseMontos que proviene de la persistencia.
     * @return El objeto desgloseDTO con los datos mapeados, o null si la entidad es nula.
     */
    public static desgloseDTO entityToDTO(desgloseMontos entity) {
        if (entity == null) {
            return null;
        }

        return new desgloseDTO(
                entity.getMontoDeclarado(),
                entity.getIdMetodoPago(),
                entity.getNombreMetodo()
        );
    }

    /**
     * Convierte un objeto desgloseDTO proveniente de la vista a una entidad desgloseMontos
     * lista para ser procesada por el negocio o guardada en la base de datos.
     * * @param dto Objeto desgloseDTO con los datos recolectados de la interfaz.
     * @return La entidad desgloseMontos configurada, o null si el DTO es nulo.
     */
    public static desgloseMontos dtoToEntity(desgloseDTO dto) {
        if (dto == null) {
            return null;
        }

        desgloseMontos entity = new desgloseMontos();
        entity.setMontoDeclarado(dto.getMontoDeclarado());
        entity.setIdMetodoPago(dto.getIdMetodoPago());
        entity.setNombreMetodo(dto.getNombreMetodo());

        return entity;
    }
}