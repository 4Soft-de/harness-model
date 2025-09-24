package com.foursoft.harness.kbl2vec.transform.components.copack;

import com.foursoft.harness.kbl.v25.KblCoPackPart;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecDocumentVersion;
import com.foursoft.harness.vec.v2x.VecGeneralTechnicalPartSpecification;
import com.foursoft.harness.vec.v2x.VecPartOrUsageRelatedSpecification;
import com.foursoft.harness.vec.v2x.VecPartVersion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CoPackDocumentVersionTransformerTest {

    @Test
    void should_transformCoPackDocumentVersion() {
        // Given
        final CoPackDocumentVersionTransformer transformer = new CoPackDocumentVersionTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblCoPackPart source = new KblCoPackPart();
        source.setVersion("TestVersion");

        final VecPartOrUsageRelatedSpecification vecSpecification = new VecPartOrUsageRelatedSpecification();
        final VecGeneralTechnicalPartSpecification vecGeneralSpecification = new VecGeneralTechnicalPartSpecification();

        orchestrator.addMockMapping(source, vecGeneralSpecification);
        orchestrator.addMockMapping(source, vecSpecification);

        final VecPartVersion vecPartVersion = new VecPartVersion();
        orchestrator.addMockMapping(source, vecPartVersion);

        // When
        final VecDocumentVersion result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("PartMaster", VecDocumentVersion::getDocumentType)
                .returns("TestVersion", VecDocumentVersion::getDocumentVersion)
                .satisfies(v -> assertThat(v.getReferencedPart()).containsExactly(vecPartVersion))
                .satisfies(v -> assertThat(v.getSpecifications()).containsExactlyInAnyOrder(vecSpecification,
                                                                                            vecGeneralSpecification));
    }
}
