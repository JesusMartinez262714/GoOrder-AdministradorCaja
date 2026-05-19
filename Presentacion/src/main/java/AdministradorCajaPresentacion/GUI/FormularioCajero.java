package AdministradorCajaPresentacion.GUI;

import AdministradorCajaDTOs.cajeroDTO;
import AdministradorCajaPresentacion.Control.Control;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Pantalla de diálogo para el registro y edición de cajeros en el sistema GoOrder.
 * Permite capturar el nombre, apellido y el turno asignado al empleado.
 * * @author Jesus Manuel Martinez Cortez
 * @version 1.0
 */
public class FormularioCajero extends JDialog {

    private Control control;
    private cajeroDTO cajeroOriginal;
    private boolean esEdicion;

    private JTextField txtNombre;
    private JTextField txtApellido;
    private JComboBox<String> cmbTurno;
    private JLabel lblTitulo;

    private final Color COLOR_FONDO = new Color(34, 34, 34);
    private final Color COLOR_ACCENTO = new Color(66, 206, 126);
    private final Color COLOR_CAMPO = new Color(55, 55, 55);

    /**
     * Constructor que inicializa los componentes del formulario de cajeros
     * y determina si es un registro nuevo o una edición.
     * * @param parent Ventana principal sobre la que se posiciona el diálogo modal.
     * @param control Instancia del controlador para la comunicación de la vista con el negocio.
     * @param cajero Objeto DTO del cajero a modificar, o null si es un nuevo registro.
     */
    public FormularioCajero(JFrame parent, Control control, cajeroDTO cajero) {
        super(parent, true);
        this.control = control;
        this.cajeroOriginal = cajero;
        this.esEdicion = (cajero != null);

        initComponents();
        configurarVentana();

        if (esEdicion) {
            cargarDatos();
        }
    }

    /**
     * Configura las propiedades básicas de la ventana, como el tamaño, título y posición.
     */
    private void configurarVentana() {
        setSize(400, 450);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setTitle(esEdicion ? "GoOrder - Editar Cajero" : "GoOrder - Nuevo Cajero");
    }

    /**
     * Inicializa y acomoda todos los componentes gráficos del formulario,
     * como campos de texto, etiquetas y botones.
     */
    private void initComponents() {
        JPanel pnlPrincipal = new JPanel();
        pnlPrincipal.setLayout(new BoxLayout(pnlPrincipal, BoxLayout.Y_AXIS));
        pnlPrincipal.setBackground(COLOR_FONDO);
        pnlPrincipal.setBorder(new EmptyBorder(30, 40, 30, 40));

        lblTitulo = new JLabel(esEdicion ? "Editar Cajero" : "Nuevo Cajero");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlPrincipal.add(lblTitulo);
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 30)));

        txtNombre = new JTextField();
        estilizarCampo(txtNombre);
        pnlPrincipal.add(crearEtiqueta("Nombre(s):"));
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlPrincipal.add(txtNombre);
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));

        txtApellido = new JTextField();
        estilizarCampo(txtApellido);
        pnlPrincipal.add(crearEtiqueta("Apellido(s):"));
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlPrincipal.add(txtApellido);
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));

        cmbTurno = new JComboBox<>(new String[]{"Matutino", "Vespertino"});
        cmbTurno.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cmbTurno.setBackground(Color.WHITE);
        cmbTurno.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        pnlPrincipal.add(crearEtiqueta("Asignar Turno:"));
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlPrincipal.add(cmbTurno);
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 40)));

        JPanel pnlBotones = new JPanel(new GridLayout(1, 2, 15, 0));
        pnlBotones.setOpaque(false);

        RoundedButton btnCancelar = new RoundedButton("Cancelar", 10, new Color(70, 70, 70), Color.WHITE);
        btnCancelar.addActionListener(e -> dispose());

        RoundedButton btnGuardar = new RoundedButton(esEdicion ? "Actualizar" : "Guardar", 10, COLOR_ACCENTO, Color.BLACK);
        btnGuardar.addActionListener(e -> validarYGuardar());

        pnlBotones.add(btnCancelar);
        pnlBotones.add(btnGuardar);

        pnlPrincipal.add(pnlBotones);
        add(pnlPrincipal);
    }

    /**
     * Carga los datos del cajero seleccionado en los campos de texto cuando
     * se abre la ventana en modo edición.
     */
    private void cargarDatos() {
        String[] partes = cajeroOriginal.getNombreCompleto().split(" ", 2);
        txtNombre.setText(partes[0]);
        if (partes.length > 1) {
            txtApellido.setText(partes[1]);
        }
        cmbTurno.setSelectedItem(cajeroOriginal.getTurno());
    }

    /**
     * Valida que los campos no estén vacíos, tengan la longitud adecuada y cumplan
     * con el formato de letras antes de enviar los datos al controlador.
     */
    private void validarYGuardar() {
        String nombre = txtNombre.getText().trim().replaceAll("\\s+", " ");
        String apellido = txtApellido.getText().trim().replaceAll("\\s+", " ");
        String turno = (String) cmbTurno.getSelectedItem();

        if (nombre.isEmpty() || apellido.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios y no pueden contener solo espacios.", "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String regexEstructura = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ]+( [a-zA-ZáéíóúÁÉÍÓÚñÑüÜ]+)*$";
        if (!nombre.matches(regexEstructura) || !apellido.matches(regexEstructura)) {
            JOptionPane.showMessageDialog(this, "Los nombres y apellidos deben iniciar con letras y no poseer espacios consecutivos.", "Formato Incorrecto", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (nombre.length() < 2 || apellido.length() < 2) {
            JOptionPane.showMessageDialog(this, "Los campos de texto deben poseer una longitud mínima de 2 caracteres.", "Longitud Insuficiente", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombreCompleto = nombre + " " + apellido;
        boolean exito;

        if (esEdicion) {
            cajeroOriginal.setNombreCompleto(nombreCompleto);
            cajeroOriginal.setTurno(turno);
            exito = control.editarCajero(cajeroOriginal);
        } else {
            cajeroDTO nuevo = new cajeroDTO(0, nombreCompleto, turno);
            exito = control.registrarCajero(nuevo);
        }

        if (exito) {
            dispose();
        }
    }

    /**
     * Crea una etiqueta JLabel configurada con los estilos de fuente
     * y color requeridos para el diseño del formulario.
     * * @param texto Contenido descriptivo que mostrará la etiqueta.
     * @return El componente JLabel configurado.
     */
    private JLabel crearEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(new Color(180, 180, 180));
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    /**
     * Aplica el diseño visual a un campo de texto y le asigna el filtro
     * DocumentFilter para validar la entrada de datos por teclado.
     * * @param campo Componente JTextField que se desea formatear y proteger.
     */
    private void estilizarCampo(JTextField campo) {
        campo.setBackground(COLOR_CAMPO);
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(Color.WHITE);
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80)),
                new EmptyBorder(8, 10, 8, 10)
        ));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new FiltroTextoRobusto());
    }

    /**
     * Filtro para los campos de texto que valida mediante expresiones regulares
     * que solo se puedan escribir letras y espacios, limitando la longitud máxima.
     */
    private class FiltroTextoRobusto extends DocumentFilter {
        private final int limiteMaximo = 30;
        private final String regexAlfabetico = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]+$";

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string == null) return;
            if ((fb.getDocument().getLength() + string.length()) <= limiteMaximo && string.matches(regexAlfabetico)) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null) return;
            if ((fb.getDocument().getLength() - length + text.length()) <= limiteMaximo && text.matches(regexAlfabetico)) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }

    /**
     * Clase interna para personalizar los botones de la interfaz con bordes
     * redondeados y efectos de color mediante Graphics2D.
     */
    class RoundedButton extends JButton {
        private int radius;
        private Color bgColor;

        /**
         * Configura el texto, colores y fuentes del botón redondo, desactivando
         * los estilos por defecto de Java Swing.
         * * @param text Texto que se va a mostrar dentro del botón.
         * @param radius Radio para redondear las esquinas del botón.
         * @param bgColor Color del fondo base asignado al botón.
         * @param fgColor Color asignado al texto del botón.
         */
        public RoundedButton(String text, int radius, Color bgColor, Color fgColor) {
            super(text);
            this.radius = radius;
            this.bgColor = bgColor;
            setForeground(fgColor);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isPressed() ? bgColor.darker() : bgColor);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
            g2.dispose();
            super.paintComponent(g);
        }
    }
}