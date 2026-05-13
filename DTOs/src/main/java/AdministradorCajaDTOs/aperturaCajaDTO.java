package AdministradorCajaDTOs;

import java.util.Date;

public class aperturaCajaDTO {
    private int idApertura;
    private Date fecha;
    private int idCajero;
    private String nombreCajero;
    private double montoInicial;
    private int idSupervisor;

    public aperturaCajaDTO(int idCajero, String nombreCajero, double montoInicial, int idSupervisor) {
        this.fecha = new Date();
        this.idCajero = idCajero;
        this.nombreCajero = nombreCajero;
        this.montoInicial = montoInicial;
        this.idSupervisor = idSupervisor;
    }

    public int getIdApertura() { return idApertura; }
    public Date getFecha() { return fecha; }
    public String getNombreCajero() { return nombreCajero; }
    public double getMontoInicial() { return montoInicial; }
    public int getIdCajero() { return idCajero; }
    public int getIdSupervisor() { return idSupervisor; }
}