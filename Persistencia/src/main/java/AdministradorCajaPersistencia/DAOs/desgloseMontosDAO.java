package AdministradorCajaPersistencia.DAOs;

import AdministradorCajaPersistencia.Entitys.desgloseMontos;
import AdministradorCajaPersistencia.Interfaces.IDesgloseMontosDAO;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import java.util.List;
import java.util.ArrayList;

public class desgloseMontosDAO implements IDesgloseMontosDAO {
    private MongoCollection<Document> coleccion;

    public desgloseMontosDAO(MongoDatabase db) {
        this.coleccion = db.getCollection("desgloses");
    }

    @Override
    public boolean insertarListaDesgloses(List<desgloseMontos> desgloses, int idCorte) {
        try {
            List<Document> documentos = new ArrayList<>();
            for (desgloseMontos d : desgloses) {
                documentos.add(new Document("idCorte", idCorte)
                        .append("montoDeclarado", d.getMontoDeclarado())
                        .append("idMetodoPago", d.getIdMetodoPago())
                        .append("nombreMetodo", d.getNombreMetodo()));
            }
            coleccion.insertMany(documentos);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    @Override
    public List<desgloseMontos> consultarPorCorte(int idCorte) {
        List<desgloseMontos> lista = new ArrayList<>();
        for (Document doc : coleccion.find(Filters.eq("idCorte", idCorte))) {
            desgloseMontos d = new desgloseMontos();
            d.setNombreMetodo(doc.getString("metodo"));

            d.setMontoDeclarado(obtenerDoubleSeguro(doc, "monto"));

            lista.add(d);
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
}