package com.iua.gestionetudiants.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Entité JPA représentant une note
 * Correspond à la table "note" dans la base de données
 */
@Entity
@Table(name = "note")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "matiere", nullable = false, length = 100)
    private String matiere;

    @Column(name = "valeur", nullable = false)
    private Double valeur;

    @Column(name = "coefficient", nullable = false)
    private Integer coefficient;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    // Relation ManyToOne : plusieurs notes pour un étudiant
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;

    // Constructeurs
    public Note() {
        this.dateCreation = LocalDate.now();
    }

    public Note(String matiere, Double valeur, Integer coefficient) {
        this.matiere = matiere;
        this.valeur = valeur;
        this.coefficient = coefficient;
        this.dateCreation = LocalDate.now();
    }

    public Note(String matiere, Double valeur, Integer coefficient, Etudiant etudiant) {
        this.matiere = matiere;
        this.valeur = valeur;
        this.coefficient = coefficient;
        this.etudiant = etudiant;
        this.dateCreation = LocalDate.now();
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatiere() {
        return matiere;
    }

    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }

    public Double getValeur() {
        return valeur;
    }

    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }

    public Integer getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Integer coefficient) {
        this.coefficient = coefficient;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", matiere='" + matiere + '\'' +
                ", valeur=" + valeur +
                ", coefficient=" + coefficient +
                '}';
    }
}
