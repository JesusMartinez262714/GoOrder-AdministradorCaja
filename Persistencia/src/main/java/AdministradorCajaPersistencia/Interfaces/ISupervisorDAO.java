package AdministradorCajaPersistencia.Interfaces;

import AdministradorCajaPersistencia.Entitys.supervisor;
import java.util.List;

public interface ISupervisorDAO {

    supervisor consultarPorId(int id);


    List<supervisor> obtenerTodos(String filtroNombre);

    boolean registrarSupervisor(supervisor entidad);

    boolean editarSupervisor(supervisor entidad);

    boolean eliminarSupervisor(int id);
}