package AdministradorCajaNegocio.Mappers;

import AdministradorCajaDTOs.aperturaCajaDTO;
import AdministradorCajaPersistencia.Entitys.aperturaCaja;

public class AperturaMapper {
    public static aperturaCaja dtoToEntity(aperturaCajaDTO dto) {
        if (dto == null) return null;
        aperturaCaja entity = new aperturaCaja();
        entity.setIdCajero(dto.getIdCajero());
        entity.setMontoInicial(dto.getMontoInicial());
        entity.setIdSupervisor(dto.getIdSupervisor());
        return entity;
    }
}