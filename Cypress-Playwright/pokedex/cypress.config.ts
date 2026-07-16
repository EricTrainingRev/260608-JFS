/**
 * Cypress Configuration File
 *
 * This is the root-level config that Cypress reads on startup. It controls
 * how tests are discovered, what browser viewport to use, and which plugins
 * to load. The file must be at the project root and use `defineConfig` for
 * type safety and IDE autocompletion.
 *
 * Docs: https://docs.cypress.io/guides/references/configuration
 */
import { defineConfig } from 'cypress';
import { addMatchImageSnapshotPlugin } from '@simonsmith/cypress-image-snapshot/plugin';

export default defineConfig({
  // Default viewport size for all tests — simulates a standard desktop browser
  viewportWidth: 1280,
  viewportHeight: 720,

  e2e: {
    // baseUrl lets you use relative paths in cy.visit('/login') instead of full URLs.
    // The Angular dev server must be running at this address before tests execute.
    baseUrl: 'http://localhost:4200',

    // specPattern tells Cypress where to find test files.
    // The **/*.cy.ts glob recursively matches any .cy.ts file inside cypress/e2e/
    specPattern: 'cypress/e2e/**/*.cy.ts',

    // supportFile is loaded automatically before every spec file.
    // It's where we register custom commands and plugin commands (axe, image snapshot).
    supportFile: 'cypress/support/e2e.ts',

    // setupNodeEvents runs in Node.js (not the browser). This is where we register
    // plugins that need server-side access — like writing snapshot image files to disk.
    setupNodeEvents(on) {
      addMatchImageSnapshotPlugin(on);
    },
  },
});
