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
package com.foursoft.harness.compatibility.core.wrapper.fixture.evolution.oldmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Older version of a model class whose properties the newer version changed without changing their meaning.
 */
public class Item {

    private Part reference;
    private List<Part> parts = new ArrayList<>();
    private String count;
    private Object untyped;
    private Object flag;
    private String legacyName;
    private List<Element> elements = new ArrayList<>();

    /**
     * @return A single reference, a list in the newer version.
     */
    public Part getReference() {
        return reference;
    }

    public void setReference(final Part reference) {
        this.reference = reference;
    }

    /**
     * @return A list, a single value in the newer version.
     */
    public List<Part> getParts() {
        return parts;
    }

    /**
     * @return A number as text, a {@link java.math.BigInteger} in the newer version.
     */
    public String getCount() {
        return count;
    }

    public void setCount(final String count) {
        this.count = count;
    }

    /**
     * @return An untyped value (e.g. a DOM node), a string in the newer version.
     */
    public Object getUntyped() {
        return untyped;
    }

    public void setUntyped(final Object untyped) {
        this.untyped = untyped;
    }

    /**
     * @return An untyped value with a {@code get} accessor, a {@code boolean} with an {@code is} accessor in
     * the newer version.
     */
    public Object getFlag() {
        return flag;
    }

    public void setFlag(final Object flag) {
        this.flag = flag;
    }

    /**
     * @return A property the newer version renamed to {@code newName}.
     */
    public String getLegacyName() {
        return legacyName;
    }

    public void setLegacyName(final String legacyName) {
        this.legacyName = legacyName;
    }

    /**
     * @return Elements, some of a class the newer version does not have.
     */
    public List<Element> getElements() {
        return elements;
    }

}
