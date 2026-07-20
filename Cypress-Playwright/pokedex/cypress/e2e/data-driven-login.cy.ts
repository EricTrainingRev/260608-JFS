/**
 * Data-Driven Login Tests
 *
 * Study Guide Section: Advanced Capabilities
 *
 * Demonstrates:
 *   - Data-driven testing pattern using JavaScript arrays
 *   - Dynamic it() block generation with forEach
 *   - Parameterized test cases with conditional assertions
 *   - TypeScript interfaces for test data structure
 *
 * Instead of writing separate it() blocks for each credential combination,
 * we define the test data in an array and let forEach generate the tests.
 * This makes it easy to add new cases without duplicating test logic.
 */

// TypeScript interface defines the shape of each test case object.
// This provides autocompletion and catches typos at compile time.
interface LoginTestCase {
  username: string;
  password: string;
  shouldSucceed: boolean;
  description: string;
}

// Test data array — each entry produces one it() block at runtime.
// At minimum: one valid case and two invalid cases (wrong username, wrong password).
const loginTestCases: LoginTestCase[] = [
  {
    username: 'dev',
    password: 'dev',
    shouldSucceed: true,
    description: 'valid credentials (dev/dev)',
  },
  {
    username: 'wronguser',
    password: 'dev',
    shouldSucceed: false,
    description: 'invalid username with correct password',
  },
  {
    username: 'dev',
    password: 'wrongpass',
    shouldSucceed: false,
    description: 'valid username with incorrect password',
  },
];

describe('Data-Driven Login Tests', () => {
  // beforeEach ensures every generated test starts on the login page
  beforeEach(() => {
    cy.visit('/login');
  });

  // forEach iterates the array and creates a separate it() block per entry.
  // Each test appears independently in the Cypress runner with its own pass/fail status.
  loginTestCases.forEach((testCase) => {
    // Template literal in the test name makes each case identifiable in results
    it(`should handle login with ${testCase.description}`, () => {
      cy.get('[data-cy=username-input]').type(testCase.username);
      cy.get('[data-cy=password-input]').type(testCase.password);
      cy.get('[data-cy=login-button]').click();

      // Conditional assertion based on the expected outcome
      if (testCase.shouldSucceed) {
        cy.url().should('include', '/home');
      } else {
        cy.get('[data-cy=error-message]')
          .should('be.visible')
          .should('contain', 'login failed, please try again');
      }
    });
  });
});
