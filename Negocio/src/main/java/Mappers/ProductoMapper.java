package Mappers;



public class ProductoMapper {
    public static GoOrderDTO.ProductoDTO toNegocio(Entitys.Producto p) {
        if (p == null) return null;
        return new GoOrderDTO.ProductoDTO(
                p.getNombre(),
                p.getDescripcion(),
                p.getPrecio(),
                p.getImagen()
        );
    }
}