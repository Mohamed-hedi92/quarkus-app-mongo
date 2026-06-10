# Movie Collection App

A full-stack movie management application built with **Quarkus**, **MongoDB**, and **React**, containerized with **Docker** and orchestrated with **Kubernetes**.

## Architecture

```
Frontend (React)  -->  Backend (Quarkus)  -->  MongoDB
  :80 (Nginx)           :8081 (REST)           :27017
```

## Tech Stack

### Backend
- **Quarkus 3.10** - Supersonic Subatomic Java Framework
- **MongoDB Panache** - Active Record Pattern for MongoDB
- **RESTEasy Reactive** - Reactive REST endpoints
- **SmallRye OpenAPI** - API documentation with Swagger UI

### Frontend
- **React 19** + **TypeScript** - Type-safe UI components
- **Vite 6** - Fast build tool with HMR

### DevOps
- **Docker** - Multi-stage builds
- **Kubernetes** - Manifests with probes and secrets
- **GitHub Actions** - CI pipeline with MongoDB service container
- **Nginx** - Reverse proxy for API routing

## Features

- CRUD Operations - Create, Read, Update, Delete movies
- Search - Filter movies by title or category
- Responsive Design - Works on desktop and mobile
- Health Probes - Kubernetes readiness and liveness checks
- Environment Config - Configurable via environment variables
- CI/CD - Automated testing and build validation

## Quick Start

### Prerequisites
- Java 17+, Maven 3.9+, Node.js 20+
- Docker and Docker Compose
- kubectl and minikube (for K8s)

### Option 1: Local Development

**1. Start MongoDB:**
```bash
docker run -d --name mongodb -p 27017:27017 -e MONGO_INITDB_ROOT_USERNAME=admin -e MONGO_INITDB_ROOT_PASSWORD=password mongo:7
```

**2. Start Backend:**
```bash
mvn quarkus:dev
```

**3. Start Frontend:**
```bash
cd frontend
npm install
npm run dev
```

### Option 2: Docker Compose

```bash
docker-compose up --build
```

### Option 3: Kubernetes (Minikube)

```bash
minikube start
minikube docker-env | Invoke-Expression
docker build -t quarkus-mongo-backend:latest .
docker build -t quarkus-mongo-frontend:latest ./frontend
kubectl apply -f k8s/
minikube service frontend -n movie-app
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /movies | Get all movies |
| GET | /movies/{id} | Get movie by ID |
| GET | /movies/title/{title} | Search by title |
| GET | /movies/category/{category} | Filter by category |
| POST | /movies | Create a new movie |
| PUT | /movies/{id} | Update a movie |
| DELETE | /movies/{id} | Delete a movie |

## Project Structure

```
quarkus-app-mongo/
  src/main/java/org/acme/    - Backend (Quarkus + MongoDB Panache)
  frontend/src/              - Frontend (React + TypeScript)
  k8s/                       - Kubernetes manifests
  .github/workflows/         - CI/CD pipeline
```

## CI/CD Pipeline

| Job | Description |
|-----|-------------|
| Backend | JDK 17, MongoDB Service, mvn test, mvn package |
| Frontend | Node 20, tsc check, npm run build |
| Docker | Validate both images build |

## Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| MONGO_URI | mongodb://admin:password@localhost:27017/... | MongoDB connection string |
| MONGO_DB | my-mongodb | Database name |
| VITE_API_URL | http://localhost:8081/movies | Backend API URL |

## Running Tests

```bash
mvn test
cd frontend && npx tsc --noEmit
```
