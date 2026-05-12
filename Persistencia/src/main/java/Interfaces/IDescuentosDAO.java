/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interfaces;

import DTOs.CodigoDescuentoDTO;
import goorderpersistencia.PersistenciaException;

/**
 *
 * @author juanl
 */
public interface IDescuentosDAO {
    
    public abstract CodigoDescuentoDTO BuscarDescuento(String codigo) throws PersistenciaException; 
    
    public CodigoDescuentoDTO cambiarEstado(String codigo) throws PersistenciaException; 
}
