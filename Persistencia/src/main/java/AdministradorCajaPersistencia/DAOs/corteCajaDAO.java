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
            int idApertura = (int) (System.currentTimeMillis() / 1000);

            Document doc = new Document("idApertura", idApertura)
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
            entidad.setId((int) (System.currentTimeMillis() / 1000));

            List<Document> subDocsDesglose = new ArrayList<>();
            if (entidad.getListaDesglose() != null) {
                for (desgloseMontos d : entidad.getListaDesglose()) {
                    Document subDoc = new Document("metodo", d.getNombreMetodo())
                            .append("montoDeclarado", d.getMontoDeclarado());
                    subDocsDesglose.add(subDoc);
                }
            }

            Bson filtroCajaAbierta = Filters.and(
                    Filters.eq("idCajero", entidad.getIdCajero()),
                    Filters.eq("estado", "ABIERTA")
            );

            Bson actualizaciones = Updates.combine(
                    Updates.set("idCorte", entidad.getId()),
                    Updates.set("fechaCorte", new Date()),
                    Updates.set("montoEsperado", entidad.getTotalEsperadoSistema()),
                    Updates.set("montoReal", entidad.getTotalRealDeclarado()),
                    Updates.set("diferencia", entidad.getDiferencia()),
                    Updates.set("desgloses", subDocsDesglose),
                    Updates.set("estado", "CERRADA")
            );

            long modificados = coleccion.updateOne(filtroCajaAbierta, actualizaciones).getModifiedCount();
            return modificados > 0;

        } catch (Exception e) {
            System.err.println("Error al guardar corte: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<corteCaja> consultarCortesRealizados(Date inicio, Date fin) {
        List<corteCaja> lista = new ArrayList<>();

        Bson filtro = Filters.and(
                Filters.gte("fechaCorte", inicio),
                Filters.lte("fechaCorte", fin),
                Filters.eq("estado", "CERRADA")
        );

        for (Document doc : coleccion.find(filtro).sort(Sorts.descending("fechaCorte"))) {
            corteCaja c = new corteCaja();

            c.setId(doc.getInteger("idCorte") != null ? doc.getInteger("idCorte") : 0);
            c.setIdApertura(doc.getInteger("idApertura") != null ? doc.getInteger("idApertura") : 0);
            c.setFecha(doc.getDate("fechaCorte"));
            c.setIdCajero(doc.getInteger("idCajero") != null ? doc.getInteger("idCajero") : 0);
            c.setEstado(doc.getString("estado"));

            c.setTotalEsperadoSistema(obtenerDoubleSeguro(doc, "montoEsperado"));
            c.setTotalRealDeclarado(obtenerDoubleSeguro(doc, "montoReal"));
            c.setDiferencia(obtenerDoubleSeguro(doc, "diferencia"));

            List<Document> desgloseDocs = doc.getList("desgloses", Document.class);
            if (desgloseDocs != null) {
                List<desgloseMontos> listaDesgloses = new ArrayList<>();
                for (Document subDoc : desgloseDocs) {
                    desgloseMontos d = new desgloseMontos();
                    d.setNombreMetodo(subDoc.getString("metodo"));
                    d.setMontoDeclarado(obtenerDoubleSeguro(subDoc, "montoDeclarado"));
                    listaDesgloses.add(d);
                }
                c.setListaDesglose(listaDesgloses);
            }

            lista.add(c);
        }
        return lista;
    }

    @Override
    public double obtenerDoubleSeguro(Document doc, String campo) {
        Object valor = doc.get(campo);
        if (valor instanceof Number) {
            return ((Number) valor).doubleValue();
        }
        return 0.0;
    }

    @Override
    public boolean eliminarCorte(int idCorte) {
        try {
            coleccion.deleteOne(Filters.eq("idCorte", idCorte));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean actualizarEstadoCorte(int idCorte, String nuevoEstado) {
        try {
            coleccion.updateOne(
                    Filters.eq("idCorte", idCorte),
                    Updates.set("estado", nuevoEstado)
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}