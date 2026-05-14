package AdministradorCajaNegocio.Interfaces;
import AdministradorCajaDTOs.aperturaCajaDTO;

public interface IAperturaBO {
    boolean registrarFondolnicial(aperturaCajaDTO apertura);
}