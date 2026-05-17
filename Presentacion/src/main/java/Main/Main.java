package Main;

import Control.Control;
import Interfaces.*;
import goorderpersistencia.CarritoDAO;
import goorderpersistencia.DescuentosDAO;
import goorderpersistencia.ProductoDAO;
import goorderpersistencia.ventaDAO;
import org.example.CarritoBO;
import org.example.ConectorBanco;
import org.example.DescuentosBO;
import org.example.ProductoBO;
import org.itson.realizarpedidocue.IRealizarPedidoCUE;
import org.itson.realizarpedidocue.RealizarPedidoCUE;

import com.mongodb.client.MongoDatabase;
//s
public class Main {
    public static void main(String[] args) {

        MongoDatabase baseDatos = conexionMONGODB.getBaseDatos();

        IVentaDAO miVentaDAO = new ventaDAO(baseDatos);

        IProductoDAO productoDAO = new ProductoDAO();
        IProductoBO productoBO = new ProductoBO(productoDAO);
        IDescuentosDAO descuentosDAO = new DescuentosDAO();
        IDescuentosBO descuentosBO = new DescuentosBO(descuentosDAO);
        ICarritoDAO carritoDAO = new CarritoDAO();
        ICarritoBO carritoBO = new CarritoBO(carritoDAO, descuentosBO);
        IServicioBanco bancoService = new ConectorBanco();

        IRealizarPedidoCUE realizarPedido = new RealizarPedidoCUE(productoBO, carritoBO, bancoService, miVentaDAO);

        Control control = new Control(realizarPedido);
        control.mostrarInicio();
    }
}