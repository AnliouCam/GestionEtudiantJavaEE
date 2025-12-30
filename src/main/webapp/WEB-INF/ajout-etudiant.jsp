<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ajouter un Étudiant</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>Ajouter un Etudiant</h1>
            <nav>
                <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">Accueil</a>
                <a href="${pageContext.request.contextPath}/etudiants" class="btn btn-info">Liste des etudiants</a>
            </nav>
        </header>

        <main>
            <div class="form-container">
                <form action="${pageContext.request.contextPath}/etudiants" method="post" class="form">
                    <input type="hidden" name="action" value="creer">

                    <div class="form-group">
                        <label for="matricule">Matricule <span class="required">*</span></label>
                        <input type="text"
                               id="matricule"
                               name="matricule"
                               required
                               placeholder="Ex: MAT2025001"
                               class="form-control">
                    </div>

                    <div class="form-group">
                        <label for="nom">Nom <span class="required">*</span></label>
                        <input type="text"
                               id="nom"
                               name="nom"
                               required
                               placeholder="Nom de famille"
                               class="form-control">
                    </div>

                    <div class="form-group">
                        <label for="prenom">Prénom <span class="required">*</span></label>
                        <input type="text"
                               id="prenom"
                               name="prenom"
                               required
                               placeholder="Prénom"
                               class="form-control">
                    </div>

                    <div class="form-group">
                        <label for="email">Email</label>
                        <input type="email"
                               id="email"
                               name="email"
                               placeholder="etudiant@iua.ci"
                               class="form-control">
                    </div>

                    <div class="form-group">
                        <label for="dateNaissance">Date de naissance</label>
                        <input type="date"
                               id="dateNaissance"
                               name="dateNaissance"
                               class="form-control">
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">Enregistrer</button>
                        <a href="${pageContext.request.contextPath}/etudiants" class="btn btn-secondary">Annuler</a>
                    </div>

                    <p class="form-note"><span class="required">*</span> Champs obligatoires</p>
                </form>
            </div>
        </main>

        <footer>
            <p>&copy; 2025 Institut Universitaire d'Abidjan</p>
        </footer>
    </div>
</body>
</html>
