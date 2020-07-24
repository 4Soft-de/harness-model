/*-
 * ========================LICENSE_START=================================
 * navigation-extender-runtime
 * %%
 * Copyright (C) 2019 - 2020 4Soft GmbH
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
package com.foursoft.xml.io.utils;

import com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultNode;
import org.w3c.dom.Node;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventLocator;
import java.net.MalformedURLException;
import java.net.URL;

public class EventHelper {

    // should be implemented with a mocking framework
    public static ValidationEvent createEvent() {
        final RuntimeException exception = new RuntimeException();

        return new ValidationEvent() {
            @Override public int getSeverity() {
                return 1;
            }

            @Override public String getMessage() {
                return "message";
            }

            @Override public Throwable getLinkedException() {
                return exception;
            }

            @Override public ValidationEventLocator getLocator() {
                return new ValidationEventLocator() {
                    @Override public URL getURL() {
                        try {
                            return new URL("http://url");
                        } catch (final MalformedURLException malformedURLException) {
                            throw new RuntimeException(malformedURLException.getMessage());
                        }
                    }

                    @Override public int getOffset() {
                        return 2;
                    }

                    @Override public int getLineNumber() {
                        return 3;
                    }

                    @Override public int getColumnNumber() {
                        return 4;
                    }

                    @Override public Object getObject() {
                        return null;
                    }

                    @Override public Node getNode() {
                        return new DefaultNode();
                    }
                };
            }
        };
    }
}
