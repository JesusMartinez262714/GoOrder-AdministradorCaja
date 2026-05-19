package AdministradorCajaPresentacion.GUI;

import AdministradorCajaDTOs.aperturaCajaDTO;
import AdministradorCajaDTOs.cajeroDTO;
import AdministradorCajaDTOs.supervisorDTO;
import AdministradorCajaPresentacion.Control.Control;
import org.apache.commons.validator.GenericValidator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

/**
 * Pantalla de la interfaz gráfica que maneja la apertura de turnos y cajas.
 * Permite seleccionar al cajero, al supervisor y asignar el fondo inicial de efectivo,
 * validando que los datos cumplan con las reglas de negocio antes de iniciar.
 * * @author Jesus Manuel Martinez Cortez
 * @version 1.0
 */
public class AperturaCaja extends JFrame {

    private Control control;
    private JComboBox<supervisorDTO> cmbSupervisores;
    private JComboBox<cajeroDTO> cmbCajeros;
    private JTextField txtMontoInicial;

    private final Color COLOR_FONDO = new Color(28, 28, 28);
    private final Color COLOR_SIDEBAR = new Color(35, 35, 35);
    private final Color COLOR_CARD = new Color(42, 42, 42);
    private final Color COLOR_ACCENTO = new Color(66, 206, 126);
    private final Color COLOR_ROJO = new Color(255, 107, 107);

    /**
     * Constructor que inicializa la ventana de apertura de caja, configura
     * los componentes de la interfaz y establece el diseño visual.
     * * @param control Instancia del controlador para comunicar la vista con el negocio.
     */
    public AperturaCaja(Control control) {
        this.control = control;
        initComponents();
        configurarVentana();
    }

    /**
     * Configura las propiedades básicas de la ventana como el tamaño,
     * título, cierre y posición en pantalla.
     */
    private void configurarVentana() {
        setTitle("GoOrder - Apertura de Caja");
        setSize(1000, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
    }

    /**
     * Inicializa y organiza todos los componentes visuales de la pantalla,
     * incluyendo la barra lateral de navegación y el formulario central.
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

        JButton btnAperturaCaja = new BotonRedondeado("AperturaCaja", true);

        JButton btnGestionCajeros = new BotonRedondeado("GestionCajeros", false);
        btnGestionCajeros.addActionListener(e -> control.mostrarGestionCajeros());

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

    /**
     * Valida que el fondo de caja sea correcto y que se hayan seleccionado
     * tanto el cajero como el supervisor antes de iniciar el turno en el sistema.
     */
    private void validarYAbrir() {
        String montoStr = txtMontoInicial.getText().trim();

        if (montoStr.isEmpty() || !GenericValidator.isDouble(montoStr)) {
            txtMontoInicial.setForeground(COLOR_ROJO);
            JOptionPane.showMessageDialog(this, "Por favor, ingresa un monto numérico válido (ej. 500.00)", "Monto Inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double montoDbl = Double.parseDouble(montoStr);
        if (montoDbl < 0.0 || montoDbl > 100000.0) {
            txtMontoInicial.setForeground(COLOR_ROJO);
            JOptionPane.showMessageDialog(this, "El fondo de apertura debe ser un monto positivo y no exceder los $100,000.00", "Límite Excedido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        supervisorDTO supervisor = (supervisorDTO) cmbSupervisores.getSelectedItem();
        cajeroDTO cajero = (cajeroDTO) cmbCajeros.getSelectedItem();

        if (supervisor == null || "---".equalsIgnoreCase(supervisor.getNombreCompleto())) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un supervisor válido para autorizar la apertura.", "Datos Incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (cajero == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un cajero operativo para asignarle el turno.", "Datos Incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        txtMontoInicial.setForeground(Color.WHITE);

        aperturaCajaDTO apertura = new aperturaCajaDTO(
                cajero.getIdCajero(),
                cajero.getNombreCompleto(),
                montoDbl,
                supervisor.getIdSupervisor()
        );

        if (control.confirmarApertura(apertura)) {
            JOptionPane.showMessageDialog(
                    this,
                    "¡Turno iniciado exitosamente para " + cajero.getNombreCompleto() + "!\nFondo inicial registrado: $" + String.format("%,.2f", montoDbl),
                    "Apertura Exitosa",
                    JOptionPane.INFORMATION_MESSAGE
            );
            control.mostrarResumenTurno(cajero.getIdCajero());
        }
    }

    /**
     * Busca y selecciona automáticamente en el ComboBox al supervisor
     * que le corresponde al cajero que se encuentra seleccionado actualmente.
     */
    private void autoseleccionarSupervisor() {
        cajeroDTO empleadoSeleccionado = (cajeroDTO) cmbCajeros.getSelectedItem();
        if (empleadoSeleccionado != null && cmbSupervisores.getItemCount() > 0) {
            String nombreSupervisorAsignado = control.obtenerNombreSupervisorAsociado(empleadoSeleccionado.getIdCajero());
            if (nombreSupervisorAsignado == null) return;

            for (int i = 0; i < cmbSupervisores.getItemCount(); i++) {
                supervisorDTO s = cmbSupervisores.getItemAt(i);
                if (s != null && s.getNombreCompleto().equalsIgnoreCase(nombreSupervisorAsignado)) {
                    cmbSupervisores.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    /**
     * Crea una etiqueta de texto JLabel configurada con la tipografía
     * y los estilos visuales del diseño del formulario.
     * * @param t Texto explicativo que llevará la etiqueta.
     * @return El componente JLabel generado.
     */
    private JLabel crearEtiqueta(String t) {
        JLabel l = new JLabel(t);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    /**
     * Aplica el diseño visual al campo de texto del monto y le asigna
     * el filtro DocumentFilter para controlar la entrada de dinero por teclado.
     * * @param f El campo JTextField que se va a estilizar y proteger.
     */
    private void estilizarCampo(JTextField f) {
        f.setBackground(new Color(55, 55, 55));
        f.setForeground(Color.WHITE);
        f.setCaretColor(Color.WHITE);
        f.setFont(new Font("Segoe UI", Font.BOLD, 18));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));

        ((AbstractDocument) f.getDocument()).setDocumentFilter(new FiltroMonedaRobusto());
    }

    /**
     * Llena el ComboBox de supervisores con la lista obtenida de la base de datos
     * verificando que los registros no sean nulos.
     * * @param lista Lista de objetos DTO con los supervisores registrados.
     */
    public void cargarSupervisores(List<supervisorDTO> lista) {
        cmbSupervisores.removeAllItems();
        if (lista != null) {
            for (supervisorDTO s : lista) {
                if (s != null) cmbSupervisores.addItem(s);
            }
        }
    }

    /**
     * Llena el ComboBox de cajeros con los empleados disponibles y dispara
     * la función para preseleccionar de forma automática al supervisor a cargo.
     * * @param lista Lista de objetos DTO con los cajeros registrados.
     */
    public void cargarCajeros(List<cajeroDTO> lista) {
        cmbCajeros.removeAllItems();
        if (lista != null) {
            for (cajeroDTO c : lista) {
                if (c != null) cmbCajeros.addItem(c);
            }
        }
        autoseleccionarSupervisor();
    }

    /**
     * Filtro para restringir el texto del monto de apertura, permitiendo por
     * medio de una expresión regular solo números y un máximo de dos decimales.
     */
    private class FiltroMonedaRobusto extends DocumentFilter {
        private final int limiteCaracteres = 9;
        private final String regexMoneda = "^\\d{0,6}(\\.\\d{0,2})?$";

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string == null) return;

            StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
            sb.insert(offset, string);
            String propuesto = sb.toString();

            if (propuesto.length() <= limiteCaracteres && propuesto.matches(regexMoneda)) {
                super.insertString(fb, offset, string, attr);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null) return;

            StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
            sb.replace(offset, offset + length, text);
            String propuesto = sb.toString();

            if (propuesto.length() <= limiteCaracteres && propuesto.matches(regexMoneda)) {
                super.replace(fb, offset, length, text, attrs);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    /**
     * Clase personalizada para crear los botones del menú lateral con bordes
     * redondeados y efectos visuales al hacer clic.
     */
    class BotonRedondeado extends JButton {
        private boolean destacado;

        /**
         * Configura el texto, fuentes y estados visuales del botón redondo.
         * * @param t Texto que se mostrará dentro del botón.
         * @param d Booleano que indica si el botón debe resaltar en verde o gris oscuro.
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
     * Clase personalizada para renderizar paneles JPanel con esquinas redondeadas
     * utilizando suavizado de bordes mediante Graphics2D.
     */
    class RoundedPanel extends JPanel {
        private int r;
        private Color c;

        /**
         * Inicializa las propiedades básicas del panel redondo.
         * * @param r Radio de curvatura para redondear las esquinas.
         * @param c Color de fondo para rellenar el panel.
         */
        public RoundedPanel(int r, Color c) {
            this.r = r;
            this.c = c;
            setOpaque(false);
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), r, r);
            g2.dispose();
        }
    }
}