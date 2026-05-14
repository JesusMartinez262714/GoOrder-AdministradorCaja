package AdministradorCajaPersistencia.Mappers;

import AdministradorCajaPersistencia.Entitys.cajero;
import org.bson.Document;

public class CajeroPersistenciaMapper {


    public static cajero documentToEntity(Document doc) {
        if (doc == null) return null;

        cajero c = new cajero();
        c.setIdCajero(doc.getInteger("idCajero"));
        c.setNombre(doc.getString("nombre"));
        c.setApellido(doc.getString("apellido"));
        c.setTurno(doc.getString("turno"));

        return c;
    }


    public static Document entityToDocument(cajero entidad) {
        if (entidad == null) return null;

        return new Document("idCajero", entidad.getIdCajero())
                .append("nombre", entidad.getNombre())
                .append("apellido", entidad.getApellido())
                .append("turno", entidad.getTurno());
    }
}