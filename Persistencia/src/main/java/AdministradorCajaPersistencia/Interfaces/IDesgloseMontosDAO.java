package AdministradorCajaPersistencia.Interfaces;

import AdministradorCajaPersistencia.Entitys.desgloseMontos;
import org.bson.Document;

import java.util.List;

public interface IDesgloseMontosDAO {
    boolean insertarListaDesgloses(List<desgloseMontos> desgloses, int idCorte);

    List<desgloseMontos> consultarPorCorte(int idCorte);

    double obtenerDoubleSeguro(Document doc, String campo);
}