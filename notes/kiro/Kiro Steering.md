# Kiro Steering Reference Guide

## Overview of Steering
Steering is the mechanism used to provide Kiro with persistent, project-specific knowledge. Rather than repeating architectural patterns, coding standards, or library preferences in every chat session, you define them in **Steering Files** (Markdown documents). These files act as the "source of truth" for the AI's behavior within your workspace.

### The Core Value Proposition
* **Consistency:** Ensures every line of generated code adheres to team-wide patterns.
* **Efficiency:** Eliminates the "context tax" of explaining your tech stack in every prompt.
* **Scalability:** Allows project knowledge to grow alongside the codebase.
* **Alignment:** Provides a single, documented standard for both new and seasoned contributors.

---

## Scope and Hierarchy
Steering files are organized by their reach, allowing you to define rules that are either universal to your machine or specific to a single project.

### 1. Global Scope (`~/.kiro/steering/`)
* **Application:** Applies to **all** workspaces on your machine.
* **Use Case:** Personal coding preferences, universal security policies, or company-wide standards.
* **Team Steering:** Can be deployed via MDM or central repositories to ensure entire engineering teams share the same global context.

### 2. Workspace Scope (`.kiro/steering/`)
* **Application:** Applies only to the current project.
* **Use Case:** Project-specific frameworks, unique architectural decisions, or local naming conventions.

### 3. Conflict Resolution
Kiro follows a **Precedence Rule**: **Workspace steering overrides Global steering.** This allows you to have general global habits that you can specifically "veto" or change for a unique project.

---

## Steering File Implementation

### Foundational Steering Files
Kiro provides three core files to establish an immediate baseline of understanding. These are included in every interaction by default.
* **Product Overview (`product.md`):** The "Why." Defines business objectives, target users, and product purpose.
* **Technology Stack (`tech.md`):** The "What." Documents frameworks, libraries, and technical constraints.
* **Project Structure (`structure.md`):** The "How." Outlines file organization, naming conventions, and import patterns.

### The `AGENTS.md` Standard
Kiro supports the `AGENTS.md` standard. Files named `AGENTS.md` placed in the workspace root or global steering folder are **automatically included in every session**. Unlike standard steering files, they do not support manual or conditional inclusion modes.

---

## Inclusion Modes (Front Matter Configuration)
To optimize performance and prevent "context bloat," steering files use YAML front matter to define when they should be loaded.

| Mode | YAML Syntax | Behavior | Best For |
| :--- | :--- | :--- | :--- |
| **Always** | `inclusion: always` | Loaded in **every** interaction. | Core tech stack, security, and coding standards. |
| **Conditional** | `inclusion: fileMatch` | Loaded only when working on files matching a `fileMatchPattern`. | Domain-specific rules (e.g., only for `.test.ts` files). |
| **Manual** | `inclusion: manual` | Loaded only when requested via `#` or `/`. | Troubleshooting guides or rare migration procedures. |
| **Auto** | `inclusion: auto` | Loaded when the AI detects the user's request matches the file's `description`. | Specialized domain knowledge (e.g., API design patterns). |

---

## Best Practices for Effective Steering

### 1. Content Structure
* **One Domain Per File:** Keep files focused (e.g., `testing.md` should not contain `api-design.md`).
* **Provide Examples:** Use code snippets and "Before/After" comparisons to demonstrate the desired standard.
* **Explain the "Why":** Don't just state a rule; explain the rationale to help the AI make better architectural inferences.
* **Live References:** Use the `#[[file:path/to/file]]` syntax within steering docs to link to actual implementation examples in your workspace.

### 2. Maintenance and Security
* **Treat as Code:** Steering files should be version-controlled, reviewed during Pull Requests, and updated during architectural shifts.
* **Security First:** **Never** include secrets, API keys, or sensitive credentials in steering files, as they become part of the AI's permanent context.

### 3. Common Strategies
* **API Standards:** Define REST conventions, status code usage, and error formats.
* **Testing Approach:** Document preferred assertion styles, mocking libraries, and coverage expectations.
* **Code Style:** Specify naming patterns, import ordering, and anti-patterns to avoid.