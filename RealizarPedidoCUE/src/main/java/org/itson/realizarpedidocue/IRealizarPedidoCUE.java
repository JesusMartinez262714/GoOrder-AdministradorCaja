package org.itson.realizarpedidocue;

import GoOrderDTO.CarritoDTO;
import GoOrderDTO.CodigoDescuentoDTO;
import GoOrderDTO.ProductoDTO;
import GoOrderDTO.ProductoSeleccionadoDTO;
import GoOrderDTO.SucursalDTO;
import java.util.List;
import org.example.NegocioException;

public interface IRealizarPedidoCUE {
    List<ProductoDTO> buscarProducto(String nombreProducto) throws NegocioException;
    List<ProductoDTO> listarProductos() throws NegocioException;
    List<SucursalDTO> consultarSucursales() throws NegocioException;
    CarritoDTO AgregarProductoCarrito(ProductoSeleccionadoDTO producto) throws NegocioException;
    CarritoDTO LimpiarCarrito() throws NegocioException;
    CarritoDTO ObtenerCarrito() throws NegocioException;
    CarritoDTO IncrementarCantidad(ProductoSeleccionadoDTO producto) throws NegocioException;
    CarritoDTO DescrementarCantidad(ProductoSeleccionadoDTO producto) throws NegocioException;
    CarritoDTO AplicarDescuento(String codigo) throws NegocioException;
    CarritoDTO EliminarProductoCarrito(ProductoSeleccionadoDTO producto) throws NegocioException;
    CodigoDescuentoDTO cambiarEstadoDescuento(String codigo) throws NegocioException;
    boolean finalizarCompra(String cuentaCliente, double totalAPagar);
    void registrarVentaBaseDatos(double montoTotal, int idMetodoPago) throws NegocioException;
}