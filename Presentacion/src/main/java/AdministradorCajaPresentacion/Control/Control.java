package AdministradorCajaPresentacion.Control;

import AdministradorCajaDTOs.*;
import AdministradorCajaPresentacion.GUI.*;
import Interfaces.INegocioCorte;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Control {

    private final INegocioCorte fachadaNegocio;
    private ResumenTurno pantallaResumen;
    private HistorialCortes pantallaHistorial;
    private FormularioCorte pantallaFormulario;
    private AperturaCaja pantallaApertura;
    private GestionCajeros pantallaGestionCajeros;
    private GestionSupervisores pantallaSupervisores;

    private List<cajeroDTO> cajerosConTurnoAbierto = new ArrayList<>();
    private Map<Integer, Integer> mapaAsignacionIds = new HashMap<>();
    private String nombreSupervisorActivo = "---";

    public Control(INegocioCorte fachadaNegocio) {
        this.fachadaNegocio = fachadaNegocio;
    }

    public void iniciarFlujoResumen() {
        this.cajerosConTurnoAbierto.clear();
        this.mapaAsignacionIds.clear();
        this.nombreSupervisorActivo = "---";

        this.cajerosConTurnoAbierto = fachadaNegocio.consultarCajerosConTurnoActivo();

        for (cajeroDTO c : cajerosConTurnoAbierto) {
            Integer idSup = fachadaNegocio.obtenerIdSupervisorDeAperturaActiva(c.getIdCajero());
            if (idSup != null && idSup > 0) {
                mapaAsignacionIds.put(c.getIdCajero(), idSup);
            }
        }

        if (pantallaResumen == null) {
            pantallaResumen = new ResumenTurno(this);
            pantallaResumen.setCajeroChangeListener(e -> {
                if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                    actualizarDatosResumen((cajeroDTO) e.getItem());
                }
            });
        }

        ocultarTodas();

        if (cajerosConTurnoAbierto.isEmpty()) {
            pantallaResumen.mostrarEstadoSinSesion();
        } else {
            mostrarResumenTurno(cajerosConTurnoAbierto.get(0).getIdCajero());
        }

        pantallaResumen.setVisible(true);
    }

    public void volverAResumen() {
        if (cajerosConTurnoAbierto.isEmpty()) {
            iniciarFlujoResumen();
        } else {
            mostrarResumenTurno(cajerosConTurnoAbierto.get(0).getIdCajero());
        }
    }

    public void ocultarTodas() {
        if (pantallaResumen != null) pantallaResumen.setVisible(false);
        if (pantallaHistorial != null) pantallaHistorial.setVisible(false);
        if (pantallaFormulario != null) pantallaFormulario.setVisible(false);
        if (pantallaApertura != null) pantallaApertura.setVisible(false);
        if (pantallaGestionCajeros != null) pantallaGestionCajeros.setVisible(false);
        if (pantallaSupervisores != null) pantallaSupervisores.setVisible(false);
    }

    public void mostrarAperturaCaja() {
        if (pantallaApertura == null) pantallaApertura = new AperturaCaja(this);
        ocultarTodas();
        pantallaApertura.cargarSupervisores(fachadaNegocio.consultarSupervisores());
        pantallaApertura.cargarCajeros(fachadaNegocio.consultarCajeros());
        pantallaApertura.setVisible(true);
    }

    public boolean confirmarApertura(aperturaCajaDTO dto) {
        boolean exito = fachadaNegocio.registrarApertura(dto);
        if (exito) {
            mapaAsignacionIds.put(dto.getIdCajero(), dto.getIdSupervisor());
            this.cajerosConTurnoAbierto = fachadaNegocio.consultarCajerosConTurnoActivo();
        }
        return exito;
    }

    public String obtenerNombreSupervisorAsociado(int idCajero) {
        Integer idSup = mapaAsignacionIds.get(idCajero);
        if (idSup == null) {
            idSup = fachadaNegocio.obtenerIdSupervisorDeAperturaActiva(idCajero);
        }
        return (idSup != null && idSup > 0) ? fachadaNegocio.obtenerNombreSupervisorPorId(idSup) : "---";
    }

    public void mostrarResumenTurno(int idCajero) {
        if (pantallaResumen == null) pantallaResumen = new ResumenTurno(this);
        ocultarTodas();

        resumenVentasDTO resumen = fachadaNegocio.generarResumenVentasTurno(idCajero, new Date());
        this.nombreSupervisorActivo = obtenerNombreSupervisorAsociado(idCajero);

        pantallaResumen.cargarDatos(resumen, cajerosConTurnoAbierto, nombreSupervisorActivo, idCajero);
        pantallaResumen.setVisible(true);
    }

    private void actualizarDatosResumen(cajeroDTO seleccionado) {
        resumenVentasDTO r = fachadaNegocio.generarResumenVentasTurno(seleccionado.getIdCajero(), new Date());
        this.nombreSupervisorActivo = obtenerNombreSupervisorAsociado(seleccionado.getIdCajero());
        pantallaResumen.actualizarMontos(r, seleccionado, nombreSupervisorActivo);
    }

    public void mostrarFormularioCorte() {
        if (pantallaFormulario == null) pantallaFormulario = new FormularioCorte(this);
        else pantallaFormulario.limpiarFormulario();
        ocultarTodas();
        pantallaFormulario.cargarEmpleados(cajerosConTurnoAbierto);
        pantallaFormulario.setVisible(true);
    }

    public void editarFormularioCorte(corteCajaDTO corteSeleccionado) {
        if (pantallaFormulario == null) pantallaFormulario = new FormularioCorte(this);
        ocultarTodas();
        pantallaFormulario.cargarCorteParaEdicion(corteSeleccionado);
        pantallaFormulario.setVisible(true);
    }

    public double obtenerMontoEsperado(int id) {
        return fachadaNegocio.obtenerVentasTotalesPorCajero(id);
    }

    public void mostrarConciliacionFinal(double esp, double cont, cajeroDTO c, List<desgloseDTO> des, String img) {
        if (pantallaFormulario != null) pantallaFormulario.setVisible(false);
        new ConciliacionFinal(this, esp, cont, c, des, img).setVisible(true);
    }

    public void volverAFormulario() {
        if (pantallaFormulario != null) pantallaFormulario.setVisible(true);
    }

    public void guardarCorteFinal(double esp, double cont, int idC, List<desgloseDTO> des, String img, String nota) {
        double diff = fachadaNegocio.calcularDiferencia(esp, cont);
        corteCajaDTO dto = new corteCajaDTO();
        dto.setMontoEsperado(esp);
        dto.setMontoReal(cont);
        dto.setDiferencia(diff);
        dto.setEstado(fachadaNegocio.evaluarEstadoCorte(diff));
        dto.setIdCajero(idC);
        dto.setIdSupervisor(mapaAsignacionIds.getOrDefault(idC, 1));
        dto.setListaDesglose(des);

        if (fachadaNegocio.guardarNuevoCorte(dto, des)) {
            if (diff < 0) {
                JOptionPane.showMessageDialog(null,
                        "Corte cerrado.\nSe registró un adeudo faltante de $" + String.format("%,.2f", Math.abs(diff)) + " al cajero.",
                        "Corte con Faltante", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "¡Corte guardado exitosamente sin faltantes!");
            }

            cajerosConTurnoAbierto.removeIf(c -> c.getIdCajero() == idC);
            mapaAsignacionIds.remove(idC);
            volverAResumen();
        }
    }

    public void mostrarHistorialCortes() {
        try {
            if (pantallaHistorial == null) pantallaHistorial = new HistorialCortes(this);
            List<corteCajaDTO> lista = fachadaNegocio.consultarCortesRealizados(new Date(), new Date());
            if (lista == null) lista = new ArrayList<>();
            pantallaHistorial.llenarTabla(lista);
            ocultarTodas();
            pantallaHistorial.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al abrir historial: " + e.getMessage());
        }
    }

    public void filtrarHistorial(String criterio) {
        List<corteCajaDTO> lista = fachadaNegocio.consultarCortesRealizados(new Date(), new Date());
        if (lista == null) return;
        switch (criterio) {
            case "Ascendente" -> lista.sort((c1, c2) -> Double.compare(c1.getMontoReal(), c2.getMontoReal()));
            case "Descendente" -> lista.sort((c2, c1) -> Double.compare(c2.getMontoReal(), c1.getMontoReal()));
            case "Activos" -> lista.removeIf(c -> c.getEstado().equalsIgnoreCase("cancelado"));
            case "Cancelados" -> lista.removeIf(c -> !c.getEstado().equalsIgnoreCase("cancelado"));
        }
        if (pantallaHistorial != null) pantallaHistorial.llenarTabla(lista);
    }

    public void eliminarCorte(int idCorte) {
        if (fachadaNegocio.eliminarCorteFisico(idCorte)) {
            JOptionPane.showMessageDialog(null, "El corte ha sido eliminado exitosamente de la base de datos.");
            mostrarHistorialCortes();
        } else {
            JOptionPane.showMessageDialog(null, "Error al intentar eliminar el corte.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cancelarCorte(int idCorte) {
        if (fachadaNegocio.cancelarCorteLogico(idCorte)) {
            JOptionPane.showMessageDialog(null, "El corte ahora tiene estado 'Cancelado'.");
            mostrarHistorialCortes();
        } else {
            JOptionPane.showMessageDialog(null, "Error al intentar cancelar el corte.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void generarReportePDF(corteCajaDTO corte) {
        JOptionPane.showMessageDialog(null,
                "Generando Reporte PDF del Folio: CC-" + corte.getId() + "\n" +
                        "Cajero: " + corte.getCajero() + "\n" +
                        "Estado: " + corte.getEstado(),
                "Reporte GoOrder", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarGestionCajeros() {
        if (pantallaGestionCajeros == null) pantallaGestionCajeros = new GestionCajeros(this);
        ocultarTodas();
        filtrarCajerosLista("", "Todos", "Todos");
        pantallaGestionCajeros.setVisible(true);
    }

    public boolean registrarCajero(cajeroDTO dto) {
        if (fachadaNegocio.registrarCajero(dto)) {
            filtrarCajerosLista("", "Todos", "Todos");
            return true;
        }
        return false;
    }

    public boolean editarCajero(cajeroDTO dto) {
        if (fachadaNegocio.editarCajero(dto)) {
            filtrarCajerosLista("", "Todos", "Todos");
            return true;
        }
        return false;
    }

    public void filtrarCajerosLista(String n, String t, String d) {
        List<cajeroDTO> lista = fachadaNegocio.obtenerCajerosFiltrados(n, t, d);
        if (pantallaGestionCajeros != null) pantallaGestionCajeros.cargarCajeros(lista);
    }

    public void eliminarCajero(int id) {
        if (fachadaNegocio.eliminarCajero(id)) filtrarCajerosLista("", "Todos", "Todos");
    }

    public void procesarPagoAdeudo(int idCajero, double montoPagado) {
        if (montoPagado <= 0) {
            JOptionPane.showMessageDialog(null, "El monto ingresado debe ser mayor a cero.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<cajeroDTO> lista = fachadaNegocio.obtenerCajerosFiltrados("", "Todos", "Todos");
        cajeroDTO cajero = null;
        for (cajeroDTO c : lista) {
            if (c.getIdCajero() == idCajero) {
                cajero = c;
                break;
            }
        }

        if (cajero == null) return;

        if (montoPagado > cajero.getMontoAdeudo()) {
            JOptionPane.showMessageDialog(null, "No se puede realizar el pago: El monto ingresado supera el adeudo actual.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (fachadaNegocio.liquidarAdeudo(idCajero, montoPagado)) {
            double saldoRestante = cajero.getMontoAdeudo() - montoPagado;
            if (saldoRestante > 0) {
                JOptionPane.showMessageDialog(null, "Abono registrado con éxito. Cantidad faltante: $" + String.format("%,.2f", saldoRestante), "Pago Registrado", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "El adeudo ha sido liquidado por completo. El cajero se encuentra al corriente.", "Adeudo Liquidado", JOptionPane.INFORMATION_MESSAGE);
            }
            filtrarCajerosLista("", "Todos", "Todos");
        } else {
            JOptionPane.showMessageDialog(null, "Ocurrió un error al procesar el pago del adeudo en el sistema.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void mostrarGestionSupervisores() {
        if (pantallaSupervisores == null) pantallaSupervisores = new GestionSupervisores(this);
        ocultarTodas();
        filtrarSupervisoresLista("");
        pantallaSupervisores.setVisible(true);
    }

    public boolean registrarSupervisor(supervisorDTO dto) {
        if (fachadaNegocio.registrarSupervisor(dto)) {
            filtrarSupervisoresLista("");
            return true;
        }
        return false;
    }

    public boolean editarSupervisor(supervisorDTO dto) {
        if (fachadaNegocio.editarSupervisor(dto)) {
            filtrarSupervisoresLista("");
            return true;
        }
        return false;
    }

    public void filtrarSupervisoresLista(String nombre) {
        List<supervisorDTO> lista = fachadaNegocio.obtenerSupervisoresFiltrados(nombre);
        if (pantallaSupervisores != null) pantallaSupervisores.cargarSupervisores(lista);
    }

    public void eliminarSupervisor(int id) {
        if (fachadaNegocio.eliminarSupervisor(id)) filtrarSupervisoresLista("");
    }
}