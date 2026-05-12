
package goorderpersistencia;

import DTOs.CarritoDTO;
import Interfaces.ICarritoDAO;

/**
 *
 * @author juanl
 */
public class CarritoDAO implements ICarritoDAO{

    private CarritoDTO carritoGuardado;
    
    public CarritoDAO() {
        this.carritoGuardado = new CarritoDTO();
    }

    @Override
    public CarritoDTO AgregarCarrito(CarritoDTO carrito) throws PersistenciaException {
        return this.carritoGuardado = carrito;
        
    }

    @Override
    public CarritoDTO ActualizarCarrito(CarritoDTO carrito) throws PersistenciaException {
         return this.carritoGuardado = carrito;
    }

    @Override
    public CarritoDTO LimpiarCarrito() throws PersistenciaException {
        return this.carritoGuardado = new CarritoDTO();
    }

    @Override
    public CarritoDTO ObtenerCarrito() throws PersistenciaException {
        return carritoGuardado;
    }  
}