package com.iua.gestionetudiants.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * ===========================================================================
 * ENTITE JPA : ETUDIANT
 * ===========================================================================
 * Cette classe represente un etudiant dans l'application.
 * Avec JPA/Hibernate, cette classe Java est automatiquement convertie
 * en table MySQL "etudiant".
 *
 * ROLE : Modele de donnees (partie "M" du pattern MVC)
 * COUCHE : Persistance (Couche 4)
 * ===========================================================================
 */

// @Entity : Indique a JPA que cette classe est une entite (= table en BDD)
@Entity

// @Table : Specifie le nom de la table MySQL correspondante
@Table(name = "etudiant")
public class Etudiant {

    // ===================================================================
    // ATTRIBUTS (correspondent aux colonnes de la table)
    // ===================================================================

    /**
     * ID : Identifiant unique de l'etudiant
     * @Id : Cle primaire de la table
     * @GeneratedValue : La valeur est generee automatiquement par MySQL (AUTO_INCREMENT)
     * IDENTITY : Utilise l'auto-increment natif de MySQL
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * MATRICULE : Numero d'identification unique de l'etudiant
     * unique=true : Impossible d'avoir 2 etudiants avec le meme matricule
     * nullable=false : Ce champ est obligatoire (NOT NULL en SQL)
     * length=50 : Maximum 50 caracteres (VARCHAR(50) en SQL)
     */
    @Column(name = "matricule", unique = true, nullable = false, length = 50)
    private String matricule;

    /**
     * NOM : Nom de famille de l'etudiant
     * nullable=false : Champ obligatoire
     */
    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    /**
     * PRENOM : Prenom de l'etudiant
     * nullable=false : Champ obligatoire
     */
    @Column(name = "prenom", nullable = false, length = 100)
    private String prenom;

    /**
     * EMAIL : Adresse email de l'etudiant
     * Ce champ est optionnel (nullable par defaut)
     */
    @Column(name = "email", length = 150)
    private String email;

    /**
     * DATE DE NAISSANCE : Date de naissance de l'etudiant
     * Type LocalDate : Format moderne Java pour les dates (depuis Java 8)
     */
    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    /**
     * DATE DE CREATION : Date a laquelle l'etudiant a ete ajoute au systeme
     * Automatiquement rempli lors de la creation
     */
    @Column(name = "date_creation")
    private LocalDate dateCreation;

    /**
     * RELATION AVEC LES NOTES
     *
     * @OneToMany : Un etudiant peut avoir PLUSIEURS notes
     * mappedBy="etudiant" : Le champ "etudiant" dans la classe Note
     *                       est le proprietaire de la relation
     *
     * cascade=CascadeType.ALL : Si on supprime un etudiant, toutes ses notes
     *                            sont supprimees automatiquement
     *
     * orphanRemoval=true : Si on retire une note de la liste, elle est
     *                      supprimee de la BDD automatiquement
     *
     * EXEMPLE : Si KOUASSI a 3 notes et qu'on le supprime,
     *           les 3 notes sont aussi supprimees (cascade)
     */
    @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Note> notes = new ArrayList<>();

    // ===================================================================
    // CONSTRUCTEURS
    // ===================================================================

    /**
     * Constructeur par defaut (obligatoire pour JPA)
     * Initialise automatiquement la date de creation a aujourd'hui
     */
    public Etudiant() {
        this.dateCreation = LocalDate.now();
    }

    /**
     * Constructeur avec parametres
     * Utilise pour creer un nouvel etudiant avec toutes ses informations
     *
     * @param matricule Numero d'identification unique
     * @param nom Nom de famille
     * @param prenom Prenom
     * @param email Adresse email
     * @param dateNaissance Date de naissance
     */
    public Etudiant(String matricule, String nom, String prenom, String email, LocalDate dateNaissance) {
        this.matricule = matricule;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.dateNaissance = dateNaissance;
        this.dateCreation = LocalDate.now(); // Date du jour
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    // ===================================================================
    // METHODES UTILITAIRES
    // ===================================================================

    /**
     * Ajoute une note a cet etudiant
     * Maintient la coherence de la relation bidirectionnelle :
     * - Ajoute la note a la liste des notes de l'etudiant
     * - Definit cet etudiant comme proprietaire de la note
     *
     * @param note La note a ajouter
     */
    public void ajouterNote(Note note) {
        notes.add(note);
        note.setEtudiant(this);
    }

    /**
     * Retire une note de cet etudiant
     * Maintient la coherence de la relation bidirectionnelle
     *
     * @param note La note a retirer
     */
    public void retirerNote(Note note) {
        notes.remove(note);
        note.setEtudiant(null);
    }

    @Override
    public String toString() {
        return "Etudiant{" +
                "id=" + id +
                ", matricule='" + matricule + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
