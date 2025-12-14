package org.example.startseeing.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import org.example.startseeing.entity.Pelicula;
import org.example.startseeing.util.UtilEntity;

import java.util.List;

public class PeliculaDAO {
    private final BitacoraDAO bitacoraDAO = new BitacoraDAO();

    public void guardar(Pelicula pelicula, String usuario) {
        EntityManager em = UtilEntity.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(pelicula);
            em.getTransaction().commit();

            // Registrar acción en la Bitácora
            bitacoraDAO.registrarAccion(usuario, "Registró una nueva pelicula: " + pelicula.getNombre());
        }catch (Exception e) {
            em.getTransaction().rollback(); // Revierte si hay error
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void actualizar(Pelicula pelicula, String usuario) {
        EntityManager em = UtilEntity.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(pelicula);
            em.getTransaction().commit();

            // Registrar acción en la Bitácora
            bitacoraDAO.registrarAccion(usuario, "Actualizo la pelicula: " + pelicula.getNombre());
        }catch (Exception e) {
            em.getTransaction().rollback(); // Revierte si hay error
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void eliminar(int id, String usuario) {
        EntityManager em = UtilEntity.getEntityManager();
        try {
            Pelicula pelicula = em.find(Pelicula.class, id);
            if (pelicula != null) {
                em.getTransaction().begin();
                em.remove(pelicula);
                em.getTransaction().commit();

                // Registrar acción en la Bitácora
                bitacoraDAO.registrarAccion(usuario, "Elimino la pelicula: " + pelicula.getNombre());
            }
        }catch (Exception e) {
            em.getTransaction().rollback(); // Revierte si hay error
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public Pelicula buscarPorId(int id) {
        EntityManager em = UtilEntity.getEntityManager();
        Pelicula pelicula = null;
        try {
            pelicula = em.find(Pelicula.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return pelicula;
    }

    public List<Pelicula> listar() {
        EntityManager em = UtilEntity.getEntityManager();
        List<Pelicula> resultado = null;
        try {
            TypedQuery<Pelicula> query = em.createQuery("SELECT p FROM Pelicula p", Pelicula.class);
            return query.getResultList();
        } catch (PersistenceException e) {
            System.err.println("Error al listar Peliculas: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return resultado;
    }



}
