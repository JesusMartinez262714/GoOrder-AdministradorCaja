package AdministradorCajaDTOs;

public class supervisorDTO {
    private int idSupervisor;
    private String nombreCompleto;

    public supervisorDTO(int idSupervisor, String nombreCompleto) {
        this.idSupervisor = idSupervisor;
        this.nombreCompleto = nombreCompleto;
    }

    public int getIdSupervisor() { return idSupervisor; }
    public void setIdSupervisor(int idSupervisor) { this.idSupervisor = idSupervisor; }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    @Override
    public String toString() { return nombreCompleto; }
}