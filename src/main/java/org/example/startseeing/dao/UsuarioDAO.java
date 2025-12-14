package org.example.startseeing.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.example.startseeing.entity.Usuario;
import org.example.startseeing.service.BitacoraService;
import org.example.startseeing.util.UtilEntity;

import java.util.List;

public class UsuarioDAO {

    private String usuarioActual; // Nuevo: guarda el usuario que ejecuta las acciones

    public UsuarioDAO() {}

    public UsuarioDAO(String usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    public void guardar(Usuario usuario) {
        EntityManager em = UtilEntity.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(usuario);
            em.getTransaction().commit();

            BitacoraService.registrar(usuarioActual, "Creó un nuevo usuario: " + usuario.getUsername());
        } finally {
            em.close();
        }
    }

    public void actualizar(Usuario usuario) {
        EntityManager em = UtilEntity.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(usuario);
            em.getTransaction().commit();

            BitacoraService.registrar(usuarioActual, "Actualizó el usuario: " + usuario.getUsername());
        } finally {
            em.close();
        }
    }

    public void eliminar(int id) {
        EntityManager em = UtilEntity.getEntityManager();
        try {
            Usuario usuario = em.find(Usuario.class, id);
            if (usuario != null) {
                em.getTransaction().begin();
                em.remove(usuario);
                em.getTransaction().commit();

                BitacoraService.registrar(usuarioActual, "Eliminó al usuario: " + usuario.getUsername());
            }
        } finally {
            em.close();
        }
    }

    public Usuario buscarPorId(int id) {
        EntityManager em = UtilEntity.getEntityManager();
        Usuario usuario = em.find(Usuario.class, id);
        em.close();
        return usuario;
    }

    public List<Usuario> listar() {
        EntityManager em = UtilEntity.getEntityManager();
        List<Usuario> lista = em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
        em.close();
        return lista;
    }

    public Usuario buscarPorUsername(String username) {
        EntityManager em = UtilEntity.getEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery(
                    "SELECT u FROM Usuario u WHERE u.username = :username", Usuario.class);
            query.setParameter("username", username);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public Usuario autenticar(String username, String password) {
        EntityManager em = UtilEntity.getEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery(
                    "SELECT u FROM Usuario u WHERE u.username = :username AND u.password = :password",
                    Usuario.class
            );
            query.setParameter("username", username);
            query.setParameter("password", password);

            Usuario usuario = query.getSingleResult();
            BitacoraService.registrar(username, "Inicio de sesión exitoso");
            return usuario;
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }


}
