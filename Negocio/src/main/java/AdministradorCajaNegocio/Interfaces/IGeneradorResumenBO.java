package AdministradorCajaNegocio.Interfaces;

import AdministradorCajaDTOs.resumenVentasDTO;
import AdministradorCajaDTOs.ventaDTO;
import java.util.Date;
import java.util.List;

public interface IGeneradorResumenBO {
    List<ventaDTO> extraerVentasPorCajero(int idCajero, Date fechaActual);
    resumenVentasDTO calcularTotalesPorMetodo(List<ventaDTO> listaVentas);
}