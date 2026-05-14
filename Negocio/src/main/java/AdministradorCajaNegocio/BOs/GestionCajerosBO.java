package AdministradorCajaNegocio.BOs;

import AdministradorCajaDTOs.cajeroDTO;
import AdministradorCajaNegocio.Interfaces.IGestionCajerosBO;
import AdministradorCajaNegocio.Mappers.CajeroMapper;
import AdministradorCajaPersistencia.Entitys.adeudo;
import AdministradorCajaPersistencia.Entitys.cajero;
import AdministradorCajaPersistencia.Interfaces.IAdeudoDAO;
import AdministradorCajaPersistencia.Interfaces.ICajeroDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GestionCajerosBO implements IGestionCajerosBO {

    private ICajeroDAO CajeroDAO;

    private IAdeudoDAO AdeudoDAO;


    public GestionCajerosBO(ICajeroDAO cajeroDAO, IAdeudoDAO adeudoDAO) {
        this.CajeroDAO = cajeroDAO;
        this.AdeudoDAO = adeudoDAO;
    }

    @Override
    public boolean registrarCajero(cajeroDTO dto) {
        return CajeroDAO.insertarCajero(CajeroMapper.dtoToEntity(dto));
    }

    @Override
    public boolean editarCajero(cajeroDTO dto) {
        return CajeroDAO.actualizarCajero(CajeroMapper.dtoToEntity(dto));
    }

    @Override
    public boolean eliminarCajero(int idCajero) {
        List<adeudo> pendientes = AdeudoDAO.consultarPendientesPorCajero(idCajero);

        if (pendientes != null && !pendientes.isEmpty()) {
            System.out.println("No se puede eliminar: El cajero tiene deudas pendientes.");
            return false;
        }

        return CajeroDAO.eliminarCajero(idCajero);
    }

    @Override
    public List<cajeroDTO> obtenerCajerosFiltrados(String nombre, String turno, String deudaStatus) {
        List<cajero> entidades = CajeroDAO.consultarTodos();
        List<cajeroDTO> listaCompleta = new ArrayList<>();

        for (cajero e : entidades) {
            cajeroDTO dto = CajeroMapper.entityToDTO(e);

            List<adeudo> deudas = AdeudoDAO.consultarPendientesPorCajero(e.getIdCajero());
            dto.setTieneAdeudo(deudas != null && !deudas.isEmpty());

            listaCompleta.add(dto);
        }

        return listaCompleta.stream()
                .filter(c -> nombre == null || nombre.isEmpty() ||
                        c.getNombreCompleto().toLowerCase().contains(nombre.toLowerCase()))
                .filter(c -> turno.equals("Todos") || c.getTurno().equalsIgnoreCase(turno))
                .filter(c -> {
                    if (deudaStatus.equals("Todos")) return true;
                    if (deudaStatus.equals("Con Adeudo")) return c.isTieneAdeudo();
                    if (deudaStatus.equals("Sin Adeudo")) return !c.isTieneAdeudo();
                    return true;
                })
                .collect(Collectors.toList());
    }
}