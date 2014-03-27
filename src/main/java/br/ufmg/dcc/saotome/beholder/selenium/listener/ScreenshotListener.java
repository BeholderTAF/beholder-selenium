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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.reporters.Files;

import br.ufmg.dcc.saotome.beholder.selenium.message.ErrorMessages;

/**
 * This class listens tests that failed and take a PNG screenshot of the
 * browser's last state before throws the exception. The screenshot is saved in
 * the folder informed by screenshotFolder parameter passed by testng.xml file. If this
 * variable doesn't exist, the screenshot will be saved in the java application
 * base directory. The complete path of the screenshot is: -
 * TESTNG_SCREENSHOTS_DIR
 * /ClassName/TestName-yyyy'-'MM'-'dd'_'HH'h'mm'm'ss's'.png
 * 
 * OBS: When there aren't a focused window to the driver take the screenshot,
 * for example an alert opened, the listener doesn't work and an exception is
 * throwed on the log.
 * 
 * @author Ícaro Clever F. Braga (icaroclever@gmail.com)
 */
public class ScreenshotListener extends TestListenerAdapter {

	private static final String SCREENSHOT_FOLDER = "screenshotFolder";
	private final Logger logger = LogManager.getLogger(this.getClass());

	@Override
	public void onTestFailure(ITestResult tr) {

		if (tr != null) { // tr is null only for test purposes.
			super.onTestFailure(tr);
		}
		
		try {
			FileInputStream screenshot = new FileInputStream(
					((TakesScreenshot) Listener.getDriver())
							.getScreenshotAs(OutputType.FILE)); // Take the
																// screenshot
			File screenshotFile = createFile(tr, createFolder(tr));
			Files.copyFile(screenshot, screenshotFile); // Copy the screenshot
														// to a repository
														// folder
		} catch (FileNotFoundException e) {
			this.logger.error(ErrorMessages.ERROR_FILE_NOT_FOUND_EXCEPTION, e);
		} catch (IOException e) {
			this.logger.error(ErrorMessages.ERROR_IO_EXCEPTION, e);
		} catch (Exception e) {
			this.logger.error(ErrorMessages.ERROR_UNKNOWN, e);
		}
	}

	/** Returns a date suffix string
	 *	@return String  
	 */
	private String getDateSuffix() {
		
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH); // 0 to 11
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);

		return String.format("-%4d-%02d-%02d_%02dh%02dm%02ds", year,
				(month + 1), day, hour, minute, second);
	}

	/**
	 * Generate, if it doesn't exist, the folder for screenshots taken. The user can pass this information by parameter in the
	 * testng.xml. If it's not passed this information, the folder is created in the current location with the name 'screenshots'.
	 * @param tr	Test Result
	 * @return	Screenshot Folder
	 * @throws IOException 
	 */
	private File createFolder(ITestResult tr) throws IOException{
		
		String path = Listener.getParameter(SCREENSHOT_FOLDER);

		if (path == null || path.isEmpty()) {
			path = "." + File.separatorChar + "screenshots";
		}

		if(tr != null) {
			path += File.separatorChar + tr.getMethod().getTestClass().getName();
		}

		File screenshotFolder = new File(path);
		if (!screenshotFolder.exists() && !screenshotFolder.mkdirs()){
			throw new IOException(ErrorMessages.ERROR_FOLDER_CANNOT_BE_CREATED);
		}
		
		return screenshotFolder;
	}
	
	/** Generate the file to save the screenshot taken.
	 * 
	 * @param tr	Test Result
	 * @param parentFolder	Screenshot Folder
	 * @return	Screenshot Folder
	 */
	private File createFile(ITestResult tr, File parentFolder) {

		String path;
				
		if (tr != null) { // tr is null only on tests purpose
			path = String.format("%s%c%s.png", parentFolder.getAbsolutePath(), File.separatorChar, tr.getName(), getDateSuffix());
		} else {
			path = String.format("%s%ctest_%d.png", parentFolder.getAbsolutePath(), File.separatorChar, System.currentTimeMillis());
		}
		
		return new File(path);
	}
}
