package org.example.startseeing.view;

import org.example.startseeing.dao.BitacoraDAO;
import org.example.startseeing.entity.Rol;
import org.example.startseeing.entity.Usuario;
import org.example.startseeing.util.UtilEntity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

public class MenuPrincipal extends JFrame {

    private final BitacoraDAO bitacoraDAO = new BitacoraDAO();

    public MenuPrincipal(Usuario usuario, Set<Rol> roles) {

        setTitle("START_SEEING - Menú Principal");
        setSize(500, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

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

        JLabel logo = new JLabel();
        ImageIcon icono = crearIcono("/imagen/Logo.png", 90, 90);
        if (icono != null) logo.setIcon(icono);
        logo.setBounds(200, 20, 100, 100);
        fondo.add(logo);

        JLabel titulo = new JLabel("START SEEING", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setBounds(120, 120, 250, 30);
        fondo.add(titulo);

        JLabel lblBienvenida = new JLabel(
                "Bienvenido: " + usuario.getUsername(),
                SwingConstants.CENTER
        );
        lblBienvenida.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblBienvenida.setBounds(130, 150, 250, 25);
        fondo.add(lblBienvenida);

        JButton btnCategorias = crearBoton("Gestionar Categorías", new Color(52, 152, 219));
        btnCategorias.setBounds(140, 200, 220, 40);
        ImageIcon iconCategorias = crearIcono("/imagen/categories.png", 22, 22);
        if (iconCategorias != null) {
            btnCategorias.setIcon(iconCategorias);
            btnCategorias.setHorizontalAlignment(SwingConstants.LEFT);
            btnCategorias.setIconTextGap(10);
            btnCategorias.setMargin(new Insets(0, 15, 0, 10));
        }
        btnCategorias.addActionListener(e ->
                new CategoriaFrame(usuario).setVisible(true)
        );

        JButton btnDirectores = crearBoton("Gestionar Directores", new Color(243, 156, 18));
        btnDirectores.setBounds(140, 250, 220, 40);
        ImageIcon iconDirectores = crearIcono("/imagen/director.png", 22, 22);
        if (iconDirectores != null) {
            btnDirectores.setIcon(iconDirectores);
            btnDirectores.setHorizontalAlignment(SwingConstants.LEFT);
            btnDirectores.setIconTextGap(10);
            btnDirectores.setMargin(new Insets(0, 15, 0, 10));
        }
        btnDirectores.addActionListener(e ->
                new DirectorFrame(usuario).setVisible(true)
        );

        JButton btnPeliculas = crearBoton("Gestionar Películas", new Color(46, 204, 113));
        btnPeliculas.setBounds(140, 300, 220, 40);
        ImageIcon iconPeliculas = crearIcono("/imagen/movies.png", 22, 22);
        if (iconPeliculas != null) {
            btnPeliculas.setIcon(iconPeliculas);
            btnPeliculas.setHorizontalAlignment(SwingConstants.LEFT);
            btnPeliculas.setIconTextGap(10);
            btnPeliculas.setMargin(new Insets(0, 15, 0, 10));
        }
        btnPeliculas.addActionListener(e ->
                new PeliculaFrame(usuario).setVisible(true)
        );

        JButton btnUsuarios = crearBoton("Gestionar Usuarios", new Color(155, 89, 182));
        btnUsuarios.setBounds(140, 350, 220, 40);
        ImageIcon iconUsuarios = crearIcono("/imagen/Usuario.png", 22, 22);
        if (iconUsuarios != null) {
            btnUsuarios.setIcon(iconUsuarios);
            btnUsuarios.setHorizontalAlignment(SwingConstants.LEFT);
            btnUsuarios.setIconTextGap(10);
            btnUsuarios.setMargin(new Insets(0, 15, 0, 10));
        }
        btnUsuarios.addActionListener(e ->
                new UsuarioFrame(usuario).setVisible(true)
        );

        JButton btnBitacora = crearBoton("Ver Bitácora", new Color(52, 73, 94));
        btnBitacora.setBounds(140, 400, 220, 40);
        ImageIcon iconBitacora = crearIcono("/imagen/bitacora.jpg", 22, 22);
        if (iconBitacora != null) {
            btnBitacora.setIcon(iconBitacora);
            btnBitacora.setHorizontalAlignment(SwingConstants.LEFT);
            btnBitacora.setIconTextGap(10);
            btnBitacora.setMargin(new Insets(0, 15, 0, 10));
        }
        btnBitacora.addActionListener(e ->
                new BitacoraFrame(usuario).setVisible(true)
        );

        JButton btnVerCatalogo = crearBoton("Visualizar Películas", new Color(41, 128, 185));
        btnVerCatalogo.setBounds(140, 250, 220, 40);
        ImageIcon iconCatalogo = crearIcono("/imagen/verPeli.png", 22, 22);
        if (iconCatalogo != null) {
            btnVerCatalogo.setIcon(iconCatalogo);
            btnVerCatalogo.setHorizontalAlignment(SwingConstants.LEFT);
            btnVerCatalogo.setIconTextGap(10);
            btnVerCatalogo.setMargin(new Insets(0, 15, 0, 10));
        }
        btnVerCatalogo.addActionListener(e -> {
            new PeliculaViewFrame(this).setVisible(true);
            this.setVisible(false);
        });

        JButton btnSalir = crearBoton("Cerrar Sesión", new Color(231, 76, 60));
        btnSalir.setBounds(140, 450, 220, 40);
        ImageIcon iconSalir = crearIcono("/imagen/cerrarSesion.png", 22, 22);
        if (iconSalir != null) {
            btnSalir.setIcon(iconSalir);
            btnSalir.setHorizontalAlignment(SwingConstants.LEFT);
            btnSalir.setIconTextGap(10);
            btnSalir.setMargin(new Insets(0, 15, 0, 10));
        }
        btnSalir.addActionListener(e -> {
            bitacoraDAO.registrarAccion(usuario.getUsername(), "Cierre de sesión");
            new LoginFrame().setVisible(true);
            dispose();
        });

        fondo.add(btnSalir);

        boolean esAdmin  = UtilEntity.tieneRol(roles, "ADMIN");
        boolean esEditor = UtilEntity.tieneRol(roles, "EDITOR");
        boolean esUser   = UtilEntity.tieneRol(roles, "USER");

        if (esAdmin) {
            fondo.add(btnCategorias);
            fondo.add(btnDirectores);
            fondo.add(btnPeliculas);
            fondo.add(btnUsuarios);
            fondo.add(btnBitacora);
        }
        else if (esEditor) {
            fondo.add(btnCategorias);
            fondo.add(btnDirectores);
            fondo.add(btnPeliculas);
        }
        else if (esUser) {
            fondo.add(btnVerCatalogo);
        }
    }

    private JButton crearBoton(String texto, Color colorFondo) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setForeground(Color.WHITE);
        boton.setBackground(colorFondo);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

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
            if (imgURL == null) return null;

            ImageIcon original = new ImageIcon(imgURL);
            Image img = original.getImage()
                    .getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(img);

        } catch (Exception e) {
            return null;
        }
    }

}
