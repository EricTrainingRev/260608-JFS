import { test as base, Page } from '@playwright/test';

type AuthFixtures = {
  authenticatedPage: Page;
};

export const test = base.extend<AuthFixtures>({
  authenticatedPage: async ({ browser }, use) => {
    const context = await browser.newContext({
      storageState: './playwright/.auth/user.json',
    });
    const page = await context.newPage();

    // The app uses sessionStorage for auth state, which storageState does not
    // capture. Inject the auth flag via addInitScript so it's set before any
    // page JavaScript (including the route guard) runs.
    await page.addInitScript(() => {
      sessionStorage.setItem('authenticated', 'true');
    });

    await use(page);
    await context.close();
  },
});

export { expect } from '@playwright/test';
