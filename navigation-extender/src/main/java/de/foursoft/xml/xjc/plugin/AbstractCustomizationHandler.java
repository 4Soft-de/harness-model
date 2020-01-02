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
package de.foursoft.xml.xjc.plugin;

import java.util.stream.Stream;

import com.sun.tools.xjc.model.CCustomizable;
import com.sun.tools.xjc.model.CPluginCustomization;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;

public abstract class AbstractCustomizationHandler {

    protected abstract CustomizationTags handledTag();

    protected abstract void handleField(final FieldOutline fieldOutline, final CPluginCustomization customization);

    protected abstract void handleClass(final ClassOutline classOutline, final CPluginCustomization customization);

    protected abstract void handleGlobal(final Outline outline, final CPluginCustomization customization);

    public void traverseModel(final Outline outline) {
        findCustomization(outline.getModel(), handledTag()).forEach(c -> {
            handleGlobal(outline, c);
        });
        for (final ClassOutline classOutline : outline.getClasses()) {
            findCustomization(classOutline.getTarget(), handledTag()).forEach(c -> {
                handleClass(classOutline, c);
            });
            for (final FieldOutline fieldOutline : classOutline.getDeclaredFields()) {
                findCustomization(fieldOutline.getPropertyInfo(), handledTag()).forEach(c -> {
                    handleField(fieldOutline, c);
                });
            }
        }
    }

    private Stream<CPluginCustomization> findCustomization(final CCustomizable customizable,
            final CustomizationTags tag) {
        return customizable.getCustomizations()
                .stream()
                .filter(tag::isCustomization);
    }

}
