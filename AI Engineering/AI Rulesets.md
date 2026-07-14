# Personal AI Rulesets & Prompt Engineering

## Overview
- The methodologies and structural patterns used to define, deliver, and enforce behavioral constraints on AI agents.
- It bridges the gap between raw model capability and specialized, reliable performance across different interfaces.

## Key Concepts
- **System Prompt:** A high-priority instruction set that defines the AI's identity and core constraints.
- **Instruction Drift:** The phenomenon where an AI gradually loses adherence to rules as the conversation context grows.
- **Context Window:** The finite amount of "memory" an AI has to hold both rules and conversation history.
- **Prompt Injection:** An attempt to bypass rules by providing user input that contradicts the system instructions.
- **Few-Shot Prompting:** Providing specific examples within a ruleset to demonstrate the desired behavior.

## Core Breakdown
### Delivery Mechanisms
- **Interface-Specific Methods**
    - **Web UI:** Uses "Custom Instructions" or "GPTs" to provide persistent, global rules.
    - **CLI/Terminal:** Uses environment variables, config files (`.yaml`/`.json`), or shell aliases for local, programmatic rule injection.
    - **API/Developer:** Uses the `system` role in a structured JSON message array to set authoritative directives.
    - **IDE/Coding:** Uses project-level files (e.g., `.cursorrules`) to apply context-aware rules to specific codebases.

### Structural Design Patterns
- **Instructional Architectures**
    - **Encapsulation:** Using clear delimiters (such as XML tags, Markdown headers, or bracketed sections) to separate instructions from user data.
    - **Modularity:** Breaking rules into specialized modules (e.g., `identity.md` vs `coding_style.md`) to manage token density.
    - **Declarative Constraints:** Using "Do [Action]" instead of "Don't [Action]" to align with probabilistic prediction patterns.

## Workflow / How It Works
1. **Definition** → Create a structured, modular ruleset using Markdown and XML tags.
2. **Selection** → Choose the delivery method (Global, Session-based, or Project-based) based on the task.
3. **Injection** → Pass the ruleset into the `system` role or local config file.
4. **Execution/Refinement** → The AI processes the rules $\rightarrow$ adheres to constraints $\rightarrow$ user adjusts rules based on performance.

## Important Relationships / Gotchas
- **System Role vs. User Prompt:** System instructions are mathematically prioritized by most models but can still be overridden by clever user input.
- **Complexity vs. Reliability:** Overly dense or contradictory rules increase the likelihood of instruction drift and hallucinations.
- **Token Density vs. Cost:** Large, "all-encompassing" rulesets increase the cost and latency of every interaction.
- **Standardization Gap:** Lack of a universal file format (like `.rules`) means rulesets are currently not easily portable between platforms.

## Best Practices
- **Use Delimiters for Boundaries:** Wrap different rule types in distinct delimiters (e.g., XML tags, Markdown headers, or brackets) to prevent the model from confusing rules with data.
- **Prioritize Declarative Language:** Define what the AI *should* do rather than what it *should not* do to guide the model's probability flow.
- **Modularize Rulesets:** Maintain a library of small, task-specific rules instead of one massive, monolithic prompt.
- **Avoid: Verbose Prose** because high information density in Markdown is easier for the model to parse and follow.
- **Provide Examples:** Use "Few-Shot" examples within the rules to clarify ambiguous instructions (e.g., "concise").

## Example
- **Scenario [Portable Ruleset Deployment]:** A developer writes a modular `coding_rules.md` $\rightarrow$ The developer copies the content into a `.cursorrules` file for a new project $\rightarrow$ The IDE agent automatically adopts the specific linting and architectural constraints of that project.

## Quick Reference
- **Structure:** Role $\rightarrow$ Context $\rightarrow$ Constraints $\rightarrow$ Examples.
- **Formats:** Markdown (Hierarchy), XML (Encapsulation), YAML (Config).
- **Key Goal:** Minimize "Instruction Drift" through clarity and structure.