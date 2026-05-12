package DTOs;

public class ProductoDTO {

    private String nombre;
    private String descripcion;
    private Double precio;
    private String imagen;

    private int cantidad = 1;

    public ProductoDTO(String nombre, Double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }
    public ProductoDTO(String nombre, Double precio, String imagen) {
        this.nombre = nombre;
        this.precio = precio;
        this.imagen = imagen;
    }

    public ProductoDTO(String nombre, String descripcion, Double precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public ProductoDTO(String nombre, String descripcion, Double precio, String imagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagen = imagen;
    }

    public ProductoDTO(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public String getImagen() {
        return imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return "ProductoDTO{" + "nombre=" + nombre + ", descripcion=" + descripcion + ", precio=" + precio + ", imagen=" + imagen + ", cantidad=" + cantidad + '}';
    }
}