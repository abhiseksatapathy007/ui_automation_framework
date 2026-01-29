package com.automation.framework.Helper;

import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Webdriverbase {

	public FirefoxProfile Ffp;
	static Properties prop = new Properties();
	public static String downloadFilepath = "./Downloads";
	public static String remote_url = "http://localhost:4444";
	public WebDriver Driver;

	@SuppressWarnings("deprecation")
	@BeforeTest
	@Parameters("browser")
	public WebDriver StartWebDriver(String browser) throws Exception {

		switch (browser.toLowerCase()) {

		case "firefox":
			FirefoxOptions ffOptions = new FirefoxOptions();
			ffOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
			Driver = WebDriverManager.firefoxdriver().capabilities(ffOptions).create();
			break;

		case "edge":
			EdgeOptions option = new EdgeOptions();
			option.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
			Driver = WebDriverManager.edgedriver().capabilities(option).create();
			break;

		case "chrome":
			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("profile.default_content_settings.popups", 0);
			chromePrefs.put("download.default_directory", downloadFilepath);
			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("prefs", chromePrefs);
			options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
			options.addArguments("--remote-allow-origins=*");
			options.addArguments("disable-infobars"); // Disables "Chrome is being controlled"
			options.addArguments("--disable-automation");
			// options.addArguments("--headless=new"); // Headless mode disabled
			options.addArguments("--start-maximized");
			Driver = WebDriverManager.chromedriver().capabilities(options).create();
			// Driver.manage().wait(5000);
			break;

		case "remote-chrome":
			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
			URL url = new URL(remote_url);
			Driver = new RemoteWebDriver(url, chromeOptions);
			break;

		case "remote-firefox":
			FirefoxOptions firefoxOptions = new FirefoxOptions();
			firefoxOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
			URL ffurl = new URL(remote_url);
			Driver = new RemoteWebDriver(ffurl, firefoxOptions);
			break;

		case "chrome-beta":
			HashMap<String, Object> chromeBetaPrefs = new HashMap<String, Object>();
			chromeBetaPrefs.put("profile.default_content_settings.popups", 0);
			chromeBetaPrefs.put("download.default_directory", downloadFilepath);
			ChromeOptions chromeBetaOptions = new ChromeOptions();
			chromeBetaOptions.setExperimentalOption("prefs", chromeBetaPrefs);
			chromeBetaOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
			chromeBetaOptions.addArguments("--remote-allow-origins=*");
			chromeBetaOptions.setBinary("C:\\Path\\To\\Chrome\\Beta\\chrome.exe"); // Adjust this to the path of your
																					// Chrome Beta binary
			// chromeBetaOptions.addArguments("--headless");
			Driver = WebDriverManager.chromedriver().capabilities(chromeBetaOptions).create();
			// Driver.manage().wait(5000);
			break;

		}
		Driver.manage().deleteAllCookies();
		Driver.manage().window().maximize();
		Driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		return Driver;
	}

}