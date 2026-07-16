# Prompt PII Guardrails

## Overview
- Security layers designed to intercept, detect, and mitigate the leakage of Personally Identifiable Information (PII) within LLM prompt-response streams.
- They address the risk of sensitive data being ingested by models for training or leaked via probabilistic outputs during conversational inference.

## Key Concepts
- **PII (Personally Identifiable Information):** Any data that can be used to uniquely identify, contact, or locate a single person.
- **Redaction:** The process of removing sensitive substrings and replacing them with generic placeholders (e.g., "[NAME]").
- **Anonymization:** Transforming data to preserve semantic utility while stripping identity (e.g., "Jane Doe" $\rightarrow$ "Patient A").
- **NER (Named Entity Recognition):** A machine learning technique used to identify and categorize entities like names, locations, and organizations.
- **Prompt Injection:** Malicious input designed to bypass security filters or instruction sets (e.g., "ignore previous instructions").
- **Quasi-identifiers:** Non-sensitive data points that, when combined, can re-identify an individual (e.g., ZIP code + DOB + Gender).

## Core Breakdown
### Detection Mechanisms
- **Pattern-Based (Regex):** Identifying structured data via deterministic character patterns.
    - High speed and low compute cost.
    - Limited to predictable formats like SSNs, credit cards, or phone numbers.
- **ML-Based (NER):** Using probabilistic models to identify unstructured entities.
    - Capable of detecting context-dependent identity data.
    - Higher computational overhead/latency than Regex.

### Mitigation Strategies
- **Interception Layers:** Acting as a buffer between the User and the LLM.
    - **Redaction:** Maximizes privacy but can degrade model utility/context.
    - **Anonymization:** Uses proxy identifiers to balance privacy with semantic preservation.
    - **Rejection:** Hard-blocking the prompt entirely to prevent any processing of sensitive data.

## Workflow / How It Works
1. **Input Interception** → The guardrail captures the raw user prompt before it reaches the LLM to prevent data ingestion.
2. **Scanning & Classification** → Detection engines (Regex + NER) identify PII and quasi-identifiers within the text.
3. **Transformation/Mitigation** → The engine applies redaction, anonymization, or rejection based on predefined security policies.
4. **Clean Prompt Delivery** → The scrubbed, non-sensitive prompt is sent to the LLM for processing.
5. **Output Inspection (Optional)** → The model's response is scanned for "Inference Leakage" before being returned to the user.

## Important Relationships / Gotchas
- **Utility-Privacy Tradeoff:** Higher levels of redaction increase security but decrease the model's ability to provide contextually accurate reasoning.
- **The Semantic Gap:** Regex cannot catch "quasi-identifiers"; only context-aware ML models can mitigate re-identification risks.
- **The Prompt Injection Arms Race:** Adversaries use sophisticated linguistic tricks (e.g., encoding, obfuscation) to bypass detection layers.
- **Training vs. Inference Leakage:** Guardrails protect real-time prompts, but do not inherently fix privacy issues caused by PII already present in a model's weights.

## Best Practices
- **Implement Context-Preserving Anonymization:** Use proxy identifiers (e.g., "User 1") instead of total redaction to maintain LLM reasoning utility.
- **Use Multi-Layered Defense:** Combine Regex (for speed/structure) with NER (for nuance/unstructured data).
- **Adopt "Zero Retention" Architectures:** For enterprise use, ensure PII is scrubbed before data hits any logging or training pipelines.
- **Avoid Relying Solely on Regex:** Because it fails to identify quasi-identifiers and complex semantic identities.
- **Sandbox Agentic Actions:** When using AI agents, implement "Policy-as-Code" to validate the intent of any PII-requiring action before execution.

## Example
- **Scenario [Medical Summary]:** User inputs "Summarize notes for Jane Smith, age 45" $\rightarrow$ Guardrail detects PII via NER $\rightarrow$ Guardrail anonymizes input to "Summarize notes for Patient A, age 45" $\rightarrow$ LLM receives scrubbed prompt $\rightarrow$ LLM returns a high-quality summary without exposing identity.