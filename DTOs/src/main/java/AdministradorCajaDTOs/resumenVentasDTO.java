package AdministradorCajaDTOs;

public class resumenVentasDTO {

    private double totalEfectivo;
    private double totalTarjeta;
    private double totalApp;
    private double totalReferencia;
    private double totalEsperadoSistema;

    public resumenVentasDTO() {
    }

    public resumenVentasDTO(double totalEfectivo, double totalTarjeta, double totalApp, double totalReferencia, double totalEsperadoSistema) {
        this.totalEfectivo = totalEfectivo;
        this.totalTarjeta = totalTarjeta;
        this.totalApp = totalApp;
        this.totalReferencia = totalReferencia;
        this.totalEsperadoSistema = totalEsperadoSistema;
    }

    public double getTotalEfectivo() {
        return totalEfectivo;
    }

    public void setTotalEfectivo(double totalEfectivo) {
        this.totalEfectivo = totalEfectivo;
    }

    public double getTotalTarjeta() {
        return totalTarjeta;
    }

    public void setTotalTarjeta(double totalTarjeta) {
        this.totalTarjeta = totalTarjeta;
    }

    public double getTotalApp() {
        return totalApp;
    }

    public void setTotalApp(double totalApp) {
        this.totalApp = totalApp;
    }

    public double getTotalReferencia() {
        return totalReferencia;
    }

    public void setTotalReferencia(double totalReferencia) {
        this.totalReferencia = totalReferencia;
    }

    public double getTotalEsperadoSistema() {
        return totalEsperadoSistema;
    }

    public void setTotalEsperadoSistema(double totalEsperadoSistema) {
        this.totalEsperadoSistema = totalEsperadoSistema;
    }
}