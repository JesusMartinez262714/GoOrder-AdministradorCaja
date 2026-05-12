package AdministradorCajaPersistencia.Interfaces;
import AdministradorCajaDTOs.corteCajaDTO;
import java.util.List;
import java.util.Date;

public interface ICorteCajaDAO {
    List<corteCajaDTO> obtenerHistorial(Date inicio, Date fin);
    void guardarCorte(corteCajaDTO corte);
}