describe('Pokemon Search', () => {
  beforeEach(() => {
    cy.visit('/login');
    cy.login('dev', 'dev');
    cy.url().should('include', '/home');
  });

  it('should display the Pokemon name after searching', () => {
    cy.get('[data-cy=search-input]').type('pikachu');
    cy.get('[data-cy=search-button]').click();

    cy.get('.pokemon-name').should('contain.text', 'Pikachu');
  });

  it('should display sprite images with non-empty src attributes', () => {
    cy.get('[data-cy=search-input]').type('pikachu');
    cy.get('[data-cy=search-button]').click();

    cy.get('.sprite-img').should('have.length.greaterThan', 0);
    cy.get('.sprite-img').each(($img) => {
      cy.wrap($img).should('have.attr', 'src').and('not.be.empty');
    });
  });

  it('should display at least 1 type element', () => {
    cy.get('[data-cy=search-input]').type('pikachu');
    cy.get('[data-cy=search-button]').click();

    cy.get('.type-item').should('have.length.greaterThan', 0);
    cy.get('.type-item').first().should('not.have.text', '');
  });

  it('should display at least 1 move element', () => {
    cy.get('[data-cy=search-input]').type('pikachu');
    cy.get('[data-cy=search-button]').click();

    cy.get('.moves-list li').should('have.length.greaterThan', 0);
  });
});
