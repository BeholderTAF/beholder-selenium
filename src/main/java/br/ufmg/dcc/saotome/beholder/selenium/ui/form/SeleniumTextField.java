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

import br.ufmg.dcc.saotome.beholder.selenium.message.ErrorMessages;
import br.ufmg.dcc.saotome.beholder.selenium.ui.SeleniumComponent;
import br.ufmg.dcc.saotome.beholder.ui.form.TextField;

/**
 * This abstract class implements the interface TextField using the
 * Selenium-Webdriver to simulate the interaction between the component
 * and a system user.
 *
 * @author Ícaro Clever F. Braga (icaroclever@gmail.com)
 * @see SeleniumComponent
 * @see TextField
 *
 */
public class SeleniumTextField extends SeleniumComponent
implements TextField {
	
	public static final String VALID_TYPES = "text,password,number,color,date,datetime,datetime-local,email,month,number,range,search,tel,time,url,week";

    public SeleniumTextField(WebDriver driver) {
		super(driver);
	}

	@Override
    public final String getValue() {
        if (getElement() == null) {
            return null;
        } else {
            return this.getElement().getAttribute("value");
        }
    }

    @Override
    public final void fill(final String value) {
    	this.getElement().clear();
        this.getElement().sendKeys(value);

    }

    @Override
    public final boolean isValidElementTag() {
        return "input".equalsIgnoreCase(getElement().getTagName());
    }
    
    @Override
    public final void validateElementTag() {
    	super.validateElementTag();

        if(isValidInputType(getAttribute("type"))){
        	throw new IllegalArgumentException(String.format(ErrorMessages.ERROR_INVALID_INPUT_TYPE,getAttribute("type"),VALID_TYPES));
    	}
    }

	@Override
	public String getBasicLocator() {
		return "input[@type='"+getAttribute("type")+"text']";
	}

	@Override
	public void click() {
		getElement().click();
	}
	
	/**
	 * Verify if the input type of the loaded element is valid as a TextField object.
	 * @param type Html input type
	 * @return Returns true if type is:
	 * <ul>
	 * <li> text </li>
	 * <li> password</li>
	 * <li> number</li>
	 * <li> color</li>
	 * <li> date</li>
	 * <li> datetime</li>
	 * <li> datetime-local</li>
	 * <li> email</li>
	 * <li> month,number</li>
	 * <li> range</li>
	 * <li> search</li>
	 * <li> tel</li>
	 * <li> time</li>
	 * <li> url</li>
	 * </ul>
	 * <p>Otherwise returns false.</p>
	 */
	public Boolean isValidInputType(final String type){
		
		String[] listValidTypes = VALID_TYPES.split(",");
		Boolean isValid = false;
		for (Integer counter = 0; counter < listValidTypes.length && isValid; counter++){
			isValid = listValidTypes[counter].equalsIgnoreCase(type);
		}
		return isValid;
	}
}
