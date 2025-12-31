# Configuration du Projet - Gestion des Étudiants

## Configuration de la Base de Données

### 1. Fichier persistence.xml

Le fichier `persistence.xml` contient les credentials de votre base de données et n'est **PAS versionné** pour des raisons de sécurité.

**Pour configurer votre environnement :**

1. Copiez le fichier template :
   ```bash
   cp src/main/resources/META-INF/persistence.xml.template src/main/resources/META-INF/persistence.xml
   ```

2. Éditez `persistence.xml` et remplacez :
   - `YOUR_DATABASE_USER` par votre nom d'utilisateur MySQL
   - `YOUR_DATABASE_PASSWORD` par votre mot de passe MySQL

3. **NE JAMAIS** commiter le fichier `persistence.xml` réel

### 2. Script SQL

Le fichier `script.sql` contient le schéma de la base de données et des données de test. Il est ignoré par Git car il peut contenir des informations sensibles.

**Pour initialiser la base de données :**

```sql
-- Créez manuellement votre base de données ou utilisez votre propre script
CREATE DATABASE gestion_etudiants CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

## Fichiers Sensibles Non Versionnés

Les fichiers suivants sont exclus du versioning pour des raisons de sécurité :

- `persistence.xml` - Contient les credentials de base de données
- `script.sql` - Peut contenir des données sensibles
- Tous les fichiers `*.sql`
- Fichiers `.env` et `*.credentials`
- Le dossier `target/` (fichiers compilés)

## Notes Importantes

⚠️ **Avant de push :**
- Vérifiez que `persistence.xml` n'est pas dans votre commit
- Vérifiez que les fichiers de build (`target/`) ne sont pas inclus
- Assurez-vous qu'aucun fichier avec des credentials n'est versionné

## Support

Pour toute question sur la configuration, contactez l'équipe de développement.
