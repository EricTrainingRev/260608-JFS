---
name: code-smell
description: Analyze code for common code smells including structural issues, naming problems, duplication, coupling, and design flaws. Use when reviewing code quality or preparing code for review.
---

# Code Smell Detection

When this skill is activated, analyze the provided code (or the active file) for common code smells. Be thorough but keep the feedback actionable and concise.

## What to Look For

### Structural Smells
- **Long Method** — methods doing too much; suggest extraction.
- **Large Class** — classes with too many responsibilities; suggest splitting.
- **God Object** — one class that knows/does everything.
- **Feature Envy** — a method that uses another class's data more than its own.
- **Data Clumps** — groups of parameters/fields that always travel together; suggest a DTO or value object.

### Naming & Clarity
- **Unclear naming** — vague variable/method/class names (e.g., `data`, `temp`, `process()`).
- **Magic numbers/strings** — unexplained literals; suggest constants or enums.
- **Inconsistent naming conventions** — mixing camelCase/snake_case, abbreviations vs. full words.

### Duplication & Coupling
- **Duplicated code** — repeated logic that could be extracted.
- **Tight coupling** — direct instantiation instead of dependency injection; missing interfaces.
- **Shotgun surgery** — a single change requiring edits in many places.

### Design Smells
- **Primitive obsession** — using primitives where a domain type would be clearer.
- **Refused bequest** — subclass ignoring inherited behavior.
- **Speculative generality** — abstractions that serve no current purpose.
- **Dead code** — unused methods, variables, imports, or commented-out blocks.

### Spring / Java Specific
- **Field injection** — prefer constructor injection for testability.
- **Missing `@Transactional`** — service methods that modify data without transaction boundaries.
- **Catching generic exceptions** — `catch (Exception e)` instead of specific types.
- **Returning entities directly from controllers** — suggest using DTOs.
- **Missing validation** — no `@Valid` or manual checks on incoming data.

## Output Format

For each smell found, provide:

1. **Smell name** (from the categories above or a custom one)
2. **Location** — file, class, method, or line range
3. **Why it's a problem** — one sentence
4. **Suggested fix** — brief, actionable recommendation

If the code is clean, say so! No need to invent problems. Keep the vibe constructive — we're helping future-us, not roasting past-us. 🌶️
