import { test as base, expect } from '@playwright/test';

/**
 * Authenticated Test Base
 *
 * This app uses sessionStorage (not localStorage or cookies) for authentication.
 * Playwright's storageState only persists cookies and localStorage, so sessionStorage
 * is lost between contexts. This custom test base uses `addInitScript` to inject
 * the sessionStorage auth flag before any page JavaScript runs, ensuring the route
 * guard sees the user as authenticated.
 *
 * Usage: Import `test` and `expect` from this file instead of `@playwright/test`
 * in any spec that navigates to authenticated routes (e.g., /home).
 */
export const test = base.extend({
  page: async ({ page }, use) => {
    // addInitScript runs in the browser BEFORE any page script executes.
    // This ensures sessionStorage.authenticated is set before Angular's
    // route guard checks it on navigation.
    await page.addInitScript(() => {
      sessionStorage.setItem('authenticated', 'true');
    });
    await use(page);
  },
});

export { expect };
