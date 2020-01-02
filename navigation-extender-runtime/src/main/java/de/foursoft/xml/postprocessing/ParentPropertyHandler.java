/*-
 * ========================LICENSE_START=================================
 * xml-runtime
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
package de.foursoft.xml.postprocessing;

import java.lang.reflect.Field;

import de.foursoft.xml.annotations.XmlParent;

public class ParentPropertyHandler {

    private final Field field;
    private final Class<?> typeOfField;

    public ParentPropertyHandler(final Field field) {
        if (!field.isAnnotationPresent(XmlParent.class)) {
            throw new ModelPostProcessorException(
                    "For the field " + field.getName() + " in " + field.getDeclaringClass()
                            .getName() + " no parent annotation is present.");
        }
        this.field = field;
        this.field.setAccessible(true);
        typeOfField = field.getType();
    }

    public boolean isHandlingParent(final Object parent) {
        return typeOfField.isInstance(parent);
    }

    public void handleParentProperty(final Object target, final Object parent) {
        try {
            field.set(target, parent);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new ModelPostProcessorException("Can not set parent property value.", e);
        }
    }

}
