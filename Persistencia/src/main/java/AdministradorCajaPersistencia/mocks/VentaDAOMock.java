package AdministradorCajaPersistencia.mocks;

import AdministradorCajaPersistencia.Interfaces.IVentaDAO;

import AdministradorCajaDTOs.ventaDTO;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class VentaDAOMock implements IVentaDAO {

    @Override
    public List<ventaDTO> obtenerVentas(int idCajero, Date fecha) {
        return MockDatabase.getVentas().stream()
                .filter(v -> v.getIdCajero() == idCajero)
                .collect(Collectors.toList());
    }
}