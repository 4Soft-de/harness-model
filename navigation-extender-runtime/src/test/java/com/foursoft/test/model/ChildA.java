/*-
 * ========================LICENSE_START=================================
 * navigation-extender-runtime
 * %%
 * Copyright (C) 2019 - 2020 4Soft GmbH
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * =========================LICENSE_END==================================
 */
package com.foursoft.test.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.foursoft.xml.annotations.XmlBackReference;
import com.foursoft.xml.annotations.XmlParent;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ChildA", propOrder = { "anotherAttribute", "referencedChildB" })
public class ChildA extends AbstractBase implements Serializable {

	private final static long serialVersionUID = 1L;

	@XmlElement(name = "anotherAttribute", required = true)
	protected String anotherAttribute;

	@XmlList
	@XmlElement(name = "ReferencedChildB", type = java.lang.Object.class)
	@XmlIDREF
	@XmlSchemaType(name = "IDREFS")
	@XmlBackReference(destinationField = "reverseChildA")
	protected List<ChildB> referencedChildB;
	@XmlTransient
	@XmlParent
	private Root root;

	public String getAnotherAttribute() {
		return anotherAttribute;
	}

	public void setAnotherAttribute(final String anotherAttribute) {
		this.anotherAttribute = anotherAttribute;
	}

	public List<ChildB> getReferencedChildB() {
		if (referencedChildB == null) {
			referencedChildB = new ArrayList<>();
		}
		return this.referencedChildB;
	}

	public Root getRoot() {
		return root;
	}

}
