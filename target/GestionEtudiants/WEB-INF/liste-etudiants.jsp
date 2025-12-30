<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des Étudiants</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>Liste des Etudiants</h1>
            <nav>
                <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">Accueil</a>
                <a href="${pageContext.request.contextPath}/etudiants?action=ajouter" class="btn btn-primary">
                    Ajouter un etudiant
                </a>
            </nav>
        </header>

        <main>
            <!-- Message de succès -->
            <c:if test="${not empty param.message}">
                <div class="alert alert-success">
                    ${param.message}
                </div>
            </c:if>

            <!-- Tableau des étudiants -->
            <div class="table-container">
                <c:choose>
                    <c:when test="${empty etudiants}">
                        <div class="empty-state">
                            <p>Aucun étudiant enregistré pour le moment.</p>
                            <a href="${pageContext.request.contextPath}/etudiants?action=ajouter" class="btn btn-primary">
                                Ajouter le premier étudiant
                            </a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Matricule</th>
                                    <th>Nom</th>
                                    <th>Prénom</th>
                                    <th>Email</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${etudiants}" var="etudiant">
                                    <tr>
                                        <td><strong>${etudiant.matricule}</strong></td>
                                        <td>${etudiant.nom}</td>
                                        <td>${etudiant.prenom}</td>
                                        <td>${etudiant.email}</td>
                                        <td class="actions">
                                            <a href="${pageContext.request.contextPath}/etudiants?action=detail&id=${etudiant.id}"
                                               class="btn btn-sm btn-info" title="Details">
                                                Voir
                                            </a>
                                            <a href="${pageContext.request.contextPath}/etudiants?action=modifier&id=${etudiant.id}"
                                               class="btn btn-sm btn-warning" title="Modifier">
                                                Modifier
                                            </a>
                                            <a href="${pageContext.request.contextPath}/etudiants?action=supprimer&id=${etudiant.id}"
                                               class="btn btn-sm btn-danger"
                                               onclick="return confirm('Etes-vous sur de vouloir supprimer cet etudiant ?');"
                                               title="Supprimer">
                                                Supprimer
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>

                        <div class="info-box">
                            <p><strong>Total :</strong> ${etudiants.size()} etudiant(s)</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </main>

        <footer>
            <p>&copy; 2025 Institut Universitaire d'Abidjan</p>
        </footer>
    </div>
</body>
</html>
