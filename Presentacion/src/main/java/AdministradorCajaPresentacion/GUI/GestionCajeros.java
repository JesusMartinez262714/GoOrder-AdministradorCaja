package AdministradorCajaPresentacion.GUI;

import AdministradorCajaDTOs.cajeroDTO;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Pantalla principal para la gestión de cajeros en el sistema GoOrder.
 * Permite visualizar la lista de cajeros activos, buscar por nombre,
 * ordenar por niveles de adeudo y realizar abonos o eliminar registros.
 * * @author Jesus Manuel Martinez Cortez
 * @version 1.0
 */
public class GestionCajeros extends JFrame {

    private Control control;
    private JPanel pnlListaTarjetas;
    private JPopupMenu menuFiltros;
    private JTextField txtSearch;

    private List<cajeroDTO> listaOriginal = new ArrayList<>();
    private JCheckBoxMenuItem chkMayorAdeudo;
    private JCheckBoxMenuItem chkMenorAdeudo;

    private final Color COLOR_FONDO = new Color(28, 28, 28);
    private final Color COLOR_SIDEBAR = new Color(35, 35, 35);
    private final Color COLOR_CARD = new Color(42, 42, 42);
    private final Color COLOR_ACCENTO = new Color(66, 206, 126);
    private final Color COLOR_TEXTO = Color.WHITE;
    private final Color COLOR_ROJO = new Color(255, 107, 107);
    private final Color COLOR_GRIS = new Color(150, 150, 150);
    private final Color COLOR_PAGO = new Color(52, 152, 219);

    /**
     * Constructor que asigna el controlador principal de la ventana
     * y genera la estructura visual de la pantalla.
     * * @param control Instancia del controlador de presentación.
     */
    public GestionCajeros(Control control) {
        this.control = control;
        configurarVentana();
        initComponents();
    }

    /**
     * Configura el comportamiento general de la ventana, como el tamaño,
     * el centrado en pantalla y la operación de cierre por defecto.
     */
    private void configurarVentana() {
        setTitle("GoOrder - Gestión de Cajeros");
        setSize(1000, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
    }

    /**
     * Inicializa los paneles del menú de navegación lateral, el buscador superior,
     * el menú desplegable de filtros y el área de tarjetas informativas.
     */
    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel pnlSidebar = new JPanel();
        pnlSidebar.setBackground(COLOR_SIDEBAR);
        pnlSidebar.setPreferredSize(new Dimension(250, 0));
        pnlSidebar.setLayout(new BoxLayout(pnlSidebar, BoxLayout.Y_AXIS));
        pnlSidebar.setBorder(new EmptyBorder(30, 20, 30, 20));

        JLabel lblLogo = new JLabel("GoOrder");
        lblLogo.setForeground(COLOR_ACCENTO);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlSidebar.add(lblLogo);
        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 40)));

        JButton btnCajaTurno = new BotonRedondeado("Caja/Turno", false);
        btnCajaTurno.addActionListener(e -> control.volverAResumen());

        JButton btnAperturaCaja = new BotonRedondeado("AperturaCaja", false);
        btnAperturaCaja.addActionListener(e -> control.mostrarAperturaCaja());

        JButton btnGestionCajeros = new BotonRedondeado("GestionCajeros", true);

        JButton btnGestionSupervisores = new BotonRedondeado("Supervisores", false);
        btnGestionSupervisores.addActionListener(e -> control.mostrarGestionSupervisores());

        pnlSidebar.add(btnCajaTurno);
        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        pnlSidebar.add(btnAperturaCaja);
        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 40)));
        pnlSidebar.add(btnGestionCajeros);
        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        pnlSidebar.add(btnGestionSupervisores);
        pnlSidebar.add(Box.createVerticalGlue());

        add(pnlSidebar, BorderLayout.WEST);

        JPanel pnlMain = new JPanel(new BorderLayout());
        pnlMain.setBackground(COLOR_FONDO);
        pnlMain.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel pnlHeader = new JPanel();
        pnlHeader.setLayout(new BoxLayout(pnlHeader, BoxLayout.Y_AXIS));
        pnlHeader.setOpaque(false);

        JLabel lblTitulo = new JLabel("GESTIÓN DE CAJEROS");
        lblTitulo.setForeground(COLOR_TEXTO);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel pnlSearchRow = new JPanel(new BorderLayout(15, 0));
        pnlSearchRow.setOpaque(false);
        pnlSearchRow.setBorder(new EmptyBorder(20, 0, 20, 0));

        txtSearch = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
            }
        };
        txtSearch.setOpaque(false);
        txtSearch.setBorder(new EmptyBorder(10, 15, 10, 15));
        txtSearch.setForeground(COLOR_TEXTO);
        txtSearch.setCaretColor(COLOR_TEXTO);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setText("Buscar por nombre...");

        txtSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtSearch.getText().equals("Buscar por nombre...")) txtSearch.setText("");
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtSearch.getText().isEmpty()) txtSearch.setText("Buscar por nombre...");
            }
        });

        ((AbstractDocument) txtSearch.getDocument()).setDocumentFilter(new FiltroBusquedaMaximo());

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { aplicarFiltrosCombinados(); }
            public void removeUpdate(DocumentEvent e) { aplicarFiltrosCombinados(); }
            public void changedUpdate(DocumentEvent e) { aplicarFiltrosCombinados(); }
        });

        menuFiltros = new JPopupMenu();
        chkMayorAdeudo = new JCheckBoxMenuItem("Mayor Adeudo primero", false);
        chkMenorAdeudo = new JCheckBoxMenuItem("Menor Adeudo primero", false);

        chkMayorAdeudo.addActionListener(e -> {
            if (chkMayorAdeudo.isSelected()) chkMenorAdeudo.setSelected(false);
            aplicarFiltrosCombinados();
        });
        chkMenorAdeudo.addActionListener(e -> {
            if (chkMenorAdeudo.isSelected()) chkMayorAdeudo.setSelected(false);
            aplicarFiltrosCombinados();
        });

        menuFiltros.add(new JLabel("  ORDENAR POR ADEUDOS:"));
        menuFiltros.add(chkMayorAdeudo);
        menuFiltros.add(chkMenorAdeudo);

        JButton btnFiltro = new BotonAccion("Filtros ▼", COLOR_CARD, COLOR_TEXTO);
        btnFiltro.setPreferredSize(new Dimension(100, 40));
        btnFiltro.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { menuFiltros.show(btnFiltro, 0, btnFiltro.getHeight()); }
        });

        pnlSearchRow.add(txtSearch, BorderLayout.CENTER);
        pnlSearchRow.add(btnFiltro, BorderLayout.EAST);

        pnlHeader.add(lblTitulo);
        pnlHeader.add(pnlSearchRow);
        pnlMain.add(pnlHeader, BorderLayout.NORTH);

        pnlListaTarjetas = new JPanel();
        pnlListaTarjetas.setLayout(new BoxLayout(pnlListaTarjetas, BoxLayout.Y_AXIS));
        pnlListaTarjetas.setOpaque(false);

        JScrollPane scroll = new JScrollPane(pnlListaTarjetas);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        pnlMain.add(scroll, BorderLayout.CENTER);

        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pnlFooter.setOpaque(false);
        pnlFooter.setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton btnAgregar = new BotonAccion("+ AGREGAR CAJERO", COLOR_ACCENTO, Color.BLACK);
        btnAgregar.setPreferredSize(new Dimension(180, 45));
        btnAgregar.addActionListener(e -> new FormularioCajero(this, control, null).setVisible(true));

        pnlFooter.add(btnAgregar);
        pnlMain.add(pnlFooter, BorderLayout.SOUTH);

        add(pnlMain, BorderLayout.CENTER);
    }

    /**
     * Guarda la lista completa recibida de la base de datos y manda a llamar
     * al método para organizar y pintar los cajeros en pantalla.
     * * @param lista Colección de objetos DTO con los cajeros del sistema.
     */
    public void cargarCajeros(List<cajeroDTO> lista) {
        this.listaOriginal = (lista != null) ? lista : new ArrayList<>();
        aplicarFiltrosCombinados();
    }

    /**
     * Aplica en tiempo real la coincidencia de texto del buscador y los estados
     * de ordenamiento seleccionados en el menú para reconstruir las tarjetas.
     */
    private void aplicarFiltrosCombinados() {
        String textoBusqueda = txtSearch.getText().toLowerCase().trim();
        final String query = textoBusqueda.equals("buscar por nombre...") ? "" : textoBusqueda;

        List<cajeroDTO> filtrada = new ArrayList<>(listaOriginal);

        if (!query.isEmpty()) {
            filtrada = filtrada.stream()
                    .filter(c -> c.getNombreCompleto() != null && c.getNombreCompleto().toLowerCase().contains(query))
                    .collect(Collectors.toList());
        }

        if (chkMayorAdeudo.isSelected()) {
            filtrada.sort((c1, c2) -> Double.compare(c2.getMontoAdeudo(), c1.getMontoAdeudo()));
        } else if (chkMenorAdeudo.isSelected()) {
            filtrada.sort((c1, c2) -> Double.compare(c1.getMontoAdeudo(), c2.getMontoAdeudo()));
        }

        pnlListaTarjetas.removeAll();
        for (cajeroDTO c : filtrada) {
            pnlListaTarjetas.add(new CardCajero(c));
            pnlListaTarjetas.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        pnlListaTarjetas.revalidate();
        pnlListaTarjetas.repaint();
    }

    /**
     * Filtro asignado al buscador de texto para limitar la cantidad máxima de caracteres
     * permitidos, previniendo inyecciones de texto masivas.
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
     * Subcomponente visual interno que representa la tarjeta de información
     * y operaciones individuales de cada cajero.
     */
    class CardCajero extends JPanel {

        /**
         * Configura el diseño visual de la tarjeta del empleado, sus indicadores
         * de adeudo y las acciones de pago, edición y eliminación.
         * * @param c Objeto DTO del cajero correspondiente a la tarjeta.
         */
        public CardCajero(cajeroDTO c) {
            setLayout(new BorderLayout(15, 0));
            setOpaque(false);
            setBorder(new EmptyBorder(15, 20, 15, 20));
            setMaximumSize(new Dimension(800, 80));

            JPanel pnlInfo = new JPanel(new GridLayout(2, 1));
            pnlInfo.setOpaque(false);
            JLabel lblNombre = new JLabel(c.getNombreCompleto());
            lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblNombre.setForeground(COLOR_TEXTO);
            JLabel lblTurno = new JLabel("Turno: " + c.getTurno());
            lblTurno.setForeground(COLOR_GRIS);
            pnlInfo.add(lblNombre);
            pnlInfo.add(lblTurno);

            JPanel pnlStatus = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
            pnlStatus.setOpaque(false);

            String textoBadge = c.isTieneAdeudo() ? "Debe: $" + String.format("%,.2f", c.getMontoAdeudo()) : "Al Corriente";
            JLabel lblPill = new JLabel(textoBadge, SwingConstants.CENTER) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(c.isTieneAdeudo() ? COLOR_ROJO : COLOR_ACCENTO);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            lblPill.setPreferredSize(new Dimension(140, 25));
            lblPill.setForeground(c.isTieneAdeudo() ? Color.WHITE : Color.BLACK);
            lblPill.setFont(new Font("Segoe UI", Font.BOLD, 12));
            pnlStatus.add(lblPill);

            JPanel pnlAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
            pnlAcciones.setOpaque(false);

            if (c.isTieneAdeudo()) {
                JButton btnPagar = new BotonAccion("Pagar", COLOR_PAGO, Color.WHITE);
                btnPagar.addActionListener(e -> {
                    String input = JOptionPane.showInputDialog(GestionCajeros.this,
                            "Monto a abonar/pagar (Adeudo actual: $" + String.format("%,.2f", c.getMontoAdeudo()) + "):",
                            "Registrar Pago de Adeudo", JOptionPane.QUESTION_MESSAGE);
                    if (input != null) {
                        String inputLimpio = input.trim();
                        if (!inputLimpio.isEmpty()) {
                            try {
                                double monto = Double.parseDouble(inputLimpio);
                                if (monto <= 0) {
                                    JOptionPane.showMessageDialog(GestionCajeros.this, "El monto a pagar debe ser mayor a cero.", "Monto Inválido", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                                if (monto > c.getMontoAdeudo()) {
                                    JOptionPane.showMessageDialog(GestionCajeros.this, "El abono no puede ser mayor al adeudo actual del cajero.", "Monto Excedido", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                                control.procesarPagoAdeudo(c.getIdCajero(), monto);
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(GestionCajeros.this, "Por favor ingrese un monto numérico válido.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                });
                pnlAcciones.add(btnPagar);
            }

            JButton btnEditar = new BotonAccion("Editar", COLOR_SIDEBAR, COLOR_TEXTO);
            btnEditar.addActionListener(e -> new FormularioCajero(GestionCajeros.this, control, c).setVisible(true));

            JButton btnEliminar = new BotonAccion("Eliminar", COLOR_SIDEBAR, COLOR_ROJO);
            btnEliminar.addActionListener(e -> {
                if (c.isTieneAdeudo()) {
                    JOptionPane.showMessageDialog(null, "No se puede eliminar: El cajero tiene adeudos pendientes.");
                } else {
                    int confirm = JOptionPane.showConfirmDialog(null, "¿Eliminar a " + c.getNombreCompleto() + "?");
                    if (confirm == JOptionPane.YES_OPTION) control.eliminarCajero(c.getIdCajero());
                }
            });

            pnlAcciones.add(btnEditar);
            pnlAcciones.add(btnEliminar);

            add(pnlInfo, BorderLayout.WEST);
            add(pnlStatus, BorderLayout.CENTER);
            add(pnlAcciones, BorderLayout.EAST);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(COLOR_CARD);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            g2.dispose();
        }
    }

    /**
     * Clase interna para dar estilo visual estilizado a los botones de acciones
     * dentro de las tarjetas del catálogo.
     */
    class BotonAccion extends JButton {

        /**
         * Inicializa el botón removiendo los bordes nativos de Java Swing.
         * * @param t Texto instructivo de la acción.
         * @param bg Color de fondo.
         * @param fg Color de las fuentes.
         */
        public BotonAccion(String t, Color bg, Color fg) {
            super(t);
            setBackground(bg);
            setForeground(fg);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            super.paintComponent(g);
        }
    }

    /**
     * Clase interna para estructurar los botones redondos del menú de navegación lateral.
     */
    class BotonRedondeado extends JButton {
        private boolean destacado;

        /**
         * Asigna la tipografía y los flags visuales del diseño del menú.
         * * @param t Texto del botón.
         * @param d Flag que determina si el botón usa el color de realce activo.
         */
        public BotonRedondeado(String t, boolean d) {
            super(t);
            this.destacado = d;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setForeground(d ? Color.BLACK : Color.GRAY);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setMaximumSize(new Dimension(220, 45));
            setAlignmentX(Component.CENTER_ALIGNMENT);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(destacado ? COLOR_ACCENTO : new Color(50, 50, 50));
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
            super.paintComponent(g);
        }
    }
}