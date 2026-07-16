/**
 * Pokemon Search E2E Tests
 *
 * Study Guide Section: Advanced Capabilities
 *
 * Demonstrates:
 *   - Using the cy.login() custom command for authentication
 *   - Testing asynchronous operations (API calls + conditional rendering)
 *   - Cypress automatic retry-and-timeout behavior (NO explicit waits)
 *   - Asserting on dynamically rendered content
 *   - .each() for iterating over multiple elements
 *
 * Key concept: Cypress automatically retries assertions until they pass
 * or the timeout expires (default 4s). This means we don't need sleep()
 * or explicit waits — Cypress handles the async nature of API responses
 * and DOM updates automatically.
 */
describe('Pokemon Search', () => {
  beforeEach(() => {
    // Navigate to login page first, then use our custom command.
    // cy.login() types credentials and clicks the button — it needs
    // to be on the /login page to find the form elements.
    cy.visit('/login');
    cy.login('dev', 'dev');

    // After login, the app redirects to /home. Verify we arrived.
    cy.url().should('include', '/home');
  });

  it('should display the Pokemon name after searching', () => {
    cy.get('[data-cy=search-input]').type('pikachu');
    cy.get('[data-cy=search-button]').click();

    // Cypress retries this assertion until .pokemon-name appears in the DOM
    // with the expected text, or until the 4s timeout expires.
    // No cy.wait() needed — this is Cypress's automatic waiting in action.
    cy.get('.pokemon-name').should('contain.text', 'Pikachu');
  });

  it('should display sprite images with non-empty src attributes', () => {
    cy.get('[data-cy=search-input]').type('pikachu');
    cy.get('[data-cy=search-button]').click();

    // First assert that sprite images exist
    cy.get('.sprite-img').should('have.length.greaterThan', 0);

    // .each() iterates over all matched elements, letting us assert on each one.
    // cy.wrap() is needed to bring a jQuery element back into the Cypress command chain.
    cy.get('.sprite-img').each(($img) => {
      cy.wrap($img).should('have.attr', 'src').and('not.be.empty');
    });
  });

  it('should display at least 1 type element', () => {
    cy.get('[data-cy=search-input]').type('pikachu');
    cy.get('[data-cy=search-button]').click();

    // Verify types rendered (e.g., "Electric" for Pikachu)
    cy.get('.type-item').should('have.length.greaterThan', 0);
    cy.get('.type-item').first().should('not.have.text', '');
  });

  it('should display at least 1 move element', () => {
    cy.get('[data-cy=search-input]').type('pikachu');
    cy.get('[data-cy=search-button]').click();

    // Verify at least one move is rendered in the list
    cy.get('.moves-list li').should('have.length.greaterThan', 0);
  });
});
