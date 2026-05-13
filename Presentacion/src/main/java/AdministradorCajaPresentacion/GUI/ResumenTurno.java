package AdministradorCajaPresentacion.GUI;

import AdministradorCajaDTOs.resumenVentasDTO;
import AdministradorCajaDTOs.cajeroDTO;
import AdministradorCajaPresentacion.Control.Control;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ResumenTurno extends JFrame {
    private Control control;
    private JLabel lblNombreSesion, lblTituloDinamico, lblEfectivo, lblApp, lblTarjeta, lblReferencia, lblHora;
    private JComboBox<cajeroDTO> cmbCajeros;
    private JButton btnCerrar;

    private final Color COLOR_FONDO = new Color(28, 28, 28);
    private final Color COLOR_CARD = new Color(42, 42, 42);
    private final Color COLOR_ACCENTO = new Color(66, 206, 126);

    public ResumenTurno(Control control) {
        this.control = control;
        initComponents();
        configurarVentana();
        iniciarReloj();
    }

    private void configurarVentana() {
        setTitle("GoOrder - Resumen de Turno");
        setSize(1000, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
    }

    private void iniciarReloj() {
        Timer timer = new Timer(1000, e -> {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            lblHora.setText(sdf.format(new Date()).toUpperCase());
        });
        timer.start();
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
        pnlSidebar.add(new BotonRedondeado("Caja/Turno", true));
        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton btnAperturaCaja = new BotonRedondeado("AperturaCaja", false);
        btnAperturaCaja.addActionListener(e -> control.mostrarAperturaCaja());
        pnlSidebar.add(btnAperturaCaja);

        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        pnlSidebar.add(new BotonRedondeado("GestionAdeudos", false));
        pnlSidebar.add(Box.createVerticalGlue());

        add(pnlSidebar, BorderLayout.WEST);

        JPanel pnlMain = new JPanel(new BorderLayout());
        pnlMain.setOpaque(false);
        pnlMain.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);

        JPanel pnlUserInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlUserInfo.setOpaque(false);
        lblNombreSesion = new JLabel("Sesión Actual: ---");
        lblNombreSesion.setForeground(Color.GRAY);
        JLabel lblEmp = new JLabel("Empleado:");
        lblEmp.setForeground(Color.WHITE);
        cmbCajeros = new JComboBox<>();
        cmbCajeros.setPreferredSize(new Dimension(180, 28));

        pnlUserInfo.add(lblNombreSesion);
        pnlUserInfo.add(lblEmp);
        pnlUserInfo.add(cmbCajeros);

        lblHora = new JLabel("--:-- --");
        lblHora.setForeground(Color.WHITE);
        lblHora.setFont(new Font("Segoe UI", Font.BOLD, 14));

        pnlHeader.add(pnlUserInfo, BorderLayout.WEST);
        pnlHeader.add(lblHora, BorderLayout.EAST);

        lblTituloDinamico = new JLabel("Resumen de Ventas del Turno.");
        lblTituloDinamico.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTituloDinamico.setForeground(Color.WHITE);
        lblTituloDinamico.setBorder(new EmptyBorder(25, 0, 25, 0));

        JPanel pnlGrid = new JPanel(new GridLayout(2, 2, 25, 25));
        pnlGrid.setOpaque(false);

        lblEfectivo = new JLabel("$0.00");
        lblApp = new JLabel("$0.00");
        lblTarjeta = new JLabel("$0.00");
        lblReferencia = new JLabel("$0.00");

        pnlGrid.add(new CardResumen("Efectivo", lblEfectivo));
        pnlGrid.add(new CardResumen("App", lblApp));
        pnlGrid.add(new CardResumen("Tarjeta sucursal", lblTarjeta));
        pnlGrid.add(new CardResumen("Referencia", lblReferencia));

        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setOpaque(false);
        pnlFooter.setBorder(new EmptyBorder(30, 0, 0, 0));

        JPanel pnlBotonesAccion = new JPanel(new BorderLayout());
        pnlBotonesAccion.setOpaque(false);

        JButton btnHistorial = new BotonRedondeado("VER HISTORIAL", true);
        btnHistorial.setPreferredSize(new Dimension(220, 55));
        btnHistorial.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnHistorial.addActionListener(e -> control.mostrarHistorialCortes());

        btnCerrar = new BotonRedondeado("INICIAR CIERRE DE CAJA", true);
        btnCerrar.setPreferredSize(new Dimension(280, 55));
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 16));

        btnCerrar.addActionListener(e -> {
            if (cmbCajeros.getItemCount() == 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "No se puede iniciar el cierre porque no hay ninguna caja abierta actualmente.",
                        "Acción Denegada",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            control.mostrarFormularioCorte();
        });
        pnlBotonesAccion.add(btnHistorial, BorderLayout.WEST);
        pnlBotonesAccion.add(btnCerrar, BorderLayout.EAST);
        pnlFooter.add(pnlBotonesAccion, BorderLayout.CENTER);

        JPanel pnlCentro = new JPanel(new BorderLayout());
        pnlCentro.setOpaque(false);
        pnlCentro.add(lblTituloDinamico, BorderLayout.NORTH);
        pnlCentro.add(pnlGrid, BorderLayout.CENTER);
        pnlCentro.add(pnlFooter, BorderLayout.SOUTH);

        pnlMain.add(pnlHeader, BorderLayout.NORTH);
        pnlMain.add(pnlCentro, BorderLayout.CENTER);

        add(pnlMain, BorderLayout.CENTER);
    }


    public void mostrarEstadoSinSesion() {
        lblNombreSesion.setText("Sesión Actual: ---");
        lblTituloDinamico.setText("Esperando apertura de turno...");

        cmbCajeros.removeAllItems();

        lblEfectivo.setText("$0.00");
        lblApp.setText("$0.00");
        lblTarjeta.setText("$0.00");
        lblReferencia.setText("$0.00");


    }

    public void cargarDatos(resumenVentasDTO resumen, List<cajeroDTO> cajeros, String nombreSupervisor, int idEmpleadoActivo) {
        lblNombreSesion.setText("Sesión Actual: " + (nombreSupervisor != null ? nombreSupervisor : "---"));



        java.awt.event.ItemListener[] listeners = cmbCajeros.getItemListeners();
        for (java.awt.event.ItemListener il : listeners) cmbCajeros.removeItemListener(il);

        cmbCajeros.removeAllItems();
        if (cajeros != null) {
            for (cajeroDTO cajero : cajeros) cmbCajeros.addItem(cajero);
        }

        seleccionarEmpleadoEnCombo(idEmpleadoActivo);

        for (java.awt.event.ItemListener il : listeners) cmbCajeros.addItemListener(il);

        actualizarMontos(resumen, (cajeroDTO) cmbCajeros.getSelectedItem(), nombreSupervisor);
    }

    private void seleccionarEmpleadoEnCombo(int idBuscado) {
        for (int i = 0; i < cmbCajeros.getItemCount(); i++) {
            if (cmbCajeros.getItemAt(i).getIdCajero() == idBuscado) {
                cmbCajeros.setSelectedIndex(i);
                break;
            }
        }
    }

    public void setCajeroChangeListener(java.awt.event.ItemListener listener) {
        for (java.awt.event.ItemListener il : cmbCajeros.getItemListeners()) {
            cmbCajeros.removeItemListener(il);
        }
        cmbCajeros.addItemListener(listener);
    }

    public void actualizarMontos(resumenVentasDTO resumen, cajeroDTO cajero, String nombreSupervisor) {
        if (resumen != null) {
            lblEfectivo.setText(String.format("$%,.2f", resumen.getTotalEfectivo()));
            lblApp.setText(String.format("$%,.2f", resumen.getTotalApp()));
            lblTarjeta.setText(String.format("$%,.2f", resumen.getTotalTarjeta()));
            lblReferencia.setText(String.format("$%,.2f", resumen.getTotalReferencia()));
        }
        if (cajero != null) {
            lblTituloDinamico.setText("Resumen de Ventas del Turno de " + cajero.getNombreCompleto() + ".");
        }
        if (nombreSupervisor != null) {
            lblNombreSesion.setText("Sesión Actual: " + nombreSupervisor);
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

    class CardResumen extends JPanel {
        public CardResumen(String titulo, JLabel montoLabel) {
            setLayout(new BorderLayout());
            setBackground(COLOR_CARD);
            setBorder(new EmptyBorder(15, 20, 15, 20));
            JLabel lblT = new JLabel(titulo);
            lblT.setForeground(Color.LIGHT_GRAY);
            lblT.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            montoLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            montoLabel.setForeground(Color.WHITE);
            add(lblT, BorderLayout.NORTH);
            add(montoLabel, BorderLayout.CENTER);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
            g2.setColor(COLOR_ACCENTO);
            g2.fill(new RoundRectangle2D.Double(0, 10, 6, getHeight() - 20, 6, 6));
            g2.dispose();
        }
    }
}