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

import java.util.List;
import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Killable;
import org.openqa.selenium.remote.RemoteWebDriver;

import br.ufmg.dcc.saotome.beholder.selenium.message.ErrorMessages;

/** 
 * The class WebDriverAdapter was created to solve the DOM reference lost in Selenium when 
 * an Ajax call reload an object in HTML page. Webdriver sends a StaleElementReferenceException
 * when it idenfifies a WebElement manipulating a DOM object that doesn't exit anymore and gives
 * the responsability of identify this problem to tester. So, to solve this problem, this class
 * implements the WebDriver interface and encapsule the WebDriver object returned by WebDriver, 
 * trying to request a new web element when the StaleElementReferenceException occurs.
 * 
 * @author Ícaro Clever F. Braga (icaroclever@gmail.com)
 * @see StaleElementReferenceException
 */
class WebDriverAdapter extends RemoteWebDriver implements TakesScreenshot, Killable, WebDriver {
	
	/** Webdriver original instance */
	private WebDriver driver;
	
	/** Constructor that encapsule the original WebDriver.
	 * @param driver
	 */
	public WebDriverAdapter(WebDriver driver) {
		
		// Driver cannot be null
		if(driver == null) {
			throw new IllegalArgumentException(ErrorMessages.ERROR_DRIVER_IS_NULL); //TODO
		}
		
		// The driver must be a original drive, if it's not, the class get the original WebDriver
		if (driver instanceof WebDriverAdapter) {
			this.driver = ((WebDriverAdapter)driver).getWebDriver();
		} else {
			this.driver = driver;
		}
	}

	@Override
	public void get(String url) {
		this.driver.get(url);
	}

	@Override
	public String getCurrentUrl() {
		return this.driver.getCurrentUrl();
	}

	@Override
	public String getTitle() {
		return this.driver.getTitle();
	}

	@Override
	public List<WebElement> findElements(final By by) {
		
		// It's instanced a html element to reuse the findElements of WebElementAdapter		
		WebElement html = this.driver.findElement(By.tagName("html"));
		WebElement htmlAdapter = new WebElementAdapter(html, null, By.tagName("html"));
		
		return htmlAdapter.findElements(by);
	}

	@Override
	public WebElement findElement(final By by) {
		
		final String tag = "html";
		
		// It's instanced a html element to reuse the findElement of WebElementAdapter
		WebElement html = this.driver.findElement(By.tagName(tag));
		WebElement htmlAdapter = new WebElementAdapter(html, null, By.tagName(tag));
		
		return htmlAdapter.findElement(by);
	}

	@Override
	public String getPageSource() {
		return this.driver.getPageSource();
	}

	@Override
	public void close() {
		this.driver.close();
	}

	@Override
	public void quit() {
		this.driver.quit();
	}

	@Override
	public Set<String> getWindowHandles() {
		return this.driver.getWindowHandles();
	}

	@Override
	public String getWindowHandle() {
		return this.driver.getWindowHandle();
	}

	@Override
	public TargetLocator switchTo() {
		return new TargetLocatorAdapter(this.driver.switchTo());
	}

	@Override
	public Navigation navigate() {
		return this.driver.navigate();
	}

	@Override
	public Options manage() {
		return this.driver.manage();
	}
	
	/**
	 * Returns the WebDriver that it's been used by the adapter.
	 * @return
	 * 		Return the WebDriver 
	 */
	public final WebDriver getWebDriver(){
		return this.driver;
	}

	@Override
	public void kill() {
		((Killable)driver).kill();
	}

	@Override
	public <X> X getScreenshotAs(OutputType<X> target) {
		return ((TakesScreenshot)driver).<X>getScreenshotAs(target);
	}
	
	private static class TargetLocatorAdapter implements TargetLocator{
		
		private TargetLocator targetLocator;
		
		public TargetLocatorAdapter(TargetLocator targetLocator) {
			this.targetLocator = targetLocator;
		}

		@Override
		public WebDriver frame(int index) {
			return this.targetLocator.frame(index);
		}

		@Override
		public WebDriver frame(String nameOrId) {
			return this.targetLocator.frame(nameOrId);
		}

		@Override
		public WebDriver frame(WebElement frameElement) {
			WebElement element;
			if(frameElement instanceof WebElementAdapter){
				element = ((WebElementAdapter)frameElement).getElement();
			}else {
				element = frameElement;
			}
			return this.targetLocator.frame(element);
		}

		@Override
		public WebDriver window(String nameOrHandle) {
			return this.targetLocator.window(nameOrHandle);
		}

		@Override
		public WebDriver defaultContent() {
			return this.targetLocator.defaultContent();
		}

		@Override
		public WebElement activeElement() {
			return this.targetLocator.activeElement();
		}

		@Override
		public Alert alert() {
			return this.targetLocator.alert();
		}
		
	}
}
