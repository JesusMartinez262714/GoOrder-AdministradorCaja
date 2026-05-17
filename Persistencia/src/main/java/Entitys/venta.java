package Entitys;

import java.util.Date;

public class venta {

    private int idVenta;
    private double montoTotal;
    private int idMetodoPago;
    private int idCajero;
    private Date fecha;

    public venta() {
    }

    public venta(int idVenta, double montoTotal, int idMetodoPago, int idCajero, Date fecha) {
        this.idVenta = idVenta;
        this.montoTotal = montoTotal;
        this.idMetodoPago = idMetodoPago;
        this.idCajero = idCajero;
        this.fecha = fecha;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public int getIdMetodoPago() {
        return idMetodoPago;
    }

    public void setIdMetodoPago(int idMetodoPago) {
        this.idMetodoPago = idMetodoPago;
    }

    public int getIdCajero() {
        return idCajero;
    }

    public void setIdCajero(int idCajero) {
        this.idCajero = idCajero;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}