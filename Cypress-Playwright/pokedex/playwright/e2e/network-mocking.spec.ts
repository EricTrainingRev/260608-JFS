import { test, expect } from '../fixtures/authenticated-test';

/**
 * Network Interception and Mocking with `page.route()` and `route.fulfill()`.
 *
 * These tests demonstrate intercepting outgoing HTTP requests to PokéAPI and
 * returning fabricated responses. This allows testing UI behavior in isolation
 * from real backend availability, including error states and edge cases.
 *
 * Requirements: 6.1, 6.2, 6.3, 6.4, 6.5
 */
test.describe('Network Mocking with page.route() and route.fulfill()', () => {
  const POKEAPI_PATTERN = '**/pokeapi.co/api/v2/pokemon/**';

  const mockPokemonResponse = {
    name: 'mockemon',
    id: 999,
    sprites: {
      front_default: 'https://example.com/sprite.png',
      back_default: 'https://example.com/sprite-back.png',
    },
    types: [{ slot: 1, type: { name: 'fire', url: '' } }],
    moves: [{ move: { name: 'mock-attack', url: '' } }],
  };

  test('intercept PokéAPI request with page.route() and fulfill with mock data', async ({ page }) => {
    // Requirement 6.1: Use page.route() to intercept requests matching the PokéAPI URL pattern
    // Requirement 6.2: Use route.fulfill() to return a custom JSON response with status 200
    await page.route(POKEAPI_PATTERN, async (route) => {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify(mockPokemonResponse),
      });
    });

    await page.goto('/home');

    // Search for a Pokemon to trigger the API request
    await page.locator('[data-cy="search-input"]').fill('mockemon');
    await page.locator('[data-cy="search-button"]').click();

    // Requirement 6.3: Verify the UI renders content from the mocked response
    await expect(page.locator('h2.pokemon-name')).toHaveText('Mockemon');
    await expect(page.locator('p.type-item').first()).toContainText('Fire');
    await expect(page.locator('ul.moves-list li').first()).toContainText('Mock Attack');
    await expect(page.locator('img.sprite-img').first()).toBeVisible();
  });

  test('mock an error response and verify the app handles failure', async ({ page }) => {
    // Requirement 6.4: Mock an error status and verify the application handles failure
    await page.route(POKEAPI_PATTERN, async (route) => {
      await route.fulfill({
        status: 500,
        contentType: 'application/json',
        body: JSON.stringify({ detail: 'Internal Server Error' }),
      });
    });

    await page.goto('/home');

    // Search for a Pokemon — the mocked route returns a 500 error
    await page.locator('[data-cy="search-input"]').fill('pikachu');
    await page.locator('[data-cy="search-button"]').click();

    // The app conditionally shows Pokemon data only when pokemonPresent() is true.
    // On error, the Pokemon section should not render.
    await expect(page.locator('h2.pokemon-name')).not.toBeVisible();
    await expect(page.locator('ul.moves-list')).not.toBeVisible();
  });

  test('real network requests are not sent when a route is fulfilled', async ({ page }) => {
    // Requirement 6.5: Demonstrate that real network requests are not sent when fulfilled
    const interceptedRequests: string[] = [];

    // Track all outgoing requests to PokéAPI
    page.on('request', (request) => {
      if (request.url().includes('pokeapi.co')) {
        interceptedRequests.push(request.url());
      }
    });

    // Set up route to fulfill with mock data — preventing real network call
    await page.route(POKEAPI_PATTERN, async (route) => {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify(mockPokemonResponse),
      });
    });

    await page.goto('/home');

    // Trigger a search
    await page.locator('[data-cy="search-input"]').fill('mockemon');
    await page.locator('[data-cy="search-button"]').click();

    // Verify the mock data rendered (proving the route was hit)
    await expect(page.locator('h2.pokemon-name')).toHaveText('Mockemon');

    // The requests were intercepted by page.route() and fulfilled locally —
    // no actual HTTP call leaves the browser. The request event still fires,
    // but the response comes from route.fulfill(), not the real server.
    // We verify by checking that the response we got is our mock data, not real PokéAPI data.
    const pokemonName = await page.locator('h2.pokemon-name').textContent();
    expect(pokemonName).toBe('Mockemon'); // This name doesn't exist on real PokéAPI

    // Additionally confirm the route was actually intercepted (request event fired)
    expect(interceptedRequests.length).toBeGreaterThan(0);
    expect(interceptedRequests[0]).toContain('pokeapi.co/api/v2/pokemon/');
  });
});
