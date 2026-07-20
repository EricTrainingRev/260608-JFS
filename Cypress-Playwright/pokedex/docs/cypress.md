# Cypress Study Guide

## 1. Foundations

### Introduction
**Cypress** is a modern, next-generation end-to-end (E2E) testing framework built for the modern web. Unlike Selenium, which runs outside the browser and sends commands via a driver, Cypress runs **inside the browser** alongside your application code. This architecture provides native access to every object, allowing for much faster, more reliable, and more easily debuggable tests.

### Installation & Execution
Cypress is installed as a development dependency via npm.

**Installation:**
```bash
npm install cypress --save-dev
```

**Execution Modes:**
*   **Interactive Mode (The Test Runner):** Opens a GUI to watch tests run in real-time. Best for development.
    ```bash
    npx cypress open
    ```
*   **Headless Mode (CLI):** Runs tests in the background without a GUI. Best for CI/CD pipelines.
    ```bash
    npx cypress run
    ```

---

## 2. Core Execution Engine

### Test Structure (Mocha BDD Syntax)
Cypress uses Mocha's Behavior Driven Development (BDD) syntax. Tests are organized into suites using `describe` blocks and individual test cases using `it` blocks.

| Hook | Description |
| :--- | :--- |
| `before()` | Runs once before the first test in the block. |
| `beforeEach()` | Runs before **every** individual test. Ideal for resetting state or visiting URLs. |
| `afterEach()` | Runs after every individual test. |
| `after()` | Runs once after all tests in the block have finished. |

**Example Template:**
```javascript
describe('User Authentication Suite', () => {
  // Setup: Ensure we start from a clean slate
  beforeEach(() => {
    cy.visit('/login');
  });

  it('should allow a valid user to log in', () => {
    // Test logic here
  });

  it('should show an error for invalid credentials', () => {
    // Test logic here
  });
});
```

### Element Selection (Locators)
Cypress relies on the `cy.get()` command to find elements. While you can use standard CSS selectors, Cypress recommends specific strategies for stability.

**Selector Strategies:**
| Strategy | Example | Best Practice Note |
| :--- | :--- | :--- |
| **Data Attributes** | `cy.get('[data-cy=submit]')` | **Gold Standard.** Decouples tests from CSS/JS changes. |
| **Class Name** | `cy.get('.btn-primary')` | Fragile; styles change often. |
| **ID** | `cy.get('#login-btn')` | Stable, but IDs are not always unique in modern frameworks. |
| **Text Content** | `cy.contains('Submit')` | Good for user-centric testing, but fragile to localization. |

### Basic UI Interactions
Once an element is selected, you can perform human-like actions:
```javascript
cy.get('[data-cy=username]').type('myUser123');      // Types text
cy.get('[data-cy=password]').type('password123{enter}'); // Types and hits Enter
cy.get('[data-cy=submit-btn]').click();               // Clicks element
cy.get('[data-cy=checkbox]').check();                 // Checks a checkbox
cy.get('[data-cy=radio]').uncheck();                  // Unchecks a radio button
cy.get('[data-cy=dropdown]').select('Option 1');      // Selects from <select>
```

---

## 3. Advanced Capabilities

### API Testing & Asynchronous Handling
Cypress is not limited to the UI. You can perform network requests directly, which is useful for setting up state (e.g., creating a user via API before testing their login via UI).

**Key Feature: Automatic Waiting**
Unlike Selenium, you rarely need `sleep()` or manual waits. Cypress automatically waits for elements to exist, for animations to finish, and for assertions to pass before moving to the next command.

```javascript
// Testing an API endpoint directly
cy.request('POST', '/api/v1/login', {
  email: 'test@example.com',
  password: 'password'
}).then((response) => {
  expect(response.status).to.eq(200);
  expect(response.body).to.have.property('token');
});
```

### Component Testing
Beyond E2E, Cypress can test individual components (React, Vue, Angular) in isolation. This is faster than E2E and more realistic than unit testing because it runs in a real browser.

```javascript
// Example: React Component Test
import { mount } from 'cypress/react';
import MyButton from './MyButton';

it('renders the button with correct text', () => {
  mount(<MyButton label="Click Me" />);
  cy.get('button').should('contains.text', 'Click Me');
});
```

### Data-Driven Testing
You can run the same test logic against multiple datasets using JavaScript arrays or `cy.fixture()`.

```javascript
const testUsers = [
  { user: 'admin', pass: 'admin123', expected: 'Welcome Admin' },
  { user: 'editor', pass: 'edit456', expected: 'Welcome Editor' }
];

testUsers.forEach((data) => {
  it(`should verify access for ${data.user}`, () => {
    cy.visit('/login');
    cy.get('#user').type(data.user);
    cy.get('#pass').type(data.pass);
    cy.get('#submit').click();
    cy.get('.welcome-msg').should('contain', data.expected);
  });
});
```

---

## 4. Stability & Troubleshooting

### Debugging Tools
When a test fails, Cypress provides a "Time Travel" feature in the Test Runner, allowing you to hover over command logs to see the exact state of the app at that moment.

**Manual Debugging Methods:**
1.  **`.debug()`**: Pauses execution and opens the DevTools console.
    ```javascript
    cy.get('.slow-element').debug().click();
    ```
2.  **`cy.pause()`**: Stops the test execution entirely, allowing you to inspect the browser manually.
3.  **`debugger`**: Standard JS breakpoint used in conjunction with `.debug()`.

### Assertions
Cypress uses **Chai** for assertions. Assertions are usually chained to commands using `.should()`.

```javascript
cy.get('.error-msg').should('be.visible');           // Visibility
cy.get('.count').should('have.text', '5');           // Exact text
cy.get('.list-item').should('have.length', 3);       // Collection size
cy.get('input').should('have.value', 'John Doe');    // Input value
cy.get('.btn').should('not.be.disabled');            // Negative assertion
```

---

## 5. Architectural Patterns & Ecosystem

### Custom Commands (The "Page Object" Alternative)
Instead of heavy Page Object Models, Cypress encourages "Custom Commands." These are reusable functions defined in `cypress/support/commands.js` that extend the `cy` object.

**Defining a Command:**
```javascript
// cypress/support/commands.js
Cypress.Commands.add('loginViaUI', (email, password) => {
  cy.get('[data-cy=email]').type(email);
  cy.get('[data-cy=password]').type(password);
  cy.get('[data-cy=login-btn]').click();
});
```

**Using the Command:**
```javascript
it('tests dashboard after login', () => {
  cy.visit('/login');
  cy.loginViaUI('user@test.com', 'pass123'); // Reusable!
  cy.url().should('include', '/dashboard');
});
```

### Cypress Studio (Low-Code Testing)
**Cypress Studio** is an interactive recorder. It allows you to perform actions in the browser, and Cypress automatically generates the corresponding code steps.
*   **Best Use Case:** Creating E2E tests for existing, complex user flows.
*   **Limitation:** Not recommended for Test Driven Development (TDD); does not currently support Component Testing.

### The Ecosystem (Plugins & Dashboards)
Cypress is highly extensible through the plugin ecosystem:

| Category | Plugin Example | Purpose |
| :--- | :--- | :--- |
| **Accessibility** | `cypress-axe` | Checks for WCAG compliance and A11y violations. |
| **Visual Testing** | `cypress-image-snapshot` | Performs visual regression by comparing screenshots. |
| **Cloud Services** | **Cypress Cloud (Dashboard)** | Provides centralized reporting, parallelization, and video recordings for CI/CD. |

## 6. Advanced Testing & Collaboration

### Dashboards and Collaboration
In a professional team environment, testing is not just about running scripts locally; it is about visibility, speed, and reliability within a CI/CD pipeline.

**Cypress Cloud (The Dashboard):**
* **Parallelization:** The ability to split your test suite across multiple virtual machines. Instead of running 100 tests in 10 minutes, you can run 5 machines simultaneously to finish in ~2 minutes.
* **Flakiness Detection:** Automatically identifies "flaky" tests (tests that pass and fail inconsistently without code changes). This allows teams to quarantine unstable tests so they don't block the deployment pipeline.
* **Analytics & Reporting:** Provides visual trends on test suite health, failure rates, and execution time over months of development.
* **Video & Screenshot Archiving:** Automatically captures video of every test run in CI, making it easy for developers to see exactly what went wrong in a headless environment.

### Accessibility (A11y) Testing
Accessibility testing ensures the application is usable by people with disabilities (visual, motor, auditory, etc.). Cypress uses the `cypress-axe` plugin to integrate the **axe-core** engine.

**Implementation Workflow:**
1. **Install:** `npm install cypress-axe axe-core`
2. **Inject:** Use `cy.injectAxe()` before running an audit.
3. **Audit:** Use `cy.checkA11y()` to scan the DOM for violations.

**Example:**
```javascript
it('should have no accessibility violations on the login page', () => {
  cy.visit('/login');
  cy.injectAxe(); // Injects the axe-core engine into the page
  cy.checkA11y(); // Runs the audit and fails if violations are found
});
```
*Note: Common violations include missing `alt` text on images, low color contrast, and missing ARIA labels.*

### Visual Regression Testing
While functional testing checks if a button *works*, Visual Regression testing checks if the button *looks* correct. It catches unintended CSS changes that functional tests miss (e.g., a button turning invisible or moving off-screen).

**How it works:**
1. **Baseline Creation:** You run a test that captures a "Golden Image" (a perfect screenshot of the component/page).
2. **Comparison:** On subsequent runs, Cypress takes a new screenshot and compares it pixel-by-pixel against the baseline.
3. **Thresholds:** You define a "mismatch threshold." If the difference is greater than X%, the test fails.

**Example (using `cypress-image-snapshot`):**
```javascript
it('should look correct visually', () => {
  cy.visit('/dashboard');
  // Captures a screenshot and compares it to the stored baseline
  cy.matchImageSnapshot('dashboard-page'); 
});
```