describe('Login Page', () => {
  beforeEach(() => {
    cy.visit('/login');
  });

  it('should login successfully with valid credentials', () => {
    cy.get('[data-cy=username-input]').type('dev');
    cy.get('[data-cy=password-input]').type('dev');
    cy.get('[data-cy=login-button]').click();

    cy.url().should('include', '/home');
  });

  it('should display error message with invalid credentials', () => {
    cy.get('[data-cy=username-input]').type('invaliduser');
    cy.get('[data-cy=password-input]').type('wrongpass');
    cy.get('[data-cy=login-button]').click();

    cy.get('[data-cy=error-message]')
      .should('be.visible')
      .should('have.text', 'login failed, please try again');
  });

  // --- Debugging Examples ---
  // Use .debug() to inspect the yielded subject in the DevTools console
  // cy.get('[data-cy=username-input]').debug();

  // Use cy.pause() to step through commands one at a time during interactive mode
  // cy.pause();

  // Use debugger inside a .then() callback to set a breakpoint when DevTools is open
  // cy.get('[data-cy=login-button]').then(($btn) => {
  //   debugger;
  // });
});
