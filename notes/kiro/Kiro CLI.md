# Kiro CLI Reference Guide

## Overview of Kiro CLI
The Kiro CLI brings AI-assisted development to the terminal. It is a highly versatile tool that can be used for interactive development, automated workflows in CI/CD pipelines, and as a backend agent for other editors.

### Primary Use Cases
* **Interactive Development:** Chatting with Kiro in a rich terminal interface.
* **Custom Automation:** Creating specialized agents for specific team workflows.
* **Headless Automation:** Running prompts non-interactively in CI/CD via API key authentication.
* **Protocol Integration:** Running Kiro as an agent within other editors (JetBrains, Zed) via the Agent Client Protocol (ACP).

---

## The Terminal UI (TUI) Experience
The default interface for `kiro-cli chat` is a rich, terminal-native UI designed for high-density information display and interactive control.

### Visual Components
* **Markdown Rendering:** Full support for syntax highlighting, tables, and lists.
* **Tool Display:** Visual indicators for tool execution (spinners, success/error icons, and progress bars). Use `Ctrl+O` to toggle between a summary and full output.
* **Thinking Display:** Streams the agent's reasoning blocks inline. Use `Ctrl+O` to expand/collapse long reasoning traces.
* **Overlay Panels:** Searchable, dismissible panels for `/help`, `/context`, `/tools`, and more.
* **Crew Monitor (`Ctrl+G`):** A real-time view of subagent activity and spawned sessions during multi-agent workflows.

### Interaction & Shortcuts
| Category | Shortcut | Action |
| :--- | :--- | :--- |
| **Editing** | `Shift+Enter` | Insert newline |
| | `Ctrl+K` / `Ctrl+U` | Kill to end / start of line |
| **Navigation** | `Ctrl+R` | Reverse incremental history search |
| | `Ctrl+T` | Open conversation transcript in pager |
| **Panels** | `Ctrl+X` | Toggle activity tray |
| | `Esc` | Close panels / cancel agent |
| **Agent Control**| `Tab` | Drill into approval options |
| | `Shift+Tab` | Enter **Plan Mode** |
| | `Ctrl+Z` (twice) | Suspend the session |

### Input Methods
* **Shell Escape:** Prefix any command with `!` to run a local shell command directly (e.g., `!npm run build`) without involving the agent.
* **File Referencing:** Use `@path` with tab-completion to attach files to your prompt.
* **Multi-line Input:** Use `/editor` to open your system `$EDITOR` for composing complex, multi-line prompts.

---

## Advanced Agent Configurations

### Custom Agents
Custom agents allow you to bypass the "one-size-fits-all" approach by defining specialized configurations.
* **Purpose:** Tailor tool access, pre-approve specific permissions, and include specific context automatically.
* **Use Case:** An "AWS Agent" that only has access to AWS CLI tools and has pre-approved permissions for `describe-instances`.

### Agent Client Protocol (ACP)
Kiro implements the **ACP standard**, allowing it to function as an agent inside non-Kiro editors.
* **Compatibility:** Works with JetBrains (via AI Assistant) and Zed.
* **Implementation:** Run `kiro-cli acp` and configure your editor to spawn this command via JSON.
* **Capabilities:** Supports session management, model selection, and Kiro-specific extensions like slash commands and MCP server notifications.

---

## Technical Configuration & Troubleshooting

### UI Engine Precedence
The interface type is determined by this priority order:
1. CLI Flag (`--tui` or `--classic`)
2. Environment Variable (`KIRO_CHAT_UI`)
3. Kiro Setting (`chat.ui`)
4. Default (`tui`)

### Troubleshooting Workflow
* **MCP/Tool Issues:** Check the **Output** tab in the Kiro panel and select `Kiro - MCP Logs`.
* **Rendering Artifacts:** If the TUI looks broken, try disabling synchronized output with `KIRO_NO_SYNCHRONIZED=1` or use a modern terminal like Ghostty, WezTerm, or iTerm2.
* **Command Failability:** Note that interactive shell commands (like `sudo` or `ssh` host prompts) are not supported and will exit immediately. Always use non-interactive flags (e.g., `npm init -y`).