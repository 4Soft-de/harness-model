/*-
 * ========================LICENSE_START=================================
 * KBL to VEC Converter
 * %%
 * Copyright (C) 2025 4Soft GmbH
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
package com.foursoft.harness.kbl2vec.transform.connectivity;

import com.foursoft.harness.kbl.v25.KblCavitySealOccurrence;
import com.foursoft.harness.kbl.v25.KblContactPoint;
import com.foursoft.harness.kbl.v25.KblExtremity;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.common.util.StreamUtils;
import com.foursoft.harness.vec.v2x.VecCavitySealRole;
import com.foursoft.harness.vec.v2x.VecWireEnd;
import com.foursoft.harness.vec.v2x.VecWireMounting;

import java.util.Comparator;
import java.util.List;

public class WireMountingTransformer implements Transformer<KblContactPoint, VecWireMounting> {

    @Override
    public TransformationResult<VecWireMounting> transform(final TransformationContext context,
                                                           final KblContactPoint source) {
        final VecWireMounting destination = new VecWireMounting();

        return TransformationResult.from(destination)
                .withLinker(Query.fromLists(getCavitySealOccurrences(source)), VecCavitySealRole.class,
                            VecWireMounting::setMountedCavitySeal)
                .withLinker(Query.fromLists(getSortedExtremities(source)), VecWireEnd.class,
                            VecWireMounting::getReferencedWireEnd)
                .build();
    }

    private List<KblExtremity> getSortedExtremities(final KblContactPoint contactPoint) {
        return contactPoint.getRefExtremity().stream()
                .sorted(Comparator.comparing(KblExtremity::getXmlId))
                .toList();
    }

    private List<KblCavitySealOccurrence> getCavitySealOccurrences(final KblContactPoint contactPoint) {
        return StreamUtils.checkAndCast(contactPoint.getAssociatedParts(), KblCavitySealOccurrence.class).toList();
    }
}
