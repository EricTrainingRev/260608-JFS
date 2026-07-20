/**
 * Visual Regression Tests
 *
 * Study Guide Section: Advanced Testing & Collaboration
 *
 * Demonstrates:
 *   - @simonsmith/cypress-image-snapshot plugin for screenshot comparison
 *   - cy.matchImageSnapshot() to capture and compare baselines
 *   - Waiting for UI stability before capturing screenshots
 *
 * How it works:
 *   1. First run: captures a baseline screenshot and saves it to cypress/snapshots/
 *   2. Subsequent runs: takes a new screenshot and compares pixel-by-pixel against the baseline
 *   3. If the diff exceeds the threshold, the test FAILS with a visual diff image
 *
 * Important:
 *   - Baseline images (cypress/snapshots/) should be committed to version control
 *   - Run with --env updateSnapshots=true to regenerate baselines after intentional UI changes
 *   - Screenshots may differ across OS/browser — generate baselines in the same environment you test in
 */
describe('Visual Regression Tests', () => {
  it('should capture a login page baseline snapshot', () => {
    cy.visit('/login');

    // Wait for the form to be fully rendered before capturing.
    // Without this, the screenshot might capture a loading state.
    cy.get('form').should('be.visible');

    // The string argument is the snapshot name — it determines the filename
    // in cypress/snapshots/. Use descriptive names to identify what was captured.
    cy.matchImageSnapshot('login-page');
  });

  it('should capture a Pokemon search results baseline snapshot', () => {
    // Authenticate and navigate to the search page
    cy.visit('/login');
    cy.login('dev', 'dev');
    cy.url().should('include', '/home');

    // Perform a search to get data on screen
    cy.get('[data-cy=search-input]').type('pikachu');
    cy.get('[data-cy=search-button]').click();

    // Wait for the async API response to render before snapshotting.
    // Both the name and sprite must be visible to ensure the page is stable.
    cy.get('.pokemon-name').should('be.visible');
    cy.get('.sprite-img').first().should('be.visible');

    cy.matchImageSnapshot('pokemon-search-results');
  });
});
