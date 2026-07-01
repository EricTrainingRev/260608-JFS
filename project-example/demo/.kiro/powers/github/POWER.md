---
name: "github"
displayName: "GitHub Operations"
description: "Steering and best practices for GitHub operations via the GitHub MCP server (pull requests, issues, branches, code reviews)"
keywords: ["github", "pull request", "PR", "issue", "branch", "merge", "commit", "repository", "code review", "git"]
author: "Local"
---

# GitHub Operations Power

This power provides on-demand access to the GitHub MCP server and guidance for working with its tools. When activated, it exposes GitHub tools for managing pull requests, issues, branches, and repository operations.

# Onboarding

## Step 1: Ensure authentication

The GitHub MCP server authenticates via `GITHUB_TOKEN`. Make sure this environment variable is set and contains a valid token with appropriate scopes (repo, read:org, etc.).

## Step 2: Verify connectivity

After activation, test connectivity by listing repositories or fetching repo info. If tools fail, check that the token is valid and the MCP server is enabled.

# When to Load Steering Files

- Creating, reviewing, or managing pull requests → `pull-requests.md`
- Working with issues (create, label, assign, close, search) → `issues.md`
- Branch management, repository info, and general repo operations → `repo-operations.md`
