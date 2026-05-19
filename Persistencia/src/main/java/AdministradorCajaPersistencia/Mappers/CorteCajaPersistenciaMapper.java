package AdministradorCajaPersistencia.Mappers;

import AdministradorCajaPersistencia.Entitys.corteCaja;
import AdministradorCajaPersistencia.Entitys.desgloseMontos;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase utilitaria encargada de mapear los datos entre los documentos nativos
 * de MongoDB (Document) y las entidades del modelo de persistencia (corteCaja).
 * * @author Jesus Manuel Martinez Cortez
 */
public class CorteCajaPersistenciaMapper {

    /**
     * Traduce un Document de MongoDB a una Entidad corteCaja.
     * * @param doc Documento proveniente de la base de datos.
     * @return Instancia de la entidad corteCaja mapeada, o null si el documento es nulo.
     */
    public static corteCaja documentToEntity(Document doc) {
        if (doc == null) return null;

        corteCaja entidad = new corteCaja();
        entidad.setIdCaja(doc.getInteger("idCaja") != null ? doc.getInteger("idCaja") : 0);
        entidad.setFecha(doc.getDate("fechaCorte"));
        entidad.setFechaApertura(doc.getDate("fechaApertura"));
        entidad.setIdCajero(doc.getInteger("idCajero") != null ? doc.getInteger("idCajero") : 0);
        entidad.setEstado(doc.getString("estado"));
        entidad.setObservaciones(doc.getString("observaciones"));
        entidad.setMotivoCancelacion(doc.getString("motivoCancelacion"));

        entidad.setTotalEsperadoSistema(obtenerDoubleSeguro(doc, "montoEsperado"));
        entidad.setTotalRealDeclarado(obtenerDoubleSeguro(doc, "montoReal"));
        entidad.setDiferencia(obtenerDoubleSeguro(doc, "diferencia"));

        // Mapeo de la evidencia gráfica en formato Base64 hacia la entidad
        entidad.setEvidenciaGrafica(doc.getString("evidenciaGrafica"));

        List<Document> desgloseDocs = doc.getList("desgloses", Document.class);
        if (desgloseDocs != null) {
            List<desgloseMontos> listaDesgloses = new ArrayList<>();
            for (Document subDoc : desgloseDocs) {
                if (subDoc != null) {
                    listaDesgloses.add(subDocumentToEntity(subDoc));
                }
            }
            entidad.setListaDesglose(listaDesgloses);
        }

        return entidad;
    }

    /**
     * Traduce una Entidad corteCaja a un Document de MongoDB para su almacenamiento.
     * * @param entidad Objeto de negocio con los datos del corte.
     * @return Documento estructurado para MongoDB, o null si la entidad es nula.
     */
    public static Document entityToDocument(corteCaja entidad) {
        if (entidad == null) return null;

        return new Document("idCaja", entidad.getIdCaja())
                .append("fechaCorte", entidad.getFecha())
                .append("fechaApertura", entidad.getFechaApertura())
                .append("idCajero", entidad.getIdCajero())
                .append("estado", entidad.getEstado())
                .append("observaciones", entidad.getObservaciones())
                .append("motivoCancelacion", entidad.getMotivoCancelacion())
                .append("montoEsperado", entidad.getTotalEsperadoSistema())
                .append("montoReal", entidad.getTotalRealDeclarado())
                .append("diferencia", entidad.getDiferencia())
                .append("evidenciaGrafica", entidad.getEvidenciaGrafica())
                .append("desgloses", desglosesToDocumentList(entidad.getListaDesglose()));
    }

    /**
     * Traduce un objeto de desglose interno (Subdocumento) a su Entidad.
     */
    private static desgloseMontos subDocumentToEntity(Document subDoc) {
        if (subDoc == null) return null;
        desgloseMontos d = new desgloseMontos();
        d.setNombreMetodo(subDoc.getString("metodo"));
        d.setMontoDeclarado(obtenerDoubleSeguro(subDoc, "montoDeclarado"));
        return d;
    }

    /**
     * Traduce una lista de objetos desgloseMontos a subdocumentos de MongoDB para guardarse.
     */
    public static List<Document> desglosesToDocumentList(List<desgloseMontos> listaDesgloses) {
        List<Document> subDocsDesglose = new ArrayList<>();
        if (listaDesgloses != null) {
            for (desgloseMontos d : listaDesgloses) {
                if (d != null) {
                    Document subDoc = new Document("metodo", d.getNombreMetodo())
                            .append("montoDeclarado", d.getMontoDeclarado());
                    subDocsDesglose.add(subDoc);
                }
            }
        }
        return subDocsDesglose;
    }

    /**
     * Método utilitario para evitar caídas por casteo de tipos numéricos en MongoDB.
     */
    private static double obtenerDoubleSeguro(Document doc, String campo) {
        if (doc == null || campo == null) return 0.0;
        Object valor = doc.get(campo);
        if (valor instanceof Number) {
            return ((Number) valor).doubleValue();
        }
        return 0.0;
    }
}