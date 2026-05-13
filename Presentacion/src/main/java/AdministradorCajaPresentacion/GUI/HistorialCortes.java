package AdministradorCajaPresentacion.GUI;

import AdministradorCajaDTOs.corteCajaDTO;
import AdministradorCajaDTOs.desgloseDTO;
import AdministradorCajaPresentacion.Control.Control;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class HistorialCortes extends JFrame {

    private Control control;
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
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel pnlSearchRow = new JPanel(new BorderLayout(15, 0));
        pnlSearchRow.setOpaque(false);
        pnlSearchRow.setBorder(new EmptyBorder(15, 0, 20, 0));

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
        txtSearch.setForeground(Color.BLACK);
        txtSearch.setCaretColor(Color.BLACK);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));


        menuFiltros = new JPopupMenu();
        menuFiltros.setBackground(new Color(45, 45, 45));
        menuFiltros.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70)));

        String[] opciones = {"Recientes", "Antiguos", "Ascendente", "Descendente", "Activos", "Cancelados"};

        for (String op : opciones) {
            JMenuItem item = new JMenuItem(op);

            item.setBackground(new Color(45, 45, 45));
            item.setForeground(Color.WHITE);
            item.setFont(new Font("Segoe UI", Font.PLAIN, 13));

            item.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
            item.setPreferredSize(new Dimension(180, 35));
            item.setOpaque(true);

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
                g2.setStroke(new BasicStroke(2f));
                int w = getWidth();
                g2.drawLine(5, 12, w-5, 12); g2.drawOval(10, 8, 8, 8);
                g2.drawLine(5, 22, w-5, 22); g2.drawOval(w-18, 18, 8, 8);
                g2.drawLine(5, 32, w-5, 32); g2.drawOval(10, 28, 8, 8);
                g2.dispose();
            }
        };
        pnlIcon.setPreferredSize(new Dimension(40, 40));
        pnlIcon.setOpaque(false);
        pnlIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pnlIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                menuFiltros.show(pnlIcon, 0, pnlIcon.getHeight());
            }
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
        btnvolver.addActionListener(e ->control.volverAResumen());
        pnlFooterLeft.add(btnvolver);
        JButton btnAgregar = new BotonAccion("Agregar", COLOR_ACCENTO, Color.BLACK, true);
        btnAgregar.addActionListener(e -> {
            control.mostrarFormularioCorte();
        });

        pnlFooterLeft.add(btnAgregar);
        JPanel pnlFooterLeftWrapper = new JPanel(new BorderLayout());
        pnlFooterLeftWrapper.setOpaque(false);
        pnlFooterLeftWrapper.setBorder(new EmptyBorder(20, 0, 0, 0));
        pnlFooterLeftWrapper.add(pnlFooterLeft, BorderLayout.WEST);
        pnlFooterLeftWrapper.add(new BotonAccion("Eliminar", COLOR_ELIMINAR, Color.WHITE, true), BorderLayout.EAST);

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
        pnlBtnsRightLeft.add(new BotonAccion("Editar", COLOR_ACCENTO, Color.BLACK, true));
        pnlBtnsRightLeft.add(new BotonAccion("Cancelar", COLOR_CANCELADO, Color.BLACK, true));

        pnlFooterRight.add(pnlBtnsRightLeft, BorderLayout.WEST);
        BotonAccion btnPdf = new BotonAccion("GENERAR PDF", COLOR_ACCENTO, Color.BLACK, true);
        btnPdf.setPreferredSize(new Dimension(140, 35));
        pnlFooterRight.add(btnPdf, BorderLayout.EAST);

        pnlRight.add(pnlFooterRight, BorderLayout.SOUTH);

        add(pnlLeft);
        add(pnlRight);
    }

    public void llenarTabla(List<corteCajaDTO> lista) {
        pnlListaTarjetas.removeAll();
        for (corteCajaDTO c : lista) {
            pnlListaTarjetas.add(new CardCortePremium(c));
            pnlListaTarjetas.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        pnlListaTarjetas.revalidate();
        pnlListaTarjetas.repaint();
    }

    private void actualizarDetalleTicket(corteCajaDTO c) {
        pnlTicketDetalle.removeAll();

        SimpleDateFormat sdfFecha = new SimpleDateFormat("dd MMM yyyy", new Locale("es", "MX"));
        SimpleDateFormat sdfHora = new SimpleDateFormat("hh:mm a", new Locale("es", "MX"));
        String fechaStr = sdfFecha.format(c.getFecha());
        String horaStr = sdfHora.format(c.getFecha());

        String estadoStr = (c.getDiferencia() == 0) ? "Vigente" : "Cancelado";
        String estadoColor = (c.getDiferencia() == 0) ? "#2ECC71" : "#E74C3C";
        String folio = "CC-2026-" + String.format("%03d", c.getId());

        StringBuilder conceptosHtml = new StringBuilder();


        if (c.getListaDesglose() != null) {
            for (desgloseDTO d : c.getListaDesglose()) {
                if (d.getMontoDeclarado() > 0) {
                    conceptosHtml.append("<tr>")
                            .append("<td style='font-style:italic; padding: 2px 0;'>").append(d.getNombreMetodo()).append("</td>")
                            .append("<td align='right'>$").append(String.format("%,.2f", d.getMontoDeclarado())).append("</td>")
                            .append("</tr>");
                }
            }
        }

        JTextPane txt = new JTextPane();
        txt.setContentType("text/html");
        txt.setEditable(false);
        txt.setBorder(null);


        String html = "<html><body style='font-family: monospace; font-size: 11px; color: #555;'>"
                + "<center>"
                + "<h1 style='color:#2ECC71; font-style:italic; font-size: 28px; margin:0;'>GoOrder</h1>"
                + "<span style='font-style:italic; color:#BDBDBD;'>Sucursal CENTRO - ID: #001</span><br>"
                + "<span style='color:#BDBDBD;'>Fecha: " + fechaStr + " | Hora: " + horaStr + "</span><br>"
                + "<span style='font-style:italic; color:#BDBDBD;'>ESTADO: <b style='color:" + estadoColor + ";'>" + estadoStr.toUpperCase() + "</b></span>"
                + "</center>"
                + "<hr style='border: 0; border-bottom: 2px solid #E0E0E0; margin: 15px 0;'>"
                + "FOLIO: " + folio + "<br><br>"
                + "CAJERO: " + c.getCajero() + "<br><br>"

                + "<table width='100%' style='color:#555; border-collapse: collapse;'>"
                + "<tr><td style='color:#2ECC71; font-style:italic;'>Concepto</td><td align='right' style='color:#2ECC71; font-style:italic;'>Monto</td></tr>"
                + "<tr><td colspan='2'><hr style='border: 0; border-bottom: 2px solid #E0E0E0;'></td></tr>"

                + conceptosHtml.toString()

                + "<tr><td colspan='2'><hr style='border: 0; border-bottom: 2px solid #E0E0E0;'></td></tr>"
                + "<tr><td><b>VENTA BRUTA TOTAL</b></td><td align='right'><b>$" + String.format("%,.2f", c.getMontoReal()) + "</b></td></tr>"
                + "</table><br>"

                + "<div style='background-color:#F5F5F5; border: 1px solid #757575; padding: 10px;'>"
                + "RESUMEN DE ARQUEO:<br>"
                + "<table width='100%' style='color:#555;'>"
                + "<tr><td>Esperado:</td><td align='right'>$" + String.format("%,.2f", c.getMontoEsperado()) + "</td></tr>"
                + "<tr><td>Real:</td><td align='right'>$" + String.format("%,.2f", c.getMontoReal()) + "</td></tr>"
                + "<tr><td style='color:#2ECC71;'>Diferencia:</td><td align='right' style='color:#2ECC71;'>$" + String.format("%,.2f", c.getDiferencia()) + "</td></tr>"
                + "</table></div>"

                + "</body></html>";

        txt.setText(html);
        pnlTicketDetalle.add(txt, BorderLayout.CENTER);
        pnlTicketDetalle.revalidate();
        pnlTicketDetalle.repaint();
    }


    class CardCortePremium extends JPanel {
        public CardCortePremium(corteCajaDTO c) {
            setLayout(new BorderLayout(15, 0));
            setBackground(COLOR_CARD_BG);
            setBorder(new EmptyBorder(15, 20, 15, 20));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setMaximumSize(new Dimension(800, 90));

            JPanel pnlPillContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
            pnlPillContainer.setOpaque(false);
            pnlPillContainer.setPreferredSize(new Dimension(95, 60));

            boolean isVigente = c.getDiferencia() == 0;
            JLabel lblPill = new JLabel(isVigente ? "Vigente" : "Cancelado", SwingConstants.CENTER) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(isVigente ? COLOR_ACCENTO : COLOR_CANCELADO);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            lblPill.setForeground(Color.WHITE);
            lblPill.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblPill.setPreferredSize(new Dimension(85, 30));
            pnlPillContainer.add(lblPill);

            JPanel pnlTextos = new JPanel(new GridLayout(2, 1, 0, 2));
            pnlTextos.setOpaque(false);

            SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM yyyy - hh:mm a", new Locale("es", "MX"));
            String fechaEspanol = sdf.format(c.getFecha());
            JLabel lblFecha = new JLabel(fechaEspanol);
            lblFecha.setFont(new Font("Segoe UI", Font.BOLD, 15));
            lblFecha.setForeground(Color.BLACK);


            JLabel lblNombres = new JLabel(c.getCajero());
            lblNombres.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblNombres.setForeground(Color.BLACK);
            pnlTextos.add(lblFecha);
            pnlTextos.add(lblNombres);

            JLabel lblMonto = new JLabel("$" + c.getMontoReal() + " >");
            lblMonto.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblMonto.setForeground(Color.BLACK);

            add(pnlPillContainer, BorderLayout.WEST);
            add(pnlTextos, BorderLayout.CENTER);
            add(lblMonto, BorderLayout.EAST);

            this.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mousePressed(java.awt.event.MouseEvent e) { actualizarDetalleTicket(c); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            g2.dispose();
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
            setPreferredSize(new Dimension(100, 35));
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