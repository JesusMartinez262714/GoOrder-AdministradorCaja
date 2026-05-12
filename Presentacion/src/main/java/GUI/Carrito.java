package GUI;

import Control.Control;
import GoOrderDTO.CarritoDTO;
import GoOrderDTO.ProductoSeleccionadoDTO;
import Pattern.IngresarImagen;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Esta clase representa la pantalla del "Carrito de Compras" de la aplicación.
 * Aquí el usuario puede ver los productos que ha elegido, cambiar la cantidad,
 * eliminarlos si ya no los quiere, y ver el subtotal a pagar antes de
 * elegir cómo quiere que se lo entreguen.
 */
public class Carrito extends JFrame {

    private Control control;

    /**
     * Constructor de la ventana del carrito.
     * Se encarga de armar toda la pantalla: pone el color de fondo, acomoda la lista
     * de productos que están en el carrito y prepara el botón para pasar a la
     * forma de entrega. Si el carrito está vacío, muestra un mensaje avisando.
     *
     * @param control El controlador principal que maneja los datos y los cambios de pantalla.
     */
    public Carrito(Control control) {
        this.control = control;

        setTitle("GoOrder - Carrito");
        setSize(400, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(control.COLOR_FONDO);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(control.COLOR_FONDO);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JButton btnRegresar = new JButton("←");
        btnRegresar.setFont(new Font("Arial", Font.BOLD, 24));
        btnRegresar.setForeground(Color.LIGHT_GRAY);
        btnRegresar.setContentAreaFilled(false);
        btnRegresar.setBorderPainted(false);
        btnRegresar.setFocusPainted(false);
        btnRegresar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnRegresar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnRegresar.setForeground(control.COLOR_NEON);
            }
            public void mouseExited(MouseEvent e) {
                btnRegresar.setForeground(Color.LIGHT_GRAY);
            }
        });

        btnRegresar.addActionListener(e -> {
            try {
                control.mostrarProductosFORM();
            } catch (Exception ex) {
                Logger.getLogger(Carrito.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        JLabel lblTitulo = new JLabel("MI CARRITO");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(control.COLOR_NEON);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblEspacio = new JLabel("    ");
        lblEspacio.setPreferredSize(btnRegresar.getPreferredSize());

        headerPanel.add(btnRegresar, BorderLayout.WEST);
        headerPanel.add(lblTitulo, BorderLayout.CENTER);
        headerPanel.add(lblEspacio, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(control.COLOR_FONDO);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        double subtotalMostrar = 0.0;

        try {
            CarritoDTO miCarrito = control.getCarrito();

            if (miCarrito != null && miCarrito.getProductos() != null && !miCarrito.getProductos().isEmpty()) {

                subtotalMostrar = miCarrito.getSubTotal();

                for (ProductoSeleccionadoDTO producto : miCarrito.getProductos()) {
                    contentPanel.add(crearPanelProducto(producto));
                    contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
                }

                contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
                JSeparator sep = new JSeparator();
                sep.setForeground(Color.DARK_GRAY);
                sep.setMaximumSize(new Dimension(340, 5));
                contentPanel.add(sep);
                contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));

                for (ProductoSeleccionadoDTO producto : miCarrito.getProductos()) {
                    contentPanel.add(crearFilaResumen(
                            producto.getNombre() + " x" + producto.getCantidad(),
                            String.format("$%.2f", producto.getImporte()),
                            false
                    ));
                    contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                }

                contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                contentPanel.add(crearFilaResumen("Subtotal", String.format("$%.2f", subtotalMostrar), true));
//lblMas
            } else {
                JLabel lblVacio = new JLabel("Tu carrito está vacío.");
                lblVacio.setFont(new Font("Arial", Font.ITALIC, 16));
                lblVacio.setForeground(Color.GRAY);
                lblVacio.setAlignmentX(Component.CENTER_ALIGNMENT);
                contentPanel.add(Box.createRigidArea(new Dimension(0, 50)));
                contentPanel.add(lblVacio);
            }

        } catch (Exception ex) {
            Logger.getLogger(Carrito.class.getName()).log(Level.SEVERE, "Error al cargar el carrito", ex);
            JOptionPane.showMessageDialog(this, "Error al cargar el carrito", "Error", JOptionPane.ERROR_MESSAGE);
        }

        contentPanel.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(control.COLOR_FONDO);
        scrollPane.getViewport().setBackground(control.COLOR_FONDO);

        add(scrollPane, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(control.COLOR_FONDO);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));

        BotonNeon btnEntrega = new BotonNeon("FORMA DE ENTREGA");

        if (subtotalMostrar == 0.0) {
            btnEntrega.setEnabled(false);
        }

        btnEntrega.addActionListener(e -> {
            if (control != null) {
                control.mostrarSeleccionMetodoEntrega();
            }
        });

        footerPanel.add(btnEntrega);
        add(footerPanel, BorderLayout.SOUTH);
    }

    /**
     * Crea una "tarjeta" visual para un producto específico dentro de la lista del carrito.
     * Esta tarjeta incluye la foto del producto, su nombre, precio, y los botones
     * para sumar (+), restar (-) o eliminar (el bote de basura) el producto.
     *
     * @param producto Los datos del producto que se va a mostrar en esta tarjeta.
     * @return Un panel (caja visual) listo para agregarse a la pantalla.
     */
    private JPanel crearPanelProducto(ProductoSeleccionadoDTO producto) {
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 10));
        panelPrincipal.setBackground(control.COLOR_TARJETA);
        panelPrincipal.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(control.COLOR_TARJETA, 2, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panelPrincipal.setMaximumSize(new Dimension(340, 100));

        JLabel lblImagen = new JLabel();
        lblImagen.setForeground(Color.GRAY);
        lblImagen.setPreferredSize(new Dimension(70, 70));
        lblImagen.setOpaque(true);
        lblImagen.setBackground(new Color(50, 50, 50));
        lblImagen.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        String nombreImagen = producto.getProducto().getImagen();
        IngresarImagen.ingresarImagen(lblImagen, nombreImagen, 100, 70);

        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBackground(control.COLOR_TARJETA);
        panelInfo.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        JLabel lblNombre = new JLabel(producto.getNombre());
        lblNombre.setFont(new Font("Arial", Font.BOLD, 16));
        lblNombre.setForeground(Color.WHITE);

        JLabel lblPrecio = new JLabel(String.format("$%.2f", producto.getPrecioActual()));
        lblPrecio.setFont(new Font("Arial", Font.PLAIN, 14));
        lblPrecio.setForeground(control.COLOR_NEON);

        JLabel lblBasura = new JLabel("🗑");
        lblBasura.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        lblBasura.setForeground(Color.GRAY);
        lblBasura.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblBasura.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                lblBasura.setForeground(new Color(255, 80, 80));
            }
            public void mouseExited(MouseEvent e) {
                lblBasura.setForeground(Color.GRAY);
            }
            public void mouseClicked(MouseEvent e) {
                try {
                    control.eliminarProducto(producto);
                } catch (Exception ex) {
                    Logger.getLogger(Carrito.class.getName()).log(Level.SEVERE, null, ex);
                }
                control.mostrarCarrito();
            }
        });

        panelInfo.add(lblNombre);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 5)));
        panelInfo.add(lblPrecio);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 5)));
        panelInfo.add(lblBasura);

        JPanel panelControles = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        panelControles.setBackground(control.COLOR_TARJETA);

        JLabel lblMenos = new JLabel("—");
        lblMenos.setFont(new Font("Arial", Font.BOLD, 16));
        lblMenos.setForeground(Color.WHITE);
        lblMenos.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblCantidad = new JLabel(String.valueOf(producto.getCantidad()));
        lblCantidad.setFont(new Font("Arial", Font.BOLD, 16));
        lblCantidad.setForeground(Color.WHITE);

        JLabel lblMas = new JLabel("+");
        lblMas.setFont(new Font("Arial", Font.BOLD, 18));
        lblMas.setForeground(control.COLOR_NEON);
        lblMas.setCursor(new Cursor(Cursor.HAND_CURSOR));

        lblMas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                try {
                    if (producto.getCantidad() < 50) {
                        control.incrementarCantidad(producto);
                        control.mostrarCarrito();
                    } else {
                        JOptionPane.showMessageDialog(
                                Carrito.this,
                                "Has alcanzado el límite de 50 unidades para este producto.",
                                "Límite de cantidad",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                } catch (Exception ex) {
                    Logger.getLogger(Carrito.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        lblMenos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                try {
                    control.decrementarCantidad(producto);
                    if (producto.getCantidad() <= 0) {
                        control.mostrarCarrito();
                        return;
                    }
                    control.mostrarCarrito();
                } catch (Exception ex) {
                    Logger.getLogger(Carrito.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        panelControles.add(lblMenos);
        panelControles.add(lblCantidad);
        panelControles.add(lblMas);

        panelPrincipal.add(lblImagen, BorderLayout.WEST);
        panelPrincipal.add(panelInfo, BorderLayout.CENTER);
        panelPrincipal.add(panelControles, BorderLayout.EAST);

        return panelPrincipal;
    }

    /**
     * Crea un pequeño renglón de texto para la sección del resumen de cobro (hasta abajo).
     * Sirve para mostrar cosas como "Hamburguesa x2 ---- $100.00" o el subtotal final.
     *
     * @param textoIzquierda El texto que va del lado izquierdo (ej. el nombre del producto).
     * @param textoDerecha El texto que va del lado derecho (ej. el precio).
     * @param esTotal Un valor que nos dice si este renglón es el total principal (para pintarlo más grande y llamativo).
     * @return Un panel con los textos acomodados en los extremos listos para mostrarse.
     */
    private JPanel crearFilaResumen(String textoIzquierda, String textoDerecha, boolean esTotal) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(control.COLOR_FONDO);
        panel.setMaximumSize(new Dimension(340, 25));

        Font fuente = esTotal ? new Font("Arial", Font.BOLD, 18) : new Font("Arial", Font.PLAIN, 16);
        Color colorTexto = esTotal ? control.COLOR_NEON : Color.WHITE;

        JLabel lblIzq = new JLabel(textoIzquierda);
        lblIzq.setFont(fuente);
        lblIzq.setForeground(colorTexto);

        JLabel lblDer = new JLabel(textoDerecha);
        lblDer.setFont(fuente);
        lblDer.setForeground(colorTexto);

        panel.add(lblIzq, BorderLayout.WEST);
        panel.add(lblDer, BorderLayout.EAST);

        return panel;
    }

    /**
     * Clase interna para crear botones personalizados en la pantalla del carrito.
     * Le da un estilo oscuro con bordes redondeados y hace que cambie a un color
     * neón brillante cuando pasamos el ratón por encima, dándole un toque moderno.
     */
    class BotonNeon extends JButton {

        private boolean over = false;

        /**
         * Crea el botón con su diseño especial.
         *
         * @param texto La palabra que se leerá dentro del botón.
         */
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
                public void mouseEntered(MouseEvent e) {
                    if (isEnabled()) {
                        over = true;
                        repaint();
                    }
                }
                public void mouseExited(MouseEvent e) {
                    over = false;
                    repaint();
                }
            });
        }

        /**
         * Este método es el que dibuja los colores del botón en la pantalla.
         * Revisa si el botón está bloqueado (gris oscuro) o si el ratón está encima
         * (verde neón) para pintarlo de la forma correcta.
         *
         * @param g La herramienta gráfica de Java para dibujar formas y colores.
         */
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (!isEnabled()) {
                g2.setColor(control.COLOR_TARJETA);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(Color.GRAY);
            } else {
                if (over) {
                    g2.setColor(control.COLOR_NEON);
                } else {
                    g2.setColor(control.COLOR_TARJETA);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                g2.setFont(new Font("Arial", Font.BOLD, 14));
                if (over) {
                    g2.setColor(Color.BLACK);
                    g2.setStroke(new BasicStroke(2));
                    g2.setColor(Color.WHITE);
                    g2.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, 15, 15);
                    g2.setColor(Color.BLACK);
                } else {
                    g2.setColor(Color.WHITE);
                }
            }

            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(getText())) / 2;
            int y = (getHeight() + fm.getAscent()) / 2 - 2;
            g2.drawString(getText(), x, y);
            g2.dispose();
        }
    }
}