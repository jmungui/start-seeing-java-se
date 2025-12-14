package org.example.startseeing.service;

import org.example.startseeing.dao.BitacoraDAO;

public class BitacoraService {
    private static final BitacoraDAO bitacoraDAO = new BitacoraDAO();

    public static void registrar(String usuario, String accion) {
        if (usuario != null && !usuario.isEmpty() && accion != null && !accion.isEmpty()) {
            bitacoraDAO.registrarAccion(usuario, accion);
        }
    }
}
