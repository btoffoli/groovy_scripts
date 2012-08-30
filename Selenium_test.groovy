//@GrabResolver(name='restlet', root='http://nexus/nexus/')
@Grapes([
	@Grab(group='org.seleniumhq.selenium.ide', module='selenium-ide', version='1.0.1'),
	@Grab(group='org.seleniumhq.selenium', module='selenium-firefox-driver', version='2.25.0'),
	@Grab(group='org.seleniumhq.selenium', module='selenium-chrome-driver', version='2.8.0')
])

import org.openqa.selenium.*;


import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxProfile

WebDriver driver;


//static Wait<WebDriver> wait;



driver = new FirefoxDriver();

FirefoxProfile profile = new FirefoxProfile();
profile.setPreference("app.update.enabled", false);
String domain = "extensions.firebug.";

// Set default Firebug preferences
profile.setPreference(domain + "currentVersion", "2.0");
profile.setPreference(domain + "allPagesActivation", "on");
profile.setPreference(domain + "defaultPanelName", "net");
profile.setPreference(domain + "net.enableSites", true);

// Set default NetExport preferences
profile.setPreference(domain + "netexport.alwaysEnableAutoExport", true);
profile.setPreference(domain + "netexport.showPreview", false);
profile.setPreference(domain + "netexport.defaultLogDir", "/tmp/");

driver.get("http://www.google.com")

sleep 3000

driver.quit()

