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
import AdministradorCajaPersistencia.Interfaces.ICajeroDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase de lógica de negocio encargada de gestionar los cortes de caja del sistema.
 * Controla operaciones como el cálculo de diferencias, el registro de desgloses
 * de dinero y la consulta del historial de cortes realizados.
 * * @author Jesus Manuel Martinez Cortez
 */
public class corteCajaBO implements ICorteCajaBO {

    private final ICorteCajaDAO corteDAO;
    private final ICajeroDAO cajeroDAO;

    /**
     * Constructor que recibe los DAOs de cortes y cajeros para conectar la capa
     * de negocio con el acceso a la base de datos.
     * * @param corteDAO Instancia del objeto de acceso a datos para cortes de caja.
     * @param cajeroDAO Instancia del objeto de acceso a datos para cajeros.
     */
    public corteCajaBO(ICorteCajaDAO corteDAO, ICajeroDAO cajeroDAO) {
        this.corteDAO = corteDAO;
        this.cajeroDAO = cajeroDAO;
    }

    /**
     * Calcula la diferencia matemática entre el dinero contado físicamente
     * y el dinero que el sistema esperaba que hubiera en la caja.
     * * @param totalEsperado Monto total esperado según el sistema de ventas.
     * @param totalReal Monto total físico contado y declarado.
     * @return El valor de la diferencia resultante (negativo si hace falta dinero).
     */
    @Override
    public double calcularDiferencia(double totalEsperado, double totalReal) {
        return totalReal - totalEsperado;
    }

    /**
     * Evalúa la diferencia monetaria para determinar qué estado le corresponde
     * asignar al registro del corte de caja.
     * * @param diferencia El valor de la diferencia calculado en el arqueo.
     * @return Cadena de texto con el nombre del estado asignado.
     */
    @Override
    public String evaluarEstadoCorte(double diferencia) {
        return "Vigente";
    }

    /**
     * Convierte los datos del DTO a la entidad de persistencia y guarda el nuevo
     * corte de caja junto con su lista detallada de desgloses en la base de datos.
     * * @param datosCorte Objeto DTO con los totales y observaciones del corte.
     * @param listaDesgloses Colección de DTOs con las cantidades por método de pago.
     * @return true si el corte se guardó correctamente en la base de datos, false en caso contrario.
     */
    @Override
    public boolean guardarNuevoCorte(corteCajaDTO datosCorte, List<desgloseDTO> listaDesgloses) {
        corteCaja entidad = CorteCajaMapper.dtoToEntity(datosCorte);

        if (listaDesgloses != null) {
            List<desgloseMontos> desgloses = listaDesgloses.stream()
                    .map(DesgloseMapper::dtoToEntity)
                    .collect(Collectors.toList());
            entidad.setListaDesglose(desgloses);
        }

        return corteDAO.guardarNuevoCorte(entidad);
    }

    /**
     * Consulta el historial de cortes de caja realizados en un rango de fechas
     * específico y recupera el nombre de los cajeros involucrados para armar los DTOs.
     * * @param inicio Fecha límite inferior para realizar la búsqueda.
     * @param fin Fecha límite superior para realizar la búsqueda.
     * @return Lista de objetos DTO con la información de los cortes encontrados.
     */
    @Override
    public List<corteCajaDTO> consultarCortesRealizados(Date inicio, Date fin) {
        List<corteCaja> entidades = corteDAO.consultarCortesRealizados(inicio, fin);
        List<corteCajaDTO> listaFinal = new ArrayList<>();

        if (entidades != null) {
            for (corteCaja e : entidades) {
                cajero c = cajeroDAO.consultarPorId(e.getIdCajero());
                String nombre = (c != null) ? c.getNombreCompleto() : "Cajero Desconocido";

                corteCajaDTO dto = CorteCajaMapper.entityToDTO(e, nombre);

                if (e.getListaDesglose() != null) {
                    List<desgloseDTO> desglosesDTO = e.getListaDesglose().stream()
                            .map(DesgloseMapper::entityToDTO)
                            .collect(Collectors.toList());
                    dto.setListaDesglose(desglosesDTO);
                }

                listaFinal.add(dto);
            }
        }
        return listaFinal;
    }

}