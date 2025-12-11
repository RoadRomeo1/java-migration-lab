# Docker Directory Structure

All Docker-related files have been organized into the `docker/` directory for better project organization.

## Directory Structure

```
java-migration-lab/
├── docker/
│   ├── Dockerfile              # Multi-stage build configuration
│   ├── docker-compose.yml      # Service orchestration
│   ├── .dockerignore          # Build context exclusions
│   └── README.md              # Docker deployment guide
├── src/
│   └── main/
│       └── resources/
│           ├── application.properties        # Default config (H2)
│           └── application-docker.properties # Docker profile (PostgreSQL)
├── pom.xml
└── README.md
```

## Why This Structure?

### ✅ **Benefits**
1. **Clean Root Directory** - Keeps project root uncluttered
2. **Clear Separation** - Docker files are grouped together
3. **Easy to Find** - All container-related configs in one place
4. **Professional** - Follows common project organization patterns

### 📁 **What's in `docker/`**

| File | Purpose |
|------|---------|
| `Dockerfile` | Instructions to build the Java application image |
| `docker-compose.yml` | Orchestrates app + PostgreSQL + pgAdmin |
| `.dockerignore` | Excludes unnecessary files from Docker build |
| `README.md` | Complete Docker deployment documentation |

## How to Use

### From Project Root
```bash
cd docker
docker-compose up -d
```

### From docker/ Directory
```bash
docker-compose up -d
```

## Path References

The `docker-compose.yml` uses relative paths:
```yaml
build:
  context: ..              # Parent directory (project root)
  dockerfile: docker/Dockerfile  # Dockerfile location
```

This allows Docker to:
- Access the entire project for building (pom.xml, src/, etc.)
- Use the Dockerfile from the docker/ directory
- Keep everything organized

## Quick Commands

All commands should be run from the `docker/` directory:

```bash
# Navigate to docker directory
cd docker

# Start services
docker-compose up -d

# View logs
docker-compose logs -f app

# Stop services
docker-compose down

# Rebuild after code changes
docker-compose up -d --build
```

## Application Profiles

- **Local Development** (default): Uses H2 in-memory database
  ```bash
  mvn spring-boot:run
  ```

- **Docker** (docker profile): Uses PostgreSQL in container
  ```bash
  cd docker
  docker-compose up -d
  ```

## Files Moved

The following files were moved from project root to `docker/`:
- `Dockerfile` → `docker/Dockerfile`
- `docker-compose.yml` → `docker/docker-compose.yml`
- `.dockerignore` → `docker/.dockerignore`
- `DOCKER.md` → `docker/README.md`

## Next Steps

See `docker/README.md` for:
- Detailed deployment instructions
- Troubleshooting guide
- Production considerations
- CI/CD integration examples
