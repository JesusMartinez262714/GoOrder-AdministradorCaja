package AdministradorCajaPersistencia.Entitys;

import AdministradorCajaDTOs.desgloseDTO;

import java.util.Date;
import java.util.List;

public class corteCaja {
    private int id;
    private int idApertura;
    private Date fecha;
    private double totalEsperadoSistema;
    private double totalRealDeclarado;
    private double diferencia;
    private String estado;
    private int idCajero;
    private int idSupervisor;
    private String cajero;
    private List<desgloseDTO> listaDesglose;

    public corteCaja() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdApertura() {
        return idApertura;
    }

    public void setIdApertura(int idApertura) {
        this.idApertura = idApertura;
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

    public void setIdSupervisor(int idSupervisor) {
        this.idSupervisor = idSupervisor;
    }

    public String getCajero() {
        return cajero;
    }

    public void setCajero(String cajero) {
        this.cajero = cajero;
    }

    public List<desgloseDTO> getListaDesglose() {
        return listaDesglose;
    }

    public void setListaDesglose(List<desgloseDTO> listaDesglose) {
        this.listaDesglose = listaDesglose;
    }
}