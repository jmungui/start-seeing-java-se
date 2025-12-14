package org.example.startseeing.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import org.example.startseeing.entity.Directores;
import org.example.startseeing.util.UtilEntity;

import java.util.List;

public class DirectoresDAO {
    private final BitacoraDAO bitacoraDAO = new BitacoraDAO();

    public void guardar(Directores director, String usuario) {


        EntityManager em = UtilEntity.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(director);
            em.getTransaction().commit();

            // Registrar acción en la Bitácora
            bitacoraDAO.registrarAccion(usuario, "Registró un nuevo director: " + director.getNombre());
        } catch (Exception e) {
            em.getTransaction().rollback(); // Revierte si hay error
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void actualizar(Directores director, String usuario) {
        EntityManager em = UtilEntity.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(director);
            em.getTransaction().commit();

            // Registrar acción en la Bitácora
            bitacoraDAO.registrarAccion(usuario, "Actualizo un director: " + director.getNombre());
        } catch (Exception e) {
            em.getTransaction().rollback(); // Revierte si hay error
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void eliminar(int id, String usuario) {
        EntityManager em = UtilEntity.getEntityManager();
        try {
            Directores director = em.find(Directores.class, id);
            if (director != null) {
                em.getTransaction().begin();
                em.remove(director);
                em.getTransaction().commit();

                // Registrar acción en la Bitácora
                bitacoraDAO.registrarAccion(usuario, "Elimino un director: " + director.getNombre());
            }
        } catch (Exception e) {
            em.getTransaction().rollback(); // Revierte si hay error
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public Directores buscarPorId(int id) {
        EntityManager em = UtilEntity.getEntityManager();
        Directores directores = null;
        try {
            directores = em.find(Directores.class, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return directores;
    }

    public List<Directores> listar() {
        EntityManager em = UtilEntity.getEntityManager();
        List<Directores> resultado = null;
        try {
            TypedQuery<Directores> query = em.createQuery("SELECT d FROM Directores d", Directores.class);
            return query.getResultList();
        } catch (PersistenceException e) {
            System.err.println("Error al listar Director: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return resultado;
    }

}
