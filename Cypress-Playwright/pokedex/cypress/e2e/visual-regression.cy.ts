describe('Visual Regression Tests', () => {
  it('should capture a login page baseline snapshot', () => {
    cy.visit('/login');

    // Wait for the login form to be visible before capturing snapshot
    cy.get('form').should('be.visible');

    cy.matchImageSnapshot('login-page');
  });

  it('should capture a Pokemon search results baseline snapshot', () => {
    cy.login('dev', 'dev');
    cy.visit('/home');

    // Search for a known Pokemon
    cy.get('[data-cy=search-input]').type('pikachu');
    cy.get('[data-cy=search-button]').click();

    // Wait for Pokemon data to be visible (name and sprite)
    cy.get('.pokemon-name').should('be.visible');
    cy.get('.sprite-img').first().should('be.visible');

    cy.matchImageSnapshot('pokemon-search-results');
  });
});
