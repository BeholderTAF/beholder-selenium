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
package br.ufmg.dcc.saotome.beholder.selenium;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import br.ufmg.dcc.saotome.beholder.Browser;
import br.ufmg.dcc.saotome.beholder.builder.Builder;
import br.ufmg.dcc.saotome.beholder.selenium.listener.Listener;
import br.ufmg.dcc.saotome.beholder.selenium.listener.ScreenshotListener;
import br.ufmg.dcc.saotome.beholder.ui.Div;
import br.ufmg.dcc.saotome.beholder.ui.IFrame;
import br.ufmg.dcc.saotome.beholder.ui.form.Button;
import br.ufmg.dcc.saotome.beholder.ui.form.TextField;
import br.ufmg.dcc.saotome.beholder.ui.table.Cell;
import br.ufmg.dcc.saotome.beholder.ui.table.Table;

public class BasicTests {

	private static URL PROTOTYPE_TABLE_PATH = BasicTests.class.getClassLoader()
			.getResource("prototype_table.html");
	private static URL PROTOTYPE_TABLE_TAB_PATH = BasicTests.class.getClassLoader()
			.getResource("prototype_table_tab.html");
	private static URL PROTOTYPE_IFRAME_PATH = BasicTests.class
			.getClassLoader().getResource("prototype_iframe.html");
	private static URL PROTOTYPE_PATH = BasicTests.class.getClassLoader()
			.getResource("prototype.html");
	private Browser browser;
	private Builder builder;

	@BeforeTest
	public void setUp() {
		this.browser = SeleniumController.getBrowser();
		this.builder = SeleniumController.getBuilder();
	}

	/**
	 * Verify if Beholder-Selenium is interacting correctly with an alert when
	 * it's a confirm alert (canceling the operation or confirming) and a
	 * information alert.
	 */
	@Test
	public void verifyAlerts() {

		browser.open(PROTOTYPE_PATH);
		browser.getAlert().confirm();
		assert browser.getAlert().getText().equals("Confirmado");
		browser.getAlert().confirm();
		browser.open(PROTOTYPE_PATH);
		browser.getAlert().cancel();
		assert browser.getAlert().getText().equals("Cancelado");
		browser.getAlert().confirm();
	}

	/**
	 * Verify if a Modal is loading only after the method display() was called.
	 * Also verify others Displayable interface methods.
	 */
	@Test
	public void verifyDisplayableModal() {

		browser.open(PROTOTYPE_IFRAME_PATH);
		// Main Interface
		IFrame frame = builder.uiComponentBuilderInstance().iFrameInstance();
		frame.loadById("iframeId");
		Button button = builder.uiComponentBuilderInstance().buttonInstance();
		button.loadById("buttonId");
		// Child Frame Interface
		TextField textFieldChild = builder.uiComponentBuilderInstance()
				.textFieldInstance();
		textFieldChild.hide();
		textFieldChild.loadById("textFieldChild");
		Button childButton = builder.uiComponentBuilderInstance()
				.buttonInstance();
		childButton.hide();
		childButton.loadById("buttonId_child");
		IFrame childFrame = builder.uiComponentBuilderInstance().iFrameInstance();
		childFrame.loadById("iframeId_child");
		frame.addComponent("childFrame", childFrame);
		frame.addComponent("childButton", childButton);
		frame.addComponent("textFieldChild", textFieldChild);
		// Grandchild Frame Interface

		button.click();
		frame.show();
		frame.<TextField> getComponent("textFieldChild").fill("blablabla");
		frame.<Button> getComponent("childButton").click();
		frame.<IFrame> getComponent("childFrame").show();
		assert browser.isTextPresent("Grandchild iFrame opened!");
		frame.<IFrame> getComponent("childFrame").hide();
		frame.<Button> getComponent("childButton").click();
		assert browser.isTextPresent("iFrame opened!");
		frame.hide();
		button.click();
	}

	@Test
	public void verifyScreenshot() throws IOException {

		Listener.setWebDriver(SeleniumController.getDriver());
		ScreenshotListener listener = new ScreenshotListener();

		browser.open(PROTOTYPE_PATH);
		listener.onTestFailure(null);
		browser.getAlert().confirm();

		browser.open(PROTOTYPE_IFRAME_PATH);
		listener.onTestFailure(null);

		IFrame frame = builder.uiComponentBuilderInstance().iFrameInstance();
		frame.loadById("iframeId");
		Button button = builder.uiComponentBuilderInstance().buttonInstance();
		button.loadById("buttonId");
		button.click();
		frame.show();
		listener.onTestFailure(null);
	}

	@Test
	public void verifyTableByLine() {
		browser.open(PROTOTYPE_TABLE_PATH);
		Table table = builder.uiComponentBuilderInstance().tableInstance();
		table.loadById("tableId");
		
		// verify head columns
		Integer lindex = 0;
		for (List<Cell> line : table.getAllHeadCellsByLine()) {
			Integer cindex = 0;
			lindex++;
			for (Cell cell : line) {
				assert String.format("head %d", ++cindex).contains(cell.getText());
			}
		}
		
		// verify body columns
		lindex = 0;
		for (List<Cell> line : table.getAllCellsByLine()) {
			Integer cindex = 0;
			lindex++;
			for (Cell cell : line) {
				assert String.format("data %d,%d", lindex, ++cindex).contains(cell.getText());
			}
		}
		
		// verify foot columns
		lindex = 0;
		for (List<Cell> line : table.getAllFootCellsByLine()) {
			Integer cindex = 0;
			lindex++;
			for (Cell cell : line) {
				assert String.format("foot %d", ++cindex).contains(cell.getText());
			}
		}
	}

	@Test
	public void verifyTableByColumn() {
		browser.open(PROTOTYPE_TABLE_PATH);
		Table table = builder.uiComponentBuilderInstance().tableInstance();
		table.loadById("tableId");
		Integer cindex;

		// verify head columns
		cindex = 1;
		for (List<Cell> column : table.getAllHeadCellsByColumn()) {
			Integer lindex = 1;
			for (Cell cell : column) {
				assert String.format("head %d", cindex).contains(cell.getText());
				lindex++;
			}
			cindex++;
		}

		// verify body columns
		cindex = 1;
		for (List<Cell> column : table.getAllCellsByColumn()) {
			Integer lindex = 1;
			for (Cell cell : column) {
				assert String.format("data %d,%d", lindex, cindex).contains(cell.getText());
				lindex++;
			}
			cindex++;
		}

		// verify foot columns
		cindex = 1;
		for (List<Cell> column : table.getAllFootCellsByColumn()) {
			Integer lindex = 1;
			for (Cell cell : column) {
				assert String.format("foot %d", cindex).contains(cell.getText());
				lindex++;
			}
			cindex++;
		}
	}
	
	@Test
	public void verifyCellClick(){
		browser.open(PROTOTYPE_TABLE_TAB_PATH);
		Table table = builder.uiComponentBuilderInstance().tableInstance();
		table.loadById("tableId");
		Div div = builder.uiComponentBuilderInstance().divInterface();
		div.hide();
		div.loadById("divId");
		List<Cell> line = table.getLineCells(1);
		line.get(0).click();
		div.show();
		assert div.getText().contains("tab1");
		
	}
}
