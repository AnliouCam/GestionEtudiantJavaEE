<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ajouter une Note</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>Ajouter une Note</h1>
            <nav>
                <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">Accueil</a>
                <a href="${pageContext.request.contextPath}/etudiants" class="btn btn-info">Liste des etudiants</a>
                <a href="${pageContext.request.contextPath}/etudiants?action=detail&id=${etudiant.id}" class="btn btn-secondary">Retour aux details</a>
            </nav>
        </header>

        <main>
            <div class="info-box">
                <h3>Étudiant : ${etudiant.prenom} ${etudiant.nom}</h3>
                <p><strong>Matricule :</strong> ${etudiant.matricule}</p>
            </div>

            <div class="form-container">
                <form action="${pageContext.request.contextPath}/notes" method="post" class="form">
                    <input type="hidden" name="action" value="creer">
                    <input type="hidden" name="etudiantId" value="${etudiant.id}">

                    <div class="form-group">
                        <label for="matiere">Matière <span class="required">*</span></label>
                        <input type="text"
                               id="matiere"
                               name="matiere"
                               required
                               placeholder="Ex: Mathématiques, Java EE, Bases de Données..."
                               class="form-control">
                        <small class="form-help">Nom de la matière pour cette note</small>
                    </div>

                    <div class="form-group">
                        <label for="valeur">Note sur 20 <span class="required">*</span></label>
                        <input type="number"
                               id="valeur"
                               name="valeur"
                               required
                               min="0"
                               max="20"
                               step="0.01"
                               placeholder="Ex: 15.5"
                               class="form-control">
                        <small class="form-help">Note entre 0 et 20</small>
                    </div>

                    <div class="form-group">
                        <label for="coefficient">Coefficient <span class="required">*</span></label>
                        <input type="number"
                               id="coefficient"
                               name="coefficient"
                               required
                               min="1"
                               max="10"
                               value="1"
                               class="form-control">
                        <small class="form-help">Coefficient de pondération (généralement entre 1 et 5)</small>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">Enregistrer la note</button>
                        <a href="${pageContext.request.contextPath}/etudiants?action=detail&id=${etudiant.id}" class="btn btn-secondary">Annuler</a>
                    </div>

                    <p class="form-note"><span class="required">*</span> Champs obligatoires</p>
                </form>
            </div>

            <div class="info-box">
                <h4>💡 Comment est calculée la moyenne ?</h4>
                <p><strong>Moyenne pondérée =</strong> (Note1 × Coef1 + Note2 × Coef2 + ...) / (Coef1 + Coef2 + ...)</p>
                <p><strong>Exemple :</strong></p>
                <ul>
                    <li>Java EE : 16/20 (coefficient 3) = 48 points</li>
                    <li>Bases de Données : 14/20 (coefficient 2) = 28 points</li>
                    <li>Anglais : 12/20 (coefficient 1) = 12 points</li>
                </ul>
                <p><strong>Moyenne =</strong> (48 + 28 + 12) / (3 + 2 + 1) = 88 / 6 = <strong>14,67/20</strong></p>
            </div>
        </main>

        <footer>
            <p>&copy; 2025 Institut Universitaire d'Abidjan</p>
        </footer>
    </div>
</body>
</html>
