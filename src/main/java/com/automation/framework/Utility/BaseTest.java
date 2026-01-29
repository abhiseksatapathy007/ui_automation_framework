package com.automation.framework.Utility;

import java.util.HashMap;
import java.util.Map;

import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import com.automation.framework.Helper.Commonfunctions;
import com.automation.framework.Helper.Webdriverbase;
import com.automation.framework.Helper.Wrapperdriver;

public class BaseTest {
	public static ExtentSparkReporter spark;
	public static ExtentReports extent;
	public ExtentTest test;
	public ThreadLocal<ExtentTest> extenttest = new ThreadLocal<ExtentTest>();
	protected WebDriver driver = null;
	protected Wrapperdriver wd;
	Webdriverbase wb = new Webdriverbase();
	SlackIntegration slack;
	Commonfunctions cf = new Commonfunctions(driver);
	Environment env;

	@BeforeSuite
	@Parameters({ "environment" })
	public void setUp(String environment) throws Exception {
		ConfigFactory.setProperty("testenv", environment);
		env = ConfigFactory.create(Environment.class);

		ExtentSparkReporter spark = new ExtentSparkReporter("HTMLReport/" + "TestReport_" + env.Env() + ".html")
				.viewConfigurer().viewOrder().as(new ViewName[] { ViewName.DASHBOARD, ViewName.CATEGORY, ViewName.TEST,
						ViewName.DEVICE, ViewName.EXCEPTION })
				.apply();
		extent = new ExtentReports();
		extent.attachReporter(spark);
		extent.setSystemInfo("Environment", env.Env());
		extent.setSystemInfo("Application URL", env.url());

		extent.setAnalysisStrategy(AnalysisStrategy.TEST);
		spark.loadXMLConfig("html-config.xml");

	}

	@BeforeMethod
	@Parameters("browser")
	public void Initialize(String browser) throws Exception {
		driver = wb.StartWebDriver(browser);
		wd = new Wrapperdriver(driver);
	}

	@AfterMethod
	public void getResult(ITestResult result) throws Exception {
		try {
			if (result.getStatus() == ITestResult.FAILURE) {
				extenttest.get().log(Status.FAIL, MarkupHelper
						.createLabel(result.getName() + " Test case FAILED due to below issues:", ExtentColor.RED));
				extenttest.get().fail(result.getThrowable());
				String screenshotBase64 = wd.getScreenshot(driver);
				extenttest.get().info("Click Here for Screenshot:",
						MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64).build());
			} else if (result.getStatus() == ITestResult.SUCCESS) {
				extenttest.get().log(Status.PASS,
						MarkupHelper.createLabel(result.getName() + " Test Case PASSED", ExtentColor.GREEN));
			} else {
				extenttest.get().log(Status.SKIP,
						MarkupHelper.createLabel(result.getName() + " Test Case SKIPPED", ExtentColor.ORANGE));
				extenttest.get().skip(result.getThrowable());
			}
		} catch (Exception e) {
			extenttest.get().log(Status.FAIL, "An error occurred during result processing: " + e.getMessage());
		} finally {
			// Ensure driver quit happens here
			if (driver != null) {
				driver.quit(); // Use quit instead of close to ensure all instances are closed.
			}
		}
	}

	@AfterSuite
	public void tearDown(ITestContext context) throws Exception {
		extent.flush();
		int passed = context.getPassedTests().getAllResults().size();
		int failed = context.getFailedTests().getAllResults().size();
		int skipped = context.getSkippedTests().getAllResults().size();
		int total = passed + failed + skipped;
		slack = new SlackIntegration();
		slack.sendMessagetoSlack(slack.SlackMessagetosend(total, passed, failed, skipped));

	}

	public Map<String, String> rowsMap = new HashMap<>();
	public Map<String, String> headerMap = new HashMap<>();

}
