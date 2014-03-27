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
package br.ufmg.dcc.saotome.beholder.selenium.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.ufmg.dcc.saotome.beholder.selenium.message.ErrorMessages;
import br.ufmg.dcc.saotome.beholder.ui.Component;

/**
 * This abstract class implements the interface HtmlComponent using the
 * Selenium-Webdriver to simulate the interaction between the component and a
 * system user.
 * 
 * @author Ícaro Clever F. Braga (icaroclever@gmail.com)
 * @see Component
 */
public abstract class SeleniumComponent implements Component {

	private static class Locator {

		public static final String ATTRIBUTE = "attribute";
		public static final String ID = "id";
		public static final String NAME = "name";

		private String loadBy;
		private String value;
		private String tagName;
		private String attributeName;

		public Locator(String loadBy, String value) {
			this.loadBy = loadBy;
			this.value = value;
		}

		public Locator(String tagName, String attributeName, String value) {
			this(ATTRIBUTE, value);
			this.attributeName = attributeName;
			this.tagName = tagName;
		}
	}

	private final WebDriver selenium;
	private SeleniumComponent parent;
	private Boolean isDisplayed = true;
	private Locator locator;

	

	@Override
	public void show() {

		this.isDisplayed = true;
		
		if (Locator.ID.equals(locator.loadBy)) {
			loadById(this.locator.value);
		} else if (Locator.NAME.equals(locator.loadBy)) {
			loadByName(this.locator.value);
		} else if (Locator.ATTRIBUTE.equals(locator.loadBy)) {
			loadByAttribute(this.locator.tagName, locator.attributeName,
					this.locator.value);
		}
	}

	@Override
	public boolean isDisplayed() {
		return this.isDisplayed;
	}

	@Override
	public void hide() {
		this.isDisplayed = false;
	}

	/** Maximum wait for a component */
	public static final long TIMEOUT = 30;// seconds

	private WebElement element;
	private Map<String, String> attributes = new HashMap<String, String>();

	public SeleniumComponent(final WebDriver driver) {
		this.selenium = driver;
	}

	/**
	 * The Selenium-Webdriver element that represents the component HTML of the
	 * simulated page.
	 * 
	 * @return WebElement element
	 */
	public final WebElement getElement() {
		return this.element;
	}

	/**
	 * The Selenium-Webdriver element that represents the component HTML of the
	 * simulated page.
	 * 
	 * @param element WebElement object
	 */
	public final void setElement(final WebElement element) {

		if (element == null) {
			throw new IllegalArgumentException(
					ErrorMessages.ERROR_ELEMENT_IS_NULL);
		}

		this.element = element;

		validateElementTag();
		validateAttributes();
	}

	@Override
	public String getAttribute(final String attribute) {
		if (this.getElement() == null) {
			throw new IllegalArgumentException(
					ErrorMessages.ERROR_ELEMENT_WAS_NOT_LOADED);
		}

		if (!this.attributes.containsKey(attribute)) {
			this.setAttribute(attribute,
					this.getElement().getAttribute(attribute));
		}
		return this.attributes.get(attribute);
	}

	@Override
	public void setAttribute(final String attribute, final String value) {
		if (attribute == null || attribute.isEmpty()) {
			throw new IllegalArgumentException(
					ErrorMessages.ERROR_ATTRIBUTE_EMPTY);
		}

		if (value == null) {
			throw new IllegalArgumentException(ErrorMessages.ERROR_VALUE_NULL);
		}

		this.attributes.put(attribute, value);
	}

	@Override
	public void loadByAttribute(final String tagName,
			final String attributeName, final String value) {

		this.locator = new Locator(tagName, attributeName,
				value);

		if (this.isDisplayed) {
			WebDriverWait wait = new WebDriverWait(getSeleniumWebDriver(),
					TIMEOUT);
			ExpectedCondition<Boolean> resultsAreDisplayed = new ExpectedCondition<Boolean>() {

				public Boolean apply(WebDriver arg0) {
					List<WebElement> elements = getSeleniumWebDriver()
							.findElements(By.tagName(tagName));
					for (WebElement el : elements) {
						if ((el.getAttribute(attributeName) != null)
								&& (el.getAttribute(attributeName)
										.equalsIgnoreCase(value))) {
							setAttribute(attributeName, value);
							setElement(el);
							return true;
						}
					}
					return false;
				}

			};
			wait.until(resultsAreDisplayed);
		}
	}

	@Override
	public final String getId() {

		return this.getAttribute("id");
	}

	@Override
	public void setId(String value) {
		this.setAttribute("id", value);
	}

	@Override
	public void loadById(final String value) {
		this.locator = new Locator(Locator.ID, value);

		if (this.isDisplayed) {
			this.setId(value);
			this.setElement(selenium.findElement(By.id(value)));
		}
	}

	@Override
	public final String getName() {
		return this.getAttribute("name");
	}

	@Override
	public void setName(String value) {
		this.setAttribute("name", value);
	}

	@Override
	public void loadByName(final String value) {
		this.locator = new Locator(Locator.NAME, value);
		if (this.isDisplayed) {
			this.setName(value);
			this.setElement(selenium.findElement(By.name(value)));
		}
	}

	/**
	 * Verify if the element loaded is a valid element for the class that is
	 * representing it.
	 * 
	 * @return Returns true if the HTML tag is valid to current class, otherwise
	 *         false.
	 * @throws RuntimeException
	 *             if the element is invalid.
	 */
	public abstract boolean isValidElementTag();

	/**
	 * Verify if the element loaded is a valid element for the class that is
	 * representing it.
	 */
	public void validateElementTag() {

		String errorMsg = String.format(
				ErrorMessages.ERROR_INVALID_TAG_TO_CLASS, getElement()
						.getTagName());

		if (!isValidElementTag()) {
			throw new IllegalArgumentException(errorMsg);
		}
	}

	public void validateAttributes() {

		for (Entry<String, String> attribute : this.attributes.entrySet()) {
			if (!getElement().getAttribute(attribute.getKey())
					.equalsIgnoreCase(attribute.getValue())) {
				throw new IllegalArgumentException(
						ErrorMessages.ERROR_ELEMENTS_ATTRIBUTES_NOT_MATCH);
			}

		}

	}

	/**
	 * This method returns a Selenium Driver instance.
	 * 
	 * @return Selenium Driver
	 */
	public final WebDriver getSeleniumWebDriver() {
		return selenium;
	}

	/**
	 * This method returns the basic location that represents the html object in
	 * the html page. For example, if a object is identified by tag <tag
	 * attrib="foo">, the basic xpath must be a[ \@attrib='foo' ]
	 * 
	 * @return Returns a minimal xpath representation of the html object on the
	 *         browser's page.
	 */
	public abstract String getBasicLocator();

	/**
	 * Returns the relative locator to find the html object on HTML page using
	 * the index of the HTML object to recover it.
	 * 
	 * @param index
	 *            HTML index of the object. Starts with 1.
	 * @return Returns a relative xpath representation of the html object on the
	 *         browser's page.
	 */
	public String getLocator(Integer index) {

		String relLocator = ".//";

		if (getBasicLocator().contains("[")) {
			relLocator += getBasicLocator().replace("]", " and position()=%d ]");
		} else {
			relLocator += "%s[%d]";
		}

		return String.format(relLocator, index);
	}

	/**
	 * @return the parent
	 */
	public SeleniumComponent getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(SeleniumComponent parent) {
		this.parent = parent;
	}
}
