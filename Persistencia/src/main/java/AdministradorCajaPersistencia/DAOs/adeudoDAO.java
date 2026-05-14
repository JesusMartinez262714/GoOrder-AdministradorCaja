package AdministradorCajaPersistencia.DAOs;

import AdministradorCajaPersistencia.Entitys.adeudo;
import AdministradorCajaPersistencia.Interfaces.IAdeudoDAO;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
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
        Document filtro = new Document("idCajero", idCajero).append("estado", "PENDIENTE");

        for (Document doc : coleccion.find(filtro)) {
            adeudo a = new adeudo();
            lista.add(a);
        }
        return lista;
    }
}