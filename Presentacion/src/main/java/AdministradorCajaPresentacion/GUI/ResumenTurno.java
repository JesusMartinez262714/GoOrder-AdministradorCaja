package AdministradorCajaPresentacion.GUI;

import AdministradorCajaDTOs.cajeroDTO;
import AdministradorCajaDTOs.resumenVentasDTO;
import AdministradorCajaPresentacion.Control.Control;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Pantalla principal que muestra el resumen de ventas del turno actual en el sistema GoOrder.
 * Permite a los supervisores ver el dinero acumulado por cada método de pago
 * y controlar los turnos activos de los cajeros.
 * * @author Jesus Manuel Martinez Cortez
 * @version 1.0
 */
public class ResumenTurno extends JFrame {
    private Control control;
    private JLabel lblNombreSesion, lblTituloDinamico, lblEfectivo, lblApp, lblTarjeta, lblReferencia, lblHora;
    private JComboBox<cajeroDTO> cmbCajeros;

    private final Color COLOR_FONDO = new Color(28, 28, 28);
    private final Color COLOR_SIDEBAR = new Color(35, 35, 35);
    private final Color COLOR_CARD = new Color(42, 42, 42);
    private final Color COLOR_ACCENTO = new Color(66, 206, 126);

    /**
     * Constructor que recibe el controlador de la aplicación, inicializa
     * la interfaz gráfica y arranca el reloj en tiempo real.
     * * @param control Instancia del controlador para comunicar la vista con el negocio.
     */
    public ResumenTurno(Control control) {
        this.control = control;
        initComponents();
        configurarVentana();
        iniciarReloj();
    }

    /**
     * Configura las propiedades básicas de la ventana como tamaño, título,
     * centrado en pantalla y el cierre de la aplicación por defecto.
     */
    private void configurarVentana() {
        setTitle("GoOrder - Resumen de Turno");
        setSize(1000, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
    }

    /**
     * Inicia un temporizador Timer de Swing para actualizar la hora
     * reflejada en pantalla cada segundo con el formato de doce horas.
     */
    private void iniciarReloj() {
        Timer timer = new Timer(1000, e -> {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            lblHora.setText(sdf.format(new Date()).toUpperCase());
        });
        timer.start();
    }

    /**
     * Inicializa y acomoda todos los componentes visuales de la ventana,
     * estructurando el menú lateral de navegación y las tarjetas de montos.
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

        JButton btnResumen = new BotonRedondeado("Caja/Turno", true);
        JButton btnAperturaCaja = new BotonRedondeado("AperturaCaja", false);
        btnAperturaCaja.addActionListener(e -> control.mostrarAperturaCaja());

        JButton btnGestionCajeros = new BotonRedondeado("GestionCajeros", false);
        btnGestionCajeros.addActionListener(e -> control.mostrarGestionCajeros());

        JButton btnGestionSupervisores = new BotonRedondeado("Supervisores", false);
        btnGestionSupervisores.addActionListener(e -> control.mostrarGestionSupervisores());

        pnlSidebar.add(btnResumen);
        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        pnlSidebar.add(btnAperturaCaja);
        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 40)));
        pnlSidebar.add(btnGestionCajeros);
        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        pnlSidebar.add(btnGestionSupervisores);
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

        JLabel lblEmp = new JLabel("Caja de:");
        lblEmp.setForeground(Color.WHITE);
        lblEmp.setFont(new Font("Segoe UI", Font.BOLD, 14));

        cmbCajeros = new JComboBox<>();
        cmbCajeros.setPreferredSize(new Dimension(200, 30));

        pnlUserInfo.add(lblNombreSesion);
        pnlUserInfo.add(Box.createRigidArea(new Dimension(10, 0)));
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
        pnlGrid.add(new CardResumen("App de Pedidos", lblApp));
        pnlGrid.add(new CardResumen("Terminal Bancaria", lblTarjeta));
        pnlGrid.add(new CardResumen("Referencia / Transferencia", lblReferencia));

        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setOpaque(false);
        pnlFooter.setBorder(new EmptyBorder(30, 0, 0, 0));

        JButton btnHistorial = new BotonRedondeado("VER HISTORIAL", true);
        btnHistorial.setPreferredSize(new Dimension(220, 55));
        btnHistorial.addActionListener(e -> control.mostrarHistorialCortes());

        JButton btnCerrar = new BotonRedondeado("INICIAR CIERRE DE CAJA", true);
        btnCerrar.setPreferredSize(new Dimension(280, 55));
        btnCerrar.addActionListener(e -> {
            if (cmbCajeros.getItemCount() == 0 || cmbCajeros.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "No hay turnos abiertos para realizar un cierre.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            control.mostrarFormularioCorte();
        });

        pnlFooter.add(btnHistorial, BorderLayout.WEST);
        pnlFooter.add(btnCerrar, BorderLayout.EAST);

        JPanel pnlCentro = new JPanel(new BorderLayout());
        pnlCentro.setOpaque(false);
        pnlCentro.add(lblTituloDinamico, BorderLayout.NORTH);
        pnlCentro.add(pnlGrid, BorderLayout.CENTER);
        pnlCentro.add(pnlFooter, BorderLayout.SOUTH);

        pnlMain.add(pnlHeader, BorderLayout.NORTH);
        pnlMain.add(pnlCentro, BorderLayout.CENTER);

        add(pnlMain, BorderLayout.CENTER);
    }

    /**
     * Limpia y restablece las etiquetas informativas y los montos a ceros
     * cuando no hay ningún turno activo en el sistema.
     */
    public void mostrarEstadoSinSesion() {
        lblNombreSesion.setText("Sesión Actual: ---");
        lblTituloDinamico.setText("Esperando apertura de turno...");
        cmbCajeros.removeAllItems();
        lblEfectivo.setText("$0.00");
        lblApp.setText("$0.00");
        lblTarjeta.setText("$0.00");
        lblReferencia.setText("$0.00");
    }

    /**
     * Carga la lista de cajeros con turnos abiertos en el ComboBox y actualiza
     * las etiquetas con los montos de venta que les corresponden.
     * * @param resumen Objeto DTO con el desglose de las ventas calculadas.
     * @param cajeros Lista de cajeros que tienen sesiones activas en el turno.
     * @param nombreSupervisor Nombre del supervisor a cargo de la sesión.
     * @param idEmpleadoActivo ID del cajero que debe mostrarse seleccionado por defecto.
     */
    public void cargarDatos(resumenVentasDTO resumen, List<cajeroDTO> cajeros, String nombreSupervisor, int idEmpleadoActivo) {
        java.awt.event.ItemListener[] listeners = cmbCajeros.getItemListeners();
        for (java.awt.event.ItemListener il : listeners) cmbCajeros.removeItemListener(il);

        cmbCajeros.removeAllItems();
        if (cajeros != null && !cajeros.isEmpty()) {
            for (cajeroDTO cajero : cajeros) {
                if (cajero != null) cmbCajeros.addItem(cajero);
            }
        }

        for (int i = 0; i < cmbCajeros.getItemCount(); i++) {
            if (cmbCajeros.getItemAt(i).getIdCajero() == idEmpleadoActivo) {
                cmbCajeros.setSelectedIndex(i);
                break;
            }
        }

        for (java.awt.event.ItemListener il : listeners) cmbCajeros.addItemListener(il);
        actualizarMontos(resumen, (cajeroDTO) cmbCajeros.getSelectedItem(), nombreSupervisor);
    }

    /**
     * Asigna un ItemListener al ComboBox de cajeros para actualizar las ventas
     * en pantalla cada vez que se seleccione un empleado diferente.
     * * @param listener El listener encargado de detectar los cambios de selección.
     */
    public void setCajeroChangeListener(java.awt.event.ItemListener listener) {
        for (java.awt.event.ItemListener il : cmbCajeros.getItemListeners()) cmbCajeros.removeItemListener(il);
        if (listener != null) cmbCajeros.addItemListener(listener);
    }

    /**
     * Actualiza los textos de las etiquetas con las cantidades de dinero de las ventas
     * formateadas correctamente. Si el objeto resumen es nulo, los valores se limpian a cero.
     * * @param resumen Objeto DTO con las ventas por concepto de pago.
     * @param cajero Datos del cajero del cual se están mostrando los montos.
     * @param nombreSupervisor Nombre del supervisor en sesión actual.
     */
    public void actualizarMontos(resumenVentasDTO resumen, cajeroDTO cajero, String nombreSupervisor) {
        if (resumen != null) {
            lblEfectivo.setText(String.format("$%,.2f", resumen.getTotalEfectivo()));
            lblApp.setText(String.format("$%,.2f", resumen.getTotalApp()));
            lblTarjeta.setText(String.format("$%,.2f", resumen.getTotalTarjeta()));
            lblReferencia.setText(String.format("$%,.2f", resumen.getTotalReferencia()));
        } else {
            lblEfectivo.setText("$0.00");
            lblApp.setText("$0.00");
            lblTarjeta.setText("$0.00");
            lblReferencia.setText("$0.00");
        }

        if (cajero != null) {
            lblTituloDinamico.setText("Resumen de Ventas: " + cajero.getNombreCompleto());
        } else {
            lblTituloDinamico.setText("Esperando selección de caja...");
        }

        if (nombreSupervisor != null) {
            lblNombreSesion.setText("Sesión Actual: " + nombreSupervisor);
        } else {
            lblNombreSesion.setText("Sesión Actual: ---");
        }
    }

    /**
     * Clase interna para crear botones con esquinas redondeadas personalizados
     * para el diseño visual del tablero de la aplicación.
     */
    class BotonRedondeado extends JButton {
        private boolean destacado;

        /**
         * Configura el texto, color de fondo y fuentes del botón redondo.
         * * @param t Texto que llevará el botón en la interfaz.
         * @param d Define si el botón usa el color verde de realce o el gris estándar.
         */
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
            setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(destacado ? COLOR_ACCENTO : new Color(50, 50, 50));
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
            super.paintComponent(g);
        }
    }

    /**
     * Clase interna para mostrar las tarjetas informativas de resumen de ventas,
     * aplicando el diseño con bordes redondeados e indicadores laterales.
     */
    class CardResumen extends JPanel {

        /**
         * Construye la tarjeta asignándole un título descriptivo y enlazando
         * la etiqueta de texto para el monto de dinero.
         * * @param titulo El concepto de la venta (Efectivo, Tarjeta, etc.).
         * @param montoLabel Componente JLabel que contendrá el valor dinámico.
         */
        public CardResumen(String titulo, JLabel montoLabel) {
            setLayout(new BorderLayout());
            setBackground(COLOR_CARD);
            setBorder(new EmptyBorder(15, 20, 15, 20));

            JLabel lblT = new JLabel(titulo);
            lblT.setForeground(Color.LIGHT_GRAY);
            lblT.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            montoLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
            montoLabel.setForeground(Color.WHITE);

            add(lblT, BorderLayout.NORTH);
            add(montoLabel, BorderLayout.CENTER);
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
            g2.setColor(COLOR_ACCENTO);
            g2.fill(new RoundRectangle2D.Double(0, 15, 5, getHeight() - 30, 5, 5));
            g2.dispose();
        }
    }
}