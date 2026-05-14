package AdministradorCajaNegocio.Mappers;

import AdministradorCajaDTOs.cajeroDTO;
import AdministradorCajaPersistencia.Entitys.cajero;

public class CajeroMapper {
    public static cajeroDTO entityToDTO(cajero e) {
        if (e == null) return null;
        return new cajeroDTO(e.getIdCajero(), e.getNombre() + " " + e.getApellido(), e.getTurno());
    }

    public static cajero dtoToEntity(cajeroDTO d) {
        if (d == null) return null;
        cajero e = new cajero();
        e.setIdCajero(d.getIdCajero());
        String[] partes = d.getNombreCompleto().split(" ", 2);
        e.setNombre(partes[0]);
        e.setApellido(partes.length > 1 ? partes[1] : "");
        e.setTurno(d.getTurno());
        return e;
    }
}