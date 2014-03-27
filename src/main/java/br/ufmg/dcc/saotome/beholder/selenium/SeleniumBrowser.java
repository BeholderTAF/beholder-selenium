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

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.ufmg.dcc.saotome.beholder.Browser;
import br.ufmg.dcc.saotome.beholder.selenium.ui.SeleniumComponent;

/**
 * Implements the Browser interface.
 * @author Ícaro Clever F. Braga (icaroclever@gmail.com)
 * @version 1.0
 */
public class SeleniumBrowser implements Browser {

	public void open(final URL url) {
		if (url != null) {
			SeleniumController.getDriver().get(url.toString());
		} // TODO else with ERROR MESSAGE
	}

	@Override
	public boolean isTextPresent(final String text) {

		try {
			WebDriverWait wait = new WebDriverWait(SeleniumController.getDriver(), SeleniumComponent.TIMEOUT);

			ExpectedCondition<Boolean> resultsAreDisplayed = new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver arg0) {
					String expression = text.toLowerCase();
					String pageText = SeleniumController.getDriver().findElement(By.tagName("body")).getText().toLowerCase();
					return pageText	.contains(expression);
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
}
