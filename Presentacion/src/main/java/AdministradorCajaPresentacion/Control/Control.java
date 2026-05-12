package AdministradorCajaPresentacion.Control;

import AdministradorCajaDTOs.desgloseDTO;
import AdministradorCajaDTOs.resumenVentasDTO;
import AdministradorCajaDTOs.cajeroDTO;
import AdministradorCajaDTOs.corteCajaDTO; // Importante
import AdministradorCajaPresentacion.GUI.ConciliacionFinal;
import AdministradorCajaPresentacion.GUI.FormularioCorte;
import AdministradorCajaPresentacion.GUI.ResumenTurno;
import AdministradorCajaPresentacion.GUI.HistorialCortes;
import Interfaces.INegocioCorte;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Control {

    private INegocioCorte fachadaNegocio;
    private ResumenTurno pantallaResumen;
    private HistorialCortes pantallaHistorial;
    private FormularioCorte pantallaFormulario;

    public Control(INegocioCorte fachadaNegocio) {
        this.fachadaNegocio = fachadaNegocio;
    }

    public void iniciarFlujoResumen() {
        if (pantallaResumen == null) {
            pantallaResumen = new ResumenTurno(this);
        }

        try {
            List<cajeroDTO> cajerosActivos = new ArrayList<>();
            cajerosActivos.add(new cajeroDTO(1, "Juan Leonel", "Matutino"));
            cajerosActivos.add(new cajeroDTO(2, "Maria Garcia", "Vespertino"));

            cajeroDTO primerCajero = cajerosActivos.get(0);
            resumenVentasDTO resumenInicial = fachadaNegocio.generarResumenVentasTurno(primerCajero.getIdCajero(), new Date());

            pantallaResumen.cargarDatos(resumenInicial, cajerosActivos, "Jesus Martinez");

            pantallaResumen.setCajeroChangeListener(e -> {
                if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                    cajeroDTO seleccionado = (cajeroDTO) e.getItem();
                    resumenVentasDTO nuevoResumen = fachadaNegocio.generarResumenVentasTurno(seleccionado.getIdCajero(), new Date());
                    pantallaResumen.actualizarMontos(nuevoResumen, seleccionado);
                }
            });

            pantallaResumen.setVisible(true);

        } catch (Exception e) {
            System.err.println("Error al cargar la pantalla de resumen: " + e.getMessage());
        }
    }



    public void iniciarFlujoHistorial() {
        if (pantallaHistorial == null) {
            pantallaHistorial = new HistorialCortes(this);
        }

        actualizarTablaHistorial(new Date(), new Date());
        pantallaHistorial.setVisible(true);
    }


    public void actualizarTablaHistorial(Date inicio, Date fin) {
        List<corteCajaDTO> historial = fachadaNegocio.consultarCortesRealizados(inicio, fin);
        if (historial != null) {
            pantallaHistorial.llenarTabla(historial);
        }
    }

    public void mostrarResumenTurno(int idCajero) {
        if (pantallaResumen == null) {
            pantallaResumen = new ResumenTurno(this);
        }
        if (pantallaHistorial != null) pantallaHistorial.setVisible(false);
        pantallaResumen.setVisible(true);
    }

    public void mostrarHistorialCortes() {
        if (pantallaHistorial == null) {
            pantallaHistorial = new HistorialCortes(this);
        }
        if (pantallaResumen != null) pantallaResumen.setVisible(false);

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

        if (pantallaResumen != null) pantallaResumen.setVisible(false);
        if (pantallaHistorial != null) pantallaHistorial.setVisible(false);

        pantallaFormulario.cargarEmpleados(fachadaNegocio.consultarCajeros());

        pantallaFormulario.setVisible(true);
    }

    public double obtenerMontoEsperado(int idEmpleado) {
        return fachadaNegocio.obtenerVentasTotalesPorCajero(idEmpleado);
    }

    public void volverAResumen() {
        mostrarResumenTurno(1);
    }

    public void mostrarConciliacionFinal(double esperado, double contado, cajeroDTO emp, List<desgloseDTO> desgloses, String rutaImg) {
        if (pantallaFormulario != null) pantallaFormulario.setVisible(false);

        ConciliacionFinal pnlConciliacion = new ConciliacionFinal(this, esperado, contado, emp, desgloses, rutaImg);
        pnlConciliacion.setVisible(true);
    }

    public void volverAFormulario() {
        if (pantallaFormulario != null) pantallaFormulario.setVisible(true);
    }

}