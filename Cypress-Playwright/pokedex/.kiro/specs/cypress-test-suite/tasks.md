# Implementation Plan: Cypress Test Suite

## Overview

Set up a complete Cypress E2E test suite for the Pokedex Angular 20 application. The implementation installs dependencies, creates configuration and support infrastructure, adds `data-cy` attributes to Angular templates, and scaffolds 7 test spec files demonstrating core Cypress concepts mapped to the study guide sections.

## Tasks

- [x] 1. Install dependencies and add npm scripts
  - [x] 1.1 Add Cypress devDependencies and npm scripts to package.json
    - Add `cypress`, `cypress-axe`, `axe-core`, and `@simonsmith/cypress-image-snapshot` to `devDependencies`
    - Add `"cy:open": "cypress open"` and `"cy:run": "cypress run"` to the `scripts` object
    - Run `npm install` to resolve and install all new packages
    - _Requirements: 2.4, 3.1, 3.2, 3.3, 3.4, 11.1, 12.1_

- [x] 2. Create Cypress configuration and support infrastructure
  - [x] 2.1 Create cypress.config.ts at project root
    - Use `defineConfig` from `cypress`
    - Set `viewportWidth: 1280`, `viewportHeight: 720`
    - Set `e2e.baseUrl` to `http://localhost:4200`
    - Set `e2e.specPattern` to `cypress/e2e/**/*.cy.ts`
    - Set `e2e.supportFile` to `cypress/support/e2e.ts`
    - Register `addMatchImageSnapshotPlugin` in `setupNodeEvents`
    - _Requirements: 1.1, 1.2, 1.3, 2.1, 2.2, 2.3, 2.4, 12.2_

  - [x] 2.2 Create cypress/support/e2e.ts
    - Import `./commands` to register custom commands
    - Import `cypress-axe` to register `cy.injectAxe()` and `cy.checkA11y()`
    - Import `@simonsmith/cypress-image-snapshot/command` to register `cy.matchImageSnapshot()`
    - _Requirements: 1.3, 1.6, 11.5, 12.3_

  - [x] 2.3 Create cypress/support/commands.ts with cy.login() custom command
    - Declare TypeScript `Cypress.Chainable` interface augmentation with `login(username: string, password: string): Chainable<void>`
    - Implement `Cypress.Commands.add('login', ...)` that types username into `[data-cy=username-input]`, types password into `[data-cy=password-input]`, and clicks `[data-cy=login-button]`
    - Export empty object to make it a module
    - _Requirements: 1.4, 10.1, 10.2, 10.3, 10.4, 10.6_

  - [x] 2.4 Create cypress/fixtures/.gitkeep
    - Create empty `.gitkeep` file to preserve the fixtures directory in version control
    - _Requirements: 1.5_

- [x] 3. Add data-cy attributes to Angular templates
  - [x] 3.1 Modify src/app/components/login/login.html
    - Add `data-cy="username-input"` to the username `<input>` element
    - Add `data-cy="password-input"` to the password `<input>` element
    - Add `data-cy="login-button"` to the login `<button>` element
    - Add `data-cy="error-message"` to the error `<p>` element
    - _Requirements: 4.1, 4.2, 4.3, 4.4_

  - [x] 3.2 Modify src/app/components/search/search.html
    - Add `data-cy="search-input"` to the search `<input>` element
    - Add `data-cy="search-button"` to the search `<button>` element
    - _Requirements: 4.5, 4.6_

- [x] 4. Checkpoint - Verify infrastructure
  - Ensure all tests pass, ask the user if questions arise.

- [x] 5. Implement core test spec files
  - [x] 5.1 Create cypress/e2e/login.cy.ts
    - Wrap tests in a `describe('Login Page', ...)` block
    - Use `beforeEach` to visit `/login`
    - Write `it` block for successful login: type "dev"/"dev" into `[data-cy=username-input]`/`[data-cy=password-input]`, click `[data-cy=login-button]`, assert `cy.url().should('include', '/home')`
    - Write `it` block for failed login: type invalid credentials, assert `[data-cy=error-message]` is visible with text "login failed, please try again"
    - Include commented-out debugging examples: `.debug()` chained to `cy.get()`, `cy.pause()`, and `debugger` inside a `.then()` callback, each with inline comment explaining when to use it
    - _Requirements: 5.1, 5.2, 5.3, 5.4, 5.5, 5.6, 13.1, 13.2, 13.3, 13.4, 14.1_

  - [x] 5.2 Create cypress/e2e/data-driven-login.cy.ts
    - Define an array of credential objects with at least 3 entries: one valid ("dev"/"dev") and two invalid combinations
    - Each object has `username`, `password`, `shouldSucceed`, and `description` properties
    - Use `forEach` to iterate the array, generating dynamic `it` blocks
    - In each `it`, use `beforeEach` to visit `/login`, type credentials, click login, and assert based on `shouldSucceed`
    - _Requirements: 6.1, 6.2, 6.3, 6.4, 6.5, 14.2_

  - [x] 5.3 Create cypress/e2e/api.cy.ts
    - Write test using `cy.request('GET', 'https://pokeapi.co/api/v2/pokemon/pikachu')` with `.then()` callback asserting status 200 and body properties (`name`, `sprites`, `types`, `moves`)
    - Write test using `.its('body.name').should('eq', 'pikachu')` to demonstrate assertion chaining
    - Write test for 404 using `cy.request({ url: '...invalidname', failOnStatusCode: false })` and assert status 404
    - _Requirements: 9.1, 9.2, 9.3, 9.4, 9.5, 9.6, 14.3_

  - [x] 5.4 Create cypress/e2e/route-guard.cy.ts
    - Use `beforeEach` to clear `sessionStorage`
    - Write test for unauthenticated access: visit `/home`, assert URL includes `/login` (with timeout 4000)
    - Write test for authenticated access: use `cy.window().then(win => win.sessionStorage.setItem('authenticated', 'true'))`, visit `/home`, assert URL includes `/home` and `app-search` is visible
    - _Requirements: 7.1, 7.2, 7.3, 7.4, 14.4_

  - [x] 5.5 Create cypress/e2e/pokemon-search.cy.ts
    - Authenticate using `cy.login('dev', 'dev')` custom command, then visit `/home`
    - Write test: type "pikachu" into `[data-cy=search-input]`, click `[data-cy=search-button]`, assert Pokemon name is displayed
    - Write test: assert sprite images have non-empty `src` attributes
    - Write test: assert at least 1 type element is displayed
    - Write test: assert at least 1 move element is displayed
    - Rely on Cypress automatic retry (no explicit waits) for async rendering
    - _Requirements: 8.1, 8.2, 8.3, 8.4, 8.5, 10.5, 14.5_

- [x] 6. Checkpoint - Ensure core specs compile
  - Ensure all tests pass, ask the user if questions arise.

- [x] 7. Implement advanced test spec files
  - [x] 7.1 Create cypress/e2e/accessibility.cy.ts
    - Write `describe` block with separate `it` blocks for each page audit
    - Login page test: visit `/login`, call `cy.injectAxe()`, call `cy.checkA11y()`
    - Home page test: use `cy.login('dev', 'dev')`, visit `/home`, call `cy.injectAxe()`, call `cy.checkA11y()`
    - Scoped audit test: demonstrate `cy.checkA11y()` with configuration options targeting a specific region or excluding known elements
    - _Requirements: 11.2, 11.3, 11.4, 11.5, 11.6, 14.6_

  - [x] 7.2 Create cypress/e2e/visual-regression.cy.ts
    - Login page snapshot test: visit `/login`, wait for form visibility, call `cy.matchImageSnapshot('login-page')`
    - Search results snapshot test: authenticate via `cy.login('dev', 'dev')`, search for a known Pokemon, wait for data to render, call `cy.matchImageSnapshot('pokemon-search-results')`
    - _Requirements: 12.4, 12.5, 14.7_

- [x] 8. Final checkpoint - Ensure all tests pass
  - Ensure all tests pass, ask the user if questions arise.

## Notes

- Each task references specific requirements for traceability
- The design document explicitly states property-based testing does not apply to this feature (test infrastructure, not application logic)
- Test files rely on the Angular dev server running at `http://localhost:4200` — start with `npm start` before running `npm run cy:run`
- Checkpoints ensure incremental validation of configuration before writing test specs
- The `cy.login()` custom command is used across multiple spec files (pokemon-search, accessibility, visual-regression) to avoid credential duplication
- Visual regression tests create baseline screenshots on first run; subsequent runs compare against baselines

## Task Dependency Graph

```json
{
  "waves": [
    { "id": 0, "tasks": ["1.1"] },
    { "id": 1, "tasks": ["2.1", "2.4", "3.1", "3.2"] },
    { "id": 2, "tasks": ["2.2", "2.3"] },
    { "id": 3, "tasks": ["5.1", "5.3", "5.4"] },
    { "id": 4, "tasks": ["5.2", "5.5"] },
    { "id": 5, "tasks": ["7.1", "7.2"] }
  ]
}
```
