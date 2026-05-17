package AdministradorCajaNegocio.BOs;

import AdministradorCajaNegocio.Interfaces.IAdeudoBO;
import AdministradorCajaNegocio.Interfaces.IAperturaBO;
import AdministradorCajaPersistencia.Interfaces.IAdeudoDAO;

public class adeudoBO implements IAdeudoBO {

    private final IAdeudoDAO adeudoDAO;

    public adeudoBO(IAdeudoDAO adeudoDAO) {
        this.adeudoDAO = adeudoDAO;
    }

    public boolean registrarAdeudoFaltante(int idCajero, double montoFaltante) {
        if (montoFaltante <= 0) {
            return false;
        }
        double adeudoActual = adeudoDAO.consultarAdeudoPorCajero(idCajero);
        return adeudoDAO.actualizarMontoAdeudo(idCajero, adeudoActual + montoFaltante);
    }

    public boolean liquidarAdeudo(int idCajero, double montoPagado) {
        double adeudoActual = adeudoDAO.consultarAdeudoPorCajero(idCajero);

        if (montoPagado <= 0 || montoPagado > adeudoActual) {
            return false;
        }

        double nuevoMonto = adeudoActual - montoPagado;
        return adeudoDAO.actualizarMontoAdeudo(idCajero, nuevoMonto);
    }

    public double consultarAdeudoPorCajero(int idCajero) {
        return adeudoDAO.consultarAdeudoPorCajero(idCajero);
    }
}