package org.example.startseeing.view;

import org.example.startseeing.dao.BitacoraDAO;
import org.example.startseeing.entity.Bitacora;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BitacoraFrame extends JFrame {

    private final BitacoraDAO bitacoraDAO = new BitacoraDAO();
    private final DefaultTableModel tableModel;

    public BitacoraFrame() {
        setTitle("Registro de Bitácora del Sistema");
        setSize(700, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.decode("#E6E6E6"));
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitulo = new JLabel("Historial de acciones registradas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(lblTitulo, BorderLayout.NORTH);

        // Configuración de la tabla
        String[] columnas = {"ID", "Usuario", "Acción", "Fecha"};
        tableModel = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(tableModel);
        tabla.setEnabled(false);
        tabla.setRowHeight(25);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(tabla);
        add(scrollPane, BorderLayout.CENTER);

        // Panel inferior con botón de actualización
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(Color.decode("#E6E6E6"));

        JButton btnActualizar = new JButton("Actualizar registros");
        btnActualizar.setBackground(new Color(52, 152, 219));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.setFont(new Font("Arial", Font.BOLD, 14));
        btnActualizar.addActionListener(e -> cargarBitacora());

        panelBotones.add(btnActualizar);
        add(panelBotones, BorderLayout.SOUTH);

        // Cargar los registros al iniciar
        cargarBitacora();
    }

    private void cargarBitacora() {
        tableModel.setRowCount(0); // Limpiar tabla
        List<Bitacora> registros = bitacoraDAO.listar();
        for (Bitacora b : registros) {
            tableModel.addRow(new Object[]{
                    b.getId(),
                    b.getUsuario(),
                    b.getAccion(),
                    b.getFecha()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BitacoraFrame().setVisible(true));
    }

}
