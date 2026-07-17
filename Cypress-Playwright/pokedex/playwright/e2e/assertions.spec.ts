import { test, expect } from '../fixtures/authenticated-test';

/**
 * Web-First Assertions (Requirement 4)
 *
 * Playwright assertions are "web-first" — they automatically retry until the
 * condition is met or the timeout expires. This eliminates the need for manual
 * waits or sleeps when checking dynamic UI state.
 *
 * Validates: Requirements 4.1, 4.2, 4.3, 4.4, 4.5, 4.6, 4.7
 */

test.describe('Web-First Assertions', () => {

  // Requirement 4.1: toBeVisible() — assert element appears after user interaction
  test('toBeVisible - error message appears after failed login', async ({ browser }) => {
    // Use a fresh context (no stored auth) to test the login page
    const context = await browser.newContext();
    const page = await context.newPage();

    await page.goto('/login');

    // Fill invalid credentials and submit
    await page.locator('[data-cy="username-input"]').fill('wronguser');
    await page.locator('[data-cy="password-input"]').fill('wrongpass');
    await page.locator('[data-cy="login-button"]').click();

    // The error message becomes visible after the failed login attempt
    await expect(page.locator('[data-cy="error-message"]')).toBeVisible();

    await context.close();
  });

  // Requirement 4.2: toBeEnabled() — assert a button or input is interactable
  test('toBeEnabled - search button is enabled and interactable', async ({ page }) => {
    await page.goto('/home');

    // The search button should be enabled and ready for interaction
    await expect(page.locator('[data-cy="search-button"]')).toBeEnabled();

    // The search input should also be enabled
    await expect(page.locator('[data-cy="search-input"]')).toBeEnabled();
  });

  // Requirement 4.3: toBeChecked() — assert checkbox state after interaction
  test('toBeChecked - checkbox reflects checked state after clicking', async ({ page }) => {
    await page.goto('/home');

    // The app does not have a native checkbox, so we inject one to demonstrate the assertion API
    await page.evaluate(() => {
      const checkbox = document.createElement('input');
      checkbox.type = 'checkbox';
      checkbox.id = 'demo-checkbox';
      document.body.appendChild(checkbox);
    });

    const checkbox = page.locator('#demo-checkbox');

    // Initially unchecked
    await expect(checkbox).not.toBeChecked();

    // Check the checkbox
    await checkbox.check();

    // Assert the checkbox is now checked
    await expect(checkbox).toBeChecked();
  });

  // Requirement 4.4: toHaveText() — assert text content of a heading or label
  test('toHaveText - Pokemon name heading displays after search', async ({ page }) => {
    await page.goto('/home');

    // Search for a Pokemon
    await page.locator('[data-cy="search-input"]').fill('pikachu');
    await page.locator('[data-cy="search-button"]').click();

    // The heading should display the Pokemon name (titlecase via Angular pipe)
    await expect(page.locator('.pokemon-name')).toHaveText('Pikachu');
  });

  // Requirement 4.5: toHaveValue() — assert current value of an input field
  test('toHaveValue - search input reflects the filled value', async ({ page }) => {
    await page.goto('/home');

    // Fill the search input
    await page.locator('[data-cy="search-input"]').fill('charizard');

    // Assert the input's current value matches what was typed
    await expect(page.locator('[data-cy="search-input"]')).toHaveValue('charizard');
  });

  // Requirement 4.6: toHaveURL() — assert page URL after navigation/login redirect
  test('toHaveURL - URL contains /home after successful login', async ({ browser }) => {
    // Use a fresh context (no stored auth) to perform login
    const context = await browser.newContext();
    const page = await context.newPage();

    await page.goto('/login');

    // Perform login with valid credentials
    await page.locator('[data-cy="username-input"]').fill('dev');
    await page.locator('[data-cy="password-input"]').fill('dev');
    await page.locator('[data-cy="login-button"]').click();

    // Assert the URL now contains /home after redirect
    await expect(page).toHaveURL(/\/home/);

    await context.close();
  });

  // Requirement 4.7: toHaveCount() — assert number of elements in a collection
  test('toHaveCount - moves list contains multiple items after search', async ({ page }) => {
    await page.goto('/home');

    // Search for a Pokemon to populate the moves list
    await page.locator('[data-cy="search-input"]').fill('pikachu');
    await page.locator('[data-cy="search-button"]').click();

    // Wait for the moves list to appear (API response must arrive first)
    const moveItems = page.locator('.moves-list li');
    await expect(moveItems.first()).toBeVisible();

    // Assert the moves list has more than one item
    const count = await moveItems.count();
    expect(count).toBeGreaterThan(1);
  });
});
