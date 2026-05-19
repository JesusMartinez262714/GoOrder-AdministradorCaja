package AdministradorCajaPersistencia.Interfaces;

import AdministradorCajaPersistencia.Entitys.cajero;

import java.util.List;

public interface ICajeroDAO {

    List<cajero> consultarTodos();

    cajero consultarPorId(int idCajero);

    boolean insertarCajero(cajero entidad);

    boolean actualizarCajero(cajero entidad);

    boolean eliminarCajero(int idCajero);
    boolean actualizarAdeudoAcumulado(int idCajero, double monto);
}