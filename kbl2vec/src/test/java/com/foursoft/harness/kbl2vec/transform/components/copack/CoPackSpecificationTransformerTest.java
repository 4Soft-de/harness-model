package com.foursoft.harness.kbl2vec.transform.components.copack;

import com.foursoft.harness.kbl.v25.KblCoPackPart;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecPartOrUsageRelatedSpecification;
import com.foursoft.harness.vec.v2x.VecPartVersion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CoPackSpecificationTransformerTest {

    @Test
    void should_transformCoPackSpecification() {
        final CoPackSpecificaionTransformer transformer = new CoPackSpecificaionTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblCoPackPart source = new KblCoPackPart();
        source.setPartType("TestPartType");
        source.setPartNumber("TestPartNumber");
        final String expectedIdentification = "POURS-TestPartNumber";

        final VecPartVersion vecPartVersion = new VecPartVersion();
        orchestrator.addMockMapping(source, vecPartVersion);

        final VecPartOrUsageRelatedSpecification result = orchestrator.transform(transformer, source);

        assertThat(result).isNotNull()
                .returns("TestPartType", VecPartOrUsageRelatedSpecification::getSpecialPartType)
                .returns(expectedIdentification, VecPartOrUsageRelatedSpecification::getIdentification)
                .satisfies(v -> assertThat(v.getDescribedPart()).containsExactly(vecPartVersion));
    }
}
