---
name: smell
description: Performs a code review looking for common code smells and assessing how easy to read and follow the code is. Use when you want a quick health check on code quality.
---

# Code Smell Review

Perform a thorough code review of the provided code, focusing on two areas: **common code smells** and **readability/clarity**.

Use the checklist in [references/smells.md](references/smells.md) to identify issues, and use the checklist in [references/readability.md](references/readability.md) to assess how easy the code is to follow.

## Output Format

For each issue found, report:

1. **Location** — file and line(s)
2. **Smell/Issue** — which code smell or readability problem
3. **Severity** — low / medium / high
4. **Suggestion** — a brief, actionable recommendation to fix it

End with a short summary rating overall code health: clean / minor issues / needs refactoring / significant concerns.
