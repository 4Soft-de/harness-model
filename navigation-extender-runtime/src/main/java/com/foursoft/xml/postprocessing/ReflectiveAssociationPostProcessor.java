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
package com.foursoft.xml.postprocessing;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import com.foursoft.xml.annotations.XmlBackReference;
import com.foursoft.xml.annotations.XmlParent;

public class ReflectiveAssociationPostProcessor implements ModelPostProcessor {

    private final Class<?> classToHandle;
    private final List<ParentPropertyHandler> parentProperties = new LinkedList<>();
    private final List<BackReferencePropertyHandler> backReferenceProperties = new LinkedList<>();

    public ReflectiveAssociationPostProcessor(final Class<?> classToHandle) {
        this.classToHandle = classToHandle;
        for (final Field f : classToHandle.getDeclaredFields()) {
            if (f.isAnnotationPresent(XmlParent.class)) {
                parentProperties.add(new ParentPropertyHandler(f));
            }
            if (f.isAnnotationPresent(XmlBackReference.class)) {
                backReferenceProperties.add(new BackReferencePropertyHandler(f));
            }
        }
    }

    @Override
    public Class<?> getClassToHandle() {
        return classToHandle;
    }

    @Override
    public void afterUnmarshalling(final Object target, final Object parent) {
        for (final ParentPropertyHandler h : parentProperties) {
            if (h.isHandlingParent(parent)) {
                h.handleParentProperty(target, parent);
            }
        }
    }

    @Override
    public void afterUnmarshallingCompleted(final Object target) {
        for (final BackReferencePropertyHandler h : backReferenceProperties) {
            h.handleObject(target);
        }
    }

    @Override
    public void clearState() {
        // No state stored during unmarshalling.
    }

}
