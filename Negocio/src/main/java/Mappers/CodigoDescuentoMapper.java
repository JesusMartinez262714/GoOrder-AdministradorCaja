package Mappers;


import DTOs.CodigoDescuentoDTO;

public class CodigoDescuentoMapper {

    /**
     * Convierte de Persistencia a Negocio
     */
    public static GoOrderDTO.CodigoDescuentoDTO toNegocio(CodigoDescuentoDTO dP) {
        if (dP == null) return null;

        return new GoOrderDTO.CodigoDescuentoDTO(
                dP.getCodigo(),
                dP.getMonto(),
                dP.getEstado()
        );
    }

    /**
     * Convierte de Negocio a Persistencia
     */
    public static CodigoDescuentoDTO toPersistencia(GoOrderDTO.CodigoDescuentoDTO dN) {
        if (dN == null) return null;

        return new CodigoDescuentoDTO(
                dN.getCodigo(),
                dN.getMonto(),
                dN.getEstado()
        );
    }
}