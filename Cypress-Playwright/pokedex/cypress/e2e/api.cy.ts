describe('PokéAPI Direct Testing', () => {
  it('should return status 200 and expected body properties for a valid Pokemon', () => {
    cy.request('GET', 'https://pokeapi.co/api/v2/pokemon/pikachu').then((response) => {
      expect(response.status).to.eq(200);
      expect(response.body).to.have.property('name');
      expect(response.body).to.have.property('sprites');
      expect(response.body).to.have.property('types');
      expect(response.body).to.have.property('moves');
    });
  });

  it('should demonstrate assertion chaining with .its() and .should()', () => {
    cy.request('GET', 'https://pokeapi.co/api/v2/pokemon/pikachu')
      .its('body.name')
      .should('eq', 'pikachu');
  });

  it('should return status 404 for a non-existent Pokemon', () => {
    cy.request({
      url: 'https://pokeapi.co/api/v2/pokemon/invalidname',
      failOnStatusCode: false,
    }).then((response) => {
      expect(response.status).to.eq(404);
    });
  });
});
