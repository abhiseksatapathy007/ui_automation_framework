package com.automation.framework.Tests;

import org.aeonbits.owner.ConfigFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.automation.framework.Helper.Wrapperdriver;
import com.automation.framework.PageObjects.HomePage;
import com.automation.framework.Utility.BaseTest;
import com.automation.framework.Utility.Environment;

public class ExampleTest extends BaseTest {
	Environment env = ConfigFactory.create(Environment.class);

	@Test(description = "Example test to verify framework setup")
	public void testHomePageNavigation() throws Exception {
		extenttest.set(test);
		test = extent.createTest("Example Test - Home Page Navigation");
		extenttest.set(test);

		// Navigate to the application
		driver.get(env.url());
		wd = new Wrapperdriver(driver);

		// Verify page title
		String pageTitle = driver.getTitle();
		Assert.assertTrue(pageTitle.contains("The Internet"), "Page title should contain 'The Internet'");

		// Create page object and interact
		HomePage homePage = new HomePage(driver);
		homePage.clickFormAuthentication();

		// Verify navigation
		String currentUrl = driver.getCurrentUrl();
		Assert.assertTrue(currentUrl.contains("login"), "Should navigate to login page");

		test.info("Successfully navigated to Form Authentication page");
	}

	@Test(description = "Example test to verify checkboxes functionality")
	public void testCheckboxes() throws Exception {
		extenttest.set(test);
		test = extent.createTest("Example Test - Checkboxes");
		extenttest.set(test);

		// Navigate to the application
		driver.get(env.url());
		wd = new Wrapperdriver(driver);

		// Navigate to checkboxes page
		HomePage homePage = new HomePage(driver);
		homePage.clickCheckboxes();

		// Verify we're on the checkboxes page
		String currentUrl = driver.getCurrentUrl();
		Assert.assertTrue(currentUrl.contains("checkboxes"), "Should navigate to checkboxes page");

		test.info("Successfully navigated to Checkboxes page");
	}
}

