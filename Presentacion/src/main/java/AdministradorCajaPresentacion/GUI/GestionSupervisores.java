package AdministradorCajaPresentacion.GUI;

import AdministradorCajaDTOs.supervisorDTO;
import AdministradorCajaPresentacion.Control.Control;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class GestionSupervisores extends JFrame {
    private Control control;
    private JPanel pnlListaTarjetas;
    private final Color COLOR_FONDO = new Color(28, 28, 28), COLOR_SIDEBAR = new Color(35, 35, 35);
    private final Color COLOR_CARD = new Color(42, 42, 42), COLOR_ACCENTO = new Color(66, 206, 126);

    public GestionSupervisores(Control control) {
        this.control = control;
        initComponents();
        setTitle("GoOrder - Gestión de Supervisores");
        setSize(1000, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel pnlSidebar = new JPanel() {{ setBackground(COLOR_SIDEBAR);
            setPreferredSize(new Dimension(250, 0)); setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); setBorder(new EmptyBorder(30, 20, 30, 20)); }};
        pnlSidebar.add(new JLabel("GoOrder") {{ setForeground(COLOR_ACCENTO);
            setFont(new Font("Segoe UI", Font.BOLD, 28)); setAlignmentX(CENTER_ALIGNMENT); }});
        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 40)));
        String[] btns = {"Caja/Turno", "AperturaCaja", "GestionCajeros", "Supervisores"};
        for(String b : btns) {
            JButton btn = new BotonRedondeado(b, b.equals("Supervisores"));
            btn.addActionListener(e -> { if(b.contains("Caja")) control.volverAResumen();
                else if(b.contains("Aper")) control.mostrarAperturaCaja(); else if(b.contains("Cajer")) control.mostrarGestionCajeros(); });
            pnlSidebar.add(btn); pnlSidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        }
        add(pnlSidebar, BorderLayout.WEST);

        JPanel pnlMain = new JPanel(new BorderLayout()) {{ setBackground(COLOR_FONDO);
            setBorder(new EmptyBorder(30, 40, 30, 40)); }};
        JTextField txtSearch = new JTextField("Buscar por nombre...") {
            @Override protected void paintComponent(Graphics g) { Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(COLOR_CARD); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15); super.paintComponent(g); }
        };
        txtSearch.setOpaque(false); txtSearch.setBorder(new EmptyBorder(10, 15, 10, 15));
        txtSearch.setForeground(Color.WHITE);
        txtSearch.addFocusListener(new java.awt.event.FocusAdapter() { public void focusGained(java.awt.event.FocusEvent e)
        { if(txtSearch.getText().contains("Buscar")) txtSearch.setText(""); } });
        txtSearch.addActionListener(e -> control.filtrarSupervisoresLista(txtSearch.getText()));

        pnlMain.add(new JPanel() {{ setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); setOpaque(false);
            add(new JLabel("GESTIÓN DE SUPERVISORES") {{ setForeground(Color.WHITE); setFont(new Font("Segoe UI", Font.BOLD, 22)); }}); add(Box.createRigidArea(new Dimension(0, 20))); add(txtSearch); }}, BorderLayout.NORTH);
        pnlListaTarjetas = new JPanel() {{ setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); setOpaque(false); }};
        pnlMain.add(new JScrollPane(pnlListaTarjetas) {{ setBorder(null); setOpaque(false); getViewport().setOpaque(false); }},
                BorderLayout.CENTER);
        pnlMain.add(new JButton("+ AGREGAR SUPERVISOR") {{ setBackground(COLOR_ACCENTO); setFont(new Font("Segoe UI",
                Font.BOLD, 13)); addActionListener(e ->
                new FormularioSupervisor(GestionSupervisores.this, control, null).setVisible(true)); }}, BorderLayout.SOUTH);
        add(pnlMain, BorderLayout.CENTER);
    }

    public void cargarSupervisores(List<supervisorDTO> l) {
        pnlListaTarjetas.removeAll();
        if(l != null) l.forEach(s -> { pnlListaTarjetas.add(new CardSupervisor(s));
            pnlListaTarjetas.add(Box.createRigidArea(new Dimension(0, 15))); });
        pnlListaTarjetas.revalidate(); pnlListaTarjetas.repaint();
    }

    class CardSupervisor extends JPanel {
        public CardSupervisor(supervisorDTO s) {
            setLayout(new BorderLayout(15, 0)); setOpaque(false);
            setBorder(new EmptyBorder(15, 20, 15, 20)); setMaximumSize(new Dimension(800, 80));
            add(new JLabel(s.getNombreCompleto()) {{ setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 16)); }}, BorderLayout.WEST);
            JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT)) {{ setOpaque(false); }};
            p.add(new JButton("Editar") {{ addActionListener(e ->
                    new FormularioSupervisor(GestionSupervisores.this, control, s).setVisible(true)); }});
            p.add(new JButton("Eliminar") {{ setForeground(Color.RED); addActionListener(e ->
                    control.eliminarSupervisor(s.getIdSupervisor())); }});
            add(p, BorderLayout.EAST);
        }
        @Override protected void paintComponent(Graphics g) { Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(COLOR_CARD); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15); }
    }

    class BotonRedondeado extends JButton {
        private boolean d;
        public BotonRedondeado(String t, boolean d) { super(t); this.d = d; setContentAreaFilled(false);
            setBorderPainted(false); setForeground(d?Color.BLACK:Color.GRAY); setFont(new Font("Segoe UI", Font.BOLD, 14)); setMaximumSize(new Dimension(220, 45)); setAlignmentX(CENTER_ALIGNMENT); }
        @Override protected void paintComponent(Graphics g) { Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(d?COLOR_ACCENTO:new Color(50, 50, 50)); g2.fillRoundRect(0, 0, getWidth(), getHeight(),
                    15, 15); super.paintComponent(g); }
    }
}