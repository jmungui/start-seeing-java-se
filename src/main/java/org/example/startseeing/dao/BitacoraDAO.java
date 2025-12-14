package org.example.startseeing.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.example.startseeing.entity.Bitacora;
import org.example.startseeing.util.UtilEntity;

import java.util.List;

public class BitacoraDAO {

    public void registrarAccion(String usuario, String accion) {
        EntityManager em = UtilEntity.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Bitacora b = new Bitacora(usuario, accion);
            em.persist(b);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<Bitacora> listar() {
        EntityManager em = UtilEntity.getEntityManager();
        List<Bitacora> lista = em.createQuery("SELECT b FROM Bitacora b ORDER BY b.fecha DESC", Bitacora.class)
                .getResultList();
        em.close();
        return lista;
    }

}
