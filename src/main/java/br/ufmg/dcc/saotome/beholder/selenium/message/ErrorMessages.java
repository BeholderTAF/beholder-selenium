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
 * This interface contains all error messages launched by the system. 
 * @author icaroclever
 */
public interface ErrorMessages {

	String ERROR_DRIVER_IS_NULL="WebDriver cannot be null.";
	String ERROR_BROWSER_INVALID = "The browser % is invalid.";
	String ERROR_ELEMENT_IS_NULL="WebElement cannot be null.";
	String ERROR_LOCATOR_IS_NULL="Locator cannot be null.";
	String ERROR_CANNOT_RELOAD_ELEMENT = "This element cannot be reloaded because there aren't ID or NAME attributes.";
	String ERROR_ELEMENT_WAS_NOT_LOADED = "WebElement was not loaded.";
	String ERROR_TAGS_DIFFERENTS = "Element loaded is not of the same tag.";
	String ERROR_ATTRIBUTE_EMPTY="Attribute cannot be null or empty";
	String ERROR_VALUE_NULL="Value cannot be null";
	String ERROR_INVALID_TAG_TO_CLASS="The Element %s is not a valid element to the class";
	String ERROR_INDEX_NEGATIVE = "Index cannot be a negative number.";
	String ERROR_INDEX_NEGATIVE_ZERO = "Index cannot be negative or zero.";
	String ERROR_ELEMENTS_ATTRIBUTES_NOT_MATCH = "The element attributes is different of the current element attributes.";
	
	String ERROR_SCREENSHOT_IS_NULL = "Screenshot method getScreenshotAs(OutputType.FILE) returned null";
	String ERROR_FILE_NOT_FOUND_EXCEPTION = "The file %s cannot be found or cannot be written if it's a read-only file.";
	String ERROR_IO_EXCEPTION = "There was a failed or interrupted I/O operation.";
	
	String ERROR_FOLDER_CANNOT_BE_CREATED = "Folder cannot be created";
	
	String ERROR_UNKNOWN = "An unknown error happened in the Beholder Framework. Contact the tecnical suport.";
}
