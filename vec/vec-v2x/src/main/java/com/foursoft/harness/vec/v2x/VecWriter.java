/*-
 * ========================LICENSE_START=================================
 * VEC 2.X
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
package com.foursoft.harness.vec.v2x;

import com.foursoft.harness.navext.runtime.io.utils.ValidationEventLogger;
import com.foursoft.harness.navext.runtime.io.write.XMLWriter;
import jakarta.xml.bind.ValidationEvent;

import java.util.function.Consumer;

/**
 * a default implementation for a vec 120 writer
 */
public final class VecWriter extends XMLWriter<VecContent> {

    /**
     * create a default VecWriter with a default validation events logger {@link ValidationEventLogger}
     */
    public VecWriter() {
        super(VecContent.class, new ValidationEventLogger());
    }

    /**
     * create a default VecWriter with a custom validation events logger
     *
     * @param validationEventConsumer a custom validation events consumer
     */
    public VecWriter(final Consumer<ValidationEvent> validationEventConsumer) {
        super(VecContent.class, validationEventConsumer);
    }

}
