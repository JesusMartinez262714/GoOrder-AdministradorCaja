package AdministradorCajaPresentacion.GUI;

import org.apache.commons.validator.GenericValidator;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class FilaMontoPanel extends JPanel {
    private JComboBox<String> cbMetodo;
    private JTextField txtMonto;
    private String metodoAnterior = "";
    private FilaMontoListener listener;


    private final Color COLOR_DIVISOR = new Color(85, 85, 85);
    private final Color COLOR_TEXTO = new Color(240, 240, 240);
    private final Color COLOR_ROJO_SUAVE = new Color(255, 107, 107);

    public FilaMontoPanel(FilaMontoListener listener) {
        this.listener = listener;
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridLayout(1, 2, 0, 0));
        setOpaque(false);
        setMaximumSize(new Dimension(500, 45));
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_DIVISOR));

        cbMetodo = new JComboBox<>(new String[]{"", "Efectivo", "Tarjeta", "App", "Referencia"});
        cbMetodo.setFont(new Font("Segoe UI", Font.BOLD, 14));

        cbMetodo.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                String nuevoMetodo = (String) e.getItem();

                if (!nuevoMetodo.equals("") && listener.verificarDuplicado(nuevoMetodo, this)) {
                    JOptionPane.showMessageDialog(this, "Este método ya está registrado.");

                    cbMetodo.setSelectedItem(metodoAnterior);
                } else {
                    metodoAnterior = nuevoMetodo;
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

    public double getMonto() {
        String texto = txtMonto.getText().trim();
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

    public String getMetodo() {
        return cbMetodo.getSelectedItem().toString();
    }

    public interface FilaMontoListener {
        void onCambio();
        boolean verificarDuplicado(String metodo, FilaMontoPanel origen);
    }
}