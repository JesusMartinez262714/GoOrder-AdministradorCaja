package Mappers;


import DTOs.SucursalDTO;

public class SucursalMapper {

    /**
     * Convierte de Persistencia a Negocio
     */
    public static GoOrderDTO.SucursalDTO toNegocio(SucursalDTO sP) {
        if (sP == null) return null;

        return new GoOrderDTO.SucursalDTO(
                sP.getSucursal_id(),
                sP.getNombre(),
                sP.getColonia(),
                sP.getCalle(),
                sP.getDescripcion(),
                sP.getNombreImagen()
        );
    }

}