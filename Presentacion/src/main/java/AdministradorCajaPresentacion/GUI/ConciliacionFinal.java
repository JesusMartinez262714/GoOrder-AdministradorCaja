package AdministradorCajaPresentacion.GUI;

import AdministradorCajaDTOs.cajeroDTO;
import AdministradorCajaDTOs.desgloseDTO;
import AdministradorCajaPresentacion.Control.Control;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Pantalla de la interfaz gráfica que sirve para realizar la conciliación final del turno.
 * Compara el dinero esperado por el sistema con el dinero contado físicamente
 * y obliga al supervisor a escribir una justificación si se detectan diferencias.
 * * @author Jesus Manuel Martinez Cortez
 * @version 1.0
 */
public class ConciliacionFinal extends JFrame {

    private Control control;
    private double esperado, contado;
    private JTextArea txtNotas;
    private cajeroDTO empleado;
    private List<desgloseDTO> desgloses;
    private String rutaImagen;
    private int idCorteEditando;

    private final Color COLOR_FONDO = new Color(34, 34, 34);
    private final Color COLOR_TEXTO = new Color(240, 240, 240);
    private final Color COLOR_CARD = new Color(50, 50, 50);
    private final Color COLOR_CARD_DARK = new Color(25, 25, 25);
    private final Color COLOR_ACCENTO = new Color(45, 212, 112);
    private final Color COLOR_ROJO = new Color(255, 107, 107);
    private final Color COLOR_DIVISOR = new Color(85, 85, 85);

    /**
     * Constructor que inicializa los montos esperado y contado, los datos del cajero
     * y la lista de desgloses para proceder con la validación del cierre.
     * * @param control Instancia del controlador para la navegación de pantallas.
     * @param esperado Monto total que el sistema calcula que debería haber en caja.
     * @param contado Monto físico que el usuario ingresó manualmente en el desglose.
     * @param emp Datos del cajero asociado al corte actual.
     * @param desgloses Lista de los métodos de pago declarados con sus cantidades.
     * @param rutaImg Ruta del archivo de imagen del comprobante adjunto.
     * @param idCorteEditando ID del corte si es una edición, o -1 si es un registro nuevo.
     */
    public ConciliacionFinal(Control control, double esperado, double contado, cajeroDTO emp, List<desgloseDTO> desgloses, String rutaImg, int idCorteEditando) {
        super();
        this.control = control;
        this.esperado = esperado;
        this.contado = contado;
        this.empleado = emp;
        this.desgloses = desgloses;
        this.rutaImagen = rutaImg;
        this.idCorteEditando = idCorteEditando;

        initComponents();
        configurarVentana();
    }

    /**
     * Configura las propiedades básicas de la ventana de conciliación final
     * como tamaño, posición y título del marco.
     */
    private void configurarVentana() {
        setTitle("GoOrder - Conciliación Final");
        setSize(650, 720);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
    }

    /**
     * Inicializa y organiza los componentes visuales de la interfaz de usuario,
     * configurando el diseño, las etiquetas de montos y los botones de acción.
     */
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

        boolean cuadra = (Double.compare(esperado, contado) == 0);
        pnlPrincipal.add(crearPanelResultado(cuadra));
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 30)));

        RoundedButton btnFinalizar = new RoundedButton("FINALIZAR Y CERRAR TURNO", 15, COLOR_ACCENTO, Color.BLACK);
        btnFinalizar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnFinalizar.setPreferredSize(new Dimension(280, 50));

        btnFinalizar.addActionListener(e -> {
            String nota = "Sin observaciones";
            if (!cuadra) {
                if (txtNotas == null) {
                    JOptionPane.showMessageDialog(this, "Error crítico de inicialización en el campo de observaciones.", "Error Interno", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                nota = txtNotas.getText().trim().replaceAll("\\s+", " ");

                if (nota.isEmpty() || nota.equalsIgnoreCase("Escribe el motivo de la diferencia aquí...")) {
                    JOptionPane.showMessageDialog(this, "Es obligatorio justificar la diferencia para poder cerrar la caja.", "Justificación Requerida", JOptionPane.WARNING_MESSAGE);
                    txtNotas.requestFocus();
                    return;
                }

                if (nota.length() < 10) {
                    JOptionPane.showMessageDialog(this, "La justificación es demasiado corta. Proporcione una explicación clara (mínimo 10 caracteres).", "Contenido Insuficiente", JOptionPane.WARNING_MESSAGE);
                    txtNotas.requestFocus();
                    return;
                }

                String regexLetrasSuficientes = ".*[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ]{4,}.*";
                String regexBypassSimbolos = "^[\\p{Punct}\\s]+$";
                if (nota.matches(regexBypassSimbolos) || !nota.matches(regexLetrasSuficientes)) {
                    JOptionPane.showMessageDialog(this, "La justificación contiene caracteres inválidos o carece de palabras descriptivas reales.", "Texto Inválido", JOptionPane.ERROR_MESSAGE);
                    txtNotas.requestFocus();
                    return;
                }
            }

            control.guardarCorteFinal(esperado, contado, empleado.getIdCajero(), desgloses, rutaImagen, nota, idCorteEditando);
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

    /**
     * Crea un panel dinámico que muestra si la caja está cuadrada o si hay diferencias,
     * agregando un campo de texto JTextArea para notas si hace falta justificar datos.
     * * @param cuadra Determina si el monto esperado y el contado coinciden.
     * @return El panel JPanel estructurado con el mensaje informativo de la conciliación.
     */
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
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 20, 20);

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
            String mensajeDif = "<html><div style='width: 350px;'><b style='font-size:14px; color:#ff6b6b;'>DIFERENCIA DETECTADA: $" + String.format("%,.2f", dif) + "</b><br><font color='#A0A0A0'>Se requiere un comentario obligatorio justificando la diferencia para proceder con el ajuste.</font></div></html>";
            JLabel lblMsg = new JLabel(mensajeDif);
            p.add(lblMsg, BorderLayout.NORTH);

            txtNotas = new JTextArea(4, 20);
            txtNotas.setBackground(COLOR_CARD_DARK);
            txtNotas.setForeground(COLOR_TEXTO);
            txtNotas.setCaretColor(Color.WHITE);
            txtNotas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            txtNotas.setLineWrap(true);
            txtNotas.setWrapStyleWord(true);

            ((AbstractDocument) txtNotas.getDocument()).setDocumentFilter(new FiltroNotasMaximo());

            txtNotas.setText("Escribe el motivo de la diferencia aquí...");
            txtNotas.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    if (txtNotas.getText().equals("Escribe el motivo de la diferencia aquí...")) {
                        txtNotas.setText("");
                    }
                }
                public void focusLost(java.awt.event.FocusEvent evt) {
                    if (txtNotas.getText().trim().isEmpty()) {
                        txtNotas.setText("Escribe el motivo de la diferencia aquí...");
                    }
                }
            });

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

    /**
     * Clase interna utilizada para pintar tarjetas visuales en pantalla
     * que muestran los montos de dinero de forma clara y estilizada.
     */
    class CardMonto extends JPanel {
        private boolean resaltada;

        /**
         * Configura el texto descriptivo y el formato numérico del monto de la tarjeta.
         * * @param t Título o descripción del monto a mostrar.
         * @param m Cantidad numérica en formato double.
         * @param resaltada Flag para añadir un borde de color destacado a la tarjeta.
         */
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
                g2.setColor(Double.compare(esperado, contado) == 0 ? COLOR_ACCENTO : COLOR_ROJO);
                g2.setStroke(new BasicStroke(2.0f));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 15, 15);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * Filtro para el JTextArea de notas que limita el número de caracteres permitidos,
     * evitando que el usuario sature el campo de texto o ingrese descripciones vacías.
     */
    private class FiltroNotasMaximo extends DocumentFilter {
        private final int caracteresMaximos = 200;

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string == null) return;
            if ((fb.getDocument().getLength() + string.length()) <= caracteresMaximos) {
                super.insertString(fb, offset, string, attr);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null) return;
            if ((fb.getDocument().getLength() - length + text.length()) <= caracteresMaximos) {
                super.replace(fb, offset, length, text, attrs);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    /**
     * Panel personalizado que permite redondear las esquinas de los contenedores
     * utilizando suavizado de bordes con Graphics2D.
     */
    class RoundedPanel extends JPanel {
        private int radius;
        private Color bgColor;

        /**
         * Inicializa las propiedades de curvatura y fondo del panel redondeado.
         * * @param radius Radio para redondear los bordes del contenedor.
         * @param bgColor Color de fondo para rellenar el panel.
         */
        public RoundedPanel(int radius, Color bgColor) {
            this.radius = radius;
            this.bgColor = bgColor;
            setOpaque(false);
            setLayout(new BorderLayout());
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * Botón personalizado con esquinas redondeadas y soporte para efectos visuales
     * cuando el mouse pasa por encima o cuando se presiona el botón.
     */
    class RoundedButton extends JButton {
        private int radius;
        private Color bgColor;
        private String customText;

        /**
         * Configura el texto, fuentes, colores y desactiva los bordes nativos de Swing.
         * * @param text Texto que mostrará el botón en pantalla.
         * @param radius Radio para suavizar las esquinas del componente.
         * @param bgColor Color del fondo base del botón.
         * @param fgColor Color asignado al texto del botón.
         */
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

        @Override protected void paintComponent(Graphics g) {
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