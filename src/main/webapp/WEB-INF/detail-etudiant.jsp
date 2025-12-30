<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Détails de l'Étudiant</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>Details de l'Etudiant</h1>
            <nav>
                <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">Accueil</a>
                <a href="${pageContext.request.contextPath}/etudiants" class="btn btn-info">Liste des etudiants</a>
            </nav>
        </header>

        <main>
            <!-- Message de succès -->
            <c:if test="${not empty param.message}">
                <div class="alert alert-success">
                    ${param.message}
                </div>
            </c:if>

            <!-- Informations de l'étudiant -->
            <div class="detail-card">
                <div class="detail-header">
                    <h2>${etudiant.prenom} ${etudiant.nom}</h2>
                    <div class="detail-actions">
                        <a href="${pageContext.request.contextPath}/etudiants?action=modifier&id=${etudiant.id}"
                           class="btn btn-warning">
                            Modifier
                        </a>
                        <a href="${pageContext.request.contextPath}/etudiants?action=supprimer&id=${etudiant.id}"
                           class="btn btn-danger"
                           onclick="return confirm('Etes-vous sur de vouloir supprimer cet etudiant et toutes ses notes ?');">
                            Supprimer
                        </a>
                    </div>
                </div>

                <div class="detail-body">
                    <div class="info-row">
                        <span class="info-label">Matricule :</span>
                        <span class="info-value"><strong>${etudiant.matricule}</strong></span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">Nom :</span>
                        <span class="info-value">${etudiant.nom}</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">Prénom :</span>
                        <span class="info-value">${etudiant.prenom}</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">Email :</span>
                        <span class="info-value">${etudiant.email}</span>
                    </div>
                    <c:if test="${not empty etudiant.dateNaissance}">
                        <div class="info-row">
                            <span class="info-label">Date de naissance :</span>
                            <span class="info-value">${etudiant.dateNaissance}</span>
                        </div>
                    </c:if>
                </div>
            </div>

            <!-- Notes de l'étudiant -->
            <div class="notes-section">
                <div class="section-header">
                    <h3>Notes</h3>
                    <a href="${pageContext.request.contextPath}/notes?action=ajouter&etudiantId=${etudiant.id}"
                       class="btn btn-primary">
                        Ajouter une note
                    </a>
                </div>

                <c:choose>
                    <c:when test="${empty etudiant.notes}">
                        <div class="empty-state">
                            <p>Aucune note enregistrée pour cet étudiant.</p>
                            <a href="${pageContext.request.contextPath}/notes?action=ajouter&etudiantId=${etudiant.id}"
                               class="btn btn-primary">
                                Ajouter la première note
                            </a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Matière</th>
                                    <th>Note / 20</th>
                                    <th>Coefficient</th>
                                    <th>Points</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${etudiant.notes}" var="note">
                                    <tr>
                                        <td><strong>${note.matiere}</strong></td>
                                        <td class="text-center">
                                            <span class="note-value">${note.valeur}</span>
                                        </td>
                                        <td class="text-center">${note.coefficient}</td>
                                        <td class="text-center">
                                            <fmt:formatNumber value="${note.valeur * note.coefficient}" maxFractionDigits="2"/>
                                        </td>
                                        <td class="actions">
                                            <a href="${pageContext.request.contextPath}/notes?action=supprimer&id=${note.id}&etudiantId=${etudiant.id}"
                                               class="btn btn-sm btn-danger"
                                               onclick="return confirm('Supprimer cette note ?');"
                                               title="Supprimer">
                                                Supprimer
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>

                        <!-- Moyenne -->
                        <div class="moyenne-card">
                            <div class="moyenne-label">Moyenne Générale (pondérée)</div>
                            <div class="moyenne-value">
                                <c:choose>
                                    <c:when test="${moyenne >= 0}">
                                        <fmt:formatNumber value="${moyenne}" maxFractionDigits="2"/> / 20
                                    </c:when>
                                    <c:otherwise>
                                        -
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <c:if test="${moyenne >= 10}">
                                <div class="moyenne-status success">Admis</div>
                            </c:if>
                            <c:if test="${moyenne < 10 && moyenne >= 0}">
                                <div class="moyenne-status danger">Non admis</div>
                            </c:if>
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
