package org.example.startseeing.view;

import org.example.startseeing.dao.PeliculaDAO;
import org.example.startseeing.entity.Pelicula;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class PeliculaViewFrame extends JFrame{
    private final PeliculaDAO peliculaDAO = new PeliculaDAO();
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private final JFrame menuReferencia;

    public PeliculaViewFrame(JFrame menu) {
        this.menuReferencia = menu;

        setTitle("Catálogo de Películas");
        setSize(800, 550);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel fondo = new JPanel(new BorderLayout(10, 10));
        fondo.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(fondo);

        // Título
        JLabel lblTitulo = new JLabel("Catálogo de Películas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        fondo.add(lblTitulo, BorderLayout.NORTH);

        // Tabla
        modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Año", "Estrellas", "Categoría", "Director"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(25);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JTableHeader header = tabla.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(52, 152, 219));
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(tabla);
        fondo.add(scrollPane, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel();
        JButton btnRegresar = new JButton("Regresar al Menú");
        btnRegresar.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnRegresar.addActionListener(e -> cerrarYVolver());

        panelInferior.add(btnRegresar);
        fondo.add(panelInferior, BorderLayout.SOUTH);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                cerrarYVolver();
            }
        });

        listar();
    }

    private void cerrarYVolver() {
        if (menuReferencia != null) {
            menuReferencia.setVisible(true);
        }
        dispose();
    }

    private void listar() {
        modeloTabla.setRowCount(0);
        List<Pelicula> peliculas = peliculaDAO.listar();
        for (Pelicula p : peliculas) {
            modeloTabla.addRow(new Object[]{
                    p.getId(),
                    p.getNombre(),
                    p.getAno(),
                    p.getEstrellas(),
                    p.getCategoria() != null ? p.getCategoria().getNombreCategoria() : "",
                    p.getDirector() != null ? p.getDirector().getNombre() : ""
            });
        }
    }


}
