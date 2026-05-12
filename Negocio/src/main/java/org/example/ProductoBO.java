
package org.example;

import Entitys.Producto;
import Interfaces.IProductoBO;
import Interfaces.IProductoDAO;
import Mappers.ProductoMapper;
import goorderpersistencia.PersistenciaException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author maild
 */
public class ProductoBO implements IProductoBO {

    private IProductoDAO productoDAO;

    public ProductoBO(IProductoDAO productoDAO) {
        this.productoDAO = productoDAO;
    }

    @Override
    public List<GoOrderDTO.ProductoDTO> buscarProducto(String nombreProducto) throws NegocioException {
        try {
            List<Producto> listaEntity = productoDAO.buscarProducto(nombreProducto);

            List<GoOrderDTO.ProductoDTO> listaDTo = new ArrayList<>();

            for (Producto p : listaEntity) {
                listaDTo.add(ProductoMapper.toNegocio(p));
            }

            return listaDTo;
        } catch (PersistenciaException e) {
            throw new NegocioException("No fue posible realizar busqueda.");
        }
    }

    @Override
    public List<GoOrderDTO.ProductoDTO> listarProductos() throws NegocioException {
        try {
            List<Entitys.Producto> listaEntidades = productoDAO.listarProductos();

            List<GoOrderDTO.ProductoDTO> listaNegocio = new ArrayList<>();
            for (Entitys.Producto p : listaEntidades) {
                listaNegocio.add(ProductoMapper.toNegocio(p));
            }
            return listaNegocio;
        } catch (PersistenciaException e) {
            throw new NegocioException("No fue posible consultar productos.");
        }
    }
}