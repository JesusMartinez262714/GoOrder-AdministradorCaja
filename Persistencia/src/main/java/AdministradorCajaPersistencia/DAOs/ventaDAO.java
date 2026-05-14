package AdministradorCajaPersistencia.DAOs;

import AdministradorCajaPersistencia.Entitys.venta;
import AdministradorCajaPersistencia.Interfaces.IVentaDAO;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ventaDAO implements IVentaDAO {

    private final MongoCollection<Document> coleccion;

    public ventaDAO(MongoDatabase database) {
        this.coleccion = database.getCollection("ventas");
    }

    @Override
    public List<venta> obtenerVentas(int idCajero, Date fecha) {
        List<venta> ventas = new ArrayList<>();

        LocalDate localDate = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date inicioDia = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date finDia = Date.from(localDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

        try {
            Document query = new Document("idCajero", idCajero)
                    .append("fecha", new Document("$gte", inicioDia).append("$lt", finDia));

            for (Document doc : coleccion.find(query)) {
                venta v = new venta();
                v.setIdVenta(doc.getInteger("idVenta"));
                v.setMontoTotal(doc.getDouble("montoTotal"));
                v.setIdMetodoPago(doc.getInteger("idMetodoPago"));
                v.setIdCajero(doc.getInteger("idCajero"));
                v.setFecha(doc.getDate("fecha"));
                ventas.add(v);
            }
        } catch (Exception e) {
            System.err.println("Error al consultar ventas en MongoDB: " + e.getMessage());
        }

        return ventas;
    }
}