/*-
 * ========================LICENSE_START=================================
 * Compatibility Core
 * %%
 * Copyright (C) 2020 - 2026 4Soft GmbH
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
package com.foursoft.harness.compatibility.core.wrapper.fixture.xmlfields;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlID;

import java.util.ArrayList;
import java.util.List;

/**
 * Mimics a generated JAXB bean: non-public fields, lazily created collections and an {@code is} getter for
 * the boolean. The {@code raw*} methods read the fields directly, so that a test can tell a written field
 * apart from a value a getter conjured up on access.
 */
public class XmlFieldsBean {

    @XmlID
    protected String identification;

    @XmlElement(name = "Description")
    protected String description;

    @XmlElement(name = "Flag")
    protected Boolean flag;

    @XmlElement(name = "Items")
    protected List<String> items;

    protected String notAnnotated;

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(final String identification) {
        this.identification = identification;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Boolean isFlag() {
        return flag;
    }

    public void setFlag(final Boolean flag) {
        this.flag = flag;
    }

    public List<String> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    public String getNotAnnotated() {
        return notAnnotated;
    }

    public void setNotAnnotated(final String notAnnotated) {
        this.notAnnotated = notAnnotated;
    }

    public List<String> rawItems() {
        return items;
    }

    public String rawNotAnnotated() {
        return notAnnotated;
    }

}
