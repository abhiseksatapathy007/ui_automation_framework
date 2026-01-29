# UI Automation Framework

A generic, reusable Selenium WebDriver automation framework built with Java, TestNG, and Maven. This framework provides a solid foundation for web UI automation testing with support for multiple browsers, comprehensive reporting, and extensible architecture.

## Features

- **Page Object Model (POM)** - Clean separation of page objects and test logic
- **Multi-Browser Support** - Chrome, Firefox, Edge, and Remote WebDriver
- **Extent Reports** - Beautiful HTML test reports with screenshots
- **TestNG Integration** - Full TestNG support with parallel execution capabilities
- **Configuration Management** - Environment-based configuration using Owner library
- **Helper Classes** - Reusable wrapper methods for common Selenium operations
- **JavaScript Waits** - Support for jQuery and Angular applications
- **Slack Integration** - Optional Slack notifications for test results

## Framework Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/automation/framework/
│   │       ├── Helper/
│   │       │   ├── Commonfunctions.java    # Common utility functions
│   │       │   ├── Webdriverbase.java      # WebDriver initialization
│   │       │   └── Wrapperdriver.java      # Selenium wrapper methods
│   │       └── Utility/
│   │           ├── BaseTest.java           # Base test class with TestNG hooks
│   │           ├── Environment.java        # Configuration interface
│   │           ├── JSWaiter.java           # JavaScript wait utilities
│   │           └── SlackIntegration.java  # Slack notification support
│   └── resources/
│       ├── demo.properties                # Demo environment configuration
│       └── Email.properties               # Email configuration (optional)
└── test/
    └── java/
        └── com/automation/framework/
            ├── PageObjects/               # Page Object classes
            └── Tests/                     # Test classes
```

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Chrome/Firefox/Edge browser installed (for local execution)

## Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd ui_automation_framework
```

### 2. Configure Environment

Edit `src/main/resources/demo.properties` to configure your test environment:

```properties
Env = Demo
url = https://the-internet.herokuapp.com
testUser = your_test_user
testPassword = your_test_password
```

### 3. Run Tests

#### Run all tests:
```bash
mvn clean test
```

#### Run specific test suite:
```bash
mvn test -DsuiteXmlFile=testng.xml
```

#### Run with specific browser:
Edit `testng.xml` and set the browser parameter:
```xml
<parameter name="browser" value="chrome" />
```

Supported browsers: `chrome`, `firefox`, `edge`, `remote-chrome`, `remote-firefox`

## Configuration

### Environment Properties

Create environment-specific property files in `src/main/resources/`:

- `demo.properties` - Demo/test environment
- `staging.properties` - Staging environment
- `production.properties` - Production environment

The framework uses the Owner library to load properties based on the `environment` parameter in `testng.xml`.

### TestNG Configuration

Edit `testng.xml` to configure:
- Browser selection
- Environment selection
- Test classes to execute
- Parallel execution settings

## Writing Tests

### Example Test Class

```java
package com.automation.framework.Tests;

import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.automation.framework.Helper.Wrapperdriver;
import com.automation.framework.PageObjects.HomePage;
import com.automation.framework.Utility.BaseTest;
import com.automation.framework.Utility.Environment;

public class ExampleTest extends BaseTest {
    Environment env = ConfigFactory.create(Environment.class);

    @Test(description = "Example test")
    public void testExample() throws Exception {
        extenttest.set(test);
        test = extent.createTest("Example Test");
        extenttest.set(test);

        driver.get(env.url());
        wd = new Wrapperdriver(driver);

        HomePage homePage = new HomePage(driver);
        homePage.clickFormAuthentication();

        Assert.assertTrue(driver.getCurrentUrl().contains("login"));
    }
}
```

### Page Object Example

```java
package com.automation.framework.PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.automation.framework.Helper.Wrapperdriver;

public class HomePage {
    public WebDriver driver;
    Wrapperdriver wd;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        wd = new Wrapperdriver(driver);
    }

    public By loginButton = By.id("login");

    public void clickLogin() {
        wd.clickOnWebElement(loginButton);
    }
}
```

## Reports

Test reports are generated in the `HTMLReport/` directory after test execution. The reports include:
- Test execution summary
- Pass/Fail status
- Screenshots for failed tests
- Execution time
- System information

## Helper Classes

### Wrapperdriver

Provides wrapper methods for common Selenium operations:
- `clickOnWebElement(By)` - Click with wait
- `sendKeysToWebelement(By, String)` - Send keys with wait
- `waitForElementToBePresent(By, long)` - Wait for element
- `getScreenshot(WebDriver)` - Capture screenshot
- And many more...

### Commonfunctions

Utility functions for:
- File operations
- Date/time manipulation
- Random string generation
- List comparisons

## Browser Configuration

### Local Execution

The framework uses WebDriverManager to automatically download and manage browser drivers. No manual driver setup required.

### Remote Execution (Selenium Grid)

Configure remote URL in `Webdriverbase.java`:
```java
public static String remote_url = "http://your-grid-hub:4444";
```

Use browser parameter: `remote-chrome` or `remote-firefox`

## Extending the Framework

### Adding New Helper Methods

Add methods to `Wrapperdriver.java` or `Commonfunctions.java` as needed.

### Adding New Utilities

Create new utility classes in `com.automation.framework.Utility` package.

### Custom Reports

Extend `BaseTest.java` to customize Extent Reports configuration.

## Best Practices

1. **Page Object Model**: Always use Page Objects for web elements and page interactions
2. **Wait Strategies**: Use explicit waits instead of Thread.sleep()
3. **Test Data**: Store test data in properties files or external data sources
4. **Error Handling**: Implement proper exception handling in tests
5. **Cleanup**: Ensure proper cleanup in @AfterMethod hooks
6. **Reporting**: Use Extent Reports for detailed test reporting

## Troubleshooting

### Browser Driver Issues
- WebDriverManager handles driver downloads automatically
- Ensure browser is installed and up to date

### Test Failures
- Check screenshots in Extent Reports
- Verify environment configuration
- Ensure application URL is accessible

### Build Issues
- Run `mvn clean install` to rebuild
- Check Java version compatibility
- Verify Maven dependencies

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## License

This framework is provided as-is for automation testing purposes.

## Support

For issues and questions, please create an issue in the repository.
