package AdministradorCajaPersistencia.DAOs;

import AdministradorCajaPersistencia.Entitys.venta;
import AdministradorCajaPersistencia.Interfaces.IVentaDAO;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ventaDAO implements IVentaDAO {

    private final MongoCollection<Document> coleccion;

    public ventaDAO(MongoDatabase database) {
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

    private double obtenerDoubleSeguro(Document doc, String campo) {
        Object valor = doc.get(campo);
        if (valor instanceof Number) {
            return ((Number) valor).doubleValue();
        }
        return 0.0;
    }
}