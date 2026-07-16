# Playwright Study Guide

## 1. Foundations

### Introduction
**Playwright** is a modern, cross-browser automation framework designed for end-to-end (E2E) testing. Unlike traditional tools, Playwright is built for the modern web, offering native support for Shadow DOM, iframes, and multiple browser contexts.

**Key Advantages:**
* **Cross-Browser/Platform:** Test on Chromium, WebKit, and Firefox across Windows, Linux, and macOS.
* **Full Isolation:** Each test runs in a fresh **Browser Context** (similar to an Incognito window), ensuring no state leakage between tests.
* **Auto-Waiting:** Built-in mechanisms wait for elements to be actionable, drastically reducing "flaky" tests.
* **Multi-Language:** Supports TypeScript, JavaScript, Python, .NET, and Java.

### Installation & Environment Setup
Playwright is initialized via a CLI that scaffolds the entire project structure.

**Initialization:**
```bash
npm init playwright@latest
```
**What is generated:**
* `playwright.config.ts`: The central brain of your project (browsers, timeouts, retries).
* `tests/`: The directory for your test files.
* `package.json`: Project dependencies.
* **Browser Binaries:** Playwright downloads its own optimized versions of Chromium, Firefox, and WebKit.

### Browser & Project Management
Playwright uses "Projects" to allow the same test suite to run against different environments (e.g., Desktop Chrome vs. Mobile Safari) simultaneously.

**Configuration Example (`playwright.config.ts`):**
```typescript
import { defineConfig, devices } from '@playwright/test';

export default defineConfig({
  projects: [
    { name: 'chromium', use: { ...devices['Desktop Chrome'] } },
    { name: 'firefox', use: { ...devices['Desktop Firefox'] } },
    { name: 'mobile-safari', use: { ...devices['iPhone 12'] } },
    { 
      name: 'local-chrome', 
      use: { executablePath: '/path/to/chrome' } // Use a local install instead of binary
    },
  ],
});
```

---

## 2. The Execution Engine

### Locators (Element Selection)
Locators are the central piece of Playwright's API. They represent a way to find elements on the page. Playwright strongly recommends **User-Facing Locators** (locating things how a human would) over brittle CSS/XPath.

**Recommended Locator Hierarchy:**
| Locator Method | Usage | Example |
| :--- | :--- | :--- |
| `getByRole` | Finds by ARIA role (button, heading, etc.) | `page.getByRole('button', { name: 'Submit' })` |
| `getByLabel` | Finds form elements by their <label> | `page.getByLabel('Email Address')` |
| `getByPlaceholder` | Finds input by placeholder text | `page.getByPlaceholder('Search...')` |
| `getByText` | Finds elements by visible text | `page.getByText('Login Successful')` |
| `getByTestId` | Finds by `data-testid` attribute | `page.getByTestId('submit-button')` |

**Advanced Selection (Chaining & Filtering):**
```javascript
// Chaining: Drill down into a specific section
const sidebar = page.locator('#sidebar');
await sidebar.getByRole('link', { name: 'Settings' }).click();

// Filtering: Find a row in a table that contains specific text
await page.locator('tr').filter({ hasText: 'John Doe' }).getByRole('button', { name: 'Edit' }).click();

// Position-based: Pick the 3rd item in a list
await page.locator('li').nth(2).click();
```

### Element Interactions (Actions)
Once a locator is defined, you perform actions. Most actions automatically wait for the element to be visible and stable.

```javascript
await page.getByRole('button').click();           // Click
await page.getByLabel('Username').fill('admin');  // Type/Fill
await page.getByRole('checkbox').check();         // Check
await page.getByRole('listbox').selectOption('US'); // Select from dropdown
await page.getByText('Hover Me').hover();         // Hover
await page.locator('#item').dragTo(page.locator('#target')); // Drag and Drop
await page.keyboard.press('Enter');               // Keyboard shortcut
```

### Web-First Assertions
Playwright's `expect` is "asynchronous and retrying." If an assertion fails, Playwright will re-check the condition multiple times until it passes or the timeout is reached.

```javascript
// Visibility & State
await expect(page.getByText('Success')).toBeVisible();
await expect(page.getByRole('button')).toBeEnabled();
await expect(page.getByRole('checkbox')).toBeChecked();

// Text & Attributes
await expect(page.getByRole('heading')).toHaveText('Dashboard');
await expect(page.getByLabel('Email')).toHaveValue('test@test.com');
await expect(page).toHaveURL(/.*\/dashboard/);

// Collection Assertions
await expect(page.locator('.list-item')).toHaveCount(5);
```

---

## 3. Advanced Capabilities

### API Testing (`APIRequestContext`)
You can use the `request` fixture to interact with your backend directly. This is ideal for "seeding" data (creating a user via API) before performing a UI test.

```javascript
test('verify user via API', async ({ request }) => {
  const response = await request.get('/api/users/1');
  expect(response.ok()).toBeTruthy();
  
  const body = await response.json();
  expect(body.name).toBe('John Doe');
});
```

### Network Interception & Mocking
Playwright allows you to intercept network traffic to mock API responses, allowing you to test "edge cases" (like 500 errors) without breaking the real backend.

**Mocking a JSON Response:**
```javascript
await page.route('**/api/data', async (route) => {
  await route.fulfill({
    status: 200,
    contentType: 'application/json',
    body: JSON.stringify({ message: 'Mocked Data' }),
  });
});

await page.goto('/dashboard');
await expect(page.getByText('Mocked Data')).toBeVisible();
```

### Data-Driven Testing
Using standard JavaScript logic, you can iterate over datasets to run the same test with different inputs.

```javascript
const users = [{u: 'admin', p: '123'}, {u: 'guest', p: '456'}];

for (const user of users) {
  test(`Login test for ${user.u}`, async ({ page }) => {
    await page.getByLabel('User').fill(user.u);
    await page.getByLabel('Pass').fill(user.p);
    await page.getByRole('button').click();
  });
}
```

---

## 4. Stability & Debugging

### Test Isolation & Contexts
Playwright provides **Browser Contexts**. A context is an isolated environment within a single browser instance. This is much faster than launching a new browser for every test but provides the same level of isolation as a fresh profile.

### Debugging Tools
1.  **Trace Viewer:** The most powerful tool. It captures a "recording" of the test, including DOM snapshots, network logs, and console output.
    *   *Best Practice:* Enable traces only on failure in `playwright.config.ts`:
    ```typescript
    use: { trace: 'on-first-retry' }
    ```
2.  **Codegen:** An interactive recorder that generates code as you click.
    ```bash
    npx playwright codegen https://example.com
    ```
    *   *Warning:* Use Codegen for scaffolding, but manually refactor the locators to use `getByRole` or `getByTestId` for long-term stability.

---

## 5. Architectural Patterns

### Custom Fixtures
Fixtures are the primary way to handle setup/teardown and dependency injection in Playwright. They allow you to create reusable "objects" that are injected directly into your tests.

**Example: Creating an Authenticated Fixture**
```javascript
// my-fixtures.ts
import { test as base } from '@playwright/test';

export const test = base.extend({
  // This fixture performs login once and provides the 'adminPage'
  adminPage: async ({ browser }, use) => {
    const context = await browser.newContext();
    const page = await context.newPage();
    await page.goto('/login');
    await page.fill('#user', 'admin');
    await page.fill('#pass', 'secret');
    await page.click('#submit');
    
    await use(page); // The test runs here
    
    await context.close(); // Cleanup happens after 'use'
  },
});

// usage in a test
import { test } from './my-fixtures';

test('admin can delete user', async ({ adminPage }) => {
  await adminPage.goto('/admin');
  await adminPage.getByRole('button', { name: 'Delete' }).click();
});
```

---

### 6. Advanced Network & API Orchestration

#### Advanced API Handling
Professional testing requires manipulating the "envelope" of the request and deeply inspecting the "payload."

**Request Headers & Authorization:**
In many applications, you must pass a Bearer token or specific content types to interact with the API.
```javascript
const response = await request.get('/api/v1/protected-data', {
  headers: {
    'Authorization': `Bearer ${token}`,
    'Accept': 'application/json',
    'X-Custom-Header': 'onboarding-week'
  }
});
```

**Parsing Complex Response Bodies:**
When dealing with deeply nested JSON, use destructuring or specific property assertions to ensure the data structure is correct.
```javascript
const body = await response.json();
// Asserting nested properties
expect(body.user.permissions.roles).toContain('admin');
expect(body.metadata.timestamp).toBeDefined();
```

#### Advanced Routing: Conditional Mocking
Conditional Mocking allows you to simulate different server behaviors (Edge Cases) within the same test suite by using logic inside the `page.route` handler.

**Example: Mocking different responses based on request content**
```javascript
await page.route('**/api/user/**', async (route) => {
  const url = route.request().url();

  if (url.includes('admin')) {
    // Simulate a successful Admin response
    await route.fulfill({
      status: 200,
      body: JSON.stringify({ role: 'admin', access: 'full' }),
    });
  } else if (url.includes('guest')) {
    // Simulate a Forbidden error for guests
    await route.fulfill({
      status: 403,
      body: JSON.stringify({ error: 'Access Denied' }),
    });
  } else {
    // Fallback: Let the real network request proceed for everything else
    await route.continue();
  }
});
```

#### Reusable Authentication (Storage State)
To avoid the "Login Tax" (logging in via UI before every single test), Playwright allows you to save your authentication state (cookies and local storage) to a file and reuse it.

**Step 1: Create a Setup Test (auth.setup.ts)**
```javascript
test('authenticate', async ({ page }) => {
  await page.goto('/login');
  await page.getByLabel('User').fill('admin');
  await page.getByLabel('Pass').fill('secret');
  await page.getByRole('button').click();
  
  // Wait for navigation/storage to be set
  await page.waitForURL('/dashboard');
  
  // Save the storage state to a file
  await page.context().storageState({ path: 'playwright/.auth/user.json' });
});
```

**Step 2: Reference state in `playwright.config.ts`**
```typescript
export default defineConfig({
  projects: [
    { name: 'setup', testMatch: /.*\.setup\.ts/ }, 
    {
      name: 'chromium',
      use: { 
        ...devices['Desktop Chrome'],
        storageState: 'playwright/.auth/user.json', 
      },
      dependencies: ['setup'], 
    },
  ],
});
```

---

### 7. Observability & Visual Testing

#### Advanced Debugging with Trace Viewer
The Trace Viewer provides a post-mortem forensic analysis of test execution:
* **Actionability Logs:** Inspect why Playwright waited (e.g., "waiting for element to be visible" or "element is intercepted by a loading spinner").
* **Network Tab:** Inspect every API call made during a specific step to identify backend failures.
* **Console Tab:** Check for JavaScript errors thrown by the application during interactions.

#### Visual Regression Testing
Visual testing ensures the UI "looks" correct by comparing screenshots against a baseline.

**Implementation:**
```javascript
test('homepage visual check', async ({ page }) => {
  await page.goto('/');
  // Fails if the page differs from the stored "golden" image
  await expect(page).toHaveScreenshot('home-page.png', {
    maxDiffPixels: 100, 
    threshold: 0.2      
  });
});
```

#### Media Capture (Automated Evidence)
Configure automatic evidence collection in `playwright.config.ts` for CI/CD reporting:

```typescript
use: {
  // Record video for every test (or only on failure)
  video: 'on-first-retry', 
  
  // Take a screenshot automatically when a test fails
  screenshot: 'only-on-failure',
  
  // Capture traces for deep debugging
  trace: 'retain-on-failure',
}
```