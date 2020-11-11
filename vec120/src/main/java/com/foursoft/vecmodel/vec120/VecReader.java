/*-
 * ========================LICENSE_START=================================
 * vec120
 * %%
 * Copyright (C) 2020 4Soft GmbH
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
package com.foursoft.vecmodel.vec120;

import com.foursoft.xml.io.read.XMLReader;
import com.foursoft.xml.model.Identifiable;

import javax.xml.bind.ValidationEvent;
import java.util.function.Consumer;

/**
 * A default implementation for a thread local stored VEC 120 reader. Validation events are logged to slf4j.
 * If a custom event consumer is needed, derive from  @{@link XMLReader}
 * <p>
 * In the past, this reader had a thread local singleton functionality in order to reuse
 * the reader for repeated reads. This caused memory leaks in environments with thread
 * pools (e.g. servlet container) as the JVM default {@link javax.xml.bind.Unmarshaller}
 * implementation does not clean up internal states properly after unmarshalling is finished.
 * Therefore the functionality has been dropped.
 * </p>
 * <p>
 * The performance overhead of creating a new reader for each read is about 10% - 15% for
 * repeated reads. The overhead is independent from the size of unmarshalled file. If this is an
 * issue, you can manage your own singleton reader (it is <b>not thread-safe</b>, but can be reused).
 * </p>
 */
public final class VecReader extends XMLReader<VecContent, Identifiable> {

    /**
     * Creates a new VEC reader.
     */
    public VecReader() {
        super(VecContent.class, Identifiable.class, Identifiable::getXmlId);
    }

    /**
     * Creates a new VEC reader.
     *
     * @param validationEventConsumer a consumer for validation events
     */
    public VecReader(final Consumer<ValidationEvent> validationEventConsumer) {
        super(VecContent.class, Identifiable.class, Identifiable::getXmlId, validationEventConsumer);
    }

    /**
     * @return a new VecReader for each call.
     * @deprecated the thread local caching has been removed due to memory leaking issues. Create your
     * own {@link VecReader} and cache it by yourself if necessary. Will be removed with a future release.
     */
    @Deprecated
    public static VecReader getLocalReader() {
        return new VecReader();
    }
}