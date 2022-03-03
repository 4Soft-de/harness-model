/*-
 * ========================LICENSE_START=================================
 * kbl-v24
 * %%
 * Copyright (C) 2020 - 2022 4Soft GmbH
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
package com.foursoft.harness.kbl.v24;

import com.foursoft.jaxb.navext.runtime.io.utils.ValidationEventLogger;
import com.foursoft.jaxb.navext.runtime.io.write.XMLWriter;

import javax.xml.bind.ValidationEvent;
import java.util.function.Consumer;

/**
 * a default implementation for a kbl writer
 */
public final class KblWriter extends XMLWriter<KBLContainer> {

    /**
     * create a default KblWriter with a default validation events logger {@link ValidationEventLogger}
     */
    public KblWriter() {
        super(KBLContainer.class, new ValidationEventLogger());
    }

    /**
     * create a default KblWriter with a custom validation events logger
     *
     * @param validationEventConsumer a custom validation events consumer
     */
    public KblWriter(final Consumer<ValidationEvent> validationEventConsumer) {
        super(KBLContainer.class, validationEventConsumer);

    }

}

