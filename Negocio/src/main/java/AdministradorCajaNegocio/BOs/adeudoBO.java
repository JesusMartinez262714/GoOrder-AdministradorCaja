package AdministradorCajaNegocio.BOs;

import AdministradorCajaNegocio.Interfaces.IAdeudoBO;
import AdministradorCajaPersistencia.Interfaces.IAdeudoDAO;

/**
 * Clase de lógica de negocio encargada de gestionar los adeudos de los cajeros.
 * Se encarga de aplicar las reglas de validación para registrar nuevos montos faltantes
 * y procesar los pagos o abonos correspondientes de forma segura.
 * * @author Jesus Manuel Martinez Cortez
 */
public class adeudoBO implements IAdeudoBO {

    private final IAdeudoDAO adeudoDAO;

    /**
     * Constructor que recibe el DAO de adeudos para conectar la capa de negocio
     * con la persistencia de la base de datos.
     * * @param adeudoDAO Instancia del objeto de acceso a datos para adeudos.
     */
    public adeudoBO(IAdeudoDAO adeudoDAO) {
        this.adeudoDAO = adeudoDAO;
    }

    /**
     * Registra un nuevo monto faltante sumándolo directamente al saldo deudor
     * actual que tiene registrado el cajero.
     * * @param idCajero ID único del cajero que generó el faltante.
     * @param montoFaltante Cantidad de dinero que hizo falta en el arqueo de caja.
     * @return true si el adeudo se actualizó en la base de datos, false si el monto es menor o igual a cero.
     */
    @Override
    public boolean registrarAdeudoFaltante(int idCajero, double montoFaltante) {
        if (montoFaltante <= 0) {
            return false;
        }
        double adeudoActual = adeudoDAO.consultarAdeudoPorCajero(idCajero);
        return adeudoDAO.actualizarMontoAdeudo(idCajero, adeudoActual + montoFaltante);
    }

    /**
     * Registra un pago o abono a la cuenta del cajero, restándolo de su deuda actual.
     * Valida que el abono sea mayor a cero y que no intente pagar más dinero del que debe.
     * * @param idCajero ID único del cajero que realiza el pago.
     * @param montoPagado Cantidad de dinero que se va a abonar.
     * @return true si el cambio se guardó en la base de datos, false si los montos son inválidos.
     */
    @Override
    public boolean liquidarAdeudo(int idCajero, double montoPagado) {
        double adeudoActual = adeudoDAO.consultarAdeudoPorCajero(idCajero);

        if (montoPagado <= 0 || montoPagado > adeudoActual) {
            return false;
        }

        double nuevoMonto = adeudoActual - montoPagado;
        return adeudoDAO.actualizarMontoAdeudo(idCajero, nuevoMonto);
    }
}