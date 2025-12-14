package org.example.startseeing.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.startseeing.entity.Rol;
import org.example.startseeing.util.UtilEntity;

import java.util.List;

public class RolDAO {

    public void guardar(Rol rol) {
        EntityManager em = UtilEntity.getEntityManager();
        em.getTransaction().begin();
        em.persist(rol);
        em.getTransaction().commit();
        em.close();
    }

    public void actualizar(Rol rol) {
        EntityManager em = UtilEntity.getEntityManager();
        em.getTransaction().begin();
        em.merge(rol);
        em.getTransaction().commit();
        em.close();
    }

    public void eliminar(int id) {
        EntityManager em = UtilEntity.getEntityManager();
        Rol rol = em.find(Rol.class, id);
        if (rol != null) {
            em.getTransaction().begin();
            em.remove(rol);
            em.getTransaction().commit();
        }
        em.close();
    }

    public Rol buscarPorId(int id) {
        EntityManager em = UtilEntity.getEntityManager();
        Rol rol = em.find(Rol.class, id);
        em.close();
        return rol;
    }

    public List<Rol> listar() {
        EntityManager em = UtilEntity.getEntityManager();
        List<Rol> lista = em.createQuery("SELECT r FROM Rol r", Rol.class).getResultList();
        em.close();
        return lista;
    }

}
