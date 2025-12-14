package org.example.startseeing.view;

import org.example.startseeing.dao.CategoriaDAO;
import org.example.startseeing.dao.DirectoresDAO;
import org.example.startseeing.dao.PeliculaDAO;
import org.example.startseeing.entity.Categoria;
import org.example.startseeing.entity.Directores;
import org.example.startseeing.entity.Pelicula;
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


public class PeliculaFrame extends JFrame {

    private final PeliculaDAO peliculaDAO = new PeliculaDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final DirectoresDAO directorDAO = new DirectoresDAO();
    private final Usuario usuarioActual; // usuario logueado

    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtAno;
    private JTextField txtEstrellas;
    private JComboBox<Categoria> cbCategoria;
    private JComboBox<Directores> cbDirector;
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public PeliculaFrame(Usuario usuario) {
        this.usuarioActual = usuario;
        setTitle("Gestión de Películas");
        setSize(800, 550);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        //Fondo con degradado
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

        //Título
        JLabel lblTitulo = new JLabel("Gestión de Películas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(40, 40, 40));
        fondo.add(lblTitulo, BorderLayout.NORTH);

        //Panel central (formulario + tabla)
        JPanel panelCentral = new JPanel(new BorderLayout(15, 15));
        panelCentral.setOpaque(false);
        fondo.add(panelCentral, BorderLayout.CENTER);

        //Panel formulario
        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 10, 10));
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(new CompoundBorder(
                new LineBorder(new Color(180, 180, 180), 1, true),
                new EmptyBorder(10, 10, 10, 10)
        ));

        panelFormulario.add(new JLabel("ID:"));
        txtId = new JTextField();
        txtId.setEditable(false);
        panelFormulario.add(txtId);

        panelFormulario.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Año:"));
        txtAno = new JTextField();
        panelFormulario.add(txtAno);

        panelFormulario.add(new JLabel("Estrellas (0-5):"));
        txtEstrellas = new JTextField();
        panelFormulario.add(txtEstrellas);

        panelFormulario.add(new JLabel("Categoría:"));
        cbCategoria = new JComboBox<>();
        panelFormulario.add(cbCategoria);

        panelFormulario.add(new JLabel("Director:"));
        cbDirector = new JComboBox<>();
        panelFormulario.add(cbDirector);

        panelCentral.add(panelFormulario, BorderLayout.NORTH);

        //Tabla
        modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Año", "Estrellas", "Categoría", "Director"}, 0);
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(25);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setGridColor(new Color(220, 220, 220));

        JTableHeader header = tabla.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(52, 152, 219));
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(tabla);
        panelCentral.add(scrollPane, BorderLayout.CENTER);

        //Panel botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setOpaque(false);
        fondo.add(panelBotones, BorderLayout.SOUTH);

        JButton btnGuardar = crearBoton("Guardar", new Color(46, 204, 113));
        JButton btnActualizar = crearBoton("Actualizar", new Color(52, 152, 219));
        JButton btnEliminar = crearBoton("Eliminar", new Color(231, 76, 60));
        JButton btnListar = crearBoton("Listar", new Color(241, 196, 15));

        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnListar);

        //Cargar combos
        cargarCategorias();
        cargarDirectores();

        //Acciones CRUD
        btnGuardar.addActionListener(e -> guardar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnListar.addActionListener(e -> listar());

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                int fila = tabla.getSelectedRow();
                txtId.setText(modeloTabla.getValueAt(fila, 0).toString());
                txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
                txtAno.setText(modeloTabla.getValueAt(fila, 2).toString());
                txtEstrellas.setText(modeloTabla.getValueAt(fila, 3).toString());
                seleccionarComboCategoria(modeloTabla.getValueAt(fila, 4).toString());
                seleccionarComboDirector(modeloTabla.getValueAt(fila, 5).toString());
            }
        });

        listar();
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

        // Efecto hover
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

    private void cargarCategorias() {
        cbCategoria.removeAllItems();
        List<Categoria> categorias = categoriaDAO.listar();
        for (Categoria c : categorias) cbCategoria.addItem(c);
    }

    private void cargarDirectores() {
        cbDirector.removeAllItems();
        List<Directores> directores = directorDAO.listar();
        for (Directores d : directores) cbDirector.addItem(d);
    }

    private void guardar() {
        if (txtNombre.getText().isEmpty() || txtAno.getText().isEmpty() || txtEstrellas.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos.");
            return;
        }

        Pelicula p = new Pelicula();
        p.setNombre(txtNombre.getText());
        p.setAno(Integer.parseInt(txtAno.getText()));
        p.setEstrellas(Double.parseDouble(txtEstrellas.getText()));
        p.setCategoria((Categoria) cbCategoria.getSelectedItem());
        p.setDirector((Directores) cbDirector.getSelectedItem());

        peliculaDAO.guardar(p, usuarioActual.getUsername());
        limpiarCampos();
        listar();
    }

    private void actualizar() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione una película para actualizar.");
            return;
        }

        Pelicula p = new Pelicula();
        p.setId(Integer.parseInt(txtId.getText()));
        p.setNombre(txtNombre.getText());
        p.setAno(Integer.parseInt(txtAno.getText()));
        p.setEstrellas(Double.parseDouble(txtEstrellas.getText()));
        p.setCategoria((Categoria) cbCategoria.getSelectedItem());
        p.setDirector((Directores) cbDirector.getSelectedItem());

        peliculaDAO.actualizar(p, usuarioActual.getUsername());
        limpiarCampos();
        listar();
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            int id = (int) modeloTabla.getValueAt(fila, 0);
            peliculaDAO.eliminar(id, usuarioActual.getUsername());
            listar();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una fila para eliminar.");
        }
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

    private void seleccionarComboCategoria(String nombre) {
        for (int i = 0; i < cbCategoria.getItemCount(); i++) {
            if (cbCategoria.getItemAt(i).getNombreCategoria().equals(nombre)) {
                cbCategoria.setSelectedIndex(i);
                break;
            }
        }
    }

    private void seleccionarComboDirector(String nombre) {
        for (int i = 0; i < cbDirector.getItemCount(); i++) {
            if (cbDirector.getItemAt(i).getNombre().equals(nombre)) {
                cbDirector.setSelectedIndex(i);
                break;
            }
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtAno.setText("");
        txtEstrellas.setText("");
        cbCategoria.setSelectedIndex(-1);
        cbDirector.setSelectedIndex(-1);
    }


}
