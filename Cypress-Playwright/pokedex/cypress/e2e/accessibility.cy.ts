/**
 * Accessibility (A11y) Audit Tests
 *
 * Study Guide Section: Advanced Testing & Collaboration
 *
 * Demonstrates:
 *   - cypress-axe plugin integration for automated accessibility testing
 *   - cy.injectAxe() to load the axe-core engine into the page
 *   - cy.checkA11y() to run a full WCAG audit
 *   - Scoped auditing with element selectors and rule configuration
 *
 * How it works:
 *   1. cy.injectAxe() injects the axe-core JavaScript library into the current page
 *   2. cy.checkA11y() runs the axe audit against the DOM
 *   3. If any WCAG violations are found, the test FAILS with detailed violation info
 *
 * Note: cy.injectAxe() must be called AFTER cy.visit() or any navigation,
 * because navigating to a new page clears the injected script.
 */
describe('Accessibility Audits', () => {
  it('should have no accessibility violations on the login page', () => {
    cy.visit('/login');

    // Inject axe-core into the page — must happen after the page loads
    cy.injectAxe();

    // Run a full accessibility audit. If violations exist, this fails the test
    // and prints detailed info about which elements violate which WCAG rules.
    cy.checkA11y();
  });

  it('should have no accessibility violations on the home page', () => {
    // Authenticate first to access the protected home page
    cy.visit('/login');
    cy.login('dev', 'dev');
    cy.url().should('include', '/home');

    // Must inject axe AFTER navigation — the script doesn't persist across pages
    cy.injectAxe();
    cy.checkA11y();
  });

  it('should have no accessibility violations within the login form region', () => {
    cy.visit('/login');
    cy.injectAxe();

    // Scoped audit — cy.checkA11y() accepts two optional arguments:
    //   1st arg: CSS selector to scope the audit (only checks this region)
    //   2nd arg: axe-core options object to configure rules
    //
    // Here we audit only the <form> element and disable the color-contrast
    // rule as an example of excluding known/acceptable violations.
    cy.checkA11y('form', {
      rules: {
        'color-contrast': { enabled: false },
      },
    });
  });
});
