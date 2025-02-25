package com.foursoft.harness.kbl2vec;

import com.foursoft.harness.kbl.v25.KBLContainer;
import com.foursoft.harness.kbl2vec.core.ConversionOrchestrator;
import com.foursoft.harness.kbl2vec.core.TransformerRegistry;
import com.foursoft.harness.kbl2vec.post.XmlIdPostProcessor;
import com.foursoft.harness.vec.v2x.VecContent;

public class KblToVecConverter {

    private final ConversionOrchestrator<KBLContainer, VecContent>
            orchestrator;

    public KblToVecConverter() {
        orchestrator = orchestrator(null);
    }

    public VecContent convert(final KBLContainer container) {
        return orchestrator.convert(container);
    }

    private static ConversionOrchestrator<KBLContainer, VecContent> orchestrator(final TransformerRegistry registry) {
        final ConversionOrchestrator<KBLContainer, VecContent> orchestrator = new ConversionOrchestrator<>(
                KBLContainer.class,
                VecContent.class,
                registry);

        orchestrator.addPostProcessor(new XmlIdPostProcessor());

        return orchestrator;
    }

}
