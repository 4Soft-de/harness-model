/*-
 * ========================LICENSE_START=================================
 * Compatibility VEC 1.1.X To VEC 1.2.X
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
package com.foursoft.harness.compatibility.vec11to12;

import com.foursoft.harness.compatibility.core.Context;
import com.foursoft.harness.compatibility.vec11to12.common.VecProcessTask;
import com.foursoft.harness.compatibility.vec11to12.common.VecProcessor;
import com.foursoft.harness.compatibility.vec11to12.common.VecVersion;
import com.foursoft.harness.vec.common.HasVecVersion;
import jakarta.xml.bind.ValidationEvent;

import java.io.InputStream;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Class responsible for processing and converting any VEC 1.X.X file into another VEC 1.X.X file.
 */
public class Vec11XTo12XProcessor implements VecProcessor {

    @Override
    public <T extends HasVecVersion> T createContent(final VecProcessTask vecProcessTask, final Class<T> targetClass) {
        if (vecProcessTask.getInputStream().isPresent()) {
            return createContentFromInputStream(vecProcessTask, targetClass);
        } else if (vecProcessTask.getContent().isPresent()) {
            return createContentFromVecContent(vecProcessTask, targetClass);
        } else {
            final String errorMsg = String.format(
                    "VEC couldn't be created for %s. The provided InputStream and VecContent are null! ",
                    vecProcessTask.getStreamName());
            throw new IllegalStateException(errorMsg);
        }
    }

    private <T> T createContentFromInputStream(final VecProcessTask vecProcessTask, final Class<T> targetClass) {
        final InputStream inputStream = vecProcessTask.getInputStream().orElse(null);
        final VecVersion sourceVersion = vecProcessTask.getSourceVersion();
        final VecVersion targetVersion = vecProcessTask.getTargetVersion();
        final Consumer<ValidationEvent> eventConsumer = vecProcessTask.getValidationEventConsumer();

        if (sourceVersion == VecVersion.VEC11X) {
            final com.foursoft.harness.vec.v113.VecReader vec11xReader = new com.foursoft.harness.vec.v113.VecReader(
                    eventConsumer);
            final com.foursoft.harness.vec.v113.VecContent vec11x = vec11xReader.read(inputStream);
            if (targetVersion == VecVersion.VEC11X) {
                return targetClass.cast(vec11x);
            } else if (targetVersion == VecVersion.VEC12X) {
                return convertVec(vec11x, vecProcessTask);
            }
        } else if (sourceVersion == VecVersion.VEC12X) {
            final com.foursoft.harness.vec.v12x.VecReader vec12xReader = new com.foursoft.harness.vec.v12x.VecReader(
                    eventConsumer);
            final com.foursoft.harness.vec.v12x.VecContent vec12x = vec12xReader.read(inputStream);
            if (targetVersion == VecVersion.VEC11X) {
                return convertVec(vec12x, vecProcessTask);
            } else if (targetVersion == VecVersion.VEC12X) {
                return targetClass.cast(vec12x);
            }
        }

        final String errorMsg = String.format(
                "VEC couldn't be created for %s." +
                        "Either the source version %s or the target version %s couldn't be processed.",
                vecProcessTask.getStreamName(), sourceVersion.getCurrentVersion(), targetVersion.getCurrentVersion());
        throw new IllegalStateException(errorMsg);
    }

    private <T extends HasVecVersion> T createContentFromVecContent(final VecProcessTask vecProcessTask,
                                                                    final Class<T> targetClass) {
        final Optional<HasVecVersion> optHasVecVersion = vecProcessTask.getContent();
        if (optHasVecVersion.isEmpty()) {
            return null;
        }
        final HasVecVersion content = optHasVecVersion.get();
        final VecVersion targetVersion = vecProcessTask.getTargetVersion();
        final VecVersion sourceVersion = VecVersion.findVersion(content).orElse(null);

        final String errorMsg = String.format(
                "VEC couldn't be created for %s." +
                        "Either the source sourceVersion %s or the target sourceVersion %s couldn't be processed.",
                vecProcessTask.getStreamName(), sourceVersion, targetVersion.getCurrentVersion());

        if (sourceVersion == null) {
            throw new IllegalStateException(errorMsg);
        }

        if (sourceVersion == VecVersion.VEC11X) {
            if (targetVersion == VecVersion.VEC11X) {
                return targetClass.cast(content);
            } else if (targetVersion == VecVersion.VEC12X) {
                return convertVec(content, vecProcessTask);
            }
        } else if (sourceVersion == VecVersion.VEC12X) {
            if (targetVersion == VecVersion.VEC11X) {
                return convertVec(content, vecProcessTask);
            } else if (targetVersion == VecVersion.VEC12X) {
                return targetClass.cast(content);
            }
        }

        throw new IllegalStateException(errorMsg);
    }

    private <T> T convertVec(final HasVecVersion vecContent, final VecProcessTask vecProcessTask) {
        final Vec11XTo12XCompatibilityWrapper vec11XTo12XCompatibilityWrapper = new Vec11XTo12XCompatibilityWrapper();

        final Context compatibilityContext = vec11XTo12XCompatibilityWrapper.getContext();
        compatibilityContext.setContent(vecContent);

        final Consumer<Context> compatibilityContextConsumer = vecProcessTask.getCompatibilityContextConsumer();

        if (compatibilityContextConsumer != null) {
            compatibilityContextConsumer.accept(compatibilityContext);
        }

        return compatibilityContext.getWrapperProxyFactory().createProxy(vecContent);
    }

}
