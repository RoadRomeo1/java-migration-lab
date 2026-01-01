# Docker Deployment Guide

This guide covers how to build and run the Java Migration Lab application using Docker and Docker Compose.

## Prerequisites

- Docker Desktop installed (Windows/Mac) or Docker Engine (Linux)
- Docker Compose (included with Docker Desktop)
- At least 4GB of RAM allocated to Docker

## Quick Start

### Option 1: Using Docker Compose (Recommended)

**Start the entire stack (app + PostgreSQL):**
```bash
cd docker
docker-compose up -d
```

**View logs:**
```bash
docker-compose logs -f app
```

**Stop the stack:**
```bash
docker-compose down
```

**Stop and remove volumes (clean slate):**
```bash
docker-compose down -v
```

### Option 2: Build and Run Docker Image Only

**Build the image:**
```bash
cd docker
docker build -t java-migration-lab:latest -f Dockerfile ..
```

**Run the container (with H2 in-memory database):**
```bash
docker run -p 8080:8080 --name java-app java-migration-lab:latest
```

## Docker Compose Services

### 1. **app** - Java Application
- **Port**: 8080
- **Database**: PostgreSQL (in Docker)
- **Profile**: docker
- **Features**:
  - Virtual Threads enabled
  - ZGC garbage collector
  - Health checks
  - Auto-restart

### 2. **postgres** - PostgreSQL Database
- **Port**: 5432
- **Database**: employeedb
- **User**: postgres
- **Password**: postgres
- **Volume**: Persistent storage

### 3. **pgadmin** - Database Management (Optional)
- **Port**: 5050
- **Email**: admin@admin.com
- **Password**: admin
- **Profile**: tools (not started by default)

**To start with pgAdmin:**
```bash
docker-compose --profile tools up -d
```

## Accessing the Application

### Application Endpoints
- **API Base**: http://localhost:8080
- **Health Check**: http://localhost:8080/actuator/health
- **Metrics**: http://localhost:8080/actuator/metrics
- **Info**: http://localhost:8080/actuator/info

### API Examples
```bash
# Create a full-time employee
curl -X POST http://localhost:8080/employees \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "name": "John Doe",
    "salary": 75000,
    "employeeType": "FULL_TIME"
  }'

# Get all employees
curl http://localhost:8080/employees

# Get employee by ID
curl http://localhost:8080/employees/1

# Calculate pay
curl http://localhost:8080/employees/1/pay
```

### Database Access

**Using pgAdmin (if started with --profile tools):**
1. Open http://localhost:5050
2. Login with admin@admin.com / admin
3. Add server:
   - Host: postgres
   - Port: 5432
   - Database: employeedb
   - Username: postgres
   - Password: postgres

**Using psql CLI:**
```bash
docker exec -it postgres-db psql -U postgres -d employeedb
```

## Docker Image Details

### Multi-Stage Build
The Dockerfile uses a multi-stage build:
1. **Builder Stage**: Uses Maven to compile and package the application
2. **Runtime Stage**: Uses JRE-only image for smaller size and better security

### Image Optimizations
- ✅ Multi-stage build (smaller final image)
- ✅ Non-root user for security
- ✅ Layer caching for dependencies
- ✅ Health checks included
- ✅ Container-aware JVM settings
- ✅ ZGC garbage collector enabled

### Image Size
- **Builder image**: ~800MB (not included in final image)
- **Final runtime image**: ~350-400MB

## Environment Variables

You can override configuration using environment variables:

```bash
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=docker \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host:5432/db \
  -e SPRING_DATASOURCE_USERNAME=user \
  -e SPRING_DATASOURCE_PASSWORD=pass \
  java-migration-lab:latest
```

## Health Checks

The application includes health checks at multiple levels:

### Docker Health Check
```bash
docker ps  # Shows health status
```

### Application Health Endpoint
```bash
curl http://localhost:8080/actuator/health
```

**Response:**
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "PostgreSQL",
        "validationQuery": "isValid()"
      }
    },
    "diskSpace": {
      "status": "UP"
    },
    "ping": {
      "status": "UP"
    }
  }
}
```

## Troubleshooting

### Container won't start
```bash
# Check logs
docker-compose logs app

# Check if port 8080 is already in use
netstat -ano | findstr :8080  # Windows
lsof -i :8080                 # Mac/Linux
```

### Database connection issues
```bash
# Ensure PostgreSQL is healthy
docker-compose ps

# Check PostgreSQL logs
docker-compose logs postgres

# Verify network connectivity
docker exec -it java-migration-lab ping postgres
```

### Out of memory errors
```bash
# Increase Docker memory limit in Docker Desktop settings
# Or adjust JVM memory settings in docker-compose.yml:
JAVA_OPTS=-XX:MaxRAMPercentage=50.0
```

### Rebuild after code changes
```bash
# Rebuild and restart
docker-compose up -d --build

# Or force rebuild
docker-compose build --no-cache
docker-compose up -d
```

## Production Considerations

### 1. Security
- [ ] Change default passwords
- [ ] Use secrets management (Docker secrets, Vault)
- [ ] Enable HTTPS/TLS
- [ ] Scan images for vulnerabilities
- [ ] Use specific image tags (not `latest`)

### 2. Resource Limits
Add resource constraints in docker-compose.yml:
```yaml
services:
  app:
    deploy:
      resources:
        limits:
          cpus: '2'
          memory: 2G
        reservations:
          cpus: '1'
          memory: 1G
```

### 3. Logging
- Use centralized logging (ELK, Splunk)
- Configure log rotation
- Set appropriate log levels

### 4. Monitoring
- Integrate with Prometheus/Grafana
- Set up alerting
- Monitor JVM metrics via Actuator

### 5. Backup
```bash
# Backup PostgreSQL data
docker exec postgres-db pg_dump -U postgres employeedb > backup.sql

# Restore
docker exec -i postgres-db psql -U postgres employeedb < backup.sql
```

## CI/CD Integration

### Build in CI Pipeline
```bash
# Build image
docker build -t myregistry/java-migration-lab:${VERSION} .

# Push to registry
docker push myregistry/java-migration-lab:${VERSION}
```

### Docker Registry
```bash
# Tag for Docker Hub
docker tag java-migration-lab:latest username/java-migration-lab:1.0.0

# Push to Docker Hub
docker push username/java-migration-lab:1.0.0
```

## Useful Commands

```bash
# View running containers
docker-compose ps

# View resource usage
docker stats

# Execute commands in container
docker exec -it java-migration-lab bash

# View application logs
docker-compose logs -f app

# Restart a service
docker-compose restart app

# Scale services (if stateless)
docker-compose up -d --scale app=3

# Remove all stopped containers
docker-compose down --remove-orphans

# Prune unused resources
docker system prune -a
```

## Next Steps

- [ ] Set up Kubernetes manifests for orchestration
- [x] Configure CI/CD pipeline
- [ ] Add monitoring with Prometheus
- [ ] Implement distributed tracing
- [ ] Set up log aggregation
