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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class VersionLookupModelProvider implements VecModelProvider {
    private final Map<Package, VecModelProvider> providerMap = new HashMap<>();
    private static final Package v113Package = com.foursoft.harness.vec.v113.VecContent.class.getPackage();
    private static final Package v12xPackage = com.foursoft.harness.vec.v12x.VecContent.class.getPackage();
    private static final Package v2xPackage = com.foursoft.harness.vec.v2x.VecContent.class.getPackage();

    @Override
    public Optional<UmlField> findField(Field field) {
        VecModelProvider provider = getProvider(field.getDeclaringClass()
                                                        .getPackage());

        return provider.findField(field);
    }

    private VecModelProvider getProvider(Package pkg) {
        return providerMap.computeIfAbsent(pkg, this::loadProvider);
    }

    private VecModelProvider loadProvider(Package aPackage) {
        if (v113Package.equals(aPackage)) {
            return new VecModelProviderBuilder(VecUmlModel.VEC_113.getInputStream()).build();
        }
        if (v12xPackage.equals(aPackage)) {
            return new VecModelProviderBuilder(VecUmlModel.VEC_12X.getInputStream()).build();
        }
        if (v2xPackage.equals(aPackage)) {
            return new VecModelProviderBuilder(VecUmlModel.VEC_2X.getInputStream()).build();
        }
        throw new VecXmiException("No suitable VecModelProvider found for: " + aPackage.getName());
    }
}
