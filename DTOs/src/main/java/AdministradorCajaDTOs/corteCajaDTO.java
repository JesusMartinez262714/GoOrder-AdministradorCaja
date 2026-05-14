package AdministradorCajaDTOs;

import java.util.Date;
import java.util.List;

public class corteCajaDTO {
    private int id;
    private int idApertura;
    private Date fechaApertura;
    private Date fecha;
    private double totalEsperadoSistema;
    private double totalRealDeclarado;
    private double diferencia;
    private String estado;
    private int idCajero;
    private int idSupervisor;
    private String cajero;
    private List<desgloseDTO> listaDesglose;

    public corteCajaDTO() {}

    public int getIdApertura() { return idApertura; }
    public void setIdApertura(int idApertura) { this.idApertura = idApertura; }

    public double getTotalEsperadoSistema() { return totalEsperadoSistema; }
    public void setTotalEsperadoSistema(double d) { this.totalEsperadoSistema = d; }

    public double getTotalRealDeclarado() { return totalRealDeclarado; }
    public void setTotalRealDeclarado(double d) { this.totalRealDeclarado = d; }

    public void setMontoEsperado(double d) { this.totalEsperadoSistema = d; }
    public double getMontoEsperado() { return totalEsperadoSistema; }

    public void setMontoReal(double d) { this.totalRealDeclarado = d; }
    public double getMontoReal() { return totalRealDeclarado; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public double getDiferencia() { return diferencia; }
    public void setDiferencia(double d) { this.diferencia = d; }

    public String getEstado() { return estado; }
    public void setEstado(String s) { this.estado = s; }

    public String getCajero() { return cajero; }
    public void setCajero(String s) { this.cajero = s; }

    public int getIdCajero() { return idCajero; }
    public void setIdCajero(int i) { this.idCajero = i; }

    public int getIdSupervisor() { return idSupervisor; }
    public void setIdSupervisor(int i) { this.idSupervisor = i; }

    public List<desgloseDTO> getListaDesglose() { return listaDesglose; }
    public void setListaDesglose(List<desgloseDTO> l) { this.listaDesglose = l; }

    public Date getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(Date fechaApertura) {
        this.fechaApertura = fechaApertura;
    }
}