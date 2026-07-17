# Implementation Plan: Playwright Test Suite

## Overview

This plan implements a Playwright end-to-end test suite for the Angular Pokedex application, structured around the Playwright study guide topics. Each task creates specific test files and configuration that demonstrate major Playwright concepts while providing functional test coverage. The implementation proceeds from foundational setup through advanced patterns, with each step building on the previous.

## Tasks

- [x] 1. Set up Playwright project structure and configuration
  - [x] 1.1 Install Playwright and create the project directory structure
    - Install `@playwright/test` as a dev dependency
    - Create `playwright/` directory with subdirectories: `e2e/`, `fixtures/`, `.auth/`
    - Add `playwright/.auth/`, `playwright-results/`, and `playwright-report/` to `.gitignore`
    - Add npm scripts to `package.json`: `pw:test`, `pw:test:ui`, `pw:codegen`, `pw:report`
    - _Requirements: 1.6_

  - [x] 1.2 Create `playwright.config.ts` with projects, timeouts, and artifact settings
    - Define `baseURL` as `http://localhost:4200`
    - Set `testDir` to `'./playwright'`
    - Configure global timeout of 5 seconds
    - Define `setup` project with `testMatch: /auth\.setup\.ts/`
    - Define `chromium` project using `devices['Desktop Chrome']` with `storageState` and `dependencies: ['setup']`
    - Configure `trace: 'on-first-retry'`, `video: 'on-first-retry'`, `screenshot: 'only-on-failure'`
    - Set `outputDir` to `'./playwright-results/'`
    - Configure retries: 1 in CI, 0 locally
    - _Requirements: 1.1, 1.2, 1.3, 1.4, 1.5, 9.1, 13.2, 13.3, 13.4, 14.1, 14.2, 16.1, 16.2, 16.3, 16.4_

- [x] 2. Implement authentication setup and storage state
  - [x] 2.1 Create `playwright/auth.setup.ts` for login and storage state persistence
    - Navigate to `/login`
    - Fill username and password fields using `getByLabel` or `getByPlaceholder`
    - Click the login button
    - Wait for redirect to `/home` using `waitForURL`
    - Save storage state to `playwright/.auth/user.json` via `page.context().storageState()`
    - _Requirements: 13.1, 13.5_

- [x] 3. Implement custom fixtures
  - [x] 3.1 Create `playwright/fixtures/auth-fixture.ts` with `base.extend()` pattern
    - Import `test as base` from `@playwright/test`
    - Define `AuthFixtures` type with `authenticatedPage: Page`
    - Use `browser.newContext()` with `storageState` to create an isolated authenticated context
    - Provide the page via `use()` callback
    - Close the context after `use()` for cleanup
    - Export the extended `test` object
    - _Requirements: 10.1, 10.2, 10.3_

- [x] 4. Checkpoint - Verify foundation setup
  - Ensure the Playwright config is valid by running `npx playwright test --list` to confirm test discovery.
  - Ensure all tests pass, ask the user if questions arise.

- [x] 5. Implement locators test spec
  - [x] 5.1 Create `playwright/e2e/locators.spec.ts` demonstrating all locator methods
    - Demonstrate `getByRole` by locating buttons, headings, or links on the login/home page
    - Demonstrate `getByLabel` by locating form inputs via associated label text on the login page
    - Demonstrate `getByPlaceholder` by locating inputs via placeholder text on the search interface
    - Demonstrate `getByText` by locating elements containing visible text content
    - Demonstrate `getByTestId` by locating elements via `data-testid` attributes
    - Demonstrate locator chaining by scoping a child locator within a parent locator
    - Demonstrate `filter({ hasText: ... })` to narrow results within a collection
    - Demonstrate `.nth()` to select a specific item from a list of elements
    - _Requirements: 2.1, 2.2, 2.3, 2.4, 2.5, 2.6, 2.7, 2.8_

- [x] 6. Implement element interactions test spec
  - [x] 6.1 Create `playwright/e2e/interactions.spec.ts` demonstrating all interaction methods
    - Demonstrate `click()` on buttons (login, search)
    - Demonstrate `fill()` on username, password, and search input fields
    - Demonstrate `check()` on a checkbox element
    - Demonstrate `selectOption()` on a dropdown element
    - Demonstrate `hover()` on an interactive element
    - Demonstrate `keyboard.press()` for simulating Enter key or keyboard shortcut
    - Include documentation comment that Playwright auto-waits for actionability (no explicit sleeps)
    - _Requirements: 3.1, 3.2, 3.3, 3.4, 3.5, 3.6, 3.7_

- [x] 7. Implement web-first assertions test spec
  - [x] 7.1 Create `playwright/e2e/assertions.spec.ts` demonstrating web-first assertions
    - Demonstrate `toBeVisible()` asserting an element appears after user interaction
    - Demonstrate `toBeEnabled()` asserting a button or input is interactable
    - Demonstrate `toBeChecked()` asserting checkbox state after interaction
    - Demonstrate `toHaveText()` asserting text content of a heading or label
    - Demonstrate `toHaveValue()` asserting current value of an input field
    - Demonstrate `toHaveURL()` asserting page URL after navigation/login redirect
    - Demonstrate `toHaveCount()` asserting number of elements in a collection
    - _Requirements: 4.1, 4.2, 4.3, 4.4, 4.5, 4.6, 4.7_

- [x] 8. Implement API testing spec
  - [x] 8.1 Create `playwright/e2e/api.spec.ts` using the `request` fixture for direct API calls
    - Use the Playwright `request` fixture to call PokéAPI without a browser page
    - Send GET to `https://pokeapi.co/api/v2/pokemon/pikachu` and verify `response.ok()` returns true
    - Verify response body contains `name`, `sprites`, `types`, `moves` properties
    - Send GET for a non-existent Pokemon and verify response status is 404
    - Demonstrate parsing nested response properties (e.g., `sprites.front_default`, `types[0].type.name`)
    - _Requirements: 5.1, 5.2, 5.3, 5.4, 5.5_

- [x] 9. Implement network mocking test spec
  - [x] 9.1 Create `playwright/e2e/network-mocking.spec.ts` demonstrating `page.route()` and `route.fulfill()`
    - Use `page.route()` to intercept requests matching the PokéAPI URL pattern
    - Use `route.fulfill()` to return a custom JSON response with status 200 and `content-type: application/json`
    - Verify the UI renders content from the mocked response (Pokemon name, type, etc.)
    - Mock an error response and verify the app handles failure appropriately
    - Demonstrate that real network requests are not sent when a route is fulfilled
    - _Requirements: 6.1, 6.2, 6.3, 6.4, 6.5_

- [x] 10. Checkpoint - Verify core test specs
  - Ensure all tests pass, ask the user if questions arise.

- [x] 11. Implement data-driven testing spec
  - [x] 11.1 Create `playwright/e2e/data-driven-login.spec.ts` with parameterized test cases
    - Define a typed `LoginTestCase[]` array with `description`, `username`, `password`, `shouldSucceed`, `expectedOutcome`
    - Iterate using `for...of` loop to generate one test per entry
    - Use template literals in test names (e.g., `` `login - ${testCase.description}` ``)
    - Assert URL change for successful login cases
    - Assert error message visibility for failure cases
    - _Requirements: 7.1, 7.2, 7.3, 7.4, 7.5_

- [x] 12. Implement test isolation spec
  - [x] 12.1 Create `playwright/e2e/test-isolation.spec.ts` demonstrating browser context isolation
    - Verify session storage is empty at test start using `page.evaluate()`
    - Set state in one test and verify it does not persist into a subsequent test
    - Use `page.evaluate()` to inspect and manipulate browser storage
    - Include documentation comments explaining Playwright's fresh Browser_Context per test model
    - _Requirements: 8.1, 8.2, 8.3, 8.4_

- [x] 13. Implement fixture demo spec
  - [x] 13.1 Create `playwright/e2e/fixture-demo.spec.ts` using the custom fixture
    - Import the custom `test` from `../fixtures/auth-fixture`
    - Use the `authenticatedPage` fixture parameter in tests
    - Demonstrate that the fixture-provided page is independent from the default `page` fixture
    - Verify that the authenticated page starts on an authenticated route without performing login
    - _Requirements: 10.4, 10.5_

- [x] 14. Implement advanced API handling spec
  - [x] 14.1 Create `playwright/e2e/advanced-api.spec.ts` demonstrating advanced request patterns
    - Send API requests with custom headers (`Authorization`, `Accept`, custom header values)
    - Parse deeply nested JSON response properties using destructuring
    - Assert specific values within nested structures (arrays within objects, objects within arrays)
    - Use `response.ok()`, `response.status()`, and `response.json()` together for complete validation
    - _Requirements: 11.1, 11.2, 11.3, 11.4_

- [x] 15. Implement conditional mocking spec
  - [x] 15.1 Create `playwright/e2e/conditional-mocking.spec.ts` with logic-based route handlers
    - Define a `page.route()` handler that inspects the request URL to choose a response
    - Return different response bodies and status codes based on URL path segments
    - Demonstrate `route.continue()` as a fallback for unmatched requests
    - Verify the UI correctly reflects conditionally mocked responses for each scenario
    - _Requirements: 12.1, 12.2, 12.3, 12.4_

- [x] 16. Implement visual regression spec
  - [x] 16.1 Create `playwright/e2e/visual-regression.spec.ts` with screenshot comparisons
    - Capture a full-page screenshot and compare against stored baseline using `toHaveScreenshot()`
    - Configure `maxDiffPixels` option for minor rendering variation tolerance
    - Configure `threshold` option for pixel comparison sensitivity
    - Demonstrate explicit screenshot naming: `toHaveScreenshot('name.png')`
    - Include documentation comment about updating baselines with `--update-snapshots`
    - _Requirements: 15.1, 15.2, 15.3, 15.4, 15.5_

- [x] 17. Add Trace Viewer and Codegen documentation
  - [x] 17.1 Add documentation comments to relevant spec files about debugging tools
    - Add comments in `playwright.config.ts` explaining trace, video, and screenshot settings
    - Add documentation comments explaining `npx playwright show-trace` usage
    - Add documentation comments explaining `npx playwright codegen <url>` usage
    - Add comments noting Codegen locators should be refactored to `getByRole`/`getByTestId`
    - Add documentation explaining actionability logs, Network tab, and Console tab in Trace Viewer
    - Add documentation explaining capture mode options (off, on, retain-on-failure, on-first-retry)
    - _Requirements: 9.2, 9.3, 9.4, 14.3, 14.4, 16.5_

- [x] 18. Final checkpoint - Ensure all tests pass
  - Run the full Playwright test suite with `npx playwright test`
  - Ensure all tests pass, ask the user if questions arise.

## Notes

- Each task references specific requirements for traceability
- Checkpoints ensure incremental validation of the test suite
- The Angular dev server must be running on `http://localhost:4200` before executing tests
- Visual regression baselines are created on first run — commit them to the repository for CI
- The `playwright/.auth/user.json` file is gitignored since it is regenerated by the setup project on each run
- No property-based tests are included — this feature is a test suite demonstrating Playwright APIs, not business logic with universal properties

## Task Dependency Graph

```json
{
  "waves": [
    { "id": 0, "tasks": ["1.1"] },
    { "id": 1, "tasks": ["1.2"] },
    { "id": 2, "tasks": ["2.1", "3.1"] },
    { "id": 3, "tasks": ["5.1", "6.1", "7.1", "8.1"] },
    { "id": 4, "tasks": ["9.1", "11.1", "12.1", "13.1", "14.1"] },
    { "id": 5, "tasks": ["15.1", "16.1"] },
    { "id": 6, "tasks": ["17.1"] }
  ]
}
```
