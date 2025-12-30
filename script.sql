-- ====================================================
-- Script SQL - Gestion des Etudiants et des Notes
-- Institut Universitaire d'Abidjan
-- MASTER 1 MIAGE/GI - Projet Java EE
-- Date: 29/12/2025
-- ====================================================

-- ====================================================
-- 1. CREATION DE LA BASE DE DONNEES
-- ====================================================

-- Supprimer la base si elle existe deja (optionnel)
DROP DATABASE IF EXISTS gestion_etudiants;

-- Creer la base de donnees
CREATE DATABASE gestion_etudiants
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- Utiliser la base de donnees
USE gestion_etudiants;

-- ====================================================
-- 2. CREATION DE LA TABLE ETUDIANT
-- ====================================================

CREATE TABLE etudiant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    matricule VARCHAR(50) NOT NULL UNIQUE,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150),
    date_naissance DATE,
    date_creation DATE NOT NULL,

    INDEX idx_matricule (matricule),
    INDEX idx_nom_prenom (nom, prenom)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ====================================================
-- 3. CREATION DE LA TABLE NOTE
-- ====================================================

CREATE TABLE note (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    matiere VARCHAR(100) NOT NULL,
    valeur DOUBLE NOT NULL,
    coefficient INT NOT NULL,
    date_creation DATE NOT NULL,
    etudiant_id BIGINT NOT NULL,

    CONSTRAINT fk_note_etudiant
        FOREIGN KEY (etudiant_id)
        REFERENCES etudiant(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT chk_valeur
        CHECK (valeur >= 0 AND valeur <= 20),

    CONSTRAINT chk_coefficient
        CHECK (coefficient >= 1 AND coefficient <= 10),

    INDEX idx_etudiant_id (etudiant_id),
    INDEX idx_matiere (matiere)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ====================================================
-- 4. INSERTION DE DONNEES DE TEST
-- ====================================================

-- Insertion des etudiants
INSERT INTO etudiant (matricule, nom, prenom, email, date_naissance, date_creation) VALUES
('MAT2025001', 'KOUASSI', 'Jean', 'jean.kouassi@iua.ci', '2000-05-15', CURDATE()),
('MAT2025002', 'YAO', 'Marie', 'marie.yao@iua.ci', '2001-08-22', CURDATE()),
('MAT2025003', 'DIALLO', 'Ibrahim', 'ibrahim.diallo@iua.ci', '1999-12-10', CURDATE()),
('MAT2025004', 'KONE', 'Fatou', 'fatou.kone@iua.ci', '2000-03-18', CURDATE()),
('MAT2025005', 'TRAORE', 'Moussa', 'moussa.traore@iua.ci', '2001-11-05', CURDATE());

-- Insertion des notes pour KOUASSI Jean (MAT2025001)
INSERT INTO note (matiere, valeur, coefficient, date_creation, etudiant_id) VALUES
('Java EE', 16.5, 3, CURDATE(), 1),
('Bases de Donnees', 14.0, 2, CURDATE(), 1),
('Anglais', 12.5, 1, CURDATE(), 1),
('Gestion de Projet', 15.0, 2, CURDATE(), 1);

-- Insertion des notes pour YAO Marie (MAT2025002)
INSERT INTO note (matiere, valeur, coefficient, date_creation, etudiant_id) VALUES
('Java EE', 18.0, 3, CURDATE(), 2),
('Bases de Donnees', 16.5, 2, CURDATE(), 2),
('Anglais', 14.0, 1, CURDATE(), 2),
('Gestion de Projet', 17.0, 2, CURDATE(), 2);

-- Insertion des notes pour DIALLO Ibrahim (MAT2025003)
INSERT INTO note (matiere, valeur, coefficient, date_creation, etudiant_id) VALUES
('Java EE', 13.0, 3, CURDATE(), 3),
('Bases de Donnees', 11.5, 2, CURDATE(), 3),
('Anglais', 10.0, 1, CURDATE(), 3),
('Gestion de Projet', 12.0, 2, CURDATE(), 3);

-- Insertion des notes pour KONE Fatou (MAT2025004)
INSERT INTO note (matiere, valeur, coefficient, date_creation, etudiant_id) VALUES
('Java EE', 15.5, 3, CURDATE(), 4),
('Bases de Donnees', 13.0, 2, CURDATE(), 4),
('Anglais', 11.5, 1, CURDATE(), 4);

-- Insertion des notes pour TRAORE Moussa (MAT2025005)
INSERT INTO note (matiere, valeur, coefficient, date_creation, etudiant_id) VALUES
('Java EE', 17.0, 3, CURDATE(), 5),
('Bases de Donnees', 15.5, 2, CURDATE(), 5);

-- ====================================================
-- 5. VERIFICATION DES DONNEES INSEREES
-- ====================================================

-- Afficher tous les etudiants
SELECT 'Liste des etudiants :' AS '';
SELECT id, matricule, nom, prenom, email, date_naissance
FROM etudiant
ORDER BY nom, prenom;

-- Afficher toutes les notes avec les informations des etudiants
SELECT 'Liste des notes par etudiant :' AS '';
SELECT
    e.matricule,
    CONCAT(e.prenom, ' ', e.nom) AS etudiant,
    n.matiere,
    n.valeur AS note,
    n.coefficient AS coef,
    ROUND(n.valeur * n.coefficient, 2) AS points
FROM etudiant e
INNER JOIN note n ON e.id = n.etudiant_id
ORDER BY e.nom, e.prenom, n.matiere;

-- Afficher les moyennes par etudiant
SELECT 'Moyennes des etudiants :' AS '';
SELECT
    e.matricule,
    CONCAT(e.prenom, ' ', e.nom) AS etudiant,
    COUNT(n.id) AS nb_notes,
    ROUND(SUM(n.valeur * n.coefficient) / SUM(n.coefficient), 2) AS moyenne,
    CASE
        WHEN SUM(n.valeur * n.coefficient) / SUM(n.coefficient) >= 10
        THEN 'Admis'
        ELSE 'Non admis'
    END AS statut
FROM etudiant e
LEFT JOIN note n ON e.id = n.etudiant_id
GROUP BY e.id, e.matricule, e.nom, e.prenom
ORDER BY moyenne DESC;

-- ====================================================
-- 6. REQUETES UTILES POUR L'ADMINISTRATION
-- ====================================================

-- Vue pour faciliter les requetes sur les moyennes
CREATE VIEW vue_moyennes_etudiants AS
SELECT
    e.id,
    e.matricule,
    e.nom,
    e.prenom,
    e.email,
    COUNT(n.id) AS nombre_notes,
    COALESCE(ROUND(SUM(n.valeur * n.coefficient) / NULLIF(SUM(n.coefficient), 0), 2), 0) AS moyenne_generale,
    CASE
        WHEN SUM(n.valeur * n.coefficient) / NULLIF(SUM(n.coefficient), 0) >= 10
        THEN 'Admis'
        ELSE 'Non admis'
    END AS statut_admission
FROM etudiant e
LEFT JOIN note n ON e.id = n.etudiant_id
GROUP BY e.id, e.matricule, e.nom, e.prenom, e.email;

-- Afficher la vue
SELECT 'Vue des moyennes :' AS '';
SELECT * FROM vue_moyennes_etudiants ORDER BY moyenne_generale DESC;

-- ====================================================
-- 7. STATISTIQUES GLOBALES
-- ====================================================

SELECT 'Statistiques globales :' AS '';
SELECT
    COUNT(DISTINCT e.id) AS total_etudiants,
    COUNT(n.id) AS total_notes,
    ROUND(AVG(n.valeur), 2) AS moyenne_generale_classe,
    MAX(n.valeur) AS meilleure_note,
    MIN(n.valeur) AS note_minimale
FROM etudiant e
LEFT JOIN note n ON e.id = n.etudiant_id;

-- ====================================================
-- FIN DU SCRIPT
-- ====================================================

-- Message de confirmation
SELECT 'Base de donnees creee avec succes !' AS message;
SELECT 'Tables : etudiant, note' AS tables_creees;
SELECT '5 etudiants de test inseres' AS donnees_test;
SELECT 'Utilisez la vue vue_moyennes_etudiants pour consulter les moyennes' AS info;
