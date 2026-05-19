package AdministradorCajaPersistencia.Entitys;

/**
 * Clase que representa la entidad de un desglose de montos en el sistema.
 * Guarda de forma detallada la cantidad de dinero físico que se contó y declaró
 * en la caja por cada tipo o método de pago específico.
 * * @author Jesus Manuel Martinez Cortez
 */
public class desgloseMontos {
    private double montoDeclarado;
    private int idMetodoPago;
    private String nombreMetodo;

    /**
     * Constructor vacío por defecto para la entidad de desglose de montos.
     */
    public desgloseMontos() {}

    /**
     * Devuelve la cantidad de dinero físico que se contó para este método de pago.
     * * @return El monto declarado en formato double.
     */
    public double getMontoDeclarado() {
        return montoDeclarado;
    }

    /**
     * Asigna la cantidad de dinero físico contado para este método de pago.
     * * @param monto El monto declarado en formato double.
     */
    public void setMontoDeclarado(double monto) {
        this.montoDeclarado = monto;
    }

    /**
     * Devuelve el ID único que identifica al método de pago utilizado.
     * * @return El identificador numérico del método de pago.
     */
    public int getIdMetodoPago() {
        return idMetodoPago;
    }

    /**
     * Asigna el ID único que identifica al método de pago utilizado.
     * * @param id El identificador numérico del método de pago.
     */
    public void setIdMetodoPago(int id) {
        this.idMetodoPago = id;
    }

    /**
     * Devuelve el nombre descriptivo del método de pago (por ejemplo, Efectivo o Tarjeta).
     * * @return Cadena de texto con el nombre del método de pago.
     */
    public String getNombreMetodo() {
        return nombreMetodo;
    }

    /**
     * Asigna el nombre descriptivo del método de pago.
     * * @param nombre Cadena de texto con el nombre del método de pago.
     */
    public void setNombreMetodo(String nombre) {
        this.nombreMetodo = nombre;
    }
}