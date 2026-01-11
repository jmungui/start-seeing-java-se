package org.example.startseeing.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.startseeing.entity.Rol;

import java.util.Set;

public class UtilEntity {

    private static final EntityManagerFactory entityManagerFactory = buildEntityManagerFactory();

    private static EntityManagerFactory buildEntityManagerFactory(){
        return Persistence.createEntityManagerFactory("myPersistenceUnit");
    }

    public static EntityManager getEntityManager(){
        return entityManagerFactory.createEntityManager();
    }

    public static boolean tieneRol(Set<Rol> roles, String rol) {
        return roles.stream()
                .anyMatch(r -> r.getNombre().equalsIgnoreCase(rol));
    }

}
