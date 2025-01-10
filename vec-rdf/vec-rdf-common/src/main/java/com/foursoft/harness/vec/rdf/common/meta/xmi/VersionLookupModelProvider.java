/*-
 * ========================LICENSE_START=================================
 * VEC RDF Common
 * %%
 * Copyright (C) 2024 4Soft GmbH
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
package com.foursoft.harness.vec.rdf.common.meta.xmi;

import com.foursoft.harness.vec.rdf.common.VecVersion;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class VersionLookupModelProvider implements UmlModelProvider {
    private final Map<String, UmlModelProvider> providerMap = new HashMap<>();

    @Override
    public UmlField findField(final Field field) {
        final UmlModelProvider provider = getProvider(field.getDeclaringClass()
                                                              .getPackageName());

        return provider.findField(field);
    }

    private UmlModelProvider getProvider(final String pkg) {
        return providerMap.computeIfAbsent(pkg, this::loadProvider);
    }

    private UmlModelProvider loadProvider(final String aPackage) {
        return new XmiModelProviderBuilder(
                VecVersion.findLatestVersionForApiPackage(aPackage).getModelInputStream()).build();
    }
}
