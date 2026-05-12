package AdministradorCajaPresentacion.GUI;

import AdministradorCajaDTOs.cajeroDTO;
import AdministradorCajaDTOs.desgloseDTO;
import AdministradorCajaPresentacion.Control.Control;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ConciliacionFinal extends JFrame {

    private Control control;
    private double esperado, contado;
    private JTextArea txtNotas;
    private cajeroDTO empleado;
    private List<desgloseDTO> desgloses;
    private String rutaImagen;

    private final Color COLOR_FONDO = new Color(34, 34, 34);
    private final Color COLOR_TEXTO = new Color(240, 240, 240);
    private final Color COLOR_CARD = new Color(50, 50, 50);
    private final Color COLOR_CARD_DARK = new Color(25, 25, 25);
    private final Color COLOR_ACCENTO = new Color(45, 212, 112); // Verde suave
    private final Color COLOR_ROJO = new Color(255, 107, 107);   // Rojo suave
    private final Color COLOR_DIVISOR = new Color(85, 85, 85);

    public ConciliacionFinal(Control control, double esperado, double contado, cajeroDTO emp, List<desgloseDTO> desgloses, String rutaImg) {
        this.control = control;
        this.esperado = esperado;
        this.contado = contado;
        this.empleado = emp;
        this.desgloses = desgloses;
        this.rutaImagen = rutaImg;

        initComponents();
        configurarVentana();
    }

    private void configurarVentana() {
        setTitle("GoOrder - Conciliación");
        setSize(650, 720);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel pnlPrincipal = new JPanel();
        pnlPrincipal.setLayout(new BoxLayout(pnlPrincipal, BoxLayout.Y_AXIS));
        pnlPrincipal.setBackground(COLOR_FONDO);
        pnlPrincipal.setBorder(new EmptyBorder(30, 40, 40, 40));

        JPanel pnlTop = new JPanel(new BorderLayout(15, 0));
        pnlTop.setBackground(COLOR_FONDO);
        pnlTop.setMaximumSize(new Dimension(650, 45));

        RoundedButton btnVolver = new RoundedButton("←", 12, COLOR_CARD, COLOR_TEXTO);
        btnVolver.setPreferredSize(new Dimension(40, 40));
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnVolver.addActionListener(e -> {
            this.dispose();
            control.volverAFormulario();
        });

        JLabel lblTitulo = new JLabel("Conciliación Final");
        lblTitulo.setForeground(COLOR_TEXTO);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));

        pnlTop.add(btnVolver, BorderLayout.WEST);
        pnlTop.add(lblTitulo, BorderLayout.CENTER);

        JPanel pnlDivisorHeader = new JPanel();
        pnlDivisorHeader.setBackground(COLOR_DIVISOR);
        pnlDivisorHeader.setMaximumSize(new Dimension(650, 1));

        pnlPrincipal.add(pnlTop);
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));
        pnlPrincipal.add(pnlDivisorHeader);
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel pnlCards = new JPanel(new GridLayout(1, 2, 20, 0));
        pnlCards.setOpaque(false);
        pnlCards.setMaximumSize(new Dimension(650, 100));
        pnlCards.add(new CardMonto("Esperado en Sistema:", esperado, false));
        pnlCards.add(new CardMonto("Contado Físicamente:", contado, true));
        pnlPrincipal.add(pnlCards);
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 30)));

        boolean cuadra = (esperado == contado);
        pnlPrincipal.add(crearPanelResultado(cuadra));
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 30)));

        RoundedButton btnFinalizar = new RoundedButton("FINALIZAR Y CERRAR TURNO", 15, COLOR_ACCENTO, Color.BLACK);
        btnFinalizar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnFinalizar.setPreferredSize(new Dimension(280, 50));

        btnFinalizar.addActionListener(e -> {
            String nota = (txtNotas != null) ? txtNotas.getText() : "Sin observaciones";

            this.dispose();
            control.volverAResumen();
        });

        JPanel pnlBtnFinal = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pnlBtnFinal.setOpaque(false);
        pnlBtnFinal.setMaximumSize(new Dimension(650, 70));
        pnlBtnFinal.add(btnFinalizar);

        pnlPrincipal.add(Box.createVerticalGlue());
        pnlPrincipal.add(pnlBtnFinal);

        JScrollPane scroll = new JScrollPane(pnlPrincipal);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(scroll, BorderLayout.CENTER);
    }

    private JPanel crearPanelResultado(boolean cuadra) {
        JPanel p = new JPanel(new BorderLayout(15, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(cuadra ? new Color(45, 212, 112, 25) : new Color(255, 107, 107, 25));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                g2.setColor(cuadra ? COLOR_ACCENTO : COLOR_ROJO);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 20, 20);

                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(25, 25, 25, 25));
        p.setMaximumSize(new Dimension(650, cuadra ? 130 : 250));

        if (cuadra) {
            JLabel lblIcon = new JLabel("✅");
            lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
            p.add(lblIcon, BorderLayout.WEST);

            JLabel lblMsg = new JLabel("<html><div style='width: 350px;'><b style='font-size:14px; color:white;'>CAJA CUADRADA CORRECTAMENTE</b><br><font color='#A0A0A0' size='4'>Los montos coinciden perfectamente. No se requiere información adicional para proceder.</font></div></html>");
            p.add(lblMsg, BorderLayout.CENTER);
        } else {
            JLabel lblIcon = new JLabel("⚠️");
            lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
            p.add(lblIcon, BorderLayout.WEST);

            double dif = contado - esperado;
            JLabel lblMsg = new JLabel("<html><div style='width: 350px;'><b style='font-size:14px; color:#ff6b6b;'>DIFERENCIA DETECTADA: $" + String.format("%,.2f", dif) + "</b><br><font color='#A0A0A0'>Se requiere un comentario obligatorio justificando la diferencia para proceder con el ajuste.</font></div></html>");
            p.add(lblMsg, BorderLayout.NORTH);

            txtNotas = new JTextArea(4, 20);
            txtNotas.setBackground(COLOR_CARD_DARK);
            txtNotas.setForeground(COLOR_TEXTO);
            txtNotas.setCaretColor(Color.WHITE);
            txtNotas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            txtNotas.setLineWrap(true);
            txtNotas.setWrapStyleWord(true);

            JScrollPane scrollNotas = new JScrollPane(txtNotas);
            scrollNotas.setBorder(null);
            scrollNotas.setOpaque(false);
            scrollNotas.getViewport().setOpaque(false);

            RoundedPanel pnlNotasWrapper = new RoundedPanel(15, COLOR_CARD_DARK);
            pnlNotasWrapper.setLayout(new BorderLayout());
            pnlNotasWrapper.setBorder(new EmptyBorder(10, 15, 10, 15));
            pnlNotasWrapper.add(scrollNotas, BorderLayout.CENTER);

            p.add(pnlNotasWrapper, BorderLayout.CENTER);
        }
        return p;
    }

    class CardMonto extends JPanel {
        private boolean resaltada;

        public CardMonto(String t, double m, boolean resaltada) {
            this.resaltada = resaltada;
            setLayout(new GridLayout(2, 1, 0, 5));
            setOpaque(false);
            setBorder(new EmptyBorder(20, 20, 20, 20));

            JLabel lblT = new JLabel(t);
            lblT.setForeground(new Color(180, 180, 180));
            lblT.setFont(new Font("Segoe UI", Font.BOLD, 13));

            JLabel lblM = new JLabel(String.format("$%,.2f", m));
            lblM.setForeground(COLOR_TEXTO);
            lblM.setFont(new Font("Segoe UI", Font.BOLD, 26));

            add(lblT);
            add(lblM);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(COLOR_CARD);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

            if (resaltada) {
                g2.setColor(esperado == contado ? COLOR_ACCENTO : COLOR_ROJO);
                g2.setStroke(new BasicStroke(2.0f));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 15, 15);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    class RoundedPanel extends JPanel {
        private int radius;
        private Color bgColor;

        public RoundedPanel(int radius, Color bgColor) {
            this.radius = radius;
            this.bgColor = bgColor;
            setOpaque(false);
            setLayout(new BorderLayout());
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    class RoundedButton extends JButton {
        private int radius;
        private Color bgColor;
        private String customText;

        public RoundedButton(String text, int radius, Color bgColor, Color fgColor) {
            super("");
            this.customText = text;
            this.radius = radius;
            this.bgColor = bgColor;
            setForeground(fgColor);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setMargin(new Insets(0, 0, 0, 0));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (getModel().isPressed()) {
                g2.setColor(bgColor.darker());
            } else if (getModel().isRollover()) {
                g2.setColor(bgColor.brighter());
            } else {
                g2.setColor(bgColor);
            }

            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

            if (customText != null) {
                g2.setColor(getForeground());
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(customText)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(customText, x, y);
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }
}