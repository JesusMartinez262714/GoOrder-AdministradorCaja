package AdministradorCajaNegocio.BOs;

import AdministradorCajaDTOs.supervisorDTO;
import AdministradorCajaNegocio.Interfaces.IGestionSupervisoresBO;
import AdministradorCajaNegocio.Mappers.SupervisorMapper;
import AdministradorCajaPersistencia.Entitys.supervisor;
import AdministradorCajaPersistencia.Interfaces.ISupervisorDAO;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase de lógica de negocio encargada de la gestión y administración de los supervisores.
 * Se encarga de aplicar las reglas para el registro de nuevos supervisores, prevenir
 * que se dupliquen nombres en el sistema y coordinar las bajas o modificaciones.
 * * * @author Jesus Manuel Martinez Cortez
 */
public class GestionSupervisoresBO implements IGestionSupervisoresBO {

    private ISupervisorDAO supervisorDAO;

    /**
     * Constructor que recibe el DAO de supervisores para conectar la capa
     * de negocio con la persistencia de la base de datos.
     * * * @param supervisorDAO Instancia del objeto de acceso a datos para supervisores.
     */
    public GestionSupervisoresBO(ISupervisorDAO supervisorDAO) {
        this.supervisorDAO = supervisorDAO;
    }

    /**
     * Registra un nuevo supervisor en el sistema. Valida primero que no exista
     * otro usuario con el mismo nombre completo para evitar registros duplicados.
     * * * @param dto Objeto DTO con los datos del supervisor que se quiere registrar.
     * * @return true si el registro se guardó con éxito, false si el nombre ya existe o el objeto es nulo.
     */
    @Override
    public boolean registrarSupervisor(supervisorDTO dto) {
        if (dto == null || dto.getNombreCompleto() == null) {
            return false;
        }

        List<supervisorDTO> actuales = obtenerTodosLosSupervisores("");
        if (actuales != null) {
            for (supervisorDTO s : actuales) {
                if (s != null && s.getNombreCompleto() != null && s.getNombreCompleto().equalsIgnoreCase(dto.getNombreCompleto())) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null,
                            "Error: Ya existe un supervisor registrado con el nombre '" + dto.getNombreCompleto() + "'.",
                            "Registro Duplicado", JOptionPane.ERROR_MESSAGE));
                    return false;
                }
            }
        }

        return supervisorDAO.registrarSupervisor(SupervisorMapper.dtoToEntity(dto));
    }

    /**
     * Modifica los datos de un supervisor existente transformando el DTO de la vista
     * a su respectiva entidad para aplicar los cambios en la base de datos.
     * * * @param dto Objeto DTO con la información actualizada del supervisor.
     * * @return true si la modificación en la persistencia fue exitosa, false en caso contrario.
     */
    @Override
    public boolean editarSupervisor(supervisorDTO dto) {
        if (dto == null || dto.getIdSupervisor() <= 0) {
            return false;
        }
        return supervisorDAO.editarSupervisor(SupervisorMapper.dtoToEntity(dto));
    }

    /**
     * Elimina permanentemente el registro de un supervisor de la base de datos usando su ID.
     * * * @param idSupervisor ID único del supervisor que se desea eliminar.
     * * @return true si la eliminación en el DAO fue exitosa, false en caso contrario.
     */
    @Override
    public boolean eliminarSupervisor(int idSupervisor) {
        if (idSupervisor <= 0) {
            return false;
        }
        return supervisorDAO.eliminarSupervisor(idSupervisor);
    }

    /**
     * Recupera todos los supervisores de la base de datos y los convierte en una lista
     * de objetos DTO aptos para ser utilizados por la interfaz gráfica.
     * * * @param filtroNombre Texto o filtro opcional para buscar por nombre.
     * * @return Lista de objetos DTO con la información de todos los supervisores.
     */
    @Override
    public List<supervisorDTO> obtenerTodosLosSupervisores(String filtroNombre) {
        List<supervisor> entidades = supervisorDAO.obtenerTodos(filtroNombre);
        List<supervisorDTO> dtos = new ArrayList<>();

        if (entidades != null) {
            for (supervisor e : entidades) {
                if (e != null) {
                    dtos.add(SupervisorMapper.entityToDTO(e));
                }
            }
        }

        return dtos;
    }

    /**
     * Consulta y filtra la lista de supervisores regresando una colección de DTOs
     * mapeados mediante el uso de flujos (Streams).
     * * * @param nombre Filtro o cadena de texto para buscar coincidencias por nombre.
     * * @return Colección de objetos DTO que cumplen con el filtro solicitado.
     */
    @Override
    public List<supervisorDTO> obtenerSupervisoresFiltrados(String nombre) {
        List<supervisor> entidades = supervisorDAO.obtenerTodos(nombre);
        if (entidades == null) {
            return new ArrayList<>();
        }

        return entidades.stream()
                .filter(e -> e != null)
                .map(SupervisorMapper::entityToDTO)
                .collect(Collectors.toList());
    }
}