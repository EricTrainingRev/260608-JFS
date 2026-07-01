# Repository Operations

## Branch Management

- List branches to understand the current state of the repo.
- Create branches from `main` (or the default branch) unless the user specifies a different base.
- Use descriptive branch names: `feature/add-login`, `fix/null-pointer-book-service`, `chore/update-deps`.
- Delete stale branches after their PRs are merged.

## Repository Information

- Fetch repo metadata (description, visibility, default branch, topics) when the user asks about the project setup.
- List collaborators or check permissions when relevant.

## Commits & History

- Browse recent commits to understand what's changed.
- Use commit history to identify when a bug was introduced or who last touched a file.

## Releases & Tags

- List releases to see what's been published.
- Create releases with proper semantic versioning and release notes when asked.

## Forking & Cloning

- Fork repos when contributing to external projects.
- Provide clone URLs when the user needs them.

## Best Practices

- Always confirm the current default branch before operations that depend on it.
- When in doubt about destructive operations (force push, branch deletion), ask the user first.
- Keep branch names lowercase with hyphens — no spaces or special characters.
