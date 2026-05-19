package AdministradorCajaPersistencia.Entitys;

public class cajero {
    private int idCajero;
    private String nombre;
    private String apellido;
    private String turno;
    private double montoAdeudo;

    public cajero() {}

    public cajero(int idCajero, String nombre, String apellido, String turno) {
        this.idCajero = idCajero;
        this.nombre = nombre;
        this.apellido = apellido;
        this.turno = turno;
    }

    public String getNombreCompleto() {
        return (nombre != null ? nombre : "") + " " + (apellido != null ? apellido : "");
    }

    public int getIdCajero() {
        return idCajero;
    }

    public void setIdCajero(int idCajero) {
        this.idCajero = idCajero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public double getMontoAdeudo() {
        return montoAdeudo;
    }

    public void setMontoAdeudo(double montoAdeudo) {
        this.montoAdeudo = montoAdeudo;
    }
}