package org.example.startseeing.view;

import org.example.startseeing.dao.CategoriaDAO;
import org.example.startseeing.entity.Categoria;
import org.example.startseeing.entity.Usuario;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;


public class CategoriaFrame extends JFrame {

    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final Usuario usuarioActual;

    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtClasificacion;
    private JTable tablaCategorias;
    private DefaultTableModel modeloTabla;


    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnListar;

    // Constructor
    public CategoriaFrame(Usuario usuario) {
        this.usuarioActual = usuario;

        setTitle("Gestión de Categorías");
        setSize(700, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Fondo con degradado
        JPanel fondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(230, 230, 230),
                        0, getHeight(), new Color(200, 200, 200)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        fondo.setLayout(new BorderLayout(10, 10));
        fondo.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(fondo);

        // Título
        JLabel lblTitulo = new JLabel("Gestión de Categorías", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(40, 40, 40));
        fondo.add(lblTitulo, BorderLayout.NORTH);

        // Panel central
        JPanel panelCentral = new JPanel(new BorderLayout(15, 15));
        panelCentral.setOpaque(false);
        fondo.add(panelCentral, BorderLayout.CENTER);

        // Formulario
        JPanel panelFormulario = new JPanel(new GridLayout(3, 2, 10, 10));
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(new CompoundBorder(
                new LineBorder(new Color(180, 180, 180), 1, true),
                new EmptyBorder(10, 10, 10, 10)
        ));

        panelFormulario.add(new JLabel("ID:"));
        txtId = new JTextField();
        txtId.setEditable(false);
        panelFormulario.add(txtId);

        panelFormulario.add(new JLabel("Nombre Categoría:"));
        txtNombre = new JTextField();
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Clasificación:"));
        txtClasificacion = new JTextField();
        panelFormulario.add(txtClasificacion);

        panelCentral.add(panelFormulario, BorderLayout.NORTH);

        // Tabla
        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Clasificación"}, 0);
        tablaCategorias = new JTable(modeloTabla);
        tablaCategorias.setRowHeight(25);
        tablaCategorias.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaCategorias.setGridColor(new Color(220, 220, 220));

        JTableHeader header = tablaCategorias.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(52, 152, 219));
        header.setForeground(Color.WHITE);

        panelCentral.add(new JScrollPane(tablaCategorias), BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setOpaque(false);
        fondo.add(panelBotones, BorderLayout.SOUTH);

        btnGuardar = crearBoton("Guardar", new Color(46, 204, 113));
        btnActualizar = crearBoton("Actualizar", new Color(52, 152, 219));
        btnEliminar = crearBoton("Eliminar", new Color(231, 76, 60));
        btnListar = crearBoton("Listar", new Color(241, 196, 15));

        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnListar);

        // Acciones
        btnGuardar.addActionListener(e -> {
            if (txtNombre.getText().isEmpty() || txtClasificacion.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos.");
                return;
            }
            Categoria c = new Categoria();
            c.setNombreCategoria(txtNombre.getText());
            c.setClasificacion(txtClasificacion.getText());
            categoriaDAO.guardar(c, usuarioActual.getUsername());
            limpiarCampos();
            listarCategorias();
        });

        btnActualizar.addActionListener(e -> {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleccione una categoría.");
                return;
            }
            Categoria c = new Categoria();
            c.setId(Integer.parseInt(txtId.getText()));
            c.setNombreCategoria(txtNombre.getText());
            c.setClasificacion(txtClasificacion.getText());
            categoriaDAO.actualizar(c, usuarioActual.getUsername());
            limpiarCampos();
            listarCategorias();
        });

        btnEliminar.addActionListener(e -> {
            int fila = tablaCategorias.getSelectedRow();
            if (fila >= 0) {
                int id = (int) modeloTabla.getValueAt(fila, 0);
                categoriaDAO.eliminar(id, usuarioActual.getUsername());
                listarCategorias();
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una fila.");
            }
        });

        btnListar.addActionListener(e -> listarCategorias());

        tablaCategorias.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaCategorias.getSelectedRow() >= 0) {
                int fila = tablaCategorias.getSelectedRow();
                txtId.setText(modeloTabla.getValueAt(fila, 0).toString());
                txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
                txtClasificacion.setText(modeloTabla.getValueAt(fila, 2).toString());
            }
        });

        listarCategorias();

        aplicarPermisos();
    }


    private void aplicarPermisos() {
        boolean esAdmin = usuarioActual.getRoles().stream()
                .anyMatch(r -> r.getNombre().equalsIgnoreCase("ADMIN"));

        boolean esEditor = usuarioActual.getRoles().stream()
                .anyMatch(r -> r.getNombre().equalsIgnoreCase("EDITOR"));

        boolean esLector = usuarioActual.getRoles().stream()
                .anyMatch(r -> r.getNombre().equalsIgnoreCase("LECTOR"));

        btnGuardar.setEnabled(esAdmin || esEditor);
        btnActualizar.setEnabled(esAdmin || esEditor);
        btnEliminar.setEnabled(esAdmin);
        btnListar.setEnabled(esAdmin || esEditor || esLector);
    }

    private JButton crearBoton(String texto, Color colorFondo) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setBackground(colorFondo);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(110, 35));

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(colorFondo.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(colorFondo);
            }
        });
        return boton;
    }

    private void listarCategorias() {
        modeloTabla.setRowCount(0);
        List<Categoria> categorias = categoriaDAO.listar();
        for (Categoria c : categorias) {
            modeloTabla.addRow(new Object[]{
                    c.getId(),
                    c.getNombreCategoria(),
                    c.getClasificacion()
            });
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtClasificacion.setText("");
    }


}
