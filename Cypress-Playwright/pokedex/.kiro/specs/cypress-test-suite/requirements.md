# Requirements Document

## Introduction

This document defines the requirements for a Cypress E2E test suite that serves as a teaching demonstration for students. The test suite exercises a Pokedex Angular 20 application (login flow, route guarding, Pokemon search, and data display) while providing practical examples for each section of the Cypress study guide (docs/cypress.md). The suite must be isolated in its own directory to coexist with a future Playwright suite.

## Glossary

- **Test_Suite**: The complete set of Cypress test files, configuration, support files, and npm scripts that constitute the Cypress E2E testing demonstration
- **Cypress_Config**: The `cypress.config.ts` configuration file that defines Cypress behavior including baseUrl, spec patterns, and plugin registration
- **Custom_Command**: A reusable function registered on the `cy` object via `Cypress.Commands.add()` in the support file, used to encapsulate repeated test actions
- **Data_Attribute**: An HTML attribute following the pattern `data-cy="identifier"` added to Angular templates to provide stable element selectors for tests
- **Test_Runner**: The Cypress interactive GUI launched via `npx cypress open` that displays real-time test execution
- **Headless_Mode**: Cypress CLI execution via `npx cypress run` that runs tests without a GUI, suitable for CI/CD
- **Pokedex_App**: The Angular 20 application under test, served at `http://localhost:4200`
- **PokéAPI**: The public REST API at `https://pokeapi.co/api/v2/pokemon/{identifier}` used by the Pokedex_App to fetch Pokemon data
- **Auth_Guard**: The Angular route guard that checks `sessionStorage` for authentication status and redirects unauthenticated users to `/login`
- **Data_Driven_Test**: A test pattern where the same test logic is executed against multiple input datasets using JavaScript iteration
- **Accessibility_Audit**: An automated scan of the rendered DOM using the axe-core engine (via cypress-axe) to detect WCAG violations
- **Visual_Regression_Test**: A test that captures a screenshot and compares it pixel-by-pixel against a stored baseline image to detect unintended visual changes

## Requirements

### Requirement 1: Isolated Project Structure

**User Story:** As an instructor, I want the Cypress test suite to reside in its own isolated directory at the project root, so that a Playwright suite can be added later without conflicts.

#### Acceptance Criteria

1. THE Test_Suite SHALL place the Cypress configuration file (`cypress.config.ts`) at the project root with all test artifacts (specs, support files, fixtures, screenshots, and videos) scoped to a `cypress/` directory
2. THE Cypress_Config SHALL set the `e2e.specPattern` to `cypress/e2e/**/*.cy.ts` to target test files exclusively within the `cypress/e2e/` directory
3. THE Cypress_Config SHALL set the `e2e.supportFile` to `cypress/support/e2e.ts`
4. THE Test_Suite SHALL store custom command definitions in `cypress/support/commands.ts`
5. THE Test_Suite SHALL store fixture data files in `cypress/fixtures/` directory
6. THE Test_Suite SHALL import `cypress/support/commands.ts` from within the support file (`cypress/support/e2e.ts`) so that custom commands are registered before test execution

### Requirement 2: Cypress Configuration

**User Story:** As a student, I want the Cypress configuration to be pre-configured with sensible defaults, so that I can focus on learning test concepts rather than debugging configuration issues.

#### Acceptance Criteria

1. THE Cypress_Config SHALL set `e2e.baseUrl` to `http://localhost:4200`
2. THE Cypress_Config SHALL set `viewportWidth` to 1280 and `viewportHeight` to 720
3. THE Cypress_Config SHALL register the `@simonsmith/cypress-image-snapshot` plugin in the `e2e.setupNodeEvents` function
4. THE Cypress_Config SHALL use TypeScript format (`cypress.config.ts`) and export configuration via the Cypress `defineConfig` function

### Requirement 3: NPM Script Integration

**User Story:** As a student, I want npm scripts for running Cypress in both interactive and headless modes, so that I can practice both execution methods described in the study guide.

#### Acceptance Criteria

1. WHEN a student runs `npm run cy:open`, THE Test_Suite SHALL execute the command `cypress open`, launching Cypress in interactive mode (Test_Runner GUI)
2. WHEN a student runs `npm run cy:run`, THE Test_Suite SHALL execute the command `cypress run`, running all Cypress tests in Headless_Mode
3. THE Test_Suite SHALL define the `cy:open` and `cy:run` scripts in the project's `package.json` file under the `scripts` object
4. THE Test_Suite SHALL list `cypress` as a `devDependencies` entry in the project's `package.json` file

### Requirement 4: Data Attributes for Element Selection

**User Story:** As a student, I want the Angular templates to include `data-cy` attributes on key interactive elements, so that I can practice the recommended element selection strategy from the study guide.

#### Acceptance Criteria

1. THE Pokedex_App SHALL include a `data-cy="username-input"` attribute on the login username input field
2. THE Pokedex_App SHALL include a `data-cy="password-input"` attribute on the login password input field
3. THE Pokedex_App SHALL include a `data-cy="login-button"` attribute on the login submit button
4. WHILE the error message element is conditionally rendered, THE Pokedex_App SHALL include a `data-cy="error-message"` attribute on the login error message `<p>` element
5. THE Pokedex_App SHALL include a `data-cy="search-input"` attribute on the Pokemon search input field
6. THE Pokedex_App SHALL include a `data-cy="search-button"` attribute on the Pokemon search button

### Requirement 5: Login Flow E2E Tests

**User Story:** As a student, I want test files that demonstrate E2E login testing using describe/it blocks, hooks, assertions, and element interactions, so that I can learn core Cypress patterns through a real scenario.

#### Acceptance Criteria

1. THE Test_Suite SHALL include a test file that organizes login tests within a `describe` block containing at least 2 `it` blocks, using `beforeEach` to visit the `/login` page before each test
2. WHEN valid credentials (username: "dev", password: "dev") are typed into `[data-cy=username-input]` and `[data-cy=password-input]` and the `[data-cy=login-button]` is clicked, THE Test_Suite SHALL verify that the URL includes `/home` using `cy.url().should('include', '/home')`
3. WHEN invalid credentials (any username/password combination other than "dev"/"dev") are submitted, THE Test_Suite SHALL verify that the element `[data-cy=error-message]` containing the text "login failed, please try again" satisfies a `.should('be.visible')` assertion
4. THE Test_Suite SHALL select elements exclusively using `data-cy` attributes: `[data-cy=username-input]` for the username field, `[data-cy=password-input]` for the password field, and `[data-cy=login-button]` for the submit button
5. THE Test_Suite SHALL demonstrate the `.type()` command to enter text into input fields and the `.click()` command to submit the login form
6. THE Test_Suite SHALL demonstrate `.should()` assertions including `cy.url().should('include', '/home')` for URL validation, `.should('have.text', 'login failed, please try again')` for text content verification, and `.should('be.visible')` for element visibility

### Requirement 6: Data-Driven Login Tests

**User Story:** As a student, I want to see data-driven testing in action with multiple credential combinations, so that I can learn how to parameterize tests using JavaScript arrays.

#### Acceptance Criteria

1. THE Test_Suite SHALL include a test file that uses `forEach` to iterate over an array of credential objects, generating a separate `it` block for each entry dynamically
2. THE Test_Suite SHALL include at least three test data entries: one valid credential set (username: "dev", password: "dev") and two invalid credential sets (one with a wrong username, one with a wrong password), where each entry includes a property indicating whether the login is expected to succeed or fail
3. WHEN a valid credential set is used, THE Test_Suite SHALL type the credentials into `[data-cy=username-input]` and `[data-cy=password-input]`, click `[data-cy=login-button]`, and assert that the URL includes `/home`
4. WHEN an invalid credential set is used, THE Test_Suite SHALL type the credentials into `[data-cy=username-input]` and `[data-cy=password-input]`, click `[data-cy=login-button]`, and assert that the `[data-cy=error-message]` element is visible and contains the text "login failed, please try again"
5. THE Test_Suite SHALL use `beforeEach` within the data-driven test file to visit the `/login` page before each generated test case

### Requirement 7: Route Guard Tests

**User Story:** As a student, I want tests that verify the authentication guard behavior, so that I can understand how to test route protection in Angular applications.

#### Acceptance Criteria

1. WHEN an unauthenticated user navigates directly to `/home`, THE Test_Suite SHALL verify that the URL changes to include `/login` within 4000 milliseconds
2. WHEN an authenticated user navigates to `/home`, THE Test_Suite SHALL verify that the URL remains `/home` and that the element `app-search` is visible on the page
3. THE Test_Suite SHALL simulate authentication state by using `cy.window()` to access `sessionStorage` and set the item with key `authenticated` to value `true` before navigating to the guarded route
4. THE Test_Suite SHALL clear `sessionStorage` before each route guard test using a `beforeEach` hook to ensure test isolation regardless of execution order

### Requirement 8: Pokemon Search E2E Tests

**User Story:** As a student, I want tests that exercise the Pokemon search flow, so that I can learn how Cypress handles asynchronous operations and conditional rendering.

#### Acceptance Criteria

1. WHEN the text "pikachu" is entered into the `[data-cy=search-input]` and the `[data-cy=search-button]` is clicked, THE Test_Suite SHALL verify that the Pokemon name is displayed on the page
2. WHEN the text "pikachu" is entered into the search input and the search button is clicked, THE Test_Suite SHALL verify that Pokemon sprite images are rendered with non-empty src attributes
3. WHEN the text "pikachu" is entered into the search input and the search button is clicked, THE Test_Suite SHALL verify that at least 1 type element is displayed containing a type name
4. WHEN the text "pikachu" is entered into the search input and the search button is clicked, THE Test_Suite SHALL verify that at least 1 move element is displayed
5. WHEN asserting on Pokemon data elements after a search, THE Test_Suite SHALL rely on Cypress built-in automatic retry-and-timeout behavior (no explicit waits or sleeps) to resolve elements that appear only after the asynchronous API response triggers conditional rendering

### Requirement 9: API Testing with cy.request

**User Story:** As a student, I want test files that demonstrate direct API testing using `cy.request`, so that I can learn how Cypress can test APIs independently of the UI.

#### Acceptance Criteria

1. THE Test_Suite SHALL include a test file that uses `cy.request` to send GET requests directly to `https://pokeapi.co/api/v2/pokemon/{identifier}` without navigating to any page in the Pokedex_App
2. WHEN a valid Pokemon identifier (name or numeric ID) is provided, THE Test_Suite SHALL assert that the response status code equals 200 using a `.then()` callback on the `cy.request` response
3. WHEN a valid Pokemon identifier is provided, THE Test_Suite SHALL assert that the response body contains the properties `name`, `sprites`, `types`, and `moves` by verifying each property exists on the response body object
4. WHEN a non-existent Pokemon identifier is provided, THE Test_Suite SHALL send the request with `failOnStatusCode: false` and assert that the response status code equals 404
5. THE Test_Suite SHALL demonstrate assertion chaining by using `.its()` to access a nested response property (e.g., `response.body.name`) and `.should()` to assert its value
6. THE Test_Suite SHALL demonstrate the `.then()` callback pattern on the `cy.request` response object to perform multiple assertions on status and body within a single test

### Requirement 10: Custom Login Command

**User Story:** As a student, I want a reusable custom command for login, so that I can learn the Custom Commands architectural pattern as an alternative to Page Object Models.

#### Acceptance Criteria

1. THE Custom_Command SHALL be named `login` and accept a `username` parameter of type string and a `password` parameter of type string
2. THE Custom_Command SHALL type the username into the `[data-cy=username-input]` element
3. THE Custom_Command SHALL type the password into the `[data-cy=password-input]` element
4. THE Custom_Command SHALL click the `[data-cy=login-button]` element
5. THE Test_Suite SHALL invoke the `cy.login()` custom command in at least one test file in place of manually typing credentials and clicking the login button, verifying that the test produces the same post-login outcome as the equivalent manual steps
6. THE Custom_Command SHALL be defined in `cypress/support/commands.ts` using `Cypress.Commands.add()` and SHALL include a TypeScript interface declaration that augments the `Cypress.Chainable` interface with the `login(username: string, password: string)` method signature so that `cy.login()` compiles without type errors

### Requirement 11: Accessibility Testing

**User Story:** As a student, I want accessibility testing examples using cypress-axe, so that I can learn how to integrate automated A11y audits into a Cypress test suite.

#### Acceptance Criteria

1. THE Test_Suite SHALL include `cypress-axe` and `axe-core` as development dependencies
2. THE Test_Suite SHALL include a test file that organizes accessibility tests within a `describe` block, with separate `it` blocks for each page audit
3. WHEN the login page is loaded, THE Test_Suite SHALL inject the axe-core engine using `cy.injectAxe()` and run an audit using `cy.checkA11y()` that fails the test if any violations are detected
4. WHEN the home page is loaded after authenticating via the `cy.login()` custom command, THE Test_Suite SHALL inject the axe-core engine using `cy.injectAxe()` and run an accessibility audit using `cy.checkA11y()`
5. THE Test_Suite SHALL import `cypress-axe` commands in the support file to make `cy.injectAxe()` and `cy.checkA11y()` available
6. THE Test_Suite SHALL include at least one test that targets a specific page region or excludes known elements using `cy.checkA11y()` configuration options, demonstrating scoped auditing

### Requirement 12: Visual Regression Testing

**User Story:** As a student, I want visual regression testing examples using cypress-image-snapshot, so that I can learn how to detect unintended visual changes through screenshot comparison.

#### Acceptance Criteria

1. THE Test_Suite SHALL include `@simonsmith/cypress-image-snapshot` as a development dependency
2. THE Test_Suite SHALL register the image snapshot plugin in the Cypress_Config `setupNodeEvents` function by calling the plugin's `addMatchImageSnapshotPlugin` function
3. THE Test_Suite SHALL register the `matchImageSnapshot` command in the support file by importing and invoking the command registration from `@simonsmith/cypress-image-snapshot/command`
4. THE Test_Suite SHALL include a test file that visits the `/login` page, waits for the login form to be visible, and captures a baseline screenshot using `cy.matchImageSnapshot()` with a descriptive snapshot name string
5. THE Test_Suite SHALL include a test that authenticates via the `cy.login()` custom command, performs a Pokemon search for a known valid Pokemon name, waits for the Pokemon data (name and sprite) to be visible on the page, and captures a baseline screenshot using `cy.matchImageSnapshot()` with a descriptive snapshot name string

### Requirement 13: Debugging Demonstration

**User Story:** As a student, I want commented examples of Cypress debugging tools, so that I can learn how to troubleshoot failing tests using `.debug()` and `cy.pause()`.

#### Acceptance Criteria

1. THE Test_Suite SHALL include at least one commented-out code example demonstrating `.debug()` chained to a Cypress command within a test file, so that the example does not execute during normal test runs
2. THE Test_Suite SHALL include at least one commented-out code example demonstrating `cy.pause()` within a test file, so that the example does not execute during normal test runs
3. THE Test_Suite SHALL include at least one commented-out code example demonstrating the `debugger` keyword within a `.then()` callback in a test file, so that the example does not execute during normal test runs
4. THE Test_Suite SHALL include an inline comment above each debugging example stating the specific scenario in which that tool is useful: `.debug()` for inspecting the yielded subject in the DevTools console, `cy.pause()` for stepping through commands one at a time during interactive mode, and `debugger` for setting a breakpoint inside a `.then()` callback when DevTools is open

### Requirement 14: Test File Organization by Study Guide Section

**User Story:** As an instructor, I want test files organized to correspond with the study guide sections, so that students can easily map test code to the concepts they are learning.

#### Acceptance Criteria

1. THE Test_Suite SHALL include a test file named `login.cy.ts` located in the `cypress/e2e/` directory, mapping to the "Core Execution Engine" study guide section and demonstrating `describe`, `it`, hooks, selectors, interactions, and assertions
2. THE Test_Suite SHALL include a test file named `data-driven-login.cy.ts` located in the `cypress/e2e/` directory, mapping to the "Advanced Capabilities" study guide section and demonstrating data-driven testing with array iteration
3. THE Test_Suite SHALL include a test file named `api.cy.ts` located in the `cypress/e2e/` directory, mapping to the "Advanced Capabilities" study guide section and demonstrating `cy.request` and response assertions
4. THE Test_Suite SHALL include a test file named `route-guard.cy.ts` located in the `cypress/e2e/` directory, mapping to the "Architectural Patterns & Ecosystem" study guide section and demonstrating navigation testing and session management
5. THE Test_Suite SHALL include a test file named `pokemon-search.cy.ts` located in the `cypress/e2e/` directory, mapping to the "Advanced Capabilities" study guide section and demonstrating automatic waiting with asynchronous operations
6. THE Test_Suite SHALL include a test file named `accessibility.cy.ts` located in the `cypress/e2e/` directory, mapping to the "Advanced Testing & Collaboration" study guide section and demonstrating the cypress-axe plugin workflow
7. THE Test_Suite SHALL include a test file named `visual-regression.cy.ts` located in the `cypress/e2e/` directory, mapping to the "Advanced Testing & Collaboration" study guide section and demonstrating the cypress-image-snapshot plugin workflow
8. THE Test_Suite SHALL contain exactly 7 test spec files in the `cypress/e2e/` directory, one for each concept area listed in criteria 1 through 7
