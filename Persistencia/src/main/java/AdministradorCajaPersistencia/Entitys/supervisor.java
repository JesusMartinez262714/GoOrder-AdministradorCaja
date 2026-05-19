package AdministradorCajaPersistencia.Entitys;

/**
 * Clase que representa la entidad de un supervisor en el sistema.
 * Almacena los datos básicos de identificación, nombre y apellido de la autoridad
 * encargada de autorizar las aperturas y los cierres de caja.
 * * @author Jesus Manuel Martinez Cortez
 */
public class supervisor {
    private int idSupervisor;
    private String nombre;
    private String apellido;

    /**
     * Constructor vacío por defecto para la entidad de supervisor.
     */
    public supervisor() {}

    /**
     * Constructor que inicializa los datos obligatorios del supervisor
     * al momento de registrarlo en el sistema.
     * * @param idSupervisor El identificador único del supervisor.
     * @param nombre El nombre o nombres del supervisor.
     * @param apellido Los apellidos del supervisor.
     */
    public supervisor(int idSupervisor, String nombre, String apellido) {
        this.idSupervisor = idSupervisor;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    /**
     * Une el nombre y el apellido del supervisor para devolverlo en una sola cadena.
     * * @return El nombre completo del supervisor.
     */
    public String getNombreCompleto() {
        return (nombre != null ? nombre : "") + " " + (apellido != null ? apellido : "");
    }

    /**
     * Devuelve el ID único del supervisor.
     * * @return El identificador numérico del supervisor.
     */
    public int getIdSupervisor() {
        return idSupervisor;
    }

    /**
     * Asigna el ID único del supervisor.
     * * @param idSupervisor El identificador numérico del supervisor.
     */
    public void setIdSupervisor(int idSupervisor) {
        this.idSupervisor = idSupervisor;
    }

    /**
     * Devuelve el nombre o nombres del supervisor.
     * * @return Cadena de texto con el nombre.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna el nombre o nombres del supervisor.
     * * @param nombre Cadena de texto con el nombre.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve los apellidos del supervisor.
     * * @return Cadena de texto con los apellidos.
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Asigna los apellidos del supervisor.
     * * @param apellido Cadena de texto con los apellidos.
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
}