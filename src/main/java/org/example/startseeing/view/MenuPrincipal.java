package org.example.startseeing.view;

import org.example.startseeing.dao.BitacoraDAO;
import org.example.startseeing.entity.Rol;
import org.example.startseeing.entity.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

public class MenuPrincipal extends JFrame {
    private final BitacoraDAO bitacoraDAO = new BitacoraDAO();

    //Constructor principal con usuario y roles
    public MenuPrincipal(Usuario usuario, Set<Rol> roles) {
        setTitle("START_SEEING - Menú Principal");
        setSize(500, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        //Fondo con degradado
        JPanel fondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(240, 240, 240),
                        0, getHeight(), new Color(210, 210, 210)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        fondo.setLayout(null);
        setContentPane(fondo);

        //Logo
        JLabel logo = new JLabel();
        ImageIcon icono = crearIcono("/imagen/Logo.png", 90, 90);
        if (icono != null) logo.setIcon(icono);
        logo.setBounds(200, 20, 100, 100);
        fondo.add(logo);

        //Título
        JLabel titulo = new JLabel("START SEEING", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(new Color(40, 40, 40));
        titulo.setBounds(120, 120, 250, 30);
        fondo.add(titulo);

        //Bienvenida
        JLabel lblBienvenida = new JLabel("Bienvenido: " + usuario.getUsername(), SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblBienvenida.setBounds(130, 150, 250, 25);
        fondo.add(lblBienvenida);

        //Botones
        JButton btnCategorias = crearBoton("Gestionar Categorías", new Color(52, 152, 219));
        btnCategorias.setBounds(140, 200, 220, 40);
        btnCategorias.addActionListener((ActionEvent e) -> new CategoriaFrame(usuario).setVisible(true));
        fondo.add(btnCategorias);

        JButton btnDirectores = crearBoton("Gestionar Directores", new Color(243, 156, 18));
        btnDirectores.setBounds(140, 250, 220, 40);
        btnDirectores.addActionListener((ActionEvent e)-> new DirectorFrame(usuario).setVisible(true));
        fondo.add(btnDirectores);

        JButton btnPeliculas = crearBoton("Gestionar Películas", new Color(46, 204, 113));
        btnPeliculas.setBounds(140, 300, 220, 40);
        btnPeliculas.addActionListener((ActionEvent e) -> new PeliculaFrame(usuario).setVisible(true));
        fondo.add(btnPeliculas);

        JButton btnUsuarios = crearBoton("👤 Gestionar Usuarios", new Color(155, 89, 182));
        btnUsuarios.setBounds(140, 350, 220, 40);
        btnUsuarios.addActionListener((ActionEvent e) -> new UsuarioFrame().setVisible(true));

        JButton btnBitacora = crearBoton("Ver Bitácora", new Color(52, 73, 94));
        btnBitacora.setBounds(140, 400, 220, 40);
        btnBitacora.addActionListener((ActionEvent e) -> new BitacoraFrame().setVisible(true));

        JButton btnSalir = crearBoton("Cerrar Sesión", new Color(231, 76, 60));
        btnSalir.setBounds(140, 450, 220, 40);
        btnSalir.addActionListener(e -> {
            bitacoraDAO.registrarAccion(usuario.getUsername(), "Cierre de sesión");
            new LoginFrame().setVisible(true);
            dispose();
        });
        fondo.add(btnSalir);

        // Control de visibilidad según roles
        boolean esAdmin = roles.stream().anyMatch(r -> r.getNombre().equalsIgnoreCase("ADMIN"));
        boolean esEditor = roles.stream().anyMatch(r -> r.getNombre().equalsIgnoreCase("EDITOR"));
        boolean esUser = roles.stream().anyMatch(r -> r.getNombre().equalsIgnoreCase("USER"));

        if (esAdmin) {
            fondo.add(btnUsuarios);
            fondo.add(btnBitacora);
        } else if (esEditor) {
            fondo.add(btnBitacora);
        } else if (esUser) {
            btnCategorias.setEnabled(false);
            btnDirectores.setEnabled(false);
        }
    }

    // metodo para obtener botones sin iconos
    private JButton crearBoton(String texto, Color colorFondo) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setForeground(Color.WHITE);
        boton.setBackground(colorFondo);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

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

    private ImageIcon crearIcono(String path, int width, int height) {
        try {
            java.net.URL imgURL = getClass().getResource(path);
            if (imgURL != null) {
                ImageIcon originalIcon = new ImageIcon(imgURL);
                Image img = originalIcon.getImage();
                Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(newImg);
            } else {
                System.err.println("Icono no encontrado: " + path);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // pruebas
    public MenuPrincipal() {
        this(new Usuario("DemoUser", "demo123"),
                new HashSet<>(Set.of(new Rol("ADMIN"))));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
    }
}
