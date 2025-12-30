<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestion des Étudiants - Accueil</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>Gestion des Etudiants et des Notes</h1>
            <p class="subtitle">Institut Universitaire d'Abidjan - MASTER 1 MIAGE/GI</p>
        </header>

        <main>
            <div class="welcome-card">
                <h2>Bienvenue dans l'application de gestion</h2>
                <p>Cette application permet de gerer les etudiants et leurs notes de maniere simple et efficace.</p>
            </div>

            <div class="cards-grid">
                <div class="card">
                    <h3>Gestion des Etudiants</h3>
                    <p>Ajouter, modifier, supprimer et consulter les etudiants</p>
                    <a href="${pageContext.request.contextPath}/etudiants" class="btn btn-primary">
                        Acceder aux etudiants
                    </a>
                </div>

                <div class="card">
                    <h3>Gestion des Notes</h3>
                    <p>Ajouter des notes et calculer les moyennes automatiquement</p>
                    <a href="${pageContext.request.contextPath}/etudiants" class="btn btn-secondary">
                        Gerer les notes
                    </a>
                </div>

                <div class="card">
                    <h3>API REST</h3>
                    <p>Acceder aux donnees au format JSON</p>
                    <a href="${pageContext.request.contextPath}/api/etudiants" class="btn btn-info" target="_blank">
                        Voir l'API JSON
                    </a>
                </div>
            </div>

            <div class="features">
                <h3>Fonctionnalites</h3>
                <ul>
                    <li>Ajouter un etudiant</li>
                    <li>Modifier les informations d'un etudiant</li>
                    <li>Supprimer un etudiant</li>
                    <li>Afficher la liste des etudiants</li>
                    <li>Ajouter des notes a un etudiant</li>
                    <li>Calculer la moyenne ponderee automatiquement</li>
                    <li>API REST pour acceder aux donnees en JSON</li>
                </ul>
            </div>
        </main>

        <footer>
            <p>&copy; 2025 Institut Universitaire d'Abidjan | Projet Java EE</p>
        </footer>
    </div>
</body>
</html>
