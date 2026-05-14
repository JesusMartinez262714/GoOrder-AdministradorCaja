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

public class GestionSupervisoresBO implements IGestionSupervisoresBO {

    private ISupervisorDAO supervisorDAO;

    public GestionSupervisoresBO(ISupervisorDAO supervisorDAO) {
        this.supervisorDAO = supervisorDAO;
    }

    @Override
    public boolean registrarSupervisor(supervisorDTO dto) {
        List<supervisorDTO> actuales = obtenerTodosLosSupervisores("");

        for (supervisorDTO s : actuales) {
            if (s.getNombreCompleto().equalsIgnoreCase(dto.getNombreCompleto())) {
                JOptionPane.showMessageDialog(null,
                        "Error: Ya existe un supervisor registrado con el nombre '" + dto.getNombreCompleto() + "'.",
                        "Registro Duplicado", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return supervisorDAO.registrarSupervisor(SupervisorMapper.dtoToEntity(dto));
    }

    @Override
    public boolean editarSupervisor(supervisorDTO dto) {
        return supervisorDAO.editarSupervisor(SupervisorMapper.dtoToEntity(dto));
    }

    @Override
    public boolean eliminarSupervisor(int idSupervisor) {
        return supervisorDAO.eliminarSupervisor(idSupervisor);
    }

    @Override
    public List<supervisorDTO> obtenerTodosLosSupervisores(String filtroNombre) {
        List<supervisor> entidades = supervisorDAO.obtenerTodos(filtroNombre);
        List<supervisorDTO> dtos = new ArrayList<>();

        for (supervisor e : entidades) {
            dtos.add(SupervisorMapper.entityToDTO(e));
        }

        return dtos;
    }

    @Override
    public List<supervisorDTO> obtenerSupervisoresFiltrados(String nombre) {
        List<supervisor> entidades = supervisorDAO.obtenerTodos(nombre);

        return entidades.stream()
                .map(SupervisorMapper::entityToDTO)
                .collect(Collectors.toList());
    }
}