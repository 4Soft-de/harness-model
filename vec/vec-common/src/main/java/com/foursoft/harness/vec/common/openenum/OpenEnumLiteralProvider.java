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

import java.util.Collection;

/**
 * Service provider interface for contributing literals of your own to open enumerations.
 *
 * <p>
 * Literals contributed here are returned by the generated {@code of(String)} factories instead of a
 * {@code Custom} literal, so a document using them reads back as a typed constant. Define an enum
 * implementing the interface of the open enumeration you are extending, and register it:
 * </p>
 *
 * <pre>{@code
 * public enum AcmeDocumentType implements VecDocumentTypeLiteral {
 *     ACME_SPECIFICATION("AcmeSpecification");
 *
 *     private final String value;
 *
 *     AcmeDocumentType(final String value) {
 *         this.value = value;
 *     }
 *
 *     public String value() {
 *         return value;
 *     }
 * }
 *
 * public class AcmeLiterals implements OpenEnumLiteralProvider {
 *     public Collection<? extends OpenEnumLiteral> literals() {
 *         return List.of(AcmeDocumentType.values());
 *     }
 * }
 * }</pre>
 *
 * <p>
 * Register the provider in {@code module-info.java} with
 * {@code provides OpenEnumLiteralProvider with AcmeLiterals;}, or on the class path with a
 * {@code META-INF/services/com.foursoft.harness.vec.common.openenum.OpenEnumLiteralProvider} entry.
 * </p>
 *
 * <p>
 * Literals defined by the VEC standard always take precedence, so a provider cannot shadow them.
 * </p>
 *
 * @see OpenEnumLiterals
 */
@FunctionalInterface
public interface OpenEnumLiteralProvider {

    /**
     * Returns the literals contributed by this provider. The literals may belong to any number of
     * different open enumerations; they are dispatched by the interfaces they implement.
     *
     * @return Possibly-empty Collection of literals, never {@code null}.
     */
    Collection<? extends OpenEnumLiteral> literals();

}
