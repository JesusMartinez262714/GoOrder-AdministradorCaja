package AdministradorCajaPersistencia.mocks;

import AdministradorCajaDTOs.cajeroDTO;
import AdministradorCajaDTOs.ventaDTO;

import java.util.ArrayList;
import java.util.List;

public class MockDatabase {

    private static List<ventaDTO> ventas = new ArrayList<>();
    private static List<cajeroDTO> cajeros = new ArrayList<>();

    static {
        cajeros.add(new cajeroDTO(1, "Juan Leonel", "Matutino"));
        cajeros.add(new cajeroDTO(2, "Maria Garcia", "Vespertino"));

        ventas.add(new ventaDTO(1, 4250.00, 1, 1));
        ventas.add(new ventaDTO(2, 2100.00, 2, 1));
        ventas.add(new ventaDTO(3, 1550.00, 3, 1));
        ventas.add(new ventaDTO(4, 500.00, 1, 2));
    }

    public static List<ventaDTO> getVentas() {
        return ventas;
    }

    public static List<cajeroDTO> getCajeros() {
        return cajeros;
    }
}