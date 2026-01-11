package org.example.startseeing.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import org.example.startseeing.entity.Bitacora;
import org.example.startseeing.util.UtilEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
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


    public List<Bitacora> buscarPorUsuario(String usuario) {
        EntityManager em = UtilEntity.getEntityManager();
        List<Bitacora> lista = em
                .createQuery(
                        "SELECT b FROM Bitacora b " +
                                "WHERE LOWER(b.usuario) LIKE LOWER(:usuario) " +
                                "ORDER BY b.fecha DESC",
                        Bitacora.class
                )
                .setParameter("usuario", "%" + usuario + "%")
                .getResultList();
        em.close();
        return lista;
    }

    public List<Bitacora> buscarPorUsuarioYFechas(
            String usuario,
            LocalDate desde,
            LocalDate hasta
    ) {
        EntityManager em = UtilEntity.getEntityManager();

        String sql =
                "SELECT * FROM bitacora " +
                        "WHERE usuario = :usuario " +
                        "AND DATE(fecha) BETWEEN :desde AND :hasta " +
                        "ORDER BY fecha DESC";

        Query q = em.createNativeQuery(sql, Bitacora.class);
        q.setParameter("usuario", usuario);
        q.setParameter("desde", desde);
        q.setParameter("hasta", hasta);

        List<Bitacora> lista = q.getResultList();
        em.close();
        return lista;
    }

    public List<Bitacora> buscarPorFechas(LocalDate desde, LocalDate hasta) {
        EntityManager em = UtilEntity.getEntityManager();

        String sql =
                "SELECT * FROM bitacora " +
                        "WHERE DATE(fecha) BETWEEN :desde AND :hasta " +
                        "ORDER BY fecha DESC";

        Query q = em.createNativeQuery(sql, Bitacora.class);
        q.setParameter("desde", desde);
        q.setParameter("hasta", hasta);

        List<Bitacora> lista = q.getResultList();
        em.close();
        return lista;
    }

}
