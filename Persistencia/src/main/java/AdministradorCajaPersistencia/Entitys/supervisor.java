package AdministradorCajaPersistencia.Entitys;

public class supervisor {
    private int idSupervisor;
    private String nombre;
    private String apellido;

    public supervisor() {}

    public supervisor(int idSupervisor, String nombre, String apellido) {
        this.idSupervisor = idSupervisor;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    public int getIdSupervisor() { return idSupervisor; }
    public void setIdSupervisor(int idSupervisor) { this.idSupervisor = idSupervisor; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
}