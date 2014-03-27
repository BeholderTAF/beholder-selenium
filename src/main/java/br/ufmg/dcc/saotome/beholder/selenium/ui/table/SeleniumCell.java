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
package br.ufmg.dcc.saotome.beholder.selenium.ui.table;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import br.ufmg.dcc.saotome.beholder.selenium.message.ErrorMessages;
import br.ufmg.dcc.saotome.beholder.ui.table.Cell;

/** 
 * Implements Cell Interface.
 * @author Ícaro Clever F. Braga (icaroclever@gmail.com)
 * @see Cell
 * @version 1.0
 */
public class SeleniumCell implements Cell{
	
	private WebElement cell;
	private Coordinate coordinate;
	
	public enum CellType {
		HEAD,
		BODY,
		FOOT
	}
	
	
	/** 
	 * Recover a cell from a given table by its coordinates.
	 * @param table Table element 
	 * @param coordinate coordinates of cell in the table
	 */
	public SeleniumCell(final WebElement table,final Coordinate coordinate, final CellType type) {
		
		validateCoordinate(coordinate);
		
		String xpath="";
		
		switch (type){
			case HEAD: 	xpath =  ".//thead/tr[%d]/th[%d]";
						break;
			case BODY: 	xpath =  ".//tbody/tr[%d]/td[%d]";
						break;
			case FOOT:	xpath =  ".//tfoot/tr[%d]/td[%d]";
						break;
		}
		
		String locator = String.format(xpath,coordinate.getLineIndex(),coordinate.getColumnIndex());
		
		this.cell = table.findElement(By.xpath(locator));		
	}
	
	/** 
	 * Recover a cell from a given table by its coordinates.
	 * @param lineIndex
	 * 		line index cell location (1 is the first line)
	 * @param columnIndex
	 * 		column index cell location (1 is the first column)
	 */
	public SeleniumCell(WebElement table, Integer lineIndex, Integer columnIndex, CellType type){
		this(table,new Coordinate(lineIndex, columnIndex),type);
	}
	
	/** Constructor of a cell. Cells to exist must have a coodinate to find it.
	 * @param cell Selenium WebElement object 
	 * 		
	 */
	public SeleniumCell(WebElement cell){
		this.cell = cell;
	}
	
	@Override
	public Coordinate getCoordinate() {
		return this.coordinate;
	}

	@Override
	public void setCoordinates(Integer lineIndex, Integer columnIndex) {
		this.setCoordinates(new Coordinate(lineIndex, columnIndex));
	}

	@Override
	public void setCoordinates(Coordinate coordinate) {
		validateCoordinate(coordinate);
		this.coordinate = coordinate;
	}
	
	private void validateCoordinate(final Coordinate coordinate) {
		if(coordinate.getLineIndex() < 1) {
			throw new IndexOutOfBoundsException("Line "+ErrorMessages.ERROR_INDEX_NEGATIVE_ZERO);
		}
		if(coordinate.getColumnIndex() < 1) {
			throw new IndexOutOfBoundsException("Column "+ErrorMessages.ERROR_INDEX_NEGATIVE_ZERO);
		}
	}

	@Override
	public String getText() {
		return this.cell.getText();
	}

	@Override
	public void click() {
		this.cell.click();
	}
}
