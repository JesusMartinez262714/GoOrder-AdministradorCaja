/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package goorderpersistencia;

import Adapters.DescuentoEntityADTO;
import Entidades.CodigoDescuento;
import static Enumeradores.EstadoCodigoDesc.COBRADO;
import static Enumeradores.EstadoCodigoDesc.NOCOBRADO;
import DTOs.CodigoDescuentoDTO;
import Interfaces.IDescuentosDAO;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author juanl
 */
public class DescuentosDAO implements IDescuentosDAO{
    
    

    private List<CodigoDescuento> descuentos;
    
    public DescuentosDAO(){
        descuentos = new ArrayList<>();
        descuentos.add(new CodigoDescuento(1L,"D20",20.0,NOCOBRADO));
        descuentos.add(new CodigoDescuento(2L,"A10",10.0,NOCOBRADO));
        descuentos.add(new CodigoDescuento(3L,"S25",25.0,NOCOBRADO));
        
    }
    

    @Override
    public CodigoDescuentoDTO BuscarDescuento(String codigo) throws PersistenciaException {
        for (CodigoDescuento descuento : descuentos) {
                    if(codigo.equals(descuento.getCodigo()) && descuento.getEstado().equals(NOCOBRADO)){
                        return DescuentoEntityADTO.converitADTO(descuento);
                    }

                }
                throw new PersistenciaException("Codigo no encontrado.");
    }

    @Override
    public CodigoDescuentoDTO cambiarEstado(String codigo) throws PersistenciaException {
         for (CodigoDescuento descuento : descuentos) {
                    if(codigo.equals(descuento.getCodigo())){
                        if(descuento.getEstado().equals(NOCOBRADO)){
                        descuento.setEstado(COBRADO);
                        return DescuentoEntityADTO.converitADTO(descuento);
                        }
                        throw new PersistenciaException("Codigo ya cobrado");
                    }

                }
                throw new PersistenciaException("Codigo no encontrado.");
    }
    
}
