package org.example.startseeing.view;

import org.example.startseeing.dao.BitacoraDAO;
import org.example.startseeing.dao.RolDAO;
import org.example.startseeing.dao.UsuarioDAO;
import org.example.startseeing.entity.Rol;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UsuarioFrame extends JFrame {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final RolDAO rolDAO = new RolDAO();
    private final BitacoraDAO bitacoraDAO = new BitacoraDAO();
    private final Usuario usuarioActual;

    private JTextField txtId;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<Rol> cbRol;

    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;

    // 🔐 Constructor
    public UsuarioFrame(Usuario usuario) {
        this.usuarioActual = usuario;
        validarPermisos();

        setTitle("Gestión de Usuarios");
        setSize(700, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // 🎨 Fondo degradado (IGUAL a CategoriaFrame)
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

        // 🏷 Título
        JLabel lblTitulo = new JLabel("Gestión de Usuarios", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(40, 40, 40));
        fondo.add(lblTitulo, BorderLayout.NORTH);

        // 📦 Panel central
        JPanel panelCentral = new JPanel(new BorderLayout(15, 15));
        panelCentral.setOpaque(false);
        fondo.add(panelCentral, BorderLayout.CENTER);

        // 📝 Formulario (MISMA ESTRUCTURA)
        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 10, 10));
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(new CompoundBorder(
                new LineBorder(new Color(180, 180, 180), 1, true),
                new EmptyBorder(10, 10, 10, 10)
        ));

        panelFormulario.add(new JLabel("ID:"));
        txtId = new JTextField();
        txtId.setEditable(false);
        panelFormulario.add(txtId);

        panelFormulario.add(new JLabel("Usuario:"));
        txtUsername = new JTextField();
        panelFormulario.add(txtUsername);

        panelFormulario.add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        panelFormulario.add(txtPassword);

        panelFormulario.add(new JLabel("Rol:"));
        cbRol = new JComboBox<>();
        panelFormulario.add(cbRol);

        panelCentral.add(panelFormulario, BorderLayout.NORTH);

        // 📊 Tabla
        modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Usuario", "Rol"}, 0
        );

        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setRowHeight(25);
        tablaUsuarios.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaUsuarios.setGridColor(new Color(220, 220, 220));

        JTableHeader header = tablaUsuarios.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(52, 152, 219));
        header.setForeground(Color.WHITE);

        panelCentral.add(new JScrollPane(tablaUsuarios), BorderLayout.CENTER);

        // 🔘 Botones (IGUALES)
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

        // ⚙ Acciones
        btnGuardar.addActionListener(e -> guardarUsuario());
        btnActualizar.addActionListener(e -> actualizarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());
        btnListar.addActionListener(e -> listarUsuarios());

        tablaUsuarios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaUsuarios.getSelectedRow() >= 0) {
                int fila = tablaUsuarios.getSelectedRow();
                txtId.setText(modeloTabla.getValueAt(fila, 0).toString());
                txtUsername.setText(modeloTabla.getValueAt(fila, 1).toString());
                seleccionarRol(modeloTabla.getValueAt(fila, 2).toString());
            }
        });

        cargarRoles();
        listarUsuarios();
    }

    // ================= CRUD =================

    private void guardarUsuario() {
        if (txtUsername.getText().isEmpty()
                || txtPassword.getPassword().length == 0
                || cbRol.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos");
            return;
        }

        Usuario u = new Usuario();
        u.setUsername(txtUsername.getText());
        u.setPassword(new String(txtPassword.getPassword()));
        u.getRoles().add((Rol) cbRol.getSelectedItem());

        usuarioDAO.guardar(u);
        bitacoraDAO.registrarAccion(
                usuarioActual.getUsername(),
                "Creó usuario: " + u.getUsername()
        );

        limpiarCampos();
        listarUsuarios();
    }

    private void actualizarUsuario() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario");
            return;
        }

        Usuario u = usuarioDAO.buscarPorId(Integer.parseInt(txtId.getText()));
        u.setUsername(txtUsername.getText());

        if (txtPassword.getPassword().length > 0) {
            u.setPassword(new String(txtPassword.getPassword()));
        }

        u.getRoles().clear();
        u.getRoles().add((Rol) cbRol.getSelectedItem());

        usuarioDAO.actualizar(u);
        bitacoraDAO.registrarAccion(
                usuarioActual.getUsername(),
                "Actualizó usuario: " + u.getUsername()
        );

        limpiarCampos();
        listarUsuarios();
    }

    private void eliminarUsuario() {
        if (txtId.getText().isEmpty()) return;

        usuarioDAO.eliminar(Integer.parseInt(txtId.getText()));
        bitacoraDAO.registrarAccion(
                usuarioActual.getUsername(),
                "Eliminó usuario ID: " + txtId.getText()
        );

        limpiarCampos();
        listarUsuarios();
    }

    // ================= UTIL =================

    private void listarUsuarios() {
        modeloTabla.setRowCount(0);
        for (Usuario u : usuarioDAO.listar()) {
            String rol = u.getRoles().stream().findFirst().map(Rol::getNombre).orElse("");
            modeloTabla.addRow(new Object[]{
                    u.getId(),
                    u.getUsername(),
                    rol
            });
        }
    }

    private void cargarRoles() {
        cbRol.removeAllItems();
        for (Rol r : rolDAO.listar()) {
            cbRol.addItem(r);
        }
    }

    private void seleccionarRol(String nombreRol) {
        for (int i = 0; i < cbRol.getItemCount(); i++) {
            if (cbRol.getItemAt(i).getNombre().equals(nombreRol)) {
                cbRol.setSelectedIndex(i);
                break;
            }
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        cbRol.setSelectedIndex(-1);
    }

    private void validarPermisos() {
        boolean esAdmin = usuarioActual.getRoles().stream()
                .anyMatch(r -> r.getNombre().equalsIgnoreCase("ADMIN"));

        if (!esAdmin) {
            JOptionPane.showMessageDialog(
                    this,
                    "No tiene permisos para administrar usuarios",
                    "Acceso denegado",
                    JOptionPane.ERROR_MESSAGE
            );
            dispose();
        }
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

}
