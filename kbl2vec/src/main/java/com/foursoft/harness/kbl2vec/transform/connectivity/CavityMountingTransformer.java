package com.foursoft.harness.kbl2vec.transform.connectivity;

import com.foursoft.harness.kbl.v25.KblCavityOccurrence;
import com.foursoft.harness.kbl.v25.KblCavityPlugOccurrence;
import com.foursoft.harness.kbl.v25.KblContactPoint;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecCavityMounting;
import com.foursoft.harness.vec.v2x.VecCavityPlugRole;
import com.foursoft.harness.vec.v2x.VecCavityReference;

import java.util.List;
import java.util.Objects;

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
        return contactPoint.getContactedCavity().stream()
                .map(KblCavityOccurrence::getAssociatedPlug)
                .filter(Objects::nonNull)
                .toList();
    }
}
