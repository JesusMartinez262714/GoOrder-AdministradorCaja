package AdministradorCajaPersistencia.DAOs;

import AdministradorCajaPersistencia.Entitys.cajero;
import AdministradorCajaPersistencia.Interfaces.ICajeroDAO;
import AdministradorCajaPersistencia.Mappers.CajeroPersistenciaMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de persistencia encargada de gestionar las operaciones CRUD y el control
 * de deudas de los cajeros dentro de la colección "cajeros" en MongoDB.
 * * @author Jesus Manuel Martinez Cortez
 */
public class cajeroDAO implements ICajeroDAO {

    private final MongoCollection<Document> coleccion;

    /**
     * Constructor que recibe la base de datos para inicializar la conexión
     * con la colección de cajeros.
     * * @param database Instancia de la base de datos MongoDB activa.
     */
    public cajeroDAO(MongoDatabase database) {
        this.coleccion = database.getCollection("cajeros");
    }

    /**
     * Obtiene la lista completa de todos los cajeros registrados en la base de datos.
     * * @return Lista de objetos tipo cajero, o una lista vacía si no hay registros o hay error.
     */
    @Override
    public List<cajero> consultarTodos() {
        List<cajero> lista = new ArrayList<>();
        try {
            for (Document doc : coleccion.find()) {
                if (doc != null) {
                    cajero c = CajeroPersistenciaMapper.documentToEntity(doc);
                    if (c != null) {
                        lista.add(c);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al consultar todos los cajeros: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Busca un cajero específico en la base de datos utilizando su ID único.
     * * @param idCajero ID único del cajero a consultar.
     * @return El objeto cajero encontrado, o null si no existe o el ID es inválido.
     */
    @Override
    public cajero consultarPorId(int idCajero) {
        if (idCajero <= 0) {
            return null;
        }
        try {
            Document doc = coleccion.find(Filters.eq("idCajero", idCajero)).first();
            if (doc == null) {
                return null;
            }
            return CajeroPersistenciaMapper.documentToEntity(doc);
        } catch (Exception e) {
            System.err.println("Error al consultar cajero por ID: " + e.getMessage());
            return null;
        }
    }

    /**
     * Registra un nuevo cajero calculando su ID de forma autoincremental basada
     * en el valor más alto registrado actualmente en la colección.
     * * @param entidad Objeto cajero con los datos que se van a guardar.
     * @return true si el registro en MongoDB fue exitoso, false en caso contrario.
     */
    @Override
    public boolean insertarCajero(cajero entidad) {
        if (entidad == null) {
            return false;
        }
        try {
            Document maxIdDoc = coleccion.find().sort(new Document("idCajero", -1)).first();
            int nuevoId = (maxIdDoc != null) ? maxIdDoc.getInteger("idCajero") + 1 : 1;
            entidad.setIdCajero(nuevoId);

            Document doc = CajeroPersistenciaMapper.entityToDocument(entidad);
            if (doc == null) {
                return false;
            }

            coleccion.insertOne(doc);
            return true;
        } catch (Exception e) {
            System.err.println("Error al insertar nuevo cajero: " + e.getMessage());
            return false;
        }
    }

    /**
     * Reemplaza el documento de un cajero existente en la base de datos
     * con los nuevos datos modificados del objeto.
     * * @param entidad Objeto cajero con los datos actualizados.
     * @return true si el reemplazo en MongoDB fue exitoso, false en caso contrario.
     */
    @Override
    public boolean actualizarCajero(cajero entidad) {
        if (entidad == null || entidad.getIdCajero() <= 0) {
            return false;
        }
        try {
            Document doc = CajeroPersistenciaMapper.entityToDocument(entidad);
            if (doc == null) {
                return false;
            }

            coleccion.replaceOne(Filters.eq("idCajero", entidad.getIdCajero()), doc);
            return true;
        } catch (Exception e) {
            System.err.println("Error al actualizar cajero: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina permanentemente el registro de un cajero de la base de datos usando su ID.
     * * @param idCajero ID único del cajero que se va a eliminar.
     * @return true si la eliminación en MongoDB fue exitosa, false en caso contrario.
     */
    @Override
    public boolean eliminarCajero(int idCajero) {
        if (idCajero <= 0) {
            return false;
        }
        try {
            coleccion.deleteOne(Filters.eq("idCajero", idCajero));
            return true;
        } catch (Exception e) {
            System.err.println("Error al eliminar cajero: " + e.getMessage());
            return false;
        }
    }

    /**
     * Incrementa o reduce de forma directa el monto acumulado del adeudo total
     * de un cajero utilizando los operadores numéricos de MongoDB.
     * * @param idCajero ID único del cajero a modificar.
     * @param monto Cantidad a sumar (o restar si es un valor negativo) al saldo del cajero.
     * @return true si se encontró el registro y se procesó el cambio, false en caso de error.
     */
    @Override
    public boolean actualizarAdeudoAcumulado(int idCajero, double monto) {
        if (idCajero <= 0) {
            return false;
        }
        try {
            long encontrados = coleccion.updateOne(
                    Filters.eq("idCajero", idCajero),
                    Updates.inc("adeudoTotal", monto)
            ).getMatchedCount();

            return encontrados > 0;
        } catch (Exception e) {
            System.err.println("Error al actualizar adeudo acumulado del cajero: " + e.getMessage());
            return false;
        }
    }
}