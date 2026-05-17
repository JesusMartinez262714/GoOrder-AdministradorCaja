package AdministradorCajaPersistencia.DAOs;

import AdministradorCajaPersistencia.Entitys.adeudo;
import AdministradorCajaPersistencia.Interfaces.IAdeudoDAO;
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
        for (Document doc : coleccion.find(Filters.and(Filters.eq("idCajero", idCajero), Filters.eq("estado", "PENDIENTE")))) {
            adeudo a = new adeudo();
            a.setIdCajero(doc.getInteger("idCajero"));
            a.setMonto(obtenerDoubleSeguro(doc, "monto"));
            a.setEstado(doc.getString("estado"));
            lista.add(a);
        }
        return lista;
    }

    @Override
    public double consultarAdeudoPorCajero(int idCajero) {
        Document doc = coleccion.find(Filters.eq("idCajero", idCajero)).first();
        return doc != null ? obtenerDoubleSeguro(doc, "monto") : 0.0;
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
            return false;
        }
    }

    private double obtenerDoubleSeguro(Document doc, String campo) {
        Object valor = doc.get(campo);
        if (valor instanceof Number) {
            return ((Number) valor).doubleValue();
        }
        return 0.0;
    }
}