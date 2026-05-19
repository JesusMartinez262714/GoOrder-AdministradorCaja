package AdministradorCajaPersistencia.Entitys;

import java.util.Date;
import java.util.List;

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

    public corteCaja() {}

    public int getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(int idCaja) {
        this.idCaja = idCaja;
    }

    public Date getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(Date fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getTotalEsperadoSistema() {
        return totalEsperadoSistema;
    }

    public void setTotalEsperadoSistema(double totalEsperadoSistema) {
        this.totalEsperadoSistema = totalEsperadoSistema;
    }

    public double getTotalRealDeclarado() {
        return totalRealDeclarado;
    }

    public void setTotalRealDeclarado(double totalRealDeclarado) {
        this.totalRealDeclarado = totalRealDeclarado;
    }

    public double getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(double diferencia) {
        this.diferencia = diferencia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getIdCajero() {
        return idCajero;
    }

    public void setIdCajero(int idCajero) {
        this.idCajero = idCajero;
    }

    public int getIdSupervisor() {
        return idSupervisor;
    }
    public String getMotivoCancelacion() { return motivoCancelacion; }
    public void setMotivoCancelacion(String motivoCancelacion) { this.motivoCancelacion = motivoCancelacion; }

    public void setIdSupervisor(int idSupervisor) {
        this.idSupervisor = idSupervisor;
    }

    public String getCajero() {
        return cajero;
    }

    public void setCajero(String cajero) {
        this.cajero = cajero;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<desgloseMontos> getListaDesglose() {
        return listaDesglose;
    }

    public void setListaDesglose(List<desgloseMontos> listaDesglose) {
        this.listaDesglose = listaDesglose;
    }
}