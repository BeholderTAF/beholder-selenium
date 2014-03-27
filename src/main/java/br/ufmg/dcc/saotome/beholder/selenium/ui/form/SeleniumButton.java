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

import org.openqa.selenium.WebDriver;

import br.ufmg.dcc.saotome.beholder.selenium.ui.SeleniumComponent;
import br.ufmg.dcc.saotome.beholder.ui.form.Button;

/**
 * This class implements the interface Button using the Selenium-Webdriver
 * to simulate the interaction between the component and a system user.
 *
 * @author icaroclever
 * @see SeleniumComponent
 * @see Button
 */
public class SeleniumButton extends SeleniumComponent implements Button {


    private static final String ERROR_INVALID_INPUT_TYPE = "The Element INPUT of the page is not of type SUBMIT,RESET,BUTTON or IMAGE";

	public SeleniumButton(WebDriver driver) {
		super(driver);
	}

	@Override
    public final void click() {
        getElement().click();
    }

    @Override
    public void setType(final ButtonType type) {
        // TODO Is necessary to set the type?
    }

    @Override
    public ButtonType getType() {
    	
    	String buttonType = getAttribute("type");
    	
        if (ButtonType.RESET.name().equalsIgnoreCase(buttonType)) {
            return ButtonType.RESET;
        }
        if (ButtonType.BUTTON.name().equalsIgnoreCase(buttonType)) {
            return ButtonType.BUTTON;
        }
        if (ButtonType.SUBMIT.name().equalsIgnoreCase(buttonType)) {
            return ButtonType.SUBMIT;
        }
        if (ButtonType.IMAGE.name().equalsIgnoreCase(buttonType)) {
        	return ButtonType.IMAGE;
        }
        return null;
    }
    

    @Override
    public final boolean isValidElementTag() {

        boolean isButton = "button".equalsIgnoreCase(getElement().getTagName()),
            isInput = "input".equalsIgnoreCase(getElement().getTagName()),
            isImg = "img".equalsIgnoreCase(getElement().getTagName());
        return isButton || isInput || isImg;
    }
    
    @Override
    public final void validateElementTag() {
    	super.validateElementTag();
    	
    	boolean isInput = "input".equalsIgnoreCase(getElement().getTagName());
    	
    	if (isInput && getType() == null){
    		throw new IllegalArgumentException(ERROR_INVALID_INPUT_TYPE);
        }
    }

	@Override
	public String getBasicLocator() {
		return "button";
	}
	
	/**
	 * Returns the minimal locator for the SeleniumButton object. As the button has many ways to be
	 * represented in the HTML source code, this method returns the correct representations, giving
	 * to it the button type desired.
	 * @param type
	 * 		button type representation
	 * @return
	 * 		Returns a minimal xpath representation of the button on the browser's page.
	 */
	public String getBasicLocator(ButtonType type) {
		switch (type) {
			case IMAGE:
				return "image";
			case BUTTON:
				return "button";
			case RESET:
				return "input[ @type='reset' ]";
			case SUBMIT:
				return "input[ @type='submit' ]";
			default:
				return "";
		}
	}
}
