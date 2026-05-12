package AdministradorCajaDTOs;

public class ventaDTO {

    private int idVenta;
    private double montoTotal;
    private int idMetodoPago;
    private int idCajero;

    public ventaDTO() {
    }

    public ventaDTO(int idVenta, double montoTotal, int idMetodoPago, int idCajero) {
        this.idVenta = idVenta;
        this.montoTotal = montoTotal;
        this.idMetodoPago = idMetodoPago;
        this.idCajero = idCajero;
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
}