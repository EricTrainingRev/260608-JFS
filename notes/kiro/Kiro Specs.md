# Kiro Feature Specs Reference Guide

## Overview of Feature Specs
Feature Specs are a structured framework for moving from a high-level idea to a fully implemented feature. They transform vague intent into actionable engineering artifacts: **Requirements**, **Design**, and **Tasks**. 

### The Core Artifacts
Every Feature Spec generates three distinct Markdown files that serve as the "living documentation" for the feature:
1.  **`requirements.md`**: Defines *what* the system must do (the behavior).
2.  **`design.md`**: Defines *how* the system will do it (the architecture).
3.  **`tasks.md`**: A checklist of discrete implementation steps derived from the design.

---

## Workflow Variants
Kiro provides two primary workflows. The choice depends on whether your project is driven by **Product** (behavior) or **Engineering** (constraints).

### 1. Requirements-First (Product-Driven)
*   **Flow:** Requirements $\rightarrow$ Design $\rightarrow$ Implementation.
*   **Best For:** Greenfield projects, customer-driven features, or scenarios where the technical implementation is flexible.
*   **Goal:** Define the desired behavior first, then let the architecture evolve to meet those needs.

### 2. Design-First (Engineering-Driven)
*   **Flow:** Design $\rightarrow$ Requirements $\rightarrow$ Implementation.
*   **Best For:** Complex systems with strict non-functional requirements (latency, security, scale), porting existing architecture, or exploring technical feasibility.
*   **Goal:** Establish a technically sound architecture first, then derive the necessary requirements from that design.

### 3. Quick Plan (High Velocity)
For well-understood features, **Quick Plan** bypasses manual approval gates between the three phases. You provide the initial prompt, answer clarifying questions, and land directly on the `tasks.md` list.

**Workflow Comparison Summary:**

| Feature | Requirements-First | Design-First |
| :--- | :--- | :--- |
| **Primary Focus** | System Behavior | Technical Architecture |
| **Key Question** | "What should it do?" | "How will it work?" |
| **Primary Constraint** | User/Product needs | Technical/System limits |

---

## The Specification Phases

### Phase 1: Requirements & EARS Notation
To prevent ambiguity, Kiro uses **EARS (Easy Approach to Requirements Syntax)** notation. This ensures requirements are structured, testable, and unambiguous.

*   **Syntax Pattern:** `WHEN [condition/event] THE SYSTEM SHALL [expected behavior]`
*   **Example:** `WHEN a user submits an invalid email, THE SYSTEM SHALL display a validation error.`
*   **Requirement Analysis:** Before moving to design, use the **Analyze Requirements** tool to identify logical gaps, conflicting constraints, or ambiguities in your EARS statements.

### Phase 2: Design Documentation
The `design.md` file captures the high-level blueprint of the feature. It typically includes:
*   **Architecture:** Component relationships and system structure.
*   **Data Flow:** How information moves through the system.
*   **Interfaces:** API definitions and communication protocols.
*   **Data Models:** Schema definitions and state management.
*   **Error Handling:** Strategies for failure and edge cases.

### Phase 3: Task Implementation
The `tasks.md` file breaks the design down into small, executable units.
*   **Execution:** Use **Run all Tasks** to allow Kiro to execute the implementation.
*   **Parallelism:** Kiro automatically builds a dependency graph, running independent tasks concurrently to maximize speed.
*   **Synchronization:** If the requirements or design change, use **Sync Files** to automatically update the task list to match the new reality.

---

## Specialized Workflows

### Bugfix Specs
While Feature Specs are for new capabilities, **Bugfix Specs** are for correcting defects.
*   **When to use:** Critical code paths, complex root causes, or when regression prevention is vital.
*   **The Strategy:** Capture the "unchanged behavior." By documenting what the system *should continue to do* alongside the fix, Kiro can generate property-based tests to prevent future regressions.
*   **Reporting:** A high-quality bug description must include: **Reproduction Steps**, **Current Behavior**, **Expected Behavior**, and **Constraints**.

### Importing Existing Context
You do not need to start from scratch. You can ingest existing documentation into a Spec session via:
*   **Manual Import:** Copying text into a file and using `#file:name.md Generate a spec from it`.
*   **Visual Import:** Uploading architecture diagrams (PNG/JPG) or whiteboard sketches for Kiro to formalize into `design.md`.
*   **MCP Integration:** Connecting directly to tools like JIRA or Confluence via Model Context Protocol.

---

## Best Practices for Spec Management

*   **Granularity:** Do not create one massive spec for an entire app. Create individual specs for discrete features (e.g., `user-auth`, `shopping-cart`) to keep files manageable and allow parallel work.
*   **Version Control:** Store `.kiro/specs/` directly in your Git repository. This ensures requirements and design evolve alongside the code.
*   **Contextual Chat:** Use the `#spec` command in chat to bring a specific specification into your conversation. This allows you to ask, *"Does my current implementation meet the requirements in #spec:auth?"*
*   **Iterative Refinement:** Treat specs as living documents. Use the **Refine** button in the editor to update design or requirements as you discover new technical constraints during implementation.