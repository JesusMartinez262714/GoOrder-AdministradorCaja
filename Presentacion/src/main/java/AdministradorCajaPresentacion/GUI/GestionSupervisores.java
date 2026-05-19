package AdministradorCajaPresentacion.GUI;

import AdministradorCajaDTOs.supervisorDTO;
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
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Pantalla principal para la gestión de supervisores en el sistema GoOrder.
 * Permite visualizar la lista de supervisores registrados, realizar búsquedas por nombre,
 * y abrir los formularios para agregar, editar o eliminar registros.
 * * @author Jesus Manuel Martinez Cortez
 * @version 1.0
 */
public class GestionSupervisores extends JFrame {

    private Control control;
    private JPanel pnlListaTarjetas;
    private JTextField txtSearch;

    private List<supervisorDTO> listaOriginal = new ArrayList<>();

    private final Color COLOR_FONDO = new Color(28, 28, 28);
    private final Color COLOR_SIDEBAR = new Color(35, 35, 35);
    private final Color COLOR_CARD = new Color(42, 42, 42);
    private final Color COLOR_ACCENTO = new Color(66, 206, 126);
    private final Color COLOR_TEXTO = Color.WHITE;
    private final Color COLOR_ROJO = new Color(255, 107, 107);
    private final Color COLOR_GRIS = new Color(150, 150, 150);

    /**
     * Constructor que guarda el controlador de la aplicación
     * y manda a llamar la inicialización gráfica de la pantalla.
     * * @param control Instancia del controlador de presentación.
     */
    public GestionSupervisores(Control control) {
        this.control = control;
        configurarVentana();
        initComponents();
    }

    /**
     * Configura el comportamiento general de la ventana, como el tamaño,
     * el centrado en pantalla y la operación de cierre por defecto.
     */
    private void configurarVentana() {
        setTitle("GoOrder - Gestión de Supervisores");
        setSize(1000, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
    }

    /**
     * Inicializa los paneles del menú de navegación lateral, el buscador superior
     * y el área de visualización de tarjetas de supervisores.
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

        JButton btnGestionCajeros = new BotonRedondeado("GestionCajeros", false);
        btnGestionCajeros.addActionListener(e -> control.mostrarGestionCajeros());

        JButton btnGestionSupervisores = new BotonRedondeado("Supervisores", true);

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

        JLabel lblTitulo = new JLabel("GESTIÓN DE SUPERVISORES");
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
            public void insertUpdate(DocumentEvent e) { aplicarFiltroTexto(); }
            public void removeUpdate(DocumentEvent e) { aplicarFiltroTexto(); }
            public void changedUpdate(DocumentEvent e) { aplicarFiltroTexto(); }
        });

        pnlSearchRow.add(txtSearch, BorderLayout.CENTER);

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

        JButton btnAgregar = new BotonAccion("+ AGREGAR SUPERVISOR", COLOR_ACCENTO, Color.BLACK);
        btnAgregar.setPreferredSize(new Dimension(200, 45));
        btnAgregar.addActionListener(e -> {
            new FormularioSupervisor(this, control, null).setVisible(true);
        });

        pnlFooter.add(btnAgregar);
        pnlMain.add(pnlFooter, BorderLayout.SOUTH);

        add(pnlMain, BorderLayout.CENTER);
    }

    /**
     * Guarda la lista completa de supervisores obtenida de la base de datos y manda
     * a llamar la función de filtrado dinámico para refrescar la pantalla.
     * * @param lista Colección de objetos DTO con los supervisores del sistema.
     */
    public void cargarSupervisores(List<supervisorDTO> lista) {
        this.listaOriginal = (lista != null) ? lista : new ArrayList<>();
        aplicarFiltroTexto();
    }

    /**
     * Filtra la lista de supervisores en tiempo real comparando el texto escrito
     * en el buscador con el nombre completo de los registros.
     */
    private void aplicarFiltroTexto() {
        String textoBusqueda = txtSearch.getText().toLowerCase().trim();
        final String query = textoBusqueda.equals("buscar por nombre...") ? "" : textoBusqueda;

        List<supervisorDTO> filtrada = new ArrayList<>(listaOriginal);

        if (!query.isEmpty()) {
            filtrada = filtrada.stream()
                    .filter(s -> s.getNombreCompleto() != null && s.getNombreCompleto().toLowerCase().contains(query))
                    .collect(Collectors.toList());
        }

        pnlListaTarjetas.removeAll();
        for (supervisorDTO s : filtrada) {
            pnlListaTarjetas.add(new CardSupervisor(s));
            pnlListaTarjetas.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        pnlListaTarjetas.revalidate();
        pnlListaTarjetas.repaint();
    }

    /**
     * Filtro asignado al buscador de texto para limitar la cantidad máxima de caracteres
     * permitidos, previniendo inyecciones masivas de texto.
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
     * y operaciones de cada supervisor individual.
     */
    class CardSupervisor extends JPanel {

        /**
         * Configura el diseño visual de la tarjeta de supervisión, sus etiquetas
         * fijas de rol y los listeners de las acciones de edición o eliminación.
         * * @param s Objeto DTO del supervisor correspondiente a la tarjeta.
         */
        public CardSupervisor(supervisorDTO s) {
            setLayout(new BorderLayout(15, 0));
            setOpaque(false);
            setBorder(new EmptyBorder(15, 20, 15, 20));
            setMaximumSize(new Dimension(800, 80));

            JPanel pnlInfo = new JPanel(new GridLayout(2, 1));
            pnlInfo.setOpaque(false);
            JLabel lblNombre = new JLabel(s.getNombreCompleto());
            lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblNombre.setForeground(COLOR_TEXTO);
            JLabel lblRol = new JLabel("Rol: Supervisor");
            lblRol.setForeground(COLOR_GRIS);
            pnlInfo.add(lblNombre);
            pnlInfo.add(lblRol);

            JPanel pnlStatus = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
            pnlStatus.setOpaque(false);
            JLabel lblPill = new JLabel("Activo", SwingConstants.CENTER) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(COLOR_ACCENTO);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            lblPill.setPreferredSize(new Dimension(100, 25));
            lblPill.setForeground(Color.BLACK);
            lblPill.setFont(new Font("Segoe UI", Font.BOLD, 12));
            pnlStatus.add(lblPill);

            JPanel pnlAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
            pnlAcciones.setOpaque(false);

            JButton btnEditar = new BotonAccion("Editar", COLOR_SIDEBAR, COLOR_TEXTO);
            btnEditar.addActionListener(e -> {
                new FormularioSupervisor(GestionSupervisores.this, control, s).setVisible(true);
            });

            JButton btnEliminar = new BotonAccion("Eliminar", COLOR_SIDEBAR, COLOR_ROJO);
            btnEliminar.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "¿Estás seguro de que deseas eliminar a " + s.getNombreCompleto() + "?",
                        "Confirmar Eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        boolean eliminado = control.eliminarSupervisor(s.getIdSupervisor());

                        if (eliminado) {
                            JOptionPane.showMessageDialog(this, "Supervisor eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                        } else {
                            JOptionPane.showMessageDialog(this, "No se pudo eliminar. Verifica que no tenga procesos activos.", "Operación Rechazada", JOptionPane.WARNING_MESSAGE);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Error del sistema: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
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
     * Clase interna para dar estilo visual a los botones de acciones
     * dentro de las tarjetas del catálogo.
     */
    class BotonAccion extends JButton {

        /**
         * Inicializa el botón removiendo los bordes nativos de Java Swing.
         * * @param t Texto del botón.
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
     * Clase interna para estructurar los botones del menú de navegación lateral.
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