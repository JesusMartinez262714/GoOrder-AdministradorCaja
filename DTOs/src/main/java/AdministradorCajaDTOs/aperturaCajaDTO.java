package AdministradorCajaDTOs;

public class aperturaCajaDTO {
    private int idCajero;
    private String nombreCajero;
    private double montoInicial;
    private int idSupervisor;

    public aperturaCajaDTO(int idCajero, String nombreCajero, double montoInicial, int idSupervisor) {
        this.idCajero = idCajero;
        this.nombreCajero = nombreCajero;
        this.montoInicial = montoInicial;
        this.idSupervisor = idSupervisor;
    }

    public int getIdCajero() { return idCajero; }
    public double getMontoInicial() { return montoInicial; }
    public int getIdSupervisor() { return idSupervisor; }
}