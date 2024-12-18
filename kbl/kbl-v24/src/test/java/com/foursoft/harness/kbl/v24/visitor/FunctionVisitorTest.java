/*-
 * ========================LICENSE_START=================================
 * KBL 2.4
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
package com.foursoft.harness.kbl.v24.visitor;

import com.foursoft.harness.kbl.v24.KBLContainer;
import com.foursoft.harness.kbl.v24.KblReader;
import com.foursoft.harness.navext.runtime.model.Identifiable;
import com.foursoft.harness.navext.runtime.model.ModifiableIdentifiable;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

class FunctionVisitorTest {

    public static final String PREFIX = "TestPrefix";

    @Test
    void updateXmlIdsTest() throws IOException {
        try (final InputStream is = getClass().getClassLoader().getResourceAsStream("sample.kbl")) {
            final KBLContainer kblContainer = new KblReader().read(is);
            final FunctionVisitor<Identifiable, Void> visitor = new FunctionVisitor<>(this::changeXmlId);
            final TraversingVisitor<Void, RuntimeException> traversingVisitor =
                    new TraversingVisitor<>(new DepthFirstTraverserImpl<>(), visitor);
            kblContainer.accept(traversingVisitor);

            assertThat(kblContainer)
                    .satisfies(c -> assertThat(c.getXmlId()).startsWith(PREFIX));
        }
    }

    private Void changeXmlId(final Identifiable element) {
        if (element instanceof ModifiableIdentifiable) {
            final ModifiableIdentifiable xmlObject = (ModifiableIdentifiable) element;
            xmlObject.setXmlId(PREFIX + element.getXmlId());
        }
        return null;
    }

}
