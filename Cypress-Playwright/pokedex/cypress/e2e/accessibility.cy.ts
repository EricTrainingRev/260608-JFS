describe('Accessibility Audits', () => {
  it('should have no accessibility violations on the login page', () => {
    cy.visit('/login');
    cy.injectAxe();
    cy.checkA11y();
  });

  it('should have no accessibility violations on the home page', () => {
    cy.visit('/login');
    cy.login('dev', 'dev');
    cy.url().should('include', '/home');
    cy.injectAxe();
    cy.checkA11y();
  });

  it('should have no accessibility violations within the login form region', () => {
    cy.visit('/login');
    cy.injectAxe();
    cy.checkA11y('form', {
      rules: {
        'color-contrast': { enabled: false },
      },
    });
  });
});
