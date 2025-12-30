<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Erreur - Gestion des Étudiants</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>Une erreur est survenue</h1>
            <nav>
                <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Retour a l'accueil</a>
                <a href="${pageContext.request.contextPath}/etudiants" class="btn btn-info">Liste des etudiants</a>
            </nav>
        </header>

        <main>
            <div class="error-container">
                <h2>Oups ! Quelque chose s'est mal passe</h2>

                <div class="error-message">
                    <c:choose>
                        <c:when test="${not empty erreur}">
                            <p><strong>Message d'erreur :</strong></p>
                            <p class="error-text">${erreur}</p>
                        </c:when>
                        <c:when test="${not empty pageContext.exception}">
                            <p><strong>Message d'erreur :</strong></p>
                            <p class="error-text">${pageContext.exception.message}</p>
                        </c:when>
                        <c:otherwise>
                            <p class="error-text">Une erreur inattendue s'est produite. Veuillez réessayer.</p>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="error-actions">
                    <a href="javascript:history.back()" class="btn btn-secondary">
                        Retour a la page precedente
                    </a>
                    <a href="${pageContext.request.contextPath}/" class="btn btn-primary">
                        Retour a l'accueil
                    </a>
                </div>

                <div class="error-help">
                    <h3>Que faire ?</h3>
                    <ul>
                        <li>Vérifiez que toutes les informations saisies sont correctes</li>
                        <li>Assurez-vous que l'étudiant ou la note existe toujours</li>
                        <li>Vérifiez que les notes sont entre 0 et 20</li>
                        <li>Vérifiez que le matricule est unique</li>
                        <li>Si le problème persiste, contactez l'administrateur</li>
                    </ul>
                </div>

                <!-- Affichage détaillé pour le développement (à supprimer en production) -->
                <c:if test="${not empty pageContext.exception && param.debug eq 'true'}">
                    <div class="error-debug">
                        <h4>Détails techniques (mode debug) :</h4>
                        <p><strong>Type :</strong> ${pageContext.exception.class.name}</p>
                        <p><strong>Message :</strong> ${pageContext.exception.message}</p>
                        <details>
                            <summary>Stack trace</summary>
                            <pre><c:forEach items="${pageContext.exception.stackTrace}" var="trace">${trace}
</c:forEach></pre>
                        </details>
                    </div>
                </c:if>
            </div>
        </main>

        <footer>
            <p>&copy; 2025 Institut Universitaire d'Abidjan</p>
        </footer>
    </div>
</body>
</html>
