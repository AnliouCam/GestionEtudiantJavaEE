package com.iua.gestionetudiants.service;

import com.iua.gestionetudiants.dao.EtudiantDAO;
import com.iua.gestionetudiants.dao.NoteDAO;
import com.iua.gestionetudiants.model.Etudiant;
import com.iua.gestionetudiants.model.Note;

import java.util.List;

/**
 * ===========================================================================
 * SERVICE METIER : GESTION DES NOTES
 * ===========================================================================
 * Cette classe contient toute la LOGIQUE METIER liee aux notes :
 * - Validation des donnees (note entre 0 et 20)
 * - CALCUL DE LA MOYENNE PONDEREE (fonction la plus importante !)
 * - Regles de gestion
 *
 * ROLE : Couche Metier (Couche 3 de l'architecture 4 couches)
 * UTILISE PAR : NoteServlet (Couche 2 - Controleur)
 * UTILISE : NoteDAO et EtudiantDAO (Couche 4 - Persistance)
 * ===========================================================================
 */
public class NoteService {

    // DAO pour acceder aux notes et etudiants en base de donnees
    private NoteDAO noteDAO = new NoteDAO();
    private EtudiantDAO etudiantDAO = new EtudiantDAO();

    /**
     * Créer une nouvelle note avec validation
     */
    public void creerNote(Note note) throws Exception {
        // Validation
        validerNote(note);

        // Créer la note
        noteDAO.creer(note);
    }

    /**
     * Trouver une note par ID
     */
    public Note trouverParId(Long id) {
        return noteDAO.trouverParId(id);
    }

    /**
     * Lister toutes les notes d'un étudiant
     */
    public List<Note> listerParEtudiant(Long etudiantId) {
        return noteDAO.listerParEtudiant(etudiantId);
    }

    /**
     * Lister toutes les notes
     */
    public List<Note> listerToutes() {
        return noteDAO.listerToutes();
    }

    /**
     * Modifier une note avec validation
     */
    public void modifierNote(Note note) throws Exception {
        // Validation
        validerNote(note);

        // Vérifier que la note existe
        Note existante = noteDAO.trouverParId(note.getId());
        if (existante == null) {
            throw new Exception("La note avec l'ID " + note.getId() + " n'existe pas");
        }

        // Modifier la note
        noteDAO.modifier(note);
    }

    /**
     * Supprimer une note
     */
    public void supprimerNote(Long id) throws Exception {
        // Vérifier que la note existe
        Note note = noteDAO.trouverParId(id);
        if (note == null) {
            throw new Exception("La note avec l'ID " + id + " n'existe pas");
        }

        // Supprimer la note
        noteDAO.supprimer(id);
    }

    /**
     * ========================================================================
     * CALCUL DE LA MOYENNE PONDEREE (FONCTION LA PLUS IMPORTANTE !)
     * ========================================================================
     *
     * Cette methode calcule la moyenne ponderee d'un etudiant
     *
     * FORMULE : Moyenne = Σ(note × coefficient) / Σ(coefficient)
     *
     * EXEMPLE CONCRET :
     * Etudiant : Jean KOUASSI
     * Notes :
     *   - Java EE : 16.5/20 (coefficient 3) → 16.5 × 3 = 49.5 points
     *   - BDD : 14/20 (coefficient 2) → 14 × 2 = 28 points
     *   - Anglais : 12.5/20 (coefficient 1) → 12.5 × 1 = 12.5 points
     *
     * Total points = 49.5 + 28 + 12.5 = 90 points
     * Total coefficients = 3 + 2 + 1 = 6
     * Moyenne = 90 / 6 = 15/20
     *
     * @param etudiantId ID de l'etudiant
     * @return La moyenne ponderee arrondie a 2 decimales
     * ========================================================================
     */
    public double calculerMoyenne(Long etudiantId) {
        // 1. Recuperer toutes les notes de l'etudiant depuis la BDD
        List<Note> notes = noteDAO.listerParEtudiant(etudiantId);

        // 2. Si l'etudiant n'a pas de notes, retourner 0
        if (notes == null || notes.isEmpty()) {
            return 0.0;
        }

        // 3. Variables pour le calcul
        double sommeNotesPonderees = 0.0;  // Numerateur de la formule
        int sommeCoefficients = 0;         // Denominateur de la formule

        // 4. Boucle sur toutes les notes pour calculer la somme ponderee
        for (Note note : notes) {
            // Multiplier chaque note par son coefficient
            sommeNotesPonderees += note.getValeur() * note.getCoefficient();

            // Additionner les coefficients
            sommeCoefficients += note.getCoefficient();
        }

        // 5. Eviter la division par zero
        if (sommeCoefficients == 0) {
            return 0.0;
        }

        // 6. Calculer la moyenne : diviser la somme ponderee par les coefficients
        double moyenne = sommeNotesPonderees / sommeCoefficients;

        // 7. Arrondir a 2 decimales (ex: 15.66666 → 15.67)
        return Math.round(moyenne * 100.0) / 100.0;
    }

    /**
     * SURCHARGE : Calculer la moyenne a partir d'une liste de notes deja chargee
     * Meme logique que ci-dessus, mais prend une liste en parametre
     * au lieu de l'ID de l'etudiant
     *
     * Utilise par : detail-etudiant.jsp (evite de recharger les notes)
     *
     * @param notes Liste des notes
     * @return La moyenne ponderee
     */
    public double calculerMoyenne(List<Note> notes) {
        if (notes == null || notes.isEmpty()) {
            return 0.0;
        }

        double sommeNotesPonderees = 0.0;
        int sommeCoefficients = 0;

        for (Note note : notes) {
            sommeNotesPonderees += note.getValeur() * note.getCoefficient();
            sommeCoefficients += note.getCoefficient();
        }

        if (sommeCoefficients == 0) {
            return 0.0;
        }

        double moyenne = sommeNotesPonderees / sommeCoefficients;
        return Math.round(moyenne * 100.0) / 100.0;
    }

    /**
     * ========================================================================
     * VALIDATION DES DONNEES D'UNE NOTE
     * ========================================================================
     * Verifie que toutes les regles de gestion sont respectees
     * LANCE UNE EXCEPTION si une regle est violee
     *
     * REGLES DE GESTION :
     * 1. La matiere est obligatoire
     * 2. La note doit etre entre 0 et 20 (systeme ivoirien)
     * 3. Le coefficient doit etre > 0
     * 4. L'etudiant doit exister en base de donnees
     *
     * @param note La note a valider
     * @throws Exception Si une regle de validation echoue
     * ========================================================================
     */
    private void validerNote(Note note) throws Exception {
        // Verification 1 : La note ne doit pas etre null
        if (note == null) {
            throw new Exception("La note ne peut pas etre null");
        }

        // Verification 2 : La matiere est obligatoire
        if (note.getMatiere() == null || note.getMatiere().trim().isEmpty()) {
            throw new Exception("La matiere est obligatoire");
        }

        // Verification 3 : La valeur est obligatoire
        if (note.getValeur() == null) {
            throw new Exception("La valeur de la note est obligatoire");
        }

        // Verification 4 : La note doit etre entre 0 et 20
        // IMPORTANT : Systeme de notation ivoirien (base 20)
        if (note.getValeur() < 0 || note.getValeur() > 20) {
            throw new Exception("La note doit etre comprise entre 0 et 20");
        }

        // Verification 5 : Le coefficient est obligatoire
        if (note.getCoefficient() == null) {
            throw new Exception("Le coefficient est obligatoire");
        }

        // Verification 6 : Le coefficient doit etre positif
        // Un coefficient de 0 n'a pas de sens dans le calcul de moyenne
        if (note.getCoefficient() <= 0) {
            throw new Exception("Le coefficient doit etre superieur a 0");
        }

        // Verification 7 : L'etudiant est obligatoire
        if (note.getEtudiant() == null) {
            throw new Exception("L'etudiant est obligatoire");
        }

        // Verification 8 : L'etudiant doit exister en BDD
        // Evite d'ajouter une note a un etudiant inexistant
        Etudiant etudiant = etudiantDAO.trouverParId(note.getEtudiant().getId());
        if (etudiant == null) {
            throw new Exception("L'etudiant avec l'ID " + note.getEtudiant().getId() + " n'existe pas");
        }
    }

    /**
     * Compter le nombre de notes d'un étudiant
     */
    public long compterNotesParEtudiant(Long etudiantId) {
        return noteDAO.compterParEtudiant(etudiantId);
    }
}
