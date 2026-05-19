package AdministradorCajaPresentacion.Control;

import AdministradorCajaDTOs.*;
import AdministradorCajaPresentacion.GUI.*;
import AdministrarCaja.JasperPDFAdapter;
import Interfaces.INegocioCorte;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase controlador que coordina el flujo de las pantallas visuales y conecta
 * la interfaz de usuario con la capa de negocio mediante el uso de una fachada.
 * * @author Jesus Manuel Martinez Cortez
 * @version 1.0
 */
public class Control {

    private final INegocioCorte fachadaNegocio;
    private ResumenTurno pantallaResumen;
    private HistorialCortes pantallaHistorial;
    private FormularioCorte pantallaFormulario;
    private AperturaCaja pantallaApertura;
    private GestionCajeros pantallaGestionCajeros;
    private GestionSupervisores pantallaSupervisores;

    private List<cajeroDTO> cajerosConTurnoAbierto = new ArrayList<>();
    private Map<Integer, Integer> mapaAsAssignacionIds = new HashMap<>();
    private String nombreSupervisorActivo = "---";

    /**
     * Constructor que recibe la fachada de negocio para inicializar la conexión
     * entre las capas del sistema.
     * * @param fachadaNegocio Interfaz de la capa de negocio.
     */
    public Control(INegocioCorte fachadaNegocio) {
        this.fachadaNegocio = fachadaNegocio;
    }

    /**
     * Arranca el flujo del resumen de ventas del turno, consulta los cajeros con
     * turno activo y configura el ComboBox de selección.
     */
    public void iniciarFlujoResumen() {
        this.cajerosConTurnoAbierto.clear();
        this.mapaAsAssignacionIds.clear();
        this.nombreSupervisorActivo = "---";

        List<cajeroDTO> listaActivos = fachadaNegocio.consultarCajerosConTurnoActivo();
        this.cajerosConTurnoAbierto = (listaActivos != null) ? listaActivos : new ArrayList<>();

        for (cajeroDTO c : cajerosConTurnoAbierto) {
            if (c != null) {
                Integer idSup = fachadaNegocio.obtenerIdSupervisorDeAperturaActiva(c.getIdCajero());
                if (idSup != null && idSup > 0) {
                    mapaAsAssignacionIds.put(c.getIdCajero(), idSup);
                }
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

    /**
     * Regresa a la vista principal de resumen de turno actualizando la información.
     */
    public void volverAResumen() {
        if (cajerosConTurnoAbierto.isEmpty()) {
            iniciarFlujoResumen();
        } else {
            mostrarResumenTurno(cajerosConTurnoAbierto.get(0).getIdCajero());
        }
    }

    /**
     * Oculta todas las ventanas registradas en el sistema para evitar superposiciones.
     */
    public void ocultarTodas() {
        if (pantallaResumen != null) pantallaResumen.setVisible(false);
        if (pantallaHistorial != null) pantallaHistorial.setVisible(false);
        if (pantallaFormulario != null) pantallaFormulario.setVisible(false);
        if (pantallaApertura != null) pantallaApertura.setVisible(false);
        if (pantallaGestionCajeros != null) pantallaGestionCajeros.setVisible(false);
        if (pantallaSupervisores != null) pantallaSupervisores.setVisible(false);
    }

    /**
     * Carga los catálogos y muestra la ventana para registrar una apertura de caja.
     */
    public void mostrarAperturaCaja() {
        if (pantallaApertura == null) pantallaApertura = new AperturaCaja(this);
        ocultarTodas();
        pantallaApertura.cargarSupervisores(fachadaNegocio.consultarSupervisores());
        pantallaApertura.cargarCajeros(fachadaNegocio.consultarCajeros());
        pantallaApertura.setVisible(true);
    }

    /**
     * Envía los datos de apertura a la capa de negocio y actualiza el mapa de sesiones.
     * * @param dto Objeto con los datos del cajero, supervisor y fondo inicial.
     * @return true si el registro fue exitoso, false en caso contrario.
     */
    public boolean confirmarApertura(aperturaCajaDTO dto) {
        if (dto == null) return false;
        boolean exito = fachadaNegocio.registrarApertura(dto);
        if (exito) {
            mapaAsAssignacionIds.put(dto.getIdCajero(), dto.getIdSupervisor());
            List<cajeroDTO> listaActivos = fachadaNegocio.consultarCajerosConTurnoActivo();
            this.cajerosConTurnoAbierto = (listaActivos != null) ? listaActivos : new ArrayList<>();
        }
        return exito;
    }

    /**
     * Busca el nombre del supervisor asignado a un cajero en el turno actual.
     * * @param idCajero ID único del cajero.
     * @return Cadena de texto con el nombre del supervisor.
     */
    public String obtenerNombreSupervisorAsociado(int idCajero) {
        Integer idSup = mapaAsAssignacionIds.get(idCajero);
        if (idSup == null) {
            idSup = fachadaNegocio.obtenerIdSupervisorDeAperturaActiva(idCajero);
        }
        return (idSup != null && idSup > 0) ? fachadaNegocio.obtenerNombreSupervisorPorId(idSup) : "---";
    }

    /**
     * Calcula las ventas del cajero y muestra su resumen en la pantalla principal.
     * * @param idCajero ID único del cajero seleccionado.
     */
    public void mostrarResumenTurno(int idCajero) {
        if (pantallaResumen == null) pantallaResumen = new ResumenTurno(this);
        ocultarTodas();

        resumenVentasDTO resumen = fachadaNegocio.generarResumenVentasTurno(idCajero, new Date());
        this.nombreSupervisorActivo = obtenerNombreSupervisorAsociado(idCajero);

        pantallaResumen.cargarDatos(resumen, cajerosConTurnoAbierto, nombreSupervisorActivo, idCajero);
        pantallaResumen.setVisible(true);
    }

    /**
     * Actualiza los montos del panel de resumen al seleccionar un cajero diferente.
     * * @param seleccionado Objeto DTO del cajero seleccionado.
     */
    private void actualizarDatosResumen(cajeroDTO seleccionado) {
        if (seleccionado == null) return;
        resumenVentasDTO r = fachadaNegocio.generarResumenVentasTurno(seleccionado.getIdCajero(), new Date());
        this.nombreSupervisorActivo = obtenerNombreSupervisorAsociado(seleccionado.getIdCajero());
        pantallaResumen.actualizarMontos(r, seleccionado, nombreSupervisorActivo);
    }

    /**
     * Despliega la pantalla del formulario para capturar un nuevo corte de caja.
     */
    public void mostrarFormularioCorte() {
        if (pantallaFormulario == null) pantallaFormulario = new FormularioCorte(this);
        else pantallaFormulario.limpiarFormulario();
        ocultarTodas();
        pantallaFormulario.cargarEmpleados(cajerosConTurnoAbierto);
        pantallaFormulario.setVisible(true);
    }

    /**
     * Carga los datos de un corte guardado en el formulario para permitir su edición.
     * * @param corteSeleccionado Objeto DTO con los datos del corte a modificar.
     */
    public void editarFormularioCorte(corteCajaDTO corteSeleccionado) {
        if (corteSeleccionado == null) return;
        if (pantallaFormulario == null) pantallaFormulario = new FormularioCorte(this);
        ocultarTodas();
        pantallaFormulario.cargarEmpleados(fachadaNegocio.consultarCajeros());
        pantallaFormulario.cargarCorteParaEdicion(corteSeleccionado);
        pantallaFormulario.setVisible(true);
    }

    /**
     * Consulta las ventas totales registradas en el sistema para un cajero.
     * * @param id ID único del cajero.
     * @return Monto acumulado de ventas esperadas en formato double.
     */
    public double obtenerMontoEsperado(int id) {
        return fachadaNegocio.obtenerVentasTotalesPorCajero(id);
    }

    /**
     * Oculta el formulario de captura y abre la ventana modal de conciliación final.
     * * @param esp Monto esperado en el sistema.
     * @param cont Monto contado físicamente.
     * @param c Datos del cajero.
     * @param des Lista con los desgloses ingresados.
     * @param img Ruta de la imagen del comprobante.
     * @param idCorteEditando ID del corte si es edición, o null si es nuevo.
     */
    public void mostrarConciliacionFinal(double esp, double cont, cajeroDTO c, List<desgloseDTO> des, String img, Integer idCorteEditando) {
        if (pantallaFormulario != null) pantallaFormulario.setVisible(false);
        new ConciliacionFinal(this, esp, cont, c, des, img, idCorteEditando).setVisible(true);
    }

    /**
     * Restablece la visibilidad del formulario de corte al cancelar la conciliación.
     */
    public void volverAFormulario() {
        if (pantallaFormulario != null) pantallaFormulario.setVisible(true);
    }

    /**
     * Estructura el DTO definitivo, gestiona reversión de adeudos anteriores si es una
     * edición, y guarda el registro del corte de caja final en el sistema.
     * * @param esp Monto esperado.
     * @param cont Monto real contado.
     * @param idC ID del cajero.
     * @param des Lista de desgloses de pago.
     * @param img Ruta del comprobante adjunto.
     * @param nota Observaciones de la conciliación.
     * @param idCorteEditando ID del corte bajo edición.
     */
    public void guardarCorteFinal(double esp, double cont, int idC, List<desgloseDTO> des, String img, String nota, Integer idCorteEditando) {
        double diff = fachadaNegocio.calcularDiferencia(esp, cont);
        corteCajaDTO dto = new corteCajaDTO();

        if (idCorteEditando != null) {
            dto.setIdCaja(idCorteEditando);
            List<corteCajaDTO> historial = fachadaNegocio.consultarCortesRealizados(new Date(0), new Date());
            if (historial != null) {
                for (corteCajaDTO viejo : historial) {
                    if (viejo != null && viejo.getIdCaja() == idCorteEditando && viejo.getDiferencia() < 0) {
                        fachadaNegocio.liquidarAdeudo(idC, Math.abs(viejo.getDiferencia()));
                        break;
                    }
                }
            }
        }

        dto.setMontoEsperado(esp);
        dto.setMontoReal(cont);
        dto.setDiferencia(diff);
        dto.setEstado(fachadaNegocio.evaluarEstadoCorte(diff));
        dto.setIdCajero(idC);
        dto.setIdSupervisor(mapaAsAssignacionIds.getOrDefault(idC, 1));
        dto.setListaDesglose(des);
        dto.setObservaciones(nota);

        if (fachadaNegocio.guardarNuevoCorte(dto, des)) {
            final String accion = (idCorteEditando != null) ? "actualizado" : "cerrado";
            final double diferenciaAbsoluta = Math.abs(diff);

            SwingUtilities.invokeLater(() -> {
                if (diff < 0) {
                    JOptionPane.showMessageDialog(null,
                            "Corte " + accion + ".\nSe registró un adeudo faltante de $" + String.format("%,.2f", diferenciaAbsoluta) + " al cajero.",
                            "Corte con Faltante", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "¡Corte " + accion + " exitosamente! La caja ha quedado completamente cuadrada.");
                }
            });

            if (idCorteEditando == null) {
                cajerosConTurnoAbierto.removeIf(c -> c != null && c.getIdCajero() == idC);
                mapaAsAssignacionIds.remove(idC);
            }

            mostrarHistorialCortes();
        }
    }

    /**
     * Consulta los registros históricos de la base de datos y despliega la pantalla
     * del historial de cortes de caja.
     */
    public void mostrarHistorialCortes() {
        try {
            if (pantallaHistorial == null) pantallaHistorial = new HistorialCortes(this);
            List<corteCajaDTO> lista = fachadaNegocio.consultarCortesRealizados(new Date(0), new Date());
            List<corteCajaDTO> listaSegura = (lista != null) ? lista : new ArrayList<>();
            pantallaHistorial.llenarTabla(listaSegura);
            ocultarTodas();
            pantallaHistorial.setVisible(true);
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Error al abrir historial: " + e.getMessage()));
        }
    }

    /**
     * Ordena o remueve registros de la lista del historial basándose en un criterio.
     * * @param criterio El tipo de filtro o clasificación a aplicar (Ascendente, Descendente, etc.).
     */
    public void filtrarHistorial(String criterio) {
        if (criterio == null) return;
        List<corteCajaDTO> lista = fachadaNegocio.consultarCortesRealizados(new Date(0), new Date());
        if (lista == null) return;
        switch (criterio) {
            case "Ascendente" -> lista.sort((c1, c2) -> Double.compare(c1.getMontoReal(), c2.getMontoReal()));
            case "Descendente" -> lista.sort((c2, c1) -> Double.compare(c2.getMontoReal(), c1.getMontoReal()));
            case "Activos" -> lista.removeIf(c -> c != null && c.getEstado().equalsIgnoreCase("cancelado"));
            case "Cancelados" -> lista.removeIf(c -> c != null && !c.getEstado().equalsIgnoreCase("cancelado"));
        }
        if (pantallaHistorial != null) pantallaHistorial.llenarTabla(lista);
    }

    /**
     * Elimina permanentemente un registro de corte de caja de la base de datos.
     * * @param idCorte ID único del corte a eliminar.
     */
    public void eliminarCorte(int idCorte) {
        if (fachadaNegocio.eliminarCorteFisico(idCorte)) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "El corte ha sido eliminado exitosamente de la base de datos."));
            mostrarHistorialCortes();
        } else {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Error al intentar eliminar el corte.", "Error", JOptionPane.ERROR_MESSAGE));
        }
    }

    /**
     * Cancela de forma lógica un corte de caja, cambiando su estado y revirtiendo
     * los adeudos asociados si aplicaba.
     * * @param corte Objeto DTO del corte a cancelar.
     * @param motivo Justificación del supervisor para dar de baja el corte.
     */
    public void cancelarCorte(corteCajaDTO corte, String motivo) {
        if (corte == null) return;
        if (fachadaNegocio.cancelarCorteLogico(corte, motivo)) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "El corte ha sido cancelado exitosamente y los adeudos se han revertido (si aplicaba)."));
            mostrarHistorialCortes();
        } else {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Error al intentar cancelar el corte.", "Error", JOptionPane.ERROR_MESSAGE));
        }
    }

    /**
     * Abre un cuadro selector para guardar el ticket de desglose del corte
     * en un archivo de formato PDF en el disco mediante JasperReports.
     * * @param corte Objeto DTO con el registro contable seleccionado.
     */
    public void generarReportePDF(corteCajaDTO corte) {
        if (corte == null) return;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Ticket de Corte PDF");
        fileChooser.setSelectedFile(new java.io.File("Corte_Caja_CC-" + corte.getIdCaja() + ".pdf"));

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File archivoParaGuardar = fileChooser.getSelectedFile();
            String rutaDestino = archivoParaGuardar.getAbsolutePath();

            if (!rutaDestino.toLowerCase().endsWith(".pdf")) {
                rutaDestino += ".pdf";
                archivoParaGuardar = new java.io.File(rutaDestino);
            }

            try {
                JasperPDFAdapter pdfAdapter = new JasperPDFAdapter();
                pdfAdapter.generarTicketCorte(corte, rutaDestino);

                if (java.awt.Desktop.isDesktopSupported()) {
                    java.awt.Desktop.getDesktop().open(archivoParaGuardar);
                } else {
                    final String destinoFinal = rutaDestino;
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "PDF generado con éxito en:\n" + destinoFinal, "Éxito", JOptionPane.INFORMATION_MESSAGE));
                }

            } catch (final Exception ex) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Error al generar el PDF:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE));
            }
        }
    }

    /**
     * Abre la ventana del catálogo para la administración y control de cajeros.
     */
    public void mostrarGestionCajeros() {
        if (pantallaGestionCajeros == null) pantallaGestionCajeros = new GestionCajeros(this);
        ocultarTodas();
        filtrarCajerosLista("", "Todos", "Todos");
        pantallaGestionCajeros.setVisible(true);
    }

    /**
     * Registra un nuevo cajero y actualiza la lista visual de la pantalla.
     * * @param dto Objeto DTO con los datos del cajero.
     * @return true si el registro fue exitoso, false en caso contrario.
     */
    public boolean registrarCajero(cajeroDTO dto) {
        if (dto == null) return false;
        if (fachadaNegocio.registrarCajero(dto)) {
            filtrarCajerosLista("", "Todos", "Todos");
            return true;
        }
        return false;
    }

    /**
     * Genera y devuelve el balance de ventas de un cajero para validaciones previas.
     * * @param idCajero ID único del cajero.
     * @return Objeto DTO con las sumas de las ventas.
     */
    public resumenVentasDTO obtenerResumenCajero(int idCajero) {
        return fachadaNegocio.generarResumenVentasTurno(idCajero, new Date());
    }

    /**
     * Modifica los datos de un cajero existente y refresca el catálogo visual.
     * * @param dto Objeto DTO con los datos actualizados del cajero.
     * @return true si la edición fue exitosa, false en caso contrario.
     */
    public boolean editarCajero(cajeroDTO dto) {
        if (dto == null) return false;
        if (fachadaNegocio.editarCajero(dto)) {
            filtrarCajerosLista("", "Todos", "Todos");
            return true;
        }
        return false;
    }

    /**
     * Actualiza el panel de gestión consultando los cajeros que cumplen con
     * los filtros de nombre, turno y adeudo elegidos.
     * * @param n Nombre o filtro de búsqueda por texto.
     * @param t Criterio de filtrado de turno.
     * @param d Criterio de filtrado de deudas.
     */
    public void filtrarCajerosLista(String n, String t, String d) {
        List<cajeroDTO> lista = fachadaNegocio.obtenerCajerosFiltrados(n, t, d);
        if (pantallaGestionCajeros != null) pantallaGestionCajeros.cargarCajeros(lista);
    }

    /**
     * Elimina a un cajero del catálogo por su ID único.
     * * @param id ID único del cajero.
     */
    public void eliminarCajero(int id) {
        if (fachadaNegocio.eliminarCajero(id)) filtrarCajerosLista("", "Todos", "Todos");
    }

    /**
     * Registra el abono de efectivo para saldar la deuda de un cajero, validando
     * que la cantidad no sobrepase el saldo pendiente real de su cuenta.
     * * @param idCajero ID único del cajero.
     * @param montoPagado Cantidad de dinero abonada.
     */
    public void procesarPagoAdeudo(int idCajero, double montoPagado) {
        if (montoPagado <= 0) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "El monto ingresado debe ser mayor a cero.", "Error", JOptionPane.ERROR_MESSAGE));
            return;
        }

        List<cajeroDTO> lista = fachadaNegocio.obtenerCajerosFiltrados("", "Todos", "Todos");
        cajeroDTO cajero = null;
        if (lista != null) {
            for (cajeroDTO c : lista) {
                if (c != null && c.getIdCajero() == idCajero) {
                    cajero = c;
                    break;
                }
            }
        }

        if (cajero == null) return;

        if (montoPagado > cajero.getMontoAdeudo()) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "No se puede realizar el pago: El monto ingresado supera el adeudo actual.", "Error", JOptionPane.ERROR_MESSAGE));
            return;
        }

        if (fachadaNegocio.liquidarAdeudo(idCajero, montoPagado)) {
            final double saldoRestante = cajero.getMontoAdeudo() - montoPagado;
            SwingUtilities.invokeLater(() -> {
                if (saldoRestante > 0) {
                    JOptionPane.showMessageDialog(null, "Abono registrado con éxito. Cantidad faltante: $" + String.format("%,.2f", saldoRestante), "Pago Registrado", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "El adeudo ha sido liquidado por completo. El cajero se encuentra al corriente.", "Adeudo Liquidado", JOptionPane.INFORMATION_MESSAGE);
                }
            });
            filtrarCajerosLista("", "Todos", "Todos");
        } else {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Ocurrió un error al procesar el pago del adeudo en el sistema.", "Error", JOptionPane.ERROR_MESSAGE));
        }
    }

    /**
     * Abre la ventana del catálogo para la administración y registro de supervisores.
     */
    public void mostrarGestionSupervisores() {
        if (pantallaSupervisores == null) pantallaSupervisores = new GestionSupervisores(this);
        ocultarTodas();
        filtrarSupervisoresLista("");
        pantallaSupervisores.setVisible(true);
    }

    /**
     * Registra un nuevo supervisor en el sistema y actualiza la tabla de datos.
     * * @param dto Objeto DTO con los datos del supervisor.
     * @return true si el registro fue exitoso, false en caso contrario.
     */
    public boolean registrarSupervisor(supervisorDTO dto) {
        if (dto == null) return false;
        if (fachadaNegocio.registrarSupervisor(dto)) {
            filtrarSupervisoresLista("");
            return true;
        }
        return false;
    }

    /**
     * Modifica la información de un supervisor existente y refresca la tabla visual.
     * * @param dto Objeto DTO con los datos modificados.
     * @return true si la edición fue exitosa, false en caso contrario.
     */
    public boolean editarSupervisor(supervisorDTO dto) {
        if (dto == null) return false;
        if (fachadaNegocio.editarSupervisor(dto)) {
            filtrarSupervisoresLista("");
            return true;
        }
        return false;
    }

    /**
     * Filtra la lista de supervisores basándose en un criterio de texto.
     * * @param nombre Cadena de caracteres o nombre a buscar.
     */
    public void filtrarSupervisoresLista(String nombre) {
        List<supervisorDTO> lista = fachadaNegocio.obtenerSupervisoresFiltrados(nombre);
        if (pantallaSupervisores != null) pantallaSupervisores.cargarSupervisores(lista);
    }

    /**
     * Elimina de forma permanente un supervisor del catálogo por su ID único.
     * * @param id ID único del supervisor.
     */
    public void eliminarSupervisor(int id) {
        if (fachadaNegocio.eliminarSupervisor(id)) filtrarSupervisoresLista("");
    }
}