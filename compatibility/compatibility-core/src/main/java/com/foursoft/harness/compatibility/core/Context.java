/*-
 * ========================LICENSE_START=================================
 * compatibility-core
 * %%
 * Copyright (C) 2020 - 2023 4Soft GmbH
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
package com.foursoft.harness.compatibility.core;

import com.foursoft.harness.compatibility.core.mapping.ClassMapper;

/**
 * Interface for a context.
 */
public interface Context {

    /**
     * Returns the {@link ClassMapper} of this Context.
     *
     * @return The {@link ClassMapper} of this Context.
     */
    ClassMapper getClassMapper();

    /**
     * Returns the {@link WrapperRegistry} of this Context.
     *
     * @return The {@link WrapperRegistry} of this Context.
     */
    WrapperRegistry getWrapperRegistry();

    /**
     * Provides access to the {@link HasUnsupportedMethods unsupported methods} of this Context.
     *
     * @return The {@link HasUnsupportedMethods unsupported methods} of this Context.
     */
    HasUnsupportedMethods checkUnsupportedMethods();

    /**
     * Returns the {@link WrapperProxyFactory} of this Context.
     *
     * @return The {@link WrapperProxyFactory} of this Context.
     */
    WrapperProxyFactory getWrapperProxyFactory();

    /**
     * Returns the target object of this Context.
     *
     * @return The target object of this Context.
     */
    Object getContent();

    /**
     * Sets the {@link ClassMapper} of this Context.
     *
     * @param content The new target object of this Context.
     */
    void setContent(Object content);

}
