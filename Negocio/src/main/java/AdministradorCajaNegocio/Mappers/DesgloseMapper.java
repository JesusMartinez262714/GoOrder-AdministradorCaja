package AdministradorCajaNegocio.Mappers;

import AdministradorCajaDTOs.desgloseDTO;
import AdministradorCajaPersistencia.Entitys.desgloseMontos;

public class DesgloseMapper {

    public static desgloseDTO entityToDTO(desgloseMontos entity) {
        if (entity == null) return null;

        return new desgloseDTO(
                entity.getMontoDeclarado(),
                entity.getIdMetodoPago(),
                entity.getNombreMetodo()
        );
    }


    public static desgloseMontos dtoToEntity(desgloseDTO dto) {
        if (dto == null) return null;

        desgloseMontos entity = new desgloseMontos();
        entity.setMontoDeclarado(dto.getMontoDeclarado());
        entity.setIdMetodoPago(dto.getIdMetodoPago());
        entity.setNombreMetodo(dto.getNombreMetodo());


        return entity;
    }
}