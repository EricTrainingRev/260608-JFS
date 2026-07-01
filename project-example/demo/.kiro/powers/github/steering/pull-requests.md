# Pull Requests

## Creating a Pull Request

1. Ensure all changes are committed and pushed to a feature branch (never push directly to main).
2. Use the GitHub MCP tools to create the PR.
3. PR titles should be concise (under 70 characters). Use the body for details.
4. Structure PR descriptions with:
   - A brief summary of what changed and why
   - Any testing that was done
   - Notes on anything blocked or left for follow-up

## Reviewing Pull Requests

- Fetch PR details (diff, comments, review status) before providing feedback.
- When reviewing, focus on correctness, readability, and adherence to project conventions.
- Leave actionable comments — suggest specific fixes rather than vague observations.

## Merging

- Confirm CI checks have passed before merging.
- Prefer squash merges for feature branches to keep history clean unless the user specifies otherwise.
- Delete the remote branch after merge unless told otherwise.

## Common Workflows

- **Draft PRs**: Create as draft when work is in progress and not ready for review.
- **Linking issues**: Reference related issues in the PR body (e.g., "Closes #42").
- **Requesting reviewers**: Ask who should review if the user doesn't specify.
