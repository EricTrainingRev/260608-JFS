/**
 * Cypress Support File (E2E)
 *
 * This file runs BEFORE every single test file. It's the place to:
 *   - Import custom commands so they're available globally as cy.commandName()
 *   - Register third-party plugin commands (axe, image snapshot, etc.)
 *   - Add global beforeEach/afterEach hooks if needed
 *
 * Think of this as the "setup" that every test inherits automatically.
 *
 * Docs: https://docs.cypress.io/guides/core-concepts/writing-and-organizing-tests#Support-file
 */

// Import our custom commands (e.g., cy.login)
import './commands';

// Registers cy.injectAxe() and cy.checkA11y() for accessibility testing.
// These commands come from the cypress-axe package which wraps the axe-core engine.
import 'cypress-axe';

// Registers cy.matchImageSnapshot() for visual regression testing.
// The v10 API requires calling addMatchImageSnapshotCommand() as a function.
import { addMatchImageSnapshotCommand } from '@simonsmith/cypress-image-snapshot/command';

addMatchImageSnapshotCommand();
