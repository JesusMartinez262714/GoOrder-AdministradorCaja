package AdministradorCajaPersistencia.mocks;

import AdministradorCajaDTOs.cajeroDTO;
import AdministradorCajaDTOs.corteCajaDTO;
import AdministradorCajaDTOs.desgloseDTO; // Importante añadir este
import AdministradorCajaDTOs.ventaDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockDatabase {

    private static List<ventaDTO> ventas = new ArrayList<>();
    private static List<cajeroDTO> cajeros = new ArrayList<>();
    private static List<corteCajaDTO> historialCortes = new ArrayList<>();

    static {
        cajeros.add(new cajeroDTO(1, "Juan Leonel", "Matutino"));
        cajeros.add(new cajeroDTO(2, "Maria Garcia", "Vespertino"));

        ventas.add(new ventaDTO(1, 4250.00, 1, 1));
        ventas.add(new ventaDTO(2, 2100.00, 2, 1));
        ventas.add(new ventaDTO(3, 1550.00, 3, 1));
        ventas.add(new ventaDTO(4, 500.00, 1, 2));


        List<desgloseDTO> desgloseJuan = new ArrayList<>();
        desgloseJuan.add(new desgloseDTO(4250.00, 1, "Ventas Efectivo"));
        desgloseJuan.add(new desgloseDTO(2100.00, 2, "Ventas App"));
        desgloseJuan.add(new desgloseDTO(1650.00, 3, "Ventas Tarjeta"));

        historialCortes.add(new corteCajaDTO(
                1,
                new Date(),
                "Juan Leonel",
                8000.00,
                8000.00,
                "Todo cuadrado perfectamente",
                desgloseJuan
        ));

        List<desgloseDTO> desgloseMaria = new ArrayList<>();
        desgloseMaria.add(new desgloseDTO(4450.00, 1, "Ventas Efectivo"));

        historialCortes.add(new corteCajaDTO(
                2,
                new Date(),
                "Maria Garcia",
                4500.00,
                4450.00,
                "Faltaron 50 pesos en la caja",
                desgloseMaria // Inyectamos la lista dinámica
        ));
    }

    public static List<ventaDTO> getVentas() {
        return ventas;
    }

    public static List<cajeroDTO> getCajeros() {
        return cajeros;
    }


    public static List<corteCajaDTO> getHistorial() {
        return new ArrayList<>(historialCortes);
    }
}