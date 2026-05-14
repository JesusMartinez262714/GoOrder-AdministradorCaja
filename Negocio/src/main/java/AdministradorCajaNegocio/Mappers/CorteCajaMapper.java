package AdministradorCajaNegocio.Mappers;

import AdministradorCajaDTOs.corteCajaDTO;
import AdministradorCajaPersistencia.Entitys.corteCaja;

import java.util.Date;

public class CorteCajaMapper {

    public static corteCajaDTO entityToDTO(corteCaja entidad, String nombreCajero) {
        corteCajaDTO dto = new corteCajaDTO();
        dto.setIdApertura(entidad.getIdApertura());
        dto.setId(entidad.getId());
        dto.setFecha(entidad.getFecha());
        dto.setMontoEsperado(entidad.getTotalEsperadoSistema());
        dto.setMontoReal(entidad.getTotalRealDeclarado());
        dto.setDiferencia(entidad.getDiferencia());
        dto.setEstado(entidad.getEstado());
        dto.setIdCajero(entidad.getIdCajero());
        dto.setCajero(nombreCajero);
        return dto;
    }

    public static corteCaja dtoToEntity(corteCajaDTO dto) {
        corteCaja entidad = new corteCaja();
        entidad.setId(dto.getId());
        entidad.setIdApertura(dto.getIdApertura());
        entidad.setFecha(new Date());
        entidad.setTotalEsperadoSistema(dto.getTotalEsperadoSistema());
        entidad.setTotalRealDeclarado(dto.getTotalRealDeclarado());
        entidad.setDiferencia(dto.getDiferencia());
        entidad.setEstado(dto.getEstado());
        entidad.setIdCajero(dto.getIdCajero());
        entidad.setIdSupervisor(dto.getIdSupervisor());

        return entidad;
    }
}