package com.iua.gestionetudiants.dao;

import com.iua.gestionetudiants.model.Etudiant;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.util.List;

/**
 * DAO (Data Access Object) pour la gestion des étudiants
 * Couche Persistance - Accès à la base de données via JPA
 */
public class EtudiantDAO {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("GestionEtudiantsPU");

    /**
     * Créer un nouvel étudiant dans la base de données
     */
    public void creer(Etudiant etudiant) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(etudiant);
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
     * Trouver un étudiant par son ID
     */
    public Etudiant trouverParId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Etudiant.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Trouver un étudiant par son matricule
     */
    public Etudiant trouverParMatricule(String matricule) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Etudiant> query = em.createQuery(
                "SELECT e FROM Etudiant e WHERE e.matricule = :matricule", Etudiant.class);
            query.setParameter("matricule", matricule);
            List<Etudiant> resultats = query.getResultList();
            return resultats.isEmpty() ? null : resultats.get(0);
        } finally {
            em.close();
        }
    }

    /**
     * Lister tous les étudiants
     */
    public List<Etudiant> listerTous() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Etudiant> query = em.createQuery(
                "SELECT e FROM Etudiant e ORDER BY e.nom, e.prenom", Etudiant.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Lister tous les étudiants avec leurs notes (évite le problème LazyInitialization)
     */
    public List<Etudiant> listerTousAvecNotes() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Etudiant> query = em.createQuery(
                "SELECT DISTINCT e FROM Etudiant e LEFT JOIN FETCH e.notes ORDER BY e.nom, e.prenom",
                Etudiant.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Trouver un étudiant avec ses notes par ID
     */
    public Etudiant trouverParIdAvecNotes(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Etudiant> query = em.createQuery(
                "SELECT e FROM Etudiant e LEFT JOIN FETCH e.notes WHERE e.id = :id",
                Etudiant.class);
            query.setParameter("id", id);
            List<Etudiant> resultats = query.getResultList();
            return resultats.isEmpty() ? null : resultats.get(0);
        } finally {
            em.close();
        }
    }

    /**
     * Mettre à jour un étudiant
     */
    public void modifier(Etudiant etudiant) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(etudiant);
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
     * Supprimer un étudiant
     */
    public void supprimer(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Etudiant etudiant = em.find(Etudiant.class, id);
            if (etudiant != null) {
                em.remove(etudiant);
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
     * Compter le nombre total d'étudiants
     */
    public long compter() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(e) FROM Etudiant e", Long.class);
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
