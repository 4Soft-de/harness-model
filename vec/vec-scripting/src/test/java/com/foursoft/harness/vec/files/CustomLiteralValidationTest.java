/*-
 * ========================LICENSE_START=================================
 * VEC 2.x Scripting API (Experimental)
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
package com.foursoft.harness.vec.files;

import com.foursoft.harness.navext.runtime.io.validation.XmlValidationException;
import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.v2x.VecDocumentType;
import com.foursoft.harness.vec.v2x.VecDocumentTypeLiteral;
import com.foursoft.harness.vec.v2x.VecPrimaryPartType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Pins how the two schemas VEC ships treat a literal the standard does not define.
 *
 * <p>
 * Open enumerations are extensible against the schema used for reading and writing, and closed
 * against the strict schema. Both are intended: the strict schema exists precisely to report where a
 * document leaves the recommended vocabulary. Custom literals and strict validation therefore cannot
 * both be had, and that is a property of the VEC standard rather than of this API.
 * </p>
 */
class CustomLiteralValidationTest {

    @Test
    void aDocumentUsingOnlyDefinedLiteralsPassesStrictValidation() {
        assertThatCode(TestUtils.storeVecAndValidate("open-enum-defined-literal",
                                                     sessionWithDocumentType(VecDocumentType.PART_MASTER)))
                .doesNotThrowAnyException();
    }

    @Test
    void aDocumentUsingACustomLiteralIsWrittenButFailsStrictValidation() {
        final VecSession session =
                sessionWithDocumentType(new VecDocumentTypeLiteral.Custom("AcmeInternalDocumentType"));

        assertThatCode(session::writeToString).doesNotThrowAnyException();
        assertThatThrownBy(TestUtils.storeVecAndValidate("open-enum-custom-literal", session))
                .isInstanceOf(XmlValidationException.class);
    }

    private static VecSession sessionWithDocumentType(final VecDocumentTypeLiteral documentType) {
        final VecSession session = new VecSession();
        session.component("CON-A", "DRAW-CON-A", VecPrimaryPartType.CONNECTOR_HOUSING,
                          comp -> comp.addGeneralTechnicalPart());
        session.getVecContentRoot()
                .getDocumentVersions()
                .forEach(documentVersion -> documentVersion.setDocumentTypeLiteral(documentType));
        return session;
    }

}
