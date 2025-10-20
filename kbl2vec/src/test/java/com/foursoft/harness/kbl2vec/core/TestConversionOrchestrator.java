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
package com.foursoft.harness.kbl2vec.core;

import com.foursoft.harness.kbl2vec.convert.ConverterRegistry;
import org.slf4j.Logger;

public class TestConversionOrchestrator {
    private final TransformationContext transformationContext;

    public TestConversionOrchestrator() {
        final ConversionProperties conversionProperties = new ConversionProperties();
        final Logger transformLogger = Logging.TRANSFORM_LOGGER;
        this.transformationContext = new TransformationContextImpl(transformLogger, conversionProperties,
                                                                   new ConverterRegistry(transformLogger,
                                                                                         conversionProperties),
                                                                   new EntityMapping());
    }

    public TestConversionOrchestrator addMockMapping(final Object source, final Object dest) {
        transformationContext.getEntityMapping().put(source, dest);
        return this;
    }

    public <S, D> D transform(final Transformer<S, D> transformer, final S source) {
        final TransformationResult<D> result = transformer.transform(transformationContext, source);
        transformationContext.getEntityMapping().put(source, result);
        final D dest = result.element();

        for (final Transformation<?, ?> downstream : result.downstreamTransformations()) {
            handleMockTransformation(downstream);
        }
        for (final Finisher finisher : result.finisher()) {
            finisher.finishTransformation(transformationContext);
        }

        return dest;
    }

    private <FROM, TO> void handleMockTransformation(final Transformation<FROM, TO> transformation) {
        for (final Object o : transformation.sourceQuery().execute()) {
            final Object mapped = transformationContext
                    .getEntityMapping()
                    .getIfUniqueOrElseThrow(o, transformation.destinationClass());
            transformation.accumulator().accept((TO) mapped);
        }

    }
}

