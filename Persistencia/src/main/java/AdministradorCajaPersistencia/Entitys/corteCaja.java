package AdministradorCajaPersistencia.Entitys;

import java.util.Date;
import java.util.List;

/**
 * Clase que representa la entidad de un corte de caja en el sistema.
 * Almacena los datos del arqueo de un turno, incluyendo las fechas de operación,
 * los montos del sistema, lo declarado por el cajero y el estado del corte.
 * * @author Jesus Manuel Martinez Cortez
 */
public class corteCaja {
    private int idCaja;
    private Date fechaApertura;
    private Date fecha;
    private double totalEsperadoSistema;
    private double totalRealDeclarado;
    private double diferencia;
    private String estado;
    private int idCajero;
    private int idSupervisor;
    private String cajero;
    private String observaciones;
    private List<desgloseMontos> listaDesglose;
    private String motivoCancelacion;

    /**
     * Constructor vacío por defecto para la entidad de corte de caja.
     */
    public corteCaja() {}

    /**
     * Devuelve el ID único del corte de caja.
     * * @return El identificador del corte.
     */
    public int getIdCaja() {
        return idCaja;
    }

    /**
     * Asigna el ID único del corte de caja.
     * * @param idCaja El identificador del corte.
     */
    public void setIdCaja(int idCaja) {
        this.idCaja = idCaja;
    }

    /**
     * Devuelve la fecha y hora en la que se abrió el turno de la caja.
     * * @return Objeto Date con la fecha de apertura.
     */
    public Date getFechaApertura() {
        return fechaApertura;
    }

    /**
     * Asigna la fecha y hora en la que se abrió el turno de la caja.
     * * @param fechaApertura Objeto Date con la fecha de apertura.
     */
    public void setFechaApertura(Date fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    /**
     * Devuelve la fecha y hora en la que se realizó el cierre o corte de caja.
     * * @return Objeto Date con la fecha del corte.
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * Asigna la fecha y hora en la que se realizó el cierre o corte de caja.
     * * @param fecha Objeto Date con la fecha del corte.
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * Devuelve el dinero total que el sistema calcula que debería haber en caja.
     * * @return El monto esperado en formato double.
     */
    public double getTotalEsperadoSistema() {
        return totalEsperadoSistema;
    }

    /**
     * Asigna el dinero total que el sistema calcula que debería haber en caja.
     * * @param totalEsperadoSistema El monto esperado en formato double.
     */
    public void setTotalEsperadoSistema(double totalEsperadoSistema) {
        this.totalEsperadoSistema = totalEsperadoSistema;
    }

    /**
     * Devuelve el dinero total físico que el usuario contó y declaró en el desglose.
     * * @return El monto real declarado en formato double.
     */
    public double getTotalRealDeclarado() {
        return totalRealDeclarado;
    }

    /**
     * Asigna el dinero total físico que el usuario contó y declaró en el desglose.
     * * @param totalRealDeclarado El monto real declarado en formato double.
     */
    public void setTotalRealDeclarado(double totalRealDeclarado) {
        this.totalRealDeclarado = totalRealDeclarado;
    }

    /**
     * Devuelve la diferencia matemática entre el monto esperado y el declarado.
     * * @return El valor de la diferencia (negativo si hace falta dinero).
     */
    public double getDiferencia() {
        return diferencia;
    }

    /**
     * Asigna la diferencia matemática entre el monto esperado y el declarado.
     * * @param diferencia El valor de la diferencia calculado.
     */
    public void setDiferencia(double diferencia) {
        this.diferencia = diferencia;
    }

    /**
     * Devuelve el estado del corte de caja (por ejemplo, CERRADA o Cancelado).
     * * @return Cadena de texto con el estado actual.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Asigna el estado del corte de caja según el resultado del arqueo.
     * * @param estado Cadena de texto con el estado actual.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Devuelve el ID único del cajero que operó la caja en el turno.
     * * @return El identificador numérico del cajero.
     */
    public int getIdCajero() {
        return idCajero;
    }

    /**
     * Asigna el ID único del cajero que operó la caja en el turno.
     * * @param idCajero El identificador numérico del cajero.
     */
    public void setIdCajero(int idCajero) {
        this.idCajero = idCajero;
    }

    /**
     * Devuelve el motivo por el cual el corte fue cancelado en el sistema.
     * * @return Cadena de texto con la justificación de la cancelación.
     */
    public String getMotivoCancelacion() {
        return motivoCancelacion;
    }

    /**
     * Asigna el motivo por el cual el corte fue cancelado en el sistema.
     * * @param motivoCancelacion Cadena de texto con la justificación de la cancelación.
     */
    public void setMotivoCancelacion(String motivoCancelacion) {
        this.motivoCancelacion = motivoCancelacion;
    }

    /**
     * Devuelve el ID del supervisor que autorizó o revisó el corte de caja.
     * * @return El identificador numérico del supervisor.
     */
    public int getIdSupervisor() {
        return idSupervisor;
    }

    /**
     * Asigna el ID del supervisor que autorizó o revisó el corte de caja.
     * * @param idSupervisor El identificador numérico del supervisor.
     */
    public void setIdSupervisor(int idSupervisor) {
        this.idSupervisor = idSupervisor;
    }

    /**
     * Devuelve el nombre descriptivo del cajero asociado al corte.
     * * @return Cadena de texto con el nombre del cajero.
     */
    public String getCajero() {
        return cajero;
    }

    /**
     * Asigna el nombre descriptivo del cajero asociado al corte.
     * * @param cajero Cadena de texto con el nombre del cajero.
     */
    public void setCajero(String cajero) {
        this.cajero = cajero;
    }

    /**
     * Devuelve las observaciones o la justificación obligatoria ingresada en la conciliación.
     * * @return Cadena de texto con los comentarios del corte.
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     * Asigna las observaciones o la justificación obligatoria ingresada en la conciliación.
     * * @param observaciones Cadena de texto con los comentarios del corte.
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    /**
     * Devuelve la lista detallada de los métodos de pago desglosados en el arqueo.
     * * @return Colección List con los desgloses de montos declarados.
     */
    public List<desgloseMontos> getListaDesglose() {
        return listaDesglose;
    }

    /**
     * Asigna la lista detallada de los métodos de pago desglosados en el arqueo.
     * * @param listaDesglose Colección List con los desgloses de montos declarados.
     */
    public void setListaDesglose(List<desgloseMontos> listaDesglose) {
        this.listaDesglose = listaDesglose;
    }
}