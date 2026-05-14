package AdministradorCajaPersistencia.Interfaces;

import AdministradorCajaPersistencia.Entitys.corteCaja;
import org.bson.Document;

import java.util.Date;
import java.util.List;

public interface ICorteCajaDAO {


    List<corteCaja> consultarCortesRealizados(Date inicio, Date fin);

    double obtenerDoubleSeguro(Document doc, String campo);

    boolean guardarNuevoCorte(corteCaja entidad);

    boolean eliminarCorte(int idCorte);

    boolean actualizarEstadoCorte(int idCorte, String nuevoEstado);
}