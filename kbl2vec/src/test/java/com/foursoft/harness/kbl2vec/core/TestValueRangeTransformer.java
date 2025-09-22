package com.foursoft.harness.kbl2vec.core;

import com.foursoft.harness.kbl.v25.KblUnit;
import com.foursoft.harness.kbl.v25.KblValueRange;
import com.foursoft.harness.kbl2vec.transform.core.ValueRangeTransformer;
import com.foursoft.harness.vec.v2x.VecOtherUnit;
import com.foursoft.harness.vec.v2x.VecUnit;
import com.foursoft.harness.vec.v2x.VecValueRange;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TestValueRangeTransformer {

    @Test
    void should_transformValueRange() {
        // Given
        final ValueRangeTransformer transformer = new ValueRangeTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblValueRange source = new KblValueRange();

        final double expectedMinimum = 1.0;
        final double expectedMaximum = 2.0;
        source.setMinimum(expectedMinimum);
        source.setMaximum(expectedMaximum);

        final KblUnit kblUnit = new KblUnit();
        source.setUnitComponent(kblUnit);

        final VecUnit vecUnit = new VecOtherUnit();
        orchestrator.addMockMapping(kblUnit, vecUnit);

        // When
        final VecValueRange result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result)
                .isNotNull()
                .returns(expectedMinimum, VecValueRange::getMinimum)
                .returns(expectedMaximum, VecValueRange::getMaximum)
                .returns(vecUnit, VecValueRange::getUnitComponent);
    }
}
