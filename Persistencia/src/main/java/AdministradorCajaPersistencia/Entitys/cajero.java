package AdministradorCajaPersistencia.Entitys;

/**
 * Clase que representa la entidad de un cajero en el sistema.
 * Almacena los datos básicos del empleado como su nombre, apellido,
 * el turno en el que trabaja y el saldo de sus adeudos.
 * * @author Jesus Manuel Martinez Cortez
 */
public class cajero {
    private int idCajero;
    private String nombre;
    private String apellido;
    private String turno;
    private double montoAdeudo;

    /**
     * Constructor vacío por defecto para la entidad cajero.
     */
    public cajero() {}

    /**
     * Constructor que inicializa los datos obligatorios del cajero
     * al momento de registrarlo en el sistema.
     * * @param idCajero El identificador único del cajero.
     * @param nombre El nombre o nombres del cajero.
     * @param apellido Los apellidos del cajero.
     * @param turno El turno asignado (por ejemplo, Matutino o Vespertino).
     */
    public cajero(int idCajero, String nombre, String apellido, String turno) {
        this.idCajero = idCajero;
        this.nombre = nombre;
        this.apellido = apellido;
        this.turno = turno;
    }

    /**
     * Une el nombre y el apellido del cajero para devolverlo en una sola cadena.
     * * @return El nombre completo del cajero.
     */
    public String getNombreCompleto() {
        return (nombre != null ? nombre : "") + " " + (apellido != null ? apellido : "");
    }

    /**
     * Devuelve el ID único del cajero.
     * * @return El identificador numérico del cajero.
     */
    public int getIdCajero() {
        return idCajero;
    }

    /**
     * Asigna el ID único del cajero.
     * * @param idCajero El identificador numérico del cajero.
     */
    public void setIdCajero(int idCajero) {
        this.idCajero = idCajero;
    }

    /**
     * Devuelve el nombre o nombres del cajero.
     * * @return Cadena de texto con el nombre.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna el nombre o nombres del cajero.
     * * @param nombre Cadena de texto con el nombre.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve los apellidos del cajero.
     * * @return Cadena de texto con los apellidos.
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Asigna los apellidos del cajero.
     * * @param apellido Cadena de texto con los apellidos.
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * Devuelve el turno asignado al cajero.
     * * @return El turno de trabajo del empleado.
     */
    public String getTurno() {
        return turno;
    }

    /**
     * Asigna el turno de trabajo del cajero.
     * * @param turno El turno de trabajo del empleado.
     */
    public void setTurno(String turno) {
        this.turno = turno;
    }

    /**
     * Devuelve el monto total de adeudo que tiene el cajero.
     * * @return Cantidad de dinero pendiente en formato double.
     */
    public double getMontoAdeudo() {
        return montoAdeudo;
    }

    /**
     * Asigna el monto total de adeudo que tiene el cajero.
     * * @param montoAdeudo Cantidad de dinero pendiente en formato double.
     */
    public void setMontoAdeudo(double montoAdeudo) {
        this.montoAdeudo = montoAdeudo;
    }
}