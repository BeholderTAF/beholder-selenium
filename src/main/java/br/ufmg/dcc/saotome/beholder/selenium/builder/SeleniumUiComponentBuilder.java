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

import br.ufmg.dcc.saotome.beholder.builder.UiComponentBuilder;
import br.ufmg.dcc.saotome.beholder.selenium.ui.SeleniumDiv;
import br.ufmg.dcc.saotome.beholder.selenium.ui.SeleniumGenericComponent;
import br.ufmg.dcc.saotome.beholder.selenium.ui.SeleniumLink;
import br.ufmg.dcc.saotome.beholder.selenium.ui.SeleniumIFrame;
import br.ufmg.dcc.saotome.beholder.selenium.ui.form.SeleniumButton;
import br.ufmg.dcc.saotome.beholder.selenium.ui.form.SeleniumCheckBoxField;
import br.ufmg.dcc.saotome.beholder.selenium.ui.form.SeleniumRadioButton;
import br.ufmg.dcc.saotome.beholder.selenium.ui.form.SeleniumSelectField;
import br.ufmg.dcc.saotome.beholder.selenium.ui.form.SeleniumTextArea;
import br.ufmg.dcc.saotome.beholder.selenium.ui.form.SeleniumTextField;
import br.ufmg.dcc.saotome.beholder.selenium.ui.table.SeleniumTable;
import br.ufmg.dcc.saotome.beholder.ui.Div;
import br.ufmg.dcc.saotome.beholder.ui.GenericComponent;
import br.ufmg.dcc.saotome.beholder.ui.Link;
import br.ufmg.dcc.saotome.beholder.ui.IFrame;
import br.ufmg.dcc.saotome.beholder.ui.form.Button;
import br.ufmg.dcc.saotome.beholder.ui.form.Checkbox;
import br.ufmg.dcc.saotome.beholder.ui.form.RadioButton;
import br.ufmg.dcc.saotome.beholder.ui.form.Select;
import br.ufmg.dcc.saotome.beholder.ui.form.TextArea;
import br.ufmg.dcc.saotome.beholder.ui.form.TextField;
import br.ufmg.dcc.saotome.beholder.ui.table.Table;

/**
 * TODO
 * @author icaroclever
 *
 */
public class SeleniumUiComponentBuilder implements UiComponentBuilder{
	
	SeleniumUiComponentBuilder() {
		// Hidden of outside package classes
	}

    @Override
    public final Button buttonInstance() {
        return new SeleniumButton(SeleniumBuilder.getDriver());
    }

    @Override
    public final TextField textFieldInstance() {
        return new SeleniumTextField(SeleniumBuilder.getDriver());
    }

    @Override
    public final Checkbox checkboxInstance() {
        return new SeleniumCheckBoxField(SeleniumBuilder.getDriver());
    }

    @Override
    public final RadioButton radioButtonInstance() {
        return new SeleniumRadioButton(SeleniumBuilder.getDriver());
    }

    @Override
    public final Select selectFieldInstance() {
        return new SeleniumSelectField(SeleniumBuilder.getDriver());
    }

    @Override
    public Link linkInstance() {
    	return new SeleniumLink(SeleniumBuilder.getDriver());
    }

	@Override
	public GenericComponent genericComponentInstance() {
		return new SeleniumGenericComponent(SeleniumBuilder.getDriver());
	}

	@Override
	public TextArea textAreaInstance() {
		return new SeleniumTextArea(SeleniumBuilder.getDriver());
	}

	@Override
	public IFrame iFrameInstance() {
		return new SeleniumIFrame(SeleniumBuilder.getDriver());
	}

	@Override
	public Table tableInstance() {
		return new SeleniumTable(SeleniumBuilder.getDriver());
	}

	@Override
	public Div divInterface() {
		return new SeleniumDiv(SeleniumBuilder.getDriver());
	}
}
