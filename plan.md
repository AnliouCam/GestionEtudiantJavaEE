# Plan de Développement - Gestion des Étudiants et Notes

## Projet : Application Web Java EE
**Classe** : MASTER 1 MIAGE / GI
**Matière** : JAVA EE
**Institut** : Institut Universitaire d'Abidjan
**Date limite** : Mardi 30/12/2025 à 18H00

---

## 1. Objectifs du Projet

Développer une application web Java EE permettant de gérer des étudiants et leurs notes, en respectant une architecture en 4 couches et en incluant des services REST.

---

## 2. Fonctionnalités à Implémenter

### 2.1 Gestion des Étudiants
- ✅ Ajouter un étudiant (nom, prénom, matricule, email, date de naissance)
- ✅ Modifier un étudiant
- ✅ Supprimer un étudiant
- ✅ Afficher la liste de tous les étudiants

### 2.2 Gestion des Notes
- ✅ Ajouter une note à un étudiant (matière, note sur 20, coefficient)
- ✅ Calculer et afficher la moyenne d'un étudiant (moyenne pondérée)

### 2.3 Services REST
- ✅ Afficher la liste des étudiants au format JSON (GET /api/etudiants)
- ✅ Afficher les notes d'un étudiant via REST (GET /api/etudiants/{id}/notes)

---

## 3. Architecture Logicielle (4 Couches)

### Couche 1 : Présentation (JSP/HTML/CSS)
**Localisation** : `src/main/webapp/`
- `index.jsp` - Page d'accueil
- `liste-etudiants.jsp` - Affichage de la liste des étudiants
- `ajout-etudiant.jsp` - Formulaire d'ajout d'un étudiant
- `modifier-etudiant.jsp` - Formulaire de modification
- `detail-etudiant.jsp` - Détails d'un étudiant avec ses notes et moyenne
- `ajout-note.jsp` - Formulaire d'ajout de note
- `css/style.css` - Styles CSS

### Couche 2 : Contrôleur (Servlets)
**Localisation** : `src/main/java/com/iua/gestionetudiants/controller/`
- `EtudiantServlet.java` - Gestion des requêtes pour les étudiants
  - GET : Afficher liste ou détails
  - POST : Ajouter un étudiant
  - PUT : Modifier un étudiant
  - DELETE : Supprimer un étudiant
- `NoteServlet.java` - Gestion des requêtes pour les notes
  - POST : Ajouter une note
  - GET : Afficher les notes

### Couche 3 : Métier (Service/Business Logic)
**Localisation** : `src/main/java/com/iua/gestionetudiants/service/`
- `EtudiantService.java` - Logique métier pour les étudiants
  - Validation des données
  - Règles de gestion
- `NoteService.java` - Logique métier pour les notes
  - Calcul de la moyenne pondérée
  - Validation des notes (entre 0 et 20)

### Couche 4 : Persistance (DAO + JPA)
**Localisation** : `src/main/java/com/iua/gestionetudiants/dao/`

**Entités JPA** : `src/main/java/com/iua/gestionetudiants/model/`
- `Etudiant.java` - Entité étudiant
- `Note.java` - Entité note

**DAO (Data Access Object)** :
- `EtudiantDAO.java` - Accès base de données pour étudiants
- `NoteDAO.java` - Accès base de données pour notes

### Couche REST
**Localisation** : `src/main/java/com/iua/gestionetudiants/rest/`
- `EtudiantRestService.java` - API REST pour étudiants
- Configuration JAX-RS

---

## 4. Modèle de Données

### Table : etudiant
```sql
- id (INT, PRIMARY KEY, AUTO_INCREMENT)
- matricule (VARCHAR(50), UNIQUE)
- nom (VARCHAR(100))
- prenom (VARCHAR(100))
- email (VARCHAR(150))
- date_naissance (DATE)
- date_creation (TIMESTAMP)
```

### Table : note
```sql
- id (INT, PRIMARY KEY, AUTO_INCREMENT)
- etudiant_id (INT, FOREIGN KEY vers etudiant)
- matiere (VARCHAR(100))
- valeur (DECIMAL(5,2)) - note sur 20
- coefficient (INT)
- date_creation (TIMESTAMP)
```

**Relation** : Un étudiant peut avoir plusieurs notes (OneToMany)

---

## 5. Technologies Utilisées

| Technologie | Version | Usage |
|-------------|---------|-------|
| Java JDK | 17 | Langage de programmation |
| Maven | 3.9.12 | Gestion des dépendances et build |
| Apache Tomcat | 10.1.50 | Serveur d'applications |
| MySQL | 8.x (WAMP) | Base de données |
| Hibernate | 6.2.7 | ORM (JPA Implementation) |
| Servlets | 6.0 | Contrôleurs web |
| JSP/JSTL | 3.x | Pages web dynamiques |
| Jersey | 3.1.3 | Services REST (JAX-RS) |
| Gson | 2.10.1 | Sérialisation JSON |

---

## 6. Étapes de Développement

### Phase 1 : Configuration et Structure ✅
1. ✅ Installer Java JDK 17
2. ✅ Installer Maven
3. ✅ Installer Tomcat 10
4. ✅ Configurer MySQL (WAMP)
5. ✅ Créer la structure du projet

### Phase 2 : Couche Persistance
1. Créer le fichier `persistence.xml` (configuration JPA)
2. Créer l'entité `Etudiant.java`
3. Créer l'entité `Note.java`
4. Créer `EtudiantDAO.java`
5. Créer `NoteDAO.java`
6. Créer le script SQL de la base de données

### Phase 3 : Couche Métier
1. Créer `EtudiantService.java`
2. Créer `NoteService.java`
3. Implémenter le calcul de la moyenne

### Phase 4 : Couche Contrôleur
1. Créer `EtudiantServlet.java`
2. Créer `NoteServlet.java`
3. Configurer le fichier `web.xml`

### Phase 5 : Couche Présentation
1. Créer la page d'accueil `index.jsp`
2. Créer `liste-etudiants.jsp`
3. Créer `ajout-etudiant.jsp`
4. Créer `modifier-etudiant.jsp`
5. Créer `detail-etudiant.jsp`
6. Créer `ajout-note.jsp`
7. Ajouter le CSS (`style.css`)

### Phase 6 : Services REST
1. Créer `EtudiantRestService.java`
2. Configurer JAX-RS
3. Tester les endpoints REST

### Phase 7 : Tests et Déploiement
1. Tester toutes les fonctionnalités
2. Générer le fichier WAR
3. Déployer sur Tomcat
4. Exporter le script SQL final

### Phase 8 : Documentation
1. Préparer la présentation PowerPoint
2. Documenter le code
3. Préparer la démonstration

---

## 7. URLs de l'Application

### Pages Web
- `http://localhost:8080/GestionEtudiants/` - Page d'accueil
- `http://localhost:8080/GestionEtudiants/etudiants` - Liste des étudiants
- `http://localhost:8080/GestionEtudiants/etudiants?action=ajouter` - Ajouter étudiant
- `http://localhost:8080/GestionEtudiants/etudiants?action=modifier&id=X` - Modifier
- `http://localhost:8080/GestionEtudiants/etudiants?action=detail&id=X` - Détails

### API REST
- `http://localhost:8080/GestionEtudiants/api/etudiants` - GET : Liste JSON
- `http://localhost:8080/GestionEtudiants/api/etudiants/{id}/notes` - GET : Notes JSON

---

## 8. Livrables à Remettre

1. ✅ Présentation PowerPoint
2. ✅ Code source complet
3. ✅ Script SQL de la base de données
4. ✅ Fichier WAR (GestionEtudiants.war)
5. ✅ Démonstration pratique

**Date de présentation : Mardi 30/12/2025 à 18H00**

---

## 9. Points d'Attention

### Sécurité
- Validation des entrées utilisateur
- Protection contre les injections SQL (utilisation de JPA)
- Gestion des erreurs

### Bonnes Pratiques
- Séparation des responsabilités (4 couches)
- Code commenté et lisible
- Nommage cohérent des variables et classes
- Gestion des exceptions

### Performance
- Utilisation de connexions poolées
- Requêtes optimisées
- Chargement lazy des relations JPA

---

## 10. Commandes Utiles

### Compiler le projet
```bash
mvn clean compile
```

### Générer le fichier WAR
```bash
mvn clean package
```

### Lancer Tomcat
```bash
C:\Users\Administrator\apache-tomcat-10.1.50\bin\startup.bat
```

### Arrêter Tomcat
```bash
C:\Users\Administrator\apache-tomcat-10.1.50\bin\shutdown.bat
```

### Déployer l'application
Copier le fichier `target/GestionEtudiants.war` vers `C:\Users\Administrator\apache-tomcat-10.1.50\webapps\`
