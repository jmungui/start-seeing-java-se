package org.example.startseeing;

import org.example.startseeing.dao.CategoriaDAO;
import org.example.startseeing.dao.DirectoresDAO;
import org.example.startseeing.dao.PeliculaDAO;
import org.example.startseeing.entity.Categoria;
import org.example.startseeing.entity.Directores;
import org.example.startseeing.entity.Pelicula;
import org.example.startseeing.view.MenuPrincipal;

import javax.swing.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

       SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
    }
}