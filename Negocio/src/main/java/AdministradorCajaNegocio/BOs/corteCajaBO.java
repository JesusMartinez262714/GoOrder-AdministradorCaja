package AdministradorCajaNegocio.BOs;

import AdministradorCajaDTOs.corteCajaDTO;
import AdministradorCajaDTOs.desgloseDTO;
import AdministradorCajaNegocio.Interfaces.ICorteCajaBO;
import AdministradorCajaNegocio.Mappers.CorteCajaMapper;
import AdministradorCajaNegocio.Mappers.DesgloseMapper;
import AdministradorCajaPersistencia.Entitys.cajero;
import AdministradorCajaPersistencia.Entitys.corteCaja;
import AdministradorCajaPersistencia.Entitys.desgloseMontos;
import AdministradorCajaPersistencia.Interfaces.ICorteCajaDAO;
import AdministradorCajaPersistencia.Interfaces.IDesgloseMontosDAO;
import AdministradorCajaPersistencia.Interfaces.ICajeroDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class corteCajaBO implements ICorteCajaBO {

    private final ICorteCajaDAO corteDAO;
    private final IDesgloseMontosDAO desgloseDAO;
    private final ICajeroDAO cajeroDAO;

    public corteCajaBO(ICorteCajaDAO corteDAO, IDesgloseMontosDAO desgloseDAO, ICajeroDAO cajeroDAO) {
        this.corteDAO = corteDAO;
        this.desgloseDAO = desgloseDAO;
        this.cajeroDAO = cajeroDAO;
    }

    @Override
    public double calcularDiferencia(double totalEsperado, double totalReal) {
        return totalEsperado - totalReal;
    }

    @Override
    public String evaluarEstadoCorte(double diferencia) {
        return "Vigente";
    }

    @Override
    public boolean guardarNuevoCorte(corteCajaDTO datosCorte, List<desgloseDTO> listaDesgloses) {
        corteCaja entidad = CorteCajaMapper.dtoToEntity(datosCorte);

        if (corteDAO.guardarNuevoCorte(entidad)) {
            List<desgloseMontos> desgloses = listaDesgloses.stream()
                    .map(d -> {
                        desgloseMontos e = DesgloseMapper.dtoToEntity(d);
                        e.setIdCorte(entidad.getId());
                        return e;
                    })
                    .collect(Collectors.toList());

            return desgloseDAO.insertarListaDesgloses(desgloses, entidad.getId());
        }
        return false;
    }

    @Override
    public List<corteCajaDTO> consultarCortesRealizados(Date inicio, Date fin) {
        List<corteCaja> entidades = corteDAO.consultarCortesRealizados(inicio, fin);
        List<corteCajaDTO> listaFinal = new ArrayList<>();

        if (entidades != null) {
            for (corteCaja e : entidades) {
                cajero c = cajeroDAO.consultarPorId(e.getIdCajero());
                String nombre = (c != null) ? c.getNombreCompleto() : "Cajero Desconocido";

                corteCajaDTO dto = CorteCajaMapper.entityToDTO(e, nombre);

                List<desgloseDTO> desglosesDTO = desgloseDAO.consultarPorCorte(e.getId())
                        .stream()
                        .map(DesgloseMapper::entityToDTO)
                        .collect(Collectors.toList());

                dto.setListaDesglose(desglosesDTO);
                listaFinal.add(dto);
            }
        }
        return listaFinal;
    }
}