# Student management microservices

Le `pom.xml` racine orchestre trois modules Maven, placés à côté de lui. Chaque service est une application Spring Boot indépendante avec son propre `pom.xml`, son dossier `src/main` et sa configuration :

```text
pom.xml
student-service/
  pom.xml
  src/main/java/tn/esprit/services/student/
  src/main/resources/application.properties
academic-service/
  pom.xml
  src/main/java/tn/esprit/services/academic/
  src/main/resources/application.properties
enrollment-service/
  pom.xml
  src/main/java/tn/esprit/services/enrollment/
  src/main/resources/application.properties
```

`student-service` gère les étudiants; `academic-service` gère les départements et les cours; `enrollment-service` gère les inscriptions. Les services ont chacun leur base de données. Les inscriptions stockent `studentId` et `courseId` et vérifient ces identifiants par HTTP auprès des deux autres services.

## Démarrage

Prérequis : Java 17 et MySQL accessible sur `localhost:3306`. Les bases `studentsdb`, `academicdb` et `enrollmentsdb` sont créées automatiquement si le compte MySQL configuré en a le droit.

Lance chaque commande dans son propre terminal PowerShell depuis le dossier racine :

```powershell
.\mvnw.cmd -pl student-service spring-boot:run
```

```powershell
.\mvnw.cmd -pl academic-service spring-boot:run
```

```powershell
.\mvnw.cmd -pl enrollment-service spring-boot:run
```

Les identifiants MySQL sont définis dans le fichier `application.properties` de chaque service. `DB_HOST` permet de changer l’hôte. Les ports par défaut sont `8081`, `8082` et `8083`; ils peuvent être remplacés par `STUDENT_PORT`, `ACADEMIC_PORT` et `ENROLLMENT_PORT`. L’URL des deux services appelés par `enrollment-service` se règle avec `STUDENT_SERVICE_URL` et `ACADEMIC_SERVICE_URL`.

Pour construire les trois JAR depuis la racine :

```powershell
.\mvnw.cmd package
```

Pour construire ou lancer un seul service, ajoute `-pl student-service`, `-pl academic-service` ou `-pl enrollment-service`.

## API

| Service | URL de base | Ressources |
| --- | --- | --- |
| Étudiants | `http://localhost:8081` | `/students` |
| Académique | `http://localhost:8082` | `/departments`, `/courses` |
| Inscriptions | `http://localhost:8083` | `/enrollments` |

La documentation interactive Swagger UI est disponible à `/docs` pour chaque service :

- Étudiants : `http://localhost:8081/docs`
- Académique : `http://localhost:8082/docs`
- Inscriptions : `http://localhost:8083/docs`

Chaque ressource expose `GET /`, `GET /{id}`, `POST /`, `PUT /{id}` et `DELETE /{id}`. Exemple de création d’inscription :

```json
{
  "enrollmentDate": "2026-11-17",
  "grade": null,
  "status": "ACTIVE",
  "studentId": 1,
  "courseId": 1
}
```
