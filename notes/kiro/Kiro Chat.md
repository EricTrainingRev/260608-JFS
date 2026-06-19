# Kiro Chat Reference Guide

## Overview of Chat
The Kiro Chat is the primary intelligence interface of the IDE. Unlike a standard chat interface, Kiro Chat is **context-aware**, meaning it does not just process text; it processes your entire workspace, terminal state, and project specifications to provide actionable engineering assistance.

### The Concept of Context
In LLM-based development, "Context" refers to the amount of information the AI can "see" at any given moment. High-quality responses depend on providing the right context without overwhelming the model with irrelevant data. Kiro manages this through three distinct mechanisms: **Providers**, **Command Context**, and **Steering**.

---

## Context Management

Effective use of Kiro requires mastering how to feed information into the chat. You can provide context through three primary methods.

### 1. Context Providers (`#` Commands)
Providers allow you to explicitly "attach" specific parts of your environment to a chat message. This is the most direct way to ensure the AI is looking at the correct data.

*   **File/Folder Context:** Using `#File` or `#Folder` to point the AI to specific implementation details.
*   **Terminal Context:** Using `#terminal` to reference the output of your currently active terminal window (e.g., `#terminal analyze this error`).
*   **Standard Providers:** Built-in tools that allow for quick referencing of project components.

### 2. Slash Commands (`/`)
While `#` provides **data**, `/` provides **instructions**. Slash commands trigger specific "Hooks" or "Steering Files" to change the behavior of the chat.

*   **Hooks (Manual Triggers):** These execute immediate, predefined actions.
    *   *Example:* `/run-tests` executes your test suite and brings the results into the chat.
*   **Steering Files (Guidance):** These inject specific rules or standards into the conversation context.
    *   *Example:* `/accessibility` applies UI accessibility guidelines to the AI's suggestions for the duration of the session.

### 3. Steering (Automated Context)
Steering is the "background intelligence" of Kiro. It can be configured to be **Always-on** (automatically included in every chat) or **Manual** (triggered via slash commands). This allows you to define "personality" or "rule-sets" for the AI, such as "Always follow Clean Architecture principles."

---

## Agent Execution Modes

Kiro operates in two distinct modes of autonomy. Choosing the correct mode is a balance between **velocity** and **verification**.

### Autopilot Mode (High Velocity)
In Autopilot mode, the agent acts as an autonomous engineer. It is designed for complex, multi-step tasks where the AI makes architectural decisions and executes them end-to-end.

*   **Workflow:** The agent creates files, modifies code across multiple locations, and runs commands without asking for permission at every step.
*   **Control Mechanisms:**
    *   **View All Changes:** A comprehensive diff view of every modification made across the workspace.
    *   **Revert All:** A global "undo" function that restores the filesystem to its state prior to the agent's intervention.
    *   **Interrupt:** The ability to halt execution mid-task if the agent's direction deviates from your intent.

### Supervised Mode (High Verification)
Supervised mode is designed for critical codebases or when the developer wants to guide the AI through every logic gate. It turns the chat into a collaborative, turn-based dialogue.

*   **Workflow:** After every turn involving a file edit, the agent "yields" and waits for human approval.
*   **Granular Review Tools:**
    *   **Hunk-based Review:** Instead of accepting a whole file, you can review "hunks" (logical groups of lines). You can **Accept**, **Reject**, or **Chat Inline** (request a specific change to that specific hunk).
    *   **Selective Approval:** You can accept the parts of a change you like and reject the parts you don't, allowing the agent to iterate based on your feedback.

**Mode Comparison Summary:**

| Feature | Autopilot Mode | Supervised Mode |
| :--- | :--- | :--- |
| **Primary Goal** | Speed and Autonomy | Precision and Control |
| **Decision Making** | Agent-led | Developer-led |
| **Review Granularity** | Workspace-wide (Diffs) | Line-by-line (Hunks) |
| **Best For** | Repetitive/Well-defined tasks | Critical/Complex systems |

---

## Summary of Interaction Syntax

| Prefix | Type | Purpose | Example |
| :--- | :--- | :--- | :--- |
| **`#`** | **Provider** | Attaches **data** to the prompt. | `#File: auth.ts` |
| **`/`** | **Command** | Triggers an **action** or **rule-set**. | `/run-tests` |
| **N/A** | **Natural Language** | Describes **intent** to the agent. | "Refactor this function." |