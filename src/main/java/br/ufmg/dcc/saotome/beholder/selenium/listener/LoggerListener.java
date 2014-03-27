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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

/** This class listens when a test has been sucessful, failed or skipped, and prints 
 * in console or log file which test has been executed and its status.
 * 
 * @author Ícaro Clever F. Braga (icaroclever@gmail.com)
 */
public class LoggerListener  extends TestListenerAdapter {
	
	@Override
	public void onTestSuccess(ITestResult tr) {
		super.onTestSuccess(tr);
		String methodName = tr.getName();
		Logger logger = LogManager.getLogger(tr.getTestClass().getName());
		String message = String.format("Method: %s - SUCCEDED",methodName);
		logger.info(message);
	}
	
	@Override
	public void onTestFailure(ITestResult tr) {
		super.onTestFailure(tr);
		String methodName = tr.getName();
		Logger logger = LogManager.getLogger(tr.getTestClass().getName());
		String message = String.format("Method: %s - FAILED",methodName);
		logger.error(message);
	}
	
	@Override
	public void onTestSkipped(ITestResult tr) {
		super.onTestSkipped(tr);
		String methodName = tr.getName();
		Logger logger = LogManager.getLogger(tr.getTestClass().getName());
		String message = String.format("Method: %s - SKIPPED",methodName);
		logger.info(message);
	}
}
