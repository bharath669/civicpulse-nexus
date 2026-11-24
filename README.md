# CivicPulse Nexus — Milestone 1
Smart Governance Platform for Administrative Operations with Citizen Service Assistance

Milestone 1 includes Citizen Registration and Grievance Management as two independent Spring Boot services, plus a React/Vite admin UI.

## Technology
- Java 25, Spring Boot 4.1.x, Spring Data JPA, Bean Validation
- MySQL 8.x (one database per microservice)
- React 19 + Vite
- Swagger / OpenAPI

## Architecture and ports
- `citizen-service`: `http://localhost:8081` — owns `civic_citizen_db`
- `grievance-service`: `http://localhost:8082` — owns `civic_grievance_db`; calls Citizen Service over HTTP
- `frontend`: `http://localhost:5173` — Vite proxies `/api/citizens` to 8081 and `/api/grievances`, `/api/dashboard` to 8082

The Grievance Service does not connect to the citizen database. It validates citizen IDs by calling Citizen Service.

## Prerequisites
Install JDK 25, Maven 3.9+, Node.js 22/24 LTS, MySQL 8, Git and VS Code. Install the Java Extension Pack, Spring Boot Extension Pack, REST Client, ESLint and Prettier extensions.

Verify:
```powershell
java -version
node -v
npm -v
mysql --version
git --version
```

## 1. Create the databases
Open MySQL Workbench or the MySQL command line as an administrative user and run `database/setup.sql`.
The development credentials are `civic_user` / `civic_pass`. Change these for any shared or deployed environment.

## 2. Configure database credentials
The defaults are in each service's `src/main/resources/application.yml`.
For safer local configuration, set environment variables before starting:
```powershell
$env:DB_USERNAME="civic_user"
$env:DB_PASSWORD="civic_pass"
```
Do not commit real passwords or production secrets.

## 3. Start the backend
Open three VS Code terminals from the repository root.

Terminal 1:
```powershell
cd citizen-service
.\mvnw.cmd spring-boot:run
```
Terminal 2:
```powershell
cd grievance-service
.\mvnw.cmd spring-boot:run
```
Start Citizen Service first because Grievance Service calls it.

Swagger:
- http://localhost:8081/swagger-ui.html
- http://localhost:8082/swagger-ui.html

Health:
- http://localhost:8081/actuator/health
- http://localhost:8082/actuator/health

## 4. Start the frontend
Terminal 3:
```powershell
cd frontend
npm install
npm run dev
```
Open http://localhost:5173.

## Main APIs
### Citizen Service
- `POST /api/citizens`
- `GET /api/citizens`
- `GET /api/citizens/{id}`
- `GET /api/citizens/count`

### Grievance Service
- `POST /api/grievances`
- `GET /api/grievances`
- `GET /api/grievances/{id}`
- `GET /api/grievances/track/{number}`
- `GET /api/grievances/{id}/history`
- `POST /api/grievances/{id}/assign`
- `PATCH /api/grievances/{id}/status`
- `POST /api/grievances/{id}/escalate`
- `POST /api/grievances/{id}/simulate-breach` (local demo only)
- `GET /api/grievances/meta`
- `GET /api/dashboard/summary`

## Milestone 1 rules
- Citizen phone: Indian 10-digit mobile beginning 6–9.
- Aadhaar: exactly 12 digits; never returned in full.
- Ward: 1–200.
- Citizen email, phone and Aadhaar are unique.
- Grievance registration validates citizen existence through HTTP.
- Category automatically selects the department and SLA.
- Grievance number: `GRV-YYYY-NNNNNN`.
- Assignment is allowed only while REGISTERED or ASSIGNED and requires an officer.
- Allowed status transitions are enforced by the backend.
- Resolution notes must contain at least 10 non-space characters.
- Rejection requires a reason.
- SLA monitor runs every 60 seconds; maximum escalation level is 3.

## Demo flow
1. Register a citizen with invalid phone/Aadhaar/ward and show validation errors.
2. Register a valid citizen.
3. Try registering the same email again and show HTTP 409.
4. Register a grievance for an unknown citizen and show HTTP 404.
5. Register WATER_SUPPLY / HIGH and verify Water Dept and 48-hour SLA.
6. Assign an officer.
7. Start work.
8. Use the local-only Simulate SLA Breach button; wait for the scheduler (up to 60 seconds).
9. Manually escalate once.
10. Try resolving with `done` and show HTTP 422.
11. Resolve with at least 10 characters, then close.
12. Show dashboard counters and the audit timeline.

## Tests
Run each backend test suite:
```powershell
cd citizen-service
.\mvnw.cmd test
cd ..\grievance-service
.\mvnw.cmd test
```

## Git submission
See `docs/GIT_GUIDE.md` for the exact step-by-step GitHub workflow.
