import { test, expect } from '@playwright/test';

/**
 * Data-Driven Testing (Requirement 7)
 *
 * Demonstrates parameterized testing using a typed dataset and for...of iteration.
 * Each entry in the dataset generates its own test, keeping test logic DRY while
 * covering multiple scenarios (valid login, wrong password, wrong username, empty fields).
 *
 * Validates: Requirements 7.1, 7.2, 7.3, 7.4, 7.5
 */

// Requirement 7.1: Define a typed array of test case objects with input values,
// expected outcomes, and descriptive labels
interface LoginTestCase {
  description: string;
  username: string;
  password: string;
  shouldSucceed: boolean;
  expectedOutcome: string;
}

const loginTestCases: LoginTestCase[] = [
  {
    description: 'valid credentials (dev/dev)',
    username: 'dev',
    password: 'dev',
    shouldSucceed: true,
    expectedOutcome: '/home',
  },
  {
    description: 'wrong password',
    username: 'dev',
    password: 'wrongpassword',
    shouldSucceed: false,
    expectedOutcome: 'login failed, please try again',
  },
  {
    description: 'wrong username',
    username: 'invaliduser',
    password: 'dev',
    shouldSucceed: false,
    expectedOutcome: 'login failed, please try again',
  },
  {
    description: 'empty credentials',
    username: '',
    password: '',
    shouldSucceed: false,
    expectedOutcome: 'login failed, please try again',
  },
];

// Override project-level storageState so each test starts unauthenticated
test.use({ storageState: { cookies: [], origins: [] } });

test.describe('Data-Driven Login Tests', () => {

  // Requirement 7.2: Iterate using for...of loop to generate one test per entry
  for (const testCase of loginTestCases) {

    // Requirement 7.3: Use template literal strings in test names
    test(`login - ${testCase.description}`, async ({ page }) => {
      await page.goto('/login');

      // Fill the login form with the test case data
      await page.locator('[data-cy="username-input"]').fill(testCase.username);
      await page.locator('[data-cy="password-input"]').fill(testCase.password);
      await page.locator('[data-cy="login-button"]').click();

      if (testCase.shouldSucceed) {
        // Requirement 7.4: Assert successful outcome — URL changes to expected path
        await expect(page).toHaveURL(new RegExp(testCase.expectedOutcome));
      } else {
        // Requirement 7.5: Assert failure outcome — error message is visible
        const errorMessage = page.locator('[data-cy="error-message"]');
        await expect(errorMessage).toBeVisible();
        await expect(errorMessage).toHaveText(testCase.expectedOutcome);
      }
    });
  }
});
