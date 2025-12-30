package com.iua.gestionetudiants.service;

import com.iua.gestionetudiants.dao.EtudiantDAO;
import com.iua.gestionetudiants.model.Etudiant;

import java.util.List;

/**
 * Service métier pour la gestion des étudiants
 * Couche Métier - Contient la logique métier et les règles de gestion
 */
public class EtudiantService {

    private EtudiantDAO etudiantDAO = new EtudiantDAO();

    /**
     * Créer un nouvel étudiant avec validation
     */
    public void creerEtudiant(Etudiant etudiant) throws Exception {
        // Validation
        validerEtudiant(etudiant);

        // Vérifier que le matricule n'existe pas déjà
        Etudiant existant = etudiantDAO.trouverParMatricule(etudiant.getMatricule());
        if (existant != null) {
            throw new Exception("Un étudiant avec le matricule " + etudiant.getMatricule() + " existe déjà");
        }

        // Créer l'étudiant
        etudiantDAO.creer(etudiant);
    }

    /**
     * Trouver un étudiant par ID
     */
    public Etudiant trouverParId(Long id) {
        return etudiantDAO.trouverParId(id);
    }

    /**
     * Trouver un étudiant avec ses notes
     */
    public Etudiant trouverParIdAvecNotes(Long id) {
        return etudiantDAO.trouverParIdAvecNotes(id);
    }

    /**
     * Lister tous les étudiants
     */
    public List<Etudiant> listerTous() {
        return etudiantDAO.listerTous();
    }

    /**
     * Lister tous les étudiants avec leurs notes
     */
    public List<Etudiant> listerTousAvecNotes() {
        return etudiantDAO.listerTousAvecNotes();
    }

    /**
     * Modifier un étudiant avec validation
     */
    public void modifierEtudiant(Etudiant etudiant) throws Exception {
        // Validation
        validerEtudiant(etudiant);

        // Vérifier que l'étudiant existe
        Etudiant existant = etudiantDAO.trouverParId(etudiant.getId());
        if (existant == null) {
            throw new Exception("L'étudiant avec l'ID " + etudiant.getId() + " n'existe pas");
        }

        // Vérifier que le matricule n'est pas déjà utilisé par un autre étudiant
        Etudiant autreEtudiant = etudiantDAO.trouverParMatricule(etudiant.getMatricule());
        if (autreEtudiant != null && !autreEtudiant.getId().equals(etudiant.getId())) {
            throw new Exception("Le matricule " + etudiant.getMatricule() + " est déjà utilisé par un autre étudiant");
        }

        // Modifier l'étudiant
        etudiantDAO.modifier(etudiant);
    }

    /**
     * Supprimer un étudiant
     */
    public void supprimerEtudiant(Long id) throws Exception {
        // Vérifier que l'étudiant existe
        Etudiant etudiant = etudiantDAO.trouverParId(id);
        if (etudiant == null) {
            throw new Exception("L'étudiant avec l'ID " + id + " n'existe pas");
        }

        // Supprimer l'étudiant (les notes seront supprimées en cascade)
        etudiantDAO.supprimer(id);
    }

    /**
     * Valider les données d'un étudiant
     */
    private void validerEtudiant(Etudiant etudiant) throws Exception {
        if (etudiant == null) {
            throw new Exception("L'étudiant ne peut pas être null");
        }

        if (etudiant.getMatricule() == null || etudiant.getMatricule().trim().isEmpty()) {
            throw new Exception("Le matricule est obligatoire");
        }

        if (etudiant.getNom() == null || etudiant.getNom().trim().isEmpty()) {
            throw new Exception("Le nom est obligatoire");
        }

        if (etudiant.getPrenom() == null || etudiant.getPrenom().trim().isEmpty()) {
            throw new Exception("Le prénom est obligatoire");
        }

        if (etudiant.getEmail() != null && !etudiant.getEmail().trim().isEmpty()) {
            if (!etudiant.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new Exception("L'email n'est pas valide");
            }
        }
    }

    /**
     * Compter le nombre total d'étudiants
     */
    public long compterEtudiants() {
        return etudiantDAO.compter();
    }
}
