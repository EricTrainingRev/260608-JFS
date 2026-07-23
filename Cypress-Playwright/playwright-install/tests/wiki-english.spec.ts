import  {test,expect} from "@playwright/test";

test('User can click the English link to visit the English version of Wikipedia', async ({page}) => {
    await page.goto('https://www.wikipedia.org/');
    await page.getByRole('link', { name: 'English 7,189,000+ articles' }).click();
    await expect(page.locator('#Welcome_to_Wikipedia')).toContainText('Welcome to Wikipedia');
})