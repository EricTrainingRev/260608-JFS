# Cypress E2E Testing Lesson Plan — Pokédex App

## Module 1: Foundations & Project Setup

**Goal:** Understand how Cypress fits into the project and how to run tests.

### Key files to show:

- `cypress.config.ts` — The root configuration
- `cypress/tsconfig.json` — TypeScript integration
- `cypress/support/e2e.ts` — Global setup (runs before every spec)
- `cypress/support/commands.ts` — Custom commands

### Talking points:

- `baseUrl` eliminates hardcoded URLs; `cy.visit('/login')` resolves against it
- `specPattern` controls test discovery (`**/*.cy.ts`)
- `supportFile` auto-loads before each spec — this is where plugins and custom commands are registered
- `setupNodeEvents` is the Node-side hook for plugins that need filesystem access (image snapshots)

### Running tests:

```bash
# Start the Angular app first
npm start

# Interactive mode (browser UI, time-travel debugger)
npm run cy:open

# Headless mode (CI-friendly, runs in terminal)
npm run cy:run
```

---

## Module 2: Core Execution Engine — `login.cy.ts`

**Goal:** Learn the basic anatomy of a Cypress test.

### Concepts covered:

| Concept | Example |
|---------|---------|
| `describe()` / `it()` | Test organization and naming |
| `beforeEach()` | Shared setup — navigate to `/login` before each test |
| `cy.visit()` | Navigate to a page |
| `cy.get()` + `data-cy` selectors | The "gold standard" for element selection |
| `.type()` / `.click()` | Simulating user input |
| `.should()` | Assertions (URL, visibility, text) |
| Chained assertions | `.should('be.visible').should('have.text', ...)` |

### Debugging tools (demo live):

- `.debug()` — logs subject to DevTools console
- `cy.pause()` — step through commands in interactive mode
- `debugger` inside `.then()` — standard JS breakpoint

---

## Module 3: Custom Commands — `commands.ts`

**Goal:** Understand reusable command abstractions.

### Concepts covered:

- `Cypress.Commands.add()` — registering a new command on `cy`
- TypeScript `declare global` — augmenting the Chainable interface for type safety
- Why custom commands over Page Objects (integrates with retry-ability, no `new` boilerplate)

### Walkthrough:

```typescript
// Registration
Cypress.Commands.add('login', (username, password) => {
  cy.get('[data-cy=username-input]').type(username);
  cy.get('[data-cy=password-input]').type(password);
  cy.get('[data-cy=login-button]').click();
});

// Usage in any test
cy.login('dev', 'dev');
```

---

## Module 4: Async & Automatic Waiting — `pokemon-search.cy.ts`

**Goal:** Understand Cypress's automatic retry/timeout model.

### Key insight:

Cypress retries `.should()` assertions until they pass or the timeout expires (default 4s). No `cy.wait()`, no `sleep()`, no polling.

### Concepts covered:

- Automatic waiting for DOM elements to appear after API calls
- `.each()` for iterating over multiple elements
- `cy.wrap()` to bring jQuery elements back into the command chain
- `.should('have.length.greaterThan', 0)` for dynamic content

---

## Module 5: API Testing — `api.cy.ts`

**Goal:** Test APIs directly without browser navigation.

### Concepts covered:

| Concept | Purpose |
|---------|---------|
| `cy.request()` | HTTP request from Node (bypasses browser) |
| `.then(response => ...)` | Access full response (status, headers, body) |
| `.its('body.name')` | Drill into nested properties concisely |
| `failOnStatusCode: false` | Test error responses without auto-fail |
| Chai `expect()` syntax | Assertions inside callbacks |

---

## Module 6: Data-Driven Testing — `data-driven-login.cy.ts`

**Goal:** Generate tests dynamically from an array of test cases.

### Pattern:

```typescript
const testCases: LoginTestCase[] = [
  { username: 'dev', password: 'dev', shouldSucceed: true, description: '...' },
  // ...more cases
];

testCases.forEach((tc) => {
  it(`should handle login with ${tc.description}`, () => {
    // single test body, conditional assertions
  });
});
```

### Talking points:

- TypeScript interface enforces test data shape
- Each array entry becomes a separate test in the runner
- Conditional assertions based on `shouldSucceed` flag
- Easy to extend — add a row, get a new test for free

---

## Module 7: Route Guards & Browser State — `route-guard.cy.ts`

**Goal:** Test Angular auth guards by manipulating browser state directly.

### Concepts covered:

- `cy.window()` — access the browser's `window` object
- `sessionStorage.setItem()` — simulate authentication without the login UI
- Test isolation via `sessionStorage.clear()` in `beforeEach`
- Testing redirects (unauthenticated → `/login`)

### Why this matters:

Tests the guard in isolation. Faster than going through the login flow and decouples from the login component's correctness.

---

## Module 8: Accessibility Testing — `accessibility.cy.ts`

**Goal:** Automate WCAG audits with `cypress-axe`.

### Concepts covered:

- `cy.injectAxe()` — load axe-core into the page (must re-inject after navigation)
- `cy.checkA11y()` — run a full audit; fails the test on violations
- Scoped audits: `cy.checkA11y('form', { rules: { ... } })`
- Disabling specific rules for known exceptions

### Plugin setup (shown in `e2e.ts`):

```typescript
import 'cypress-axe';
```

---

## Module 9: Visual Regression — `visual-regression.cy.ts`

**Goal:** Catch unintended UI changes with screenshot comparison.

### Concepts covered:

- `cy.matchImageSnapshot('name')` — capture/compare baseline screenshots
- First run saves baseline; subsequent runs diff pixel-by-pixel
- Waiting for UI stability before capture (`.should('be.visible')`)
- Regenerating baselines: `--env updateSnapshots=true`

### Plugin setup:

```typescript
// e2e.ts (browser-side)
import { addMatchImageSnapshotCommand } from '@simonsmith/cypress-image-snapshot/command';
addMatchImageSnapshotCommand();

// cypress.config.ts (node-side)
import { addMatchImageSnapshotPlugin } from '@simonsmith/cypress-image-snapshot/plugin';
setupNodeEvents(on) { addMatchImageSnapshotPlugin(on); }
```

---

## Module 10: Creating New Tests

### Option 1: Cypress Studio (Record & Playback)

Cypress Studio lets you generate tests by recording real interactions in your app — no code writing required. It translates clicks, typing, checks, and selects into Cypress commands in real time.

**How to open Studio:**

- **New test:** Click "New Test" on any spec or suite in the Cypress App runner
- **Extend existing test:** Hover over a test in the Command Log → click "Edit in Studio"
- **Header panel:** Click "Studio Beta" in the Cypress App header

**Recording workflow:**

1. Studio opens your app and puts you in recording mode
2. Interact with your app normally (click, type, check, uncheck, select)
3. Studio translates each action into a Cypress command and writes it to your spec file in real time
4. Right-click any element to manually add assertions (visible, hidden, text content, value, etc.)
5. Click "Save" when done — the generated code is written directly to the `.cy.ts` file

**Supported recorded actions:**

- `.click()`
- `.type()`
- `.check()` / `.uncheck()`
- `.select()`

**Selector priority (auto-chosen by Studio):**

1. `data-cy`
2. `data-test`
3. `data-testid`
4. `data-qa`
5. `name`
6. `id`
7. `class`
8. `tag`
9. `attributes`
10. `nth-child`

**Studio AI (optional, requires Cypress Cloud):**

Studio AI adds an AI layer on top of recording. As you interact with the app, it observes DOM changes between actions and recommends assertions automatically. You can accept, reject, or edit each recommendation. This requires:

- Cypress v15.11.0+
- A Cypress Cloud account (free accounts get 60 recommendations/hour)
- Linked project

Studio AI suggests assertions for: visibility, element existence, text content, element count, form values, CSS classes, aria attributes, URL, and page title.

**Known limitations:**

- E2E tests only (no Component Testing)
- Cannot record across multiple origins
- iFrames and Shadow DOM not supported
- Cucumber-style tests not supported

**Reference:** [Cypress Studio AI docs](https://docs.cypress.io/app/guides/cypress-studio)

---

### Option 2: Manual file creation

Create a new file in `cypress/e2e/` with the `.cy.ts` extension:

```typescript
// cypress/e2e/my-feature.cy.ts
describe('My Feature', () => {
  beforeEach(() => {
    cy.visit('/some-page');
  });

  it('should do something', () => {
    cy.get('[data-cy=some-element]').should('exist');
  });
});
```

Cypress discovers it automatically via the `specPattern` glob.

### Option 3: Using the Cypress interactive runner

1. Run `npm run cy:open`
2. Select "E2E Testing" → choose a browser
3. Click "Create new spec" in the specs list
4. Cypress generates a skeleton file for you

### Option 4: Scaffold with the Angular CLI + Cypress schematic

```bash
# If using @cypress/schematic (not installed here, but available)
ng generate @cypress/schematic:e2e --name=my-feature
```

### Option 5: Copy an existing test as a template

Duplicate `login.cy.ts` or `pokemon-search.cy.ts` and modify. This is often the fastest approach for similar features.

### Selector strategy for new tests

Add `data-cy` attributes to your Angular templates:

```html
<button data-cy="submit-button">Submit</button>
```

Then select in tests:

```typescript
cy.get('[data-cy=submit-button]').click();
```

This decouples tests from CSS classes, element types, and content text — making them resilient to refactors.

### Choosing what type of test to write

| If you want to test... | Use this pattern | Example file |
|------------------------|------------------|--------------|
| UI flow (click, type, navigate) | Standard E2E | `login.cy.ts` |
| API response shape/status | `cy.request()` | `api.cy.ts` |
| Many input combos | Data-driven | `data-driven-login.cy.ts` |
| Auth/redirect logic | State manipulation | `route-guard.cy.ts` |
| WCAG compliance | cypress-axe | `accessibility.cy.ts` |
| UI appearance | Image snapshot | `visual-regression.cy.ts` |

---

## Suggested Demo Order

1. Run `cy:open` and show the interactive runner (time-travel, DOM snapshots)
2. Walk through `login.cy.ts` live — show commands executing in real time
3. Show the custom command in `commands.ts`, then `pokemon-search.cy.ts` using it
4. Open `api.cy.ts` — demonstrate API testing without a browser
5. Show `data-driven-login.cy.ts` — highlight how 3 tests are generated from 1 loop
6. `route-guard.cy.ts` — show sessionStorage manipulation
7. `accessibility.cy.ts` — show a failure screenshot and the violation details
8. `visual-regression.cy.ts` — show the baseline in `cypress/snapshots/` and explain the diff
