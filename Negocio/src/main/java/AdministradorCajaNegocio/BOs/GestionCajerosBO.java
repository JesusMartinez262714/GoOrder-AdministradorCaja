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

/**
 * Clase de lógica de negocio encargada de la gestión y administración de los cajeros.
 * Se encarga de coordinar los filtros de búsqueda por turnos o deudas y valida las
 * restricciones del sistema, como impedir la eliminación de cajeros con adeudos activos.
 * * @author Jesus Manuel Martinez Cortez
 */
public class GestionCajerosBO implements IGestionCajerosBO {

    private ICajeroDAO CajeroDAO;
    private IAdeudoDAO AdeudoDAO;

    /**
     * Constructor que recibe los DAOs de cajeros y adeudos para comunicar la capa
     * de negocio con la persistencia de la base de datos.
     * * @param cajeroDAO Instancia del objeto de acceso a datos para los cajeros.
     * @param adeudoDAO Instancia del objeto de acceso a datos para las deudas.
     */
    public GestionCajerosBO(ICajeroDAO cajeroDAO, IAdeudoDAO adeudoDAO) {
        this.CajeroDAO = cajeroDAO;
        this.AdeudoDAO = adeudoDAO;
    }

    /**
     * Registra un nuevo cajero convirtiendo los datos del objeto DTO a una entidad
     * de persistencia antes de mandarlo a guardar en la base de datos.
     * * @param dto Objeto DTO con la información que se va a registrar.
     * @return true si el cajero se insertó correctamente, false en caso contrario.
     */
    @Override
    public boolean registrarCajero(cajeroDTO dto) {
        if (dto == null) {
            return false;
        }
        return CajeroDAO.insertarCajero(CajeroMapper.dtoToEntity(dto));
    }

    /**
     * Modifica los datos de un cajero existente transformando el DTO de la vista
     * a su respectiva entidad para aplicar los cambios en la base de datos.
     * * @param dto Objeto DTO con la información actualizada del cajero.
     * @return true si la actualización en la persistencia fue exitosa, false en caso contrario.
     */
    @Override
    public boolean editarCajero(cajeroDTO dto) {
        if (dto == null || dto.getIdCajero() <= 0) {
            return false;
        }
        return CajeroDAO.actualizarCajero(CajeroMapper.dtoToEntity(dto));
    }

    /**
     * Intenta eliminar un cajero del sistema por su ID único. Realiza una validación
     * previa para comprobar si el empleado tiene deudas pendientes; si cuenta con saldos
     * sin pagar, la operación se rechaza por seguridad financiera.
     * * @param idCajero ID único del cajero que se desea eliminar.
     * @return true si el cajero fue removido con éxito, false si tiene adeudos o falló el proceso.
     */
    @Override
    public boolean eliminarCajero(int idCajero) {
        if (idCajero <= 0) {
            return false;
        }

        List<adeudo> pendientes = AdeudoDAO.consultarPendientesPorCajero(idCajero);

        if (pendientes != null && !pendientes.isEmpty()) {
            System.out.println("No se puede eliminar: El cajero tiene deudas pendientes.");
            return false;
        }

        return CajeroDAO.eliminarCajero(idCajero);
    }

    /**
     * Consulta la lista total de cajeros en la base de datos, calcula dinámicamente la suma
     * de sus adeudos pendientes y aplica filtros de coincidencia por nombre completo,
     * turnos específicos y estados de cuenta.
     * * @param nombre Filtro para buscar coincidencias parciales en el nombre del cajero.
     * @param turno Filtro para separar por turnos de trabajo o "Todos" para omitir el filtro.
     * @param deudaStatus Filtro para separar cajeros según su estado ("Todos", "Con Adeudo", "Sin Adeudo").
     * @return Colección de objetos DTO filtrados y configurados listos para mostrarse en la interfaz gráfica.
     */
    @Override
    public List<cajeroDTO> obtenerCajerosFiltrados(String nombre, String turno, String deudaStatus) {
        List<cajero> entidades = CajeroDAO.consultarTodos();
        List<cajeroDTO> listaCompleta = new ArrayList<>();

        if (entidades == null) {
            return listaCompleta;
        }

        for (cajero e : entidades) {
            if (e != null) {
                cajeroDTO dto = CajeroMapper.entityToDTO(e);
                List<adeudo> deudas = AdeudoDAO.consultarPendientesPorCajero(e.getIdCajero());

                double montoTotalAdeudo = 0.0;
                if (deudas != null) {
                    for (adeudo a : deudas) {
                        if (a != null) {
                            montoTotalAdeudo += a.getMonto();
                        }
                    }
                }

                dto.setMontoAdeudo(montoTotalAdeudo);
                dto.setTieneAdeudo(montoTotalAdeudo > 0.01);

                listaCompleta.add(dto);
            }
        }

        return listaCompleta.stream()
                .filter(c -> nombre == null || nombre.isEmpty() ||
                        c.getNombreCompleto().toLowerCase().contains(nombre.toLowerCase()))
                .filter(c -> turno == null || turno.equals("Todos") || c.getTurno().equalsIgnoreCase(turno))
                .filter(c -> {
                    if (deudaStatus == null || deudaStatus.equals("Todos")) return true;
                    if (deudaStatus.equals("Con Adeudo")) return c.isTieneAdeudo();
                    if (deudaStatus.equals("Sin Adeudo")) return !c.isTieneAdeudo();
                    return true;
                })
                .collect(Collectors.toList());
    }
}