package AdministradorCajaPersistencia.DAOs;

import AdministradorCajaPersistencia.Entitys.corteCaja;
import AdministradorCajaPersistencia.Entitys.desgloseMontos;
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

public class corteCajaDAO implements ICorteCajaDAO {
    private MongoCollection<Document> coleccion;

    public corteCajaDAO(MongoDatabase db) {
        this.coleccion = db.getCollection("caja");
    }

    @Override
    public boolean registrarApertura(int idCajero, double montoInicial, int idSupervisor) {
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

    @Override
    public boolean tieneAperturaActiva(int idCajero) {
        Document doc = coleccion.find(Filters.and(
                Filters.eq("idCajero", idCajero),
                Filters.eq("estado", "ABIERTA")
        )).first();
        return doc != null;
    }

    @Override
    public List<Integer> obtenerIdsCajerosConCajaAbierta() {
        List<Integer> ids = new ArrayList<>();
        for (Document doc : coleccion.find(Filters.eq("estado", "ABIERTA"))) {
            ids.add(doc.getInteger("idCajero"));
        }
        return ids;
    }

    @Override
    public Document consultarUltimaCaja(int idCajero) {
        return coleccion.find(Filters.eq("idCajero", idCajero))
                .sort(Sorts.descending("fechaApertura"))
                .first();
    }

    @Override
    public boolean guardarNuevoCorte(corteCaja entidad) {
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

    @Override
    public List<corteCaja> consultarCortesRealizados(Date inicio, Date fin) {
        List<corteCaja> lista = new ArrayList<>();
        Bson filtro = Filters.ne("estado", "ABIERTA");

        for (Document doc : coleccion.find(filtro).sort(Sorts.descending("fechaCorte"))) {
            corteCaja c = AdministradorCajaPersistencia.Mappers.CorteCajaPersistenciaMapper.documentToEntity(doc);

            if (c != null) {
                if ("CERRADA".equalsIgnoreCase(c.getEstado())) {
                    c.setEstado("Vigente");
                }
                lista.add(c);
            }
        }
        return lista;
    }


    @Override
    public boolean eliminarCorte(int idCorte) {
        try {
            var filtro = com.mongodb.client.model.Filters.eq("idCaja", idCorte);

            long borrados = coleccion.deleteOne(filtro).getDeletedCount();

            return borrados > 0;

        } catch (Exception e) {
            System.err.println("Error al eliminar corte físicamente: " + e.getMessage());
            return false;
        }
    }


    @Override
    public boolean actualizarEstadoCorte(int idCaja, String nuevoEstado, String motivoCancelacion) {
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