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
package br.ufmg.dcc.saotome.beholder.selenium.builder;

import org.openqa.selenium.WebDriver;

import br.ufmg.dcc.saotome.beholder.builder.Builder;
import br.ufmg.dcc.saotome.beholder.builder.UiComponentBuilder;
import br.ufmg.dcc.saotome.beholder.builder.ValidatorBuilder;


/**
 * This class is a implementation of Builder interface to create
 * all the objects used to execute a test case. All objects
 * uses Selenium-Webdriver to find the component and interact with a
 * web browser, simulating actions of a system user.
 *
 * @author Ícaro Clever F. Braga (icaroclever@gmail.com)
 * @see Builder
 */
public class SeleniumBuilder implements Builder {
	
	/** Browser driver that contains the application component search. */
	private static WebDriver driver;
	
	/**
	 * @return the driver
	 */
	static WebDriver getDriver() {
		return driver;
	}

	public SeleniumBuilder(WebDriver driver) {
		SeleniumBuilder.driver = driver;
	}

    @Override
    public UiComponentBuilder uiComponentBuilderInstance() {
        return new SeleniumUiComponentBuilder();
    }

    @Override
    public ValidatorBuilder validatorBuilderInstance() {
        return new SeleniumValidatorBuilder();
    }
}
