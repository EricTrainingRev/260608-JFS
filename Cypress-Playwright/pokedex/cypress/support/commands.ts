/**
 * Custom Commands
 *
 * Custom commands let you encapsulate repeated sequences of actions into a
 * single reusable function on the `cy` object. This is an alternative to
 * the Page Object Model pattern — Cypress recommends custom commands because
 * they integrate naturally with the command chain and retry-ability system.
 *
 * Docs: https://docs.cypress.io/api/cypress-api/custom-commands
 */

// TypeScript interface augmentation — this tells the TS compiler that
// cy.login() exists and what parameters it accepts. Without this, you'd
// get "Property 'login' does not exist on type 'cy'" errors.
declare global {
  namespace Cypress {
    interface Chainable {
      /**
       * Custom command to log in via the UI.
       * Must be called while on the /login page.
       * @param username - The username to type into the username field
       * @param password - The password to type into the password field
       */
      login(username: string, password: string): Chainable<void>;
    }
  }
}

// Cypress.Commands.add() registers the command on the cy object.
// After this, any test can call cy.login('dev', 'dev') instead of
// repeating the type/type/click sequence manually.
Cypress.Commands.add('login', (username: string, password: string) => {
  // Using data-cy attributes for element selection — the "Gold Standard"
  // selector strategy. These attributes are immune to CSS class changes
  // and provide a clear contract between test code and app templates.
  cy.get('[data-cy=username-input]').type(username);
  cy.get('[data-cy=password-input]').type(password);
  cy.get('[data-cy=login-button]').click();
});

// Empty export makes this file a TypeScript module, which is required
// for the `declare global` augmentation above to work correctly.
export {};
