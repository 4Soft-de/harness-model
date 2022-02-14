/*-
 * ========================LICENSE_START=================================
 * xjc-plugin
 * %%
 * Copyright (C) 2019 4Soft GmbH
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
package com.foursoft.jaxb.navext.runtime.xjc.plugin;

import java.util.Optional;
import java.util.stream.Stream;

import com.sun.tools.xjc.model.CPluginCustomization;

public enum CustomizationTags {

    EXT_REFERENCE("ext-reference"), PARENT("parent"), SELECTOR("selector");

    public static final String NS = "http://www.4soft.de/xjc-plugins/navigations";

    private final String tagName;

    CustomizationTags(final String tagName) {
        this.tagName = tagName;
    }

    public boolean isCustomization(final CPluginCustomization customization) {
        return NS.equals(customization.element.getNamespaceURI())
                && tagName.equals(customization.element.getLocalName());
    }

    public static Optional<CustomizationTags> of(final String nsUri, final String localName) {
        if (!NS.equals(nsUri)) {
            return Optional.empty();
        }
        return Stream.of(values())
                .filter(p -> p.tagName.equals(localName))
                .findAny();
    }

}
