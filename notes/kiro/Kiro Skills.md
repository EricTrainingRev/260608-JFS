# Kiro Agent Skills Reference Guide

## Overview of Agent Skills
Skills are portable, reusable instruction packages that follow the open **Agent Skills** standard. They allow Kiro to access specialized knowledge, executable scripts, and templates without overwhelming the AI's active context.

### The Concept of Progressive Disclosure
To maintain high performance and accuracy, Kiro uses a "Progressive Disclosure" model for intelligence. Instead of loading every piece of information at startup, Kiro follows a three-step lifecycle:

1.  **Discovery:** At startup, Kiro only loads the *names* and *descriptions* of available skills.
2.  **Activation:** When a user's request matches a skill's description, Kiro loads the *full instructions*.
3.  **Execution:** Kiro executes the instructions, pulling in necessary scripts or reference files only as required.

This approach keeps the AI's "working memory" focused while providing access to an unlimited library of specialized workflows.

---

## Skill Management & Scope

### Scope and Hierarchy
Like steering files, skills can be scoped to either an individual project or the entire machine.

*   **Workspace Skills (`.kiro/skills/`):** Apply only to the current project. Use these for team-specific procedures, such as project-specific deployment workflows or internal code review standards.
*   **Global Skills (`~/.kiro/skills/`):** Available across all workspaces. Use these for personal workflows, such as your preferred documentation style or a universal security checklist.

**Precedence Rule:** In the event of a naming conflict, **Workspace skills override Global skills.**

### Importing Skills
Skills can be imported into the Kiro environment from two primary sources:
*   **GitHub:** Import via a public repository URL (must point to a specific subdirectory containing the skill).
*   **Local Filesystem:** Import from a folder on your local machine.

---

## Technical Structure

A skill is defined as a directory containing a required `SKILL.md` file and optional supporting assets.

### Directory Anatomy
```text
my-skill/
├── SKILL.md           # Required: The core logic and metadata
├── scripts/           # Optional: Executable code for deterministic tasks
├── references/        # Optional: Deep documentation and context
└── assets/            # Optional: Templates and boilerplate
```

### The `SKILL.md` Specification
The `SKILL.md` file uses YAML front matter to define the skill's identity and activation logic.

**Required Front Matter Fields:**
| Field | Description |
| :--- | :--- |
| `name` | The identifier (lowercase, numbers, hyphens only; max 64 chars). |
| `description` | The activation trigger. Kiro matches user requests against this text (max 1024 chars). |

**Optional Front Matter Fields:**
*   `license`: The license governing the skill.
*   `compatibility`: Environment requirements (e.g., specific tools or network access).
*   `metadata`: Custom key-value pairs (e.g., author, version).

---

## Comparison: Skills vs. Steering vs. Powers
Understanding when to use each mechanism is critical for effective Kiro orchestration.

| Feature | Primary Purpose | Key Characteristic |
| :--- | :--- | :--- |
| **Steering** | Project Standards | Kiro-specific context (Always, Auto, Manual). |
| **Skills** | Portable Workflows | Open standard; includes scripts; on-demand loading. |
| **Powers** | Tool Integration | Bundles MCP tools with knowledge; activates via context. |

**Decision Rule:** 
*   Use **Steering** for "how we write code here."
*   Use **Skills** for "how we perform this specific portable task" (e.g., a PR review workflow).
*   Use **Powers** when you need to give the AI a new "tool" (via MCP) alongside instructions.

---

## Best Practices

*   **Optimize for Discovery:** Write highly precise descriptions. Instead of "helps with testing," use "Runs unit tests and analyzes coverage reports." The more specific the description, the more reliably Kiro will activate the skill.
*   **Minimize `SKILL.md` Bloat:** Keep the core `SKILL.md` file concise. Move heavy documentation or lengthy reference materials into the `references/` directory to keep the activation fast.
*   **Prioritize Scripts for Logic:** For tasks that require precision (like file generation, API calls, or complex validation), use executable scripts in the `scripts/` folder rather than relying on the LLM to generate the logic on the fly.
*   **Scope Appropriately:** Use Global skills for "Me" (personal habits) and Workspace skills for "Us" (team procedures).