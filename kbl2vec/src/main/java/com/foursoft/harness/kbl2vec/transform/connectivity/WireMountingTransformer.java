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
