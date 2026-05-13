package AdministradorCajaPresentacion.Control;

import AdministradorCajaDTOs.*;
import AdministradorCajaPresentacion.GUI.*;
import Interfaces.INegocioCorte;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Control {

    private INegocioCorte fachadaNegocio;
    private ResumenTurno pantallaResumen;
    private HistorialCortes pantallaHistorial;
    private FormularioCorte pantallaFormulario;
    private AperturaCaja pantallaApertura;
    private List<cajeroDTO> cajerosConTurnoAbierto = new ArrayList<>();
    private String nombreSupervisorActivo = "---";

    public Control(INegocioCorte fachadaNegocio) {
        this.fachadaNegocio = fachadaNegocio;
    }

    public void iniciarFlujoResumen() {
        if (pantallaResumen == null) {
            pantallaResumen = new ResumenTurno(this);


            pantallaResumen.setCajeroChangeListener(e -> {
                if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                    cajeroDTO seleccionado = (cajeroDTO) e.getItem();
                    resumenVentasDTO nuevoResumen = fachadaNegocio.generarResumenVentasTurno(seleccionado.getIdCajero(), new Date());
                    String nuevoSupervisor = obtenerNombreSupervisorAsociado(seleccionado.getIdCajero());
                    nombreSupervisorActivo = nuevoSupervisor;
                    pantallaResumen.actualizarMontos(nuevoResumen, seleccionado, nuevoSupervisor);
                }
            });
        }


        pantallaResumen.mostrarEstadoSinSesion();

        this.nombreSupervisorActivo = "---";

        ocultarTodas();
        pantallaResumen.setVisible(true);
    }

    public void mostrarResumenTurno(int idCajero, String nombreSupervisor) {
        this.nombreSupervisorActivo = nombreSupervisor;
        mostrarResumenTurno(idCajero);
    }

    public void mostrarResumenTurno(int idCajero) {
        if (pantallaResumen == null) {
            pantallaResumen = new ResumenTurno(this);
        }

        ocultarTodas();

        resumenVentasDTO resumen = fachadaNegocio.generarResumenVentasTurno(idCajero, new Date());

        // (BD):en lugar de usar 'cajerosConTurnoAbierto',creare un método en el DAO:
        pantallaResumen.cargarDatos(resumen, cajerosConTurnoAbierto, nombreSupervisorActivo, idCajero);

        pantallaResumen.setVisible(true);
    }


    public void mostrarHistorialCortes() {
        if (pantallaHistorial == null) {
            pantallaHistorial = new HistorialCortes(this);
        }

        ocultarTodas();

        List<corteCajaDTO> lista = fachadaNegocio.consultarCortesRealizados(new Date(), new Date());
        pantallaHistorial.llenarTabla(lista);

        pantallaHistorial.setVisible(true);
    }



    public void filtrarHistorial(String criterio) {
        try {
            java.util.Date hoy = new java.util.Date();
            List<corteCajaDTO> listaOriginal = fachadaNegocio.consultarCortesRealizados(hoy, hoy);

            if (listaOriginal == null || listaOriginal.isEmpty()) {
                System.out.println("No hay datos para filtrar.");
                return;
            }

            List<corteCajaDTO> listaAMostrar = new ArrayList<>(listaOriginal);

            switch (criterio) {
                case "Recientes":
                    listaAMostrar.sort((c1, c2) -> c2.getFecha().compareTo(c1.getFecha()));
                    break;
                case "Antiguos":
                    listaAMostrar.sort((c1, c2) -> c1.getFecha().compareTo(c2.getFecha()));
                    break;
                case "Ascendente":
                    listaAMostrar.sort((c1, c2) -> Double.compare(c1.getMontoReal(), c2.getMontoReal()));
                    break;
                case "Descendente":
                    listaAMostrar.sort((c1, c2) -> Double.compare(c2.getMontoReal(), c1.getMontoReal()));
                    break;
                case "Activos":
                    listaAMostrar.removeIf(c -> c.getDiferencia() != 0);
                    break;
                case "Cancelados":
                    listaAMostrar.removeIf(c -> c.getDiferencia() == 0);
                    break;
                default:
                    break;
            }

            pantallaHistorial.llenarTabla(listaAMostrar);

        } catch (Exception e) {
            System.err.println("Error al aplicar el filtro: " + e.getMessage());
        }
    }


    public void mostrarFormularioCorte() {
        if (pantallaFormulario == null) {
            pantallaFormulario = new FormularioCorte(this);
        } else {
            pantallaFormulario.limpiarFormulario();
        }

        ocultarTodas();


        pantallaFormulario.cargarEmpleados(cajerosConTurnoAbierto);

        pantallaFormulario.setVisible(true);
    }

    public double obtenerMontoEsperado(int idEmpleado) {
        return fachadaNegocio.obtenerVentasTotalesPorCajero(idEmpleado);
    }

    public void mostrarConciliacionFinal(double esperado, double contado, cajeroDTO emp, List<desgloseDTO> desgloses, String rutaImg) {
        if (pantallaFormulario != null) pantallaFormulario.setVisible(false);

        ConciliacionFinal pnlConciliacion = new ConciliacionFinal(this, esperado, contado, emp, desgloses, rutaImg);
        pnlConciliacion.setVisible(true);
    }

    public void volverAFormulario() {
        if (pantallaFormulario != null) pantallaFormulario.setVisible(true);
    }

    public void volverAResumen() {
        if (nombreSupervisorActivo.equals("---") || cajerosConTurnoAbierto.isEmpty()) {
            iniciarFlujoResumen();
        } else {
            int idRetorno = cajerosConTurnoAbierto.get(0).getIdCajero();
            mostrarResumenTurno(idRetorno);
        }
    }
    public void mostrarAperturaCaja() {
        if (pantallaApertura == null) {
            pantallaApertura = new AperturaCaja(this);
        }

        ocultarTodas();

        pantallaApertura.cargarSupervisores(fachadaNegocio.consultarSupervisores());
        pantallaApertura.cargarCajeros(fachadaNegocio.consultarCajeros());

        pantallaApertura.setVisible(true);
    }

    public boolean confirmarApertura(aperturaCajaDTO dto) {
        boolean exito = fachadaNegocio.registrarApertura(dto);

        if (exito) {
            List<cajeroDTO> todosLosCajeros = fachadaNegocio.consultarCajeros();
            for (cajeroDTO c : todosLosCajeros) {
                if (c.getIdCajero() == dto.getIdCajero()) {
                    if (!cajerosConTurnoAbierto.contains(c)) {
                        cajerosConTurnoAbierto.add(c);
                    }
                    break;
                }
            }
        }
        return exito;
    }



    private void ocultarTodas() {
        if (pantallaResumen != null) pantallaResumen.setVisible(false);
        if (pantallaHistorial != null) pantallaHistorial.setVisible(false);
        if (pantallaFormulario != null) pantallaFormulario.setVisible(false);
        if (pantallaApertura != null) pantallaApertura.setVisible(false);
    }

    public String obtenerNombreSupervisorAsociado(int idCajero) {
        // TODO (BD): Cuando conecte mongo, aquí hare una consulta a la tabla AperturaCaja.

        // MOCK TEMPORAL
        List<supervisorDTO> supervisores = fachadaNegocio.consultarSupervisores();
        if (supervisores != null && !supervisores.isEmpty()) {
            int indice = (idCajero % 2 == 0) ? 1 : 0;
            if (indice < supervisores.size()) {
                return supervisores.get(indice).getNombreCompleto();
            }
            return supervisores.get(0).getNombreCompleto();
        }
        return "---";
    }


    // (BD): Crear la lógica para guardar el corte final en la base de datos.
    public void guardarCorteFinal(double montoEsperado, double montoReal, int idCajero, List<desgloseDTO> desgloses, String rutaImagen, String notas) {

        System.out.println("Guardando corte en BD y cerrando caja para el cajero ID: " + idCajero);

        cajerosConTurnoAbierto.removeIf(c -> c.getIdCajero() == idCajero);

        volverAResumen();
    }
}