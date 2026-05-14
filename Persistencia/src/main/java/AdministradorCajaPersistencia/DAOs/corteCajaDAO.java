package AdministradorCajaPersistencia.DAOs;

import AdministradorCajaPersistencia.Entitys.corteCaja;
import AdministradorCajaPersistencia.Interfaces.ICorteCajaDAO;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class corteCajaDAO implements ICorteCajaDAO {
    private MongoCollection<Document> coleccion;

    public corteCajaDAO(MongoDatabase db) {
        this.coleccion = db.getCollection("cortes");
    }

    @Override
    public List<corteCaja> consultarCortesRealizados(Date inicio, Date fin) {
        List<corteCaja> lista = new ArrayList<>();
        for (Document doc : coleccion.find().sort(Sorts.descending("fecha"))) {
            corteCaja c = new corteCaja();

            c.setId(doc.getInteger("idCorte") != null ? doc.getInteger("idCorte") : 0);
            c.setIdApertura(doc.getInteger("idApertura") != null ? doc.getInteger("idApertura") : 0);
            c.setFecha(doc.getDate("fecha"));
            c.setIdCajero(doc.getInteger("idCajero") != null ? doc.getInteger("idCajero") : 0);
            c.setEstado(doc.getString("estado") != null ? doc.getString("estado") : "Vigente");

            c.setTotalEsperadoSistema(obtenerDoubleSeguro(doc, "montoEsperado"));
            c.setTotalRealDeclarado(obtenerDoubleSeguro(doc, "montoReal"));
            c.setDiferencia(obtenerDoubleSeguro(doc, "diferencia"));

            lista.add(c);
        }
        return lista;
    }

    @Override
    public double obtenerDoubleSeguro(Document doc, String campo) {
        Object valor = doc.get(campo);
        if (valor instanceof Number) {
            return ((Number) valor).doubleValue();
        }
        return 0.0;
    }

    @Override
    public boolean guardarNuevoCorte(corteCaja entidad) {
        try {
            entidad.setId((int) (System.currentTimeMillis() / 1000));
            Document doc = new Document("idCorte", entidad.getId())
                    .append("idApertura", entidad.getIdApertura())
                    .append("fecha", new Date())
                    .append("idCajero", entidad.getIdCajero())
                    .append("montoEsperado", entidad.getTotalEsperadoSistema())
                    .append("montoReal", entidad.getTotalRealDeclarado())
                    .append("diferencia", entidad.getDiferencia())
                    .append("estado", entidad.getEstado());
            coleccion.insertOne(doc);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean eliminarCorte(int idCorte) {
        try {
            coleccion.deleteOne(Filters.eq("idCorte", idCorte));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean actualizarEstadoCorte(int idCorte, String nuevoEstado) {
        try {
            coleccion.updateOne(
                    Filters.eq("idCorte", idCorte),
                    Updates.set("estado", nuevoEstado)
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}