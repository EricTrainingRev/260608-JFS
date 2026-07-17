/**
 * Test Isolation & Browser Context Demonstration
 *
 * Validates: Requirements 8.1, 8.2, 8.3, 8.4
 *
 * Playwright creates a FRESH Browser Context for every test by default.
 * A Browser Context is equivalent to an incognito browser window — it has its
 * own cookies, session storage, local storage, and cache that are completely
 * isolated from other contexts.
 *
 * This means:
 * - Each test starts with a blank slate (no leftover cookies, storage, etc.)
 * - State set during one test (e.g., sessionStorage items) is automatically
 *   discarded before the next test runs.
 * - Tests can run in parallel without interfering with each other.
 * - No manual cleanup (beforeEach/afterEach) is needed for state reset.
 *
 * This file demonstrates this isolation model by:
 * 1. Verifying session storage is empty at the start of each test.
 * 2. Setting session storage in one test and confirming it does NOT persist
 *    into the subsequent test.
 * 3. Using page.evaluate() to inspect and manipulate browser storage.
 */

import { test, expect } from '@playwright/test';

/**
 * Override the storageState to start with completely empty storage.
 * The chromium project normally loads storageState from .auth/user.json
 * (which includes the 'authenticated' sessionStorage item). By setting
 * storageState to an empty object, we ensure tests start with zero state.
 */
test.use({ storageState: { cookies: [], origins: [] } });

test.describe('Browser Context Isolation', () => {
  /**
   * This test runs FIRST. It verifies that session storage is empty
   * at the beginning of a test (fresh Browser Context), then sets a
   * value in session storage. If isolation is working correctly, the
   * next test should NOT see this value.
   */
  test('session storage is empty at test start and can be set', async ({ page }) => {
    // Navigate to /login since we have no auth (empty storageState)
    await page.goto('/login');

    // Verify session storage is completely empty at the start of this test
    const storageLength = await page.evaluate(() => sessionStorage.length);
    expect(storageLength).toBe(0);

    // Verify a specific key does not exist
    const testValue = await page.evaluate(() =>
      sessionStorage.getItem('isolation-test-key')
    );
    expect(testValue).toBeNull();

    // Now SET a value in session storage within this test
    await page.evaluate(() => {
      sessionStorage.setItem('isolation-test-key', 'set-in-first-test');
      sessionStorage.setItem('another-key', 'should-not-persist');
    });

    // Confirm the values were set successfully within this context
    const setValue = await page.evaluate(() =>
      sessionStorage.getItem('isolation-test-key')
    );
    expect(setValue).toBe('set-in-first-test');

    const anotherValue = await page.evaluate(() =>
      sessionStorage.getItem('another-key')
    );
    expect(anotherValue).toBe('should-not-persist');
  });

  /**
   * This test runs SECOND. It verifies that the session storage values
   * set in the previous test are NOT present — proving that Playwright
   * created a completely new Browser Context for this test.
   *
   * This is the core demonstration of test isolation: state from one
   * test never leaks into another because each test gets its own context.
   */
  test('state from previous test does not persist (fresh context)', async ({ page }) => {
    // Navigate to /login (fresh context, no auth)
    await page.goto('/login');

    // Verify session storage is empty — the items set in the previous
    // test should NOT be here because this is a brand-new Browser Context
    const storageLength = await page.evaluate(() => sessionStorage.length);
    expect(storageLength).toBe(0);

    // Explicitly check that the keys set in the first test do not exist
    const isolationKey = await page.evaluate(() =>
      sessionStorage.getItem('isolation-test-key')
    );
    expect(isolationKey).toBeNull();

    const anotherKey = await page.evaluate(() =>
      sessionStorage.getItem('another-key')
    );
    expect(anotherKey).toBeNull();
  });

  /**
   * This test further demonstrates isolation by setting cookies in
   * one context and verifying they are absent. It also shows that
   * page.evaluate() can be used to manipulate any browser-side state.
   */
  test('cookies set in a test do not persist to subsequent tests', async ({ page }) => {
    // Navigate to /login
    await page.goto('/login');

    // Verify no cookies are set at the start of this fresh context
    const cookies = await page.context().cookies();
    expect(cookies).toHaveLength(0);

    // Set a cookie via page.evaluate for demonstration
    await page.evaluate(() => {
      document.cookie = 'test-cookie=isolation-demo; path=/';
    });

    // Confirm the cookie was set within this context
    const updatedCookies = await page.context().cookies();
    const testCookie = updatedCookies.find(c => c.name === 'test-cookie');
    expect(testCookie).toBeDefined();
    expect(testCookie!.value).toBe('isolation-demo');
  });

  /**
   * This test verifies the cookie set in the previous test is gone,
   * reinforcing that each test gets an isolated Browser Context.
   */
  test('cookies from previous test are absent (confirms isolation)', async ({ page }) => {
    // Navigate to /login
    await page.goto('/login');

    // The cookie set in the previous test should not exist here
    const cookies = await page.context().cookies();
    const testCookie = cookies.find(c => c.name === 'test-cookie');
    expect(testCookie).toBeUndefined();

    // Session storage should also be empty
    const storageLength = await page.evaluate(() => sessionStorage.length);
    expect(storageLength).toBe(0);
  });
});
