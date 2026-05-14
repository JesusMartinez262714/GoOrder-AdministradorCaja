package AdministradorCajaPersistencia.Entitys;

import java.util.Date;

public class aperturaCaja {
    private int idApertura;
    private Date fechaHora;
    private double montoInicial;
    private int idCajero;
    private int idSupervisor;

    public aperturaCaja() {}

    // Getters y Setters
    public int getIdApertura() { return idApertura; }
    public void setIdApertura(int id) { this.idApertura = id; }
    public Date getFechaHora() { return fechaHora; }
    public void setFechaHora(Date f) { this.fechaHora = f; }
    public double getMontoInicial() { return montoInicial; }
    public void setMontoInicial(double m) { this.montoInicial = m; }
    public int getIdCajero() { return idCajero; }
    public void setIdCajero(int id) { this.idCajero = id; }
    public int getIdSupervisor() { return idSupervisor; }
    public void setIdSupervisor(int id) { this.idSupervisor = id; }
}