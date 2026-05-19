package AdministradorCajaPersistencia.DAOs;

import AdministradorCajaPersistencia.Entitys.corteCaja;
import AdministradorCajaPersistencia.Interfaces.ICorteCajaDAO;
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

/**
 * Clase de persistencia encargada de gestionar los flujos de apertura, cierre
 * e historial de los turnos de caja en la colección "caja" de MongoDB.
 * * @author Jesus Manuel Martinez Cortez
 */
public class corteCajaDAO implements ICorteCajaDAO {
    private MongoCollection<Document> coleccion;

    /**
     * Constructor que recibe la base de datos para conectar con la colección de caja.
     * * @param db Instancia de la base de datos MongoDB activa.
     */
    public corteCajaDAO(MongoDatabase db) {
        if (db != null) {
            this.coleccion = db.getCollection("caja");
        }
    }

    /**
     * Registra un nuevo documento de apertura de caja con un estado inicial de ABIERTA.
     * * @param idCajero ID del cajero que operará la caja.
     * @param montoInicial Cantidad de dinero del fondo fijo inicial.
     * @param idSupervisor ID del supervisor que autoriza la apertura.
     * @return true si la inserción en la base de datos fue exitosa, false en caso contrario.
     */
    @Override
    public boolean registrarApertura(int idCajero, double montoInicial, int idSupervisor) {
        if (idCajero <= 0 || idSupervisor <= 0 || montoInicial < 0.0 || coleccion == null) {
            return false;
        }
        try {
            int idCaja = (int) (System.currentTimeMillis() / 1000);

            Document doc = new Document("idCaja", idCaja)
                    .append("idCajero", idCajero)
                    .append("idSupervisor", idSupervisor)
                    .append("fechaApertura", new Date())
                    .append("montoInicial", montoInicial)
                    .append("estado", "ABIERTA");

            coleccion.insertOne(doc);
            return true;
        } catch (Exception e) {
            System.err.println("Error al registrar apertura: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verifica si un cajero en específico cuenta con un registro de caja en estado ABIERTA.
     * * @param idCajero ID único del cajero a consultar.
     * @return true si se encontró una caja abierta, false en caso contrario.
     */
    @Override
    public boolean tieneAperturaActiva(int idCajero) {
        if (idCajero <= 0 || coleccion == null) {
            return false;
        }
        try {
            Document doc = coleccion.find(Filters.and(
                    Filters.eq("idCajero", idCajero),
                    Filters.eq("estado", "ABIERTA")
            )).first();
            return doc != null;
        } catch (Exception e) {
            System.err.println("Error al verificar apertura activa: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene una lista con los IDs de todos los cajeros que tienen un turno abierto.
     * * @return Lista de enteros con los IDs de los cajeros encontrados.
     */
    @Override
    public List<Integer> obtenerIdsCajerosConCajaAbierta() {
        List<Integer> ids = new ArrayList<>();
        if (coleccion == null) {
            return ids;
        }
        try {
            for (Document doc : coleccion.find(Filters.eq("estado", "ABIERTA"))) {
                if (doc != null && doc.containsKey("idCajero")) {
                    ids.add(doc.getInteger("idCajero"));
                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener IDs de cajas abiertas: " + e.getMessage());
        }
        return ids;
    }

    /**
     * Consulta el último registro de caja de un cajero ordenado por la fecha de apertura.
     * * @param idCajero ID único del cajero.
     * @return Documento de BSON con los datos de la caja, o null si no se encuentra.
     */
    @Override
    public Document consultarUltimaCaja(int idCajero) {
        if (idCajero <= 0 || coleccion == null) {
            return null;
        }
        try {
            return coleccion.find(Filters.eq("idCajero", idCajero))
                    .sort(Sorts.descending("fechaApertura"))
                    .first();
        } catch (Exception e) {
            System.err.println("Error al consultar ultima caja: " + e.getMessage());
            return null;
        }
    }

    /**
     * Guarda los datos definitivos del corte de caja. Si el corte ya tiene un ID asignado
     * actualiza el registro por ID; de lo contrario, busca la caja ABIERTA del cajero y la cierra.
     * * @param entidad Objeto de entidad con los datos calculados del corte de caja.
     * @return true si el documento fue modificado en MongoDB, false en caso de error.
     */
    @Override
    public boolean guardarNuevoCorte(corteCaja entidad) {
        if (entidad == null || coleccion == null) {
            return false;
        }
        try {
            List<Document> subDocsDesglose = AdministradorCajaPersistencia.Mappers.CorteCajaPersistenciaMapper.desglosesToDocumentList(entidad.getListaDesglose());

            Bson filtro;
            Bson actualizaciones;

            if (entidad.getIdCaja() > 0) {
                filtro = Filters.eq("idCaja", entidad.getIdCaja());

                actualizaciones = Updates.combine(
                        Updates.set("montoEsperado", entidad.getTotalEsperadoSistema()),
                        Updates.set("montoReal", entidad.getTotalRealDeclarado()),
                        Updates.set("diferencia", entidad.getDiferencia()),
                        Updates.set("observaciones", entidad.getObservaciones()),
                        Updates.set("desgloses", subDocsDesglose),
                        Updates.set("estado", "CERRADA")
                );
            } else {
                int nuevoId = (int) (System.currentTimeMillis() / 1000);

                filtro = Filters.and(
                        Filters.eq("idCajero", entidad.getIdCajero()),
                        Filters.eq("estado", "ABIERTA")
                );

                actualizaciones = Updates.combine(
                        Updates.set("idCaja", nuevoId),
                        Updates.set("fechaCorte", new Date()),
                        Updates.set("montoEsperado", entidad.getTotalEsperadoSistema()),
                        Updates.set("montoReal", entidad.getTotalRealDeclarado()),
                        Updates.set("diferencia", entidad.getDiferencia()),
                        Updates.set("observaciones", entidad.getObservaciones()),
                        Updates.set("desgloses", subDocsDesglose),
                        Updates.set("estado", "CERRADA")
                );
            }

            long modificados = coleccion.updateOne(filtro, actualizaciones).getModifiedCount();
            return modificados > 0;

        } catch (Exception e) {
            System.err.println("Error al guardar/editar el corte en la base de datos: " + e.getMessage());
            return false;
        }
    }

    /**
     * Consulta el historial de cortes de caja realizados dentro de un rango de fechas específico,
     * omitiendo aquellos registros que aún sigan abiertos.
     * * @param inicio Fecha límite inferior para la búsqueda.
     * @param fin Fecha límite superior para la búsqueda.
     * @return Lista de objetos corteCaja ordenados desde el más reciente.
     */
    @Override
    public List<corteCaja> consultarCortesRealizados(Date inicio, Date fin) {
        List<corteCaja> lista = new ArrayList<>();
        if (coleccion == null) {
            return lista;
        }
        try {
            List<Bson> condiciones = new ArrayList<>();
            condiciones.add(Filters.ne("estado", "ABIERTA"));

            if (inicio != null) {
                condiciones.add(Filters.gte("fechaCorte", inicio));
            }
            if (fin != null) {
                condiciones.add(Filters.lte("fechaCorte", fin));
            }

            Bson filtroFinal = Filters.and(condiciones);

            for (Document doc : coleccion.find(filtroFinal).sort(Sorts.descending("fechaCorte"))) {
                if (doc != null) {
                    corteCaja c = AdministradorCajaPersistencia.Mappers.CorteCajaPersistenciaMapper.documentToEntity(doc);
                    if (c != null) {
                        if ("CERRADA".equalsIgnoreCase(c.getEstado())) {
                            c.setEstado("Vigente");
                        }
                        lista.add(c);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al consultar cortes realizados: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Elimina físicamente el documento de un corte de caja de la base de datos por su ID.
     * * @param idCorte ID único del corte de caja que se va a borrar.
     * @return true si el documento fue eliminado, false en caso contrario o error.
     */
    @Override
    public boolean eliminarCorte(int idCorte) {
        if (idCorte <= 0 || coleccion == null) {
            return false;
        }
        try {
            Bson filtro = Filters.eq("idCaja", idCorte);
            long borrados = coleccion.deleteOne(filtro).getDeletedCount();
            return borrados > 0;
        } catch (Exception e) {
            System.err.println("Error al eliminar corte físicamente: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza el estado de un corte (por ejemplo, a "Cancelado") e inyecta el motivo
     * de la cancelación proporcionado por el supervisor.
     * * @param idCaja ID único del corte de caja.
     * @param nuevoEstado Nombre del estado que se le va a asignar al registro.
     * @param motivoCancelacion Texto explicativo con la justificación del movimiento.
     * @return true si la actualización se completó con éxito, false en caso de error.
     */
    @Override
    public boolean actualizarEstadoCorte(int idCaja, String nuevoEstado, String motivoCancelacion) {
        if (idCaja <= 0 || nuevoEstado == null || motivoCancelacion == null || coleccion == null) {
            return false;
        }
        try {
            coleccion.updateOne(
                    Filters.eq("idCaja", idCaja),
                    Updates.combine(
                            Updates.set("estado", nuevoEstado),
                            Updates.set("motivoCancelacion", motivoCancelacion)
                    )
            );
            return true;
        } catch (Exception e) {
            System.err.println("Error al cancelar corte: " + e.getMessage());
            return false;
        }
    }
}