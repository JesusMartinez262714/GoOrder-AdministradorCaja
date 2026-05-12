
package Interfaces;

import DTOs.CarritoDTO;
import goorderpersistencia.PersistenciaException;

/**
 *
 * @author juanl
 */
public interface ICarritoDAO {
    
    public abstract CarritoDTO AgregarCarrito(CarritoDTO carrito) throws PersistenciaException;
    
    public abstract CarritoDTO ActualizarCarrito(CarritoDTO carrito) throws PersistenciaException;
    
    public abstract CarritoDTO LimpiarCarrito() throws PersistenciaException;
    
    public abstract CarritoDTO ObtenerCarrito() throws PersistenciaException; 
}