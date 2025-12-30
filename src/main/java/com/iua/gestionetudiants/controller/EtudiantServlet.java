package com.iua.gestionetudiants.controller;

import com.iua.gestionetudiants.model.Etudiant;
import com.iua.gestionetudiants.service.EtudiantService;
import com.iua.gestionetudiants.service.NoteService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * ===========================================================================
 * SERVLET CONTROLEUR : GESTION DES ETUDIANTS
 * ===========================================================================
 * Cette classe est un SERVLET, c'est-a-dire un composant Java EE qui
 * traite les requetes HTTP (GET, POST, PUT, DELETE) venant du navigateur.
 *
 * ROLE : Couche Controleur (Couche 2 de l'architecture 4 couches)
 * RESPONSABILITES :
 * 1. Recevoir les requetes HTTP du navigateur
 * 2. Extraire les parametres (formulaires, URLs)
 * 3. Appeler la couche Service (logique metier)
 * 4. Transmettre les donnees a la JSP (vue)
 * 5. Gerer les redirections et les erreurs
 *
 * PATTERN MVC :
 * - Model (M) : Etudiant.java (entite JPA)
 * - View (V) : Les fichiers JSP (liste-etudiants.jsp, etc.)
 * - Controller (C) : Ce Servlet (EtudiantServlet.java) <-- VOUS ETES ICI
 *
 * EXEMPLE DE FLUX :
 * 1. Utilisateur clique sur "Ajouter etudiant" dans le navigateur
 * 2. Navigateur envoie requete HTTP GET vers /etudiants?action=ajouter
 * 3. Tomcat recoit la requete et l'achemine vers ce Servlet
 * 4. La methode doGet() est appelee automatiquement
 * 5. Le Servlet analyse le parametre "action=ajouter"
 * 6. Il affiche le formulaire JSP (ajout-etudiant.jsp)
 * 7. Utilisateur remplit le formulaire et clique "Enregistrer"
 * 8. Navigateur envoie requete HTTP POST vers /etudiants?action=creer
 * 9. La methode doPost() est appelee
 * 10. Le Servlet extrait les donnees du formulaire
 * 11. Il appelle etudiantService.creerEtudiant() (couche metier)
 * 12. Il redirige vers la liste avec un message de succes
 * ===========================================================================
 */

/**
 * @WebServlet("/etudiants") : Annotation qui mappe ce Servlet a l'URL /etudiants
 *
 * SIGNIFICATION :
 * - Toutes les requetes HTTP vers http://localhost:8080/GestionEtudiant/etudiants
 *   seront traitees par ce Servlet
 *
 * EXEMPLES D'URLS GEREES :
 * - /etudiants                           → Liste tous les etudiants
 * - /etudiants?action=ajouter            → Affiche formulaire d'ajout
 * - /etudiants?action=detail&id=1        → Affiche details de l'etudiant 1
 * - /etudiants?action=modifier&id=2      → Affiche formulaire de modification
 * - /etudiants?action=supprimer&id=3     → Supprime l'etudiant 3
 */
@WebServlet("/etudiants")
public class EtudiantServlet extends HttpServlet {

    // ===================================================================
    // SERVICES (Couche 3 - Logique Metier)
    // ===================================================================
    // Le Servlet NE FAIT PAS de logique metier lui-meme
    // Il DELEGUE tout a la couche Service

    /**
     * Service pour gerer la logique metier des etudiants
     * (validation, regles de gestion, orchestration)
     */
    private EtudiantService etudiantService = new EtudiantService();

    /**
     * Service pour gerer la logique metier des notes
     * (calcul de moyenne, validation)
     */
    private NoteService noteService = new NoteService();

    // ===================================================================
    // METHODE doGet() - GERER LES REQUETES HTTP GET
    // ===================================================================
    /**
     * Cette methode est AUTOMATIQUEMENT appelee par Tomcat
     * quand le navigateur fait une requete GET vers /etudiants
     *
     * QUAND EST-ELLE APPELEE ?
     * - Quand l'utilisateur tape une URL dans le navigateur
     * - Quand l'utilisateur clique sur un lien <a href="...">
     * - Quand la page charge des donnees
     *
     * ACTIONS GEREES :
     * - Aucune action (null)    → Afficher la liste des etudiants
     * - action=detail           → Afficher les details d'un etudiant
     * - action=ajouter          → Afficher le formulaire d'ajout
     * - action=modifier         → Afficher le formulaire de modification
     * - action=supprimer        → Supprimer un etudiant
     *
     * @param request  L'objet qui contient toutes les informations de la requete
     *                 (parametres, headers, session, cookies, etc.)
     * @param response L'objet utilise pour envoyer la reponse au navigateur
     *                 (HTML, redirection, cookies, etc.)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. EXTRAIRE LE PARAMETRE "action" DE L'URL
        // Exemple : /etudiants?action=detail → action = "detail"
        //           /etudiants                → action = null
        String action = request.getParameter("action");

        try {
            // 2. ROUTER LA REQUETE SELON L'ACTION DEMANDEE
            if (action == null) {
                // Aucune action specifiee → Afficher la liste par defaut
                listerEtudiants(request, response);
            } else {
                // Une action est specifiee → Router selon le cas
                switch (action) {
                    case "detail":
                        // Afficher les details d'un etudiant avec ses notes
                        afficherDetail(request, response);
                        break;
                    case "ajouter":
                        // Afficher le formulaire d'ajout d'un nouvel etudiant
                        afficherFormulaireAjout(request, response);
                        break;
                    case "modifier":
                        // Afficher le formulaire de modification
                        afficherFormulaireModification(request, response);
                        break;
                    case "supprimer":
                        // Supprimer un etudiant
                        supprimerEtudiant(request, response);
                        break;
                    default:
                        // Action inconnue → Afficher la liste par securite
                        listerEtudiants(request, response);
                }
            }
        } catch (Exception e) {
            // 3. GESTION DES ERREURS
            // Si une erreur survient, afficher la page d'erreur
            request.setAttribute("erreur", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/erreur.jsp").forward(request, response);
        }
    }

    // ===================================================================
    // METHODE doPost() - GERER LES REQUETES HTTP POST
    // ===================================================================
    /**
     * Cette methode est AUTOMATIQUEMENT appelee par Tomcat
     * quand le navigateur fait une requete POST vers /etudiants
     *
     * QUAND EST-ELLE APPELEE ?
     * - Quand un formulaire HTML est soumis avec method="POST"
     * - Lors de la creation ou modification de donnees
     *
     * DIFFERENCE ENTRE GET ET POST :
     * - GET  : Recuperer/Afficher des donnees (lecture seule)
     * - POST : Creer/Modifier des donnees (ecriture en base)
     *
     * ACTIONS GEREES :
     * - action=creer   → Creer un nouvel etudiant
     * - action=modifier → Modifier un etudiant existant
     *
     * @param request  Contient les donnees du formulaire
     * @param response Utilise pour rediriger apres traitement
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. EXTRAIRE L'ACTION DU FORMULAIRE
        // Le formulaire contient un champ cache : <input type="hidden" name="action" value="creer">
        String action = request.getParameter("action");

        try {
            // 2. ROUTER SELON L'ACTION
            if ("creer".equals(action)) {
                // L'utilisateur a soumis le formulaire de creation
                creerEtudiant(request, response);
            } else if ("modifier".equals(action)) {
                // L'utilisateur a soumis le formulaire de modification
                modifierEtudiant(request, response);
            } else {
                // Action inconnue → Rediriger vers la liste
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
     * ACTION : LISTER TOUS LES ETUDIANTS
     * ========================================================================
     * Cette methode recupere la liste de tous les etudiants et l'affiche
     *
     * FLUX DE TRAITEMENT :
     * 1. Appeler le Service pour recuperer les etudiants depuis la BDD
     * 2. Stocker la liste dans la requete (setAttribute)
     * 3. Transmettre la requete a la JSP (forward)
     * 4. La JSP affiche les donnees avec JSTL
     *
     * @param request  Pour stocker la liste d'etudiants
     * @param response Non utilise ici
     */
    private void listerEtudiants(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ETAPE 1 : Appeler la couche Service pour recuperer les donnees
        // Le Service appelle le DAO qui execute la requete SQL
        List<Etudiant> etudiants = etudiantService.listerTous();

        // ETAPE 2 : Stocker les donnees dans la requete
        // setAttribute("nom", valeur) rend les donnees accessibles a la JSP
        request.setAttribute("etudiants", etudiants);

        // ETAPE 3 : Transmettre la requete a la JSP
        // forward() = Passer la main a la JSP pour afficher les donnees
        // La JSP peut lire les donnees avec ${etudiants}
        request.getRequestDispatcher("/WEB-INF/liste-etudiants.jsp").forward(request, response);
    }

    /**
     * ========================================================================
     * ACTION : AFFICHER LES DETAILS D'UN ETUDIANT
     * ========================================================================
     * Affiche toutes les informations d'un etudiant :
     * - Informations personnelles (nom, prenom, email, etc.)
     * - Liste de ses notes
     * - Moyenne ponderee calculee
     *
     * EXEMPLE D'URL : /etudiants?action=detail&id=1
     *
     * @param request  Pour recuperer l'ID et stocker l'etudiant + moyenne
     * @param response Pour rediriger si l'etudiant n'existe pas
     */
    private void afficherDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ETAPE 1 : Recuperer l'ID de l'etudiant depuis l'URL
        // Exemple : /etudiants?action=detail&id=5 → id = 5
        Long id = Long.parseLong(request.getParameter("id"));

        // ETAPE 2 : Appeler le Service pour recuperer l'etudiant avec ses notes
        // IMPORTANT : On charge aussi les notes (relation OneToMany)
        Etudiant etudiant = etudiantService.trouverParIdAvecNotes(id);

        // ETAPE 3 : Verification - L'etudiant existe-t-il ?
        if (etudiant == null) {
            // L'etudiant n'existe pas → Rediriger vers la liste
            response.sendRedirect(request.getContextPath() + "/etudiants");
            return;
        }

        // ETAPE 4 : Calculer la moyenne ponderee de l'etudiant
        // Le NoteService applique la formule : Σ(note × coef) / Σ(coef)
        double moyenne = noteService.calculerMoyenne(etudiant.getNotes());

        // ETAPE 5 : Stocker les donnees dans la requete
        request.setAttribute("etudiant", etudiant);
        request.setAttribute("moyenne", moyenne);

        // ETAPE 6 : Transmettre a la JSP pour affichage
        request.getRequestDispatcher("/WEB-INF/detail-etudiant.jsp").forward(request, response);
    }

    /**
     * ========================================================================
     * ACTION : AFFICHER LE FORMULAIRE D'AJOUT
     * ========================================================================
     * Affiche un formulaire vide pour creer un nouvel etudiant
     *
     * EXEMPLE D'URL : /etudiants?action=ajouter
     */
    private void afficherFormulaireAjout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Aucun traitement necessaire, on affiche juste le formulaire vide
        request.getRequestDispatcher("/WEB-INF/ajout-etudiant.jsp").forward(request, response);
    }

    /**
     * ========================================================================
     * ACTION : AFFICHER LE FORMULAIRE DE MODIFICATION
     * ========================================================================
     * Affiche un formulaire pre-rempli avec les donnees de l'etudiant
     *
     * EXEMPLE D'URL : /etudiants?action=modifier&id=2
     */
    private void afficherFormulaireModification(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ETAPE 1 : Recuperer l'ID de l'etudiant a modifier
        Long id = Long.parseLong(request.getParameter("id"));

        // ETAPE 2 : Charger l'etudiant depuis la BDD
        Etudiant etudiant = etudiantService.trouverParId(id);

        // ETAPE 3 : Verification
        if (etudiant == null) {
            // L'etudiant n'existe pas → Rediriger vers la liste
            response.sendRedirect(request.getContextPath() + "/etudiants");
            return;
        }

        // ETAPE 4 : Passer l'etudiant a la JSP
        // La JSP va pre-remplir le formulaire avec ces donnees
        request.setAttribute("etudiant", etudiant);

        // ETAPE 5 : Afficher le formulaire
        request.getRequestDispatcher("/WEB-INF/modifier-etudiant.jsp").forward(request, response);
    }

    /**
     * ========================================================================
     * ACTION : CREER UN NOUVEL ETUDIANT (POST)
     * ========================================================================
     * Recupere les donnees du formulaire et cree un nouvel etudiant en BDD
     *
     * FLUX DE TRAITEMENT :
     * 1. Extraire tous les parametres du formulaire (nom, prenom, etc.)
     * 2. Creer un objet Etudiant et remplir ses attributs
     * 3. Appeler le Service pour valider et enregistrer en BDD
     * 4. Rediriger vers la liste avec un message de succes
     *
     * @param request  Pour recuperer les donnees du formulaire
     * @param response Pour rediriger apres creation
     */
    private void creerEtudiant(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // ETAPE 1 : RECUPERER LES PARAMETRES DU FORMULAIRE
        // Chaque champ du formulaire HTML devient un parametre
        // Exemple : <input name="matricule"> → request.getParameter("matricule")
        String matricule = request.getParameter("matricule");
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String email = request.getParameter("email");
        String dateNaissanceStr = request.getParameter("dateNaissance");

        // ETAPE 2 : CREER L'OBJET ETUDIANT
        Etudiant etudiant = new Etudiant();
        etudiant.setMatricule(matricule);
        etudiant.setNom(nom);
        etudiant.setPrenom(prenom);
        etudiant.setEmail(email);

        // ETAPE 3 : CONVERTIR LA DATE (si fournie)
        // Le formulaire HTML envoie la date au format "yyyy-MM-dd"
        // On la convertit en LocalDate
        if (dateNaissanceStr != null && !dateNaissanceStr.trim().isEmpty()) {
            etudiant.setDateNaissance(LocalDate.parse(dateNaissanceStr));
        }

        // ETAPE 4 : APPELER LE SERVICE POUR ENREGISTRER EN BDD
        // Le Service va :
        // - Valider les donnees (matricule unique, champs obligatoires, etc.)
        // - Appeler le DAO pour executer l'INSERT en BDD
        etudiantService.creerEtudiant(etudiant);

        // ETAPE 5 : REDIRIGER VERS LA LISTE AVEC UN MESSAGE DE SUCCES
        // sendRedirect() = Demander au navigateur d'aller vers une nouvelle page
        // Le message est affiche par la JSP si le parametre "message" existe
        response.sendRedirect(request.getContextPath() + "/etudiants?message=Etudiant cree avec succes");
    }

    /**
     * ========================================================================
     * ACTION : MODIFIER UN ETUDIANT EXISTANT (POST)
     * ========================================================================
     * Recupere les nouvelles donnees du formulaire et met a jour l'etudiant
     *
     * @param request  Pour recuperer l'ID et les nouvelles donnees
     * @param response Pour rediriger apres modification
     */
    private void modifierEtudiant(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // ETAPE 1 : RECUPERER LES PARAMETRES
        Long id = Long.parseLong(request.getParameter("id"));
        String matricule = request.getParameter("matricule");
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String email = request.getParameter("email");
        String dateNaissanceStr = request.getParameter("dateNaissance");

        // ETAPE 2 : RECUPERER L'ETUDIANT EXISTANT DEPUIS LA BDD
        Etudiant etudiant = etudiantService.trouverParId(id);
        if (etudiant == null) {
            throw new Exception("Etudiant introuvable");
        }

        // ETAPE 3 : MODIFIER LES DONNEES
        // On modifie l'objet existant avec les nouvelles valeurs
        etudiant.setMatricule(matricule);
        etudiant.setNom(nom);
        etudiant.setPrenom(prenom);
        etudiant.setEmail(email);

        // Convertir la date
        if (dateNaissanceStr != null && !dateNaissanceStr.trim().isEmpty()) {
            etudiant.setDateNaissance(LocalDate.parse(dateNaissanceStr));
        }

        // ETAPE 4 : ENREGISTRER LES MODIFICATIONS EN BDD
        // Le Service va valider puis le DAO va executer un UPDATE SQL
        etudiantService.modifierEtudiant(etudiant);

        // ETAPE 5 : REDIRIGER VERS LES DETAILS DE L'ETUDIANT
        response.sendRedirect(request.getContextPath() + "/etudiants?action=detail&id=" + id + "&message=Etudiant modifie avec succes");
    }

    /**
     * ========================================================================
     * ACTION : SUPPRIMER UN ETUDIANT
     * ========================================================================
     * Supprime un etudiant de la base de donnees
     *
     * IMPORTANT : Toutes les notes de l'etudiant sont aussi supprimees
     * grace au cascade=CascadeType.ALL dans la relation @OneToMany
     *
     * @param request  Pour recuperer l'ID de l'etudiant a supprimer
     * @param response Pour rediriger apres suppression
     */
    private void supprimerEtudiant(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // ETAPE 1 : Recuperer l'ID de l'etudiant a supprimer
        Long id = Long.parseLong(request.getParameter("id"));

        // ETAPE 2 : Appeler le Service pour supprimer
        // Le Service va verifier que l'etudiant existe
        // Le DAO va executer un DELETE SQL
        // Grace au cascade, toutes les notes sont supprimees automatiquement
        etudiantService.supprimerEtudiant(id);

        // ETAPE 3 : Rediriger vers la liste avec un message
        response.sendRedirect(request.getContextPath() + "/etudiants?message=Etudiant supprime avec succes");
    }
}
