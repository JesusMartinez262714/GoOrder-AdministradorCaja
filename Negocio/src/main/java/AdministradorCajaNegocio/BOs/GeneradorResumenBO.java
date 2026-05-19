package AdministradorCajaNegocio.BOs;

import AdministradorCajaDTOs.resumenVentasDTO;
import AdministradorCajaDTOs.ventaDTO;
import AdministradorCajaNegocio.Interfaces.IGeneradorResumenBO;
import AdministradorCajaNegocio.Mappers.VentaMapper;
import Entitys.venta;
import Interfaces.IVentaDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Clase de lógica de negocio encargada de procesar las transacciones de un turno.
 * Filtra las ventas registradas por un cajero desde su hora de apertura y calcula
 * los totales acumulados divididos por cada método de pago disponible.
 * * @author Jesus Manuel Martinez Cortez
 */
public class GeneradorResumenBO implements IGeneradorResumenBO {

    private final IVentaDAO miVentaDAO;

    /**
     * Constructor que recibe el DAO de ventas para comunicar la capa de negocio
     * con la persistencia de la base de datos.
     * * @param ventaDAO Instancia del objeto de acceso a datos para las ventas.
     */
    public GeneradorResumenBO(IVentaDAO ventaDAO) {
        this.miVentaDAO = ventaDAO;
    }

    /**
     * Coordina el proceso completo para obtener las ventas de un cajero y mandarlas
     * a clasificar para generar el objeto DTO con los totales del resumen.
     * * @param idCajero ID único del cajero del cual se consulta el turno.
     * @param fechaApertura Fecha y hora exacta en la que se inició el turno de caja.
     * @return Objeto DTO que contiene la suma de los montos de cada método de pago y el total general.
     */
    @Override
    public resumenVentasDTO generarResumenVentasTurno(int idCajero, Date fechaApertura) {
        return calcularTotalesPorMetodo(extraerVentasPorCajero(idCajero, fechaApertura));
    }

    /**
     * Consulta las ventas de la base de datos por medio del DAO y las transforma
     * de entidades de persistencia a una lista de objetos DTO de negocio.
     * * @param idCajero ID único del cajero.
     * @param fechaApertura Fecha y hora del inicio del turno de la caja.
     * @return Lista de objetos DTO con la información individual de cada venta encontrada.
     */
    @Override
    public List<ventaDTO> extraerVentasPorCajero(int idCajero, Date fechaApertura) {
        List<venta> entidades = miVentaDAO.obtenerVentas(idCajero, fechaApertura);
        List<ventaDTO> dtos = new ArrayList<>();
        if (entidades != null) {
            for (venta v : entidades) {
                dtos.add(VentaMapper.entityToDTO(v));
            }
        }
        return dtos;
    }

    /**
     * Recorre la lista de ventas proporcionada y va sumando los montos totales
     * en variables separadas según corresponda a su ID de método de pago.
     * * @param listaVentas Colección de objetos DTO con las ventas a clasificar.
     * @return Objeto DTO resumenVentasDTO con el desglose numérico final de las categorías.
     */
    @Override
    public resumenVentasDTO calcularTotalesPorMetodo(List<ventaDTO> listaVentas) {
        double efectivo = 0, tarjeta = 0, app = 0, referencia = 0;
        if (listaVentas != null) {
            for (ventaDTO v : listaVentas) {
                switch (v.getIdMetodoPago()) {
                    case 1 -> efectivo += v.getMontoTotal();
                    case 2 -> tarjeta += v.getMontoTotal();
                    case 3 -> app += v.getMontoTotal();
                    case 4 -> referencia += v.getMontoTotal();
                }
            }
        }
        return new resumenVentasDTO(efectivo, tarjeta, app, referencia, efectivo + tarjeta + app + referencia);
    }
}