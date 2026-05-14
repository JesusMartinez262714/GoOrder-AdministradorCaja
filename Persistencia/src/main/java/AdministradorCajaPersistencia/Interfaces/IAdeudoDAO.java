package AdministradorCajaPersistencia.Interfaces;

import AdministradorCajaPersistencia.Entitys.adeudo;

import java.util.List;

public interface IAdeudoDAO {

    List<adeudo> consultarPendientesPorCajero(int idCajero);
}