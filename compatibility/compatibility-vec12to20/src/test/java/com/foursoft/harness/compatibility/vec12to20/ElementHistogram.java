/*-
 * ========================LICENSE_START=================================
 * Compatibility VEC 1.2.X To VEC 2.0.X
 * %%
 * Copyright (C) 2020 - 2026 4Soft GmbH
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
package com.foursoft.harness.compatibility.vec12to20;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Counts the elements of an XML document per path of element names ({@code Parent/Child}), so that two
 * versions of the same content can be compared for lost or added data without comparing them in detail.
 */
public final class ElementHistogram {

    private final Map<String, Integer> countByPath;

    private ElementHistogram(final Map<String, Integer> countByPath) {
        this.countByPath = countByPath;
    }

    /**
     * Counts the elements of the given document.
     *
     * @param xml Document to analyse.
     * @return The histogram of the document.
     */
    public static ElementHistogram of(final String xml) {
        final Map<String, Integer> counts = new TreeMap<>();
        final Deque<String> names = new ArrayDeque<>();
        try {
            final XMLStreamReader reader = XMLInputFactory.newFactory().createXMLStreamReader(new StringReader(xml));
            while (reader.hasNext()) {
                final int event = reader.next();
                if (event == XMLStreamConstants.START_ELEMENT) {
                    final String name = reader.getLocalName();
                    final String parent = names.isEmpty() ? "" : names.peek();
                    counts.merge(parent + "/" + name, 1, Integer::sum);
                    names.push(name);
                } else if (event == XMLStreamConstants.END_ELEMENT) {
                    names.pop();
                }
            }
        } catch (final XMLStreamException e) {
            throw new IllegalArgumentException("Not a well formed XML document.", e);
        }
        return new ElementHistogram(counts);
    }

    /**
     * Compares this histogram with another one.
     *
     * @param other Histogram of the other document.
     * @return For every path whose count differs, {@code count in this -> count in other}.
     */
    public Map<String, String> differencesTo(final ElementHistogram other) {
        final Map<String, String> differences = new TreeMap<>();
        final TreeSet<String> paths = new TreeSet<>(countByPath.keySet());
        paths.addAll(other.countByPath.keySet());
        for (final String path : paths) {
            final Integer mine = countByPath.getOrDefault(path, 0);
            final Integer theirs = other.countByPath.getOrDefault(path, 0);
            if (!Objects.equals(mine, theirs)) {
                differences.put(path, mine + " -> " + theirs);
            }
        }
        return differences;
    }

}
