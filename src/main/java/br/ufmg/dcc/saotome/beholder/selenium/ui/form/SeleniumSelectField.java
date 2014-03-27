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
package br.ufmg.dcc.saotome.beholder.selenium.ui.form;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.ufmg.dcc.saotome.beholder.selenium.ui.SeleniumComponent;
import br.ufmg.dcc.saotome.beholder.ui.form.Select;

/**
 * This abstract class implements the interface Select using the
 * Selenium-Webdriver to simulate the interaction between the component and a
 * system user.
 * 
 * @author Ícaro Clever F. Braga (icaroclever@gmail.com)
 * @see SeleniumComponent
 * @see br.ufmg.dcc.saotome.beholder.ui.form.Select
 * 
 */
public class SeleniumSelectField extends SeleniumComponent implements Select {

	public SeleniumSelectField(WebDriver driver) {
		super(driver);
	}

	/** List of Options loaded by select */
	private List<Option> options = new ArrayList<Option>();
	
	/** This class extends Option, inserting the concept of WebElement to be
	 * worked inside of SeleniumSelectField class*/
	private static class SeleniumOption extends Option{
		/** Option WebElement */
		private WebElement webElement;
	}
	
	private enum Type {
		VALUE, INDEX, TEXT
	};
	
	@Override
	public final List<Option> getOptions() {

		try {
			SeleniumOption option;
			List<WebElement> webElements = getElement().findElements(By.tagName("option"));
			for (WebElement element : webElements) {
				option = new SeleniumOption();
				option.setIndex(Integer.valueOf(element.getAttribute("index")));
				option.setValue(element.getAttribute("value"));
				option.setText(element.getText());
				option.webElement = element;
				options.add(option);
			}
			// TODO Esse if está impedindo a releitura das opções quando o select é carregado antes de ser atualizado.
		} catch (StaleElementReferenceException e) {
			loadById(getId());
			// TODO verificar como tratar
			if(!this.options.isEmpty()) {
				this.options = new ArrayList<Option>(); // reset the list
			}
						
		}
		return this.options;
	}

	/**
	 * {@inheritDoc} If the option text passed contains "submit::" in the string,
	 * the submit value is searched, not the text inside of the option.
	 */
	@Override
	public final void select(final String optionText) {
		if(optionText.contains("submit::")){
			String value = optionText.split("::")[1];
			select(Type.VALUE, value);
		}else {
			select(Type.TEXT, optionText);
		}
	}

	@Override
	public final void select(final int optionIndex) {
		select(Type.INDEX, optionIndex);
	}

	/**
	 * this private method try to search the value of the select for n seconds
	 * specified by TIMEOUT static variable. The coerence if the data passed is
	 * or isn't the correct value is responsability of the test developer.
	 * 
	 * @param type
	 *            values defined by the Type private enumeration
	 * @param value
	 *            value to be selected
	 */

	private void select(Type type, Object value) {

		assert value != null;
		
		try {
			this.selectByType(type, value);
		} catch (StaleElementReferenceException e) {
			// TODO verificar como tratar
			loadById(getId());
			this.selectByType(type, value);
		}
	}

	private void selectByType(final Type type, final Object value) {
		
		final Option option = new SeleniumOption();
		switch (type) {
			case INDEX:
				option.setIndex((Integer)value);
				break;
			case VALUE:
				option.setValue((String)value);
				break;
			case TEXT:
				option.setText((String)value);
				break;
		}
		
		WebDriverWait wait = new WebDriverWait(getSeleniumWebDriver(), SeleniumComponent.TIMEOUT);
		ExpectedCondition<Option> resultsAreDisplayed = new ExpectedCondition<Option>() {

			public Option apply(WebDriver driver) {
				Iterator<Option> iterator = getOptions().iterator();
				
				while(iterator.hasNext()){
					Option opt = iterator.next();
					if(isSame(option,opt)){
						((SeleniumOption)opt).webElement.click();
						return opt;
					}
				}
				return null; 
			}
		};
		wait.until(resultsAreDisplayed);
	}
	
	
	/** This method makes easy to compare between two option components. There are 2 rules
	 * implemented:<br/>
	 * - only class attributes not null in both classes are evaluated;<br/>
	 * - all evaluable attributes must be equals.
	 * @return Returns true only if the 2 rules above are true. Otherwise the return is false.
	 */
	private boolean isSame(Option option1, Option option2) {
		
		if (option1 == null || option2 == null) {
			return false;
		} if (option1.equals(option2)) {
			return true;
		}
		
		String 	index1 = (option1.getIndex() == null)?null:Integer.toString(option1.getIndex()),
				index2 = (option2.getIndex() == null)?null:Integer.toString(option2.getIndex());
		
		boolean isValidIndex = validField(index1, index2); 
		boolean isValidValue = validField(option1.getValue(), option2.getValue());
		boolean isValidText = validField(option1.getText(),option2.getText());
		
		return isValidIndex && isValidValue && isValidText;
	}
	
	private boolean validField(String valueA, String valueB){
		
		boolean isANull, isBNull;
		
		isANull = (valueA == null);
		isBNull = (valueB == null);
		
		if(isANull && isBNull) {
			return false;
		}
		
		if (!isANull && !isBNull) {
			return valueA.equalsIgnoreCase(valueB);	
		}

		return true;
	}


	@Override
	public final boolean isValidElementTag() {
		return "select".equalsIgnoreCase(getElement().getTagName());
	}

	@Override
	public String getBasicLocator() {
		return "select";
	}

	@Override
	public void click() {
		getElement().click();
	}
}
