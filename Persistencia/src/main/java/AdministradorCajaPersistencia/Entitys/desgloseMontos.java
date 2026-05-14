package AdministradorCajaPersistencia.Entitys;

public class desgloseMontos {
    private double montoDeclarado;
    private int idMetodoPago;
    private String nombreMetodo;
    private int idCorte;

    public desgloseMontos() {}

    public double getMontoDeclarado() { return montoDeclarado; }
    public void setMontoDeclarado(double monto) { this.montoDeclarado = monto; }
    public int getIdMetodoPago() { return idMetodoPago; }
    public void setIdMetodoPago(int id) { this.idMetodoPago = id; }
    public String getNombreMetodo() { return nombreMetodo; }
    public void setNombreMetodo(String nombre) { this.nombreMetodo = nombre; }
    public int getIdCorte() { return idCorte; }
    public void setIdCorte(int idCorte) { this.idCorte = idCorte; }
}