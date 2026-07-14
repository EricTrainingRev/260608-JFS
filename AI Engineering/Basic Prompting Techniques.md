# Prompt Engineering Techniques for LLMs

## Overview
- Prompt engineering is the strategic structuring of inputs to guide Large Language Model (LLM) probabilistic reasoning. It is the primary mechanism for controlling output quality, accuracy, and format during the interaction lifecycle.

## Key Concepts
- **Zero-Shot:** Providing a task without any prior examples.
- **Few-Shot:** Providing a small set of examples to establish pattern/format.
- **Chain-of-Thought (CoT):** Forcing step-by-step reasoning to improve logic.
- **Role Prompting:** Assigning a specific persona to set tone and expertise.
- **Negative Prompting:** Explicitly defining constraints and prohibited elements.
- **Self-Reflection:** Instructing the model to critique and revise its own output.
- **Delimiters:** Using markers (e.g., `"""`, `###`) to separate instructions from data.

## Core Breakdown
### Prompting Modalities
- **Instruction-Based (Direct Control)**
    - Defines the specific verb/action (e.g., "Summarize", "Debug").
    - Sets the boundaries of the task to prevent scope creep.
- **Pattern-Based (Implicit Control)**
    - Uses Few-Shot examples to "train" the model on the fly.
    - Mimics desired syntax, tone, or structural patterns without prose.

### Cognitive Strategies
- **Reasoning Enhancement**
    - Chain-of-Thought (CoT) breaks complex logic into manageable tokens.
    - Least-to-Most decomposition prevents the model from skipping intermediate steps.
- **Error Mitigation**
    - RAG-style (Retrieval-Augmented) prompting provides grounding data.
    - Self-Correction loops utilize the model's evaluative capabilities to fix hallucinations.

## Workflow / How It Works
1. **Contextualization** → Define Role + Task + Data → Establishes the "search space" for the model.
2. **Constraint Application** → Add Delimiters + Negative Constraints → Narrows the probability of incorrect or undesired outputs.
3. **Reasoning Trigger** → Append "Let's think step by step" → Converts pattern-matching into sequential logic.
4. **Iterative Refinement** → Review output → Apply feedback loops to correct drift or tone errors.

## Important Relationships / Gotchas
- **Instruction vs. Data:** Without delimiters, the model may mistake your data for new instructions (Prompt Injection).
- **Temperature vs. Logic:** High temperature increases creativity but exponentially increases the risk of logical hallucinations.
- **Context Window Limits:** Extremely long prompts/context may lead to "Lost in the Middle" phenomena where the model ignores central info.
- **Zero-Shot vs. Few-Shot:** Zero-shot relies on pre-trained weights; Few-shot relies on in-context learning.

## Best Practices
- Use specific, high-signal verbs (e.g., "Analyze" instead of "Look at").
- Use delimiters (e.g., `###` or `"""`) to wrap input text.
- Avoid vague instructions like "Be brief" because "brief" is subjective; use "Under 50 words" instead.
- If the model fails a complex task, use Least-to-Most prompting to break it into sub-tasks.
- Always provide "Ground Truth" context when asking for factual extraction to minimize hallucinations.

## Example
- **Scenario [Complex Logic Task]:** 
    - **Context:** A user needs to solve a multi-step math word problem.
    - **Action:** Prompt: "You are a mathematician. Solve the following problem step-by-step. [Problem Text]. Check your work for errors before providing the final answer."
    - **Result:** The model outputs the logical steps, verifies the calculation, and provides a verified final result.

## Quick Reference
- **Logic/Math:** Low Temp (0.1–0.3) + CoT + Few-Shot.
- **Creative Writing:** High Temp (0.7–0.9) + Role Prompting.
- **Factual Extraction:** RAG-style + Negative Constraints + Delimiters.
