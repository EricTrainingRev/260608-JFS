# Kiro MCP (Model Context Protocol) Reference Guide

## Overview of MCP
The Model Context Protocol (MCP) is an open standard that allows Kiro to communicate with external servers. This transforms Kiro from a localized editor into a powerful orchestrator capable of interacting with the broader software ecosystem.

### Capabilities of MCP
By connecting to MCP servers, Kiro gains several high-level abilities:
* **Knowledge Expansion:** Access specialized documentation (e.g., AWS, Stripe) directly in chat.
* **Service Integration:** Connect to external APIs and third-party services.
* **Custom Tooling:** Run domain-specific tools created for your specific organizational workflows.
* **Dynamic Templates:** Use server-provided prompt and resource templates via the `#` mention system.

---

## Configuration & Management

MCP servers can be configured at two levels of scope, allowing for both personal and project-specific toolsets.

### 1. Configuration Scopes
* **User Level (`~/.kiro/settings/mcp.json`):** Global configuration. Best for tools you use across every project (e.g., web search, personal API integrations).
* **Workspace Level (`.kiro/settings/mcp.json`):** Project-specific configuration. Best for tools tied to a specific codebase (e.g., a database explorer for a local dev environment).

**Precedence:** Workspace configurations override User configurations if a conflict occurs.

### 2. Configuration Structure
Configurations are defined in JSON. There are two primary server types:

#### **Local Servers**
Run as a process on your machine via a command.
```json
{
  "mcpServers": {
    "local-tool": {
      "command": "npx",
      "args": ["-y", "@example/server"],
      "env": { "API_KEY": "${MY_KEY}" }
    }
  }
}
```

#### **Remote Servers**
Connect via a network endpoint.
```json
{
  "mcpServers": {
    "remote-service": {
      "url": "https://api.example.com/mcp",
      "headers": { "Authorization": "Bearer token" }
    }
  }
}
```

### 3. Key Configuration Properties
| Property | Type | Description |
| :--- | :--- | :--- |
| `command` / `url` | String | The executable path or HTTPS endpoint. |
| `env` | Object | Environment variables passed to the server. |
| `disabled` | Boolean | Set to `true` to temporarily deactivate a server. |
| `autoApprove` | Array | List of tool names to run without prompting (use `["*"]` for all). |
| `disabledTools` | Array | Specific tools within a server to hide from the Agent. |

---

## Security & Governance

Because MCP servers run code outside of Kiro's primary sandbox, a robust security model is required to protect your system and credentials.

### 1. Protecting Credentials
* **Environment Variable Expansion:** Never hardcode API keys in your JSON config. Use the `${VARIABLE_NAME}` syntax to pull values from your system environment.
* **Kiro Variable Approval:** Kiro will not expand an environment variable unless it has been explicitly approved in **Settings $\rightarrow$ Mcp Approved Env Vars**.
* **Least Privilege:** When creating API tokens for MCP use (e.g., GitHub tokens), use fine-grained tokens with the absolute minimum permissions required.

### 2. The Tool Approval Workflow
Kiro maintains a "Human-in-the-loop" requirement for all tool executions to prevent unauthorized actions.
* **Manual Approval:** By default, Kiro prompts you to review the tool and its parameters before execution.
* **Auto-Approval Strategy:** Only use `autoApprove` for tools that are:
    1. From a verified, trusted source.
    2. Read-only (no write access to sensitive systems).
    3. Frequently used in your specific workflow.

### 3. Monitoring and Auditing
To ensure the integrity of your environment, use the following diagnostic tools:
* **MCP Logs:** Monitor all communication and errors via the **Output** tab in the Kiro panel (select `Kiro - MCP Logs`).
* **Configuration Permissions:** For sensitive user-level configs, restrict file permissions using `chmod 600`.
* **Incident Response:** If a server behaves unexpectedly, immediately set `"disabled": true` in your config and revoke any associated API tokens.

---

## Troubleshooting Common Issues

| Issue | Potential Cause | Solution |
| :--- | :--- | :--- |
| **Connection Failure** | Prerequisites missing | Verify the server's specific requirements (e.g., Node.js version). |
| **Permission Error** | Invalid Tokens | Check your environment variables and token scopes. |
| **Tool Not Responding** | Server Crash | Review the **Kiro - MCP Logs** for specific error messages. |
| **Config Not Loading** | Syntax Error | Validate your `mcp.json` for valid JSON formatting. |