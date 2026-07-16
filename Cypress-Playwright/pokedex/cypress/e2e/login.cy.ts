/**
 * Login Page E2E Tests
 *
 * Study Guide Section: Core Execution Engine
 *
 * Demonstrates:
 *   - describe() / it() blocks for test organization
 *   - beforeEach() hook for shared setup
 *   - cy.visit() for navigation
 *   - cy.get() with data-cy selectors for element selection
 *   - .type() and .click() for user interactions
 *   - .should() for assertions (URL, visibility, text content)
 *   - Debugging tools (.debug, cy.pause, debugger)
 */
describe('Login Page', () => {
  // beforeEach runs before EVERY it() block in this describe.
  // This ensures each test starts from a clean state on the login page.
  beforeEach(() => {
    cy.visit('/login');
  });

  it('should login successfully with valid credentials', () => {
    // .type() simulates keyboard input into the focused element
    cy.get('[data-cy=username-input]').type('dev');
    cy.get('[data-cy=password-input]').type('dev');

    // .click() simulates a mouse click — triggers the form submission
    cy.get('[data-cy=login-button]').click();

    // cy.url() yields the current URL as a string.
    // .should('include', '/home') asserts the URL contains '/home',
    // confirming the redirect after successful login.
    cy.url().should('include', '/home');
  });

  it('should display error message with invalid credentials', () => {
    cy.get('[data-cy=username-input]').type('invaliduser');
    cy.get('[data-cy=password-input]').type('wrongpass');
    cy.get('[data-cy=login-button]').click();

    // Chaining multiple .should() assertions on the same element:
    // - 'be.visible' confirms the element is rendered and not hidden
    // - 'have.text' checks the exact text content of the element
    cy.get('[data-cy=error-message]')
      .should('be.visible')
      .should('have.text', 'login failed, please try again');
  });

  // ═══════════════════════════════════════════════════════════════════════
  // DEBUGGING EXAMPLES (commented out so they don't block CI runs)
  // ═══════════════════════════════════════════════════════════════════════

  // .debug() — Pauses execution and logs the yielded subject to the
  // browser DevTools console. Useful for inspecting what Cypress found
  // when a selector isn't behaving as expected.
  // cy.get('[data-cy=username-input]').debug();

  // cy.pause() — Stops the test runner and lets you step through
  // subsequent commands one at a time using the Cypress UI controls.
  // Only works in interactive mode (cy:open), not headless.
  // cy.pause();

  // debugger — Standard JS breakpoint. When DevTools is open, execution
  // halts here and you can inspect variables in the Sources panel.
  // Must be inside a .then() because Cypress commands are asynchronous.
  // cy.get('[data-cy=login-button]').then(($btn) => {
  //   debugger;
  // });
});
