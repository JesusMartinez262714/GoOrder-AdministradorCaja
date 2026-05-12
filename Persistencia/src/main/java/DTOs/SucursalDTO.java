package DTOs;

public class SucursalDTO {
    private int sucursal_id;
    private String nombre;
    private String colonia;
    private String calle;
    private String descripcion;
    private String nombreImagen;

    public SucursalDTO(int sucursal_id, String nombre, String colonia, String calle, String descripcion, String nombreImagen) {
        this.sucursal_id = sucursal_id;
        this.nombre = nombre;
        this.colonia = colonia;
        this.calle = calle;
        this.descripcion = descripcion;
        this.nombreImagen = nombreImagen;
    }

    public int getSucursal_id() {
        return sucursal_id;
    }

    public void setSucursal_id(int sucursal_id) {
        this.sucursal_id = sucursal_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreImagen() {
        return nombreImagen;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
    }
}
