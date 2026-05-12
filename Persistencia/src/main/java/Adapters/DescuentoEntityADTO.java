
package Adapters;

import Entidades.CodigoDescuento;
import DTOs.CodigoDescuentoDTO;
import Enums.EstadoCodigoDesc;

/**
 *
 * @author juanl
 */
public class DescuentoEntityADTO {

    public static CodigoDescuentoDTO converitADTO(CodigoDescuento codigo){
        if(codigo == null){
            return null;
        }
        EstadoCodigoDesc estado = EstadoCodigoDesc.valueOf(codigo.getEstado().name());
        CodigoDescuentoDTO codigoDto = new CodigoDescuentoDTO(
                codigo.getCodigo(),
                codigo.getMonto(),
                estado
        );

        return codigoDto;
    }
}