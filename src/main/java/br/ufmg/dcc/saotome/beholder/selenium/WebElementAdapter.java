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

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.ufmg.dcc.saotome.beholder.selenium.message.ErrorMessages;
import br.ufmg.dcc.saotome.beholder.selenium.ui.SeleniumComponent;

/**
 * <p>The class WebElementAdapter was created to solve the DOM reference lost in
 * Selenium when an Ajax call reload an object in HTML page. Webdriver sends a
 * StaleElementReferenceException when it idenfifies a WebElement manipulating a
 * DOM object that doesn't exit anymore and gives the responsability of identify
 * this problem to tester. So, to solve this problem, this class implements the
 * WebElementAdapter interface and encapsule the WebElement object returned by a
 * WebElement or WebDriver, trying to request a new web element when the
 * StaleElementReferenceException occurs.
 * <p> Methods using the elements matched by <b>findElements</b> must implement a catch for 
 * StaleElementReferenceException, because if a AJAX reloads one of the elements, the
 * exceptions is not solved by WebElementAdapter.
 * 
 * @author Ícaro Clever F. Braga (icaroclever@gmail.com)
 * @see StaleElementReferenceException
 */
class WebElementAdapter implements WebElement{

	
	// Global Variables
	/** Parent that found the element. */
	private WebElementAdapter parent;
	/** element order of match on search */
	/** Element locator. */
	private By locator;
	/** Element. */
	private WebElement element;
	/** The element is unique? */
	private Boolean isUnique;
	
	// Alias for this object
	private final WebElementAdapter thisObject = this;

	/**
	 * Constructor to a WebElementAdapter which locator was finding a unique reference
	 * match. If the goal of the search was found 2 or more elements, must be
	 * used the contructor with index parameter.
	 * 
	 * @param element
	 *            WebElementAdapter found.
	 * @param parent
	 *            WebElementAdapter that execute the search.
	 * @param locator
	 *            WebElementAdapter locator.
	 */
	public WebElementAdapter(WebElement element, WebElementAdapter parent, By locator) {
		this(element, parent, locator, true);
	}

	/**
	 * Constructor to a WebElementAdapter which locator was finding a list of objects
	 * to match.
	 * 
	 * @param element
	 *            WebElementAdapter found.
	 * @param parent
	 *            WebElementAdapter that execute the search.
	 * @param locator
	 *            WebElementAdapter locator.
	 * @param index
	 *            WebElementAdapter order in which it was found.
	 */
	public WebElementAdapter(WebElement element, WebElementAdapter parent, By locator,
			Boolean isUnique) {

		// Element cannot be null
		if (element == null) {
			throw new IllegalArgumentException(ErrorMessages.ERROR_ELEMENT_IS_NULL);
		}

		// Locator cannot be null
		if (locator == null) {
			throw new IllegalArgumentException(ErrorMessages.ERROR_LOCATOR_IS_NULL);
		}
		
		this.element = element;
		this.parent = parent;
		this.locator = locator;
		this.isUnique = isUnique;
	}

	/**
	 * Recover the index that represents the order when the element was found in
	 * html search
	 * 
	 * @return Returns the index of the element
	 */
	public Boolean isUnique() {
		return this.isUnique;
	}
	
	/**
	 * Recover the index that represents the order when the element was found in
	 * html search
	 * 
	 * @return Returns the index of the element
	 */
	public WebElementAdapter getParent() {
		return this.parent;
	}
	
	/**
	 * recover the element that it was found in
	 * html search
	 * 
	 * @return Returns the index of the element
	 */
	public WebElement getElement() {
		return this.element;
	}

	@Override
	public void click() {
		(new StaleExceptionResolver<Object>() {

			@Override
			public Object execute(WebElement element) {
				element.click();
				return null;
			}
		}).waitForElement();
	}

	@Override
	public void submit() {
		(new StaleExceptionResolver<Object>() {

			@Override
			public Object execute(WebElement element) {
				element.submit();
				return null;
			}
		}).waitForElement();

	}

	@Override
	public void sendKeys(final CharSequence... keysToSend) {
		(new StaleExceptionResolver<Object>() {

			@Override
			public Object execute(WebElement element) {
				element.sendKeys(keysToSend);
				return null;
			}
		}).waitForElement();
	}

	@Override
	public void clear() {
		(new StaleExceptionResolver<Object>() {

			@Override
			public Object execute(WebElement element) {
				element.clear();
				return null;
			}
		}).waitForElement();
	}

	@Override
	public String getTagName() {
		return new StaleExceptionResolver<String>() {
			@Override
			public String execute(WebElement element) {
				return element.getTagName();
			}
		}.waitForElement();
	}

	@Override
	public String getAttribute(final String name) {
		return new StaleExceptionResolver<String>() {

			@Override
			public String execute(WebElement element) {
				return element.getAttribute(name);
			}
		}.waitForElement();
	}

	@Override
	public boolean isSelected() {
		return new StaleExceptionResolver<Boolean>() {
			@Override
			public Boolean execute(WebElement element) {
				return element.isSelected();
			}
		}.waitForElement();
	}

	@Override
	public boolean isEnabled() {
		return new StaleExceptionResolver<Boolean>() {
			@Override
			public Boolean execute(WebElement element) {
				return element.isEnabled();
			}
		}.waitForElement();
	}

	@Override
	public String getText() {
		return new StaleExceptionResolver<String>() {
			@Override
			public String execute(WebElement element) {
				return element.getText();
			}
		}.waitForElement();
	}

	/** {@inheritDoc}
	 * <p> The method using the elements matched by findElements must implement a catch for 
	 * StaleElementReferenceException, because if a AJAX reloads one of the elements, the
	 * exceptions is not solved by WebElementAdapter.*/
	@Override
	public List<WebElement> findElements(final By by) {

		return (List<WebElement>) (new StaleExceptionResolver<List<WebElement>>() {
			@Override
			public List<WebElement> execute(WebElement element) {
				List<WebElement> elements = new ArrayList<WebElement>(); // create
																			// a
																			// new
																			// list
																			// of
																			// WebElements
				for (WebElement webElement : element.findElements(by)) {
					// encapsule the WebElements inside of a WebElementAdapter
					elements.add(new WebElementAdapter(webElement, thisObject, by, false)); 
				}// end for
				return elements;
			}// end execute
		}).waitForElement();
	}// end findElements

	@Override
	public WebElement findElement(final By by) {
		return new StaleExceptionResolver<WebElement>() {

			@Override
			public WebElementAdapter execute(WebElement element) {
				return new WebElementAdapter(element.findElement(by),thisObject,by);
			}
		}.waitForElement();
	}

	@Override
	public boolean isDisplayed() {
		return new StaleExceptionResolver<Boolean>() {
			@Override
			public Boolean execute(WebElement element) {
				return element.isDisplayed();
			}
		}.waitForElement();
	}

	@Override
	public Point getLocation() {
		return (Point) (new StaleExceptionResolver<Point>() {
			@Override
			public Point execute(WebElement element) {
				return element.getLocation();
			}
		}).waitForElement();
	}

	@Override
	public Dimension getSize() {
		return new StaleExceptionResolver<Dimension>() {
			@Override
			public Dimension execute(WebElement element) {
				return element.getSize();
			}
		}.waitForElement();
	}

	@Override
	public String getCssValue(final String propertyName) {
		return new StaleExceptionResolver<String>() {
			@Override
			public String execute(WebElement element) {
				return element.getCssValue(propertyName);
			}
		}.waitForElement();
	}

	/**
	 * This class encapsules the StaleElementReferenceException treatment. If
	 * the DOM reference is lost for a AJAX reload, the class tries to resolve
	 * find another DOM object that represents the same HTML element. It's only
	 * efective when the object was found by <b>findElement</b> method. Otherwise,
	 * if the element was matched by the <b>findElements</b> method, the class
	 * launch a exception and transfer the responsability to the caller method. 
	 * 
	 * @author icaroclever
	 * @param <T>
	 */
	private abstract class StaleExceptionResolver<T> {

		/**
		 * Contains Webdriver comands to be executed and threatned inside of the
		 * waitForElement.
		 * 
		 * @param element
		 *            Webdriver Element
		 * @return 
		 * 		return of the WebElementAdapter command executed. If there aren't return in the 
		 * 		WebElementAdapter method, it returns null.
		 */
		public WebElement reload(WebDriver driver, WebElementAdapter parent, By locator) {

			List<WebElement> elements;

			if (parent == null || parent.getTagName().equalsIgnoreCase("html")) {
				elements = driver.findElements(locator);
			} else {
				elements = parent.findElements(locator);
			}

			if (!elements.isEmpty()) {
				element = elements.get(0);
				return element;
			} else {
				return null;
			}
		}
		
		/**
		 * Contains Webdriver comands to be executed and threatned inside of the
		 * waitForElement.
		 * @param element 
		 * 				Webdriver Element
		 * @return
		 * 		return of the WebElementAdapter command executed. If there arent return 
		 * 		in the WebElementAdapter method, it returns null.
		 */
		public abstract T execute(WebElement element);

		/**
		 * This method tries to resolve the element, even when the DOM object
		 * reference is lost. If the object is unique and occurs a StaleElementReferenceException,
		 * the problem is solved and the element without reference is overwrited by one with
		 * a reference. Althought, if the element is not unique, it throws the 
		 * StaleElementReferenceException to be solved by the element method caller.
		 * 
		 * @param element
		 *            Webdriver Element
		 * @return return of the WebElementAdapter command executed. If there arent
		 *         return in the WebElementAdapter method, it returns null.
		 * @throws StaleElementReferenceException
		 */
		public T waitForElement(final WebDriver driver,
				final WebElement element,
				final WebElementAdapter parent, final By locator,
				final Boolean isUnique) {
			try {
				return this.execute(element);
			} catch (StaleElementReferenceException staleException) {
				
				if(!isUnique) {
					throw staleException;
				}
				
				WebDriverWait wait = new WebDriverWait(driver,
						SeleniumComponent.TIMEOUT);
				ExpectedCondition<WebElement> resultsAreDisplayed = new ExpectedCondition<WebElement>() {
					public WebElement apply(WebDriver driver) {
						return reload(driver, parent, locator);
					}
				};
				WebElement elementNew = wait.until(resultsAreDisplayed);
				
				return this.execute(elementNew);
			}
		}
		
		/**
		 * Method that encapsules the waitForElement superclass method, passing
		 * all parameters necessary to execution, and increasing the
		 * manutenability of the source-code.
		 * 
		 * @return
		 */
		public T waitForElement() {
			return waitForElement(SeleniumController.getDriver(), element, parent, locator,isUnique);
		}
	}
}
