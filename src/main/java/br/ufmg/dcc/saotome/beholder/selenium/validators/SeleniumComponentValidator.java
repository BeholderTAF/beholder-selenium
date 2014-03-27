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
package br.ufmg.dcc.saotome.beholder.selenium.validators;

import br.ufmg.dcc.saotome.beholder.selenium.ui.SeleniumComponent;
import br.ufmg.dcc.saotome.beholder.ui.Component;
import br.ufmg.dcc.saotome.beholder.validators.ComponentValidator;

/**
 * TODO
 * @author icaroclever
 *
 */
public class SeleniumComponentValidator implements ComponentValidator {

	private static final String MESSAGE_ELEMENT_NOT_EXIST = "Elemento nulo.";
	private static final String MESSAGE_ATTRIBUTE_NOT_EXIST = "Atributo nulo ou vazio.";
	private static final String MESSAGE_VALUE_NOT_EXIST = "Valor nulo.";
	
	@Override
	public boolean isEqual(Component component, String attribute, String value) {
		
		boolean 	existAttribute = attribute != null && !attribute.isEmpty(),
						existValue = value != null,
						existElement = component != null && ((SeleniumComponent)component).getElement() != null; 
		
		if(existElement && existAttribute && existValue) {
			return ((SeleniumComponent)component).getElement().getAttribute(attribute).equals(value);
		} else {
			if (!existElement){
				throw new IllegalArgumentException(MESSAGE_ELEMENT_NOT_EXIST);
			}else if (!existAttribute){
				throw new IllegalArgumentException(MESSAGE_ATTRIBUTE_NOT_EXIST);
			}else {
				throw new IllegalArgumentException(MESSAGE_VALUE_NOT_EXIST);
			}
		}
	}

	@Override
	public boolean contains(Component component, String attribute, String value) {
				
		boolean 	existAttribute = attribute != null && !attribute.isEmpty(),
						existValue = value != null,
						existElement = component != null && ((SeleniumComponent)component).getElement() != null; 		
		
		if(existElement && existAttribute && existValue) {
			return ((SeleniumComponent)component).getElement().getAttribute(attribute).contains(value);
		} else {
			if (!existElement){
				throw new IllegalArgumentException(MESSAGE_ELEMENT_NOT_EXIST);
			}else if (!existAttribute){
				throw new IllegalArgumentException(MESSAGE_ATTRIBUTE_NOT_EXIST);
			}else {
				throw new IllegalArgumentException(MESSAGE_VALUE_NOT_EXIST);
			}
		}
	}

	@Override
	public boolean exist(Component component) {
		//REPENSAR: Alterar para isDisplayed?
		return false;
	}

	@Override
	public boolean isEnabled(Component component) {
		if(component != null && ((SeleniumComponent)component).getElement() != null) {		
			return ((SeleniumComponent)component).getElement().isEnabled();
		} else {
			throw new IllegalArgumentException(MESSAGE_ELEMENT_NOT_EXIST);
		}
	}

}
