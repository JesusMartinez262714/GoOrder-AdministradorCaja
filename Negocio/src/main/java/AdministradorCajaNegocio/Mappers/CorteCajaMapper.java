package AdministradorCajaNegocio.Mappers;

import AdministradorCajaDTOs.corteCajaDTO;
import AdministradorCajaPersistencia.Entitys.corteCaja;

import java.util.Date;

/**
 * Clase encargada de transformar los datos de los cortes de caja entre la capa de
 * persistencia (Entidades) y la capa de negocio o presentación (DTOs).
 * Permite traspasar la información de montos, fechas de turno, estados de la caja y evidencia gráfica.
 * * @author Jesus Manuel Martinez Cortez
 * @version 1.1
 */
public class CorteCajaMapper {

    /**
     * Convierte una entidad corteCaja que proviene de la base de datos a un objeto
     * corteCajaDTO listo para ser utilizado por las pantallas del historial o resumen.
     * * @param entidad Objeto entidad de tipo corteCaja que proviene de la persistencia.
     * @param nombreCajero Nombre completo del cajero asociado al corte.
     * @return El objeto corteCajaDTO mapeado con toda su información, o null si la entidad es nula.
     */
    public static corteCajaDTO entityToDTO(corteCaja entidad, String nombreCajero) {
        if (entidad == null) {
            return null;
        }

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

        dto.setEvidenciaGrafica(entidad.getEvidenciaGrafica());

        return dto;
    }

    /**
     * Convierte un objeto corteCajaDTO proveniente de la vista a una entidad corteCaja
     * lista para ser procesada por el negocio o guardada en la base de datos.
     * * @param dto Objeto corteCajaDTO con los datos recolectados de la interfaz.
     * @return La entidad corteCaja configurada, o null si el DTO es nulo.
     */
    public static corteCaja dtoToEntity(corteCajaDTO dto) {
        if (dto == null) {
            return null;
        }

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

        entidad.setEvidenciaGrafica(dto.getEvidenciaGrafica());

        return entidad;
    }
}