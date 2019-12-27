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
package de.foursoft.harness.xml.postprocessing;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.Unmarshaller;

public class ModelPostProcessorManager extends Unmarshaller.Listener {

    private final ModelPostProcessorRegistry modelPostProcessorRegistry;

    private final List<Object> targetObjects = new LinkedList<>();

    public ModelPostProcessorManager(final ModelPostProcessorRegistry modelPostProcessorRegistry) {
        this.modelPostProcessorRegistry = modelPostProcessorRegistry;
    }

    @Override
    public void afterUnmarshal(final Object target, final Object parent) {
        final List<ModelPostProcessor> postProcessors = modelPostProcessorRegistry.postProcessorsFor(target.getClass());
        for (final ModelPostProcessor modelPostProcessor : postProcessors) {
            modelPostProcessor.afterUnmarshalling(target, parent);
        }

        targetObjects.add(target);
    }

    public void doPostProcessing() {
        for (final Object target : targetObjects) {
            final List<ModelPostProcessor> postProcessors = modelPostProcessorRegistry
                    .postProcessorsFor(target.getClass());
            for (final ModelPostProcessor modelPostProcessor : postProcessors) {
                modelPostProcessor.afterUnmarshallingCompleted(target);
            }
        }
    }

}
