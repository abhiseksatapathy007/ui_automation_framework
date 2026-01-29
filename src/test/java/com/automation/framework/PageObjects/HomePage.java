package com.automation.framework.PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.automation.framework.Helper.Wrapperdriver;
import com.automation.framework.Utility.JSWaiter;

public class HomePage {
	public WebDriver driver = null;
	Wrapperdriver wd;
	JSWaiter js = new JSWaiter();

	// Constructor
	public HomePage(WebDriver driver) {
		this.driver = driver;
		wd = new Wrapperdriver(driver);
		JSWaiter.setDriver(driver);
	}

	// WebElements for the-internet.herokuapp.com home page
	public By linkText = By.linkText("Form Authentication");
	public By addRemoveElements = By.linkText("Add/Remove Elements");
	public By checkboxes = By.linkText("Checkboxes");
	public By dropdown = By.linkText("Dropdown");

	public void clickFormAuthentication() {
		wd.waitForElementToBePresent(linkText, 10);
		wd.clickOnWebElement(linkText);
		js.waitAllRequest();
	}

	public void clickAddRemoveElements() {
		wd.waitForElementToBePresent(addRemoveElements, 10);
		wd.clickOnWebElement(addRemoveElements);
		js.waitAllRequest();
	}

	public void clickCheckboxes() {
		wd.waitForElementToBePresent(checkboxes, 10);
		wd.clickOnWebElement(checkboxes);
		js.waitAllRequest();
	}

	public void clickDropdown() {
		wd.waitForElementToBePresent(dropdown, 10);
		wd.clickOnWebElement(dropdown);
		js.waitAllRequest();
	}
}

