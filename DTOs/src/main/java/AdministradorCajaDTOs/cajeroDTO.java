package AdministradorCajaDTOs;

public class cajeroDTO {
    private int idCajero;
    private String nombreCompleto;
    private String turno;
    private boolean tieneAdeudo;
    private double montoAdeudo;

    public cajeroDTO(int idCajero, String nombreCompleto, String turno) {
        this.idCajero = idCajero;
        this.nombreCompleto = nombreCompleto;
        this.turno = turno;
    }

    public int getIdCajero() { return idCajero; }
    public void setIdCajero(int idCajero) { this.idCajero = idCajero; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }

    public boolean isTieneAdeudo() { return tieneAdeudo; }
    public void setTieneAdeudo(boolean tieneAdeudo) { this.tieneAdeudo = tieneAdeudo; }

    public double getMontoAdeudo() { return montoAdeudo; }
    public void setMontoAdeudo(double montoAdeudo) { this.montoAdeudo = montoAdeudo; }

    @Override
    public String toString() { return nombreCompleto; }
}