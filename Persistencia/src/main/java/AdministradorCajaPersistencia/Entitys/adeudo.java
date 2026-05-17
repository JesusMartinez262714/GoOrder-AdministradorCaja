package AdministradorCajaPersistencia.Entitys;

public class adeudo {
    private int idCajero;
    private double monto;
    private String estado;

    public adeudo() {}

    public int getIdCajero() { return idCajero; }
    public void setIdCajero(int idCajero) { this.idCajero = idCajero; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}