# Kiro Agent Hooks Reference Guide

## Overview of Agent Hooks
Agent Hooks are event-driven automation tools that execute predefined actions when specific triggers occur within the IDE. They transform Kiro from a reactive assistant into an active participant in your development lifecycle, allowing you to automate routine tasks, enforce standards, and prevent errors without manual intervention.

### The Automation Loop
Every hook follows a two-step lifecycle:
1.  **Event Detection:** Kiro monitors the workspace for a specific trigger (e.g., a file being saved or a tool being called).
2.  **Automated Action:** Upon detection, Kiro executes a predefined response, which can be an AI-driven prompt or a local shell command.

---

## Hook Trigger Types (The "When")

Hooks are categorized by the specific event that initiates them.

### 1. Workspace & File Events
These triggers respond to changes in the physical files of your project.
*   **File Create:** Triggers when a new file matching a pattern is added. (Use: Generating boilerplate or license headers).
*   **File Save:** Triggers when a file is saved. (Use: Running linters, formatters, or auto-generating documentation).
*   **File Delete:** Triggers when a file is removed. (Use: Cleaning up related assets or updating imports).

### 2. Agent & Interaction Events
These triggers respond to the communication flow between the user and the AI.
*   **Prompt Submit:** Triggers immediately when a user sends a message. (Use: Injecting context or blocking prohibited prompts).
*   **Agent Stop:** Triggers when the AI finishes its response. (Use: Automatically running tests to verify the AI's code).

### 3. Tool & Task Events
These triggers respond to the execution of specific technical operations.
*   **Pre Tool Use:** Triggers *before* an agent invokes a tool. (Use: Validating permissions or adding instructions).
*   **Post Tool Use:** Triggers *after* a tool completes. (Use: Reviewing the output of a "write" command).
*   **Pre/Post Task Execution:** Triggers before or after a **Feature Spec** task. (Use: Environment preparation or verification).

### 4. Manual Trigger
*   **On-Demand:** Triggers that do not rely on an event but are executed manually by the user via the UI.

**Targeting Specific Tools:**
When using Pre/Post Tool triggers, you can target specific tools using prefixes:
*   `@builtin`: All native Kiro tools.
*   `@mcp`: All external MCP tools.
*   `@powers`: All installed Powers.
*   **Regex Support:** You can use patterns like `@mcp.*sql.*` to target specific tool families.

---

## Hook Actions (The "What")

Once a trigger is detected, Kiro performs one of two types of actions.

### 1. Agent Prompt ("Ask Kiro")
The agent is given a new set of instructions to process.
*   **Mechanism:** The prompt is sent to the LLM. In the case of `Prompt Submit`, the hook's instructions are *appended* to the user's message.
*   **Best For:** Tasks requiring reasoning, natural language, or creative implementation (e.g., "Review this code for security").
*   **Note:** These actions consume LLM credits as they initiate a new agent loop.

### 2. Shell Command ("Run Command")
A local terminal command is executed on your machine.
*   **Mechanism:** 
    *   **Success (Exit Code 0):** The command's output (`stdout`) is added to the agent's context.
    *   **Failure (Non-zero Exit Code):** The error (`stderr`) is sent to the agent, and the agent is notified of the failure. In "Pre" triggers, this also blocks the original action.
*   **Best For:** Deterministic, non-reasoning tasks (e.s., `npm test`, `eslint --fix`, `mkdir`).
*   **Note:** These are faster and do not consume LLM credits.

---

## Implementation & Best Practices

### Creation Methods
*   **Natural Language:** Describe the workflow to Kiro (e.g., "Every time I save a .ts file, run the linter") and let Kiro generate the JSON configuration.
*   **Manual Configuration:** Use the Hook UI to precisely define the Title, Event, Tool/File patterns, and Action.

### Design Principles
*   **Specificity:** Use precise file patterns (e.g., `src/components/**/*.tsx`) rather than global patterns to avoid performance degradation and unnecessary executions.
*   **Single Responsibility:** Each hook should perform one specific task. Avoid "God Hooks" that attempt to do too much.
*   **Security:** 
    *   **Validate Inputs:** Ensure hooks handle unexpected file content gracefully.
    *   **Limit Scope:** Avoid running broad shell commands on every file save; target only the relevant directories.
*   **Reliability:** For Shell Command actions, implement appropriate timeouts (default is 60s) to prevent hanging processes.

### Summary of Action Logic

| Feature | Agent Prompt (Ask Kiro) | Shell Command (Run Command) |
| :--- | :--- | :--- |
| **Core Strength** | Reasoning & Creativity | Deterministic Execution |
| **Speed** | Slower (LLM Latency) | Faster (Local Execution) |
| **Cost** | Consumes LLM Credits | Free (Local Resource) |
| **Ideal Use** | Code review, refactoring advice | Linting, testing, file manipulation |