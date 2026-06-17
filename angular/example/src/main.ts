import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { App } from './app/app';

/*
  Similiar to how index.html is the webpage entry point, this main.ts is
  the programatic entrypoint. Once again, we don't need to change anything
  that is happening here
*/

bootstrapApplication(App, appConfig)
  .catch((err) => console.error(err));
