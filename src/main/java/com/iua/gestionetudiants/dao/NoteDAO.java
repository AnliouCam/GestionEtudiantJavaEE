package com.iua.gestionetudiants.dao;

import com.iua.gestionetudiants.model.Note;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.util.List;

/**
 * DAO (Data Access Object) pour la gestion des notes
 * Couche Persistance - Accès à la base de données via JPA
 */
public class NoteDAO {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("GestionEtudiantsPU");

    /**
     * Créer une nouvelle note dans la base de données
     */
    public void creer(Note note) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(note);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Trouver une note par son ID
     */
    public Note trouverParId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Note.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Lister toutes les notes d'un étudiant
     */
    public List<Note> listerParEtudiant(Long etudiantId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Note> query = em.createQuery(
                "SELECT n FROM Note n WHERE n.etudiant.id = :etudiantId ORDER BY n.matiere",
                Note.class);
            query.setParameter("etudiantId", etudiantId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Lister toutes les notes
     */
    public List<Note> listerToutes() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Note> query = em.createQuery(
                "SELECT n FROM Note n ORDER BY n.etudiant.nom, n.matiere", Note.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Mettre à jour une note
     */
    public void modifier(Note note) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(note);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Supprimer une note
     */
    public void supprimer(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Note note = em.find(Note.class, id);
            if (note != null) {
                em.remove(note);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Compter le nombre de notes d'un étudiant
     */
    public long compterParEtudiant(Long etudiantId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(n) FROM Note n WHERE n.etudiant.id = :etudiantId", Long.class);
            query.setParameter("etudiantId", etudiantId);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    /**
     * Fermer l'EntityManagerFactory
     */
    public static void fermer() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
