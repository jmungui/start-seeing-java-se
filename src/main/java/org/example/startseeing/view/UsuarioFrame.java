package org.example.startseeing.view;

import org.example.startseeing.dao.RolDAO;
import org.example.startseeing.dao.UsuarioDAO;
import org.example.startseeing.entity.Rol;
import org.example.startseeing.entity.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UsuarioFrame extends JFrame {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final RolDAO rolDAO = new RolDAO();

    private JTextField txtId;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JList<Rol> listRoles;
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public UsuarioFrame() {
        setTitle("Gestión de Usuarios");
        setSize(700, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.decode("#E6E6E6"));

        // Panel superior
        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Usuario"));
        panelFormulario.setBackground(Color.decode("#E6E6E6"));

        panelFormulario.add(new JLabel("ID:"));
        txtId = new JTextField();
        txtId.setEditable(false);
        panelFormulario.add(txtId);

        panelFormulario.add(new JLabel("Username:"));
        txtUsername = new JTextField();
        panelFormulario.add(txtUsername);

        panelFormulario.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        panelFormulario.add(txtPassword);

        panelFormulario.add(new JLabel("Roles:"));
        listRoles = new JList<>();
        listRoles.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        panelFormulario.add(new JScrollPane(listRoles));

        add(panelFormulario, BorderLayout.NORTH);

        // Panel (Tabla)
        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Username", "Roles"}, 0);
        tabla = new JTable(modeloTabla);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Panel (Botones)
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(Color.decode("#E6E6E6"));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnListar = new JButton("Listar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnListar);

        add(panelBotones, BorderLayout.SOUTH);

        // Eventos
        btnGuardar.addActionListener(e -> guardarUsuario());
        btnActualizar.addActionListener(e -> actualizarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());
        btnListar.addActionListener(e -> listarUsuarios());

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                int fila = tabla.getSelectedRow();
                txtId.setText(modeloTabla.getValueAt(fila, 0).toString());
                txtUsername.setText(modeloTabla.getValueAt(fila, 1).toString());
                txtPassword.setText("");
                seleccionarRolesTabla(fila);
            }
        });

        cargarRoles();
        listarUsuarios();
    }

    private void guardarUsuario() {
        if (txtUsername.getText().isEmpty() || txtPassword.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "Debe ingresar usuario y contraseña.");
            return;
        }

        Usuario u = new Usuario();
        u.setUsername(txtUsername.getText());
        u.setPassword(new String(txtPassword.getPassword()));
        u.setRoles(new HashSet<>(listRoles.getSelectedValuesList()));

        usuarioDAO.guardar(u);
        limpiarCampos();
        listarUsuarios();
    }

    private void actualizarUsuario() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para actualizar.");
            return;
        }

        Usuario u = usuarioDAO.buscarPorId(Integer.parseInt(txtId.getText()));
        if (u != null) {
            u.setUsername(txtUsername.getText());
            if (txtPassword.getPassword().length > 0) {
                u.setPassword(new String(txtPassword.getPassword()));
            }
            u.setRoles(new HashSet<>(listRoles.getSelectedValuesList()));

            usuarioDAO.actualizar(u);
            limpiarCampos();
            listarUsuarios();
        }
    }

    private void eliminarUsuario() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            int id = (int) modeloTabla.getValueAt(fila, 0);
            usuarioDAO.eliminar(id);
            listarUsuarios();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una fila para eliminar.");
        }
    }

    private void listarUsuarios() {
        modeloTabla.setRowCount(0);
        List<Usuario> usuarios = usuarioDAO.listar();
        for (Usuario u : usuarios) {
            String roles = String.join(", ",
                    u.getRoles().stream().map(Rol::getNombre).toList());
            modeloTabla.addRow(new Object[]{u.getId(), u.getUsername(), roles});
        }
    }

    private void cargarRoles() {
        List<Rol> roles = rolDAO.listar();
        DefaultListModel<Rol> model = new DefaultListModel<>();
        for (Rol rol : roles) {
            model.addElement(rol);
        }
        listRoles.setModel(model);
    }

    private void seleccionarRolesTabla(int fila) {
        String username = modeloTabla.getValueAt(fila, 1).toString();
        Usuario usuario = usuarioDAO.buscarPorUsername(username);

        if (usuario != null) {
            Set<Rol> rolesUsuario = usuario.getRoles();
            int[] indicesSeleccionados = usuario.getRoles().stream()
                    .mapToInt(r -> {
                        for (int i = 0; i < listRoles.getModel().getSize(); i++) {
                            if (listRoles.getModel().getElementAt(i).getId() == r.getId()) {
                                return i;
                            }
                        }
                        return -1;
                    }).filter(i -> i != -1).toArray();

            listRoles.setSelectedIndices(indicesSeleccionados);
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        listRoles.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UsuarioFrame().setVisible(true));
    }
}
