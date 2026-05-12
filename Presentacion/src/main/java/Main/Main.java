
package Main;

import Control.Control;
import Interfaces.ICarritoBO;
import Interfaces.ICarritoDAO;
import Interfaces.IDescuentosBO;
import Interfaces.IDescuentosDAO;
import Interfaces.IProductoBO;
import Interfaces.IProductoDAO;
import Interfaces.IServicioBanco;
import goorderpersistencia.CarritoDAO;
import goorderpersistencia.DescuentosDAO;
import goorderpersistencia.ProductoDAO;
import org.example.CarritoBO;
import org.example.ConectorBanco;
import org.example.DescuentosBO;
import org.example.ProductoBO;
import org.itson.realizarpedidocue.IRealizarPedidoCUE;
import org.itson.realizarpedidocue.RealizarPedidoCUE;

/**
 *
 *
 */

public class Main {
    public static void main(String[] args) {
        IProductoDAO productoDAO = new ProductoDAO();
        IProductoBO productoBO = new ProductoBO(productoDAO);
        IDescuentosDAO descuentosDAO = new DescuentosDAO();
        IDescuentosBO descuentosBO = new DescuentosBO(descuentosDAO);
        ICarritoDAO carritoDAO = new CarritoDAO();
        ICarritoBO carritoBO = new CarritoBO(carritoDAO,descuentosBO);
        IServicioBanco bancoService = new ConectorBanco();
        
        IRealizarPedidoCUE realizarPedido = new RealizarPedidoCUE(productoBO,carritoBO,bancoService);
        Control control = new Control(realizarPedido);
        control.mostrarInicio();
    }    
}