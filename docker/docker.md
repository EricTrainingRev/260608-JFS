
# Docker Quick Reference Guide

## Introduction
Docker is a platform for developing, shipping, and running applications in lightweight, portable containers. Containers package code and dependencies together, ensuring consistency across environments and simplifying deployment. Docker is widely used for microservices, CI/CD, and scalable cloud-native applications.

## Containers vs Virtual Machines (VMs)
Both containers and VMs provide isolated environments for running applications, but differ fundamentally:
- **VMs**: Virtualize hardware, run full OS per instance, heavier resource usage, slower startup.
- **Containers**: Virtualize at OS level, share host kernel, lightweight, fast startup, efficient resource use.
Containers are ideal for rapid deployment, scaling, and portability, while VMs are suited for full OS isolation and legacy workloads.

## Docker Architecture
Docker uses a client-server architecture:
- **Docker Client**: CLI or GUI that sends commands to the Docker daemon.
- **Docker Daemon (`dockerd`)**: Background service that builds, runs, and manages containers, images, networks, and volumes.
- **Docker Registries**: Repositories for storing and distributing images (e.g., Docker Hub, private registries).
- **REST API**: Enables integration with other tools and automation.

## Installing Docker
Docker can be installed on Windows, macOS, and Linux:
- **Docker Desktop**: Recommended for Windows/macOS, includes GUI and CLI.
- **Linux**: Install via package manager (e.g., `apt`, `yum`).
After installation, verify with `docker --version` and test with `docker run hello-world`.

## Docker Images
Images are read-only templates for containers. Each image contains:
- Application code
- Runtime (e.g., Python, Node.js)
- Libraries and dependencies
- Environment variables
- Configuration files
Images are built from Dockerfiles, versioned, and shared via registries.

### Image Registries
- **Docker Hub**: Default public registry.
- **Private Registries**: For internal sharing and security.
Registries enable collaboration and automated deployments.

## Dockerfile & Image Optimization

A `Dockerfile` is a text document containing the instructions to build a Docker image. Each instruction creates a new **read-only layer** in the image.

### Core Instructions
| Instruction | Purpose | Best Practice Note |
| :--- | :--- | :--- |
| `FROM` | Sets the Base Image. | Always use a specific version (e.g., `python:3.10-slim`) rather than `latest`. |
| `WORKDIR` | Sets the working directory. | Always use `WORKDIR` instead of `RUN cd ...`; `cd` does not persist across layers. |
| `COPY` | Copies files from host to container. | Use `COPY` instead of `ADD` unless you specifically need to extract tarballs. |
| `RUN` | Executes commands in a new layer. | Combine commands (e.g., `apt update && apt install ...`) to reduce layer count. |
| `ENV` | Sets environment variables. | Persist these variables in the image for runtime use. |
| `EXPOSE` | Informs Docker which port is intended. | Primarily documentation; does not actually open the port (use `-p` at runtime). |
| `CMD` | Default command for the container. | Easily overridden by arguments passed to `docker run`. |
| `ENTRYPOINT` | The main executable of the image. | Harder to override; used to make the container behave like a dedicated binary. |

#### **CMD vs. ENTRYPOINT: Understanding Execution**
While both define what command runs when a container starts, they behave differently when a user provides arguments at the command line (e.g., `docker run <image> <arg>`). 

**`ENTRYPOINT`** defines the "main executable" of the container. It is difficult to override and makes the container behave like a dedicated tool. If you use `ENTRYPOINT ["python"]`, the container is essentially a Python runner.

**`CMD`** defines the *default arguments* for the `ENTRYPOINT`. If an `ENTRYPOINT` is set, `CMD` acts as a default set of parameters that can be easily replaced. If no `ENTRYPOINT` is defined, `CMD` acts as the entire command. 

**The Golden Rule:** Use `ENTRYPOINT` for the command that *must* run, and use `CMD` to provide the *default arguments* that users might want to change.

### The Layering & Caching Mechanism
Docker uses a **Layered File System**. When you build an image, Docker caches each instruction. 
* **Cache Hit:** If an instruction and the files it relies on haven't changed, Docker reuses the existing layer from the cache, making builds nearly instant.
* **Cache Busting:** If a layer changes (e.g., a file you `COPY` is modified), **all subsequent layers** are invalidated and must be rebuilt from scratch.

#### Optimization Strategy: "Order Matters"
To maximize build speed, arrange instructions from **least frequently changed** to **most frequently changed**.

**BAD (Slow builds):**
```Dockerfile
FROM python:3.10
WORKDIR /app
COPY . .               # 1. Any code change busts the cache here...
RUN pip install -r requirements.txt # 2. ...forcing a slow reinstall of all packages.
CMD ["python", "app.py"]
```

**GOOD (Fast builds):**
```Dockerfile
FROM python:3.10
WORKDIR /app
COPY requirements.txt . # 1. Only changes if dependencies change.
RUN pip install -r requirements.txt # 2. Cached unless requirements.txt changes.
COPY . .                # 3. Code changes only trigger this and the final layer.
CMD ["python", "app.py"]
```

### Advanced Optimization Techniques

#### 1. Multi-Stage Builds (The "Gold Standard")
Multi-stage builds allow you to use heavy "build-time" dependencies (compilers, SDKs) in one stage and copy only the compiled artifacts into a tiny "production" stage. This drastically reduces the attack surface and image size.

```Dockerfile
# Stage 1: The Build Stage
FROM node:18 AS builder
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build  # Creates a /dist folder

# Stage 2: The Production Stage
FROM nginx:alpine
# Only copy the compiled files from the builder stage
COPY --from=builder /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

#### 2. Use `.dockerignore`
Similar to `.gitignore`, a `.dockerignore` file prevents heavy or sensitive files (like `.git`, `node_modules`, or `.env`) from being sent to the Docker daemon. This speeds up the "build context" transfer and keeps images clean.

#### 3. Minimizing Layers
Instead of multiple `RUN` commands, chain them using `&&` and clean up temporary files in the **same layer** to keep the image slim.
* **Avoid:** `RUN apt-get update` $\rightarrow$ `RUN apt-get install -y package`
* **Prefer:** `RUN apt-get update && apt-get install -y package && rm -rf /var/lib/apt/lists/*`

## Building Images
Use `docker build -t <image-name> .` to build an image from a Dockerfile. Tag images for versioning. Push images to a registry with `docker push <image-name>`.

## Containers
Containers are runnable instances of images. They encapsulate the application and its environment, providing isolation and portability.

### Creating Containers
Use `docker run <options> <image>` to start a container. Common options:
- `-p`: Map ports (e.g., `-p 8080:80`)
- `-v`: Mount volumes (e.g., `-v /host:/container`)
- `-e`: Set environment variables
- `--name`: Assign a name

### Managing Containers
Commands for lifecycle management:
- `docker ps`: List running containers
- `docker stop <container>`: Stop container
- `docker start <container>`: Start container
- `docker rm <container>`: Remove container
- `docker logs <container>`: View logs
- `docker exec -it <container> <cmd>`: Run command in container

## Volumes

Volumes provide persistent storage for containers, surviving container removal and enabling data sharing.

- **Types:**
	- **Named Volumes:**
		- Created and managed by Docker with a specific name.
		- Useful for sharing data between containers and persisting data beyond container lifecycle.
		- Example: `docker volume create mydata` and `docker run -v mydata:/app/data ...`
	- **Anonymous Volumes:**
		- Created without a name, often by specifying only the mount path in `docker run`.
		- Docker assigns a random name; useful for temporary storage when the name is not important.
		- Example: `docker run -v /app/data ...` (no volume name specified)
	- **Bind Mounts:**
		- Link a specific directory or file on the host to a path in the container.
		- Useful for development, sharing source code, or accessing host files directly.
		- Example: `docker run -v /host/path:/container/path ...`

- **Usage:** `docker volume create`, `docker run -v <volume>:/path`, `docker volume ls`

## Networking
Docker provides several networking options:
- **Bridge**: Default network for containers on a single host
- **Host**: Container shares host network stack
- **Overlay**: Multi-host networking for Docker Swarm
- **Custom networks**: User-defined for isolation and communication
Use `docker network create` and `docker network ls` to manage networks.

## Best Practices
- Use minimal base images for security and efficiency
- Keep Dockerfiles simple and readable
- Use multi-stage builds to reduce image size
- Tag images with meaningful versions
- Regularly update images and dependencies
- Limit container privileges (use non-root user)
- Clean up unused images, containers, and volumes

## Common Docker Commands
- `docker build -t <name> .`: Build image
- `docker run -d -p 80:80 <image>`: Run container detached
- `docker ps -a`: List all containers
- `docker images`: List images
- `docker exec -it <container> bash`: Open shell in container
- `docker logs <container>`: View container logs
- `docker stop <container>`: Stop container
- `docker rm <container>`: Remove container
- `docker volume ls`: List volumes

## Troubleshooting
- Use `docker logs` to diagnose container issues
- Inspect containers with `docker inspect <container>`
- Remove unused resources with `docker system prune`
- Check Docker daemon status with `systemctl status docker` (Linux)

---

## Docker Compose

While the Docker CLI is great for managing individual containers, **Docker Compose** is a tool for defining and running multi-container applications. It uses a declarative `.yaml` file to configure all the services, networks, and volumes your application needs, allowing you to start an entire stack with a single command.

### Key Concepts
- **Services**: Each service in a Compose file represents a single container (e.g., a `web` service, a `db` service, and a `cache` service).
- **Declarative Configuration**: Instead of typing long `docker run` commands with dozens of flags, you define the desired state in a `docker-compose.yml` file.
- **Service Discovery**: Compose automatically creates a shared network for all services defined in the file. Services can communicate with each other using their **service name** as the hostname (e.g., the `web` app can connect to the database using `host: db`).

### Common `docker-compose.yml` Structure
```yaml
version: '3.8'  # Version of the Compose file format

services:
  web:
    build: .             # Build the Dockerfile in the current directory
    ports:
      - "8080:80"        # Map host port 8080 to container port 80
    volumes:
      - .:/app           # Bind mount for hot-reloading during development
    depends_on:
      - db               # Ensure the 'db' service starts before 'web'
    environment:
      - DEBUG=true

  db:
    image: postgres:15   # Pull a pre-built image from Docker Hub
    volumes:
      - postgres_data:/var/lib/postgresql/data
    environment:
      POSTGRES_PASSWORD: example_password

volumes:
  postgres_data:         # Define a named volume for persistent DB storage
```

### Essential Compose Commands
| Command | Description |
| :--- | :--- |
| `docker-compose up` | Builds, (re)creates, starts, and attaches to containers in a new environment. |
| `docker-compose up -d` | Starts the entire stack in **detached mode** (runs in the background). |
| `docker-compose ps` | Lists the status of the services in the current Compose project. |
| `docker-compose logs -f` | Tails the combined log output from all running services. |
| `docker-compose stop` | Stops the containers without removing them. |
| `docker-compose down` | **Stops and removes** containers, networks, and images defined in the file. |
| `docker-compose exec <service> <cmd>` | Runs a command inside a running service (e.g., `docker-compose exec db bash`). |

### When to use Compose vs. Docker CLI
- **Use Docker CLI** for testing a single image, debugging a specific container, or quick one-off tasks.
- **Use Docker Compose** for local development environments, simulating production architectures, and managing complex applications that require multiple interacting services (Frontend + API + Database).