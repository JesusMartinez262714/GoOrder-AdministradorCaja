package AdministradorCajaPersistencia.DAOs;

import AdministradorCajaPersistencia.Entitys.cajero;
import AdministradorCajaPersistencia.Interfaces.ICajeroDAO;
import AdministradorCajaPersistencia.Mappers.CajeroPersistenciaMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

public class cajeroDAO implements ICajeroDAO {

    private final MongoCollection<Document> coleccion;

    public cajeroDAO(MongoDatabase database) {
        this.coleccion = database.getCollection("cajeros");
    }

    @Override
    public List<cajero> consultarTodos() {
        List<cajero> lista = new ArrayList<>();
        for (Document doc : coleccion.find()) {
            lista.add(CajeroPersistenciaMapper.documentToEntity(doc));
        }
        return lista;
    }

    @Override
    public cajero consultarPorId(int idCajero) {
        Document doc = coleccion.find(Filters.eq("idCajero", idCajero)).first();
        return CajeroPersistenciaMapper.documentToEntity(doc);
    }

    @Override
    public boolean insertarCajero(cajero entidad) {
        try {
            Document maxIdDoc = coleccion.find().sort(new Document("idCajero", -1)).first();
            int nuevoId = (maxIdDoc != null) ? maxIdDoc.getInteger("idCajero") + 1 : 1;
            entidad.setIdCajero(nuevoId);
            coleccion.insertOne(CajeroPersistenciaMapper.entityToDocument(entidad));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean actualizarCajero(cajero entidad) {
        try {
            coleccion.replaceOne(Filters.eq("idCajero", entidad.getIdCajero()),
                    CajeroPersistenciaMapper.entityToDocument(entidad));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean eliminarCajero(int idCajero) {
        try {
            coleccion.deleteOne(Filters.eq("idCajero", idCajero));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}