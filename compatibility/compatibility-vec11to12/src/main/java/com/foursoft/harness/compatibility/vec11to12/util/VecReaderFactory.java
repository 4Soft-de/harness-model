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
package com.foursoft.harness.compatibility.vec11to12.util;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.wrapper.CompatibilityWrapper;
import com.foursoft.harness.compatibility.vec.common.VecVersion;
import com.foursoft.harness.compatibility.vec11to12.Vec11XTo12XCompatibilityWrapper;
import com.foursoft.harness.vec.v12x.VecContent;
import com.foursoft.harness.vec.v12x.VecReader;
import jakarta.xml.bind.ValidationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.function.Consumer;

/**
 * Reader factory to create a VEC 1.2.X {@link VecContent}.
 */
public class VecReaderFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(VecReaderFactory.class);

    private VecReaderFactory() {
        //hidden Constructor
    }

    /**
     * Creates a VEC 1.2.X {@link VecContent} from the given {@link InputStream}.
     * If the given {@link VecVersion} is a {@link VecVersion#VEC11X}, it will be converted to a VEC 1.2.X.
     *
     * @param vecVersion                   VecVersion of the InputStream.
     * @param inputStream                  InputStream to read and create the VecContent from.
     * @param eventConsumer                Consumer to consume {@link ValidationEvent}s.
     * @param compatibilityContextConsumer Consumer to consume the {@link CompatibilityContext} of
     *                                     an additionally created {@link CompatibilityWrapper}.
     *                                     Can be used to further adjust the creation of the VecContent.
     */
    public static VecContent createContent(final VecVersion vecVersion, final InputStream inputStream,
                                           final Consumer<ValidationEvent> eventConsumer,
                                           final Consumer<CompatibilityContext> compatibilityContextConsumer) {
        if (VecVersion.VEC11X == vecVersion) {
            final com.foursoft.harness.vec.v113.VecReader vec113Reader =
                    new com.foursoft.harness.vec.v113.VecReader(eventConsumer);
            LOGGER.debug("Trying to read VEC {}.", VecVersion.VEC11X.getCurrentVersion());

            final com.foursoft.harness.vec.v113.VecContent vecContent = vec113Reader.read(inputStream);
            final Vec11XTo12XCompatibilityWrapper vec11XTo12XCompatibilityWrapper =
                    new Vec11XTo12XCompatibilityWrapper();

            final CompatibilityContext compatibilityContext = vec11XTo12XCompatibilityWrapper.getContext();
            if (compatibilityContextConsumer != null) {
                compatibilityContextConsumer.accept(compatibilityContext);
            }

            return compatibilityContext.getWrapperProxyFactory().createProxy(vecContent);
        }

        if (VecVersion.VEC12X == vecVersion) {
            LOGGER.debug("Trying to read VEC {}.", VecVersion.VEC12X.getCurrentVersion());

            final VecReader vec12xReader = new VecReader(eventConsumer);
            return vec12xReader.read(inputStream);
        }

        final String version = vecVersion == null ? "-" : vecVersion.getIdentifier();
        throw new IllegalArgumentException("Not supported VEC version: " + version);
    }

    /**
     * Creates a VEC 1.2.X {@link VecContent} from the given {@link InputStream}.
     * If the given {@link VecVersion} is a {@link VecVersion#VEC11X}, it will be converted to a VEC 1.2.X.
     *
     * @param vecVersion    VecVersion of the InputStream.
     * @param inputStream   InputStream to read and create the VecContent from.
     * @param eventConsumer Consumer to consume {@link ValidationEvent}s.
     */
    public static VecContent createContent(final VecVersion vecVersion, final InputStream inputStream,
                                           final Consumer<ValidationEvent> eventConsumer) {
        return createContent(vecVersion, inputStream, eventConsumer, null);
    }

}
