# AI Task Framing

## Overview
- The engineering process of translating nebulous human objectives into structured, high-probability computational instructions for probabilistic models.
- It serves as the critical bridge between human intent and machine execution, determining the reliability and accuracy of AI outputs.

## Key Concepts
- **Task Framing:** Defining the persona, constraints, context, and output schema to steer a model toward a specific objective.
- **Task Decomposition:** Breaking a complex macro-task into a hierarchy of discrete, manageable micro-tasks.
- **Chain-of-Thought (CoT):** Prompting techniques that encourage the model to output intermediate reasoning steps to improve logical accuracy.
- **Few-Shot Learning:** Providing specific input-output examples within the context to condition the model's behavior.
- **Neuro-symbolic Approach:** Integrating probabilistic neural networks (for reasoning/intuition) with deterministic code (for logic/execution).
- **Alignment:** Ensuring the model's optimized objective functions remain consistent with human values and safety constraints.

## Core Breakdown

### Structural Framing (The Architecture)
- **Defining the operational boundaries of the model.**
    - Persona Assignment: Establishing the expertise level and tone of the agent.
    - Constraint Definition: Setting explicit "do" and "do not" boundaries to prevent hallucinations or scope creep.
    - Output Schema: Using structured formats (e.g., JSON) to ensure compatibility with downstream deterministic systems.

### Functional Decomposition (The Strategy)
- **Reducing entropy by narrowing the scope of inference.**
    - Perception Layer: Using AI to parse, extract, or summarize unstructured data into structured inputs.
    - Reasoning Layer: Leveraging the model's semantic capabilities to evaluate or compare extracted data.
    - Decision Layer: Passing processed data into a deterministic logic engine to trigger specific actions.

## Workflow / How It Works
1. **Objective Identification** → Define the ultimate human goal to establish the high-level intent.
2. **Decomposition** → Partition the goal into a sequence of micro-tasks to reduce variance and error probability.
3. **Instruction Conditioning** → Apply persona, constraints, and few-shot examples to each micro-task to guide the model's manifold.
4. **Structured Extraction** → Force the model to output via a schema (e.g., JSON) to facilitate reliable programmatic handling.
5. **Verification/Eval Loop** → Run the output through "Evals" to identify drift and iteratively refine the frame.

## Important Relationships / Gotchas
- **The Alignment-Complexity Tradeoff:** More complex frames reduce error but increase latency and token cost.
- **Determinism vs. Probability:** Using LLMs for purely mathematical/logic-based tasks is a framing error; use traditional code for those modules.
- **The "Stochastic Parrot" Misconception:** Model failures are often failures of instruction (framing) rather than a lack of underlying intelligence.
- **Intent Leakage:** Vague instructions lead to "hallucinated interpretation" where the model fills gaps with statistically likely but factually incorrect data.

## Best Practices
- **Modularize via Pipelines:** Break complex workflows into a series of single-purpose LLM calls rather than one monolithic prompt.
- **Use Negative Constraints:** Explicitly define what the model *must not* do to prevent unintended behaviors.
- **Implement Evals Early:** Build a testing suite of edge cases to quantify how well your framing holds up under pressure.
- **Avoid: Vague Directives** (e.g., "Fix this report") because they provide insufficient context for the model's probabilistic engine.
- **Optimization Tip:** Use "Chain-of-Thought" prompting for reasoning-heavy tasks to increase the probability of correct logical paths.

## Example
- **Sequence [Supply Chain Agent]:** Macro-goal (Manage Supply) $\rightarrow$ Decompose (Parse Invoice $\rightarrow$ Compare to History $\rightarrow$ Flag Variance) $\rightarrow$ Parse (LLM extracts JSON) $\rightarrow$ Compare (LLM reasons on delta) $\rightarrow$ Flag (Deterministic code triggers alert).

## Quick Reference
- **High-Variance Tasks:** Semantic extraction, reasoning, summarization, creative synthesis.
- **Low-Variance Tasks (Use Code):** Arithmetic, database queries, strict logical branching, data formatting.
- **Framing Components:** Persona + Context + Constraints + Examples + Output Schema.