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

public class GeneradorResumenBO implements IGeneradorResumenBO {

    private final IVentaDAO miVentaDAO;

    public GeneradorResumenBO(IVentaDAO ventaDAO) {
        this.miVentaDAO = ventaDAO;
    }

    @Override
    public resumenVentasDTO generarResumenVentasTurno(int idCajero, Date fechaApertura) {
        return calcularTotalesPorMetodo(extraerVentasPorCajero(idCajero, fechaApertura));
    }

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