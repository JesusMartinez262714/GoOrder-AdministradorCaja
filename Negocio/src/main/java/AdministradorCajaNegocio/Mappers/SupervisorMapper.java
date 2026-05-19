package AdministradorCajaNegocio.Mappers;

import AdministradorCajaDTOs.supervisorDTO;
import AdministradorCajaPersistencia.Entitys.supervisor;

/**
 * Clase encargada de transformar los datos de los supervisores entre la capa de
 * persistencia (Entidades) y la capa de negocio o presentación (DTOs).
 * Mapea los atributos de identificación y maneja la conversión del nombre completo.
 * * @author Jesus Manuel Martinez Cortez
 */
public class SupervisorMapper {

    /**
     * Convierte una entidad supervisor que viene de la base de datos a un objeto
     * supervisorDTO listo para ser utilizado por las pantallas del sistema.
     * * @param e Objeto entidad de tipo supervisor que proviene de la persistencia.
     * @return El objeto supervisorDTO con los datos mapeados, o null si la entidad es nula.
     */
    public static supervisorDTO entityToDTO(supervisor e) {
        if (e == null) {
            return null;
        }
        return new supervisorDTO(e.getIdSupervisor(), e.getNombre() + " " + e.getApellido());
    }

    /**
     * Convierte un objeto supervisorDTO proveniente de la vista a una entidad supervisor
     * lista para ser procesada o guardada en la base de datos.
     * Separa el nombre completo recibido en nombre y apellido independientes.
     * * @param d Objeto supervisorDTO con los datos recolectados de la interfaz.
     * @return La entidad supervisor configurada, o null si el DTO es nulo.
     */
    public static supervisor dtoToEntity(supervisorDTO d) {
        if (d == null) {
            return null;
        }
        supervisor e = new supervisor();
        e.setIdSupervisor(d.getIdSupervisor());

        String[] partes = d.getNombreCompleto().split(" ", 2);
        e.setNombre(partes[0]);
        e.setApellido(partes.length > 1 ? partes[1] : "");

        return e;
    }
}