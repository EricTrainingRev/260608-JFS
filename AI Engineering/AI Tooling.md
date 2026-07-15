# AI Tooling

## Overview
- The ecosystem of software, frameworks, and infrastructure used to design, train, deploy, and manage artificial intelligence models throughout their lifecycle.
- It transforms raw mathematical theory and massive compute into functional, reliable, and scalable intelligence.

## Key Concepts
- **Frameworks:** Libraries that provide the mathematical building blocks for neural networks.
- **MLOps:** The practice of applying DevOps principles (automation, CI/CD) to the machine learning lifecycle.
- **Orchestration:** The coordination of multiple AI components (models, tools, memory) to complete a task.
- **Vector Database:** Specialized storage for high-dimensional mathematical representations of data (embeddings).
- **Agentic Workflow:** A paradigm where AI uses "tools" to interact with external environments (terminals, APIs, web).

## Core Breakdown
### Development & Training (The Build)
- **Creating the model's intelligence from raw data and mathematics.**
    - Frameworks (PyTorch, TensorFlow) for defining architecture.
    - Data Labeling (Scale AI) to create ground-truth training sets.
    - Distributed Training to scale across thousands of GPUs.

### Deployment & Operations (The Run)
- **Moving models from a research lab to a production environment.**
    - Model Serving (Triton, vLLM) to provide API access to the model.
    - MLOps (MLflow) to track model versions and performance.
    - Observability (Arize) to detect "drift" or hallucinations in real-time.

### Agentic Execution (The Act)
- **Giving the model "hands" to perform actions in the real world.**
    - Orchestration (LangChain, LlamaIndex) to manage complex logic chains.
    - Execution Harnesses (Claude CLI, OpenCode) to provide secure sandboxes for tool use.

## Workflow / How It Works
1. **Data Preparation** → Clean, label, and vectorize data to create a high-quality training/retrieval set.
2. **Model Training/Fine-tuning** → Optimize weights using frameworks and high-compute hardware.
3. **Deployment/Serving** → Package the model into an API or container for scalable access.
4. **Agentic Loop** → The model receives a goal $\rightarrow$ selects a tool $\rightarrow$ executes via a harness $\rightarrow$ observes result $\rightarrow$ iterates.

## Important Relationships / Gotchas
- **Compute-Data Dependency:** Model performance is a function of both hardware scale (Compute) and dataset quality (Data).
- **The "Garbage In, Garbage Out" Trap:** High-end orchestration (LangChain) cannot fix a poorly trained or hallucinating base model.
- **Latency vs. Capability:** Complex agentic loops (multiple reasoning steps) significantly increase response time.
- **Environment Leakage:** Running agentic tools (like a CLI) without a sandbox can allow the AI to execute destructive commands on a host system.

## Best Practices
- **Modularize the Stack:** Use specialized tools for each layer (e.g., Pinecone for memory, vLLM for serving) rather than monolithic solutions.
- **Implement Observability Early:** Monitor for "concept drift" (when real-world data diverges from training data).
- **Use Sandboxed Execution:** Always run agentic tools (OpenCode, etc.) in isolated containers/environments.
- **Avoid Over-engineering orchestration** because simple prompting is often more cost-effective and lower latency.

## Example
- **Scenario [Software Engineering Agent]:** A developer uses a coding agent (OpenCode) $\rightarrow$ The agent analyzes the codebase $\rightarrow$ The agent writes a patch $\rightarrow$ The agent runs unit tests in a sandbox $\rightarrow$ The patch is verified and submitted.

## Quick Reference
- **Foundational:** PyTorch, TensorFlow, JAX.
- **Memory/Retrieval:** Pinecone, Milvus, Weaviate.
- **Orchestration:** LangChain, LlamaIndex, CrewAI.
- **Execution/Harness:** Claude CLI, OpenCode, Jupyter.