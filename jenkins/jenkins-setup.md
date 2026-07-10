# 🚀 Jenkins CI/CD Deployment Guide (Docker Cloud + Ephemeral Agents)

# Phase 1: Environment Preparation

## 1. Create a Project Directory

```bash
mkdir jenkins-cicd && cd jenkins-cicd
```

## 2. Create a Persistent Gradle Cache Directory

This directory will be mounted into every ephemeral build agent so Gradle dependencies are downloaded only once.

```bash
sudo mkdir -p /opt/jenkins-gradle-cache
sudo chmod 777 /opt/jenkins-gradle-cache
```

***

# Phase 2: Docker Compose Configuration

## 3. Create `docker-compose.yml`

Create a file named `docker-compose.yml`:

```yaml
services:
  jenkins-controller:
    image: jenkins/jenkins:lts-jdk17
    container_name: jenkins-controller
    privileged: true
    user: root
    ports:
      - "8080:8080"
      - "50000:50000"
    volumes:
      - jenkins_data:/var/jenkins_home
      - /var/run/docker.sock:/var/run/docker.sock
    restart: always
volumes:
  jenkins_data:
```

### Why only one container?

In this design:

* The Jenkins controller runs continuously.
* Build agents are automatically created by the Docker plugin.
* Agents are destroyed when builds finish.
* No SSH configuration is required.

***

# Phase 3: Deployment

## 4. Start Jenkins

```bash
docker compose up -d
```

## 5. Retrieve Initial Admin Password

```bash
docker logs jenkins-controller
```

Locate and copy the initial administrator password.

***

# Phase 4: Jenkins Initial Setup

## 6. Complete Jenkins Setup

Open:

```text
http://{url}:8080
```

Then:

1. Enter the administrator password.
2. Select **Install Suggested Plugins**.
3. Create an admin account.
4. Log in.

***

# Phase 5: Install Required Plugins

## 7. Verify Docker Plugins

Navigate to:

```text
Manage Jenkins → Plugins
```

Install:

* Docker Plugin
* Docker Pipeline Plugin

Restart Jenkins if prompted.

***

# Phase 6: Configure Docker Cloud

## 8. Create a Docker Cloud

Navigate to:

```text
Manage Jenkins → Clouds
```

Click:

```text
New Cloud
```

Choose:

```text
Docker
```

Cloud Name:

```text
Docker-Cloud
```

Docker Host URI:

```text
unix:///var/run/docker.sock
```

Click:

```text
Test Connection
```

Expected result:

```text
Docker API Version: ...
Docker Version: ...
```

***

# Phase 7: Configure Dynamic Agent Template

Inside the Docker Cloud configuration:

## Basic Settings

### Docker Image

```text
jenkins/inbound-agent:latest-jdk21
```

### Labels

```text
docker
```

### Remote File System Root

```text
/home/jenkins/agent
```

### Instance Capacity

```text
5
```

***

## Connector

Select:

```text
Connect with JNLP
```

or

```text
Attach Docker Container
```

(depending on plugin version)

***

## Mounts

Add the Gradle cache mount:

```text
type=bind,source=/opt/jenkins-gradle-cache,target=/home/jenkins/.gradle
```

This allows all ephemeral agents to share a common Gradle cache.

***

# Phase 8: Configure Gradle Build Cache

Add the following to your project's `gradle.properties`:

```properties
org.gradle.caching=true
```

This enables Gradle task output caching in addition to dependency caching.

***

# Phase 9: Create Your First Build

## 9. Create a Freestyle Project

Navigate to:

```text
New Item
```

Select:

```text
Freestyle Project
```

Name:

```text
spring-boot-test
```

***

## Restrict Build Location

Enable:

```text
Restrict where this project can be run
```

Label Expression:

```text
docker
```

***

## Build Step

Add:

```text
Execute Shell
```

Example:

```bash
./gradlew clean build
```

***

# Validation

## Verify Dynamic Agent Creation

Navigate to:

```text
Manage Jenkins → Nodes
```

When a build starts you should see a temporary node appear.

Example:

```text
docker-agent-abc123
```

When the build finishes the node should disappear automatically.

***

## Verify Gradle Cache Usage

After the first successful build:

```bash
ls -la /opt/jenkins-gradle-cache
```

You should see folders such as:

```text
caches/
daemon/
native/
wrapper/
```

Future builds should be noticeably faster because Gradle dependencies no longer need to be downloaded.
