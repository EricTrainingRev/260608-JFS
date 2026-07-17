import { test, expect } from '../fixtures/authenticated-test';

/**
 * Visual Regression — Screenshot Comparisons with toHaveScreenshot()
 *
 * This spec demonstrates Playwright's built-in visual regression testing using
 * `toHaveScreenshot()`. Screenshots are compared pixel-by-pixel against stored
 * baselines to detect unintended UI changes.
 *
 * Key concepts:
 * - `toHaveScreenshot()` captures a screenshot and compares it against a baseline
 * - On the FIRST run, baselines are created automatically (test will fail until
 *   you accept the new baseline by re-running)
 * - Use `npx playwright test --update-snapshots` to update baselines when the UI
 *   changes intentionally
 * - Baselines are stored in a `-snapshots` folder adjacent to this spec file
 *   (e.g., `visual-regression.spec.ts-snapshots/`)
 * - Different operating systems may produce different screenshots due to font
 *   rendering and anti-aliasing differences. Consider running visual tests in CI
 *   with a consistent environment (e.g., Docker container) for reliable comparisons.
 *
 * Configuration options:
 * - `fullPage: true` — captures the entire scrollable page, not just the viewport
 * - `maxDiffPixels` — maximum number of differing pixels allowed before failure
 * - `threshold` — per-pixel color comparison sensitivity (0 = exact, 1 = any color)
 *
 * Validates: Requirements 15.1, 15.2, 15.3, 15.4, 15.5
 */
test.describe('Visual Regression Tests', () => {
  test('home page matches baseline screenshot', async ({ page }) => {
    await page.goto('/home');

    // Full-page screenshot comparison with tolerance for minor rendering variations.
    // maxDiffPixels allows up to 100 pixels to differ (e.g., cursor blink, font hinting).
    // threshold sets per-pixel sensitivity on a 0-1 scale (0.2 tolerates slight color shifts).
    await expect(page).toHaveScreenshot('home-page.png', {
      fullPage: true,
      maxDiffPixels: 100,
      threshold: 0.2,
    });
  });

  test('login page matches baseline screenshot', async ({ page }) => {
    // Navigate explicitly to the login page (not authenticated for this capture)
    await page.goto('/login');

    // Explicit screenshot name ensures consistent baseline file naming across runs
    await expect(page).toHaveScreenshot('login-page.png', {
      fullPage: true,
      maxDiffPixels: 100,
      threshold: 0.2,
    });
  });
});
