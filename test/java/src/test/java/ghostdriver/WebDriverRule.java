package ghostdriver;

import org.apache.commons.io.FileUtils;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;

import static org.openqa.selenium.OutputType.BYTES;

public class WebDriverRule extends TestWatcher {
  private static DriverFactory factory = new DriverFactory();

  private WebDriver webDriver;

  @Override
  protected void starting(Description description) {
    webDriver = factory.createDriver();
  }

  @Override
  protected void failed(Throwable testFailure, Description description) {
    try {
      String fileName = description.getClassName() + '.' + description.getMethodName() + '.' + System.currentTimeMillis();
      System.out.println("\nTest failed\n" +
          "\n  Page source: " + savePageSource(fileName) +
          "\n  Screenshot: " + takeScreenshot(fileName) +
          "\n");
    } catch (Exception failedToTakeScreenshot) {
      failedToTakeScreenshot.printStackTrace();
    }
  }

  @Override
  protected void finished(Description description) {
    if (webDriver != null) {
      webDriver.quit();
      webDriver = null;
    }
  }

  private String savePageSource(String testName) throws IOException {
    File sourceFile = new File("build/reports/" + testName + ".html");
    FileUtils.writeStringToFile(sourceFile, webDriver.getPageSource());
    return sourceFile.toURI().toURL().toString();
  }

  private String takeScreenshot(String testName) throws IOException {
    byte[] bytes = ((TakesScreenshot) webDriver).getScreenshotAs(BYTES);
    File imageFile = new File("build/reports/" + testName + ".png");
    FileUtils.writeByteArrayToFile(imageFile, bytes);
    return imageFile.toURI().toURL().toString();
  }

  public WebDriver getWebDriver() {
    return webDriver;
  }
}
