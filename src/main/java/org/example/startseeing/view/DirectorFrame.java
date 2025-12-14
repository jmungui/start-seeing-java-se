package org.example.startseeing.view;

import org.example.startseeing.dao.DirectoresDAO;
import org.example.startseeing.entity.Directores;
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


public class DirectorFrame extends JFrame {
    private final DirectoresDAO directorDAO = new DirectoresDAO();
    private final Usuario usuarioActual; // usuario logueado

    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtPais;
    private JCheckBox chkOscar;
    private JTable tablaDirectores;
    private DefaultTableModel modeloTabla;

    public DirectorFrame(Usuario usuario) {
        this.usuarioActual = usuario;
        setTitle("Gestión de Directores");
        setSize(700, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        //Fondo con degradado suave
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

        //Título principal
        JLabel lblTitulo = new JLabel("Gestión de Directores", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(40, 40, 40));
        fondo.add(lblTitulo, BorderLayout.NORTH);

        //Panel central (formulario + tabla)
        JPanel panelCentral = new JPanel(new BorderLayout(15, 15));
        panelCentral.setOpaque(false);
        fondo.add(panelCentral, BorderLayout.CENTER);

        //Panel del formulario
        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 10, 10));
        panelFormulario.setOpaque(true);
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

        panelFormulario.add(new JLabel("País de Origen:"));
        txtPais = new JTextField();
        panelFormulario.add(txtPais);

        panelFormulario.add(new JLabel("¿Tiene Oscar?:"));
        chkOscar = new JCheckBox();
        chkOscar.setBackground(Color.WHITE);
        panelFormulario.add(chkOscar);

        panelCentral.add(panelFormulario, BorderLayout.NORTH);

        //Tabla de directores
        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "País", "Oscar"}, 0);
        tablaDirectores = new JTable(modeloTabla);
        tablaDirectores.setRowHeight(25);
        tablaDirectores.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaDirectores.setGridColor(new Color(220, 220, 220));

        JTableHeader header = tablaDirectores.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(52, 152, 219));
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(tablaDirectores);
        panelCentral.add(scrollPane, BorderLayout.CENTER);

        //Panel inferior (botones)
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

        // Acciones CRUD
        btnGuardar.addActionListener(e -> guardar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnListar.addActionListener(e -> listar());

        // Selección en la tabla
        tablaDirectores.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaDirectores.getSelectedRow() >= 0) {
                int fila = tablaDirectores.getSelectedRow();
                txtId.setText(modeloTabla.getValueAt(fila, 0).toString());
                txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
                txtPais.setText(modeloTabla.getValueAt(fila, 2).toString());
                chkOscar.setSelected((boolean) modeloTabla.getValueAt(fila, 3));
            }
        });

        listar();
    }

    // Crear botón con estilo y hover
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

    // ===== MÉTODOS CRUD =====
    private void guardar() {
        if (txtNombre.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Directores d = new Directores();
        d.setNombre(txtNombre.getText());
        d.setPaisOrigen(txtPais.getText());
        d.setOscar(chkOscar.isSelected());
        directorDAO.guardar(d, usuarioActual.getUsername());
        limpiarCampos();
        listar();
    }

    private void actualizar() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un director para actualizar.");
            return;
        }
        Directores d = new Directores();
        d.setId(Integer.parseInt(txtId.getText()));
        d.setNombre(txtNombre.getText());
        d.setPaisOrigen(txtPais.getText());
        d.setOscar(chkOscar.isSelected());
        directorDAO.actualizar(d, usuarioActual.getUsername());
        limpiarCampos();
        listar();
    }

    private void eliminar() {
        int fila = tablaDirectores.getSelectedRow();
        if (fila >= 0) {
            int id = (int) modeloTabla.getValueAt(fila, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de eliminar este director?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                directorDAO.eliminar(id, usuarioActual.getUsername());
                listar();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una fila para eliminar.");
        }
    }

    private void listar() {
        modeloTabla.setRowCount(0);
        List<Directores> directores = directorDAO.listar();
        for (Directores d : directores) {
            modeloTabla.addRow(new Object[]{d.getId(), d.getNombre(), d.getPaisOrigen(), d.isOscar()});
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtPais.setText("");
        chkOscar.setSelected(false);
    }


}
