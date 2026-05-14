package AdministradorCajaDTOs;

public class cajeroDTO {
    private int idCajero;
    private String nombreCompleto;
    private String turno;
    private boolean tieneAdeudo;

    public cajeroDTO(int idCajero, String nombreCompleto, String turno) {
        this.idCajero = idCajero;
        this.nombreCompleto = nombreCompleto;
        this.turno = turno;
    }


    public int getIdCajero() { return idCajero; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getTurno() { return turno; }
    public boolean isTieneAdeudo() { return tieneAdeudo; }
    public void setTieneAdeudo(boolean tieneAdeudo) { this.tieneAdeudo = tieneAdeudo; }

    public void setIdCajero(int idCajero) {
        this.idCajero = idCajero;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    @Override
    public String toString() { return nombreCompleto; }
}