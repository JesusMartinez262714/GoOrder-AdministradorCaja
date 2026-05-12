package AdministradorCajaDTOs;

public class desgloseDTO {

    private double montoDeclarado;
    private int idMetodoPago;
    private String nombreMetodo;


    public desgloseDTO() {
    }

    public desgloseDTO(double montoDeclarado, int idMetodoPago, String nombreMetodo) {
        this.montoDeclarado = montoDeclarado;
        this.idMetodoPago = idMetodoPago;
        this.nombreMetodo = nombreMetodo;
    }

    public double getMontoDeclarado() { return montoDeclarado; }
    public void setMontoDeclarado(double montoDeclarado) { this.montoDeclarado = montoDeclarado; }

    public int getIdMetodoPago() { return idMetodoPago; }
    public void setIdMetodoPago(int idMetodoPago) { this.idMetodoPago = idMetodoPago; }

    public String getNombreMetodo() { return nombreMetodo; }
    public void setNombreMetodo(String nombreMetodo) { this.nombreMetodo = nombreMetodo; }
}