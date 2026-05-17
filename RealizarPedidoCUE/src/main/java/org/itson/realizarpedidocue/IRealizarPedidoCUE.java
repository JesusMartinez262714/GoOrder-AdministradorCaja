
package org.itson.realizarpedidocue;


import GoOrderDTO.CarritoDTO;
import GoOrderDTO.CodigoDescuentoDTO;
import GoOrderDTO.ProductoDTO;
import GoOrderDTO.ProductoSeleccionadoDTO;
import java.util.List;
import GoOrderDTO.SucursalDTO;
import org.example.NegocioException;

/**
 *
 * @author
 */
public interface IRealizarPedidoCUE {
    
    public abstract List<ProductoDTO> buscarProducto(String nombreProducto) throws NegocioException;
    
    public abstract List<ProductoDTO> listarProductos() throws NegocioException;

    List<SucursalDTO> consultarSucursales() throws NegocioException;
    
    public abstract CarritoDTO AgregarProductoCarrito(ProductoSeleccionadoDTO producto) throws NegocioException;
    
    public abstract CarritoDTO LimpiarCarrito() throws NegocioException;
    
    public abstract CarritoDTO ObtenerCarrito() throws NegocioException;
    
    public abstract CarritoDTO IncrementarCantidad(ProductoSeleccionadoDTO producto) throws NegocioException;
    
    public abstract CarritoDTO DescrementarCantidad(ProductoSeleccionadoDTO producto) throws NegocioException;
    
    public abstract CarritoDTO AplicarDescuento(String codigo)throws NegocioException;
    
    public abstract CarritoDTO EliminarProductoCarrito(ProductoSeleccionadoDTO producto)throws NegocioException;
    
    public CodigoDescuentoDTO cambiarEstadoDescuento(String codigo)throws NegocioException;
    
    public boolean finalizarCompra(String cuentaCliente, double totalAPagar) throws NegocioException;

    void registrarVentaBaseDatos(int idCajero, double montoTotal, int idMetodoPago) throws NegocioException;
}