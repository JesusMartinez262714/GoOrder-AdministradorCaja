package AdministradorCajaDTOs;

import java.util.Date;
import java.util.List; // Importante

public class corteCajaDTO {
    private int id;
    private Date fecha;
    private String cajero;
    private double montoEsperado;
    private double montoReal;
    private double diferencia;
    private String notas;
    private List<desgloseDTO> listaDesglose;
    private String rutaComprobante;

    public corteCajaDTO(int id, Date fecha, String cajero, double montoEsperado, double montoReal, String notas, List<desgloseDTO> listaDesglose) {
        this.id = id;
        this.fecha = fecha;
        this.cajero = cajero;
        this.montoEsperado = montoEsperado;
        this.montoReal = montoReal;
        this.diferencia = montoReal - montoEsperado;
        this.notas = notas;
        this.listaDesglose = listaDesglose;
    }

    public corteCajaDTO(int id, Date fecha, String cajero, double montoEsperado, double montoReal, double diferencia, String notas, List<desgloseDTO> listaDesglose, String rutaComprobante) {
        this.id = id;
        this.fecha = fecha;
        this.cajero = cajero;
        this.montoEsperado = montoEsperado;
        this.montoReal = montoReal;
        this.diferencia = diferencia;
        this.notas = notas;
        this.listaDesglose = listaDesglose;
        this.rutaComprobante = rutaComprobante;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getCajero() {
        return cajero;
    }

    public void setCajero(String cajero) {
        this.cajero = cajero;
    }

    public double getMontoEsperado() {
        return montoEsperado;
    }

    public void setMontoEsperado(double montoEsperado) {
        this.montoEsperado = montoEsperado;
    }

    public double getMontoReal() {
        return montoReal;
    }

    public void setMontoReal(double montoReal) {
        this.montoReal = montoReal;
    }

    public double getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(double diferencia) {
        this.diferencia = diferencia;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public List<desgloseDTO> getListaDesglose() {
        return listaDesglose;
    }

    public void setListaDesglose(List<desgloseDTO> listaDesglose) {
        this.listaDesglose = listaDesglose;
    }

    public String getRutaComprobante() {
        return rutaComprobante;
    }

    public void setRutaComprobante(String rutaComprobante) {
        this.rutaComprobante = rutaComprobante;
    }
}