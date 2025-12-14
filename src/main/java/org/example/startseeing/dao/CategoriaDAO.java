package org.example.startseeing.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import org.example.startseeing.entity.Categoria;
import org.example.startseeing.util.UtilEntity;

import java.util.List;

public class CategoriaDAO {

    private final BitacoraDAO bitacoraDAO = new BitacoraDAO();

    public void guardar(Categoria categoria, String usuario) {
        EntityManager em = UtilEntity.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(categoria);
            em.getTransaction().commit();

            // Registrar acción en la Bitácora
            bitacoraDAO.registrarAccion(usuario, "Registró una nueva categoría: " + categoria.getNombreCategoria());

        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void actualizar(Categoria categoria, String usuario) {
        EntityManager em = UtilEntity.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(categoria);
            em.getTransaction().commit();

            // 🔹 Registrar acción en Bitácora
            bitacoraDAO.registrarAccion(usuario, "Actualizó la categoría: " + categoria.getNombreCategoria());

        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void eliminar(int id, String usuario) {
        EntityManager em = UtilEntity.getEntityManager();
        try {
            Categoria categoria = em.find(Categoria.class, id);
            if (categoria != null) {
                em.getTransaction().begin();
                em.remove(categoria);
                em.getTransaction().commit();

                // Registrar acción en la Bitácora
                bitacoraDAO.registrarAccion(usuario, "Eliminó la categoría: " + categoria.getNombreCategoria());
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public Categoria buscarPorId(int id) {
        EntityManager em = UtilEntity.getEntityManager();
        Categoria categoria = null;
        try {
            categoria = em.find(Categoria.class, id);
        } catch (PersistenceException e) {
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return categoria;
    }

    public List<Categoria> listar() {
        EntityManager em = UtilEntity.getEntityManager();
        try {
            TypedQuery<Categoria> query = em.createQuery("SELECT c FROM Categoria c", Categoria.class);
            return query.getResultList();
        } catch (PersistenceException e) {
            System.err.println("Error al listar categorías: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

}
