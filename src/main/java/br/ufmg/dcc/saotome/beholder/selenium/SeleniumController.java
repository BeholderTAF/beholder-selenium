/*  Copyright 2014 Ícaro Clever da Fonseca Braga

	Licensed to the Apache Software Foundation (ASF) under one
	or more contributor license agreements.  See the NOTICE file
	distributed with this work for additional information
	regarding copyright ownership.  The ASF licenses this file
	to you under the Apache License, Version 2.0 (the
	"License"); you may not use this file except in compliance
	with the License.  You may obtain a copy of the License at
	
	  http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing,
	software distributed under the License is distributed on an
	"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
	KIND, either express or implied.  See the License for the
	specific language governing permissions and limitations
	under the License.
 */
package br.ufmg.dcc.saotome.beholder.selenium;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import br.ufmg.dcc.saotome.beholder.Browser;
import br.ufmg.dcc.saotome.beholder.builder.Builder;
import br.ufmg.dcc.saotome.beholder.selenium.builder.SeleniumBuilder;
import br.ufmg.dcc.saotome.beholder.selenium.listener.Listener;
import br.ufmg.dcc.saotome.beholder.selenium.message.ErrorMessages;

/**
 * This class is the basic structure to start a test using the TestNG framework and Selenium-Webdriver.
 * Furthermore, it's the unique beholder-selenium class which will be called explicitly, because it contains
 * the builder to generate the object of the other implemented classes of beholder interface.
 * The controller supports three browsers: Firefox, Chrome (or Chromium) and Internet Explorer. Firefox and
 * Internet Explorer only needs be parameter of startSelenium to be executed. However, Chrome needs a chromedriver
 * that can be found in this link: http://code.google.com/p/chromedriver/downloads/list. To use the chrome 
 * driver, the system environment variable CHROME_DRIVER_BIN must be set. If the chrome application
 * is not in the environment PATH, the CHROME_BIN can be used to pass the binary path of the chrome browser.
 * @author Ícaro Clever F. Braga (icaroclever@gmail.com)
 */
public final class SeleniumController {
	
	/** List of the browsers supported */
	enum BrowsersList {
		FIREFOX,
		CHROME,
		IE
	};
	
	/* Hidden Constructor */
	private SeleniumController() {
	}
	
	/** Browser driver that contains the application component search. */
	private static WebDriver driver;
	/** This object generates all usable components of Beholder. */
	private static Builder builder;	
	/** This object represents a browser functionalities */ 
	private static Browser browser;
	
	private static Map<String,String> parametersMap;
		
	/**
	 * This method starts the selenium remote control using the parameters
	 * informed by testng.xml file
	 * @param parameters
	 * @throws Exception
	 */
	@BeforeSuite(alwaysRun=true)
	@Parameters(value={"parameters"})
	public static void startSelenium(String parameters)
	{
		parametersMap = parameterScanner(parameters);
		
		parametersInfo();
		
		String 	browserName = parametersMap.get("browser"),
				profile = parametersMap.get("profile"),
				chromeDriverBin = parametersMap.get("chromeDriverBin"),
				chromeBin = parametersMap.get("chromeBin");
		
		if(browserName == null){
			throw new IllegalArgumentException("");
		}
		
		if(driver == null) {
			if(browserName.equalsIgnoreCase(BrowsersList.FIREFOX.name())){
				FirefoxProfile fp = new FirefoxProfile();
				fp.setPreference("dom.max_script_run_time", 0);
				fp.setPreference("dom.max_chrome_script_run_time", 0);
				if(profile != null){
					fp.setPreference("webdriver.firefox.profile", profile);
				}
				driver = new WebDriverAdapter(new FirefoxDriver(fp));
			}
			else if(browserName.equalsIgnoreCase(BrowsersList.CHROME.name())) {
				
				if(chromeBin == null || chromeDriverBin == null){
					throw new IllegalArgumentException("");
				}
				
				// Optional, if not specified, WebDriver will search your path for chromedriver 
				// in the system environment.
				System.setProperty("webdriver.chrome.driver", chromeDriverBin);
				ChromeOptions co = new ChromeOptions();
				// Get the chrome binary directory path from System Envionment.
				co.setBinary(new File(chromeBin));
				driver = new WebDriverAdapter(new ChromeDriver(co));
			}
			else if(browserName.equalsIgnoreCase(BrowsersList.IE.name()))
			{
				driver = new WebDriverAdapter(new InternetExplorerDriver());
			}
			else {
				throw new IllegalArgumentException(ErrorMessages.ERROR_BROWSER_INVALID);
			}
		}
		/* Sets to all driver methods the global timeout of 1 second. 
		 * To tests, Timeouts must be specified on the components.
		 */
		SeleniumController.driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		SeleniumController.builder = new SeleniumBuilder(driver);
		SeleniumController.browser = new SeleniumBrowser();
		Listener.setWebDriver(driver);
		Listener.setParameters(parametersMap);
	}
	
	/** Close the driver. It'll close the browsers window and stop the selenium RC.*/
	@AfterSuite(alwaysRun=true)
	public static void closeSelenium()
	{
		if(driver != null) {
			driver.quit();
		}
	}

	/** Returns the Selenium driver object.
	 * @return the driver
	 */
	public static WebDriver getDriver() {
		return driver;
	}

	/** Returns the Beholder builder objects structure.
	 * @return the builder
	 */
	public static Builder getBuilder() {
		return builder;
	}

	/** Returns a Browser object that contains methods to interact with the Browser's interface.
	 * @return the webPage
	 */
	public static Browser getBrowser() {
		return browser;
	}
	
	/**
	 * Processes and returns parameters informed by TestNG XML file. Parameters must have the structure: <br/>
	 * atribute1=value1;atribute2=value2;...
	 * @param parameters
	 * @return a Map with the structure attribute and value
	 */
	private static Map<String,String> parameterScanner(String parameters){
	
		String[] parametersArray = parameters.split(";");
		
		Map<String, String> localParametersMap = new HashMap<String, String>();
		
		for(int count=0;count < parametersArray.length; count++){
			String[] parameter  = parametersArray[count].split("=");
			localParametersMap.put(parameter[0], parameter[1]);
		}
		
		return localParametersMap;
	}

	/**
	 * @return the parametersMap
	 */
	public static String getParameter(String paramenter) {
		return parametersMap.get(paramenter);
	}
	
	private static void parametersInfo(){
		
		Logger logger = LogManager.getLogger("general");
		Set<String> keys = parametersMap.keySet();
		
		StringBuilder parameters = new StringBuilder();
		for (String key : keys) {
			parameters.append(String.format("* %s: %s\n",key,parametersMap.get(key)));
		}
		logger.info("\n[Parameters]\n"+parameters);
	}
}
