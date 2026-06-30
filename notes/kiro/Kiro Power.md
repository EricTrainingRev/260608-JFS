# Kiro Agent Powers Reference Guide

## Overview of Powers
Powers are unified, portable instruction packages that provide Kiro with "instant expertise." While standard MCP servers provide raw tools, a **Power** bundles those tools with the specific knowledge, best practices, and workflows required to use them effectively.

### The Concept of Progressive Disclosure
To maintain high performance and accuracy, Kiro uses a "Progressive Disclosure" model for intelligence. Instead of loading all specialized knowledge at startup, Kiro follows a three-step lifecycle:

1.  **Discovery:** Kiro scans the names and keywords of installed powers.
2.  **Activation:** When a user's request matches a power's description, the power is loaded into the context.
3.  **Deactivation:** When the user moves to an unrelated task, the power is unloaded, keeping the context window clean.

---

## Anatomy of a Power

A power is a directory containing specialized metadata and assets.

### Directory Structure
A complete power follows this organizational pattern:
```text
power-name/
├── POWER.md              # Required: Metadata, onboarding, and steering mappings
├── mcp.json              # Optional: MCP server configuration for tool integration
└── steering/             # Optional: Workflow-specific guidance files
```

### Component Breakdown

#### 1. `POWER.md` (The Brain)
This file contains the core logic of the power and is divided into two functional sections:
* **Frontmatter (Activation Logic):** Uses YAML to define the `name`, `displayName`, `author`, and most importantly, the `description` and `keywords`. Kiro uses these fields to decide when to activate the power.
* **Onboarding Instructions:** A section that runs the first time the power is used. It is used to validate dependencies (e.g., checking if a CLI is installed) or to automatically inject workspace hooks.

#### 2. `mcp.json` (The Tools)
If the power requires external tools, this file contains the MCP server configuration. Note that Kiro automatically namespaces these servers during installation (e.g., `supabase-local` becomes `power-supabase-supabase-local`) to prevent conflicts.

#### 3. `steering/` (The Expertise)
For complex powers, guidance is split into multiple Markdown files within this directory. This allows Kiro to load only the relevant workflow (e.g., `database-setup.md`) when the user is performing that specific task, rather than loading the entire manual at once.

---

## Building a Power

### Step 1: Define the `POWER.md`
Create the mandatory `POWER.md` file. You must define the frontmatter so Kiro knows how to trigger the power.

**Example Frontmatter:**
```yaml
---
name: "supabase"
displayName: "Supabase with local CLI"
description: "Build fullstack applications with Supabase's Postgres database and auth"
keywords: ["database", "postgres", "auth", "supabase"]
author: "Supabase"
---
```

### Step 2: Implement Onboarding and Guidance
In the body of `POWER.md`, add an `# Onboarding` section to ensure the environment is ready.
* **Validation:** Use commands to check for required software (e.g., `docker --version`).
* **Automation:** Use onboarding to automatically add Kiro hooks to the user's workspace.

For advanced powers, move complex workflows into the `steering/` directory and map them in the `POWER.md` file to ensure modularity.

### Step 3: Add Tooling (Optional)
If your power requires MCP tools, create an `mcp.json` file in the power directory. Use environment variable references (e.g., `${SUPABASE_URL}`) for sensitive keys to maintain security.

### Step 4: Testing and Deployment
* **Local Testing:** Open the Kiro Powers panel and select **Add power from Local Path** to test your directory immediately.
* **Sharing:** Push your power directory to a public GitHub repository. Others can then install it by providing the repository URL via the Kiro interface.

---

## Summary of Implementation Strategies

| Strategy | Structure | Best Use Case |
| :--- | :--- | :--- |
| **Simple** | `POWER.md` + `mcp.json` | Single-tool powers with basic instructions. |
| **Single-Tool Steering** | `POWER.md` + `steering/` | Tools with one main workflow (e.g., a specific CLI). |
| **Multi-Tool / Full-Stack**| `POWER.md` + `mcp.json` + `steering/` | Complex ecosystems (e.g., Supabase, AWS) with many distinct workflows. |
| **Documentation-Only** | `POWER.md` + `steering/` | Providing patterns or linting rules without external tools. |