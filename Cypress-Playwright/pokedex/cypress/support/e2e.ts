// cypress/support/e2e.ts
import './commands';
import 'cypress-axe';
import { addMatchImageSnapshotCommand } from '@simonsmith/cypress-image-snapshot/command';

addMatchImageSnapshotCommand();
