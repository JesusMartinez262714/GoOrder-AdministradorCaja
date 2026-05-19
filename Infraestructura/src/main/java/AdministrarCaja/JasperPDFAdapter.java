package AdministrarCaja;

import AdministradorCajaDTOs.corteCajaDTO;
import AdministradorCajaDTOs.desgloseDTO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class JasperPDFAdapter {

    public void generarTicketCorte(corteCajaDTO corte, String rutaDestino) throws Exception {

        Rectangle pageSize = new Rectangle(300, 700);
        Document document = new Document(pageSize, 20, 20, 20, 20);
        PdfWriter.getInstance(document, new FileOutputStream(rutaDestino));

        document.open();

        BaseColor COLOR_ACCENTO = new BaseColor(46, 204, 113);
        BaseColor COLOR_GRIS = new BaseColor(153, 153, 153);
        BaseColor COLOR_GRIS_OSCURO = new BaseColor(85, 85, 85);
        BaseColor COLOR_ROJO_BORDE = new BaseColor(211, 47, 47);
        BaseColor COLOR_ROJO_BG = new BaseColor(255, 244, 244);

        Font fontTitulo = FontFactory.getFont(FontFactory.COURIER_OBLIQUE, 24, Font.BOLD, COLOR_ACCENTO);
        Font fontSubtitulo = FontFactory.getFont(FontFactory.COURIER_OBLIQUE, 10, Font.NORMAL, COLOR_GRIS);
        Font fontNormal = FontFactory.getFont(FontFactory.COURIER_OBLIQUE, 10, Font.NORMAL, COLOR_GRIS_OSCURO);
        Font fontGreenBold = FontFactory.getFont(FontFactory.COURIER_OBLIQUE, 10, Font.BOLD, COLOR_ACCENTO);

        SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "MX"));
        SimpleDateFormat sdfHora = new SimpleDateFormat("hh:mm a", new Locale("es", "MX"));

        String fechaStr = (corte.getFecha() != null) ? sdfFecha.format(corte.getFecha()) : "S/F";
        String horaCierreStr = (corte.getFecha() != null) ? sdfHora.format(corte.getFecha()) : "--:--";
        String horaAperturaStr = (corte.getFechaApertura() != null) ? sdfHora.format(corte.getFechaApertura()) : "--:--";

        Paragraph titulo = new Paragraph("GoOrder", fontTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);

        Paragraph subtitulo = new Paragraph("Sucursal CENTRO - ID: #001\nFecha: " + fechaStr + " | Cierre: " + horaCierreStr, fontSubtitulo);
        subtitulo.setAlignment(Element.ALIGN_CENTER);
        document.add(subtitulo);

        boolean isVigente = corte.getEstado().equalsIgnoreCase("CERRADA") || corte.getEstado().equalsIgnoreCase("Vigente");
        BaseColor colorEstadoActual = isVigente ? COLOR_ACCENTO : COLOR_GRIS_OSCURO;
        Font fontEstadoDinamico = FontFactory.getFont(FontFactory.COURIER_OBLIQUE, 10, Font.BOLD, colorEstadoActual);

        Paragraph estado = new Paragraph("ESTADO: " + corte.getEstado().toUpperCase(), fontEstadoDinamico);
        estado.setAlignment(Element.ALIGN_CENTER);
        document.add(estado);

        document.add(new Paragraph("\n"));
        document.add(new com.itextpdf.text.pdf.draw.LineSeparator(0.5f, 100, COLOR_GRIS, Element.ALIGN_CENTER, -1));
        document.add(new Paragraph("\n"));

        document.add(new Paragraph("FOLIO DE CORTE: CC-" + corte.getIdCaja(), fontNormal));
        document.add(new Paragraph("CAJERO: " + (corte.getCajero() != null ? corte.getCajero() : "Sin nombre"), fontNormal));
        document.add(new Paragraph("TURNO: (" + horaAperturaStr + " - " + horaCierreStr + ")", fontNormal));
        document.add(new Paragraph("\n"));

        if (corte.getEstado().equalsIgnoreCase("Cancelado")) {
            String mot = (corte.getMotivoCancelacion() != null && !corte.getMotivoCancelacion().isEmpty())
                    ? corte.getMotivoCancelacion() : "No especificado por el supervisor.";

            PdfPTable tableCancel = new PdfPTable(1);
            tableCancel.setWidthPercentage(100);

            Font fontTituloCancel = FontFactory.getFont(FontFactory.COURIER_OBLIQUE, 10, Font.BOLD, COLOR_ROJO_BORDE);
            Font fontTextoCancel = FontFactory.getFont(FontFactory.COURIER_OBLIQUE, 9, Font.NORMAL, BaseColor.BLACK);

            PdfPCell cellCancel = new PdfPCell();
            cellCancel.setPadding(8);
            cellCancel.setBorderColor(COLOR_ROJO_BORDE);
            cellCancel.setBackgroundColor(COLOR_ROJO_BG);

            cellCancel.addElement(new Paragraph(" CORTE CANCELADO", fontTituloCancel));
            cellCancel.addElement(new Paragraph("Motivo de Baja: " + mot, fontTextoCancel));

            tableCancel.addCell(cellCancel);
            document.add(tableCancel);
            document.add(new Paragraph("\n"));
        }

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2f, 1f});

        PdfPCell cellConcepto = new PdfPCell(new Phrase("Concepto", fontGreenBold));
        cellConcepto.setBorder(Rectangle.BOTTOM);
        cellConcepto.setBorderColor(COLOR_GRIS_OSCURO);
        table.addCell(cellConcepto);

        PdfPCell cellMonto = new PdfPCell(new Phrase("Monto", fontGreenBold));
        cellMonto.setBorder(Rectangle.BOTTOM);
        cellMonto.setBorderColor(COLOR_GRIS_OSCURO);
        cellMonto.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cellMonto);

        if (corte.getListaDesglose() != null && !corte.getListaDesglose().isEmpty()) {
            for (desgloseDTO d : corte.getListaDesglose()) {
                if (d.getMontoDeclarado() > 0) {
                    PdfPCell c1 = new PdfPCell(new Phrase(d.getNombreMetodo(), fontNormal));
                    c1.setBorder(Rectangle.NO_BORDER);
                    table.addCell(c1);

                    PdfPCell c2 = new PdfPCell(new Phrase("$" + String.format("%,.2f", d.getMontoDeclarado()), fontNormal));
                    c2.setBorder(Rectangle.NO_BORDER);
                    c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c2);
                }
            }
        } else {
            PdfPCell c1 = new PdfPCell(new Phrase("Sin Desgloses", fontNormal));
            c1.setBorder(Rectangle.NO_BORDER);
            table.addCell(c1);

            PdfPCell c2 = new PdfPCell(new Phrase("$" + String.format("%,.2f", corte.getMontoReal()), fontNormal));
            c2.setBorder(Rectangle.NO_BORDER);
            c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(c2);
        }

        PdfPCell cellLinea = new PdfPCell(new Phrase(""));
        cellLinea.setBorder(Rectangle.TOP);
        cellLinea.setColspan(2);
        table.addCell(cellLinea);

        PdfPCell cTotalTxt = new PdfPCell(new Phrase("VENTA BRUTA TOTAL", fontNormal));
        cTotalTxt.setBorder(Rectangle.NO_BORDER);
        table.addCell(cTotalTxt);

        PdfPCell cTotalMonto = new PdfPCell(new Phrase("$" + String.format("%,.2f", corte.getMontoReal()), fontNormal));
        cTotalMonto.setBorder(Rectangle.NO_BORDER);
        cTotalMonto.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cTotalMonto);

        document.add(table);
        document.add(new Paragraph("\n"));

        // --- TABLA DE ARQUEO FINANCIERO ---
        PdfPTable tableArqueo = new PdfPTable(2);
        tableArqueo.setWidthPercentage(100);

        PdfPCell headerArqueo = new PdfPCell(new Phrase("RESUMEN DE ARQUEO:\n\n", fontNormal));
        headerArqueo.setColspan(2);
        headerArqueo.setBorder(Rectangle.TOP | Rectangle.LEFT | Rectangle.RIGHT);
        headerArqueo.setBorderColor(COLOR_GRIS);
        headerArqueo.setPadding(10);
        tableArqueo.addCell(headerArqueo);

        PdfPCell cEspTxt = new PdfPCell(new Phrase("Esperado:", fontNormal));
        cEspTxt.setBorder(Rectangle.LEFT);
        cEspTxt.setBorderColor(COLOR_GRIS);
        cEspTxt.setPaddingLeft(10);
        tableArqueo.addCell(cEspTxt);

        PdfPCell cEspMonto = new PdfPCell(new Phrase("$" + String.format("%,.2f", corte.getMontoEsperado()), fontNormal));
        cEspMonto.setBorder(Rectangle.RIGHT);
        cEspMonto.setBorderColor(COLOR_GRIS);
        cEspMonto.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cEspMonto.setPaddingRight(10);
        tableArqueo.addCell(cEspMonto);

        PdfPCell cRealTxt = new PdfPCell(new Phrase("Real:", fontNormal));
        cRealTxt.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
        cRealTxt.setBorderColor(COLOR_GRIS);
        cRealTxt.setPaddingLeft(10);
        cRealTxt.setPaddingBottom(10);
        tableArqueo.addCell(cRealTxt);

        PdfPCell cRealMonto = new PdfPCell(new Phrase("$" + String.format("%,.2f", corte.getMontoReal()), fontNormal));
        cRealMonto.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM);
        cRealMonto.setBorderColor(COLOR_GRIS);
        cRealMonto.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cRealMonto.setPaddingRight(10);
        cRealMonto.setPaddingBottom(10);
        tableArqueo.addCell(cRealMonto);

        double dif = corte.getDiferencia();
        Font fontDif = FontFactory.getFont(FontFactory.COURIER_OBLIQUE, 10, Font.BOLD, (dif == 0) ? COLOR_ACCENTO : BaseColor.RED);

        PdfPCell cDifTxt = new PdfPCell(new Phrase("Diferencia:", fontDif));
        cDifTxt.setBorder(Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.RIGHT);
        cDifTxt.setBorderColor(COLOR_GRIS);
        cDifTxt.setPadding(10);
        tableArqueo.addCell(cDifTxt);

        PdfPCell cDifMonto = new PdfPCell(new Phrase("$" + String.format("%,.2f", dif), fontDif));
        cDifMonto.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT);
        cDifMonto.setBorderColor(COLOR_GRIS);
        cDifMonto.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cDifMonto.setPadding(10);
        tableArqueo.addCell(cDifMonto);

        document.add(tableArqueo);
        document.add(new Paragraph("\n"));

        String motivoStr = (corte.getObservaciones() != null && !corte.getObservaciones().isEmpty())
                ? corte.getObservaciones() : "Sin observaciones registradas.";

        PdfPTable tableMotivo = new PdfPTable(1);
        tableMotivo.setWidthPercentage(100);
        PdfPCell cMotivo = new PdfPCell(new Phrase("Motivo / Observaciones del Corte:\n" + motivoStr, fontNormal));
        cMotivo.setPadding(10);
        cMotivo.setBorderColor(COLOR_GRIS);
        tableMotivo.addCell(cMotivo);
        document.add(tableMotivo);

        document.close();
    }
}