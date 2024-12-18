/*-
 * ========================LICENSE_START=================================
 * NavExt Runtime
 * %%
 * Copyright (C) 2019 - 2023 4Soft GmbH
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
package com.foursoft.harness.navext.runtime.io.write.xmlmeta.comments;

import com.foursoft.harness.navext.runtime.io.write.xmlmeta.XMLMetaAwareXMLStreamWriter;
import jakarta.xml.bind.Marshaller.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamException;
import java.util.Optional;

public class CommentAdderListener extends Listener {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentAdderListener.class);
    private final XMLMetaAwareXMLStreamWriter xsw;
    private final Comments comments;

    /**
     * @param xsw      the xml stream writer
     * @param comments map of xjc objects and comment strings
     */
    public CommentAdderListener(final XMLMetaAwareXMLStreamWriter xsw, final Comments comments) {
        this.xsw = xsw;
        this.comments = comments;
    }

    @Override
    public void beforeMarshal(final Object source) {
        final Optional<String> comment = comments.get(source);
        if (comment.isPresent()) {
            try {
                xsw.writeComment(comment.get(), source);
            } catch (final XMLStreamException e) {
                LOGGER.warn("Ignored Exception while writing comments:", e);
            }
        }
    }
}
