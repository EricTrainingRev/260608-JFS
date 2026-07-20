import { test, expect } from '../fixtures/authenticated-test';

/**
 * Locators Spec — Demonstrates all major Playwright locator methods.
 *
 * Validates: Requirements 2.1, 2.2, 2.3, 2.4, 2.5, 2.6, 2.7, 2.8
 *
 * These tests run authenticated (storageState is loaded by the chromium project)
 * so they start already logged in and can navigate to /home directly.
 *
 * ─────────────────────────────────────────────────────────────────────────────
 * CODEGEN — Interactive Test Recorder (Req 9.3, 9.4)
 * ─────────────────────────────────────────────────────────────────────────────
 *
 * Playwright Codegen launches a browser and records your clicks as test code:
 *
 *   npx playwright codegen http://localhost:4200
 *
 * This opens a browser window and an inspector panel. As you interact with the
 * page (clicking buttons, filling forms, navigating), Codegen generates the
 * corresponding Playwright API calls in real time.
 *
 * IMPORTANT — Refactoring Codegen Output:
 *   Codegen often produces CSS selectors or XPath-based locators like:
 *     page.locator('#mat-input-0')
 *     page.locator('div > span.class-name')
 *
 *   These are FRAGILE and will break when the DOM structure changes. Always
 *   refactor Codegen-generated locators to use user-facing strategies:
 *     - page.getByRole('button', { name: 'Search' })
 *     - page.getByLabel('Username')
 *     - page.getByTestId('search-input')
 *     - page.getByPlaceholder('pikachu')
 *
 *   User-facing locators are resilient to implementation changes because they
 *   target what the user sees (roles, labels, text) rather than internal DOM
 *   structure (CSS classes, IDs, nesting depth).
 *
 * ─────────────────────────────────────────────────────────────────────────────
 * TRACE VIEWER — Debugging Failed Tests (Req 9.2, 14.3, 14.4)
 * ─────────────────────────────────────────────────────────────────────────────
 *
 * When a test fails and retries, Playwright captures a trace file (.zip).
 * Open it with:
 *
 *   npx playwright show-trace playwright-results/<test-folder>/trace.zip
 *
 * Trace Viewer Features:
 *
 *   1. Actionability Logs — For each action (click, fill, etc.), the log shows
 *      WHY Playwright waited before executing. For example:
 *        - "waiting for element to be visible"
 *        - "waiting for element to be stable"
 *        - "waiting for element to receive pointer events"
 *      This is invaluable for diagnosing timeout failures — you can see exactly
 *      which actionability check failed and why the element wasn't ready.
 *
 *   2. Network Tab — Shows all HTTP requests and responses made during the test.
 *      Inspect request headers, response bodies, timing, and status codes.
 *      Useful for debugging API failures, verifying mocked routes were hit, or
 *      spotting unexpected network calls.
 *
 *   3. Console Tab — Displays all browser console output (console.log, warnings,
 *      errors) captured during the test. Helpful for seeing JavaScript errors,
 *      Angular framework warnings, or application-level debug messages that
 *      occurred during the failure.
 *
 *   4. DOM Snapshots — Step through each action and see the exact state of the
 *      page DOM at that moment. Click any action in the timeline to inspect
 *      what the page looked like before and after the action.
 *
 * ─────────────────────────────────────────────────────────────────────────────
 */
test.describe('Locator Methods', () => {

  /**
   * Requirement 2.1 — getByRole
   * Locates elements by their ARIA role (button, heading, textbox, etc.)
   */
  test('getByRole locates a button on the home page', async ({ page }) => {
    await page.goto('/home');

    // Locate the "Search" button by its role and accessible name
    const searchButton = page.getByRole('button', { name: 'Search' });
    await expect(searchButton).toBeVisible();
  });

  /**
   * Requirement 2.2 — getByLabel
   * Locates form inputs by their associated label text.
   *
   * Note: The login form uses <label><p>Username</p><input/></label>.
   * Playwright's getByLabel may not resolve this since the text is inside a <p>
   * rather than being direct label text. We navigate to /login in a new context
   * to demonstrate the concept. If getByLabel doesn't work with the nested <p>,
   * we fall back to locating via the label structure.
   */
  test('getByLabel locates form inputs on the login page', async ({ page }) => {
    // Navigate to login page (no auth required to view it)
    await page.goto('/login');

    // The app wraps input text in <label><p>Username</p><input/></label>
    // getByLabel looks for label text — attempt with the label text content
    const usernameInput = page.getByLabel('Username');
    await expect(usernameInput).toBeVisible();

    const passwordInput = page.getByLabel('Password');
    await expect(passwordInput).toBeVisible();
  });

  /**
   * Requirement 2.3 — getByPlaceholder
   * Locates inputs via their placeholder attribute value.
   */
  test('getByPlaceholder locates the search input', async ({ page }) => {
    await page.goto('/home');

    // The search input has placeholder="pikachu"
    const searchInput = page.getByPlaceholder('pikachu');
    await expect(searchInput).toBeVisible();
  });

  /**
   * Requirement 2.4 — getByText
   * Locates elements by their visible text content.
   */
  test('getByText locates elements with visible text content', async ({ page }) => {
    await page.goto('/home');

    // The instructions component renders a <p> with instruction text
    // The search button has text "Search"
    const searchButtonText = page.getByText('Search');
    await expect(searchButtonText).toBeVisible();
  });

  /**
   * Requirement 2.5 — getByTestId
   * Locates elements via data-testid attributes.
   *
   * Note: This app uses `data-cy` attributes rather than the Playwright default
   * `data-testid`. We use locator('[data-cy="..."]') as an equivalent approach.
   * Alternatively, you could set `testIdAttribute: 'data-cy'` in playwright.config.ts
   * to make page.getByTestId() work with `data-cy` attributes.
   */
  test('getByTestId concept — locating elements via data attributes', async ({ page }) => {
    await page.goto('/home');

    // Using CSS attribute selector as equivalent to getByTestId with data-cy
    const searchInput = page.locator('[data-cy="search-input"]');
    await expect(searchInput).toBeVisible();

    const searchButton = page.locator('[data-cy="search-button"]');
    await expect(searchButton).toBeVisible();
  });

  /**
   * Requirement 2.6 — Locator Chaining
   * Scopes a child locator within a parent locator.
   */
  test('locator chaining scopes a child within a parent', async ({ page }) => {
    await page.goto('/home');

    // Scope: find the search button inside the search component div
    const searchComponent = page.locator('.pokedex-search-component');
    const searchButton = searchComponent.getByRole('button', { name: 'Search' });
    await expect(searchButton).toBeVisible();

    // Chain further: find the input inside the search component
    const searchInput = searchComponent.locator('input');
    await expect(searchInput).toBeVisible();
  });

  /**
   * Requirement 2.7 — filter({ hasText: ... })
   * Narrows a collection of elements by their text content.
   */
  test('filter({ hasText }) narrows a locator collection', async ({ page }) => {
    await page.goto('/home');

    // Search for a Pokemon to populate the page with types
    await page.getByPlaceholder('pikachu').fill('pikachu');
    await page.getByRole('button', { name: 'Search' }).click();

    // Wait for the types section to appear
    await expect(page.locator('.types-header')).toBeVisible();

    // Filter all type <p> elements to find one with specific text
    const typeItems = page.locator('.type-item');
    const electricType = typeItems.filter({ hasText: 'Electric' });
    await expect(electricType).toBeVisible();
  });

  /**
   * Requirement 2.8 — .nth()
   * Selects a specific item from a list by index.
   */
  test('.nth() selects a specific item from a list', async ({ page }) => {
    await page.goto('/home');

    // Search for a Pokemon to populate the moves list
    await page.getByPlaceholder('pikachu').fill('pikachu');
    await page.getByRole('button', { name: 'Search' }).click();

    // Wait for moves list to appear
    await expect(page.locator('.moves-header')).toBeVisible();

    // Select the first move item using .nth(0)
    const moveItems = page.locator('.moves-list li');
    const firstMove = moveItems.nth(0);
    await expect(firstMove).toBeVisible();

    // Select the second move item using .nth(1)
    const secondMove = moveItems.nth(1);
    await expect(secondMove).toBeVisible();
  });

});
