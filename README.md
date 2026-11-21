# Student management microservices

Le `pom.xml` racine orchestre cinq modules Maven, placés à côté de lui. Chaque application Spring Boot a son propre `pom.xml` et son dossier `src/main` :

```text
pom.xml
student-service/
  pom.xml
  src/main/java/tn/esprit/services/student/
  src/main/resources/application.yml
academic-service/
  pom.xml
  src/main/java/tn/esprit/services/academic/
  src/main/resources/application.yml
enrollment-service/
  pom.xml
  src/main/java/tn/esprit/services/enrollment/
  src/main/resources/application.yml
discovery-server/
  pom.xml
  src/main/java/tn/esprit/discovery/
api-gateway/
  pom.xml
  src/main/java/tn/esprit/gateway/
```

`student-service` gère les étudiants; `academic-service` gère les départements et les cours; `enrollment-service` gère les inscriptions. Les services ont chacun leur base de données. Les appels REST entre services utilisent OpenFeign avec Eureka : `student-service` valide `departmentId` auprès du service académique, et `enrollment-service` valide `studentId` et `courseId` auprès des services concernés.

`discovery-server` fournit Eureka. Les trois services métier et l’API Gateway s’y enregistrent; `enrollment-service` et la Gateway trouvent les services par leur nom. `api-gateway` expose les routes métier sur un point d’entrée unique.

## Démarrage

Prérequis : Java 17 et MySQL accessible sur `localhost:3306`. Les bases `studentsdb`, `academicdb` et `enrollmentsdb` sont créées automatiquement si le compte MySQL configuré en a le droit.

Lance chaque commande dans son propre terminal PowerShell depuis le dossier racine :

```powershell
.\mvnw.cmd -pl discovery-server spring-boot:run
```

```powershell
.\mvnw.cmd -pl student-service spring-boot:run
```

```powershell
.\mvnw.cmd -pl academic-service spring-boot:run
```

```powershell
.\mvnw.cmd -pl enrollment-service spring-boot:run
```

```powershell
.\mvnw.cmd -pl api-gateway spring-boot:run
```

Démarre d’abord Eureka, puis les services métier, puis la Gateway. Les identifiants MySQL sont définis dans le fichier `application.yml` de chaque service. `DB_HOST` permet de changer l’hôte. Les ports par défaut sont `8761` (Eureka), `8081` (étudiants), `8082` (académique), `8083` (inscriptions) et `8080` (Gateway).

Pour construire les cinq JAR depuis la racine :

```powershell
.\mvnw.cmd package
```

Pour construire ou lancer un seul module, indique son nom avec `-pl`, par exemple `-pl api-gateway` ou `-pl discovery-server`.

## API

Utilise la Gateway pour appeler les services métier :

| Ressource | URL |
| --- | --- |
| Étudiants | `http://localhost:8080/students` |
| Départements | `http://localhost:8080/departments` |
| Cours | `http://localhost:8080/courses` |
| Inscriptions | `http://localhost:8080/enrollments` |

Le tableau d’Eureka est sur `http://localhost:8761`.

La documentation interactive Swagger UI reste accessible directement sur chaque service métier :

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
