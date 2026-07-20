/**
 * Route Guard Tests
 *
 * Study Guide Section: Architectural Patterns & Ecosystem
 *
 * Demonstrates:
 *   - Testing Angular route guards (auth protection)
 *   - cy.window() to access browser APIs (sessionStorage)
 *   - Simulating authentication state without going through the UI
 *   - URL assertion with timeout for async redirects
 *   - Test isolation via beforeEach cleanup
 *
 * The Angular app uses an AuthGuard that checks sessionStorage for an
 * 'authenticated' key. If missing, it redirects to /login.
 */
describe('Route Guard', () => {
  // Clear sessionStorage before each test to ensure complete isolation.
  // Without this, a passing authentication from a previous test could
  // leak into the next one, causing false positives.
  beforeEach(() => {
    cy.window().then((win) => {
      win.sessionStorage.clear();
    });
  });

  it('should redirect unauthenticated user to /login', () => {
    // Visit the protected route directly without authenticating
    cy.visit('/home');

    // The AuthGuard should redirect us to /login.
    // The { timeout: 4000 } gives Angular time to process the guard
    // and perform the redirect (default Cypress timeout is 4s anyway,
    // but being explicit communicates intent).
    cy.url().should('include', '/login', { timeout: 4000 });
  });

  it('should allow authenticated user to access /home', () => {
    // cy.window() gives access to the browser's window object.
    // We set sessionStorage directly to simulate a logged-in user
    // WITHOUT going through the login UI — this is faster and tests
    // the guard in isolation from the login flow.
    cy.window().then((win) => {
      win.sessionStorage.setItem('authenticated', 'true');
    });

    cy.visit('/home');

    // Verify we stayed on /home (no redirect happened)
    cy.url().should('include', '/home');

    // Verify the search component rendered — confirms the page loaded fully
    cy.get('app-search').should('be.visible');
  });
});
