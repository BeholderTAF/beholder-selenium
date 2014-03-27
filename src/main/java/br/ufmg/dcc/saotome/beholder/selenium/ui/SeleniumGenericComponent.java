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

import org.openqa.selenium.WebDriver;

import br.ufmg.dcc.saotome.beholder.ui.Component;
import br.ufmg.dcc.saotome.beholder.ui.GenericComponent;

/**
 * TODO
 * @author icaroclever
 *
 */
public class SeleniumGenericComponent extends SeleniumComponent implements
		GenericComponent {

	public SeleniumGenericComponent(WebDriver driver) {
		super(driver);
	}

	@Override
	public void click() {
		this.getElement().click();
	}
	
	@Override
	public boolean isValidElementTag() {
		boolean isInput = "input".equalsIgnoreCase(getElement().getTagName()) && !getElement().getAttribute("type").equalsIgnoreCase("hidden"),
				 isSelect = "select".equalsIgnoreCase(getElement().getTagName()),
				 isLink = "a".equalsIgnoreCase(getElement().getTagName()),
				 isButton = "button".equalsIgnoreCase(getElement().getTagName());
		
		return !isInput && !isSelect && !isLink && !isButton;
	}

	@Override
	public String getBasicLocator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addComponent(String locator, Component component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeComponent(String locator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T extends Component> T getComponent(String locator) {
		// TODO Auto-generated method stub
		return null;
	}

}
