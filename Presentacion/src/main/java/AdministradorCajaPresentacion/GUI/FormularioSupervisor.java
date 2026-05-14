package AdministradorCajaPresentacion.GUI;

import AdministradorCajaDTOs.supervisorDTO;
import AdministradorCajaPresentacion.Control.Control;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;


public class FormularioSupervisor extends JDialog {

    private Control control;
    private supervisorDTO supervisorOriginal;
    private boolean esEdicion;

    private JTextField txtNombre;
    private JTextField txtApellido;
    private JLabel lblTitulo;

    private final Color COLOR_FONDO = new Color(34, 34, 34);
    private final Color COLOR_ACCENTO = new Color(66, 206, 126);
    private final Color COLOR_CAMPO = new Color(55, 55, 55);


    public FormularioSupervisor(JFrame parent, Control control, supervisorDTO supervisor) {
        super(parent, true); // Modal
        this.control = control;
        this.supervisorOriginal = supervisor;
        this.esEdicion = (supervisor != null);

        initComponents();
        configurarVentana();

        if (esEdicion) {
            cargarDatos();
        }
    }

    private void configurarVentana() {
        setSize(400, 380);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setTitle(esEdicion ? "GoOrder - Editar Supervisor" : "GoOrder - Nuevo Supervisor");
    }

    private void initComponents() {
        JPanel pnlPrincipal = new JPanel();
        pnlPrincipal.setLayout(new BoxLayout(pnlPrincipal, BoxLayout.Y_AXIS));
        pnlPrincipal.setBackground(COLOR_FONDO);
        pnlPrincipal.setBorder(new EmptyBorder(30, 40, 30, 40));

        lblTitulo = new JLabel(esEdicion ? "Editar Supervisor" : "Nuevo Supervisor");
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
        pnlPrincipal.add(Box.createRigidArea(new Dimension(0, 35)));

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


    private void cargarDatos() {
        String[] partes = supervisorOriginal.getNombreCompleto().split(" ", 2);
        txtNombre.setText(partes[0]);
        if (partes.length > 1) {
            txtApellido.setText(partes[1]);
        }
    }


    private void validarYGuardar() {
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();

        if (nombre.isEmpty() || apellido.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombreCompleto = nombre + " " + apellido;
        boolean exito;

        if (esEdicion) {
            supervisorOriginal.setNombreCompleto(nombreCompleto);
            exito = control.editarSupervisor(supervisorOriginal);
        } else {
            supervisorDTO nuevo = new supervisorDTO(0, nombreCompleto);
            exito = control.registrarSupervisor(nuevo);
        }

        if (exito) {
            dispose();
        }
    }


    private JLabel crearEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(new Color(180, 180, 180));
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

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
    }

    class RoundedButton extends JButton {
        private int radius;
        private Color bgColor;

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