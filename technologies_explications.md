# Guide des Technologies et Concepts - Projet Java EE

Ce document explique **tous les éléments, technologies et concepts** utilisés dans ce projet, de manière simple et pédagogique.

---

## 📚 Table des Matières

1. [Technologies de Base](#1-technologies-de-base)
2. [Architecture 4 Couches](#2-architecture-4-couches)
3. [Maven - Gestion de Projet](#3-maven---gestion-de-projet)
4. [JPA & Hibernate - Base de Données](#4-jpa--hibernate---base-de-données)
5. [Servlets - Contrôleurs Web](#5-servlets---contrôleurs-web)
6. [JSP & JSTL - Pages Dynamiques](#6-jsp--jstl---pages-dynamiques)
7. [REST & JSON - API Web](#7-rest--json---api-web)
8. [Tomcat - Serveur d'Applications](#8-tomcat---serveur-dapplications)
9. [Structure des Dossiers](#9-structure-des-dossiers)
10. [Flux de Données](#10-flux-de-données)

---

## 1. Technologies de Base

### 1.1 Java (JDK 17)

**C'est quoi ?**
- Java est le langage de programmation utilisé
- JDK = Java Development Kit (kit de développement)
- Version 17 = version Long Term Support (support à long terme)

**À quoi ça sert ?**
- Écrire tout le code de l'application (servlets, services, modèles)
- Compiler et exécuter le programme

**Exemple simple :**
```java
public class Etudiant {
    private String nom;
    private String prenom;

    // C'est du Java classique
}
```

---

### 1.2 Java EE (Jakarta EE)

**C'est quoi ?**
- Java EE = Java Enterprise Edition
- Ensemble de spécifications pour développer des applications web d'entreprise
- Nouvelle nom : Jakarta EE (depuis Oracle a donné Java EE à la fondation Eclipse)

**Composants utilisés :**
- **Servlets** : Pour gérer les requêtes web
- **JSP** : Pour créer des pages web dynamiques
- **JPA** : Pour accéder à la base de données
- **JAX-RS** : Pour créer des services REST

---

## 2. Architecture 4 Couches

### Pourquoi 4 couches ?

C'est comme construire une maison avec des étages bien séparés :

```
┌─────────────────────────────────────┐
│  COUCHE 1 : PRÉSENTATION (JSP)      │  ← Ce que l'utilisateur voit
├─────────────────────────────────────┤
│  COUCHE 2 : CONTRÔLEUR (Servlets)   │  ← Reçoit les demandes
├─────────────────────────────────────┤
│  COUCHE 3 : MÉTIER (Services)       │  ← Logique de l'application
├─────────────────────────────────────┤
│  COUCHE 4 : PERSISTANCE (DAO/JPA)   │  ← Communique avec la BDD
└─────────────────────────────────────┘
```

### 2.1 Couche Présentation (Vue)

**Rôle :** Afficher les informations à l'utilisateur

**Technologies :**
- **JSP** (Java Server Pages) : HTML + code Java
- **HTML** : Structure des pages
- **CSS** : Design et couleurs
- **JSTL** : Balises pour afficher des données

**Fichiers :**
- `index.jsp` - Page d'accueil
- `liste-etudiants.jsp` - Liste des étudiants
- `ajout-etudiant.jsp` - Formulaire d'ajout

**Exemple JSP :**
```jsp
<%-- Ceci est une page JSP --%>
<h1>Liste des Étudiants</h1>
<c:forEach items="${etudiants}" var="etudiant">
    <p>${etudiant.nom} ${etudiant.prenom}</p>
</c:forEach>
```

---

### 2.2 Couche Contrôleur

**Rôle :** Recevoir les demandes de l'utilisateur et orchestrer les réponses

**Technologies :**
- **Servlets** : Classes Java qui gèrent les requêtes HTTP

**Comment ça marche ?**
1. L'utilisateur clique sur un bouton
2. Le navigateur envoie une requête HTTP (GET ou POST)
3. Le Servlet reçoit la requête
4. Le Servlet appelle les services métier
5. Le Servlet renvoie une page JSP

**Exemple Servlet :**
```java
@WebServlet("/etudiants")
public class EtudiantServlet extends HttpServlet {

    // Quand l'utilisateur demande la page (GET)
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        // 1. Récupérer les étudiants du service
        List<Etudiant> etudiants = etudiantService.listerTous();

        // 2. Mettre dans la requête
        request.setAttribute("etudiants", etudiants);

        // 3. Afficher la page JSP
        request.getRequestDispatcher("/liste-etudiants.jsp").forward(request, response);
    }

    // Quand l'utilisateur soumet un formulaire (POST)
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        // Créer un nouvel étudiant
    }
}
```

---

### 2.3 Couche Métier (Business Logic)

**Rôle :** Contenir la logique de l'application (règles métier, calculs)

**Classes :**
- `EtudiantService.java`
- `NoteService.java`

**Exemple de logique métier :**
```java
public class NoteService {

    // Calculer la moyenne pondérée d'un étudiant
    public double calculerMoyenne(List<Note> notes) {
        double sommeNotes = 0;
        int sommeCoefficients = 0;

        for (Note note : notes) {
            sommeNotes += note.getValeur() * note.getCoefficient();
            sommeCoefficients += note.getCoefficient();
        }

        return sommeNotes / sommeCoefficients;
    }

    // Valider qu'une note est entre 0 et 20
    public boolean validerNote(double valeur) {
        return valeur >= 0 && valeur <= 20;
    }
}
```

**Pourquoi séparer ?**
- Si on veut changer le calcul de la moyenne, on modifie seulement ici
- Réutilisable : le calcul peut être appelé depuis plusieurs endroits

---

### 2.4 Couche Persistance (Accès aux Données)

**Rôle :** Communiquer avec la base de données

**Technologies :**
- **JPA** (Java Persistence API) : Standard Java pour la persistance
- **Hibernate** : Implémentation de JPA (fait le travail réel)
- **DAO** (Data Access Object) : Pattern de design

**Composants :**

#### A) Entités JPA (Modèles)

Les entités représentent les tables de la base de données.

**Exemple Etudiant.java :**
```java
@Entity  // ← Indique que c'est une table
@Table(name = "etudiant")  // ← Nom de la table
public class Etudiant {

    @Id  // ← Clé primaire
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ← Auto-increment
    private Long id;

    @Column(name = "nom", nullable = false, length = 100)  // ← Colonne
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @OneToMany(mappedBy = "etudiant")  // ← Relation : un étudiant a plusieurs notes
    private List<Note> notes;

    // Getters et Setters
}
```

**Annotations JPA expliquées :**
- `@Entity` : Cette classe représente une table
- `@Table` : Nom de la table dans la BDD
- `@Id` : Clé primaire
- `@GeneratedValue` : Valeur auto-générée (auto-increment)
- `@Column` : Détails de la colonne
- `@OneToMany` : Relation entre tables (un étudiant → plusieurs notes)

#### B) DAO (Data Access Object)

Les DAO contiennent les requêtes vers la base de données.

**Exemple EtudiantDAO.java :**
```java
public class EtudiantDAO {

    private EntityManager em;  // ← Gestionnaire JPA

    // CREATE - Ajouter un étudiant
    public void ajouter(Etudiant etudiant) {
        em.getTransaction().begin();
        em.persist(etudiant);  // ← Enregistre dans la BDD
        em.getTransaction().commit();
    }

    // READ - Trouver par ID
    public Etudiant trouverParId(Long id) {
        return em.find(Etudiant.class, id);
    }

    // READ - Lister tous
    public List<Etudiant> listerTous() {
        return em.createQuery("SELECT e FROM Etudiant e", Etudiant.class)
                 .getResultList();
    }

    // UPDATE - Modifier
    public void modifier(Etudiant etudiant) {
        em.getTransaction().begin();
        em.merge(etudiant);  // ← Met à jour
        em.getTransaction().commit();
    }

    // DELETE - Supprimer
    public void supprimer(Long id) {
        Etudiant etudiant = trouverParId(id);
        em.getTransaction().begin();
        em.remove(etudiant);  // ← Supprime
        em.getTransaction().commit();
    }
}
```

---

## 3. Maven - Gestion de Projet

### C'est quoi Maven ?

Maven est un **outil de build** (construction) qui :
- Gère les dépendances (bibliothèques)
- Compile le code
- Crée le fichier WAR (package de l'application)

### Le fichier pom.xml

C'est le **fichier de configuration** de Maven.

**Structure simplifiée :**
```xml
<project>
    <!-- Informations du projet -->
    <groupId>com.iua</groupId>
    <artifactId>GestionEtudiants</artifactId>
    <version>1.0</version>
    <packaging>war</packaging>  ← Type de package (war pour web)

    <!-- Dépendances (bibliothèques) -->
    <dependencies>
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-core</artifactId>
            <version>6.2.7</version>
        </dependency>
    </dependencies>
</project>
```

### Commandes Maven

```bash
mvn clean           # Nettoyer le projet
mvn compile         # Compiler le code Java
mvn package         # Créer le fichier WAR
mvn clean package   # Nettoyer puis créer le WAR
```

---

## 4. JPA & Hibernate - Base de Données

### JPA (Java Persistence API)

**C'est quoi ?**
- Une **spécification** (ensemble de règles) pour la persistance des données
- Permet de manipuler la BDD avec des objets Java au lieu de SQL

**Avantages :**
- Plus besoin d'écrire du SQL à la main
- Code plus simple et sûr
- Protection contre les injections SQL

### Hibernate

**C'est quoi ?**
- L'**implémentation** de JPA la plus populaire
- Fait le travail réel (génère et exécute les requêtes SQL)

### ORM (Object-Relational Mapping)

**Concept :** Transformer des objets Java en lignes de base de données

```
Classe Java                    Table MySQL
┌─────────────────┐           ┌──────────────────┐
│ Etudiant        │    ←→     │ etudiant         │
│ - id            │           │ - id             │
│ - nom           │           │ - nom            │
│ - prenom        │           │ - prenom         │
└─────────────────┘           └──────────────────┘
```

### persistence.xml

Fichier de configuration JPA qui contient :
- URL de connexion à la base de données
- Login / mot de passe
- Classes entités à gérer

**Exemple :**
```xml
<persistence-unit name="GestionEtudiantsPU">
    <properties>
        <!-- Connexion MySQL -->
        <property name="jakarta.persistence.jdbc.url"
                  value="jdbc:mysql://localhost:3306/gestion_etudiants"/>
        <property name="jakarta.persistence.jdbc.user" value="root"/>
        <property name="jakarta.persistence.jdbc.password" value=""/>

        <!-- Hibernate auto-crée les tables -->
        <property name="hibernate.hbm2ddl.auto" value="update"/>
    </properties>
</persistence-unit>
```

---

## 5. Servlets - Contrôleurs Web

### C'est quoi un Servlet ?

Un Servlet est une **classe Java qui gère les requêtes HTTP**.

### Cycle de vie

```
1. L'utilisateur fait une action (clic bouton, ouvre une page)
2. Le navigateur envoie une requête HTTP
3. Tomcat reçoit la requête
4. Tomcat appelle le Servlet correspondant
5. Le Servlet traite la demande
6. Le Servlet renvoie une réponse (page HTML/JSP)
```

### Méthodes HTTP

| Méthode | Usage | Exemple |
|---------|-------|---------|
| **GET** | Récupérer des données | Afficher la liste des étudiants |
| **POST** | Envoyer des données | Soumettre un formulaire d'ajout |
| **PUT** | Modifier des données | Mettre à jour un étudiant |
| **DELETE** | Supprimer des données | Supprimer un étudiant |

### Mapping URL

L'annotation `@WebServlet` indique l'URL du servlet :

```java
@WebServlet("/etudiants")  // ← URL : http://localhost:8080/GestionEtudiants/etudiants
public class EtudiantServlet extends HttpServlet {
    // ...
}
```

---

## 6. JSP & JSTL - Pages Dynamiques

### JSP (Java Server Pages)

**C'est quoi ?**
- Fichiers qui mélangent **HTML** et **code Java**
- Permettent d'afficher des données dynamiques

**Exemple :**
```jsp
<h1>Bienvenue ${utilisateur.nom}</h1>
<!-- ${...} affiche une variable -->
```

### JSTL (JSP Standard Tag Library)

**C'est quoi ?**
- Bibliothèque de balises pour simplifier le code JSP
- Évite d'écrire du Java dans les JSP

**Balises principales :**

| Balise | Usage | Exemple |
|--------|-------|---------|
| `<c:forEach>` | Boucle | Afficher une liste |
| `<c:if>` | Condition | Afficher si... |
| `<c:out>` | Afficher | Afficher une valeur |
| `<c:url>` | Créer URL | Créer un lien |

**Exemple complet :**
```jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<table>
    <c:forEach items="${etudiants}" var="etudiant">
        <tr>
            <td>${etudiant.nom}</td>
            <td>${etudiant.prenom}</td>
            <td>
                <a href="etudiants?action=modifier&id=${etudiant.id}">Modifier</a>
            </td>
        </tr>
    </c:forEach>
</table>
```

---

## 7. REST & JSON - API Web

### REST (REpresentational State Transfer)

**C'est quoi ?**
- Un style d'architecture pour créer des **API web**
- Utilise les méthodes HTTP standard (GET, POST, PUT, DELETE)
- Renvoie des données au format JSON

**Exemple d'API REST :**
```
GET  /api/etudiants           → Liste de tous les étudiants
GET  /api/etudiants/5         → Étudiant avec id=5
POST /api/etudiants           → Créer un nouvel étudiant
PUT  /api/etudiants/5         → Modifier l'étudiant 5
DELETE /api/etudiants/5       → Supprimer l'étudiant 5
```

### JSON (JavaScript Object Notation)

**C'est quoi ?**
- Format de données léger et facile à lire
- Utilisé pour échanger des données

**Exemple JSON :**
```json
{
  "id": 1,
  "nom": "Kouassi",
  "prenom": "Aya",
  "email": "aya@iua.ci",
  "notes": [
    {
      "matiere": "Java EE",
      "valeur": 16.5,
      "coefficient": 3
    },
    {
      "matiere": "Base de données",
      "valeur": 14.0,
      "coefficient": 2
    }
  ]
}
```

### JAX-RS & Jersey

**JAX-RS** : Spécification Java pour créer des services REST
**Jersey** : Implémentation de JAX-RS

**Exemple de service REST :**
```java
@Path("/api/etudiants")  // ← URL de base
public class EtudiantRestService {

    @GET  // ← Méthode HTTP GET
    @Produces(MediaType.APPLICATION_JSON)  // ← Renvoie du JSON
    public List<Etudiant> listerTous() {
        return etudiantService.listerTous();
    }

    @GET
    @Path("/{id}/notes")  // ← URL : /api/etudiants/5/notes
    @Produces(MediaType.APPLICATION_JSON)
    public List<Note> listerNotes(@PathParam("id") Long id) {
        return noteService.trouverParEtudiant(id);
    }
}
```

---

## 8. Tomcat - Serveur d'Applications

### C'est quoi Tomcat ?

**Apache Tomcat** est un **serveur d'applications Java** qui :
- Exécute les applications web Java
- Gère les Servlets et JSP
- Écoute sur le port 8080 (par défaut)

### Comment ça marche ?

```
1. Vous créez un fichier WAR (Web Application Archive)
2. Vous copiez le WAR dans le dossier webapps/ de Tomcat
3. Tomcat détecte le WAR et le déploie automatiquement
4. L'application est accessible via http://localhost:8080/NomApplication
```

### Structure de Tomcat

```
apache-tomcat-10.1.50/
├── bin/           ← Scripts de démarrage/arrêt
│   ├── startup.bat    (Windows)
│   └── shutdown.bat   (Windows)
├── conf/          ← Fichiers de configuration
├── logs/          ← Logs du serveur
├── webapps/       ← Applications web (déploiement)
│   └── GestionEtudiants.war  ← Notre application
├── work/          ← Fichiers temporaires
└── temp/          ← Fichiers temporaires
```

### Commandes Tomcat

**Démarrer :**
```bash
C:\Users\Administrator\apache-tomcat-10.1.50\bin\startup.bat
```

**Arrêter :**
```bash
C:\Users\Administrator\apache-tomcat-10.1.50\bin\shutdown.bat
```

**Accéder à l'application :**
```
http://localhost:8080/GestionEtudiants/
```

---

## 9. Structure des Dossiers

### Structure Maven Standard

```
GestionEtudiant/
├── src/
│   ├── main/
│   │   ├── java/                    ← Code Java
│   │   │   └── com/iua/gestionetudiants/
│   │   │       ├── model/           ← Entités JPA (Etudiant, Note)
│   │   │       ├── dao/             ← Classes DAO
│   │   │       ├── service/         ← Services métier
│   │   │       ├── controller/      ← Servlets
│   │   │       └── rest/            ← Services REST
│   │   ├── resources/               ← Fichiers de configuration
│   │   │   └── META-INF/
│   │   │       └── persistence.xml  ← Config JPA
│   │   └── webapp/                  ← Fichiers web (JSP, CSS, etc.)
│   │       ├── WEB-INF/
│   │       │   └── web.xml          ← Config Servlet
│   │       ├── css/
│   │       │   └── style.css
│   │       ├── index.jsp
│   │       ├── liste-etudiants.jsp
│   │       └── ...
│   └── test/                        ← Tests (pas utilisé ici)
├── target/                          ← Fichiers compilés (généré par Maven)
│   └── GestionEtudiants.war         ← Fichier final à déployer
├── pom.xml                          ← Configuration Maven
├── plan.md
├── etat_avancement.md
└── technologies_explications.md
```

---

## 10. Flux de Données

### Scénario : Afficher la liste des étudiants

```
┌──────────────┐
│ Utilisateur  │
└──────┬───────┘
       │ 1. Ouvre http://localhost:8080/GestionEtudiants/etudiants
       ↓
┌──────────────────────┐
│ Navigateur (Browser) │
└──────┬───────────────┘
       │ 2. Requête HTTP GET /etudiants
       ↓
┌──────────────────┐
│ Tomcat (Serveur) │
└──────┬───────────┘
       │ 3. Route vers EtudiantServlet
       ↓
┌────────────────────────┐
│ EtudiantServlet        │ (Couche Contrôleur)
│ - doGet()              │
└──────┬─────────────────┘
       │ 4. Appelle etudiantService.listerTous()
       ↓
┌────────────────────────┐
│ EtudiantService        │ (Couche Métier)
│ - listerTous()         │
└──────┬─────────────────┘
       │ 5. Appelle etudiantDAO.listerTous()
       ↓
┌────────────────────────┐
│ EtudiantDAO            │ (Couche Persistance)
│ - listerTous()         │
└──────┬─────────────────┘
       │ 6. Requête SQL via JPA
       ↓
┌────────────────────────┐
│ MySQL (Base données)   │
└──────┬─────────────────┘
       │ 7. Renvoie les données
       ↓
┌────────────────────────┐
│ EtudiantDAO            │
│ → List<Etudiant>       │
└──────┬─────────────────┘
       │ 8. Renvoie au Service
       ↓
┌────────────────────────┐
│ EtudiantService        │
│ → List<Etudiant>       │
└──────┬─────────────────┘
       │ 9. Renvoie au Servlet
       ↓
┌────────────────────────┐
│ EtudiantServlet        │
│ - setAttribute()       │
│ - forward()            │
└──────┬─────────────────┘
       │ 10. Forward vers liste-etudiants.jsp
       ↓
┌────────────────────────┐
│ liste-etudiants.jsp    │ (Couche Présentation)
│ <c:forEach...>         │
└──────┬─────────────────┘
       │ 11. Génère HTML
       ↓
┌────────────────────────┐
│ Navigateur             │
│ Affiche la page HTML   │
└────────────────────────┘
```

---

## 11. Glossaire des Termes

| Terme | Signification |
|-------|---------------|
| **API** | Application Programming Interface - Interface de programmation |
| **DAO** | Data Access Object - Objet d'accès aux données |
| **Entity** | Entité - Classe qui représente une table |
| **HTTP** | HyperText Transfer Protocol - Protocole web |
| **JAR** | Java Archive - Archive de classes Java |
| **JPA** | Java Persistence API - API de persistance |
| **JSON** | JavaScript Object Notation - Format de données |
| **JSP** | Java Server Pages - Pages serveur Java |
| **JSTL** | JSP Standard Tag Library - Bibliothèque de balises |
| **MVC** | Model-View-Controller - Modèle-Vue-Contrôleur |
| **ORM** | Object-Relational Mapping - Mapping objet-relationnel |
| **POJO** | Plain Old Java Object - Simple objet Java |
| **REST** | REpresentational State Transfer - Style d'architecture |
| **Servlet** | Programme Java côté serveur |
| **WAR** | Web Application Archive - Archive d'application web |

---

## 12. Ressources et Aide

### Documentation officielle

- **Java SE** : https://docs.oracle.com/en/java/javase/17/
- **Jakarta EE** : https://jakarta.ee/
- **Hibernate** : https://hibernate.org/orm/documentation/
- **Maven** : https://maven.apache.org/guides/
- **Tomcat** : https://tomcat.apache.org/tomcat-10.1-doc/

### En cas de problème

1. **Vérifier les logs Tomcat** : `apache-tomcat-10.1.50/logs/catalina.out`
2. **Vérifier que MySQL fonctionne** : Démarrer WAMP
3. **Vérifier les dépendances Maven** : `mvn dependency:tree`
4. **Nettoyer et recompiler** : `mvn clean package`

---

**Fin du document**

Ce guide devrait vous aider à comprendre tous les éléments utilisés dans le projet Java EE !
