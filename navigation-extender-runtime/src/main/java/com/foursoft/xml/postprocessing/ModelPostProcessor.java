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

import javax.xml.bind.Unmarshaller;

public interface ModelPostProcessor {

    Class<?> getClassToHandle();

    /**
     * Called during the unmarshalling the JAXB unmarshalling process, after the unmarshalling of <tt>target</tt> has
     * been completed. IDREF relations are not yet initialized at this point of the unmarshalling process.
     *
     * @param target non-null instance of JAXB mapped class prior to unmarshalling into it.
     * @param parent instance of JAXB mapped class that will reference
     *               <tt>target</tt>. <tt>null</tt> when <tt>target</tt> is root
     *               element.
     * @see Unmarshaller.Listener#afterUnmarshal(Object, Object)
     */
    void afterUnmarshalling(Object target, Object parent);

    /**
     * Called after the unmarshalling process of the whole model has been completed.
     *
     * @param target the object co call
     */
    void afterUnmarshallingCompleted(Object target);

    /**
     * Clear the state of the {@link ModelPostProcessor} to allow garbage collection and reuse during multiple
     * unmarshallings.
     */
    void clearState();

}
