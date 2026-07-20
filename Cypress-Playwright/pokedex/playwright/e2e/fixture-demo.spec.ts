import { test, expect } from '../fixtures/auth-fixture';

/**
 * Fixture Demo — Custom Fixtures with base.extend()
 *
 * This spec demonstrates using a custom fixture (`authenticatedPage`) that
 * provides an independently authenticated Page instance via the `use()` callback.
 * The fixture creates its own BrowserContext with saved storageState, so
 * tests receive a page that is already authenticated without performing login.
 *
 * Validates: Requirements 10.4, 10.5
 */
test.describe('Custom Fixture Demo', () => {
  test('authenticatedPage navigates to /home without performing login', async ({
    authenticatedPage,
  }) => {
    // Navigate to the protected route — no login step needed
    await authenticatedPage.goto('/home');

    // Verify we are on /home and NOT redirected to /login
    await expect(authenticatedPage).toHaveURL(/\/home/);

    // Confirm the page actually rendered authenticated content (heading present)
    await expect(
      authenticatedPage.getByRole('heading', { name: /pokedex|home/i })
    ).toBeVisible();
  });

  test('authenticatedPage is independent from the default page fixture', async ({
    authenticatedPage,
    page,
  }) => {
    // Navigate the fixture-provided page to /home (authenticated route)
    await authenticatedPage.goto('/home');
    await expect(authenticatedPage).toHaveURL(/\/home/);

    // Navigate the default page to /login (a different route)
    await page.goto('/login');
    await expect(page).toHaveURL(/\/login/);

    // Demonstrate that the two pages are completely independent instances
    const authenticatedUrl = authenticatedPage.url();
    const defaultUrl = page.url();

    expect(authenticatedUrl).toContain('/home');
    expect(defaultUrl).toContain('/login');
    expect(authenticatedUrl).not.toEqual(defaultUrl);
  });
});
