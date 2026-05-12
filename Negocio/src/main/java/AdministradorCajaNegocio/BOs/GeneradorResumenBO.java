package AdministradorCajaNegocio.BOs;

import AdministradorCajaDTOs.resumenVentasDTO;
import AdministradorCajaDTOs.ventaDTO;
import AdministradorCajaNegocio.Interfaces.IGeneradorResumenBO;
import AdministradorCajaPersistencia.Interfaces.IVentaDAO;
import java.util.Date;
import java.util.List;

public class GeneradorResumenBO implements IGeneradorResumenBO {

    private IVentaDAO ventaDAO;

    public GeneradorResumenBO(IVentaDAO ventaDAO) {
        this.ventaDAO = ventaDAO;
    }

    @Override
    public List<ventaDTO> extraerVentasPorCajero(int idCajero, Date fechaActual) {
        return ventaDAO.obtenerVentas(idCajero, fechaActual);
    }

    @Override
    public resumenVentasDTO calcularTotalesPorMetodo(List<ventaDTO> listaVentas) {
        resumenVentasDTO resumen = new resumenVentasDTO();

        double sumaEfectivo = 0.0;
        double sumaApp = 0.0;
        double sumaTarjeta = 0.0;

        for (ventaDTO venta : listaVentas) {
            if (venta.getIdMetodoPago() == 1) {
                sumaEfectivo += venta.getMontoTotal();
            } else if (venta.getIdMetodoPago() == 2) {
                sumaApp += venta.getMontoTotal();
            } else if (venta.getIdMetodoPago() == 3) {
                sumaTarjeta += venta.getMontoTotal();
            }
        }

        double totalEsperado = sumaEfectivo + sumaApp + sumaTarjeta;

        resumen.setTotalEfectivo(sumaEfectivo);
        resumen.setTotalApp(sumaApp);
        resumen.setTotalTarjeta(sumaTarjeta);
        resumen.setTotalEsperadoSistema(totalEsperado);

        return resumen;
    }
}