package AdministradorCajaDTOs;

public class cajeroDTO {
    public int idCajero;
    public String nombreCompleto;
    public String turno;

    public cajeroDTO(int id, String nombre, String turno) {
        this.idCajero = id;
        this.nombreCompleto = nombre;
        this.turno = turno;
    }

    public int getIdCajero() {
        return idCajero;
    }

    public void setIdCajero(int idCajero) {
        this.idCajero = idCajero;
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
    public String toString() { return nombreCompleto; }
}