package org.example.startseeing.view;

import com.toedter.calendar.JDateChooser;
import org.example.startseeing.dao.BitacoraDAO;
import org.example.startseeing.entity.Bitacora;
import org.example.startseeing.entity.Usuario;
import org.example.startseeing.render.FechaRenderer;
import org.example.startseeing.report.ReporteBitacoraManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;
import java.util.Date;

public class BitacoraFrame extends JFrame {

    private final BitacoraDAO bitacoraDAO = new BitacoraDAO();
    private final DefaultTableModel tableModel;
    private final JTable tabla;

    private JTextField txtUsuario;
    private JDateChooser dcDesde;
    private JDateChooser dcHasta;

    private final Usuario usuarioActual;
    private JButton btnActualizar;

    public BitacoraFrame(Usuario usuario) {
        this.usuarioActual = usuario;

        setTitle("Registro de Bitácora del Sistema");
        setSize(950, 520);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel fondo = new JPanel(new BorderLayout(10, 10));
        fondo.setBorder(new EmptyBorder(10, 10, 10, 10));
        fondo.setBackground(new Color(230, 230, 230));
        setContentPane(fondo);

        // TÍTULO
        JLabel lblTitulo = new JLabel("Historial de acciones registradas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        fondo.add(lblTitulo, BorderLayout.NORTH);

        // PANEL BÚSQUEDA
        JPanel panelBusqueda = new JPanel(new GridBagLayout());
        panelBusqueda.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        panelBusqueda.add(new JLabel("Usuario:"), gbc);

        txtUsuario = new JTextField(14);
        gbc.gridx = 1;
        panelBusqueda.add(txtUsuario, gbc);

        gbc.gridx = 2;
        panelBusqueda.add(new JLabel("Desde:"), gbc);

        dcDesde = new JDateChooser();
        dcDesde.setDateFormatString("yyyy-MM-dd");
        gbc.gridx = 3;
        panelBusqueda.add(dcDesde, gbc);

        gbc.gridx = 4;
        panelBusqueda.add(new JLabel("Hasta:"), gbc);

        dcHasta = new JDateChooser();
        dcHasta.setDateFormatString("yyyy-MM-dd");
        gbc.gridx = 5;
        panelBusqueda.add(dcHasta, gbc);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(46, 204, 113));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.addActionListener(e -> buscar());

        gbc.gridx = 6;
        panelBusqueda.add(btnBuscar, gbc);

        fondo.add(panelBusqueda, BorderLayout.BEFORE_FIRST_LINE);

        // TABLA
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Usuario", "Acción", "Fecha"}, 0
        );
        tabla = new JTable(tableModel);
        tabla.setRowHeight(25);
        tabla.setEnabled(false);
        tabla.getColumnModel().getColumn(3).setCellRenderer(new FechaRenderer());

        fondo.add(new JScrollPane(tabla), BorderLayout.CENTER);

        // BOTONES
        JPanel panelBotones = new JPanel();
        panelBotones.setOpaque(false);

        btnActualizar = new JButton("Actualizar");
        btnActualizar.setBackground(new Color(52, 152, 219));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.addActionListener(e -> cargarBitacora());

        JButton btnImprimir = new JButton("Imprimir");
        btnImprimir.setBackground(new Color(155, 89, 182));
        btnImprimir.setForeground(Color.WHITE);
        btnImprimir.addActionListener(e -> imprimirReporte());

        JButton btnRegresar = new JButton("Regresar");
        btnRegresar.setBackground(new Color(231, 76, 60));
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.addActionListener(e -> {
            new MenuPrincipal(usuarioActual, usuarioActual.getRoles()).setVisible(true);
            dispose();
        });

        panelBotones.add(btnActualizar);
        panelBotones.add(btnImprimir);
        panelBotones.add(btnRegresar);

        fondo.add(panelBotones, BorderLayout.SOUTH);

        cargarBitacora();
        aplicarPermisos();
    }

    private void cargarBitacora() {
        tableModel.setRowCount(0);
        cargarTabla(bitacoraDAO.listar());
    }

    private void buscar() {
        String usuario = txtUsuario.getText().trim();
        Date desdeDate = dcDesde.getDate();
        Date hastaDate = dcHasta.getDate();

        List<Bitacora> registros;

        boolean hayUsuario = !usuario.isEmpty();
        boolean hayFechas = desdeDate != null && hastaDate != null;

        if (hayUsuario && hayFechas) {
            registros = bitacoraDAO.buscarPorUsuarioYFechas(
                    usuario,
                    toLocalDate(desdeDate),
                    toLocalDate(hastaDate)
            );
        } else if (hayUsuario) {
            registros = bitacoraDAO.buscarPorUsuario(usuario);
        } else if (hayFechas) {
            registros = bitacoraDAO.buscarPorFechas(
                    toLocalDate(desdeDate),
                    toLocalDate(hastaDate)
            );
        } else {
            registros = bitacoraDAO.listar();
        }

        tableModel.setRowCount(0);
        cargarTabla(registros);
    }

    private void cargarTabla(List<Bitacora> registros) {
        for (Bitacora b : registros) {
            tableModel.addRow(new Object[]{
                    b.getId(),
                    b.getUsuario(),
                    b.getAccion(),
                    b.getFecha()
            });
        }
    }

    private void aplicarPermisos() {
        boolean permitido = usuarioActual.getRoles().stream()
                .anyMatch(r ->
                        r.getNombre().equalsIgnoreCase("ADMIN")
                                || r.getNombre().equalsIgnoreCase("LECTOR")
                );

        btnActualizar.setEnabled(permitido);
    }

    // IMPRIMIR REPORTE

    private void imprimirReporte() {

        String usuario = txtUsuario.getText().trim();
        Date desde = dcDesde.getDate();
        Date hasta = dcHasta.getDate();

        java.sql.Date fechaInicio = null;
        java.sql.Date fechaFin = null;

        if (desde != null && hasta != null) {
            fechaInicio = new java.sql.Date(inicioDia(desde).getTime());
            fechaFin = new java.sql.Date(finDia(hasta).getTime());
        }

        Object[] opciones = {"PDF", "Excel", "Cancelar"};
        int opcion = JOptionPane.showOptionDialog(
                this,
                "Seleccione el formato del reporte",
                "Exportar Bitácora",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (opcion == 2 || opcion == JOptionPane.CLOSED_OPTION) return;

        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File archivoSeleccionado = chooser.getSelectedFile();
        File carpetaDestino = archivoSeleccionado.getParentFile();

        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/start_seeing_db",
                "root",
                "1234"
        )) {

            File archivoGenerado;

            if (opcion == 0) {
                archivoGenerado = ReporteBitacoraManager.exportarPDF(
                        con,
                        usuario.isEmpty() ? null : usuario,
                        fechaInicio,
                        fechaFin,
                        carpetaDestino
                );
            } else {
                archivoGenerado = ReporteBitacoraManager.exportarExcel(
                        con,
                        usuario.isEmpty() ? null : usuario,
                        fechaInicio,
                        fechaFin,
                        carpetaDestino
                );
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Reporte generado correctamente:\n" +
                            archivoGenerado.getAbsolutePath(),
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Error al generar el reporte:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    private LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private Date inicioDia(Date fecha) {
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    private Date finDia(Date fecha) {
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

}
