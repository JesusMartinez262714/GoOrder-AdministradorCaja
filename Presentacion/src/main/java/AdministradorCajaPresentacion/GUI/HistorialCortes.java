package AdministradorCajaPresentacion.GUI;

import AdministradorCajaDTOs.corteCajaDTO;
import AdministradorCajaDTOs.desgloseDTO;
import AdministradorCajaPresentacion.Control.Control;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Pantalla que muestra el historial de todos los cortes de caja registrados en el sistema.
 * Permite buscar cortes por cajero o folio, aplicar filtros por estado y ordenar la lista.
 * * @author Jesus Manuel Martinez Cortez
 * @version 1.1
 */
public class HistorialCortes extends JFrame {

    private Control control;
    private List<corteCajaDTO> listaOriginal = new ArrayList<>();
    private corteCajaDTO corteSeleccionado;
    private JPanel pnlListaTarjetas;
    private JPanel pnlTicketDetalle;
    private JPopupMenu menuFiltros;

    private JTextField txtSearch;
    private JCheckBoxMenuItem chkRecientes;
    private JCheckBoxMenuItem chkAntiguos;
    private JCheckBoxMenuItem chkAscendente;
    private JCheckBoxMenuItem chkDescendente;
    private JCheckBoxMenuItem chkActivos;
    private JCheckBoxMenuItem chkCancelados;

    private final Color COLOR_FONDO = new Color(26, 26, 26);
    private final Color COLOR_ACCENTO = new Color(37, 211, 102);
    private final Color COLOR_CANCELADO = new Color(139, 115, 105);
    private final Color COLOR_ELIMINAR = new Color(211, 47, 47);
    private final Color COLOR_CARD_BG = Color.WHITE;

    /**
     * Constructor que recibe el controlador para comunicar la vista con el negocio
     * e inicializa todos los componentes de la ventana.
     * * @param control Controlador encargado de la navegación y flujo del sistema.
     */
    public HistorialCortes(Control control) {
        this.control = control;
        initComponents();
        configurarVentana();
    }

    /**
     * Configura los parámetros básicos de la ventana como tamaño, título,
     * cierre por defecto y centrado en pantalla.
     */
    private void configurarVentana() {
        setTitle("GoOrder - Historial de Cortes");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    /**
     * Inicializa y organiza todos los componentes visuales de la interfaz,
     * dividiendo la pantalla en el historial izquierdo y el detalle del ticket derecho.
     */
    private void initComponents() {
        setLayout(new GridLayout(1, 2));

        JPanel pnlLeft = new JPanel(new BorderLayout());
        pnlLeft.setBackground(COLOR_FONDO);
        pnlLeft.setBorder(new EmptyBorder(30, 40, 20, 40));

        JPanel pnlHeader = new JPanel();
        pnlHeader.setLayout(new BoxLayout(pnlHeader, BoxLayout.Y_AXIS));
        pnlHeader.setOpaque(false);

        JLabel lblTitulo = new JLabel("HISTORIAL DE CORTES DE CAJA");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel pnlSearchRow = new JPanel(new BorderLayout(15, 0));
        pnlSearchRow.setOpaque(false);
        pnlSearchRow.setBorder(new EmptyBorder(20, 0, 25, 0));

        txtSearch = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(220, 220, 220));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
            }
        };
        txtSearch.setOpaque(false);
        txtSearch.setBorder(new EmptyBorder(10, 15, 10, 15));
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setForeground(new Color(50, 50, 50));

        ((AbstractDocument) txtSearch.getDocument()).setDocumentFilter(new FiltroBusquedaMaximo());

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { aplicarFiltrosCombinados(); }
            public void removeUpdate(DocumentEvent e) { aplicarFiltrosCombinados(); }
            public void changedUpdate(DocumentEvent e) { aplicarFiltrosCombinados(); }
        });

        menuFiltros = new JPopupMenu();

        chkRecientes = new JCheckBoxMenuItem("Recientes", true);
        chkAntiguos = new JCheckBoxMenuItem("Antiguos", false);
        chkAscendente = new JCheckBoxMenuItem("Monto: Ascendente", false);
        chkDescendente = new JCheckBoxMenuItem("Monto: Descendente", false);
        chkActivos = new JCheckBoxMenuItem("Mostrar Activos", true);
        chkCancelados = new JCheckBoxMenuItem("Mostrar Cancelados", true);

        chkRecientes.addActionListener(e -> { desmarcarOtrosOrdenamientos(chkRecientes); aplicarFiltrosCombinados(); });
        chkAntiguos.addActionListener(e -> { desmarcarOtrosOrdenamientos(chkAntiguos); aplicarFiltrosCombinados(); });
        chkAscendente.addActionListener(e -> { desmarcarOtrosOrdenamientos(chkAscendente); aplicarFiltrosCombinados(); });
        chkDescendente.addActionListener(e -> { desmarcarOtrosOrdenamientos(chkDescendente); aplicarFiltrosCombinados(); });

        chkActivos.addActionListener(e -> aplicarFiltrosCombinados());
        chkCancelados.addActionListener(e -> aplicarFiltrosCombinados());

        menuFiltros.add(new JLabel("  ORDENAR POR:"));
        menuFiltros.add(chkRecientes);
        menuFiltros.add(chkAntiguos);
        menuFiltros.add(chkAscendente);
        menuFiltros.add(chkDescendente);
        menuFiltros.addSeparator();
        menuFiltros.add(new JLabel("  FILTRAR ESTADO:"));
        menuFiltros.add(chkActivos);
        menuFiltros.add(chkCancelados);

        JPanel pnlIcon = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2.0f));
                int w = getWidth();
                g2.drawLine(5, 12, w - 5, 12);
                g2.drawOval(10, 8, 8, 8);
                g2.drawLine(5, 22, w - 5, 22);
                g2.drawOval(w - 18, 18, 8, 8);
                g2.drawLine(5, 32, w - 5, 32);
                g2.drawOval(10, 28, 8, 8);
                g2.dispose();
            }
        };
        pnlIcon.setPreferredSize(new Dimension(40, 40));
        pnlIcon.setOpaque(false);
        pnlIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pnlIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) { menuFiltros.show(pnlIcon, 0, pnlIcon.getHeight()); }
        });

        pnlSearchRow.add(txtSearch, BorderLayout.CENTER);
        pnlSearchRow.add(pnlIcon, BorderLayout.EAST);
        pnlHeader.add(lblTitulo);
        pnlSearchRow.setBorder(new EmptyBorder(20, 0, 25, 0));
        pnlHeader.add(pnlSearchRow);
        pnlLeft.add(pnlHeader, BorderLayout.NORTH);

        pnlListaTarjetas = new JPanel();
        pnlListaTarjetas.setLayout(new BoxLayout(pnlListaTarjetas, BoxLayout.Y_AXIS));
        pnlListaTarjetas.setOpaque(false);

        JScrollPane scrollLeft = new JScrollPane(pnlListaTarjetas);
        scrollLeft.setBorder(null);
        scrollLeft.setOpaque(false);
        scrollLeft.getViewport().setOpaque(false);
        scrollLeft.getVerticalScrollBar().setUnitIncrement(16);
        pnlLeft.add(scrollLeft, BorderLayout.CENTER);

        JPanel pnlFooterLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlFooterLeft.setOpaque(false);

        JButton btnvolver = new BotonAccion("<-VOLVER", COLOR_ACCENTO, Color.BLACK, false);
        btnvolver.addActionListener(e -> control.volverAResumen());

        JButton btnAgregar = new BotonAccion("Agregar", COLOR_ACCENTO, Color.BLACK, true);
        btnAgregar.addActionListener(e -> control.mostrarFormularioCorte());

        pnlFooterLeft.add(btnvolver);
        pnlFooterLeft.add(btnAgregar);

        JPanel pnlFooterLeftWrapper = new JPanel(new BorderLayout());
        pnlFooterLeftWrapper.setOpaque(false);
        pnlFooterLeftWrapper.setBorder(new EmptyBorder(20, 0, 0, 0));
        pnlFooterLeftWrapper.add(pnlFooterLeft, BorderLayout.WEST);

        BotonAccion btnEliminar = new BotonAccion("Eliminar", COLOR_ELIMINAR, Color.WHITE, true);
        btnEliminar.addActionListener(e -> {
            if (corteSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un corte primero para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Esta acción eliminará el corte permanentemente de la base de datos.\n¿Desea continuar?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                control.eliminarCorte(corteSeleccionado.getIdCaja());
                corteSeleccionado = null;
            }
        });
        pnlFooterLeftWrapper.add(btnEliminar, BorderLayout.EAST);
        pnlLeft.add(pnlFooterLeftWrapper, BorderLayout.SOUTH);

        JPanel pnlRight = new JPanel(new BorderLayout());
        pnlRight.setBackground(Color.WHITE);
        pnlRight.setBorder(new EmptyBorder(30, 40, 30, 40));

        pnlTicketDetalle = new JPanel(new BorderLayout());
        pnlTicketDetalle.setBackground(Color.WHITE);
        JLabel lblPlaceholder = new JLabel("Seleccione un corte", SwingConstants.CENTER);
        lblPlaceholder.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        lblPlaceholder.setForeground(Color.LIGHT_GRAY);
        pnlTicketDetalle.add(lblPlaceholder);
        pnlRight.add(pnlTicketDetalle, BorderLayout.CENTER);

        JPanel pnlFooterRight = new JPanel(new BorderLayout());
        pnlFooterRight.setOpaque(false);
        JPanel pnlBtnsRightLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlBtnsRightLeft.setOpaque(false);

        BotonAccion btnEditar = new BotonAccion("Editar", COLOR_ACCENTO, Color.BLACK, true);
        btnEditar.addActionListener(e -> {
            if (corteSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un corte primero para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (corteSeleccionado.getEstado().equalsIgnoreCase("Cancelado")) {
                JOptionPane.showMessageDialog(this, "Este corte fue cancelado y ya no puede ser modificado.", "Acción Denegada", JOptionPane.ERROR_MESSAGE);
                return;
            }
            control.editarFormularioCorte(corteSeleccionado);
        });

        BotonAccion btnCancelar = new BotonAccion("Cancelar", COLOR_CANCELADO, Color.BLACK, true);
        btnCancelar.addActionListener(e -> {
            if (corteSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un corte primero para cancelar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (corteSeleccionado.getEstado().equalsIgnoreCase("Cancelado")) {
                JOptionPane.showMessageDialog(this, "Este corte ya se encuentra cancelado.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String motivo = JOptionPane.showInputDialog(this,
                    "Ingrese el motivo de la cancelación para el folio CC-" + corteSeleccionado.getIdCaja() + ":",
                    "Confirmar Cancelación",
                    JOptionPane.WARNING_MESSAGE);

            if (motivo != null) {
                String motivoLimpio = motivo.trim().replaceAll("\\s+", " ");

                if (motivoLimpio.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Debe ingresar un motivo válido para cancelar el corte.", "Motivo Requerido", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (motivoLimpio.length() < 10) {
                    JOptionPane.showMessageDialog(this, "El motivo ingresado es demasiado corto. Describa detalladamente la anomalía (mínimo 10 caracteres).", "Longitud Insuficiente", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String regexLetrasSuficientes = ".*[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ]{4,}.*";
                if (!motivoLimpio.matches(regexLetrasSuficientes)) {
                    JOptionPane.showMessageDialog(this, "El motivo carece de palabras descriptivas coherentes.", "Texto Inválido", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                control.cancelarCorte(corteSeleccionado, motivoLimpio);
            }
        });

        pnlBtnsRightLeft.add(btnEditar);
        pnlBtnsRightLeft.add(btnCancelar);
        pnlFooterRight.add(pnlBtnsRightLeft, BorderLayout.WEST);
        JButton btnGenerarPDF = new BotonAccion("GENERAR PDF", COLOR_ACCENTO, Color.BLACK, true);
        btnGenerarPDF.addActionListener(e -> {
            if (corteSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un corte primero para generar el PDF.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            control.generarReportePDF(corteSeleccionado);
        });
        pnlFooterRight.add(btnGenerarPDF,BorderLayout.EAST);

        pnlRight.add(pnlFooterRight, BorderLayout.SOUTH);

        add(pnlLeft);
        add(pnlRight);
    }

    /**
     * Desmarca las otras opciones de ordenamiento para asegurar que solo
     * se filtre por un criterio a la vez (recientes, antiguos, montos).
     * * @param seleccionado El checkbox de ordenamiento que el usuario activó.
     */
    private void desmarcarOtrosOrdenamientos(JCheckBoxMenuItem seleccionado) {
        if (seleccionado != chkRecientes) chkRecientes.setSelected(false);
        if (seleccionado != chkAntiguos) chkAntiguos.setSelected(false);
        if (seleccionado != chkAscendente) chkAscendente.setSelected(false);
        if (seleccionado != chkDescendente) chkDescendente.setSelected(false);
    }

    /**
     * Recibe la lista completa de cortes desde la base de datos, limpia la selección
     * actual del ticket y activa los filtros combinados.
     * * @param lista Lista de objetos DTO con los cortes de caja encontrados.
     */
    public void llenarTabla(List<corteCajaDTO> lista) {
        this.listaOriginal = (lista != null) ? lista : new ArrayList<>();
        this.corteSeleccionado = null;
        pnlTicketDetalle.removeAll();
        JLabel lblPlaceholder = new JLabel("Seleccione un corte", SwingConstants.CENTER);
        lblPlaceholder.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        lblPlaceholder.setForeground(Color.LIGHT_GRAY);
        pnlTicketDetalle.add(lblPlaceholder);
        pnlTicketDetalle.revalidate();
        pnlTicketDetalle.repaint();
        aplicarFiltrosCombinados();
    }

    /**
     * Filtra y ordena la lista de cortes en tiempo real combinando el texto
     * del buscador con el estado de los checkboxes seleccionados.
     */
    private void aplicarFiltrosCombinados() {
        String textoBusqueda = txtSearch.getText().toLowerCase().trim();
        final String query = textoBusqueda.equals("buscar por nombre...") ? "" : textoBusqueda;

        List<corteCajaDTO> filtrada = new ArrayList<>(listaOriginal);

        if (!query.isEmpty()) {
            filtrada = filtrada.stream()
                    .filter(c -> (c.getCajero() != null && c.getCajero().toLowerCase().contains(query)) ||
                            String.valueOf(c.getIdCaja()).contains(query))
                    .collect(Collectors.toList());
        }

        boolean mostrarActivos = chkActivos.isSelected();
        boolean mostrarCancelados = chkCancelados.isSelected();

        if (!mostrarActivos) {
            filtrada.removeIf(c -> c.getEstado().equalsIgnoreCase("CERRADA") || c.getEstado().equalsIgnoreCase("Vigente"));
        }
        if (!mostrarCancelados) {
            filtrada.removeIf(c -> c.getEstado().equalsIgnoreCase("Cancelado"));
        }

        if (chkAscendente.isSelected()) {
            filtrada.sort((c1, c2) -> Double.compare(c1.getMontoReal(), c2.getMontoReal()));
        } else if (chkDescendente.isSelected()) {
            filtrada.sort((c1, c2) -> Double.compare(c2.getMontoReal(), c1.getMontoReal()));
        } else if (chkAntiguos.isSelected()) {
            filtrada.sort((c1, c2) -> {
                if (c1.getFecha() == null || c2.getFecha() == null) return 0;
                return c1.getFecha().compareTo(c2.getFecha());
            });
        } else if (chkRecientes.isSelected()) {
            filtrada.sort((c1, c2) -> {
                if (c1.getFecha() == null || c2.getFecha() == null) return 0;
                return c2.getFecha().compareTo(c1.getFecha());
            });
        }

        renderizarCards(filtrada);
    }

    /**
     * Limpia el panel izquierdo y dibuja las tarjetas visuales de los cortes
     * que cumplieron con los filtros activos.
     * * @param lista Lista filtrada de cortes de caja que se van a mostrar.
     */
    private void renderizarCards(List<corteCajaDTO> lista) {
        pnlListaTarjetas.removeAll();
        for (corteCajaDTO c : lista) {
            pnlListaTarjetas.add(new CardCortePremium(c));
            pnlListaTarjetas.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        pnlListaTarjetas.revalidate();
        pnlListaTarjetas.repaint();
    }

    /**
     * Reconstruye de forma dinámica el diseño del ticket en formato HTML para mostrar
     * detalladamente el arqueo, desgloses y observaciones, e incrusta la evidencia gráfica
     * de forma nativa en la parte inferior absoluta del documento.
     */
    private void actualizarDetalleTicket(corteCajaDTO c) {
        this.corteSeleccionado = c;
        pnlTicketDetalle.removeAll();

        SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "MX"));
        SimpleDateFormat sdfHora = new SimpleDateFormat("hh:mm a", new Locale("es", "MX"));

        String fechaStr = (c.getFecha() != null) ? sdfFecha.format(c.getFecha()) : "S/F";
        String horaCierreStr = (c.getFecha() != null) ? sdfHora.format(c.getFecha()) : "--:--";
        String horaAperturaStr = (c.getFechaApertura() != null) ? sdfHora.format(c.getFechaApertura()) : "--:--";

        boolean isVigente = c.getEstado().equalsIgnoreCase("CERRADA") || c.getEstado().equalsIgnoreCase("Vigente");
        String estadoColor = isVigente ? "#2ECC71" : "#8B7369";

        double dif = c.getDiferencia();
        String colorDiferencia = (dif == 0) ? "#2ECC71" : "#D32F2F";

        String nombreSupervisor = control.obtenerNombreSupervisorAsociado(c.getIdCajero());
        String nombreCajero = (c.getCajero() != null) ? c.getCajero() : "Sin nombre";

        StringBuilder cancelacionHtml = new StringBuilder();
        if (c.getEstado().equalsIgnoreCase("Cancelado")) {
            String mot = (c.getMotivoCancelacion() != null && !c.getMotivoCancelacion().isEmpty())
                    ? c.getMotivoCancelacion() : "No especificado por el supervisor.";

            cancelacionHtml.append("<table width='100%' cellpadding='6' style='border: 1px solid #D32F2F; color:#D32F2F; background-color:#FFF4F4; margin-bottom:15px;'>")
                    .append("<tr><td><b> CORTE CANCELADO</b><br>")
                    .append("<span style='color:#333; font-size:11px;'><b>Motivo de Baja:</b> <i>").append(mot).append("</i></span></td></tr>")
                    .append("</table>");
        }

        StringBuilder conceptosHtml = new StringBuilder();
        if (c.getListaDesglose() != null && !c.getListaDesglose().isEmpty()) {
            for (desgloseDTO d : c.getListaDesglose()) {
                if (d.getMontoDeclarado() > 0) {
                    conceptosHtml.append("<tr><td style='font-style:italic;'>").append(d.getNombreMetodo())
                            .append("</td><td align='right'>$").append(String.format("%,.2f", d.getMontoDeclarado()))
                            .append("</td></tr>");
                }
            }
        } else {
            conceptosHtml.append("<tr><td style='font-style:italic;'>Sin Desgloses</td><td align='right'>$").append(String.format("%,.2f", c.getMontoReal())).append("</td></tr>");
        }

        String motivoStr = (c.getObservaciones() != null && !c.getObservaciones().isEmpty())
                ? c.getObservaciones()
                : "Sin observaciones registradas en este corte.";

        JTextPane txt = new JTextPane();
        txt.setContentType("text/html");
        txt.setEditable(false);
        txt.setBorder(null);

        String htmlTicket = "<html><body style='font-family:monospace; font-size:12px; color:#555; margin:10px;'>"
                + "<center>"
                + "<h1 style='color:#2ECC71; font-style:italic; font-size:26px; margin:0;'>GoOrder</h1>"
                + "<span style='color:#999; font-style:italic;'>Sucursal CENTRO - ID: #001</span><br>"
                + "<span style='color:#999; font-style:italic;'>Fecha: " + fechaStr + " | Cierre: " + horaCierreStr + "</span><br>"
                + "<span style='color:#999; font-style:italic;'>ESTADO: <b style='color:" + estadoColor + ";'>" + c.getEstado().toUpperCase() + "</b></span>"
                + "</center>"
                + "<hr style='color:#ccc; background-color:#ccc; height:1px; border:none; margin-top:15px;'>"
                + "<br><i style='color:#777;'>FOLIO DE CORTE: CC-" + c.getIdCaja() + "</i><br>"
                + "<br><i style='color:#777;'>CAJERO: " + nombreCajero + "</i><br>"
                + "<br><i style='color:#777;'>TURNO: (" + horaAperturaStr + " - " + horaCierreStr + ")</i><br><br>"

                + cancelacionHtml.toString()

                + "<table width='100%' cellspacing='0' cellpadding='2' style='color:#555;'>"
                + "<tr><th align='left' style='color:#2ECC71; font-style:italic; border-bottom:1px solid #555;'>Concepto</th>"
                + "<th align='right' style='color:#2ECC71; font-style:italic; border-bottom:1px solid #555;'>Monto</th></tr>"
                + "<tr><td colspan='2'><hr style='color:#ccc; background-color:#ccc; height:1px; border:none; margin:2px 0;'></td></tr>"
                + conceptosHtml.toString()
                + "<tr><td colspan='2'><hr style='color:#ccc; background-color:#ccc; height:1px; border:none; margin:2px 0;'></td></tr>"
                + "<tr><td style='font-style:italic;'>VENTA BRUTA TOTAL</td><td align='right'>$" + String.format("%,.2f", c.getMontoReal()) + "</td></tr>"
                + "<tr><td colspan='2'><hr style='color:#ccc; background-color:#ccc; height:1px; border:none; margin:2px 0;'></td></tr>"
                + "</table><br>"
                + "<table width='100%' cellpadding='5' style='border: 1px solid #aaa; color:#555; background-color:#f9f9f9;'>"
                + "<tr><td colspan='2' style='font-style:italic;'>RESUMEN DE ARQUEO:</td></tr>"
                + "<tr><td style='font-style:italic;'>Esperado:</td><td align='right'>$" + String.format("%,.2f", c.getMontoEsperado()) + "</td></tr>"
                + "<tr><td style='font-style:italic;'>Real:</td><td align='right'>$" + String.format("%,.2f", c.getMontoReal()) + "</td></tr>"
                + "<tr><td colspan='2' style='border-bottom: 1px dotted #aaa; padding:0;'></td></tr>"
                + "<tr><td style='color:" + colorDiferencia + "; font-weight:bold; font-style:italic;'>Diferencia:</td>"
                + "<td align='right' style='color:" + colorDiferencia + "; font-weight:bold;'>$" + String.format("%,.2f", c.getDiferencia()) + "</td></tr>"
                + "</table><br>"
                + ""
                + "<table width='100%' cellpadding='6' style='border: 1px solid " + colorDiferencia + "; color:#555; background-color:#FFFDFD;'>"
                + "<tr><td><b>Motivo / Observaciones del Corte:</b><br><i style='color:#333; font-size:11px;'>" + motivoStr + "</i></td></tr>"
                + "</table><br>"

                + "<br><table width='100%' style='color:#999;'>"
                + "<tr>"
                + "<td align='center'>_________________<br><br><i>FIRMA<br>" + nombreCajero.toUpperCase() + "</i></td>"
                + "<td align='center'>_________________<br><br><i>FIRMA<br>" + (nombreSupervisor != null ? nombreSupervisor.toUpperCase() : "SUPERVISOR") + "</i></td>"
                + "</tr>"
                + "</table><br><br>"

                + "<center><span style='color:#999; font-size:10px; font-style:italic;'>EVIDENCIA DE AUDITORÍA</span></center>"
                + "</body></html>";

        txt.setText(htmlTicket);

        String base64Image = c.getEvidenciaGrafica();
        if (base64Image != null && !base64Image.trim().isEmpty()) {
            try {
                byte[] imageBytes = java.util.Base64.getDecoder().decode(base64Image);
                Image awtImage = Toolkit.getDefaultToolkit().createImage(imageBytes);

                Image imagenEscalada = awtImage.getScaledInstance(120, 90, Image.SCALE_SMOOTH);
                ImageIcon iconoFinal = new ImageIcon(imagenEscalada);

                JLabel lblFotoComponente = new JLabel(iconoFinal);
                lblFotoComponente.setAlignmentX(Component.CENTER_ALIGNMENT);

                txt.setCaretPosition(txt.getDocument().getLength());

                txt.replaceSelection("\n\n");

                javax.swing.text.SimpleAttributeSet center = new javax.swing.text.SimpleAttributeSet();
                javax.swing.text.StyleConstants.setAlignment(center, javax.swing.text.StyleConstants.ALIGN_CENTER);
                txt.getStyledDocument().setParagraphAttributes(txt.getDocument().getLength(), 1, center, false);

                txt.insertComponent(lblFotoComponente);

            } catch (Exception ex) {
                System.err.println("Error al renderizar el componente nativo de imagen: " + ex.getMessage());
            }
        }

        JScrollPane scrollPane = new JScrollPane(txt);
        scrollPane.setBorder(null);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);

        pnlTicketDetalle.add(scrollPane, BorderLayout.CENTER);
        pnlTicketDetalle.revalidate();
        pnlTicketDetalle.repaint();
    }

    /**
     * Filtro para el campo de búsqueda que limita la cantidad máxima de caracteres
     * permitidos para evitar errores de desbordamiento en la entrada de texto.
     */
    private class FiltroBusquedaMaximo extends DocumentFilter {
        private final int limiteMaximo = 40;

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string == null) return;
            if ((fb.getDocument().getLength() + string.length()) <= limiteMaximo) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null) return;
            if ((fb.getDocument().getLength() - length + text.length()) <= limiteMaximo) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }

    /**
     * Clase interna para diseñar de forma visual la tarjeta resumida de cada corte,
     * incluyendo animaciones de color cuando el mouse pasa por encima.
     */
    class CardCortePremium extends JPanel {

        /**
         * Configura los datos, etiquetas de estado y colores para la tarjeta del corte.
         * * @param c Objeto DTO con la información del corte de caja.
         */
        public CardCortePremium(corteCajaDTO c) {
            setOpaque(false);
            setLayout(new BorderLayout(15, 0));
            setBackground(COLOR_CARD_BG);
            setBorder(new EmptyBorder(12, 15, 12, 15));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setMaximumSize(new Dimension(800, 85));

            boolean isVigente = c.getEstado().equalsIgnoreCase("CERRADA") || c.getEstado().equalsIgnoreCase("Vigente");

            String textoEstado = c.getEstado().equalsIgnoreCase("CERRADA") ? "Vigente" : c.getEstado();

            JLabel lblPill = new JLabel(textoEstado, SwingConstants.CENTER) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(isVigente ? COLOR_ACCENTO : COLOR_CANCELADO);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    super.paintComponent(g);
                }
            };
            lblPill.setForeground(Color.WHITE);
            lblPill.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblPill.setPreferredSize(new Dimension(85, 30));

            JPanel pnlPillCenter = new JPanel(new GridBagLayout());
            pnlPillCenter.setOpaque(false);
            pnlPillCenter.add(lblPill);

            JPanel pnlTextos = new JPanel(new GridLayout(2, 1, 0, 2));
            pnlTextos.setOpaque(false);

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy - hh:mm a", new Locale("es", "MX"));
            JLabel lblFecha = new JLabel((c.getFecha() != null) ? sdf.format(c.getFecha()) : "Sin fecha");
            lblFecha.setFont(new Font("Segoe UI", Font.BOLD, 15));
            lblFecha.setForeground(Color.BLACK);

            String nombreCajero = (c.getCajero() != null) ? c.getCajero() : "Sin nombre";
            JLabel lblCajero = new JLabel(nombreCajero);
            lblCajero.setFont(new Font("Segoe UI", Font.BOLD, 11));
            lblCajero.setForeground(new Color(50, 50, 50));

            pnlTextos.add(lblFecha);
            pnlTextos.add(lblCajero);

            JPanel pnlRightElement = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            pnlRightElement.setOpaque(false);

            JLabel lblMonto = new JLabel("$" + String.format("%,.2f", c.getMontoReal()));
            lblMonto.setFont(new Font("Segoe UI", Font.BOLD, 18));
            lblMonto.setForeground(Color.BLACK);

            JLabel lblArrow = new JLabel(">");
            lblArrow.setFont(new Font("Segoe UI", Font.BOLD, 20));
            lblArrow.setForeground(Color.BLACK);

            pnlRightElement.add(lblMonto);
            pnlRightElement.add(lblArrow);

            add(pnlPillCenter, BorderLayout.WEST);
            add(pnlTextos, BorderLayout.CENTER);
            add(pnlRightElement, BorderLayout.EAST);

            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mousePressed(java.awt.event.MouseEvent e) { actualizarDetalleTicket(c); }
                public void mouseEntered(java.awt.event.MouseEvent e) { setBackground(new Color(245, 255, 248)); repaint(); }
                public void mouseExited(java.awt.event.MouseEvent e) { setBackground(COLOR_CARD_BG); repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * Clase interna para personalizar el diseño visual de los botones de acción,
     * aplicando bordes redondeados y fuentes específicas de la paleta.
     */
    class BotonAccion extends JButton {

        /**
         * Inicializa el botón configurando su texto, colores de fondo y estilo de fuente.
         * * @param t Texto que llevará el botón.
         * @param bg Color de fondo.
         * @param fg Color del texto.
         * @param italic Indica si la fuente se mostrará en estilo cursiva.
         */
        public BotonAccion(String t, Color bg, Color fg, boolean italic) {
            super(t);
            setBackground(bg);
            setForeground(fg);
            int style = Font.BOLD | (italic ? Font.ITALIC : Font.PLAIN);
            setFont(new Font("Segoe UI", style, 13));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(8, 20, 8, 20));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            super.paintComponent(g);
        }
    }
}