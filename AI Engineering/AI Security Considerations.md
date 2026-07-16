# AI Security & Robustness
## Overview
- The discipline of protecting AI models, training pipelines, and deployment environments from specialized adversarial threats.
- It addresses vulnerabilities inherent in high-dimensional statistical learning that traditional software security fails to capture.
## Key Concepts
- **Adversarial Attack:** Manipulating inputs to trigger incorrect model classifications or behaviors.
- **Prompt Injection:** Using natural language to bypass safety guardrails or manipulate model intent.
- **Data Poisoning:** Corrupting training datasets to introduce malicious biases or hidden backdoors.
- **Model Extraction:** Reconstructing a proprietary model's weights or logic through repeated API querying.
- **Interpretability:** The ability to understand and trace the mathematical reasoning behind a model's output.
- **Adversarial Perturbation:** Subtle, often invisible changes to input data that cause massive shifts in model perception.
## Core Breakdown
### Attack Vectors
- **Input-Based Vulnerabilities**
    - Prompt Injection: Social engineering the LLM via text to ignore system instructions.
    - Adversarial Perturbations: Mathematical noise applied to vision/audio inputs to spoof classifications.
- **Lifecycle-Based Vulnerabilities**
    - Data Poisoning: Attacking the "supply chain" by injecting malicious data into training sets.
    - Model Extraction: Stealing IP via "distillation" through high-frequency input/output observation.
### Defense Mechanisms
- **Proactive Hardening**
    - Adversarial Training: Training models on intentionally corrupted data to increase robustness.
    - Differential Privacy: Adding mathematical noise to training to prevent memorization of sensitive data.
- **Operational Guardrails**
    - Observability: Monitoring for drift, hallucinations, and anomalous query patterns.
    - Sandboxing: Isolating agentic tool execution to prevent host system compromise.
## Workflow / How It Works
1. **Threat Modeling** → Identify potential entry points (e.g., public APIs, training scrapers, user prompts).
2. **Adversarial Testing** → Intentionally attack the model using perturbations or injections to find blind spots.
3. **Hardening/Vaccination** → Apply Adversarial Training or Differential Privacy to mitigate identified weaknesses.
4. **Continuous Monitoring** → Observe production outputs for signs of extraction attempts or prompt manipulation.
## Important Relationships / Gotchas
- **Scale vs. Auditability:** As dataset size increases (Petabytes), manual data auditing becomes mathematically impossible.
- **The Black Box Constraint:** High-performing models often suffer from low interpretability, making root-cause analysis difficult.
- **Security vs. Utility:** Excessive safety guardrails or differential privacy noise can degrade model accuracy and reasoning capability.
- **Logic vs. Perception:** Traditional security fixes code bugs; AI security must fix mathematical "blind spots."
## Best Practices
- **Adopt a Security-First AI Lifecycle:** Integrate adversarial testing during the training phase, not just at deployment.
- **Implement Robust Observability:** Monitor for unusual input patterns that indicate extraction or injection attempts.
- **Use Sandboxed Execution:** Always run AI agents with external tool access (APIs, CLIs) in isolated environments.
- **Avoid: Relying solely on LLM-based filters** because the filters themselves are susceptible to the same prompt injection attacks.
## Example
- **Scenario [Computer Vision Attack]:** An attacker applies specific, nearly invisible stickers to a "Stop" sign $\rightarrow$ The model's mathematical perception is shifted $\rightarrow$ The autonomous vehicle classifies the sign as "Speed Limit 65" $\rightarrow$ A safety failure occurs.
## Quick Reference
- **Primary Defense Pillars:** Adversarial Training, Differential Privacy, Sandboxing.
- **Core Threat Categories:** Injection (Prompt), Poisoning (Data), Extraction (Model).