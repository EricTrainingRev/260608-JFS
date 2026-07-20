import { test as setup, expect } from '@playwright/test';

const authFile = 'playwright/.auth/user.json';

setup('authenticate', async ({ page }) => {
  // Navigate to the login page
  await page.goto('/login');

  // Fill in the username and password fields
  await page.locator('[data-cy="username-input"]').fill('dev');
  await page.locator('[data-cy="password-input"]').fill('dev');

  // Click the login button
  await page.getByRole('button', { name: 'Login' }).click();

  // Wait for redirect to /home
  await page.waitForURL('**/home');

  // The app uses sessionStorage for auth state, which Playwright's storageState
  // does not capture. Mirror the auth flag into localStorage so that storageState
  // persists it across test contexts. The chromium project uses addInitScript
  // (configured below) to copy localStorage.authenticated → sessionStorage on load.
  await page.evaluate(() => {
    localStorage.setItem('authenticated', 'true');
  });

  // Save storage state for reuse by other test projects
  await page.context().storageState({ path: authFile });
});
