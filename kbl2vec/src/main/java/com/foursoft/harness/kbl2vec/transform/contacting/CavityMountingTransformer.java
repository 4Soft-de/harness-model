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
package com.foursoft.harness.kbl2vec.transform.contacting;

import com.foursoft.harness.kbl.v25.*;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.common.util.StreamUtils;
import com.foursoft.harness.vec.v2x.VecCavityMounting;
import com.foursoft.harness.vec.v2x.VecCavityPlugRole;
import com.foursoft.harness.vec.v2x.VecCavityReference;

import java.util.List;

public class CavityMountingTransformer implements Transformer<KblContactPoint, VecCavityMounting> {

    @Override
    public TransformationResult<VecCavityMounting> transform(final TransformationContext context,
                                                             final KblContactPoint source) {
        final VecCavityMounting destination = new VecCavityMounting();

        return TransformationResult.from(destination)
                .withLinker(Query.fromLists(getCavityPlugOccurrences(source)), VecCavityPlugRole.class,
                            VecCavityMounting::getReplacedPlug)
                .withLinker(Query.fromLists(source.getContactedCavity()), VecCavityReference.class,
                            VecCavityMounting::getEquippedCavityRef)
                .build();
    }

    private List<KblCavityPlugOccurrence> getCavityPlugOccurrences(final KblContactPoint contactPoint) {
        return contactPoint.getAssociatedParts().stream()
                .flatMap(StreamUtils.ofClass(HasReplacing.class))
                .flatMap(hasReplacing -> hasReplacing.getReplacings().stream())
                .map(KblPartSubstitution::getReplaced)
                .toList();
    }
}
