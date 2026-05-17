
package GUI;

import Control.Control;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import org.example.NegocioException;

public class PagoTarjeta extends JFrame {

    private Control control;

    private JTextField txtNombre;
    private JTextField txtNumeroTarjeta;
    private JComboBox<String> cbMes;
    private JComboBox<String> cbAnio;
    private JPasswordField txtCVV;

    public PagoTarjeta(Control control) {
        this.control = control;

        setTitle("GoOrder - Pago con Tarjeta");
        setSize(400, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(control.COLOR_FONDO);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(control.COLOR_FONDO);
        headerPanel.setBorder(new EmptyBorder(20, 20, 10, 20));

        JButton btnRegresar = new JButton("←");
        btnRegresar.setFont(new Font("Arial", Font.BOLD, 24));
        btnRegresar.setForeground(Color.LIGHT_GRAY);
        btnRegresar.setContentAreaFilled(false);
        btnRegresar.setBorderPainted(false);
        btnRegresar.setFocusPainted(false);
        btnRegresar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnRegresar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnRegresar.setForeground(control.COLOR_NEON); }
            public void mouseExited(MouseEvent e) { btnRegresar.setForeground(Color.LIGHT_GRAY); }
        });

        btnRegresar.addActionListener(e -> {
            control.mostrarSeleccionFormaPago();
        });

        JLabel lblTitulo = new JLabel("PAGO CON TARJETA");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(control.COLOR_NEON);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblEspacio = new JLabel("    ");
        lblEspacio.setPreferredSize(btnRegresar.getPreferredSize());

        headerPanel.add(btnRegresar, BorderLayout.WEST);
        headerPanel.add(lblTitulo, BorderLayout.CENTER);
        headerPanel.add(lblEspacio, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(control.COLOR_FONDO);
        contentPanel.setBorder(new EmptyBorder(10, 30, 20, 30));

        txtNombre = crearTextFieldEstilizado();
        contentPanel.add(crearSeccion("Nombre de titular", txtNombre));
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        String[] meses = {"01 - Ene", "02 - Feb", "03 - Mar", "04 - Abr", "05 - May", "06 - Jun", "07 - Jul", "08 - Ago", "09 - Sep", "10 - Oct", "11 - Nov", "12 - Dic"};
        String[] anios = {"2026", "2027", "2028", "2029", "2030", "2031", "2032"};

        cbMes = crearComboBoxEstilizado(meses);
        cbAnio = crearComboBoxEstilizado(anios);

        JPanel panelFecha = new JPanel(new GridLayout(1, 2, 15, 0));
        panelFecha.setBackground(control.COLOR_FONDO);
        panelFecha.add(cbMes);
        panelFecha.add(cbAnio);

        contentPanel.add(crearSeccion("Fecha de vencimiento", panelFecha));
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        txtNumeroTarjeta = crearTextFieldEstilizado();
        contentPanel.add(crearSeccion("Número de tarjeta", txtNumeroTarjeta));
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        txtCVV = new JPasswordField();
        txtCVV.setBackground(control.COLOR_TARJETA);
        txtCVV.setForeground(Color.WHITE);
        txtCVV.setFont(new Font("Arial", Font.BOLD, 16));
        txtCVV.setCaretColor(control.COLOR_NEON);
        txtCVV.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JPanel panelCVV = new JPanel(new BorderLayout());
        panelCVV.setBackground(control.COLOR_FONDO);
        panelCVV.add(txtCVV, BorderLayout.WEST);
        txtCVV.setPreferredSize(new Dimension(100, 45));

        contentPanel.add(crearSeccion("Código de seguridad (CVV)", panelCVV));

        contentPanel.add(Box.createVerticalGlue());
        add(contentPanel, BorderLayout.CENTER);


        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(control.COLOR_FONDO);
        footerPanel.setBorder(new EmptyBorder(10, 30, 30, 30));
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));

        BotonNeon btnPagar = new BotonNeon("PAGAR");

        btnPagar.addActionListener(e -> {
            try {
                boolean pagoExitoso = control.intentarPago(3, txtNumeroTarjeta.getText(), control.getCarrito().getTotal());
                if (pagoExitoso) {
                    JOptionPane.showMessageDialog(this, "¡Pago aprobado con éxito! Tu pedido está confirmado.", "Transacción Exitosa", JOptionPane.INFORMATION_MESSAGE);
                    control.mostrarAgradecimiento();
                    control.limpiarCarrito();
                } else {
                    JOptionPane.showMessageDialog(this, "El pago fue rechazado. Verifica el número o tus fondos.", "Transacción Declinada", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        footerPanel.add(btnPagar);
        add(footerPanel, BorderLayout.SOUTH);
    }


    private JPanel crearSeccion(String titulo, JComponent input) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(control.COLOR_FONDO);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setForeground(Color.LIGHT_GRAY);

        panel.add(lblTitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));

        input.setAlignmentX(Component.LEFT_ALIGNMENT);
        input.setMaximumSize(new Dimension(340, 45));
        panel.add(input);

        return panel;
    }

    private JTextField crearTextFieldEstilizado() {
        JTextField txt = new JTextField();
        txt.setBackground(control.COLOR_TARJETA);
        txt.setForeground(Color.WHITE);
        txt.setFont(new Font("Arial", Font.BOLD, 16));
        txt.setCaretColor(control.COLOR_NEON);
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true),
                new EmptyBorder(10, 10, 10, 10)
        ));
        return txt;
    }

    private JComboBox<String> crearComboBoxEstilizado(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setBackground(control.COLOR_TARJETA);
        cb.setForeground(Color.WHITE);
        cb.setFont(new Font("Arial", Font.PLAIN, 14));
        cb.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1, true));
        cb.setFocusable(false);
        return cb;
    }

    class BotonNeon extends JButton {
        private boolean over = false;

        public BotonNeon(String texto) {
            super(texto);
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setAlignmentX(Component.CENTER_ALIGNMENT);
            setPreferredSize(new Dimension(340, 50));
            setMaximumSize(new Dimension(340, 50));

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { over = true; repaint(); }
                public void mouseExited(MouseEvent e) { over = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (over) g2.setColor(control.COLOR_NEON);
            else g2.setColor(control.COLOR_TARJETA);

            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

            g2.setFont(new Font("Arial", Font.BOLD, 14));
            if (over) {
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(2));
                g2.setColor(Color.WHITE);
                g2.drawRoundRect(2, 2, getWidth()-5, getHeight()-5, 15, 15);
                g2.setColor(Color.BLACK);
            } else {
                g2.setColor(Color.WHITE);
            }

            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(getText())) / 2;
            int y = (getHeight() + fm.getAscent()) / 2 - 2;
            g2.drawString(getText(), x, y);
            g2.dispose();
        }
    }
}