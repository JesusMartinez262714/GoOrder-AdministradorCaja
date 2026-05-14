package AdministradorCajaPresentacion.GUI;

import AdministradorCajaDTOs.corteCajaDTO;
import AdministradorCajaDTOs.desgloseDTO;
import AdministradorCajaPresentacion.Control.Control;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class HistorialCortes extends JFrame {

    private Control control;
    private List<corteCajaDTO> listaOriginal = new ArrayList<>();
    private corteCajaDTO corteSeleccionado;
    private JPanel pnlListaTarjetas;
    private JPanel pnlTicketDetalle;
    private JPopupMenu menuFiltros;

    private final Color COLOR_FONDO = new Color(26, 26, 26);
    private final Color COLOR_ACCENTO = new Color(37, 211, 102);
    private final Color COLOR_CANCELADO = new Color(139, 115, 105);
    private final Color COLOR_ELIMINAR = new Color(211, 47, 47);
    private final Color COLOR_CARD_BG = Color.WHITE;

    public HistorialCortes(Control control) {
        this.control = control;
        initComponents();
        configurarVentana();
    }

    private void configurarVentana() {
        setTitle("GoOrder - Historial de Cortes");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

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

        JTextField txtSearch = new JTextField() {
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

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrar(txtSearch.getText()); }
            public void removeUpdate(DocumentEvent e) { filtrar(txtSearch.getText()); }
            public void changedUpdate(DocumentEvent e) { filtrar(txtSearch.getText()); }
        });

        menuFiltros = new JPopupMenu();
        String[] opciones = {"Recientes", "Antiguos", "Ascendente", "Descendente", "Activos", "Cancelados"};
        for (String op : opciones) {
            JMenuItem item = new JMenuItem(op);
            item.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            item.addActionListener(e -> control.filtrarHistorial(op));
            menuFiltros.add(item);
        }

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
                control.eliminarCorte(corteSeleccionado.getId());
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
            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea marcar el folio CC-" + corteSeleccionado.getId() + " como CANCELADO?", "Confirmar Cancelación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                control.cancelarCorte(corteSeleccionado.getId());
            }
        });

        pnlBtnsRightLeft.add(btnEditar);
        pnlBtnsRightLeft.add(btnCancelar);
        pnlFooterRight.add(pnlBtnsRightLeft, BorderLayout.WEST);
        pnlFooterRight.add(new BotonAccion("GENERAR PDF", COLOR_ACCENTO, Color.BLACK, true), BorderLayout.EAST);
        pnlRight.add(pnlFooterRight, BorderLayout.SOUTH);

        add(pnlLeft);
        add(pnlRight);
    }

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
        renderizarCards(this.listaOriginal);
    }

    private void filtrar(String texto) {
        String query = texto.toLowerCase();
        List<corteCajaDTO> filtrada = listaOriginal.stream()
                .filter(c -> c.getCajero().toLowerCase().contains(query) ||
                        String.valueOf(c.getId()).contains(query))
                .collect(Collectors.toList());
        renderizarCards(filtrada);
    }

    private void renderizarCards(List<corteCajaDTO> lista) {
        pnlListaTarjetas.removeAll();
        for (corteCajaDTO c : lista) {
            pnlListaTarjetas.add(new CardCortePremium(c));
            pnlListaTarjetas.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        pnlListaTarjetas.revalidate();
        pnlListaTarjetas.repaint();
    }

    private void actualizarDetalleTicket(corteCajaDTO c) {
        this.corteSeleccionado = c;
        pnlTicketDetalle.removeAll();

        SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "MX"));
        SimpleDateFormat sdfHora = new SimpleDateFormat("hh:mm a", new Locale("es", "MX"));

        String fechaStr = (c.getFecha() != null) ? sdfFecha.format(c.getFecha()) : "S/F";
        String horaCierreStr = (c.getFecha() != null) ? sdfHora.format(c.getFecha()) : "--:--";
        String horaAperturaStr = (c.getFechaApertura() != null) ? sdfHora.format(c.getFechaApertura()) : "--:--";

        boolean isVigente = c.getEstado().equalsIgnoreCase("Vigente");
        String estadoColor = isVigente ? "#2ECC71" : "#8B7369";
        String nombreSupervisor = control.obtenerNombreSupervisorAsociado(c.getIdCajero());

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
                + "<br><i style='color:#777;'>FOLIO DE CORTE: CC-" + c.getId() + "</i><br>"
                + "<br><i style='color:#777;'>CAJERO: " + c.getCajero() + "</i><br>"
                + "<br><i style='color:#777;'>TURNO: (" + horaAperturaStr + " - " + horaCierreStr + ")</i><br><br>"
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
                + "<tr><td style='color:#2ECC71; font-weight:bold; font-style:italic;'>Diferencia:</td>"
                + "<td align='right' style='color:#2ECC71; font-weight:bold;'>$" + String.format("%,.2f", c.getDiferencia()) + "</td></tr>"
                + "</table><br><br><br>"
                + "<table width='100%' style='color:#999;'>"
                + "<tr>"
                + "<td align='center'>_________________<br><br><i>FIRMA<br>" + c.getCajero().toUpperCase() + "</i></td>"
                + "<td align='center'>_________________<br><br><i>FIRMA<br>" + nombreSupervisor.toUpperCase() + "</i></td>"
                + "</tr>"
                + "</table>"
                + "</body></html>";

        txt.setText(htmlTicket);

        JScrollPane scrollPane = new JScrollPane(txt);
        scrollPane.setBorder(null);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);

        pnlTicketDetalle.add(scrollPane, BorderLayout.CENTER);
        pnlTicketDetalle.revalidate();
        pnlTicketDetalle.repaint();
    }

    class CardCortePremium extends JPanel {
        public CardCortePremium(corteCajaDTO c) {
            setOpaque(false);
            setLayout(new BorderLayout(15, 0));
            setBackground(COLOR_CARD_BG);
            setBorder(new EmptyBorder(12, 15, 12, 15));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setMaximumSize(new Dimension(800, 85));

            boolean isVigente = c.getEstado().equalsIgnoreCase("Vigente");

            JLabel lblPill = new JLabel(c.getEstado(), SwingConstants.CENTER) {
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

            JLabel lblCajero = new JLabel(c.getCajero());
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

    class BotonAccion extends JButton {
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