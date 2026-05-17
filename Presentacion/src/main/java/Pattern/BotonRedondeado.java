
package Pattern;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;

/**
 *
 * @author
 */
public class BotonRedondeado  extends JButton {

    private int radioDeEsquina = 20; // Puedes cambiar este número para hacer la curva más o menos pronunciada

    public BotonRedondeado() {
        super();
        // Es vital quitar el pintado por defecto de Swing para dibujar el nuestro
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // Activar el suavizado (Antialiasing) para que los bordes redondos se vean limpios
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar el fondo redondeado con el color actual del botón
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radioDeEsquina, radioDeEsquina);

        g2.dispose();

        // Llamar al super para que dibuje el texto del botón por encima del fondo
        super.paintComponent(g);
    }
}