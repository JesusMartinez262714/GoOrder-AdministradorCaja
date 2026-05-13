package AdministradorCajaPresentacion.GUI;

import AdministradorCajaDTOs.aperturaCajaDTO;
import AdministradorCajaDTOs.cajeroDTO;
import AdministradorCajaDTOs.supervisorDTO;
import AdministradorCajaPresentacion.Control.Control;
import org.apache.commons.validator.GenericValidator;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AperturaCaja extends JFrame {

    private Control control;
    private JComboBox<supervisorDTO> cmbSupervisores;
    private JComboBox<cajeroDTO> cmbCajeros;
    private JTextField txtMontoInicial;

    private final Color COLOR_FONDO = new Color(28, 28, 28);
    private final Color COLOR_CARD = new Color(42, 42, 42);
    private final Color COLOR_ACCENTO = new Color(66, 206, 126);

    public AperturaCaja(Control control) {
        this.control = control;
        initComponents();
        configurarVentana();
    }

    private void configurarVentana() {
        setTitle("GoOrder - Apertura de Caja");
        setSize(1000, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel pnlSidebar = new JPanel();
        pnlSidebar.setBackground(new Color(35, 35, 35));
        pnlSidebar.setPreferredSize(new Dimension(250, 0));
        pnlSidebar.setLayout(new BoxLayout(pnlSidebar, BoxLayout.Y_AXIS));
        pnlSidebar.setBorder(new EmptyBorder(30, 20, 30, 20));

        JLabel lblLogo = new JLabel("GoOrder");
        lblLogo.setForeground(COLOR_ACCENTO);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 28));

        pnlSidebar.add(lblLogo);
        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 40)));

        JButton btnCajaTurno = new BotonRedondeado("Caja/Turno", false);
        btnCajaTurno.addActionListener(e -> control.mostrarResumenTurno(1));
        pnlSidebar.add(btnCajaTurno);
        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton btnAperturaCaja = new BotonRedondeado("AperturaCaja", true);
        pnlSidebar.add(btnAperturaCaja);
        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        pnlSidebar.add(new BotonRedondeado("GestionAdeudos", false));
        pnlSidebar.add(Box.createVerticalGlue());

        add(pnlSidebar, BorderLayout.WEST);

        JPanel pnlMain = new JPanel(new GridBagLayout());
        pnlMain.setBackground(COLOR_FONDO);

        RoundedPanel cardApertura = new RoundedPanel(30, COLOR_CARD);
        cardApertura.setPreferredSize(new Dimension(500, 480));
        cardApertura.setLayout(new BoxLayout(cardApertura, BoxLayout.Y_AXIS));
        cardApertura.setBorder(new EmptyBorder(35, 50, 35, 50));

        JLabel lblTitulo = new JLabel("Nueva Apertura");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        cmbSupervisores = new JComboBox<>();
        cmbCajeros = new JComboBox<>();
        cmbCajeros.addActionListener(e -> autoseleccionarSupervisor());
        txtMontoInicial = new JTextField("0.00");
        estilizarCampo(txtMontoInicial);

        cardApertura.add(lblTitulo);
        cardApertura.add(Box.createRigidArea(new Dimension(0, 30)));

        cardApertura.add(crearEtiqueta("Seleccionar Supervisor:"));
        cardApertura.add(cmbSupervisores);
        cardApertura.add(Box.createRigidArea(new Dimension(0, 20)));

        cardApertura.add(crearEtiqueta("Seleccionar Cajero (Empleado):"));
        cardApertura.add(cmbCajeros);
        cardApertura.add(Box.createRigidArea(new Dimension(0, 20)));

        cardApertura.add(crearEtiqueta("Monto Inicial en Caja (Fondo):"));
        cardApertura.add(txtMontoInicial);
        cardApertura.add(Box.createRigidArea(new Dimension(0, 35)));

        JButton btnAbrir = new JButton("INICIAR TURNO");
        btnAbrir.setBackground(Color.WHITE);
        btnAbrir.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnAbrir.setForeground(Color.BLACK);
        btnAbrir.setPreferredSize(new Dimension(200, 50));
        btnAbrir.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAbrir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAbrir.setFocusPainted(false);
        btnAbrir.addActionListener(e -> validarYAbrir());

        cardApertura.add(btnAbrir);

        pnlMain.add(cardApertura);
        add(pnlMain, BorderLayout.CENTER);
    }

    private void validarYAbrir() {
        String montoStr = txtMontoInicial.getText().trim();

        if (!GenericValidator.isDouble(montoStr) || Double.parseDouble(montoStr) < 0) {
            txtMontoInicial.setForeground(new Color(255, 107, 107));
            JOptionPane.showMessageDialog(this, "Por favor, ingresa un monto válido (ej. 500.00)", "Monto Inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        supervisorDTO supervisor = (supervisorDTO) cmbSupervisores.getSelectedItem();
        cajeroDTO cajero = (cajeroDTO) cmbCajeros.getSelectedItem();

        if (supervisor != null && cajero != null) {
            aperturaCajaDTO apertura = new aperturaCajaDTO(
                    cajero.getIdCajero(),
                    cajero.getNombreCompleto(),
                    Double.parseDouble(montoStr),
                    supervisor.getIdSupervisor()
            );

            if (control.confirmarApertura(apertura)) {
                JOptionPane.showMessageDialog(
                        this,
                        "¡Turno iniciado exitosamente para " + cajero.getNombreCompleto() + "!\nFondo inicial registrado: $" + String.format("%,.2f", Double.parseDouble(montoStr)),
                        "Apertura Exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                );

                control.mostrarResumenTurno(cajero.getIdCajero(), supervisor.getNombreCompleto());            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione Supervisor y Cajero.", "Datos Incompletos", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void autoseleccionarSupervisor() {
        cajeroDTO empleadoSeleccionado = (cajeroDTO) cmbCajeros.getSelectedItem();
        if (empleadoSeleccionado != null && cmbSupervisores.getItemCount() > 0) {
            // MOCK TEMPORAL PARA HOY:
            // Si el ID del cajero es par, le asignamos el segundo supervisor, si no, el primero.
            // MAÑANA CON BD: Esto se hará comparando empleadoSeleccionado.getIdSupervisor() con la BD.
            int indice = (empleadoSeleccionado.getIdCajero() % 2 == 0) ? 1 : 0;

            if (indice < cmbSupervisores.getItemCount()) {
                cmbSupervisores.setSelectedIndex(indice);
            }
        }
    }
    

    private JLabel crearEtiqueta(String t) {
        JLabel l = new JLabel(t);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private void estilizarCampo(JTextField f) {
        f.setBackground(new Color(55, 55, 55));
        f.setForeground(Color.WHITE);
        f.setCaretColor(Color.WHITE);
        f.setFont(new Font("Segoe UI", Font.BOLD, 18));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
    }

    public void cargarSupervisores(java.util.List<supervisorDTO> lista) {
        cmbSupervisores.removeAllItems();
        if(lista != null) {
            for (supervisorDTO s : lista) cmbSupervisores.addItem(s);
        }
    }

    public void cargarCajeros(java.util.List<cajeroDTO> lista) {
        cmbCajeros.removeAllItems();
        if(lista != null) {
            for (cajeroDTO c : lista) cmbCajeros.addItem(c);
        }
    }


    class BotonRedondeado extends JButton {
        private boolean destacado;
        public BotonRedondeado(String t, boolean d) {
            super(t);
            this.destacado = d;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(d ? Color.BLACK : Color.GRAY);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setMaximumSize(new Dimension(220, 45));
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

    class RoundedPanel extends JPanel {
        private int r; Color c;
        public RoundedPanel(int r, Color c) { this.r = r; this.c = c; setOpaque(false); }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), r, r);
            g2.dispose();
        }
    }
}