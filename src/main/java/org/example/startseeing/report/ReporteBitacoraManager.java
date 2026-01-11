package org.example.startseeing.report;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class ReporteBitacoraManager {

    private static final String RUTA_REPORTE = "/reportes/bitacora.jasper";
    private static final String RUTA_REPORTE_EXCEL = "/reportes/bitacoraExcel.jasper";
    private static final String RUTA_LOGO = "/imagen/logo.png";

    //pdf
    public static File exportarPDF(
            Connection connection,
            String usuario,
            Date fechaInicio,
            Date fechaFin,
            File carpetaDestino
    ) throws Exception {

        InputStream reporte =
                ReporteBitacoraManager.class.getResourceAsStream(RUTA_REPORTE);

        InputStream logo =
                ReporteBitacoraManager.class.getResourceAsStream(RUTA_LOGO);

        Map<String, Object> params = new HashMap<>();
        params.put("usuario", usuario);
        params.put("fecha_inicio", fechaInicio);
        params.put("fecha_fin", fechaFin);
        params.put("logo", logo);

        JasperPrint print = JasperFillManager.fillReport(
                reporte, params, connection
        );

        String nombreArchivo = generarNombreArchivo("Bitacora", "pdf");
        File destino = new File(carpetaDestino, nombreArchivo);

        JasperExportManager.exportReportToPdfFile(
                print,
                destino.getAbsolutePath()
        );

        return destino;
    }

    //excel
    public static File exportarExcel(
            Connection connection,
            String usuario,
            Date fechaInicio,
            Date fechaFin,
            File carpetaDestino
    ) throws Exception {

        InputStream reporte =
                ReporteBitacoraManager.class.getResourceAsStream(RUTA_REPORTE_EXCEL);

        InputStream logo =
                ReporteBitacoraManager.class.getResourceAsStream(RUTA_LOGO);

        Map<String, Object> params = new HashMap<>();
        params.put("usuario", usuario);
        params.put("fecha_inicio", fechaInicio);
        params.put("fecha_fin", fechaFin);
        params.put("logo", logo);

        JasperPrint print = JasperFillManager.fillReport(
                reporte, params, connection
        );

        String nombreArchivo = generarNombreArchivo("Bitacora", "xlsx");
        File destino = new File(carpetaDestino, nombreArchivo);

        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.setExporterOutput(
                new SimpleOutputStreamExporterOutput(destino)
        );

        SimpleXlsxReportConfiguration config =
                new SimpleXlsxReportConfiguration();
        config.setDetectCellType(true);
        config.setOnePagePerSheet(false);
        config.setRemoveEmptySpaceBetweenRows(true);

        exporter.setConfiguration(config);
        exporter.exportReport();

        return destino;
    }

    private static String generarNombreArchivo(
            String base,
            String extension
    ) {
        SimpleDateFormat sdf =
                new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
        return base + " - " + sdf.format(new java.util.Date()) + "." + extension;
    }

}
