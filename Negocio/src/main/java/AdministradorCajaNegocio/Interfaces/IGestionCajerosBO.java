package AdministradorCajaNegocio.Interfaces;


import AdministradorCajaDTOs.cajeroDTO;
import java.util.List;

public interface IGestionCajerosBO {
    boolean registrarCajero(cajeroDTO dto);
    boolean editarCajero(cajeroDTO dto);
    boolean eliminarCajero(int idCajero);


    List<cajeroDTO> obtenerCajerosFiltrados(String nombre, String turno, String deudaStatus);
}