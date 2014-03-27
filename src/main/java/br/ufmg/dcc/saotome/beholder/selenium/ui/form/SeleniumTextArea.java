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
import br.ufmg.dcc.saotome.beholder.ui.form.TextArea;

/**
 * TODO
 * @author icaroclever
 *
 */
public class SeleniumTextArea extends SeleniumComponent 
implements TextArea{

	public SeleniumTextArea(WebDriver driver) {
		super(driver);
	}

	@Override
	public boolean isValidElementTag() {
        return "textarea".equalsIgnoreCase(getElement().getTagName());
	}

	@Override
	public String getText() {
        if (getElement() == null) {
            return null;
        } else {
            return this.getElement().getText();
        }
	}

	@Override
	public void fill(String text) {
		this.getElement().clear();
		this.getElement().sendKeys(text);
	}

	@Override
	public String getBasicLocator() {
		return "textarea";
	}

	@Override
	public void click() {
		getElement().click();
	}

}
