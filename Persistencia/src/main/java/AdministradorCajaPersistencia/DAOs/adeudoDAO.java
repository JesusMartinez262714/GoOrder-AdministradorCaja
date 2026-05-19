package AdministradorCajaPersistencia.DAOs;

import AdministradorCajaPersistencia.Entitys.adeudo;
import AdministradorCajaPersistencia.Interfaces.IAdeudoDAO;
import AdministradorCajaPersistencia.Mappers.AdeudoPersistenciaMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

public class adeudoDAO implements IAdeudoDAO {

    private final MongoCollection<Document> coleccion;

    public adeudoDAO(MongoDatabase database) {
        this.coleccion = database.getCollection("adeudos");
    }

    @Override
    public List<adeudo> consultarPendientesPorCajero(int idCajero) {
        List<adeudo> lista = new ArrayList<>();
        try {
            for (Document doc : coleccion.find(Filters.and(Filters.eq("idCajero", idCajero), Filters.eq("estado", "PENDIENTE")))) {
                adeudo a = AdeudoPersistenciaMapper.documentToEntity(doc);
                if (a != null) {
                    lista.add(a);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al consultar adeudos pendientes: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public double consultarAdeudoPorCajero(int idCajero) {
        try {
            Document doc = coleccion.find(Filters.eq("idCajero", idCajero)).first();
            adeudo a = AdeudoPersistenciaMapper.documentToEntity(doc);
            return a != null ? a.getMonto() : 0.0;
        } catch (Exception e) {
            System.err.println("Error al consultar adeudo por cajero: " + e.getMessage());
            return 0.0;
        }
    }

    @Override
    public boolean actualizarMontoAdeudo(int idCajero, double nuevoMonto) {
        try {
            String nuevoEstado = nuevoMonto > 0 ? "PENDIENTE" : "PAGADO";
            coleccion.updateOne(
                    Filters.eq("idCajero", idCajero),
                    Updates.combine(
                            Updates.set("monto", nuevoMonto),
                            Updates.set("estado", nuevoEstado)
                    ),
                    new UpdateOptions().upsert(true)
            );
            return true;
        } catch (Exception e) {
            System.err.println("Error al actualizar monto de adeudo: " + e.getMessage());
            return false;
        }
    }
}