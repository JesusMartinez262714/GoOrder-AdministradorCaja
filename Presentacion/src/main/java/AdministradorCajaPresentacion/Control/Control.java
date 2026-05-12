package AdministradorCajaPresentacion.Control;

import AdministradorCajaDTOs.resumenVentasDTO;
import AdministradorCajaDTOs.cajeroDTO;
import AdministradorCajaPresentacion.GUI.ResumenTurno;
import Interfaces.INegocioCorte;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Control {

    private INegocioCorte fachadaNegocio;

    private ResumenTurno pantallaResumen;


    public Control(INegocioCorte fachadaNegocio) {
        this.fachadaNegocio = fachadaNegocio;
    }


    public void iniciarFlujoResumen() {
        if (pantallaResumen == null) {
            pantallaResumen = new ResumenTurno();
        }

        try {
            List<cajeroDTO> cajerosActivos = new ArrayList<>();
            cajerosActivos.add(new cajeroDTO(1, "Juan Leonel", "Matutino"));
            cajerosActivos.add(new cajeroDTO(2, "Maria Garcia", "Vespertino"));

            cajeroDTO primerCajero = cajerosActivos.get(0);
            resumenVentasDTO resumenInicial = fachadaNegocio.generarResumenVentasTurno(primerCajero.getIdCajero(), new Date());

            String nombreSupervisor = "Jesus Martinez";

            pantallaResumen.cargarDatos(resumenInicial, cajerosActivos, nombreSupervisor);

            pantallaResumen.setCajeroChangeListener(e -> {
                if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {

                    cajeroDTO seleccionado = (cajeroDTO) e.getItem();

                    resumenVentasDTO nuevoResumen = fachadaNegocio.generarResumenVentasTurno(seleccionado.getIdCajero(), new Date());

                    pantallaResumen.actualizarMontos(nuevoResumen, seleccionado);
                }
            });

            pantallaResumen.setVisible(true);

        } catch (Exception e) {
            System.err.println("Error al cargar la pantalla de resumen: " + e.getMessage());
        }
    }
}