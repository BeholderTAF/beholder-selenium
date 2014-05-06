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
package br.ufmg.dcc.saotome.beholder.selenium.listener;

import java.util.Map;

import org.openqa.selenium.WebDriver;

/**
 * This class holds parameters and variables commonly used in all system. 
 * @author icaroclever
 */
public final class ListenerGateway {

	private static WebDriver driver;
	
	private static Map<String,String> parameters;
	
	/**
	 * @return the parameters
	 */
	static String getParameter(String parameter) {
		return parameters.get(parameter);
	}

	/**
	 * @param parameters the parameters to set
	 */
	public static void setParameters(Map<String, String> parameters) {
		ListenerGateway.parameters = parameters;
	}

	private ListenerGateway() {
	}

	/**
	 * Getter to return  a browser driver
	 * @return the browser driver
	 */
	static WebDriver getDriver() {
		return driver;
	}
	
	/**
	 * Setter to browser driver
	 * @param driver Browser driver
	 */
	public static void setWebDriver(final WebDriver driver) {
		ListenerGateway.driver = driver;
	}
}
