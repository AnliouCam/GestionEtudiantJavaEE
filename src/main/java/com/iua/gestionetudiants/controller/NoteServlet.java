package com.iua.gestionetudiants.controller;

import com.iua.gestionetudiants.model.Etudiant;
import com.iua.gestionetudiants.model.Note;
import com.iua.gestionetudiants.service.EtudiantService;
import com.iua.gestionetudiants.service.NoteService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * ===========================================================================
 * SERVLET CONTROLEUR : GESTION DES NOTES
 * ===========================================================================
 * Ce Servlet gere toutes les operations liees aux notes des etudiants
 *
 * ROLE : Couche Controleur (Couche 2 de l'architecture 4 couches)
 * RESPONSABILITES :
 * 1. Recevoir les requetes HTTP pour la gestion des notes
 * 2. Afficher les formulaires d'ajout de notes
 * 3. Creer de nouvelles notes pour un etudiant
 * 4. Supprimer des notes
 * 5. Valider et transmettre les donnees a la couche Service
 *
 * DIFFERENCE AVEC EtudiantServlet :
 * - EtudiantServlet : Gere les etudiants (creation, modification, liste)
 * - NoteServlet : Gere les notes (ajout, suppression)
 *
 * COLLABORATION :
 * - Utilise NoteService pour la logique metier des notes
 * - Utilise EtudiantService pour verifier l'existence des etudiants
 *
 * EXEMPLE DE FLUX :
 * 1. Utilisateur consulte les details d'un etudiant
 * 2. Il clique sur "Ajouter une note"
 * 3. Navigateur envoie GET /notes?action=ajouter&etudiantId=1
 * 4. Ce Servlet affiche le formulaire d'ajout
 * 5. Utilisateur remplit : matiere="Java EE", valeur=16.5, coefficient=3
 * 6. Navigateur envoie POST /notes?action=creer
 * 7. Ce Servlet cree la note via NoteService
 * 8. La note est validee (valeur entre 0-20, coefficient > 0)
 * 9. La note est enregistree en BDD
 * 10. L'utilisateur est redirige vers la page de l'etudiant
 * 11. La moyenne est recalculee automatiquement
 * ===========================================================================
 */

/**
 * @WebServlet("/notes") : Mappe ce Servlet a l'URL /notes
 *
 * EXEMPLES D'URLS GEREES :
 * - /notes?action=ajouter&etudiantId=1  → Affiche formulaire d'ajout de note
 * - /notes?action=creer                 → Cree une nouvelle note (POST)
 * - /notes?action=supprimer&id=5        → Supprime la note avec ID 5
 */
@WebServlet("/notes")
public class NoteServlet extends HttpServlet {

    // ===================================================================
    // SERVICES (Couche 3 - Logique Metier)
    // ===================================================================

    /**
     * Service pour gerer la logique metier des notes
     * (validation : note entre 0-20, coefficient > 0, calcul de moyenne)
     */
    private NoteService noteService = new NoteService();

    /**
     * Service pour gerer les etudiants
     * (necessaire pour verifier que l'etudiant existe avant d'ajouter une note)
     */
    private EtudiantService etudiantService = new EtudiantService();

    // ===================================================================
    // METHODE doGet() - GERER LES REQUETES HTTP GET
    // ===================================================================
    /**
     * Gere les requetes GET pour les notes
     *
     * QUAND EST-ELLE APPELEE ?
     * - Quand l'utilisateur clique sur "Ajouter une note"
     * - Quand l'utilisateur clique sur "Supprimer" pour une note
     *
     * ACTIONS GEREES :
     * - action=ajouter   → Afficher le formulaire d'ajout de note
     * - action=supprimer → Supprimer une note
     * - Autre            → Rediriger vers la liste des etudiants
     *
     * @param request  Contient les parametres (action, etudiantId, id)
     * @param response Utilise pour afficher le formulaire ou rediriger
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. EXTRAIRE L'ACTION DE L'URL
        String action = request.getParameter("action");

        try {
            // 2. ROUTER SELON L'ACTION
            if ("ajouter".equals(action)) {
                // Afficher le formulaire pour ajouter une note a un etudiant
                afficherFormulaireAjout(request, response);
            } else if ("supprimer".equals(action)) {
                // Supprimer une note existante
                supprimerNote(request, response);
            } else {
                // Action inconnue ou absente → Rediriger vers la liste des etudiants
                response.sendRedirect(request.getContextPath() + "/etudiants");
            }
        } catch (Exception e) {
            // 3. GESTION DES ERREURS
            request.setAttribute("erreur", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/erreur.jsp").forward(request, response);
        }
    }

    // ===================================================================
    // METHODE doPost() - GERER LES REQUETES HTTP POST
    // ===================================================================
    /**
     * Gere les requetes POST pour les notes
     *
     * QUAND EST-ELLE APPELEE ?
     * - Quand l'utilisateur soumet le formulaire d'ajout de note
     *
     * ACTIONS GEREES :
     * - action=creer → Creer une nouvelle note pour un etudiant
     *
     * @param request  Contient les donnees du formulaire
     *                 (etudiantId, matiere, valeur, coefficient)
     * @param response Utilise pour rediriger apres creation
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. EXTRAIRE L'ACTION DU FORMULAIRE
        String action = request.getParameter("action");

        try {
            // 2. ROUTER SELON L'ACTION
            if ("creer".equals(action)) {
                // L'utilisateur a soumis le formulaire d'ajout de note
                creerNote(request, response);
            } else {
                // Action inconnue → Rediriger vers la liste des etudiants
                response.sendRedirect(request.getContextPath() + "/etudiants");
            }
        } catch (Exception e) {
            // 3. GESTION DES ERREURS
            request.setAttribute("erreur", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/erreur.jsp").forward(request, response);
        }
    }

    // ===================================================================
    // METHODES PRIVEES - ACTIONS SPECIFIQUES
    // ===================================================================

    /**
     * ========================================================================
     * ACTION : AFFICHER LE FORMULAIRE D'AJOUT DE NOTE
     * ========================================================================
     * Affiche un formulaire pour ajouter une note a un etudiant specifique
     *
     * EXEMPLE D'URL : /notes?action=ajouter&etudiantId=1
     *
     * FLUX DE TRAITEMENT :
     * 1. Recuperer l'ID de l'etudiant depuis l'URL
     * 2. Charger l'etudiant depuis la BDD
     * 3. Verifier que l'etudiant existe
     * 4. Passer l'etudiant a la JSP
     * 5. La JSP affiche le formulaire avec le nom de l'etudiant
     *
     * @param request  Pour recuperer etudiantId et stocker l'etudiant
     * @param response Pour rediriger si l'etudiant n'existe pas
     */
    private void afficherFormulaireAjout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ETAPE 1 : Recuperer l'ID de l'etudiant
        // Exemple : /notes?action=ajouter&etudiantId=3 → etudiantId = 3
        Long etudiantId = Long.parseLong(request.getParameter("etudiantId"));

        // ETAPE 2 : Charger l'etudiant depuis la BDD
        Etudiant etudiant = etudiantService.trouverParId(etudiantId);

        // ETAPE 3 : Verification - L'etudiant existe-t-il ?
        if (etudiant == null) {
            // L'etudiant n'existe pas → Rediriger vers la liste
            response.sendRedirect(request.getContextPath() + "/etudiants");
            return;
        }

        // ETAPE 4 : Passer l'etudiant a la JSP
        // La JSP va afficher : "Ajouter une note pour Jean KOUASSI"
        request.setAttribute("etudiant", etudiant);

        // ETAPE 5 : Afficher le formulaire
        request.getRequestDispatcher("/WEB-INF/ajout-note.jsp").forward(request, response);
    }

    /**
     * ========================================================================
     * ACTION : CREER UNE NOUVELLE NOTE (POST)
     * ========================================================================
     * Cree une nouvelle note pour un etudiant
     *
     * FLUX DE TRAITEMENT :
     * 1. Recuperer les parametres du formulaire
     * 2. Verifier que l'etudiant existe
     * 3. Creer l'objet Note
     * 4. Associer la note a l'etudiant
     * 5. Appeler NoteService pour valider et enregistrer
     * 6. Rediriger vers la page de l'etudiant
     *
     * EXEMPLE DE FORMULAIRE :
     * - etudiantId = 1
     * - matiere = "Java EE"
     * - valeur = 16.5
     * - coefficient = 3
     *
     * VALIDATIONS EFFECTUEES PAR NoteService :
     * - La matiere est obligatoire
     * - La valeur doit etre entre 0 et 20 (systeme ivoirien)
     * - Le coefficient doit etre > 0
     * - L'etudiant doit exister
     *
     * @param request  Pour recuperer les donnees du formulaire
     * @param response Pour rediriger apres creation
     */
    private void creerNote(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // ETAPE 1 : RECUPERER LES PARAMETRES DU FORMULAIRE
        // Le formulaire contient 4 champs :
        Long etudiantId = Long.parseLong(request.getParameter("etudiantId"));
        String matiere = request.getParameter("matiere");
        Double valeur = Double.parseDouble(request.getParameter("valeur"));
        Integer coefficient = Integer.parseInt(request.getParameter("coefficient"));

        // ETAPE 2 : VERIFIER QUE L'ETUDIANT EXISTE
        // IMPORTANT : On ne peut pas ajouter une note a un etudiant inexistant
        Etudiant etudiant = etudiantService.trouverParId(etudiantId);
        if (etudiant == null) {
            throw new Exception("Etudiant introuvable");
        }

        // ETAPE 3 : CREER L'OBJET NOTE
        Note note = new Note();
        note.setMatiere(matiere);
        note.setValeur(valeur);
        note.setCoefficient(coefficient);

        // ETAPE 4 : ASSOCIER LA NOTE A L'ETUDIANT
        // IMPORTANT : Une note est TOUJOURS liee a un etudiant
        // C'est une relation ManyToOne (Plusieurs notes → Un etudiant)
        note.setEtudiant(etudiant);

        // ETAPE 5 : APPELER NoteService POUR VALIDER ET ENREGISTRER
        // Le NoteService va :
        // 1. Valider la matiere (non vide)
        // 2. Valider la valeur (0 <= valeur <= 20)
        // 3. Valider le coefficient (> 0)
        // 4. Appeler NoteDAO pour executer l'INSERT en BDD
        noteService.creerNote(note);

        // ETAPE 6 : REDIRIGER VERS LA PAGE DE L'ETUDIANT
        // L'utilisateur retourne sur la page de details de l'etudiant
        // La nouvelle note apparait dans la liste
        // La moyenne est recalculee automatiquement
        response.sendRedirect(request.getContextPath() + "/etudiants?action=detail&id=" + etudiantId + "&message=Note ajoutee avec succes");
    }

    /**
     * ========================================================================
     * ACTION : SUPPRIMER UNE NOTE
     * ========================================================================
     * Supprime une note de la base de donnees
     *
     * EXEMPLE D'URL : /notes?action=supprimer&id=5&etudiantId=1
     *
     * FLUX DE TRAITEMENT :
     * 1. Recuperer l'ID de la note a supprimer
     * 2. Recuperer l'ID de l'etudiant (pour redirection)
     * 3. Appeler NoteService pour supprimer
     * 4. Rediriger vers la page de l'etudiant
     *
     * CONSEQUENCE :
     * - La note est supprimee de la BDD
     * - La moyenne de l'etudiant est recalculee automatiquement
     *   lors du prochain affichage
     *
     * @param request  Pour recuperer noteId et etudiantId
     * @param response Pour rediriger apres suppression
     */
    private void supprimerNote(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // ETAPE 1 : Recuperer l'ID de la note a supprimer
        Long noteId = Long.parseLong(request.getParameter("id"));

        // ETAPE 2 : Recuperer l'ID de l'etudiant
        // Necessaire pour rediriger vers la bonne page apres suppression
        Long etudiantId = Long.parseLong(request.getParameter("etudiantId"));

        // ETAPE 3 : Supprimer la note
        // NoteService va :
        // 1. Verifier que la note existe
        // 2. Appeler NoteDAO pour executer DELETE FROM note WHERE id = ?
        noteService.supprimerNote(noteId);

        // ETAPE 4 : Rediriger vers la page de l'etudiant
        // L'utilisateur voit la liste mise a jour des notes
        // La moyenne est recalculee sans la note supprimee
        response.sendRedirect(request.getContextPath() + "/etudiants?action=detail&id=" + etudiantId + "&message=Note supprimee avec succes");
    }
}
