# Test Case Design Quick Reference Guide

## Overview of Test Case Design

A **Test Case** is a formal collection of resources required to verify a specific function or feature. Rather than just "trying things out," a structured test case provides a repeatable roadmap for validation.

### Anatomy of a Test Case
To be effective and repeatable, a test case should define:
*   **Preconditions:** The state the system must be in before testing begins (e.g., "User is logged in").
*   **Inputs:** The specific data being fed into the system.
*   **Actions:** The specific steps the tester (or script) must take.
*   **Expected Results:** The precise outcome that signifies success.

## The Testing Pyramid: Levels of Abstraction

When designing a test suite, it is critical to balance the *number* and *type* of tests. The **Testing Pyramid** is a visual representation of this balance, organized by levels of abstraction.

### The Pyramid Hierarchy
The pyramid is wide at the bottom and narrow at the top. This is because lower-level tests are faster and cheaper to run, while higher-level tests are more complex and slower.

1.  **Unit Tests (The Base):** The most granular level. They test individual components in isolation. Because they are isolated, **data mocking** is often used to simulate external dependencies.
2.  **Integration Tests (The Middle):** Focus on the "handshakes" between components. They verify that logically related modules work together and that dependencies function as expected.
3.  **System Tests (The Upper Middle):** Evaluates the entire application as a whole. This identifies "emergent" defects—issues that only appear when all modules are engaged together.
4.  **Acceptance Tests (The Apex):** The highest level of abstraction. These determine if the system meets the "acceptability" standards of stakeholders, regulators, or end-users. This often involves subjective assessments of usability and intuition.

**Conceptual Summary:**
| Level | Scope | Focus | Speed |
| :--- | :--- | :--- | :--- |
| **Unit** | Individual Component | Logic & Isolation | Very Fast |
| **Integration** | Group of Modules | Interaction & Dependencies | Moderate |
| **System** | Entire Application | End-to-End Behavior | Slow |
| **Acceptance** | User/Stakeholder Needs | Value & Usability | Slowest |

## Visibility Models: How We View the System

Test design is heavily influenced by how much knowledge the tester has regarding the internal workings of the application.

### 1. White Box Testing (Structural)
The tester has full access to the "internals"—the source code, configurations, and architecture.
*   **Goal:** To design tests that thoroughly examine the internal logic and code paths.
*   **Statement Testing:** A subset of White Box testing where the goal is to execute specific lines of code. This is often measured by **Code Coverage** (e.g., "80% coverage" means 80% of the lines were executed during testing).

### 2. Black Box Testing (Functional)
The tester treats the system as an opaque box. They do not see the code; they only see inputs and outputs based on documentation and specifications.
*   **Goal:** To verify that the application behaves correctly from an external, user-centric perspective.
*   **Common Techniques:** Use Case Testing, Boundary Value Analysis, and Equivalence Partitioning.

## Test Design Techniques: The "How-To"

Once you know *what* level you are testing and *how much* you can see, you use specific techniques to generate your test data and scenarios.

### 1. Logical & Structural Techniques
These techniques help organize complex requirements into manageable test patterns.

*   **Use Case Testing:** Uses real-world user stories to identify end-to-end scenarios. It helps ensure the system meets both technical requirements and user expectations.
*   **State Transition Diagram:** A flowchart used when a system moves through different "states" (e.g., a request moving from `Pending` $\rightarrow$ `Accepted` $\rightarrow$ `Rejected`). This helps organize tests and prevents redundant overlap.
*   **Decision Table Testing:** A method for managing complex combinations of inputs. If a user must make multiple decisions (like a login form with various combinations of valid/invalid credentials), a table ensures every possible combination is covered.
*   **Conditional Testing:** Used for "either/or" scenarios where the outcome depends on specific conditions (e.g., "If logged in, show profile; if not, show login").

### 2. Data Optimization Techniques
These techniques prevent "test bloat" by reducing the number of test cases while maintaining high coverage.

*   **Equivalence Partitioning:** Dividing data into "classes." Instead of testing every possible file type, you test one representative from the "Image" class and one from the "Non-Image" class.
*   **Boundary Value Analysis (BVA):** Focusing on the "edges" of requirements. If a username must be 6–12 characters, you test the boundaries: 5 (invalid), 6 (valid), 12 (valid), and 13 (invalid).
*   **Data-Driven Testing:** Running the same test script multiple times using different data sets from an external source (CSV, Database, etc.). This is highly efficient for testing a single logic path against a wide variety of inputs.

### 3. Intuitive & Exploratory Techniques
These rely on human intelligence rather than strict documentation.

*   **Error Guessing:** Leveraging the tester's experience to make "educated guesses" about where the code is most likely to fail.
*   **Exploratory Testing:** A simultaneous process of learning, designing, and executing tests. The tester investigates the software in real-time without a predefined script, responding to how the application actually behaves.
*   **Checklist Testing:** Following a pre-determined list of tasks (e.g., "Verify login," "Check navigation"). This is excellent for ensuring consistency and for **Regression Testing**.

### Summary of Techniques

| Technique | Category | Primary Benefit |
| :--- | :--- | :--- |
| **Use Case** | Black Box | Ensures real-world user flow. |
| **BVA / Equivalence** | Data Optimization | Reduces redundancy; finds edge-case bugs. |
| **Decision Table** | Logical | Ensures all input combinations are covered. |
| **State Transition** | Logical | Manages complex workflows/lifecycle states. |
| **Error Guessing** | Intuitive | Finds bugs that formal methods miss. |
| **Data-Driven** | Automation | Efficiently tests large volumes of data. |