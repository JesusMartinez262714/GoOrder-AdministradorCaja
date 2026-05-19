package AdministradorCajaNegocio.Mappers;

import AdministradorCajaDTOs.corteCajaDTO;
import AdministradorCajaPersistencia.Entitys.corteCaja;

import java.util.Date;

public class CorteCajaMapper {

    public static corteCajaDTO entityToDTO(corteCaja entidad, String nombreCajero) {
        corteCajaDTO dto = new corteCajaDTO();

            dto.setIdCaja(entidad.getIdCaja());

        dto.setFecha(entidad.getFecha());
        dto.setMontoEsperado(entidad.getTotalEsperadoSistema());
        dto.setMontoReal(entidad.getTotalRealDeclarado());
        dto.setDiferencia(entidad.getDiferencia());
        dto.setEstado(entidad.getEstado());
        dto.setIdCajero(entidad.getIdCajero());
        dto.setCajero(nombreCajero);
        dto.setMotivoCancelacion(entidad.getMotivoCancelacion());
        dto.setFechaApertura(entidad.getFechaApertura());
        dto.setObservaciones(entidad.getObservaciones());

        return dto;
    }

    public static corteCaja dtoToEntity(corteCajaDTO dto) {
        corteCaja entidad = new corteCaja();

        entidad.setIdCaja(dto.getIdCaja());

        entidad.setFecha(dto.getFecha() != null ? dto.getFecha() : new Date());

        entidad.setTotalEsperadoSistema(dto.getTotalEsperadoSistema());
        entidad.setTotalRealDeclarado(dto.getTotalRealDeclarado());
        entidad.setDiferencia(dto.getDiferencia());
        entidad.setEstado(dto.getEstado());
        entidad.setIdCajero(dto.getIdCajero());
        entidad.setIdSupervisor(dto.getIdSupervisor());
        entidad.setMotivoCancelacion(dto.getMotivoCancelacion());
        entidad.setFechaApertura(dto.getFechaApertura());
        entidad.setObservaciones(dto.getObservaciones());

        return entidad;
    }
}