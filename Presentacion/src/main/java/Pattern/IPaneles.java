
package Pattern;

import javax.swing.JPanel;

/**
 *
 * @author
 */
public interface IPaneles {

    //FACTORY

    /**
     * Creacion del objeto que se va a crear, este caso un panel
     * Este panel se implementara de un modo especial
     * DECIMOS QUE QUEREMOS CREAR UN BLOQUE
     */
    public abstract JPanel getPanel();
}