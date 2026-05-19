package AdministradorCajaNegocio.Interfaces;


public interface IAdeudoBO {

    boolean registrarAdeudoFaltante(int idCajero, double montoFaltante);

    boolean liquidarAdeudo(int idCajero, double montoPagado);
}