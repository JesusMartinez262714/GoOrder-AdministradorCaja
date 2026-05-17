package goorderpersistencia;

import Entitys.venta;
import Interfaces.IVentaDAO;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ventaDAO implements IVentaDAO {

    private final MongoDatabase database;
    private final MongoCollection<Document> coleccion;

    public ventaDAO(MongoDatabase database) {
        this.database = database;
        this.coleccion = database.getCollection("ventas");
    }

    @Override
    public List<venta> obtenerVentas(int idCajero, Date fechaApertura) {
        List<venta> ventas = new ArrayList<>();
        try {
            for (Document doc : coleccion.find(Filters.and(
                    Filters.eq("idCajero", idCajero),
                    Filters.gte("fecha", fechaApertura)
            ))) {
                venta v = new venta();
                v.setIdVenta(doc.getInteger("idVenta"));
                v.setMontoTotal(obtenerDoubleSeguro(doc, "montoTotal"));
                v.setIdMetodoPago(doc.getInteger("idMetodoPago"));
                v.setIdCajero(doc.getInteger("idCajero"));
                v.setFecha(doc.getDate("fecha"));
                ventas.add(v);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return ventas;
    }

    @Override
    public boolean insertarVenta(venta nuevaVenta) {
        try {
            Document doc = new Document("idVenta", nuevaVenta.getIdVenta())
                    .append("montoTotal", nuevaVenta.getMontoTotal())
                    .append("idMetodoPago", nuevaVenta.getIdMetodoPago())
                    .append("idCajero", nuevaVenta.getIdCajero())
                    .append("fecha", nuevaVenta.getFecha());

            coleccion.insertOne(doc);
            return true;
        } catch (Exception e) {
            System.err.println("Error al insertar venta: " + e.getMessage());
            return false;
        }
    }

    @Override
    public int obtenerIdCajeroConCajaAbierta() {
        try {
            MongoCollection<Document> collAperturas = database.getCollection("aperturas");
            Document doc = collAperturas.find(Filters.eq("estado", "ABIERTA")).first();
            if (doc == null) {
                doc = collAperturas.find().sort(new Document("fechaHora", -1)).first();
            }
            if (doc != null) {
                return doc.getInteger("idCajero");
            }
        } catch (Exception e) {
            System.err.println("Error al buscar cajero activo: " + e.getMessage());
        }
        return 1;
    }

    private double obtenerDoubleSeguro(Document doc, String campo) {
        Object valor = doc.get(campo);
        if (valor instanceof Number) {
            return ((Number) valor).doubleValue();
        }
        return 0.0;
    }
}