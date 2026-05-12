package AdministradorCajaDTOs;

public class supervisorDTO {

    private int idSupervisor;
    private String nombreCompleto;
    private String turno;
    private String claveAcceso;


    public supervisorDTO() {
    }


    public supervisorDTO(int idSupervisor, String nombreCompleto, String turno, String claveAcceso) {
        this.idSupervisor = idSupervisor;
        this.nombreCompleto = nombreCompleto;
        this.turno = turno;
        this.claveAcceso = claveAcceso;
    }


    public int getIdSupervisor() { return idSupervisor; }
    public void setIdSupervisor(int idSupervisor) { this.idSupervisor = idSupervisor; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }

    public String getClaveAcceso() { return claveAcceso; }
    public void setClaveAcceso(String claveAcceso) { this.claveAcceso = claveAcceso; }

    @Override
    public String toString() {
        return nombreCompleto;
    }
}