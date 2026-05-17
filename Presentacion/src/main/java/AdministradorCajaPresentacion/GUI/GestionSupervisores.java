package AdministradorCajaPresentacion.GUI;

import AdministradorCajaDTOs.supervisorDTO;
import AdministradorCajaPresentacion.Control.Control;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class GestionSupervisores extends JFrame {

    private Control control;
    private JPanel pnlListaTarjetas;
    private JTextField txtSearch;

    private final Color COLOR_FONDO = new Color(28, 28, 28);
    private final Color COLOR_SIDEBAR = new Color(35, 35, 35);
    private final Color COLOR_CARD = new Color(42, 42, 42);
    private final Color COLOR_ACCENTO = new Color(66, 206, 126);
    private final Color COLOR_TEXTO = Color.WHITE;
    private final Color COLOR_ROJO = new Color(255, 107, 107);
    private final Color COLOR_GRIS = new Color(150, 150, 150);

    public GestionSupervisores(Control control) {
        this.control = control;
        configurarVentana();
        initComponents();
    }

    private void configurarVentana() {
        setTitle("GoOrder - Gestión de Supervisores");
        setSize(1000, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
    }

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

        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String busqueda = txtSearch.getText().equals("Buscar por nombre...") ? "" : txtSearch.getText();
                    control.filtrarSupervisoresLista(busqueda);
                }
            }
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

    public void cargarSupervisores(List<supervisorDTO> lista) {
        pnlListaTarjetas.removeAll();
        if (lista != null) {
            for (supervisorDTO s : lista) {
                pnlListaTarjetas.add(new CardSupervisor(s));
                pnlListaTarjetas.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }
        pnlListaTarjetas.revalidate();
        pnlListaTarjetas.repaint();
    }

    class CardSupervisor extends JPanel {
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
                int confirm = JOptionPane.showConfirmDialog(null, "¿Eliminar a " + s.getNombreCompleto() + "?");
                if (confirm == JOptionPane.YES_OPTION) control.eliminarSupervisor(s.getIdSupervisor());
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

    class BotonAccion extends JButton {
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

    class BotonRedondeado extends JButton {
        private boolean destacado;
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