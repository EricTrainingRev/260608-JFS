describe('Example recorded tests', () => {

  const VALID_USERNAME = 'dev';
  const VALID_PASSWORD = 'dev';

  /* ==== Test Created with Cypress Studio ==== */
  it('recorded login test positive', function() {
    /* ==== Generated with Cypress Studio ==== */
    cy.visit('http://localhost:4200');
    cy.get('[data-cy="username-input"]').clear();
    cy.get('[data-cy="username-input"]').type(VALID_USERNAME);
    cy.get('[data-cy="password-input"]').clear();
    cy.get('[data-cy="password-input"]').type(VALID_PASSWORD);
    cy.get('[data-cy="login-button"]').click();
    cy.get('.pokedex-instructions').should('be.visible');
    /* ==== End Cypress Studio ==== */
  });

  /* ==== Test Created with Cypress Studio ==== */
  it('recorded login test negative', function() {
    /* ==== Generated with Cypress Studio ==== */
    cy.visit('http://localhost:4200');
    cy.get('[data-cy="username-input"]').clear();
    cy.get('[data-cy="username-input"]').type('Gary');
    cy.get('[data-cy="password-input"]').clear();
    cy.get('[data-cy="password-input"]').type('I am the best!');
    cy.get('[data-cy="login-button"]').click();
    cy.get('[data-cy="error-message"]').should('be.visible');
    cy.get('[data-cy="error-message"]').should('have.text', 'login failed, please try again');
    cy.get('[data-cy="error-message"]').should('have.class', 'error-message');
    /* ==== End Cypress Studio ==== */
  });
})

