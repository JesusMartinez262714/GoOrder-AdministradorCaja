package AdministradorCajaPersistencia.DAOs;

import AdministradorCajaPersistencia.Entitys.supervisor;
import AdministradorCajaPersistencia.Interfaces.ISupervisorDAO;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
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
            if (doc != null) {
                return new supervisor(
                        doc.getInteger("idSupervisor"),
                        doc.getString("nombre"),
                        doc.getString("apellido")
                );
            }
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
                lista.add(new supervisor(
                        doc.getInteger("idSupervisor"),
                        doc.getString("nombre"),
                        doc.getString("apellido")
                ));
            }
        } catch (Exception e) {
            System.err.println("Error al obtener supervisores: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean registrarSupervisor(supervisor entidad) {
        try {
            int nuevoId = (int) (System.currentTimeMillis() / 1000);
            Document doc = new Document("idSupervisor", nuevoId)
                    .append("nombre", entidad.getNombre())      // Campo 1
                    .append("apellido", entidad.getApellido()); // Campo 2

            coleccion.insertOne(doc);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean editarSupervisor(supervisor entidad) {
        try {
            coleccion.updateOne(
                    Filters.eq("idSupervisor", entidad.getIdSupervisor()),
                    Updates.set("nombreCompleto", entidad.getNombreCompleto())
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean eliminarSupervisor(int id) {
        try {
            coleccion.deleteOne(Filters.eq("idSupervisor", id));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}