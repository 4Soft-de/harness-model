package com.foursoft.harness.kbl2vec.transform.core;

import com.foursoft.harness.kbl.v25.KblInstructionClassification;
import com.foursoft.harness.kbl.v25.KblProcessingInstruction;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecCustomProperty;
import com.foursoft.harness.vec.v2x.VecSimpleValueProperty;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProcessingInformationCustomPropertyTransformerTest {

    @Test
    void should_transformCustomProperty() {
        // Given
        final ProcessingInformationCustomPropertyTransformer transformer =
                new ProcessingInformationCustomPropertyTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblProcessingInstruction source = new KblProcessingInstruction();
        source.setClassification(KblInstructionClassification.CUSTOM_PROPERTY);
        source.setInstructionType("TestInstructionType");
        source.setInstructionValue("TestInstructionValue");

        // When
        final VecSimpleValueProperty result = (VecSimpleValueProperty) orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("TestInstructionType", VecCustomProperty::getPropertyType)
                .returns("TestInstructionValue", VecSimpleValueProperty::getValue);
    }
}
