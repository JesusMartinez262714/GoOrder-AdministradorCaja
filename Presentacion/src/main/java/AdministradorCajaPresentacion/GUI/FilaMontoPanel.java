package AdministradorCajaPresentacion.GUI;

import org.apache.commons.validator.GenericValidator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * Clase que representa una fila individual en el panel de montos manuales.
 * Permite al usuario seleccionar un método de pago y escribir la cantidad física,
 * controlando que no se ingresen datos inválidos.
 * * @author Jesus Manuel Martinez Cortez
 * @version 1.0
 */
public class FilaMontoPanel extends JPanel {
    private JComboBox<String> cbMetodo;
    private JTextField txtMonto;
    private String metodoAnterior = "";
    private FilaMontoListener listener;

    private final Color COLOR_DIVISOR = new Color(85, 85, 85);
    private final Color COLOR_TEXTO = new Color(240, 240, 240);
    private final Color COLOR_ROJO_SUAVE = new Color(255, 107, 107);

    /**
     * Constructor que recibe el listener para reportar los cambios de la fila
     * hacia el formulario principal.
     * * @param listener Instancia que escucha las modificaciones de la fila.
     */
    public FilaMontoPanel(FilaMontoListener listener) {
        this.listener = listener;
        initComponents();
    }

    /**
     * Inicializa y estructura los componentes gráficos de la fila, como el
     * JComboBox para los métodos y el JTextField para el monto.
     */
    private void initComponents() {
        setLayout(new GridLayout(1, 2, 0, 0));
        setOpaque(false);
        setMaximumSize(new Dimension(500, 45));
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_DIVISOR));

        cbMetodo = new JComboBox<>(new String[]{"", "Efectivo", "Tarjeta", "App", "Referencia"});
        cbMetodo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cbMetodo.setBackground(Color.WHITE);

        cbMetodo.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                String nuevoMetodo = (String) e.getItem();

                if (nuevoMetodo != null && !nuevoMetodo.equals("") && listener.verificarDuplicado(nuevoMetodo, this)) {
                    JOptionPane.showMessageDialog(this,
                            "Este método de pago ya está registrado en otra fila.",
                            "Método Duplicado", JOptionPane.WARNING_MESSAGE);

                    cbMetodo.setSelectedItem(metodoAnterior);
                } else {
                    metodoAnterior = nuevoMetodo != null ? nuevoMetodo : "";
                    listener.onCambio();
                }
            }
        });

        txtMonto = new JTextField("0");
        txtMonto.setHorizontalAlignment(JTextField.CENTER);
        txtMonto.setFont(new Font("Segoe UI", Font.BOLD, 16));
        txtMonto.setForeground(COLOR_TEXTO);
        txtMonto.setOpaque(false);
        txtMonto.setBorder(null);

        txtMonto.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(() -> txtMonto.selectAll());
            }

            @Override
            public void focusLost(FocusEvent e) {
                String contenido = txtMonto.getText().trim();
                if (contenido.isEmpty() || contenido.equals(".")) {
                    txtMonto.setText("0");
                }
                listener.onCambio();
            }
        });

        ((AbstractDocument) txtMonto.getDocument()).setDocumentFilter(new FiltroMonedaFila());

        txtMonto.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { listener.onCambio(); }
            public void removeUpdate(DocumentEvent e) { listener.onCambio(); }
            public void changedUpdate(DocumentEvent e) { listener.onCambio(); }
        });

        JPanel pnlLeft = new JPanel(new BorderLayout());
        pnlLeft.setOpaque(false);
        pnlLeft.setBorder(new EmptyBorder(8, 15, 8, 15));
        pnlLeft.add(cbMetodo);

        add(pnlLeft);
        add(txtMonto);
    }

    /**
     * Asigna los valores guardados a la fila cuando se abre la vista
     * en el modo de edición de un corte existente.
     * * @param metodo Nombre del método de pago a cargar.
     * @param monto Cantidad de dinero registrada originalmente.
     */
    public void setDatos(String metodo, String monto) {
        this.metodoAnterior = metodo != null ? metodo : "";
        this.cbMetodo.setSelectedItem(metodo);
        this.txtMonto.setText(monto);
    }

    /**
     * Valida y devuelve el valor numérico ingresado en el campo de texto.
     * Si está vacío o incompleto durante la edición, devuelve cero.
     * * @return El monto en formato double, o -1 si el formato es incorrecto.
     */
    public double getMonto() {
        String texto = txtMonto.getText().trim();
        if (texto.isEmpty() || texto.equals(".")) {
            txtMonto.setForeground(COLOR_TEXTO);
            return 0.0;
        }
        if (GenericValidator.isDouble(texto)) {
            double v = Double.parseDouble(texto);
            if (v >= 0) {
                txtMonto.setForeground(COLOR_TEXTO);
                return v;
            }
        }
        txtMonto.setForeground(COLOR_ROJO_SUAVE);
        return -1;
    }

    /**
     * Devuelve el método de pago que el usuario seleccionó en el ComboBox.
     * * @return Cadena de texto con el método de pago seleccionado.
     */
    public String getMetodo() {
        Object item = cbMetodo.getSelectedItem();
        return item != null ? item.toString() : "";
    }

    /**
     * Interfaz para comunicar los eventos y cambios ocurridos en la fila
     * hacia el formulario principal que la contiene.
     */
    public interface FilaMontoListener {

        /**
         * Método que se ejecuta cuando el monto o el tipo de pago cambian en la fila.
         */
        void onCambio();

        /**
         * Verifica si el método de pago seleccionado ya se encuentra en uso por otra fila.
         * * @param metodo El nombre del método a validar.
         * @param origen La fila que solicita la validación para no compararse consigo misma.
         * @return true si el método está duplicado, false en caso contrario.
         */
        boolean verificarDuplicado(String metodo, FilaMontoPanel origen);
    }

    /**
     * Filtro personalizado para el campo de texto que restringe la entrada
     * para que el usuario solo pueda escribir números, un punto y hasta dos decimales.
     */
    private class FiltroMonedaFila extends DocumentFilter {
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
}