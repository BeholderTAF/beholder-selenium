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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.WebDriver;

import br.ufmg.dcc.saotome.beholder.selenium.ui.SeleniumComponent;
import br.ufmg.dcc.saotome.beholder.selenium.ui.table.SeleniumCell.CellType;
import br.ufmg.dcc.saotome.beholder.ui.table.Cell;
import br.ufmg.dcc.saotome.beholder.ui.table.Cell.Coordinate;
import br.ufmg.dcc.saotome.beholder.ui.table.Table;

/** The class SeleniumTable implements the Table interface. A table component is a set of cells 
 * organized by lines and columns. The objective of this class is making easy the user to recover
 * the information of this cells. 
 * @author icaroclever
 */
public class SeleniumTable extends SeleniumComponent implements Table{

	public SeleniumTable(WebDriver driver) {
		super(driver);
	}

	@Override
	public Cell getHeadCell(Integer line, Integer column) {
		return new SeleniumCell(this.getElement(), line, column,CellType.HEAD);
	}

	@Override
	public Cell getHeadCell(Coordinate coordinate) {
		return new SeleniumCell(this.getElement(), coordinate,CellType.HEAD);
	}

	@Override
	public Cell getCell(Integer line, Integer column) {
		return new SeleniumCell(this.getElement(), line, column,CellType.BODY);
	}

	@Override
	public Cell getCell(Coordinate coordinate) {
		return new SeleniumCell(this.getElement(), coordinate,CellType.BODY);
	}

	@Override
	public boolean isValidElementTag() {
		return "table".equalsIgnoreCase(getElement().getTagName());
	}

	@Override
	public String getBasicLocator() {
		return "table";
	}
	
	
	/**
	 * Generic method to take line cells of a tbody, thead or tfoot structure and returns as a List<List<Cell>>.
	 * @param type
	 * @return List<List<Cell>>
	 */
	private List<List<Cell>> getAllCellsByLine(CellType type) {
	
		String locatorLines="", locatorColumn="";
		
		switch(type){
			case HEAD: 	locatorLines = ".//thead/tr";
						locatorColumn = ".//th";
						break;
			case BODY: 	locatorLines = ".//tbody/tr";
						locatorColumn = ".//td";
						break;
			case FOOT: 	locatorLines = ".//tfoot/tr";
						locatorColumn = ".//td";
						break;
		}
		
		List<WebElement> lines = this.getElement().findElements(By.xpath(locatorLines)); // Recover all lines from the table
		
		List<List<Cell>> cellLines = new ArrayList<List<Cell>>(); // List of lines of the table
		
		Integer lineIndex = 1;
		
		for (WebElement line : lines) { // iterate the lines taken from table
			
			List<Cell> cellColumns = new ArrayList<Cell>();
			List<WebElement> columns = line.findElements(By.xpath(locatorColumn)); // Recover all columns from the lines
			Integer columnIndex = 1;
			
			for (WebElement column : columns) { // iterate the columns taken from the line
				Cell cell = new SeleniumCell(column);
				cell.setCoordinates(lineIndex, columnIndex);
				cellColumns.add(cell);
				columnIndex++;
			} // end of columns iteration
			
			cellLines.add(cellColumns);
			lineIndex++;
		}// end of lines iteration
		
		return cellLines;
	}

	private List<List<Cell>> getAllCellsByColumn(CellType type) {
		
		final Integer splitFactor = 100000; 
		
		List<List<Cell>> orderByLines = getAllCellsByLine(type);

		Map<Integer, Cell> plainList = new HashMap<Integer, Cell>();
		List<Integer> keys = new ArrayList<Integer>(); 
		
		Integer lineIndex = 1;
		Integer columnIndex;
		for (List<Cell> line : orderByLines) { // iterates on the lines
			columnIndex = 1;
			for (Cell cell : line) { // iterates on the columns
				Integer coordinates = (columnIndex * splitFactor) + lineIndex;
				plainList.put(coordinates, cell);
				keys.add(coordinates);
				columnIndex++;
			}// end of column iteration
			lineIndex++;
		}// end of line iteration
		
		Collections.sort(keys);// sort the keys organizing the collection by column.
		
		Integer currentColumn = 1; // variable to hold the current column index 
		
		List<List<Cell>> lines = new ArrayList<List<Cell>>();
		List<Cell> columns = new ArrayList<Cell>();
		
		
		for (Integer coordinate : keys) { // generate list loop
			lineIndex = coordinate % splitFactor;			
			columnIndex = coordinate / splitFactor;
			
			if (!currentColumn.equals(columnIndex)) {
				currentColumn=columnIndex;
				lines.add(columns);
				columns = new ArrayList<Cell>();
			}
			
			Cell cell = plainList.get(coordinate);
			cell.setCoordinates(lineIndex, columnIndex);
			columns.add(cell);
		} // end of generate list loop
		lines.add(columns); // insert the last one column
		
		return lines;
	}
	
	@Override
	public List<List<Cell>> getAllCellsByLine() {
		return getAllCellsByLine(CellType.BODY);
	}
	
	@Override
	public List<List<Cell>> getAllCellsByColumn() {
		return getAllCellsByColumn(CellType.BODY);
	}
	
	@Override
	public List<List<Cell>> getAllHeadCellsByLine() {
		return getAllCellsByLine(CellType.HEAD);
	}
	
	@Override
	public List<List<Cell>> getAllHeadCellsByColumn() {
		return getAllCellsByColumn(CellType.HEAD);
	}
	
	@Override
	public List<List<Cell>> getAllFootCellsByLine() {
		return getAllCellsByLine(CellType.FOOT);
	}
	
	@Override
	public List<List<Cell>> getAllFootCellsByColumn() {
		return getAllCellsByColumn(CellType.FOOT);
	}
	
	@Override
	public List<Cell> getHeadLineCells(Integer index) {
		return getAllHeadCellsByLine().get(index-1);
	}

	@Override
	public List<Cell> getHeadColumnCells(Integer index) {
		return getAllHeadCellsByColumn().get(index-1);
	}

	@Override
	public List<Cell> getFootLineCells(Integer index) {
		return getAllFootCellsByLine().get(index-1);
	}

	@Override
	public List<Cell> getFootColumnCells(Integer index) {
		return getAllFootCellsByColumn().get(index-1);
	}
	
	@Override
	public List<Cell> getLineCells(Integer index) {		
		return getAllCellsByLine().get(index-1);
	}

	@Override
	public List<Cell> getColumnCells(Integer index) {
		return getAllCellsByColumn().get(index-1);
	}

	@Override
	public void click() {
		getElement().click();
	}
}
