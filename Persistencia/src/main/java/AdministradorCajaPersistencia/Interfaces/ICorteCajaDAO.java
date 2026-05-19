package AdministradorCajaPersistencia.Interfaces;

import AdministradorCajaPersistencia.Entitys.corteCaja;
import org.bson.Document;
import java.util.Date;
import java.util.List;

public interface ICorteCajaDAO {
    boolean registrarApertura(int idCajero, double montoInicial, int idSupervisor);
    boolean guardarNuevoCorte(corteCaja entidad);
    List<corteCaja> consultarCortesRealizados(Date inicio, Date fin);
    boolean eliminarCorte(int idCorte);

    boolean tieneAperturaActiva(int idCajero);
    List<Integer> obtenerIdsCajerosConCajaAbierta();
    Document consultarUltimaCaja(int idCajero);

    boolean actualizarEstadoCorte(int idCaja, String nuevoEstado, String motivoCancelacion);
}