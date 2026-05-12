package DTOs;

import Enums.EstadoCodigoDesc;

public class CodigoDescuentoDTO {

    private String codigo;
    private Double monto;
    private EstadoCodigoDesc estado;

    public CodigoDescuentoDTO(String codigo) {
        this.codigo = codigo;
    }


    public CodigoDescuentoDTO(String codigo, Double monto, EstadoCodigoDesc estado) {
        this.codigo = codigo;
        this.monto = monto;
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public Double getMonto() {
        return monto;
    }

    public EstadoCodigoDesc getEstado() {
        return estado;
    }


}
