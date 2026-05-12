package Interfaces;

import AdministradorCajaDTOs.resumenVentasDTO;
import java.util.Date;

public interface INegocioCorte {

    resumenVentasDTO generarResumenVentasTurno(int idCajero, Date fechaActual);

}