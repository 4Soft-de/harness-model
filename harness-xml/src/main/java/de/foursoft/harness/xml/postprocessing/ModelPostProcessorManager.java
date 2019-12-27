package de.foursoft.harness.xml.postprocessing;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.Unmarshaller;

public class ModelPostProcessorManager extends Unmarshaller.Listener {

    private final ModelPostProcessorRegistry modelPostProcessorRegistry;

    private final List<Object> targetObjects = new LinkedList<>();

    public ModelPostProcessorManager(final ModelPostProcessorRegistry modelPostProcessorRegistry) {
        this.modelPostProcessorRegistry = modelPostProcessorRegistry;
    }

    @Override
    public void afterUnmarshal(final Object target, final Object parent) {
        final List<ModelPostProcessor> postProcessors = modelPostProcessorRegistry.postProcessorsFor(target.getClass());
        for (final ModelPostProcessor modelPostProcessor : postProcessors) {
            modelPostProcessor.afterUnmarshalling(target, parent);
        }

        targetObjects.add(target);
    }

    public void doPostProcessing() {
        for (final Object target : targetObjects) {
            final List<ModelPostProcessor> postProcessors = modelPostProcessorRegistry
                    .postProcessorsFor(target.getClass());
            for (final ModelPostProcessor modelPostProcessor : postProcessors) {
                modelPostProcessor.afterUnmarshallingCompleted(target);
            }
        }
    }

}
