package Mappers;



public class CodigoDescuentoMapper {

    /**
     * Convierte de Persistencia a Negocio
     */
    public static GoOrderDTO.CodigoDescuentoDTO toNegocio(DTOs.CodigoDescuentoDTO dP) {
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
    public static DTOs.CodigoDescuentoDTO toPersistencia(GoOrderDTO.CodigoDescuentoDTO dN) {
        if (dN == null) return null;

        return new DTOs.CodigoDescuentoDTO(
                dN.getCodigo(),
                dN.getMonto(),
                dN.getEstado()
        );
    }
}