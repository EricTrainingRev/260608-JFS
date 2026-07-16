/**
 * PokéAPI Direct Testing
 *
 * Study Guide Section: Advanced Capabilities
 *
 * Demonstrates:
 *   - cy.request() for API testing without browser navigation
 *   - .then() callback for accessing the full response object
 *   - .its() for drilling into nested properties
 *   - .should() assertion chaining
 *   - failOnStatusCode: false for testing error responses
 *
 * cy.request() bypasses the browser entirely — it sends HTTP requests
 * directly from the Cypress Node.js process. This makes it fast and
 * ideal for testing APIs independently of any UI.
 */
describe('PokéAPI Direct Testing', () => {
  it('should return status 200 and expected body properties for a valid Pokemon', () => {
    // cy.request() returns a response object with status, headers, body, etc.
    // .then() gives us access to the full response for multiple assertions.
    cy.request('GET', 'https://pokeapi.co/api/v2/pokemon/pikachu').then((response) => {
      // Chai's expect() syntax for assertions inside .then() callbacks
      expect(response.status).to.eq(200);

      // Verify the response body has the expected shape.
      // .have.property() checks that the key exists (doesn't check the value).
      expect(response.body).to.have.property('name');
      expect(response.body).to.have.property('sprites');
      expect(response.body).to.have.property('types');
      expect(response.body).to.have.property('moves');
    });
  });

  it('should demonstrate assertion chaining with .its() and .should()', () => {
    // .its() drills into a property of the yielded subject without a .then().
    // 'body.name' accesses response.body.name in one step.
    // .should() then asserts on that extracted value.
    // This is more concise than .then() when you only need one assertion.
    cy.request('GET', 'https://pokeapi.co/api/v2/pokemon/pikachu')
      .its('body.name')
      .should('eq', 'pikachu');
  });

  it('should return status 404 for a non-existent Pokemon', () => {
    // By default, cy.request() fails the test on any non-2xx status code.
    // Setting failOnStatusCode: false lets us assert on error responses
    // without Cypress automatically failing the test.
    cy.request({
      url: 'https://pokeapi.co/api/v2/pokemon/invalidname',
      failOnStatusCode: false,
    }).then((response) => {
      expect(response.status).to.eq(404);
    });
  });
});
