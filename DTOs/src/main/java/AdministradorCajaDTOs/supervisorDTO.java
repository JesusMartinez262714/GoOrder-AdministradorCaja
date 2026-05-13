package AdministradorCajaDTOs;

public class supervisorDTO {
    private int idSupervisor;
    private String nombreCompleto;
    private String turno;


    public supervisorDTO(int idSupervisor, String nombreCompleto, String turno) {
        this.idSupervisor = idSupervisor;
        this.nombreCompleto = nombreCompleto;
        this.turno = turno;

    }

    public supervisorDTO() {
    }

    public int getIdSupervisor() {
        return idSupervisor;
    }

    public void setIdSupervisor(int idSupervisor) {
        this.idSupervisor = idSupervisor;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    @Override
    public String toString() {
        return nombreCompleto; // Para que el JComboBox muestre el nombre
    }
}