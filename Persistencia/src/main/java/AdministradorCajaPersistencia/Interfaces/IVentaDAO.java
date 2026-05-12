package AdministradorCajaPersistencia.Interfaces;

import AdministradorCajaDTOs.ventaDTO;

import java.util.Date;
import java.util.List;

public interface IVentaDAO {
    List<ventaDTO> obtenerVentas(int idCajero, Date fecha);
}
