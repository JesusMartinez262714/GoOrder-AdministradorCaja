package AdministradorCajaNegocio.Mappers;

import AdministradorCajaDTOs.supervisorDTO;
import AdministradorCajaPersistencia.Entitys.supervisor;

public class SupervisorMapper {

    public static supervisorDTO entityToDTO(supervisor e) {
        if (e == null) return null;
        return new supervisorDTO(e.getIdSupervisor(), e.getNombre() + " " + e.getApellido());
    }

    public static supervisor dtoToEntity(supervisorDTO d) {
        if (d == null) return null;
        supervisor e = new supervisor();
        e.setIdSupervisor(d.getIdSupervisor());

        String[] partes = d.getNombreCompleto().split(" ", 2);
        e.setNombre(partes[0]);
        e.setApellido(partes.length > 1 ? partes[1] : "");

        return e;
    }
}