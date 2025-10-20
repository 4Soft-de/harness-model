/*-
 * ========================LICENSE_START=================================
 * KBL to VEC Converter
 * %%
 * Copyright (C) 2025 4Soft GmbH
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
package com.foursoft.harness.kbl2vec.utils;

import com.foursoft.harness.kbl.v25.KblSlot;
import com.foursoft.harness.kbl2vec.convert.ConverterRegistry;
import com.foursoft.harness.kbl2vec.core.*;
import com.foursoft.harness.vec.v2x.VecLocalizedString;
import com.foursoft.harness.vec.v2x.VecSlot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static org.assertj.core.api.Assertions.assertThat;

class DeterministicXmlIdGeneratorTest {
    private TransformationContext context;

    @BeforeEach
    void setUp() {
        final ConversionProperties conversionProperties = new ConversionProperties();
        final Logger transformLogger = Logging.TRANSFORM_LOGGER;
        this.context = new TransformationContextImpl(transformLogger, conversionProperties,
                                                     new ConverterRegistry(transformLogger, conversionProperties),
                                                     new EntityMapping());
    }

    @Test
    void should_createDeterministicId() {
        // Given
        final KblSlot kblSlot = new KblSlot();
        final VecSlot vecSlot = new VecSlot();

        kblSlot.setXmlId("TestXmlId");
        context.getEntityMapping().put(kblSlot, vecSlot);

        final DeterministicXmlIdGenerator xmlIdGenerator = new DeterministicXmlIdGenerator(context);

        // When
        xmlIdGenerator.createIdForXmlBean(vecSlot, "TestPrefix_");

        // Then
        assertThat(vecSlot).returns("TestPrefix_TestXmlId", VecSlot::getXmlId);
    }

    @Test
    void should_createDeterministicIdWithCounter() {
        // Given
        final KblSlot kblSlot = new KblSlot();
        final VecSlot vecSlot1 = new VecSlot();
        final VecSlot vecSlot2 = new VecSlot();

        kblSlot.setXmlId("TestXmlId");
        context.getEntityMapping().put(kblSlot, vecSlot1);
        context.getEntityMapping().put(kblSlot, vecSlot2);

        final DeterministicXmlIdGenerator xmlIdGenerator = new DeterministicXmlIdGenerator(context);
        xmlIdGenerator.createIdForXmlBean(vecSlot1, "TestPrefix_");

        // When
        xmlIdGenerator.createIdForXmlBean(vecSlot2, "TestPrefix_");

        // Then
        assertThat(vecSlot2).returns("TestPrefix_TestXmlId_1", VecSlot::getXmlId);
    }

    @Test
    void should_not_createDeterministicId() {
        // Given
        final VecLocalizedString vecLocalizedString = new VecLocalizedString();
        final DeterministicXmlIdGenerator xmlIdGenerator = new DeterministicXmlIdGenerator(context);

        // When
        xmlIdGenerator.createIdForXmlBean(vecLocalizedString, "TestPrefix_");

        // Then
        assertThat(vecLocalizedString).returns("TestPrefix_00000", VecLocalizedString::getXmlId);
    }
}
