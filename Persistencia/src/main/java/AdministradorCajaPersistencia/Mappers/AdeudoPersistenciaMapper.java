package AdministradorCajaPersistencia.Mappers;

import AdministradorCajaPersistencia.Entitys.adeudo;
import org.bson.Document;

public class AdeudoPersistenciaMapper {

    /**
     * Convierte un Document a una Entidad adeudo.
     */
    public static adeudo documentToEntity(Document doc) {
        if (doc == null) return null;

        adeudo entidad = new adeudo();
        entidad.setIdCajero(doc.getInteger("idCajero") != null ? doc.getInteger("idCajero") : 0);
        entidad.setEstado(doc.getString("estado"));
        entidad.setMonto(obtenerDoubleSeguro(doc, "monto"));

        return entidad;
    }

    /**
     * Convierte una Entidad adeudo a un Document .
     */
    public static Document entityToDocument(adeudo entidad) {
        if (entidad == null) return null;

        return new Document("idCajero", entidad.getIdCajero())
                .append("monto", entidad.getMonto())
                .append("estado", entidad.getEstado());
    }


    private static double obtenerDoubleSeguro(Document doc, String campo) {
        Object valor = doc.get(campo);
        if (valor instanceof Number) {
            return ((Number) valor).doubleValue();
        }
        return 0.0;
    }
}