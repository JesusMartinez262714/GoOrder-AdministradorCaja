package AdministradorCajaNegocio.Interfaces;
import AdministradorCajaDTOs.supervisorDTO;
import java.util.List;

public interface IGestionSupervisoresBO {
    boolean registrarSupervisor(supervisorDTO dto);
    boolean editarSupervisor(supervisorDTO dto);
    boolean eliminarSupervisor(int idSupervisor);
    List<supervisorDTO> obtenerTodosLosSupervisores(String filtroNombre);
    List<supervisorDTO> obtenerSupervisoresFiltrados(String nombre);
}