package AdministradorCajaDTOs;

import java.util.Date;
import java.util.List;

public class corteCajaDTO {

    private int idCorte;
    private Date fechaHora;
    private double totalEsperadoSistema;
    private double totalRealDeclarado;
    private double diferencia;
    private String estado;
    private int idCajero;
    private int idSupervisor;
    private List<desgloseDTO> listaDesglose;


    public corteCajaDTO() {
    }

    public int getIdCorte() { return idCorte; }
    public void setIdCorte(int idCorte) { this.idCorte = idCorte; }

    public Date getFechaHora() { return fechaHora; }
    public void setFechaHora(Date fechaHora) { this.fechaHora = fechaHora; }

    public double getTotalEsperadoSistema() { return totalEsperadoSistema; }
    public void setTotalEsperadoSistema(double totalEsperadoSistema) { this.totalEsperadoSistema = totalEsperadoSistema; }

    public double getTotalRealDeclarado() { return totalRealDeclarado; }
    public void setTotalRealDeclarado(double totalRealDeclarado) { this.totalRealDeclarado = totalRealDeclarado; }

    public double getDiferencia() { return diferencia; }
    public void setDiferencia(double diferencia) { this.diferencia = diferencia; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public int getIdCajero() { return idCajero; }
    public void setIdCajero(int idCajero) { this.idCajero = idCajero; }

    public int getIdSupervisor() { return idSupervisor; }
    public void setIdSupervisor(int idSupervisor) { this.idSupervisor = idSupervisor; }

    public List<desgloseDTO> getListaDesglose() { return listaDesglose; }
    public void setListaDesglose(List<desgloseDTO> listaDesglose) { this.listaDesglose = listaDesglose; }
}