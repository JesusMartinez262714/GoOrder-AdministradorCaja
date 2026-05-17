
package Pattern;

import Control.Control;
import GoOrderDTO.ProductoDTO;
import GoOrderDTO.ProductoSeleccionadoDTO;


/**
 *
 * @author
 */
public class FactoriaPaneles {

    /**
     * Y AQUÍ TENEMOS A LA FACTORY DE BLOQUES LOS CUAL
     * NOS DICE EN LA INTERFAZ QUE SERA UN BLOQUE(PANEL)
     * LUEGO EL NOMBRE DEL METODO (CREAR)
     * POR ULTIMO VAMOS A REGRESAR UN NUEVO BLOQUE CON LAS ESPECIFICACIONES(IMPLEMENTACIONPANEL)
     * ES DECIR, CONTRATO DICE QUE SE HACE
     * IMPLEMENTACION DICE COMO SE HACE
     * AL FINAL CREAMOS ESOS BLOQUES EN SERIE EN BASE A LAS ESPECIFICACIONES
     * @param control
     * @param productoDescripcion
     * @return
     */
    public static IPaneles crearPanelProducto(Control control, ProductoDTO productoDescripcion) {
        return new ImplementacionPanel(control, productoDescripcion);
    }
}