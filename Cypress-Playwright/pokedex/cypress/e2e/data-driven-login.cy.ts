interface LoginTestCase {
  username: string;
  password: string;
  shouldSucceed: boolean;
  description: string;
}

const loginTestCases: LoginTestCase[] = [
  {
    username: 'dev',
    password: 'dev',
    shouldSucceed: true,
    description: 'valid credentials (dev/dev)',
  },
  {
    username: 'wronguser',
    password: 'dev',
    shouldSucceed: false,
    description: 'invalid username with correct password',
  },
  {
    username: 'dev',
    password: 'wrongpass',
    shouldSucceed: false,
    description: 'valid username with incorrect password',
  },
];

describe('Data-Driven Login Tests', () => {
  beforeEach(() => {
    cy.visit('/login');
  });

  loginTestCases.forEach((testCase) => {
    it(`should handle login with ${testCase.description}`, () => {
      cy.get('[data-cy=username-input]').type(testCase.username);
      cy.get('[data-cy=password-input]').type(testCase.password);
      cy.get('[data-cy=login-button]').click();

      if (testCase.shouldSucceed) {
        cy.url().should('include', '/home');
      } else {
        cy.get('[data-cy=error-message]')
          .should('be.visible')
          .should('contain', 'login failed, please try again');
      }
    });
  });
});
