package org.example.startseeing.view;

import org.example.startseeing.dao.BitacoraDAO;
import org.example.startseeing.dao.UsuarioDAO;
import org.example.startseeing.entity.Rol;
import org.example.startseeing.entity.Usuario;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class LoginFrame extends JFrame {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final BitacoraDAO bitacoraDAO = new BitacoraDAO();

    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public LoginFrame() {
        setTitle("Inicio de Sesión - START_SEEING");
        setSize(350, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.decode("#E6E6E6"));
        setLayout(null);

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setBounds(40, 50, 80, 25);
        add(lblUsuario);

        txtUsername = new JTextField();
        txtUsername.setBounds(130, 50, 150, 25);
        add(txtUsername);

        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setBounds(40, 90, 80, 25);
        add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(130, 90, 150, 25);
        add(txtPassword);

        JButton btnLogin = new JButton("Ingresar");
        btnLogin.setBounds(110, 140, 120, 35);
        btnLogin.setBackground(new Color(46, 204, 113));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        add(btnLogin);

        btnLogin.addActionListener(e -> autenticar());
    }

    private void autenticar() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        Usuario usuario = usuarioDAO.autenticar(username, password);

        if (usuario != null) {
            bitacoraDAO.registrarAccion(username, "Inicio de sesión exitoso");

            JOptionPane.showMessageDialog(this, "Bienvenido " + username);

            // Abrimos el menú principal con roles
            Set<Rol> roles = usuario.getRoles();
            new MenuPrincipal(usuario, roles).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
