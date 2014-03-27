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
import java.util.Map;
import java.util.Stack;

import org.openqa.selenium.WebDriver;

import br.ufmg.dcc.saotome.beholder.selenium.message.ErrorMessages;
import br.ufmg.dcc.saotome.beholder.ui.Component;
import br.ufmg.dcc.saotome.beholder.ui.IFrame;

/**
 * This class implements the Modal interface. This class works a little different of
 * the others components because Modals are loaded after the page loading. So, if 
 * you try to load a modal during the page loading, probably a miss component error
 * will happen. So, a page is only loaded if the display mode is enabled. If a 
 * loadBySomething is called with the display disabled, the informations will be
 * saved. When the display() method is called, the informations saved will be 
 * loaded by the last loadBySomething called.
 * @author icaroclever
 *
 */
public class SeleniumIFrame extends SeleniumComponent implements IFrame {
	
	private Map<String,Component> components = new HashMap<String, Component>();
	
	private static Stack<Integer> stack;
	
	static{
		stack = new Stack<Integer>();
	}
	private int frameDeep = 1;
		
	public SeleniumIFrame(WebDriver driver) {
		super(driver);
		super.hide();
	}

	private Object testInterface; 
	
	@Override
	public boolean isValidElementTag() {
		
		if(this.isDisplayed()){
			Boolean isIFrame = "iframe".equalsIgnoreCase(getElement().getTagName());
			Boolean isFrame = "frame".equalsIgnoreCase(getElement().getTagName());
			return isIFrame || isFrame;	
		}
		return true;
	}

	// I haven't found a better solution to solve the problem.
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getInterface() {
		return (T)this.testInterface;
	}

	@Override
	public <T> void setInterface(T testInterface) {
		this.testInterface = testInterface;
	}

	@Override
	public String getBasicLocator() {
		return "iframe";
	}

	@Override
	public void show() {
		
		super.show();
		stack.push(frameDeep-1);
		
		if(this.getElement() == null){
			throw new IllegalArgumentException(ErrorMessages.ERROR_ELEMENT_IS_NULL);
		}
		
		this.getSeleniumWebDriver().switchTo().frame(this.getElement());
	}

	@Override
	public void hide() {
		super.hide();
		
		if(getParent() == null){
			this.getSeleniumWebDriver().switchTo().defaultContent();
		}
		else {
			this.getParent().hide();
			this.getParent().show();
		}
	}

	@Override
	public void addComponent(String locator, Component component) {
		
		if(component instanceof SeleniumIFrame){
			SeleniumIFrame frame = (SeleniumIFrame)component;
			frame.setParent(this);
			frame.setFrameDeep(this.frameDeep+1);
		}
		components.put(locator, component);	
	}

	@Override
	public void removeComponent(String locator) {
		components.remove(locator);	
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Component> T getComponent(String locator) {
		if(isDisplayed()){
			T component = (T)components.get(locator);
			if(!(component instanceof IFrame)){
				component.show();
			}
			return component;
		} 
		return null;
	}

	/**
	 * @return the frameDeep
	 */
	int getFrameDeep() {
		return frameDeep;
	}

	/**
	 * @param frameDeep the frameDeep to set
	 */
	void setFrameDeep(int frameDeep) {
		this.frameDeep = frameDeep;
	}
	
}
