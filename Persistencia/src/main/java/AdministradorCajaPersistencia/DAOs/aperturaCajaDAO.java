package AdministradorCajaPersistencia.DAOs;

import AdministradorCajaPersistencia.Entitys.aperturaCaja;
import AdministradorCajaPersistencia.Interfaces.IAperturaCajaDAO;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class aperturaCajaDAO implements IAperturaCajaDAO {
    private MongoCollection<Document> coleccion;

    public aperturaCajaDAO(MongoDatabase db) {
        this.coleccion = db.getCollection("aperturas");
    }

    @Override
    public boolean insertarApertura(aperturaCaja apertura) {
        try {
            int nuevoId = (int) (System.currentTimeMillis() / 1000);
            Document doc = new Document("idApertura", nuevoId)
                    .append("fechaHora", new Date())
                    .append("montoInicial", apertura.getMontoInicial())
                    .append("idCajero", apertura.getIdCajero())
                    .append("idSupervisor", apertura.getIdSupervisor())
                    .append("idCorte", 0);

            coleccion.insertOne(doc);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public aperturaCaja consultarUltimaApertura(int idCajero) {
        try {
            Document resultado = coleccion.find(Filters.eq("idCajero", idCajero))
                    .sort(Sorts.descending("fechaHora"))
                    .first();

            if (resultado != null) {
                aperturaCaja apertura = new aperturaCaja();
                apertura.setIdApertura(resultado.getInteger("idApertura"));
                apertura.setFechaHora(resultado.getDate("fechaHora"));
                apertura.setMontoInicial(resultado.getDouble("montoInicial"));
                apertura.setIdCajero(resultado.getInteger("idCajero"));
                apertura.setIdSupervisor(resultado.getInteger("idSupervisor"));
                return apertura;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Integer> obtenerIdsCajerosConCajaAbierta() {
        List<Integer> activos = new ArrayList<>();
        try {
            Bson filtro = Filters.eq("idCorte", 0);
            for (Document doc : coleccion.find(filtro)) {
                activos.add(doc.getInteger("idCajero"));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return activos;
    }

    @Override
    public boolean tieneAperturaActiva(int idCajero) {
        try {
            return coleccion.countDocuments(
                    Filters.and(
                            Filters.eq("idCajero", idCajero),
                            Filters.eq("idCorte", 0)
                    )
            ) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void vincularCorteAApertura(int idApertura, int idCorte) {
        try {
            coleccion.updateOne(
                    Filters.eq("idApertura", idApertura),
                    Updates.set("idCorte", idCorte)
            );
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    @Override
    public Date obtenerFechaAperturaPorId(int idApertura) {
        try {
            org.bson.Document doc = coleccion.find(com.mongodb.client.model.Filters.eq("idApertura", idApertura)).first();

            if (doc != null) {
                return doc.getDate("fechaHora");
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}