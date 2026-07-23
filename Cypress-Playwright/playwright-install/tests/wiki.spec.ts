import { test, expect } from '@playwright/test';

test('test', async ({ page }) => {
  await page.goto('https://www.wikipedia.org/');
  await page.getByText('Wikipedia The Free Encyclopedia English 7,189,000+ articles 日本語 1,503,000+ 記事').click({
    button: 'middle'
  });
  await page.getByRole('searchbox', { name: 'Search Wikipedia' }).click();
  await page.getByRole('searchbox', { name: 'Search Wikipedia' }).fill('puppy');
  await page.getByRole('link', { name: 'Puppy Juvenile dog' }).click();
  await expect(page.locator('#firstHeading').getByText('Puppy')).toBeVisible();
  await expect(page.locator('#firstHeading')).toContainText('Puppy');
});