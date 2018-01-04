package ghostdriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Takes care of initialising the Remote WebDriver
 */
public class DriverFactory {
  private DesiredCapabilities sCaps;

  public DriverFactory() {
    Properties sConfig = readConfigFile();

    // Prepare capabilities
    sCaps = new DesiredCapabilities();
    sCaps.setJavascriptEnabled(true);
    sCaps.setCapability("takesScreenshot", false);

    // Fetch PhantomJS-specific configuration parameters
    // "phantomjs_exec_path"
    if (sConfig.getProperty("phantomjs_exec_path") != null) {
      sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, sConfig.getProperty("phantomjs_exec_path"));
    } else {
      System.out.println(String.format("Property '%s' not set. Hoping to find phantomjs in PATH.", PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY));
    }
    // "phantomjs_driver_path"
    if (sConfig.getProperty("phantomjs_driver_path") != null) {
      System.out.println("Test will use an external GhostDriver");
      sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_PATH_PROPERTY, sConfig.getProperty("phantomjs_driver_path"));
    } else {
      System.out.println("Test will use PhantomJS internal GhostDriver");
    }

    // Disable "web-security", enable all possible "ssl-protocols" and "ignore-ssl-errors" for PhantomJSDriver
//        sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, new String[] {
//            "--web-security=false",
//            "--ssl-protocol=any",
//            "--ignore-ssl-errors=true"
//        });
    ArrayList<String> cliArgsCap = new ArrayList<String>();
    cliArgsCap.add("--web-security=false");
    cliArgsCap.add("--ssl-protocol=any");
    cliArgsCap.add("--ignore-ssl-errors=true");
    sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);

    // Control LogLevel for GhostDriver, via CLI arguments
    sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_CLI_ARGS, new String[]{
        "--logLevel=" + (sConfig.getProperty("phantomjs_driver_loglevel") != null ? sConfig.getProperty("phantomjs_driver_loglevel") : "INFO")
    });
  }

  private Properties readConfigFile() {
    String configFile = "../config.ini";
    Properties sConfig = new Properties();
    try {
      sConfig.load(new FileReader(configFile));
    } catch (IOException e) {
      throw new RuntimeException("Cannot read file " + configFile, e);
    }
    return sConfig;
  }

  public WebDriver createDriver() {
    return new PhantomJSDriver(sCaps);
  }

  public DesiredCapabilities getCapabilities() {
    return sCaps;
  }
}
