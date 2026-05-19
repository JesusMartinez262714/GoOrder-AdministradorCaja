package AdministradorCajaNegocio.Interfaces;

import AdministradorCajaDTOs.corteCajaDTO;
import AdministradorCajaDTOs.desgloseDTO;
import java.util.Date;
import java.util.List;

public interface ICorteCajaBO {
    double calcularDiferencia(double totalEsperado, double totalReal);
    String evaluarEstadoCorte(double diferencia);

    boolean guardarNuevoCorte(corteCajaDTO datosCorte, List<desgloseDTO> listaDesgloses);

    List<corteCajaDTO> consultarCortesRealizados(Date inicio, Date fin);

}