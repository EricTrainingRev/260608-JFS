import { test, expect } from '../fixtures/authenticated-test';

/**
 * Conditional Mocking with Logic-Based Route Handlers.
 *
 * These tests demonstrate using `page.route()` with conditional logic inside the
 * handler to return different responses based on the request URL. This pattern
 * allows a single route registration to simulate multiple server behaviors,
 * including fallback to the real server via `route.continue()`.
 *
 * Requirements: 12.1, 12.2, 12.3, 12.4
 */
test.describe('Conditional Mocking with Logic-Based Route Handlers', () => {
  const POKEAPI_PATTERN = '**/pokeapi.co/api/v2/pokemon/**';

  // Mock response for Pikachu (electric type)
  const pikachuMock = {
    name: 'pikachu',
    id: 25,
    sprites: { front_default: 'https://example.com/pikachu.png' },
    types: [{ slot: 1, type: { name: 'electric', url: '' } }],
    moves: [{ move: { name: 'thunderbolt', url: '' } }],
  };

  // Mock response for Charizard (fire/flying types)
  const charizardMock = {
    name: 'charizard',
    id: 6,
    sprites: { front_default: 'https://example.com/charizard.png' },
    types: [
      { slot: 1, type: { name: 'fire', url: '' } },
      { slot: 2, type: { name: 'flying', url: '' } },
    ],
    moves: [{ move: { name: 'flamethrower', url: '' } }],
  };

  /**
   * Requirement 12.1: Define a page.route() handler that inspects the request URL
   * to determine which mock response to return.
   *
   * Requirement 12.2: Return different response bodies and status codes based on
   * URL path segments.
   *
   * Requirement 12.3: Demonstrate route.continue() as a fallback for unmatched requests.
   */
  function setupConditionalRoute(page: import('@playwright/test').Page) {
    return page.route(POKEAPI_PATTERN, async (route) => {
      const url = route.request().url();

      if (url.includes('/pikachu')) {
        // Return mock pikachu response with electric type
        await route.fulfill({
          status: 200,
          contentType: 'application/json',
          body: JSON.stringify(pikachuMock),
        });
      } else if (url.includes('/charizard')) {
        // Return mock charizard response with fire/flying types
        await route.fulfill({
          status: 200,
          contentType: 'application/json',
          body: JSON.stringify(charizardMock),
        });
      } else if (url.includes('/unknown-pokemon-error')) {
        // Return 404 for a specific "error" keyword indicating unknown pokemon
        await route.fulfill({
          status: 404,
          contentType: 'application/json',
          body: JSON.stringify({ detail: 'Not found' }),
        });
      } else {
        // Fallback: let unmatched requests proceed to the real server
        await route.continue();
      }
    });
  }

  test('conditional handler returns pikachu mock when URL contains /pikachu', async ({ page }) => {
    // Requirement 12.1: Route handler inspects URL to choose response
    await setupConditionalRoute(page);
    await page.goto('/home');

    await page.locator('[data-cy="search-input"]').fill('pikachu');
    await page.locator('[data-cy="search-button"]').click();

    // Requirement 12.4: Verify the UI correctly reflects the pikachu mock
    await expect(page.locator('h2.pokemon-name')).toHaveText('Pikachu');
    await expect(page.locator('p.type-item').first()).toContainText('Electric');
    await expect(page.locator('ul.moves-list li').first()).toContainText('Thunderbolt');
  });

  test('conditional handler returns charizard mock when URL contains /charizard', async ({ page }) => {
    // Requirement 12.2: Different response body based on URL path segment
    await setupConditionalRoute(page);
    await page.goto('/home');

    await page.locator('[data-cy="search-input"]').fill('charizard');
    await page.locator('[data-cy="search-button"]').click();

    // Requirement 12.4: Verify the UI reflects charizard's dual types
    await expect(page.locator('h2.pokemon-name')).toHaveText('Charizard');
    await expect(page.locator('p.type-item').first()).toContainText('Fire');
    await expect(page.locator('p.type-item').nth(1)).toContainText('Flying');
    await expect(page.locator('ul.moves-list li').first()).toContainText('Flamethrower');
  });

  test('conditional handler returns 404 for unknown pokemon error path', async ({ page }) => {
    // Requirement 12.2: Return different status codes based on URL path segments
    await setupConditionalRoute(page);
    await page.goto('/home');

    await page.locator('[data-cy="search-input"]').fill('unknown-pokemon-error');
    await page.locator('[data-cy="search-button"]').click();

    // On 404, the app should not render Pokemon data
    await expect(page.locator('h2.pokemon-name')).not.toBeVisible();
    await expect(page.locator('ul.moves-list')).not.toBeVisible();
  });

  test('conditional handler uses route.continue() for unmatched requests', async ({ page }) => {
    // Requirement 12.3: Demonstrate route.continue() as a fallback
    let continuedRequest = false;

    await page.route(POKEAPI_PATTERN, async (route) => {
      const url = route.request().url();

      if (url.includes('/pikachu')) {
        await route.fulfill({
          status: 200,
          contentType: 'application/json',
          body: JSON.stringify(pikachuMock),
        });
      } else {
        // Fallback: let request continue to the real server
        continuedRequest = true;
        await route.continue();
      }
    });

    await page.goto('/home');

    // Search for a Pokemon not explicitly handled — triggers route.continue()
    await page.locator('[data-cy="search-input"]').fill('bulbasaur');
    await page.locator('[data-cy="search-button"]').click();

    // The request was forwarded to the real server via route.continue()
    expect(continuedRequest).toBe(true);

    // The real API response should render (bulbasaur exists on PokéAPI)
    await expect(page.locator('h2.pokemon-name')).toHaveText('Bulbasaur');
  });
});
