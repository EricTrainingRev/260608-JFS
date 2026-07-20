import { test, expect } from '@playwright/test';

/**
 * Advanced API Handling — Custom Headers and Deep Response Parsing
 *
 * These tests demonstrate advanced request patterns using Playwright's `request`
 * fixture: sending custom headers (Authorization, Accept, X-Custom-Header),
 * parsing deeply nested JSON response properties via destructuring, and
 * combining response.ok(), response.status(), and response.json() for
 * complete response validation.
 *
 * Requirements: 11.1, 11.2, 11.3, 11.4
 */
test.describe('Advanced API Handling', () => {
  const BASE_URL = 'https://pokeapi.co/api/v2/pokemon';

  test.describe('Custom Headers', () => {
    test('send request with Authorization, Accept, and custom headers', async ({ request }) => {
      // Requirement 11.1: Demonstrate sending API requests with custom headers
      const response = await request.get(`${BASE_URL}/charizard`, {
        headers: {
          'Authorization': 'Bearer my-test-token-12345',
          'Accept': 'application/json',
          'X-Custom-Header': 'playwright-advanced-test',
        },
      });

      // Requirement 11.4: Use response.ok(), response.status(), and response.json() together
      expect(response.ok()).toBeTruthy();
      expect(response.status()).toBe(200);

      const body = await response.json();
      expect(body.name).toBe('charizard');
    });

    test('send request with multiple custom header values', async ({ request }) => {
      // Requirement 11.1: Additional custom header demonstration
      const response = await request.get(`${BASE_URL}/bulbasaur`, {
        headers: {
          'Authorization': 'Bearer another-token-67890',
          'Accept': 'application/json',
          'X-Request-ID': 'req-abc-123',
          'X-Client-Version': '2.0.0',
        },
      });

      expect(response.ok()).toBeTruthy();
      expect(response.status()).toBe(200);

      const body = await response.json();
      expect(body.name).toBe('bulbasaur');
    });
  });

  test.describe('Deeply Nested JSON Parsing', () => {
    test('parse official artwork URL from deeply nested sprites', async ({ request }) => {
      // Requirement 11.2: Parse deeply nested JSON response properties using destructuring
      const response = await request.get(`${BASE_URL}/pikachu`);
      const body = await response.json();

      // Access deeply nested property: sprites.other['official-artwork'].front_default
      const { sprites: { other: { 'official-artwork': { front_default: officialArtwork } } } } = body;

      expect(officialArtwork).toBeTruthy();
      expect(typeof officialArtwork).toBe('string');
      expect(officialArtwork).toContain('https://');
    });

    test('parse type information from nested array structures', async ({ request }) => {
      // Requirement 11.2: Destructure nested properties
      // Requirement 11.3: Assert values within nested structures (objects within arrays)
      const response = await request.get(`${BASE_URL}/charizard`);
      const body = await response.json();

      // Destructure the first type entry: types[0].type.name
      const { types: [{ type: { name: firstTypeName } }] } = body;
      expect(firstTypeName).toBe('fire');

      // Destructure the second type entry: types[1].type.name
      const { types: [, { type: { name: secondTypeName } }] } = body;
      expect(secondTypeName).toBe('flying');
    });

    test('parse ability information from nested structures', async ({ request }) => {
      // Requirement 11.2: Parse deeply nested properties
      // Requirement 11.3: Assert values within arrays inside objects
      const response = await request.get(`${BASE_URL}/pikachu`);
      const body = await response.json();

      // Destructure ability: abilities[0].ability.name
      const { abilities: [{ ability: { name: firstAbilityName } }] } = body;
      expect(typeof firstAbilityName).toBe('string');
      expect(firstAbilityName).toBe('static');
    });

    test('parse stats from nested array of objects', async ({ request }) => {
      // Requirement 11.2: Destructure deeply nested properties
      // Requirement 11.3: Assert values within nested structures
      const response = await request.get(`${BASE_URL}/pikachu`);
      const body = await response.json();

      // Destructure stats: stats[0].base_stat and stats[0].stat.name
      const { stats: [{ base_stat: firstBaseStat, stat: { name: firstStatName } }] } = body;
      expect(typeof firstBaseStat).toBe('number');
      expect(firstBaseStat).toBeGreaterThan(0);
      expect(firstStatName).toBe('hp');
    });

    test('parse game indices from nested structures', async ({ request }) => {
      // Requirement 11.2: Deeply nested property access
      // Requirement 11.3: Arrays within objects, objects within arrays
      const response = await request.get(`${BASE_URL}/pikachu`);
      const body = await response.json();

      // Destructure game_indices: game_indices[0].game_index and game_indices[0].version.name
      const { game_indices: [{ game_index: firstGameIndex, version: { name: firstVersionName } }] } = body;
      expect(typeof firstGameIndex).toBe('number');
      expect(typeof firstVersionName).toBe('string');
      expect(firstVersionName.length).toBeGreaterThan(0);
    });
  });

  test.describe('Complete Response Validation', () => {
    test('validate response using ok(), status(), and json() together', async ({ request }) => {
      // Requirement 11.4: Use response.ok(), response.status(), and response.json() together
      const response = await request.get(`${BASE_URL}/gengar`, {
        headers: {
          'Authorization': 'Bearer validation-token',
          'Accept': 'application/json',
        },
      });

      // Step 1: Verify response is successful
      expect(response.ok()).toBeTruthy();

      // Step 2: Verify exact status code
      expect(response.status()).toBe(200);

      // Step 3: Parse and validate the JSON body
      const body = await response.json();
      expect(body.name).toBe('gengar');
      expect(body.id).toBe(94);

      // Validate nested structures within the body
      const { types: [{ type: { name: primaryType } }] } = body;
      expect(primaryType).toBe('ghost');

      // Validate array lengths indicate populated data
      expect(body.abilities.length).toBeGreaterThan(0);
      expect(body.moves.length).toBeGreaterThan(0);
      expect(body.stats).toHaveLength(6);
    });

    test('validate failed response with complete status checking', async ({ request }) => {
      // Requirement 11.4: Complete validation including error scenarios
      const response = await request.get(`${BASE_URL}/not-a-pokemon-99999`, {
        headers: {
          'Authorization': 'Bearer error-test-token',
          'Accept': 'application/json',
          'X-Custom-Header': 'error-scenario',
        },
        failOnStatusCode: false,
      });

      // response.ok() returns false for non-2xx status codes
      expect(response.ok()).toBeFalsy();

      // response.status() gives the exact HTTP status
      expect(response.status()).toBe(404);
    });
  });
});
