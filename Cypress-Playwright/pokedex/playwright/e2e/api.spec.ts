import { test, expect } from '@playwright/test';

/**
 * API Testing with the Playwright `request` fixture.
 *
 * These tests demonstrate making direct HTTP calls to PokéAPI without launching
 * a browser page. The `request` fixture provides a lightweight APIRequestContext
 * for integration testing against real endpoints.
 *
 * Requirements: 5.1, 5.2, 5.3, 5.4, 5.5
 */
test.describe('API Testing with request fixture', () => {
  const BASE_URL = 'https://pokeapi.co/api/v2/pokemon';

  test('GET valid pokemon returns 200 and response.ok() is true', async ({ request }) => {
    // Requirement 5.1: Use the request fixture to make HTTP calls without a browser page
    // Requirement 5.2: Verify response status is 200 using response.ok()
    const response = await request.get(`${BASE_URL}/pikachu`);

    expect(response.ok()).toBeTruthy();
    expect(response.status()).toBe(200);
  });

  test('GET valid pokemon response body contains expected properties', async ({ request }) => {
    // Requirement 5.3: Verify response body contains expected properties
    const response = await request.get(`${BASE_URL}/pikachu`);
    const body = await response.json();

    // Verify top-level properties exist
    expect(body).toHaveProperty('name');
    expect(body).toHaveProperty('sprites');
    expect(body).toHaveProperty('types');
    expect(body).toHaveProperty('moves');

    // Verify property values are the expected types
    expect(body.name).toBe('pikachu');
    expect(Array.isArray(body.types)).toBeTruthy();
    expect(Array.isArray(body.moves)).toBeTruthy();
    expect(typeof body.sprites).toBe('object');
  });

  test('GET non-existent pokemon returns 404', async ({ request }) => {
    // Requirement 5.4: Verify response status is 404 for non-existent Pokemon
    const response = await request.get(`${BASE_URL}/not-a-real-pokemon-xyz`, {
      failOnStatusCode: false,
    });

    expect(response.ok()).toBeFalsy();
    expect(response.status()).toBe(404);
  });

  test('parse nested response properties from API response', async ({ request }) => {
    // Requirement 5.5: Demonstrate parsing nested response body properties
    const response = await request.get(`${BASE_URL}/pikachu`);
    const body = await response.json();

    // Parse nested property: sprites.front_default
    const frontDefaultSprite = body.sprites.front_default;
    expect(frontDefaultSprite).toBeTruthy();
    expect(typeof frontDefaultSprite).toBe('string');
    expect(frontDefaultSprite).toContain('https://');

    // Parse nested property: types[0].type.name
    const firstTypeName = body.types[0].type.name;
    expect(firstTypeName).toBe('electric');

    // Parse nested property: moves array contains move objects with nested name
    expect(body.moves.length).toBeGreaterThan(0);
    const firstMoveName = body.moves[0].move.name;
    expect(typeof firstMoveName).toBe('string');
    expect(firstMoveName.length).toBeGreaterThan(0);
  });
});
