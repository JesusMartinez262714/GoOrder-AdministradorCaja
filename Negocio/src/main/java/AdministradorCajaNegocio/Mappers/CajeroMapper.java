package AdministradorCajaNegocio.Mappers;

import AdministradorCajaDTOs.cajeroDTO;
import AdministradorCajaPersistencia.Entitys.cajero;

/**
 * Clase encargada de transformar los datos de los cajeros entre la capa de
 * persistencia (Entidades) y la capa de negocio o presentación (DTOs).
 * Mapea los atributos individuales y maneja la conversión del nombre completo.
 * * @author Jesus Manuel Martinez Cortez
 */
public class CajeroMapper {

    /**
     * Convierte una entidad cajero que viene de la base de datos a un objeto
     * cajeroDTO listo para ser utilizado por la interfaz gráfica.
     * * @param e Objeto entidad de tipo cajero que proviene de la persistencia.
     * @return El objeto cajeroDTO con los datos mapeados, o null si la entidad es nula.
     */
    public static cajeroDTO entityToDTO(cajero e) {
        if (e == null) {
            return null;
        }

        cajeroDTO dto = new cajeroDTO(
                e.getIdCajero(),
                e.getNombre() + " " + e.getApellido(),
                e.getTurno()
        );

        dto.setMontoAdeudo(e.getMontoAdeudo());
        dto.setTieneAdeudo(e.getMontoAdeudo() > 0);

        return dto;
    }

    /**
     * Convierte un objeto cajeroDTO proveniente de la vista a una entidad cajero
     * lista para ser procesada o guardada en la base de datos.
     * Separa el nombre completo recibido en nombre y apellido independientes.
     * * @param d Objeto cajeroDTO con los datos recolectados de la interfaz.
     * @return La entidad cajero configurada, o null si el DTO es nulo.
     */
    public static cajero dtoToEntity(cajeroDTO d) {
        if (d == null) {
            return null;
        }

        cajero e = new cajero();
        e.setIdCajero(d.getIdCajero());

        String[] partes = d.getNombreCompleto().split(" ", 2);
        e.setNombre(partes[0]);
        e.setApellido(partes.length > 1 ? partes[1] : "");
        e.setTurno(d.getTurno());

        e.setMontoAdeudo(d.getMontoAdeudo());

        return e;
    }
}