package AdministradorCajaPersistencia.Interfaces;

import AdministradorCajaPersistencia.Entitys.aperturaCaja;

import java.util.Date;
import java.util.List;

public interface IAperturaCajaDAO {
    boolean insertarApertura(aperturaCaja apertura);

    aperturaCaja consultarUltimaApertura(int idCajero);

    List<Integer> obtenerIdsCajerosConCajaAbierta();

    boolean tieneAperturaActiva(int idCajero);
    void vincularCorteAApertura(int idApertura, int idCorte);

    Date obtenerFechaAperturaPorId(int idApertura);
}