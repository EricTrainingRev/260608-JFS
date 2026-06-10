# Kiro IDE Quick Reference Guide

## Overview of Kiro
Kiro is an AI-native Integrated Development Environment (IDE) designed to bridge the gap between traditional coding and AI-assisted development. Unlike standard editors that treat AI as a side-car plugin, Kiro integrates AI directly into the core interface, workflows, and project security models.

### The AI-Native Paradigm
To use Kiro effectively, it is helpful to understand how it differs from a traditional IDE:

1.  **Context-Aware Assistance:** Instead of just providing autocomplete, Kiro uses the "Agent" model. It doesn't just suggest text; it understands the relationship between files, folders, and specifications.
2.  **Integrated Agency:** Through features like **Agent Hooks** and **MCP (Model Context Protocol) servers**, Kiro moves from being a passive editor to an active participant in the development lifecycle.
3.  **Proactive Security:** Kiro acknowledges that AI models require context to be useful, but introduces explicit control mechanisms (like `.kiroignore`) to ensure sensitive data never leaves your local environment.

---

## Kiro Interface & Workflow

The Kiro interface is structured to separate the **creation** of code from the **management** of the AI agent and project metadata.

### Core Interface Components

#### 1. The Workspace (The Creation Layer)
* **Editor:** The central workspace. It features standard professional tools such as syntax highlighting, code folding, and split-view support for side-by-side comparisons.
* **Command Palette (`Cmd/Ctrl+Shift+P`):** The high-speed command center. This is the primary way to execute AI-specific actions, access MCP tools, and configure workspace settings without using a mouse.

#### 2. The AI Interaction Layer
* **Chat Panel:** The primary interface for high-level reasoning. You can request code generation, ask for architectural reviews, or debug complex errors.
    * **Contextual Commands:** Use `#` symbols (e.g., `#File`, `#Folder`) to manually inject specific context into the AI's "brain."
* **Kiro View (Sidebar):** A specialized view dedicated to AI management. This is where you manage:
    * **Specs:** High-level requirements and project definitions.
    * **Agent Steering & Hooks:** Configuration for how the AI behaves and what actions it is allowed to take.
    * **MCP Servers:** Connections to external tools and data sources.

#### 3. The Project Management Layer
* **Views (Sidebar):** Standard IDE functionality including the **Explorer** (file structure), **Search** (global operations), **Source Control** (Git management with AI-generated commit messages), and **Run and Debug**.
* **Status Bar:** A real-time telemetry strip at the bottom of the screen providing file info, Git status, error counts, and—crucially—**Agent status indicators**.

**Interface Quick Reference:**
| Component | Primary Role | Key Feature |
| :--- | :--- | :--- |
| **Editor** | Code Writing | Split views & Syntax highlighting |
| **Chat Panel** | AI Reasoning | `#` context commands |
| **Kiro View** | AI Orchestration | Specs & Agent Steering |
| **Command Palette**| Rapid Execution | Access to MCP & Settings |

---

## Managing AI Context: `.kiroignore`

In an AI-integrated environment, **Context is Currency**. However, providing too much context can lead to security risks or "noise" that degrades AI performance. The `.kiroignore` file is the mechanism used to define the boundaries of the AI's awareness.

### The Purpose of Ignoring
* **Security:** Protecting API keys, `.env` files, and credentials from being sent to external LLM services.
* **Privacy & Compliance:** Ensuring PII (Personally Identifiable Information) or sensitive company data remains local.
* **Agent Focus:** Preventing the AI from getting "lost" in massive build artifacts (`dist/`, `node_modules/`) or large data dumps, which improves response accuracy.

### Implementation & Hierarchy
Kiro follows a hierarchical pattern for ignoring files, similar to Git.

1.  **Local Project Level:** Create a `.kiroignore` in your project root.
2.  **Subdirectory Level:** You can place `.kiroignore` files in subdirectories to override parent rules.
3.  **Global Level:** Kiro automatically honors global patterns located at `~/.kiro/settings/kiroignore`.

#### Syntax Reference
`.kiroignore` uses standard `gitignore` syntax:
| Pattern | Effect |
| :--- | :--- |
| `secret.txt` | Ignores a specific file |
| `*.log` | Ignores all files with a specific extension |
| `folder/` | Ignores an entire directory |
| `!important.txt` | **Negation:** Ensures a specific file is NOT ignored |

**Critical Constraint: The Parent Directory Rule**
If you ignore a parent directory (e.g., `secrets/`), you **cannot** re-include a file inside it using negation (`!secrets/public.txt`). To allow specific files within a sensitive folder, you must avoid ignoring the folder itself and instead ignore specific patterns within it.

---

## Enterprise Administration: Extension Registry

For organizations requiring strict control over the development environment, Kiro allows administrators to bypass the default marketplace (`open-vsx.org`) in favor of a **Private Extension Registry**. This ensures that only vetted, secure, and compliant extensions are available to developers.

### Deployment by Operating System

| OS | Mechanism | Implementation Method |
| :--- | :--- | :--- |
| **Windows** | Registry Policy | Create `.admx`/`.adml` files to define a new `ExtensionGalleryServiceUrl` in the Windows Registry. |
| **macOS** | Configuration Profile | Deploy a `.mobileconfig` file via an MDM (Mobile Device Management) solution. |
| **Linux** | Policy File | Deploy a `policy.json` file to the `/etc/kiro/` directory. |

#### Configuration Logic
Regardless of the OS, the goal is to set the `ExtensionGalleryServiceUrl` property. Once this URL is defined, Kiro will redirect all extension search and installation requests to your organization's private server. When deploying custom registries, always test the deployment on a single machine using the manual methods (Registry Editor, local `.mobileconfig`, or local `policy.json`) before pushing the policy via MDM/Group Policy to the entire fleet.