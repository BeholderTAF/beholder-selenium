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

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.ufmg.dcc.saotome.beholder.Browser;
import br.ufmg.dcc.saotome.beholder.selenium.message.ErrorMessages;

/**
 * Implements the Browser interface.
 * @author Ícaro Clever F. Braga (icaroclever@gmail.com)
 * @version 1.0
 */
public class SeleniumBrowser implements Browser {
	
	
	/** Maximum wait for a component */
	private static long TIMEOUT = 25;// 25 seconds
	private static long AJAX_TIMEOUT=5; // 5 seconds
	private static final long NEXT_INSPECTION_TIME=500; // Next inspection of the webdriver wait.
	private static final SeleniumBrowser INSTANCE = new SeleniumBrowser();
	
	/* Singleton Contructor */
	private SeleniumBrowser () {}
	
	public static final SeleniumBrowser getInstance(){
		return INSTANCE;
	}

	public void open(final URL url) {
		if (url == null) {
			throw new IllegalArgumentException(String.format(ErrorMessages.ERROR_TEMPLATE_VARIABLE_NULL,"url"));
		}
		SeleniumController.getDriver().get(url.toString());
	}

	@Override
	public boolean isTextPresent(final String text) {

		SeleniumController.getDriver().manage().timeouts().pageLoadTimeout(SeleniumBrowser.TIMEOUT, TimeUnit.SECONDS);
		final String expression = text.toLowerCase();
		
		try {
			WebDriverWait wait = new WebDriverWait(SeleniumController.getDriver(), SeleniumBrowser.AJAX_TIMEOUT,NEXT_INSPECTION_TIME);

			ExpectedCondition<Boolean> resultsAreDisplayed = new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver webdriver) {
					
					String pageText = webdriver.findElement(By.tagName("body")).getText().toLowerCase();
					return pageText.contains(expression);
				}
			};
			wait.until(resultsAreDisplayed);
			return true;
		} catch (TimeoutException toe) {
			return false;
		}
	}
	
	/**
	 * Implements the Alert subclass.
	 * @author Ícaro Clever F. Braga (icaroclever@gmail.com)
	 * @version 1.0
	 */
	public class SeleniumAlert extends Alert {

		@Override
		public void confirm() {
			SeleniumController.getDriver().switchTo().alert().accept();
		}

		@Override
		public void cancel() {
			SeleniumController.getDriver().switchTo().alert().dismiss();
		}

		@Override
		public String getText() {
			return SeleniumController.getDriver().switchTo().alert().getText();		
		}
		
	}

	@Override	
	public Alert getAlert() {
		return new SeleniumAlert();
	}

	/**
	 * @return the page timeout in seconds 
	 */
	public static final long getTimeout() {
		return SeleniumBrowser.TIMEOUT;
	}

	/**
	 * @param timeout the page timeout to set in seconds
	 */
	public static final void setTimeout(long timeout) {
		SeleniumBrowser.TIMEOUT = timeout;
	}

	/**
	 * @return the ajax wait Timeout in seconds 
	 */
	public static final long getAjaxTimeout() {
		return SeleniumBrowser.AJAX_TIMEOUT;
	}

	/**
	 * @param ajaxTimeout the ajax wait Timeout to set in secods
	 */
	public static final void setAjaxTimeout(long ajaxTimeout) {
		SeleniumBrowser.AJAX_TIMEOUT = ajaxTimeout;
	}
}
