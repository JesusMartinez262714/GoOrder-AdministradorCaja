package AdministradorCajaPresentacion.GUI;

import AdministradorCajaDTOs.cajeroDTO;
import AdministradorCajaDTOs.corteCajaDTO;
import AdministradorCajaDTOs.desgloseDTO;
import AdministradorCajaDTOs.resumenVentasDTO;
import AdministradorCajaPresentacion.Control.Control;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Pantalla principal del formulario para realizar el corte de caja.
 * Permite capturar los montos físicos por cada método de pago, adjuntar
 * la imagen del comprobante y calcular las diferencias lógicas antes de cerrar.
 * * @author Jesus Manuel Martinez Cortez
 * @version 1.0
 */
public class FormularioCorte extends JFrame implements FilaMontoPanel.FilaMontoListener {

    private Control control;
    private JPanel pnlContenedorFilas;
    private JComboBox<cajeroDTO> cmbEmpleados;
    private JTextField txtSupervisor;
    private JLabel lblTotalManualBadge, lblFaltanteBadge, lblPreviewImagen;
    private String rutaImagenSeleccionada = "";
    private int idCorteEditando = -1;
    private double montoEsperadoEditando = 0.0;

    private final Color COLOR_FONDO = new Color(34, 34, 34);
    private final Color COLOR_TEXTO = new Color(240, 240, 240);
    private final Color COLOR_ACCENTO = new Color(45, 212, 112);
    private final Color COLOR_CARD = new Color(60, 60, 60);
    private final Color COLOR_DIVISOR = new Color(85, 85, 85);

    /**
     * Constructor que enlaza el controlador de la aplicación, inicializa
     * el diseño gráfico y añade la primera fila de captura por defecto.
     * * @param control Instancia del controlador para la navegación entre ventanas.
     */
    public FormularioCorte(Control control) {
        this.control = control;
        initComponents();
        configurarVentana();
        agregarFila();
    }

    /**
     * Configura las propiedades esenciales de la ventana, como el tamaño,
     * título del marco, centrado y comportamiento de cierre.
     */
    private void configurarVentana() {
        setTitle("GoOrder - Nuevo Corte");
        setSize(480, 780);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
    }

    /**
     * Inicializa y organiza todos los componentes visuales de la pantalla,
     * aplicando el diseño oscuro y las fuentes tipográficas del sistema.
     */
    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel pnlPrincipal = new JPanel();
        pnlPrincipal.setLayout(new BoxLayout(pnlPrincipal, BoxLayout.Y_AXIS));
        pnlPrincipal.setBackground(COLOR_FONDO);
        pnlPrincipal.setBorder(new EmptyBorder(25, 30, 25, 30));

        JPanel pnlTop = new JPanel(new BorderLayout(15, 0));
        pnlTop.setBackground(COLOR_FONDO);
        pnlTop.setMaximumSize(new Dimension(500, 45));

        RoundedButton btnBack = new RoundedButton("←", 12, COLOR_ACCENTO, Color.BLACK);
        btnBack.setPreferredSize(new Dimension(35, 35));
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnBack.addActionListener(e -> control.volverAResumen());

        JLabel lblTitulo = new JLabel("CORTE DE CAJA");
        lblTitulo.setForeground(COLOR_TEXTO);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));

        pnlTop.add(btnBack, BorderLayout.WEST);
        pnlTop.add(lblTitulo, BorderLayout.CENTER);

        JPanel pnlDivisorHeader = new JPanel();
        pnlDivisorHeader.setBackground(COLOR_DIVISOR);
        pnlDivisorHeader.setMaximumSize(new Dimension(500, 1));

        pnlPrincipal.add(pnlTop);
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlPrincipal.add(pnlDivisorHeader);
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));

        txtSupervisor = new JTextField("---");
        txtSupervisor.setEditable(false);
        pnlPrincipal.add(crearFilaPildora("Supervisor:", txtSupervisor));
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));

        cmbEmpleados = new JComboBox<>();
        cmbEmpleados.addActionListener(e -> {
            actualizarSupervisor();
            calcularTotales();
        });
        pnlPrincipal.add(crearFilaPildora("Empleado:", cmbEmpleados));
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 25)));

        JPanel pnlManualHeader = new JPanel(new BorderLayout());
        pnlManualHeader.setBackground(COLOR_FONDO);
        pnlManualHeader.setMaximumSize(new Dimension(500, 35));

        JLabel lblManual = new JLabel("Montos Manuales");
        lblManual.setForeground(COLOR_TEXTO);
        lblManual.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JPanel pnlBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        pnlBtns.setBackground(COLOR_FONDO);

        RoundedButton btnAdd = new RoundedButton("+", 8, new Color(210, 210, 210), Color.BLACK);
        RoundedButton btnRemove = new RoundedButton("-", 8, new Color(210, 210, 210), Color.BLACK);
        btnAdd.setPreferredSize(new Dimension(35, 28));
        btnRemove.setPreferredSize(new Dimension(35, 28));
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnRemove.setFont(new Font("Segoe UI", Font.BOLD, 22));

        btnAdd.addActionListener(e -> agregarFila());
        btnRemove.addActionListener(e -> quitarFila());

        pnlBtns.add(btnAdd);
        pnlBtns.add(btnRemove);
        pnlManualHeader.add(lblManual, BorderLayout.WEST);
        pnlManualHeader.add(pnlBtns, BorderLayout.EAST);
        pnlPrincipal.add(pnlManualHeader);
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 5)));

        RoundedPanel pnlTabla = new RoundedPanel(15, COLOR_CARD);
        pnlTabla.setLayout(new BorderLayout());
        pnlTabla.setMaximumSize(new Dimension(500, 220));
        pnlTabla.setBorder(BorderFactory.createLineBorder(COLOR_DIVISOR, 1));

        JPanel pnlHeaderTabla = new JPanel(new GridLayout(1, 2));
        pnlHeaderTabla.setOpaque(false);
        pnlHeaderTabla.setBorder(new EmptyBorder(12, 10, 12, 10));

        JLabel lblTipo = new JLabel("TIPO", SwingConstants.CENTER);
        JLabel lblCant = new JLabel("CANTIDAD", SwingConstants.CENTER);
        lblTipo.setForeground(COLOR_TEXTO); lblTipo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCant.setForeground(COLOR_TEXTO); lblCant.setFont(new Font("Segoe UI", Font.BOLD, 14));

        pnlHeaderTabla.add(lblTipo);
        pnlHeaderTabla.add(lblCant);
        pnlTabla.add(pnlHeaderTabla, BorderLayout.NORTH);

        pnlContenedorFilas = new JPanel();
        pnlContenedorFilas.setLayout(new BoxLayout(pnlContenedorFilas, BoxLayout.Y_AXIS));
        pnlContenedorFilas.setOpaque(false);

        JScrollPane scroll = new JScrollPane(pnlContenedorFilas);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_DIVISOR));
        pnlTabla.add(scroll, BorderLayout.CENTER);

        pnlPrincipal.add(pnlTabla);
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel pnlImagen = new JPanel(new GridLayout(1, 2, 20, 0));
        pnlImagen.setBackground(COLOR_FONDO);
        pnlImagen.setMaximumSize(new Dimension(500, 110));

        RoundedPanel pnlPreviewContenedor = new RoundedPanel(15, new Color(25, 25, 25));
        pnlPreviewContenedor.setLayout(new BorderLayout());
        lblPreviewImagen = new JLabel("", SwingConstants.CENTER);
        pnlPreviewContenedor.add(lblPreviewImagen, BorderLayout.CENTER);

        RoundedButton btnAdjuntar = new RoundedButton("Adjunta Comprobante", 15, new Color(160, 160, 160), Color.BLACK);
        btnAdjuntar.setTextHtml("<html><center><font size='6' color='#1b7a42'>☁</font><br>Adjunta<br>Comprobante</center></html>");
        btnAdjuntar.addActionListener(e -> adjuntarImagen());

        pnlImagen.add(pnlPreviewContenedor);
        pnlImagen.add(btnAdjuntar);
        pnlPrincipal.add(pnlImagen);
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBackground(COLOR_FONDO);
        pnlFooter.setMaximumSize(new Dimension(500, 80));

        JPanel pnlTotales = new JPanel();
        pnlTotales.setLayout(new BoxLayout(pnlTotales, BoxLayout.Y_AXIS));
        pnlTotales.setOpaque(false);

        JPanel pnlTotalLine = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlTotalLine.setOpaque(false);
        JLabel lblTotalText = new JLabel("Total:");
        lblTotalText.setForeground(COLOR_TEXTO);
        lblTotalText.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotalText.setPreferredSize(new Dimension(75, 30));

        lblTotalManualBadge = new JLabel("$0.00", SwingConstants.CENTER);
        lblTotalManualBadge.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalManualBadge.setForeground(Color.BLACK);
        RoundedPanel badgeTotal = new RoundedPanel(15, Color.WHITE);
        badgeTotal.setPreferredSize(new Dimension(100, 25));
        badgeTotal.add(lblTotalManualBadge);

        pnlTotalLine.add(lblTotalText);
        pnlTotalLine.add(badgeTotal);

        JPanel pnlFaltanteLine = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlFaltanteLine.setOpaque(false);
        JLabel lblFaltanteText = new JLabel("Faltante:");
        lblFaltanteText.setForeground(COLOR_TEXTO);
        lblFaltanteText.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFaltanteText.setPreferredSize(new Dimension(75, 30));

        lblFaltanteBadge = new JLabel("$0.00", SwingConstants.CENTER);
        lblFaltanteBadge.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblFaltanteBadge.setForeground(Color.BLACK);
        RoundedPanel badgeFaltante = new RoundedPanel(15, Color.WHITE);
        badgeFaltante.setPreferredSize(new Dimension(100, 25));
        badgeFaltante.add(lblFaltanteBadge);

        pnlFaltanteLine.add(lblFaltanteText);
        pnlFaltanteLine.add(badgeFaltante);

        pnlTotales.add(pnlTotalLine);
        pnlTotales.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlTotales.add(pnlFaltanteLine);

        RoundedButton btnVerificar = new RoundedButton("Verificar", 15, COLOR_ACCENTO, Color.BLACK);
        btnVerificar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnVerificar.setPreferredSize(new Dimension(120, 45));
        btnVerificar.addActionListener(e -> realizarCorte());

        JPanel pnlBtnVerificarWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 10));
        pnlBtnVerificarWrap.setOpaque(false);
        pnlBtnVerificarWrap.add(btnVerificar);

        pnlFooter.add(pnlTotales, BorderLayout.WEST);
        pnlFooter.add(pnlBtnVerificarWrap, BorderLayout.EAST);

        pnlPrincipal.add(pnlFooter);
        add(new JScrollPane(pnlPrincipal), BorderLayout.CENTER);
    }

    /**
     * Construye una fila visual estilizada con bordes redondeados para agrupar
     * una etiqueta de texto con un componente JComponent específico.
     * * @param texto Mensaje descriptivo de la etiqueta de la fila.
     * @param comp Elemento interactivo de Swing que se integrará al contenedor.
     * @return El panel JPanel configurado con la estructura visual.
     */
    private JPanel crearFilaPildora(String texto, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout(15, 0));
        p.setBackground(COLOR_FONDO);
        p.setMaximumSize(new Dimension(500, 32));

        JLabel l = new JLabel(texto);
        l.setForeground(COLOR_TEXTO);
        l.setFont(new Font("Segoe UI", Font.BOLD, 15));
        l.setPreferredSize(new Dimension(95, 30));

        RoundedPanel pill = new RoundedPanel(15, Color.WHITE);
        pill.setLayout(new BorderLayout());
        pill.setBorder(new EmptyBorder(2, 10, 2, 10));

        comp.setBorder(null);
        comp.setOpaque(false);
        comp.setFont(new Font("Segoe UI", Font.BOLD, 14));
        comp.setForeground(Color.BLACK);

        if (comp instanceof JComboBox) comp.setBackground(Color.WHITE);

        pill.add(comp, BorderLayout.CENTER);
        p.add(l, BorderLayout.WEST);
        p.add(pill, BorderLayout.CENTER);
        return p;
    }

    /**
     * Añade una nueva fila dinámica para capturar otro método de pago en el panel,
     * limitando la cantidad a un máximo de cuatro desgloses simultativos.
     */
    private void agregarFila() {
        if (pnlContenedorFilas.getComponentCount() >= 4) return;
        pnlContenedorFilas.add(new FilaMontoPanel(this));
        pnlContenedorFilas.revalidate();
        calcularTotales();
    }

    /**
     * Remueve la última fila de desglose agregada al panel y actualiza de inmediato
     * las etiquetas con las sumas financieras acumulativas del formulario.
     */
    private void quitarFila() {
        int count = pnlContenedorFilas.getComponentCount();
        if (count > 0) {
            pnlContenedorFilas.remove(count - 1);
            pnlContenedorFilas.revalidate();
            pnlContenedorFilas.repaint();
            calcularTotales();
        }
    }

    /**
     * Abre un cuadro selector de archivos JFileChooser enfocado en imágenes para
     * cargar la foto del comprobante físico y mostrar una previsualización en la GUI.
     */
    private void adjuntarImagen() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "png", "jpeg"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (file != null && file.exists() && file.isFile()) {
                rutaImagenSeleccionada = file.getAbsolutePath();
                lblPreviewImagen.setIcon(new ImageIcon(new ImageIcon(rutaImagenSeleccionada).getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH)));
            } else {
                JOptionPane.showMessageDialog(this, "El archivo seleccionado no es válido o no existe en el disco.", "Archivo Inválido", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Actualiza automáticamente el campo del supervisor consultando el nombre
     * asociado al cajero que se encuentra seleccionado en el ComboBox.
     */
    private void actualizarSupervisor() {
        cajeroDTO emp = (cajeroDTO) cmbEmpleados.getSelectedItem();
        txtSupervisor.setText((emp != null) ? control.obtenerNombreSupervisorAsociado(emp.getIdCajero()) : "---");
    }

    /**
     * Suma las cantidades físicas declaradas en cada una de las filas activas y calcula
     * la diferencia con respecto al dinero esperado del turno para mostrar el faltante.
     */
    public void calcularTotales() {
        double total = 0;
        for (Component c : pnlContenedorFilas.getComponents()) {
            if (c instanceof FilaMontoPanel) total += ((FilaMontoPanel) c).getMonto();
        }
        lblTotalManualBadge.setText(String.format("$%,.2f", total));

        cajeroDTO emp = (cajeroDTO) cmbEmpleados.getSelectedItem();
        if (emp != null) {
            double esperado = (this.idCorteEditando != -1) ? this.montoEsperadoEditando : control.obtenerMontoEsperado(emp.getIdCajero());
            lblFaltanteBadge.setText(String.format("$%,.2f", Math.max(0, esperado - total)));
        } else {
            lblFaltanteBadge.setText("$0.00");
        }
    }

    /**
     * Ejecuta las validaciones obligatorias del formulario (cajero seleccionado, campos completos
     * y métodos coherentes con las ventas) antes de avanzar a la ventana de conciliación final.
     */
    private void realizarCorte() {
        cajeroDTO emp = (cmbEmpleados.getSelectedItem() != null) ? (cajeroDTO) cmbEmpleados.getSelectedItem() : null;
        if (emp == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un empleado para proceder con el corte.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (pnlContenedorFilas.getComponentCount() == 0) {
            JOptionPane.showMessageDialog(this, "Debe declarar al menos un método de pago en el desglose.", "Datos Incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!rutaImagenSeleccionada.isEmpty()) {
            File archivoComprobante = new File(rutaImagenSeleccionada);
            if (!archivoComprobante.exists() || !archivoComprobante.isFile()) {
                JOptionPane.showMessageDialog(this, "La ruta del comprobante adjunto ha dejado de ser válida. Por favor, reasigne el archivo.", "Error de Archivo", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        double totalContado = 0;
        List<desgloseDTO> desgloses = new ArrayList<>();

        resumenVentasDTO resumenReal = null;
        if (this.idCorteEditando == -1) {
            resumenReal = control.obtenerResumenCajero(emp.getIdCajero());
        }

        for (Component c : pnlContenedorFilas.getComponents()) {
            if (c instanceof FilaMontoPanel) {
                FilaMontoPanel f = (FilaMontoPanel) c;
                String metodoStr = f.getMetodo().trim();
                double montoIngresado = f.getMonto();

                if (metodoStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Selecciona un método en todas las filas.", "Faltante", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (montoIngresado < 0) {
                    JOptionPane.showMessageDialog(this, "Corrige los montos en rojo.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (this.idCorteEditando == -1 && montoIngresado > 0 && resumenReal != null) {
                    boolean errorMetodoInvalido = false;

                    switch (metodoStr.toLowerCase()) {
                        case "efectivo":
                            if (resumenReal.getTotalEfectivo() <= 0) errorMetodoInvalido = true;
                            break;
                        case "tarjeta":
                            if (resumenReal.getTotalTarjeta() <= 0) errorMetodoInvalido = true;
                            break;
                        case "app":
                            if (resumenReal.getTotalApp() <= 0) errorMetodoInvalido = true;
                            break;
                        case "referencia":
                            if (resumenReal.getTotalReferencia() <= 0) errorMetodoInvalido = true;
                            break;
                    }

                    if (errorMetodoInvalido) {
                        JOptionPane.showMessageDialog(this,
                                "Estás declarando dinero en '" + metodoStr + "', pero el sistema indica que no hubo ventas con este método.\nRevisa el desglose y corrige el error.",
                                "Método de Pago Inválido", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                totalContado += montoIngresado;
                desgloses.add(new desgloseDTO(montoIngresado, 0, metodoStr));
            }
        }

        if (totalContado == 0 && desgloses.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se puede procesar un corte con una declaración de montos físicos en cero.", "Validación de Contabilidad", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double esperadoFinal = (this.idCorteEditando != -1) ? this.montoEsperadoEditando : control.obtenerMontoEsperado(emp.getIdCajero());
        control.mostrarConciliacionFinal(esperadoFinal, totalContado, emp, desgloses, rutaImagenSeleccionada, idCorteEditando);
    }

    /**
     * Llena el ComboBox de empleados con la lista de cajeros recibida desde la
     * capa de negocio y refresca el cálculo totalizador en pantalla.
     * * @param empleados Lista de objetos DTO con los cajeros disponibles.
     */
    public void cargarEmpleados(List<cajeroDTO> empleados) {
        cmbEmpleados.removeAllItems();
        if (empleados != null) for (cajeroDTO emp : empleados) cmbEmpleados.addItem(emp);
        actualizarSupervisor();
        calcularTotales();
    }

    /**
     * Borra todas las selecciones de la ventana, remueve la vista previa del comprobante
     * y vacía las variables para dejar el formulario listo para un corte nuevo.
     */
    public void limpiarFormulario() {
        this.idCorteEditando = -1;
        this.montoEsperadoEditando = 0.0;
        pnlContenedorFilas.removeAll();
        rutaImagenSeleccionada = "";
        lblPreviewImagen.setIcon(null);
        if (cmbEmpleados.getItemCount() > 0) cmbEmpleados.setSelectedIndex(0);
        txtSupervisor.setText("---");
        lblTotalManualBadge.setText("$0.00");
        lblFaltanteBadge.setText("$0.00");
        agregarFila();
    }

    /**
     * Configura el formulario inyectando la información de un corte histórico seleccionado,
     * permitiendo modificar las cantidades declaradas anteriormente.
     * * @param corte Objeto DTO con el registro contable histórico que se va a editar.
     */
    public void cargarCorteParaEdicion(corteCajaDTO corte) {
        limpiarFormulario();

        this.idCorteEditando = corte.getIdCaja();
        this.montoEsperadoEditando = corte.getMontoEsperado();

        for (int i = 0; i < cmbEmpleados.getItemCount(); i++) {
            if (cmbEmpleados.getItemAt(i).getIdCajero() == corte.getIdCajero()) {
                cmbEmpleados.setSelectedIndex(i);
                break;
            }
        }

        pnlContenedorFilas.removeAll();

        if (corte.getListaDesglose() != null && !corte.getListaDesglose().isEmpty()) {
            for (desgloseDTO d : corte.getListaDesglose()) {
                if (pnlContenedorFilas.getComponentCount() < 4) {
                    FilaMontoPanel fila = new FilaMontoPanel(this);
                    fila.setDatos(d.getNombreMetodo(), String.valueOf(d.getMontoDeclarado()));
                    pnlContenedorFilas.add(fila);
                }
            }
        } else {
            agregarFila();
        }

        pnlContenedorFilas.revalidate();
        pnlContenedorFilas.repaint();
        calcularTotales();
    }

    @Override public void onCambio() { calcularTotales(); }

    @Override public boolean verificarDuplicado(String metodo, FilaMontoPanel origen) {
        for (Component c : pnlContenedorFilas.getComponents()) {
            if (c instanceof FilaMontoPanel && c != origen && ((FilaMontoPanel) c).getMetodo().equals(metodo)) return true;
        }
        return false;
    }

    /**
     * Clase interna personalizada para dibujar paneles JPanel con esquinas curvas
     * utilizando suavizado de bordes con Graphics2D.
     */
    class RoundedPanel extends JPanel {
        private int radius;
        private Color bgColor;

        /**
         * Inicializa las propiedades de curvatura y fondo del panel redondeado.
         * * @param radius Radio para redondear las esquinas del contenedor.
         * @param bgColor Color de fondo base asignado al panel.
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
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * Clase interna para personalizar los botones del formulario con bordes
     * redondeados y efectos visuales de interacción mediante Graphics2D.
     */
    class RoundedButton extends JButton {
        private int radius;
        private Color bgColor;
        private String customText;

        /**
         * Configura el texto, colores y fuentes del botón redondo, desactivando
         * los estilos por defecto de Java Swing.
         * * @param text Texto descriptivo que se mostrará dentro del botón.
         * @param radius Radio de curvatura para redondear las esquinas del botón.
         * @param bgColor Color del fondo base asignado al componente.
         * @param fgColor Color asignado a las fuentes de texto del botón.
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
        }

        /**
         * Permite inyectar código estructurado HTML dentro del botón para
         * personalizar el formateo del contenido.
         * * @param html Cadena de texto estructurada con etiquetas HTML.
         */
        public void setTextHtml(String html) {
            super.setText(html);
            this.customText = null;
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isPressed() ? bgColor.darker() : bgColor);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
            if (customText != null) {
                g2.setColor(getForeground());
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(customText, (getWidth() - fm.stringWidth(customText)) / 2, ((getHeight() - fm.getHeight()) / 2) + fm.getAscent());
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }
}