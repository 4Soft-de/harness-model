/*-
 * ========================LICENSE_START=================================
 * VEC Common
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
package com.foursoft.harness.vec.common.openenum;

/**
 * A single literal of an <i>open enumeration</i>.
 *
 * <p>
 * The VEC defines open enumerations as enumerations whose literals are recommended but not
 * exhaustive: a sender may use a literal of its own. Therefore an open enumeration cannot be
 * represented by a Java enum alone. Instead, every open enumeration is represented by its own
 * interface extending this one, which is implemented by
 * </p>
 * <ul>
 *     <li>the generated enum holding the literals defined by the VEC standard,</li>
 *     <li>a generated {@code Custom} class holding an unrecognized literal, and</li>
 *     <li>optionally, enums of your own, contributed through an
 *     {@link OpenEnumLiteralProvider}.</li>
 * </ul>
 *
 * @see OpenEnumLiteralProvider
 * @see CustomOpenEnumLiteral
 */
public interface OpenEnumLiteral {

    /**
     * Returns the literal as it appears in the XML.
     *
     * @return The literal, never {@code null}.
     */
    String value();

    /**
     * Returns whether this literal is neither defined by the VEC standard nor contributed by an
     * {@link OpenEnumLiteralProvider}, but was read verbatim from a document.
     *
     * @return {@code true} for a generated {@code Custom} literal, {@code false} otherwise.
     */
    default boolean isCustom() {
        return this instanceof CustomOpenEnumLiteral;
    }

}
