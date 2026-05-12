package AdministradorCajaPersistencia.mocks;

import AdministradorCajaDTOs.corteCajaDTO;
import AdministradorCajaPersistencia.Interfaces.ICorteCajaDAO;

import java.util.Date;
import java.util.List;

public class CorteCajaDAOMock implements ICorteCajaDAO {
    @Override
    public List<corteCajaDTO> obtenerHistorial(Date inicio, Date fin) {
        return MockDatabase.getHistorial();
    }

    @Override
    public void guardarCorte(corteCajaDTO corte) {
        MockDatabase.getHistorial().add(corte);
    }
}