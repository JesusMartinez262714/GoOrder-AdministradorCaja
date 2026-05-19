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

/**
 * Clase de persistencia encargada de gestionar las operaciones CRUD de los
 * supervisores dentro de la colección "supervisores" en MongoDB.
 * * @author Jesus Manuel Martinez Cortez
 */
public class supervisorDAO implements ISupervisorDAO {

    private MongoCollection<Document> coleccion;

    /**
     * Constructor que recibe la base de datos para inicializar la colección
     * de supervisores.
     * * @param db Instancia de la base de datos MongoDB activa.
     */
    public supervisorDAO(MongoDatabase db) {
        if (db != null) {
            this.coleccion = db.getCollection("supervisores");
        }
    }

    /**
     * Busca un supervisor en la base de datos utilizando su ID único.
     * * @param id ID único del supervisor a consultar.
     * @return El objeto supervisor encontrado, o null si no existe.
     */
    @Override
    public supervisor consultarPorId(int id) {
        if (id <= 0 || coleccion == null) {
            return null;
        }
        try {
            Document doc = coleccion.find(Filters.eq("idSupervisor", id)).first();
            if (doc == null) {
                return null;
            }
            return SupervisorPersistenciaMapper.documentToEntity(doc);
        } catch (Exception e) {
            System.err.println("Error al consultar supervisor por ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Consulta la lista de supervisores registrados, permitiendo filtrar los
     * resultados por medio de una búsqueda parcial de su nombre completo.
     * * @param filtroNombre Texto o nombre parcial para filtrar la búsqueda.
     * @return Lista de objetos supervisor que coinciden con el filtro.
     */
    @Override
    public List<supervisor> obtenerTodos(String filtroNombre) {
        List<supervisor> lista = new ArrayList<>();
        if (coleccion == null) {
            return lista;
        }
        try {
            Document filtro = (filtroNombre != null && !filtroNombre.isEmpty())
                    ? new Document("nombreCompleto", new Document("$regex", filtroNombre).append("$options", "i"))
                    : new Document();

            for (Document doc : coleccion.find(filtro)) {
                if (doc != null) {
                    supervisor s = SupervisorPersistenciaMapper.documentToEntity(doc);
                    if (s != null) {
                        lista.add(s);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener supervisores: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Registra un nuevo supervisor en la base de datos, calculando su ID de forma
     * autoincremental según el último registro guardado.
     * * @param entidad Objeto supervisor con los datos que se van a guardar.
     * @return true si el registro en MongoDB fue exitoso, false en caso contrario.
     */
    @Override
    public boolean registrarSupervisor(supervisor entidad) {
        if (entidad == null || coleccion == null) {
            return false;
        }
        try {
            Document maxIdDoc = coleccion.find().sort(new Document("idSupervisor", -1)).first();
            int nuevoId = (maxIdDoc != null) ? maxIdDoc.getInteger("idSupervisor") + 1 : 1;
            entidad.setIdSupervisor(nuevoId);

            Document doc = SupervisorPersistenciaMapper.entityToDocument(entidad);
            if (doc == null) {
                return false;
            }

            coleccion.insertOne(doc);
            return true;
        } catch (Exception e) {
            System.err.println("Error al registrar supervisor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza los campos de nombre, apellido y nombre completo de un supervisor
     * existente en la base de datos comparando por su ID.
     * * @param entidad Objeto supervisor con los datos modificados.
     * @return true si se actualizó el registro en MongoDB, false en caso contrario.
     */
    @Override
    public boolean editarSupervisor(supervisor entidad) {
        if (entidad == null || entidad.getIdSupervisor() <= 0 || coleccion == null) {
            return false;
        }
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

    /**
     * Elimina permanentemente el registro de un supervisor de la base de datos usando su ID.
     * * @param id ID único del supervisor que se desea eliminar.
     * @return true si la eliminación en MongoDB fue exitosa, false en caso contrario.
     */
    @Override
    public boolean eliminarSupervisor(int id) {
        if (id <= 0 || coleccion == null) {
            return false;
        }
        try {
            long borrados = coleccion.deleteOne(Filters.eq("idSupervisor", id)).getDeletedCount();
            return borrados > 0;
        } catch (Exception e) {
            System.err.println("Error al eliminar supervisor: " + e.getMessage());
            return false;
        }
    }
}