package AdministradorCajaPersistencia.Entitys;

/**
 * Clase que representa la entidad de un adeudo en el sistema.
 * Almacena la información del cajero que tiene la deuda, el monto pendiente
 * y el estado actual del pago.
 * * @author Jesus Manuel Martinez Cortez
 */
public class adeudo {
    private int idCajero;
    private double monto;
    private String estado;

    /**
     * Constructor vacío por defecto para la entidad de adeudo.
     */
    public adeudo() {}

    /**
     * Devuelve el ID del cajero asociado a la deuda.
     * * @return El identificador único del cajero.
     */
    public int getIdCajero() {
        return idCajero;
    }

    /**
     * Asigna el ID del cajero que generó la deuda.
     * * @param idCajero El identificador único del cajero.
     */
    public void setIdCajero(int idCajero) {
        this.idCajero = idCajero;
    }

    /**
     * Devuelve el monto total de la deuda o faltante.
     * * @return El dinero pendiente en formato double.
     */
    public double getMonto() {
        return monto;
    }

    /**
     * Asigna el monto total de la deuda o faltante.
     * * @param monto El dinero pendiente en formato double.
     */
    public void setMonto(double monto) {
        this.monto = monto;
    }

    /**
     * Devuelve el estado actual de la deuda (por ejemplo, PENDIENTE o PAGADO).
     * * @return Cadena de texto con el estado del adeudo.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Asigna el estado actual de la deuda según el pago realizado.
     * * @param estado Cadena de texto con el estado del adeudo.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
}