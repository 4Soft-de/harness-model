package com.foursoft.harness.kbl2vec;

import com.foursoft.harness.kbl.v25.KBLContainer;
import com.foursoft.harness.kbl2vec.core.ConversionOrchestrator;
import com.foursoft.harness.kbl2vec.core.ConversionProperties;
import com.foursoft.harness.kbl2vec.post.XmlIdPostProcessor;
import com.foursoft.harness.vec.v2x.VecContent;

public class KblToVecConverter {

    private final ReflectionsBasedTransformerRegistry registry;

    public KblToVecConverter() {
        registry = new ReflectionsBasedTransformerRegistry();

    }

    public VecContent convert(final KBLContainer container) {
        final ConversionOrchestrator<KBLContainer, VecContent> orchestrator = createOrchestrator();
        return orchestrator.orchestrateTransformation(container);
    }

    private ConversionOrchestrator<KBLContainer, VecContent> createOrchestrator() {
        final ConversionOrchestrator<KBLContainer, VecContent> orchestrator = new ConversionOrchestrator<>(
                KBLContainer.class,
                VecContent.class,
                registry, new ConversionProperties());

        orchestrator.addPostProcessor(new XmlIdPostProcessor());

        return orchestrator;
    }

}
