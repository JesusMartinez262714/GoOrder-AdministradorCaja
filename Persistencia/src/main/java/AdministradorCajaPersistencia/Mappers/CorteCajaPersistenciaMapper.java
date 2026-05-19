package AdministradorCajaPersistencia.Mappers;

import AdministradorCajaPersistencia.Entitys.corteCaja;
import AdministradorCajaPersistencia.Entitys.desgloseMontos;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

public class CorteCajaPersistenciaMapper {

    /**
     * Traduce un Document de MongoDB a una Entidad corteCaja.
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

        List<Document> desgloseDocs = doc.getList("desgloses", Document.class);
        if (desgloseDocs != null) {
            List<desgloseMontos> listaDesgloses = new ArrayList<>();
            for (Document subDoc : desgloseDocs) {
                listaDesgloses.add(subDocumentToEntity(subDoc));
            }
            entidad.setListaDesglose(listaDesgloses);
        }

        return entidad;
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
                Document subDoc = new Document("metodo", d.getNombreMetodo())
                        .append("montoDeclarado", d.getMontoDeclarado());
                subDocsDesglose.add(subDoc);
            }
        }
        return subDocsDesglose;
    }

    /**
     * Método utilitario para evitar caídas por casteo de tipos numéricos en MongoDB.
     */
    private static double obtenerDoubleSeguro(Document doc, String campo) {
        Object valor = doc.get(campo);
        if (valor instanceof Number) {
            return ((Number) valor).doubleValue();
        }
        return 0.0;
    }
}