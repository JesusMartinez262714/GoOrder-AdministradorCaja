package AdministradorCajaPersistencia.Interfaces;

import AdministradorCajaPersistencia.Entitys.adeudo;

import java.util.List;

public interface IAdeudoDAO {

    List<adeudo> consultarPendientesPorCajero(int idCajero);

    double consultarAdeudoPorCajero(int idCajero);

    boolean actualizarMontoAdeudo(int idCajero, double nuevoMonto);
}