package DTOs;


import java.util.ArrayList;
import java.util.List;

public class CarritoDTO {

    private List<ProductoSeleccionadoDTO> productos = new ArrayList<>();
    //private Long idCliente;
    private Double subTotal = 0.0;
    private Double total = 0.0;
    private Double descuento = 0.0;
    private String metodoEntrega;
    private String direccionEntrega;
    public CarritoDTO() {
    }

    public CarritoDTO(List<ProductoSeleccionadoDTO> productos, Double subTotal, Double total, Double descuento) {
        this.productos = productos;
        this.subTotal = subTotal;
        this.total = total;
        this.descuento = descuento;

    }


    public void setProductos(List<ProductoSeleccionadoDTO> productos) {
        this.productos = productos;
    }


    public List<ProductoSeleccionadoDTO> getProductos() {
        return productos;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getMetodoEntrega() {
        return metodoEntrega;
    }

    public void setMetodoEntrega(String metodoEntrega) {
        this.metodoEntrega = metodoEntrega;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }
}
