package org.itson.realizarpedidocue;

import Entitys.venta;
import GoOrderDTO.CarritoDTO;
import GoOrderDTO.CodigoDescuentoDTO;
import GoOrderDTO.ProductoDTO;
import GoOrderDTO.ProductoSeleccionadoDTO;
import GoOrderDTO.SucursalDTO;
import Interfaces.*;
import Mappers.SucursalMapper;
import goorderpersistencia.PersistenciaException;
import goorderpersistencia.SucursalesDAO;
import java.util.List;
import java.util.Date;
import org.example.NegocioException;

public class RealizarPedidoCUE implements IRealizarPedidoCUE {

    private IProductoBO productoBO;
    private ISucursalesDAO sucursalesDAO;
    private ICarritoBO carritoBO;
    private IDescuentosBO descuentosBO;
    private IServicioBanco bancoService;
    private IVentaDAO ventaDAO;

    public RealizarPedidoCUE(IProductoBO productoBO, ICarritoBO carritoBO, IServicioBanco bancoService, IVentaDAO ventaDAO) {
        this.productoBO = productoBO;
        this.sucursalesDAO = new SucursalesDAO();
        this.carritoBO = carritoBO;
        this.bancoService = bancoService;
        this.ventaDAO = ventaDAO;
    }

    @Override
    public List<ProductoDTO> buscarProducto(String nombreProducto) throws NegocioException {
        if (nombreProducto.isEmpty()) {
            throw new NegocioException("El nombre no puede quedar vacio al realizar busqueda.");
        }

        if (nombreProducto.matches("\\d+")) {
            throw new NegocioException("El nombre del producto no puede contener números.");
        }
        try {
            return productoBO.buscarProducto(nombreProducto);
        } catch (NegocioException e) {
            throw new NegocioException("No fue posible realizar busqueda.");
        }
    }

    @Override
    public List<ProductoDTO> listarProductos() throws NegocioException {
        try {
            List<ProductoDTO> lista = productoBO.listarProductos();

            if (lista.isEmpty()) {
                throw new NegocioException("No existen productos.");
            }
            return lista;

        } catch (NegocioException e) {
            throw new NegocioException("No fue posible realizar listado de productos.");
        }
    }

    @Override
    public List<SucursalDTO> consultarSucursales() throws NegocioException {
        try {
            List<DTOs.SucursalDTO> listaPersistencia = sucursalesDAO.consultarSucursales();

            if (listaPersistencia.isEmpty()) {
                throw new NegocioException("No hay sucursales disponibles en este momento");
            }

            List<SucursalDTO> listaNegocio = new java.util.ArrayList<>();

            for (DTOs.SucursalDTO sP : listaPersistencia) {
                listaNegocio.add(SucursalMapper.toNegocio(sP));
            }

            return listaNegocio;

        } catch (PersistenciaException e) {
            throw new NegocioException("No se pudo establecer conexion con la base de datos");
        }
    }

    @Override
    public CarritoDTO AgregarProductoCarrito(ProductoSeleccionadoDTO producto) throws NegocioException {
        if(producto == null) {
            throw new NegocioException("El carrito no puede ser nulo");
        }
        return carritoBO.AgregarProductoCarrito(producto);
    }

    @Override
    public CarritoDTO LimpiarCarrito() throws NegocioException {
        return carritoBO.LimpiarCarrito();
    }

    @Override
    public CarritoDTO ObtenerCarrito() throws NegocioException {
        return carritoBO.ObtenerCarrito();
    }

    @Override
    public CarritoDTO IncrementarCantidad(ProductoSeleccionadoDTO producto) throws NegocioException {
        if(producto == null){
            throw new NegocioException("El carrito no puede ser nulo");
        }
        return carritoBO.IncrementarCantidad(producto);
    }

    @Override
    public CarritoDTO DescrementarCantidad(ProductoSeleccionadoDTO producto) throws NegocioException {
        if(producto == null){
            throw new NegocioException("El carrito no puede ser nulo");
        }
        return carritoBO.DescrementarCantidad(producto);
    }

    @Override
    public CarritoDTO AplicarDescuento(String codigo) throws NegocioException {
        return carritoBO.AplicarDescuento(codigo);
    }

    @Override
    public CarritoDTO EliminarProductoCarrito(ProductoSeleccionadoDTO producto) throws NegocioException {
        return carritoBO.EliminarProductoCarrito(producto);
    }

    @Override
    public CodigoDescuentoDTO cambiarEstadoDescuento(String codigo) throws NegocioException {
        return descuentosBO.CambiarEstadoDescuento(codigo);
    }

    @Override
    public boolean finalizarCompra(String cuentaCliente, double totalAPagar) {
        return bancoService.procesarPago(cuentaCliente, totalAPagar);
    }

    @Override
    public void registrarVentaBaseDatos(int idCajero, double montoTotal, int idMetodoPago) throws NegocioException {
        try {
            venta nuevaVenta = new venta();
            nuevaVenta.setIdVenta((int) (System.currentTimeMillis() / 1000));
            nuevaVenta.setIdCajero(idCajero);
            nuevaVenta.setMontoTotal(montoTotal);
            nuevaVenta.setIdMetodoPago(idMetodoPago);
            nuevaVenta.setFecha(new Date());

            ventaDAO.insertarVenta(nuevaVenta);
        } catch (Exception e) {
            throw new NegocioException("Error al registrar la venta en la base de datos.");
        }
    }
}