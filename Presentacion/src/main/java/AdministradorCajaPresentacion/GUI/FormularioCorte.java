package AdministradorCajaPresentacion.GUI;

import AdministradorCajaDTOs.cajeroDTO;
import AdministradorCajaDTOs.desgloseDTO;
import AdministradorCajaPresentacion.Control.Control;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FormularioCorte extends JFrame {

    private Control control;
    private JPanel pnlContenedorFilas;
    private JComboBox<cajeroDTO> cmbEmpleados;
    private JLabel lblTotalManualBadge, lblFaltanteBadge, lblPreviewImagen;
    private String rutaImagenSeleccionada = "";

    private final Color COLOR_FONDO = new Color(34, 34, 34);
    private final Color COLOR_TEXTO = new Color(240, 240, 240);
    private final Color COLOR_ACCENTO = new Color(45, 212, 112);
    private final Color COLOR_CARD = new Color(60, 60, 60);
    private final Color COLOR_DIVISOR = new Color(85, 85, 85);
    private final Color COLOR_ROJO_SUAVE = new Color(255, 107, 107);

    public FormularioCorte(Control control) {
        this.control = control;
        initComponents();
        configurarVentana();
        agregarFila();
    }

    private void configurarVentana() {
        setTitle("GoOrder - Nuevo Corte");
        setSize(480, 780);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
    }

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

        JTextField txtSupervisor = new JTextField("Jesus Martinez");
        txtSupervisor.setEditable(false);
        pnlPrincipal.add(crearFilaPildora("Supervisor:", txtSupervisor));
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));

        cmbEmpleados = new JComboBox<>();
        cmbEmpleados.addActionListener(e -> calcularTotales());
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

        lblTotalManualBadge = new JLabel("$0", SwingConstants.CENTER);
        lblTotalManualBadge.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalManualBadge.setForeground(Color.BLACK);
        RoundedPanel badgeTotal = new RoundedPanel(15, Color.WHITE);
        badgeTotal.setPreferredSize(new Dimension(100, 25));
        badgeTotal.add(lblTotalManualBadge);

        pnlTotalLine.add(lblTotalText);
        pnlTotalLine.add(badgeTotal);

        // Badge Faltante
        JPanel pnlFaltanteLine = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlFaltanteLine.setOpaque(false);
        JLabel lblFaltanteText = new JLabel("Faltante:");
        lblFaltanteText.setForeground(COLOR_TEXTO);
        lblFaltanteText.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFaltanteText.setPreferredSize(new Dimension(75, 30));

        lblFaltanteBadge = new JLabel("$0", SwingConstants.CENTER);
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

        // Botón Verificar
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

        if (comp instanceof JComboBox) {
            comp.setBackground(Color.WHITE);
        }

        pill.add(comp, BorderLayout.CENTER);

        p.add(l, BorderLayout.WEST);
        p.add(pill, BorderLayout.CENTER);
        return p;
    }

    private void agregarFila() {
        FilaMonto fila = new FilaMonto();
        pnlContenedorFilas.add(fila);
        pnlContenedorFilas.revalidate();
        calcularTotales();
    }

    private void quitarFila() {
        int count = pnlContenedorFilas.getComponentCount();
        if (count > 0) {
            pnlContenedorFilas.remove(count - 1);
            pnlContenedorFilas.revalidate();
            pnlContenedorFilas.repaint();
            calcularTotales();
        }
    }

    private void adjuntarImagen() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "png", "jpeg"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            rutaImagenSeleccionada = file.getAbsolutePath();
            ImageIcon icon = new ImageIcon(new ImageIcon(rutaImagenSeleccionada).getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH));
            lblPreviewImagen.setIcon(icon);
        }
    }

    public void calcularTotales() {
        double total = 0;
        for (Component c : pnlContenedorFilas.getComponents()) {
            if (c instanceof FilaMonto) {
                total += ((FilaMonto) c).getMonto();
            }
        }
        lblTotalManualBadge.setText(String.format("$%.0f", total));

        cajeroDTO emp = null;
        if(cmbEmpleados.getSelectedItem() != null) {
            emp = (cajeroDTO) cmbEmpleados.getSelectedItem();
        }

        if (emp != null) {
            double esperado = control.obtenerMontoEsperado(emp.getIdCajero());
            double faltante = esperado - total;
            lblFaltanteBadge.setText(String.format("$%.0f", Math.max(0, faltante)));
        }
    }

    private void realizarCorte() {
        double totalContado = 0;
        List<desgloseDTO> desgloses = new ArrayList<>();

        for (Component c : pnlContenedorFilas.getComponents()) {
            if (c instanceof FilaMonto) {
                FilaMonto f = (FilaMonto) c;
                totalContado += f.getMonto();
                desgloses.add(new desgloseDTO(f.getMonto(), 0, f.getMetodo()));
            }
        }

        cajeroDTO emp = (cajeroDTO) cmbEmpleados.getSelectedItem();
        if (emp != null) {
            // Obtenemos el esperado del sistema
            double esperado = control.obtenerMontoEsperado(emp.getIdCajero());

            // Llamamos a la nueva pantalla de conciliación pasándole los datos
            control.mostrarConciliacionFinal(esperado, totalContado, emp, desgloses, rutaImagenSeleccionada);
        } else {
            JOptionPane.showMessageDialog(this, "Por favor selecciona un empleado.");
        }
    }

    class FilaMonto extends JPanel {
        private JComboBox<String> cbMetodo;
        private JTextField txtMonto;

        public FilaMonto() {
            setLayout(new GridLayout(1, 2, 0, 0));
            setOpaque(false);
            setMaximumSize(new Dimension(500, 45));
            setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_DIVISOR)); // Divisor horizontal abajo

            cbMetodo = new JComboBox<>(new String[]{"Efectivo", "Tarjeta", "App", "Referencia"});
            cbMetodo.setFont(new Font("Segoe UI", Font.BOLD, 14));
            cbMetodo.setForeground(Color.BLACK);
            cbMetodo.setBackground(Color.WHITE);

            txtMonto = new JTextField("0");
            txtMonto.setHorizontalAlignment(JTextField.CENTER);
            txtMonto.setFont(new Font("Segoe UI", Font.BOLD, 16));
            txtMonto.setForeground(COLOR_TEXTO);
            txtMonto.setOpaque(false);
            txtMonto.setBorder(null);
            txtMonto.setCaretColor(Color.WHITE);
            txtMonto.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                @Override
                public void insertUpdate(javax.swing.event.DocumentEvent e) { calcularTotales(); }
                @Override
                public void removeUpdate(javax.swing.event.DocumentEvent e) { calcularTotales(); }
                @Override
                public void changedUpdate(javax.swing.event.DocumentEvent e) { calcularTotales(); }
            });
            RoundedPanel pnlComboWrap = new RoundedPanel(15, Color.WHITE);
            pnlComboWrap.setLayout(new BorderLayout());
            pnlComboWrap.add(cbMetodo, BorderLayout.CENTER);

            JPanel pnlLeft = new JPanel(new BorderLayout());
            pnlLeft.setOpaque(false);
            pnlLeft.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, COLOR_DIVISOR)); // Divisor vertical
            pnlLeft.add(pnlComboWrap, BorderLayout.CENTER);
            pnlLeft.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 0, 1, COLOR_DIVISOR),
                    new EmptyBorder(8, 15, 8, 15) // Padding interno
            ));

            add(pnlLeft);
            add(txtMonto);
        }

        public double getMonto() {
            try {
                return Double.parseDouble(txtMonto.getText().replace("$", ""));
            } catch (Exception e) { return 0; }
        }
        public String getMetodo() { return cbMetodo.getSelectedItem().toString(); }
    }

    public void cargarEmpleados(List<cajeroDTO> empleados) {
        cmbEmpleados.removeAllItems();
        if (empleados != null) {
            for (cajeroDTO emp : empleados) {
                cmbEmpleados.addItem(emp);
            }
        }
    }


    class RoundedPanel extends JPanel {
        private int radius;
        private Color bgColor;
        public RoundedPanel(int radius, Color bgColor) {
            this.radius = radius;
            this.bgColor = bgColor;
            setOpaque(false);
            setLayout(new BorderLayout());
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    class RoundedButton extends JButton {
        private int radius;
        private Color bgColor;
        private String customText;
        private String htmlText = null;

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

        public void setTextHtml(String html) {
            this.htmlText = html;
            super.setText(html);
            this.customText = null;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (getModel().isPressed()) {
                g2.setColor(bgColor.darker());
            } else {
                g2.setColor(bgColor);
            }
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));

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
    public void limpiarFormulario() {
        pnlContenedorFilas.removeAll();

        // 2. Quitamos la imagen
        rutaImagenSeleccionada = "";
        lblPreviewImagen.setIcon(null);

        if (cmbEmpleados.getItemCount() > 0) {
            cmbEmpleados.setSelectedIndex(0);
        }

        agregarFila();
    }
}