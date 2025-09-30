package com.foursoft.harness.kbl2vec.transform.components.ee_components;

import com.foursoft.harness.kbl.v25.KblComponentBox;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecDocumentVersion;
import com.foursoft.harness.vec.v2x.VecEEComponentSpecification;
import com.foursoft.harness.vec.v2x.VecGeneralTechnicalPartSpecification;
import com.foursoft.harness.vec.v2x.VecPartVersion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EEComponentDocumentVersionTransformerTest {

    @Test
    void should_transformEEComponentDocumentVersion() {
        // Given
        final EEComponentDocumentVersionTransformer transformer = new EEComponentDocumentVersionTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblComponentBox source = new KblComponentBox();

        final VecEEComponentSpecification specification = new VecEEComponentSpecification();
        orchestrator.addMockMapping(source, specification);

        final VecPartVersion vecPartVersion = new VecPartVersion();
        orchestrator.addMockMapping(source, vecPartVersion);

        final VecGeneralTechnicalPartSpecification generalTechnicalPartSpecification =
                new VecGeneralTechnicalPartSpecification();
        orchestrator.addMockMapping(source, generalTechnicalPartSpecification);

        // When
        final VecDocumentVersion result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .satisfies(v -> assertThat(v.getSpecifications()).containsExactlyInAnyOrder(specification,
                                                                                            generalTechnicalPartSpecification))
                .satisfies(v -> assertThat(v.getReferencedPart()).containsExactly(vecPartVersion));
    }
}
