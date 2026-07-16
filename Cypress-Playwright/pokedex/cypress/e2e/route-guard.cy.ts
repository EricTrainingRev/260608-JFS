describe('Route Guard', () => {
  beforeEach(() => {
    cy.window().then((win) => {
      win.sessionStorage.clear();
    });
  });

  it('should redirect unauthenticated user to /login', () => {
    cy.visit('/home');
    cy.url().should('include', '/login', { timeout: 4000 });
  });

  it('should allow authenticated user to access /home', () => {
    cy.window().then((win) => {
      win.sessionStorage.setItem('authenticated', 'true');
    });
    cy.visit('/home');
    cy.url().should('include', '/home');
    cy.get('app-search').should('be.visible');
  });
});
