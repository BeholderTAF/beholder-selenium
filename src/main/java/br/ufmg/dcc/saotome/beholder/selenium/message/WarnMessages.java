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
package br.ufmg.dcc.saotome.beholder.selenium.message;

/**
 * This interface contains all warning messages launched by the system. 
 * @author icaroclever
 *
 */
public interface WarnMessages {

	String WARN_LONG_SIZE_SCREENSHOT = "The system cannot take a screenshot because the " +
			"dimensions of the PNG file is greater than maximum allowed. The problem usually happens when" +
			"there are to much information in the webpage.";
}
