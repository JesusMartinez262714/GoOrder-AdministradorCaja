package Interfaces;

import Entitys.venta;
import java.util.Date;
import java.util.List;

public interface IVentaDAO {
    List<venta> obtenerVentas(int idCajero, Date fechaApertura);
    boolean insertarVenta(venta nuevaVenta);
    int obtenerIdCajeroConCajaAbierta();
}