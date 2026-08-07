/*-
 * ========================LICENSE_START=================================
 * VEC 2.x Scripting API (Experimental)
 * %%
 * Copyright (C) 2020 - 2025 4Soft GmbH
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
package com.foursoft.harness.vec.scripting.enums;

import com.foursoft.harness.vec.v2x.VecDocumentType;
import com.foursoft.harness.vec.v2x.VecDocumentTypeLiteral;

/**
 * @deprecated Use {@link VecDocumentType}, which is generated from the literals the VEC schema defines for
 * this open enumeration, together with the {@link VecDocumentTypeLiteral} it implements.
 */
@Deprecated(forRemoval = true)
public enum DocumentType implements VecDocumentTypeLiteral {

    /**
     * Replaced by {@link VecDocumentType#HARNESS_DESCRIPTION}.
     */
    HARNESS_DESCRIPTION(VecDocumentType.HARNESS_DESCRIPTION),

    /**
     * Replaced by {@link VecDocumentType#NETWORK_ARCHITECTURE}.
     */
    NETWORK_ARCHITECTURE(VecDocumentType.NETWORK_ARCHITECTURE),

    /**
     * Replaced by {@link VecDocumentType#PART_MASTER}.
     */
    PART_MASTER(VecDocumentType.PART_MASTER),

    /**
     * Replaced by {@link VecDocumentType#PROCESSING_INSTRUCTION}.
     */
    PROCESSING_INSTRUCTION(VecDocumentType.PROCESSING_INSTRUCTION),

    /**
     * Replaced by {@link VecDocumentType#REQUIREMENTS_DESCRIPTION}.
     */
    REQUIREMENTS_DESCRIPTION(VecDocumentType.REQUIREMENTS_DESCRIPTION),

    /**
     * Replaced by {@link VecDocumentType#SYSTEM_SCHEMATIC}.
     */
    SYSTEM_SCHEMATIC(VecDocumentType.SYSTEM_SCHEMATIC);

    private final VecDocumentTypeLiteral delegate;

    DocumentType(final VecDocumentTypeLiteral delegate) {
        this.delegate = delegate;
    }

    @Override
    public String value() {
        return delegate.value();
    }

}
