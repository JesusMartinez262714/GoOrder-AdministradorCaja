package AdministradorCajaPersistencia.DAOs;

import AdministradorCajaPersistencia.Entitys.supervisor;
import AdministradorCajaPersistencia.Interfaces.ISupervisorDAO;
import AdministradorCajaPersistencia.Mappers.SupervisorPersistenciaMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.util.ArrayList;
import java.util.List;

public class supervisorDAO implements ISupervisorDAO {

    private MongoCollection<Document> coleccion;

    public supervisorDAO(MongoDatabase db) {
        this.coleccion = db.getCollection("supervisores");
    }

    @Override
    public supervisor consultarPorId(int id) {
        try {
            Document doc = coleccion.find(Filters.eq("idSupervisor", id)).first();
            return SupervisorPersistenciaMapper.documentToEntity(doc);
        } catch (Exception e) {
            System.err.println("Error al consultar supervisor por ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<supervisor> obtenerTodos(String filtroNombre) {
        List<supervisor> lista = new ArrayList<>();
        try {
            Document filtro = (filtroNombre != null && !filtroNombre.isEmpty())
                    ? new Document("nombreCompleto", new Document("$regex", filtroNombre).append("$options", "i"))
                    : new Document();

            for (Document doc : coleccion.find(filtro)) {
                supervisor s = SupervisorPersistenciaMapper.documentToEntity(doc);
                if (s != null) {
                    lista.add(s);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener supervisores: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean registrarSupervisor(supervisor entidad) {
        try {
            Document doc = SupervisorPersistenciaMapper.entityToDocument(entidad);
            coleccion.insertOne(doc);
            return true;
        } catch (Exception e) {
            System.err.println("Error al registrar supervisor: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean editarSupervisor(supervisor entidad) {
        try {
            Bson actualizaciones = Updates.combine(
                    Updates.set("nombre", entidad.getNombre()),
                    Updates.set("apellido", entidad.getApellido()),
                    Updates.set("nombreCompleto", entidad.getNombreCompleto())
            );

            long modificados = coleccion.updateOne(
                    Filters.eq("idSupervisor", entidad.getIdSupervisor()),
                    actualizaciones
            ).getModifiedCount();

            return modificados > 0;
        } catch (Exception e) {
            System.err.println("Error al editar supervisor: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminarSupervisor(int id) {
        try {
            long borrados = coleccion.deleteOne(Filters.eq("idSupervisor", id)).getDeletedCount();
            return borrados > 0;
        } catch (Exception e) {
            System.err.println("Error al eliminar supervisor: " + e.getMessage());
            return false;
        }
    }
}