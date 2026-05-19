package AdministradorCajaPersistencia.Mappers;

import AdministradorCajaPersistencia.Entitys.supervisor;
import org.bson.Document;

public class SupervisorPersistenciaMapper {

    /**
     * Convierte un Document a una Entidad supervisor.
     */
    public static supervisor documentToEntity(Document doc) {
        if (doc == null) return null;

        supervisor entidad = new supervisor();
        entidad.setIdSupervisor(doc.getInteger("idSupervisor") != null ? doc.getInteger("idSupervisor") : 0);
        entidad.setNombre(doc.getString("nombre"));
        entidad.setApellido(doc.getString("apellido"));

        return entidad;
    }

    /**
     * Convierte una Entidad supervisor a un Document.
     */
    public static Document entityToDocument(supervisor entidad) {
        if (entidad == null) return null;

        int idSup = entidad.getIdSupervisor() > 0 ? entidad.getIdSupervisor() : (int) (System.currentTimeMillis() / 1000);

        return new Document("idSupervisor", idSup)
                .append("nombre", entidad.getNombre())
                .append("apellido", entidad.getApellido())
                .append("nombreCompleto", entidad.getNombreCompleto());
    }
}