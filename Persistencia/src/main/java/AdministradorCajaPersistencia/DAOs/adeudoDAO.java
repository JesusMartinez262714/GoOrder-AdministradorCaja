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

/**
 * Clase de persistencia encargada de gestionar los registros de deudas y abonos
 * de los cajeros dentro de la colección "adeudos" en MongoDB.
 * * @author Jesus Manuel Martinez Cortez
 */
public class adeudoDAO implements IAdeudoDAO {

    private final MongoCollection<Document> coleccion;

    /**
     * Constructor que recibe la base de datos para inicializar la conexión
     * con la colección específica de adeudos.
     * * @param database Instancia de la base de datos MongoDB activa.
     */
    public adeudoDAO(MongoDatabase database) {
        this.coleccion = database.getCollection("adeudos");
    }

    /**
     * Busca la lista de deudas que tengan un estado "PENDIENTE" asociadas al ID de un cajero.
     * * @param idCajero ID único del cajero a consultar.
     * @return Colección de objetos tipo adeudo encontrados en el sistema.
     */
    @Override
    public List<adeudo> consultarPendientesPorCajero(int idCajero) {
        List<adeudo> lista = new ArrayList<>();
        if (idCajero <= 0) {
            return lista;
        }

        try {
            for (Document doc : coleccion.find(Filters.and(Filters.eq("idCajero", idCajero), Filters.eq("estado", "PENDIENTE")))) {
                if (doc != null) {
                    adeudo a = AdeudoPersistenciaMapper.documentToEntity(doc);
                    if (a != null) {
                        lista.add(a);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al consultar adeudos pendientes: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Obtiene el valor monetario de la deuda total que tiene acumulada un cajero en el sistema.
     * * @param idCajero ID único del cajero a consultar.
     * @return El monto double de la deuda, o 0.0 si no debe nada o si ocurre un error.
     */
    @Override
    public double consultarAdeudoPorCajero(int idCajero) {
        if (idCajero <= 0) {
            return 0.0;
        }

        try {
            Document doc = coleccion.find(Filters.eq("idCajero", idCajero)).first();
            if (doc == null) {
                return 0.0;
            }

            adeudo a = AdeudoPersistenciaMapper.documentToEntity(doc);
            return a != null ? a.getMonto() : 0.0;
        } catch (Exception e) {
            System.err.println("Error al consultar adeudo por cajero: " + e.getMessage());
            return 0.0;
        }
    }

    /**
     * Actualiza la deuda de un cajero en la base de datos. Si el nuevo monto es mayor a cero
     * el estado cambia a PENDIENTE, y si llega a cero se registra automáticamente como PAGADO.
     * * @param idCajero ID único del cajero a modificar.
     * @param nuevoMonto El nuevo valor total acumulado de la deuda.
     * @return true si la operación en MongoDB fue exitosa, false en caso contrario.
     */
    @Override
    public boolean actualizarMontoAdeudo(int idCajero, double nuevoMonto) {
        if (idCajero <= 0 || nuevoMonto < 0.0) {
            return false;
        }

        try {
            String nuevoEstado = nuevoMonto > 0.0 ? "PENDIENTE" : "PAGADO";
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