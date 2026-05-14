package AdministradorCajaNegocio.BOs;

import AdministradorCajaDTOs.resumenVentasDTO;
import AdministradorCajaDTOs.ventaDTO;
import AdministradorCajaNegocio.Interfaces.IGeneradorResumenBO;
import AdministradorCajaNegocio.Mappers.VentaMapper;
import AdministradorCajaPersistencia.Entitys.venta;
import AdministradorCajaPersistencia.Interfaces.IVentaDAO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GeneradorResumenBO implements IGeneradorResumenBO {

    private IVentaDAO miVentaDAO;

    public GeneradorResumenBO(IVentaDAO ventaDAO) {
        this.miVentaDAO = ventaDAO;
    }

    @Override
    public resumenVentasDTO generarResumenVentasTurno(int idCajero, Date fechaActual) {
        List<ventaDTO> ventas = extraerVentasPorCajero(idCajero, fechaActual);
        return calcularTotalesPorMetodo(ventas);
    }

    @Override
    public List<ventaDTO> extraerVentasPorCajero(int idCajero, Date fechaActual) {
        List<venta> entidades = miVentaDAO.obtenerVentas(idCajero, fechaActual);
        List<ventaDTO> dtos = new ArrayList<>();
        if (entidades != null) {
            for (venta v : entidades) {
                dtos.add(VentaMapper.entityToDTO(v));
            }
        }
        return dtos;
    }

    @Override
    public resumenVentasDTO calcularTotalesPorMetodo(List<ventaDTO> listaVentas) {
        double efectivo = 0, tarjeta = 0, app = 0, referencia = 0;
        if (listaVentas != null) {
            for (ventaDTO v : listaVentas) {
                switch (v.getIdMetodoPago()) {
                    case 1: efectivo += v.getMontoTotal(); break;
                    case 2: tarjeta += v.getMontoTotal(); break;
                    case 3: app += v.getMontoTotal(); break;
                    case 4: referencia += v.getMontoTotal(); break;
                }
            }
        }
        double totalEsperado = efectivo + tarjeta + app + referencia;
        return new resumenVentasDTO(efectivo, tarjeta, app, referencia, totalEsperado);
    }
}