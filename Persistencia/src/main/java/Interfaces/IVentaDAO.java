package Interfaces;

import Entitys.venta;

import java.util.Date;
import java.util.List;

public interface IVentaDAO {
    List<venta> obtenerVentas(int idCajero, Date fecha);

    boolean insertarVenta(venta nuevaVenta);
}
