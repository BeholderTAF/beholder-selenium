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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.ufmg.dcc.saotome.beholder.selenium.SeleniumBrowser;
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
public abstract  class SeleniumComponent implements Component {
	
	private final WebDriver selenium;
	private SeleniumComponent parent;
	private Boolean isDisplayed = true;
	private Locator locator;
	private Boolean validated = false;

	private WebElement element;
	private Map<String, String> attributes = new HashMap<String, String>();

	/* ------------------------------------------------------------------------ *
	 *							CONTRUCTORS										*
	 * ------------------------------------------------------------------------ */
	
	public SeleniumComponent(final WebDriver driver) {
		this.selenium = driver;
	}
	
	/* ------------------------------------------------------------------------ *
	 *							IMPLEMENTED METHODS								*
	 * ------------------------------------------------------------------------ */

	
	@Override
	public String getAttribute(final String attribute) {
		
		if (this.locator == null) { throw new IllegalArgumentException(ErrorMessages.ERROR_ELEMENT_WAS_NOT_LOADED); }
		if (attribute == null || attribute.isEmpty()){ throw new IllegalArgumentException(ErrorMessages.ERROR_ATTRIBUTE_EMPTY); }
		if(this.element == null){
			reloadElement();
		}
		return this.element.getAttribute(attribute);
	}

	@Override
	public void setAttribute(final String attribute, final String value) {
		
		if (attribute == null || attribute.isEmpty()) { throw new IllegalArgumentException( ErrorMessages.ERROR_ATTRIBUTE_EMPTY); }
		if (value == null) { throw new IllegalArgumentException(String.format(ErrorMessages.ERROR_TEMPLATE_VARIABLE_NULL,"value")); }
		
		this.attributes.put(attribute, value);
	}
	
	@Override
	public void loadByAttribute(final String tagName,final String attributeName, final String value) {
		this.locator = new Locator(tagName, attributeName,value);
	}
	
	@Override
	public void loadById(final String value) {
		this.locator = new Locator(Locator.LocatorType.ID, value);
	}
	
	@Override
	public void loadByName(final String value) {
		this.locator = new Locator(Locator.LocatorType.NAME, value);
	}
	
	@Override
	public <T extends Component,  Y extends T> List<T> loadByAttribute(Class<Y> type, final String IdFather, final String tagName, final String attributeName, final String value) {
				
		List<T> components = new ArrayList<T>();
		
		this.locator = new Locator(tagName, attributeName,value);
		
		if (this.isDisplayed) {
			WebDriverWait wait = new WebDriverWait(getSeleniumWebDriver(),SeleniumBrowser.getTimeout());
		
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName(tagName)));
			List<WebElement> elements;
			
			if (IdFather == null){
				elements  =  getSeleniumWebDriver()
					.findElements(By.tagName(tagName));
			} else {
				elements = getSeleniumWebDriver().findElement(By.id(IdFather)).
						findElements(By.tagName(tagName));
			}	
			
			for (WebElement el : elements) {
				if ((el.getAttribute(attributeName) != null)
						&& (el.getAttribute(attributeName)
								.equalsIgnoreCase(value))) {	
						T sc = null;
				
						try {
							// Use of reflection for instantiate sc 
							// this equivalent the get an instance of Builder.uiComponentBuilderInstance()
							sc =  	(T) type.getDeclaredConstructor(
									WebDriver.class).newInstance(getSeleniumWebDriver());
						
						} catch (Exception e) {						
							e.printStackTrace();
						}
						
						((SeleniumComponent) sc).setAttribute(attributeName, value);
						((SeleniumComponent) sc).setElement(el);			
						components.add(sc);
				}
			}
		}
		return components;
	}
	
	@Override
	public void loadByXPath(final String value){		
		this.locator = new Locator(Locator.LocatorType.XPATH , value);
		if(this.isDisplayed){
			WebDriverWait wait = new WebDriverWait(getSeleniumWebDriver(), 	SeleniumBrowser.getTimeout());			
			wait.until(ExpectedConditions.visibilityOf(getSeleniumWebDriver().findElement(By.xpath(value))));
			setXPath(value);
			setElement(getSeleniumWebDriver().findElement(By.xpath(value)));
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
	public final String getName() {
		return this.getAttribute("name");
	}

	@Override
	public void setName(String value) {
		this.setAttribute("name", value);
	}

	@Override
	public final String getXPath(){
		return this.getAttribute("xpath");
	}
	
	@Override
	public void setXPath(String value){
		this.setAttribute("xpath", value);		
	}
	
	@Override
	public void show() {
		this.isDisplayed = true;
		reloadElement();
	}

	@Override
	public boolean isDisplayed() {
		return this.isDisplayed;
	}

	@Override
	public void hide() {
		this.isDisplayed = false;
	}
	
	/* ------------------------------------------------------------------------ *
	 *							PUBLIC METHODS									*
	 * ------------------------------------------------------------------------ */
	/**
	 * The Selenium-Webdriver element that represents the component HTML of the
	 * simulated page.
	 * 
	 * @return Returns the newest DOM WebElement.
	 */
	public final WebElement getElement() {

		if(this.locator == null){
			throw new IllegalStateException(ErrorMessages.ERROR_CANNOT_RELOAD_ELEMENT);
		}
		
		if(this.validated || this.element == null){
			reloadElement();
		}
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
			throw new IllegalArgumentException(String.format(ErrorMessages.ERROR_TEMPLATE_VARIABLE_NULL,"element"));
		}

		this.element = element;
		
		if(!this.validated){
			validateElementTag();
			validateAttributes();
			this.validated = true;
		}
	}

	/**
	 *	Reload an previously loaded element.
	 */
	public void reloadElement(){
		
		switch(this.locator.loadBy){
			case XPATH:
				loadByXPath(this.locator.value);
			break;
			default:
				loadBy(this.locator);
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
				ErrorMessages.ERROR_INVALID_TAG_TO_CLASS, this.element
						.getTagName());

		if (!isValidElementTag()) {
			throw new IllegalArgumentException(errorMsg);
		}
	}

	public void validateAttributes() {
		for (Entry<String, String> attribute : this.attributes.entrySet()) {		
			if(!attribute.getKey().equals("xpath")){
				if (!this.element.getAttribute(attribute.getKey()).equalsIgnoreCase(attribute.getValue())) { throw new IllegalArgumentException(ErrorMessages.ERROR_ELEMENTS_ATTRIBUTES_NOT_MATCH);	}
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
	 * the html page. For example, if a object is identified by tag {@literal <tag
	 * attrib="foo">}, the basic xpath must be a[ \@attrib='foo' ]
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
	 * Getter to the parent component
	 * @return the parent
	 */
	public SeleniumComponent getParent() {
		return parent;
	}

	/**
	 * Setter to the parent compoment
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(SeleniumComponent parent) {
		this.parent = parent;
	}
	
	/* ------------------------------------------------------------------------ *
	 *							PRIVATE METHODS									*
	 * ------------------------------------------------------------------------ */
	
	/**
	 * Retrieve the webdriver element for the locator informed.
	 * @param Locator of to webpage element.
	 */
	private void loadBy(final Locator locator){
		
		if (this.isDisplayed) {
			switch (locator.loadBy) {
			case ID:
				this.setId(locator.value);
				this.setElement(selenium.findElement(By.id(locator.value)));
				break;
			case NAME:
				this.setName(locator.value);
				this.setElement(selenium.findElement(By.name(locator.value)));
				break;
			case ATTRIBUTE:
				WebDriverWait wait = new WebDriverWait(getSeleniumWebDriver(),SeleniumBrowser.getTimeout());
				ExpectedCondition<Boolean> resultsAreDisplayed = new ExpectedCondition<Boolean>() {

					public Boolean apply(WebDriver arg0) {
						List<WebElement> elements = getSeleniumWebDriver().findElements(By.tagName(locator.tagName));
						for (WebElement el : elements) {
							if ((el.getAttribute(locator.attributeName) != null) && (el.getAttribute(locator.attributeName).equalsIgnoreCase(locator.value))) {
								setAttribute(locator.attributeName, locator.value);
								setElement(el);
								return true;
							}
						}
						return false;
					}
				};
				wait.until(resultsAreDisplayed);
				break;
				default:
					throw new IllegalArgumentException(ErrorMessages.ERROR_LOCATOR_LOADBY_INCORRECT);
			}
		}
	}
	
	/**
	 * Inner private class to save the component's locator. 
	 * @author icaroclever
	 */
	private static class Locator {

		public enum LocatorType{
			ATTRIBUTE,
			ID,
			NAME,
			XPATH;
		}
		
		private LocatorType loadBy;
		private String value;
		private String tagName;
		private String attributeName;

		private Locator(LocatorType loadBy, String value) {
			
			// Preconditions
			if(loadBy == null){ throw new IllegalArgumentException(String.format(ErrorMessages.ERROR_PARAMETER_NULL,"loadBy")); }
			if(value == null || value.isEmpty()){ throw new IllegalArgumentException(String.format(ErrorMessages.ERROR_PARAMETER_NULL,"value")); }
			
			this.loadBy = loadBy;
			this.value = value;
		}

		private Locator(String tagName, String attributeName, String value) {
			
			this(LocatorType.ATTRIBUTE, value);
			
			// Preconditions			
			if(tagName == null || tagName.isEmpty()){ throw new IllegalArgumentException(String.format(ErrorMessages.ERROR_PARAMETER_NULL,"tagName")); }
			if(attributeName == null || attributeName.isEmpty()){ throw new IllegalArgumentException(String.format(ErrorMessages.ERROR_PARAMETER_NULL,"attributeName")); }
			
			this.attributeName = attributeName;
			this.tagName = tagName;
		}
	}	
}
