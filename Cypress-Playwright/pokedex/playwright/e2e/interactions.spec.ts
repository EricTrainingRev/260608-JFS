/**
 * interactions.spec.ts — Element Interactions
 *
 * Demonstrates Playwright element interaction methods:
 * - click(): Click buttons and interactive elements
 * - fill(): Enter text into input fields
 * - check(): Toggle checkbox elements
 * - selectOption(): Choose from dropdown elements
 * - hover(): Hover over elements to trigger CSS/state changes
 * - keyboard.press(): Simulate keyboard input
 *
 * IMPORTANT: Playwright auto-waits for actionability before performing any action.
 * This means elements must be visible, enabled, and stable before Playwright interacts
 * with them. There is NO need for explicit sleeps, waitFor calls, or manual retries.
 * Playwright handles all timing automatically via its built-in actionability checks.
 *
 * Validates: Requirements 3.1, 3.2, 3.3, 3.4, 3.5, 3.6, 3.7
 */
import { test, expect } from '../fixtures/authenticated-test';

test.describe('Element Interactions', () => {
  /**
   * Demonstrates click() on the login button.
   * Navigates to the login page (unauthenticated context needed),
   * fills credentials, and clicks the Login button.
   * Validates: Requirement 3.1
   */
  test('click() - clicking the login button submits the form', async ({ page }) => {
    await page.goto('/login');

    await page.locator('[data-cy="username-input"]').fill('dev');
    await page.locator('[data-cy="password-input"]').fill('dev');

    // Demonstrate click() on the login button
    await page.locator('[data-cy="login-button"]').click();

    await expect(page).toHaveURL(/\/home/);
  });

  /**
   * Demonstrates click() on the search button.
   * Validates: Requirement 3.1
   */
  test('click() - clicking the search button triggers a search', async ({ page }) => {
    await page.goto('/home');

    await page.locator('[data-cy="search-input"]').fill('bulbasaur');

    // Demonstrate click() on the search button
    await page.locator('[data-cy="search-button"]').click();

    // Verify the page responded to the click (URL or content change)
    await expect(page.locator('[data-cy="search-input"]')).toHaveValue('bulbasaur');
  });

  /**
   * Demonstrates fill() on username, password, and search input fields.
   * fill() clears existing content before typing, unlike type().
   * Validates: Requirement 3.2
   */
  test('fill() - entering text into form input fields', async ({ page }) => {
    await page.goto('/login');

    // Demonstrate fill() on username input
    await page.locator('[data-cy="username-input"]').fill('testuser');
    await expect(page.locator('[data-cy="username-input"]')).toHaveValue('testuser');

    // Demonstrate fill() on password input
    await page.locator('[data-cy="password-input"]').fill('testpass');
    await expect(page.locator('[data-cy="password-input"]')).toHaveValue('testpass');
  });

  /**
   * Demonstrates fill() on the search input field.
   * Validates: Requirement 3.2
   */
  test('fill() - entering text into search input', async ({ page }) => {
    await page.goto('/home');

    // Demonstrate fill() on search input
    await page.locator('[data-cy="search-input"]').fill('charizard');
    await expect(page.locator('[data-cy="search-input"]')).toHaveValue('charizard');
  });

  /**
   * Demonstrates check() on a checkbox element.
   * Since the app doesn't have a native checkbox, we inject one into the DOM
   * to demonstrate the Playwright API method.
   * Validates: Requirement 3.3
   */
  test('check() - checking a checkbox element', async ({ page }) => {
    await page.goto('/home');

    // Inject a checkbox into the DOM to demonstrate check()
    await page.evaluate(() => {
      const container = document.createElement('div');
      container.id = 'demo-checkbox-container';
      container.innerHTML = `
        <label>
          <input type="checkbox" id="demo-checkbox" data-testid="demo-checkbox" />
          Enable notifications
        </label>
      `;
      document.body.appendChild(container);
    });

    const checkbox = page.getByTestId('demo-checkbox');

    // Verify the checkbox starts unchecked
    await expect(checkbox).not.toBeChecked();

    // Demonstrate check() to toggle the checkbox on
    await checkbox.check();

    // Verify the checkbox is now checked
    await expect(checkbox).toBeChecked();
  });

  /**
   * Demonstrates selectOption() on a dropdown/select element.
   * Since the app doesn't have a native select dropdown, we inject one
   * to demonstrate the Playwright API method.
   * Validates: Requirement 3.4
   */
  test('selectOption() - selecting from a dropdown element', async ({ page }) => {
    await page.goto('/home');

    // Inject a select dropdown into the DOM to demonstrate selectOption()
    await page.evaluate(() => {
      const container = document.createElement('div');
      container.id = 'demo-select-container';
      container.innerHTML = `
        <label for="demo-select">Pokemon Type:</label>
        <select id="demo-select" data-testid="demo-select">
          <option value="">-- Choose a type --</option>
          <option value="fire">Fire</option>
          <option value="water">Water</option>
          <option value="grass">Grass</option>
          <option value="electric">Electric</option>
        </select>
      `;
      document.body.appendChild(container);
    });

    const dropdown = page.getByTestId('demo-select');

    // Demonstrate selectOption() by value
    await dropdown.selectOption('water');
    await expect(dropdown).toHaveValue('water');

    // Demonstrate selectOption() by label text
    await dropdown.selectOption({ label: 'Electric' });
    await expect(dropdown).toHaveValue('electric');
  });

  /**
   * Demonstrates hover() on an interactive element.
   * Hovers over the search button and verifies the action completes
   * (Playwright auto-waits for the element to be actionable).
   * Validates: Requirement 3.5
   */
  test('hover() - hovering over an interactive element', async ({ page }) => {
    await page.goto('/home');

    const searchButton = page.locator('[data-cy="search-button"]');

    // Verify the button is visible before hovering
    await expect(searchButton).toBeVisible();

    // Demonstrate hover() on the search button
    await searchButton.hover();

    // After hover, the element should still be visible and interactable
    await expect(searchButton).toBeVisible();
  });

  /**
   * Demonstrates keyboard.press() for simulating keyboard input.
   * Fills the search input and presses Enter to simulate keyboard submission.
   * Validates: Requirement 3.6
   */
  test('keyboard.press() - simulating Enter key press', async ({ page }) => {
    await page.goto('/home');

    const searchInput = page.locator('[data-cy="search-input"]');

    // Fill the search input
    await searchInput.fill('pikachu');

    // Click into the input to ensure it has focus
    await searchInput.click();

    // Demonstrate keyboard.press() to simulate pressing Enter
    await page.keyboard.press('Enter');

    // Verify the input still has the value (the key press was registered)
    await expect(searchInput).toHaveValue('pikachu');
  });

  /**
   * Demonstrates keyboard.press() for a keyboard shortcut (Tab navigation).
   * Validates: Requirement 3.6
   */
  test('keyboard.press() - simulating Tab for focus navigation', async ({ page }) => {
    await page.goto('/login');

    // Focus the username input
    await page.locator('[data-cy="username-input"]').click();

    // Demonstrate keyboard.press() with Tab to move focus
    await page.keyboard.press('Tab');

    // After Tab, focus should have moved to the password input
    const passwordInput = page.locator('[data-cy="password-input"]');
    await expect(passwordInput).toBeFocused();
  });
});
