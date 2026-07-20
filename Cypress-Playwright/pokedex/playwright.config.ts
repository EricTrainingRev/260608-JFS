import { defineConfig, devices } from '@playwright/test';

/**
 * Playwright Configuration — Pokedex E2E Test Suite
 *
 * This configuration defines browser projects, timeouts, retries, and media
 * capture settings for debugging and CI/CD reporting.
 *
 * ─────────────────────────────────────────────────────────────────────────────
 * TRACE VIEWER — Post-Mortem Debugging
 * ─────────────────────────────────────────────────────────────────────────────
 *
 * Trace files (.zip) capture a full timeline of test execution including:
 *   - DOM Snapshots: inspect the page state at each action step
 *   - Actionability Logs: see WHY Playwright waited for an element (e.g.,
 *     element was not visible, not stable, or obscured by another element)
 *   - Network Tab: all HTTP requests and responses made during the test
 *   - Console Tab: browser console output (logs, warnings, errors)
 *
 * To open a trace file after a test failure:
 *   npx playwright show-trace path/to/trace.zip
 *
 * Example:
 *   npx playwright show-trace playwright-results/my-test-chromium/trace.zip
 *
 * The Trace Viewer opens in the browser and lets you step through each action,
 * inspect the DOM at that moment, view network traffic, and read console logs.
 *
 * ─────────────────────────────────────────────────────────────────────────────
 * CODEGEN — Interactive Test Recorder
 * ─────────────────────────────────────────────────────────────────────────────
 *
 * Codegen launches a browser and records user interactions as Playwright code:
 *   npx playwright codegen http://localhost:4200
 *
 * Usage notes:
 *   - Click through the app and Codegen generates locators + actions in real time
 *   - Copy the generated code into your spec files as a starting point
 *   - IMPORTANT: Codegen-generated locators often use CSS selectors or fragile
 *     paths. Always refactor them to use `getByRole`, `getByLabel`, `getByTestId`,
 *     or other user-facing locator strategies for long-term stability.
 *
 * ─────────────────────────────────────────────────────────────────────────────
 * CAPTURE MODE OPTIONS
 * ─────────────────────────────────────────────────────────────────────────────
 *
 * Each media capture setting (trace, video, screenshot) accepts these modes:
 *
 *   'off'               — Never capture. Fastest execution, no debugging artifacts.
 *   'on'                — Always capture for every test. Produces the most data
 *                         but slows down every run significantly.
 *   'retain-on-failure' — Capture for all tests but only keep artifacts from
 *                         tests that fail. Good balance for CI environments.
 *   'on-first-retry'    — Only capture on the first retry of a failed test.
 *                         Best balance: no overhead on passing tests or first
 *                         attempts, but full debugging artifacts when a test
 *                         fails and retries. Requires `retries >= 1`.
 *
 * For `screenshot` specifically, there is also:
 *   'only-on-failure'   — Capture a screenshot only when a test fails (no retry
 *                         required). Lightweight failure evidence.
 *
 * The settings below use 'on-first-retry' for trace and video (requires the
 * retry to trigger capture) and 'only-on-failure' for screenshots (immediate
 * capture on any failure). This balances debugging capability with test speed.
 */
export default defineConfig({
  testDir: './playwright',
  timeout: 5000,
  retries: process.env.CI ? 1 : 0,
  use: {
    baseURL: 'http://localhost:4200',

    /**
     * Trace capture — records DOM snapshots, network, console, and actionability logs.
     *
     * Mode: 'on-first-retry'
     * Why: Avoids trace overhead on passing tests while ensuring full debugging
     * data is available when a flaky or failing test retries.
     *
     * View traces with: npx playwright show-trace playwright-results/<test>/trace.zip
     */
    trace: 'on-first-retry',

    /**
     * Video capture — records a video of the browser during test execution.
     *
     * Mode: 'on-first-retry'
     * Why: Video files are large; capturing only on retry keeps CI artifacts small
     * while still providing visual playback for debugging failures.
     */
    video: 'on-first-retry',

    /**
     * Screenshot capture — takes a PNG screenshot of the page on failure.
     *
     * Mode: 'only-on-failure'
     * Why: Provides immediate visual evidence of the failure state without
     * requiring a retry. Lightweight compared to video or trace.
     */
    screenshot: 'only-on-failure',
  },
  outputDir: './playwright-results/',
  projects: [
    {
      name: 'setup',
      testMatch: /auth\.setup\.ts/,
    },
    {
      name: 'chromium',
      use: {
        ...devices['Desktop Chrome'],
        storageState: './playwright/.auth/user.json',
      },
      dependencies: ['setup'],
    },
    {
      name: 'edge',
      use: {
        ...devices['Desktop Edge'],
        storageState: './playwright/.auth/user.json',
      },
      dependencies: ['setup'],
    },
    {
      name: 'mobile-chrome',
      use: {
        ...devices['Pixel 7'],
        storageState: './playwright/.auth/user.json',
      },
      dependencies: ['setup'],
    },
  ],
});
