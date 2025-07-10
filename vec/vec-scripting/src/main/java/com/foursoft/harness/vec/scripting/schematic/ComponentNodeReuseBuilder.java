package com.foursoft.harness.vec.scripting.schematic;

import com.foursoft.harness.vec.scripting.Builder;
import com.foursoft.harness.vec.v2x.*;

public class ComponentNodeReuseBuilder implements Builder<VecConnectionSpecification> {

    private final VecConnectionSpecification templateSpecification;
    private final VecReusageSpecification reusageSpecification;
    private final String templateIdentification;
    private final String usageIdentification;

    public ComponentNodeReuseBuilder(final VecConnectionSpecification templateSpecification,
                                     final VecReusageSpecification reusageSpecification,
                                     final String templateIdentification, final String usageIdentification) {
        this.templateSpecification = templateSpecification;
        this.reusageSpecification = reusageSpecification;
        this.templateIdentification = templateIdentification;
        this.usageIdentification = usageIdentification;
    }

    @Override public VecConnectionSpecification build() {
        final VecConnectionSpecification usageFragment = new VecConnectionSpecification();
        final VecComponentNode templateNode = templateSpecification.getComponentNodes().stream().filter(
                        cn -> cn.getIdentification().equals(templateIdentification)).findFirst()
                .orElseThrow();

        usageFragment.getComponentNodes().add(cloneNode(templateNode, usageIdentification));

        return usageFragment;
    }

    private VecComponentNode cloneNode(final VecComponentNode node, final String usageIdentification) {
        final VecComponentNode clone = new VecComponentNode();
        clone.setIdentification(usageIdentification);
        addReusage(node, clone);

        node.getComponentConnectors()
                .stream()
                .map(this::cloneConnector)
                .forEach(clone.getComponentConnectors()::add);

        return clone;
    }

    private VecComponentConnector cloneConnector(final VecComponentConnector connector) {
        final VecComponentConnector clone = new VecComponentConnector();
        clone.setIdentification(connector.getIdentification());
        addReusage(connector, clone);
        connector.getComponentPorts()
                .stream()
                .map(this::clonePort)
                .forEach(clone.getComponentPorts()::add);
        
        return clone;
    }

    private VecComponentPort clonePort(final VecComponentPort port) {
        final VecComponentPort clone = new VecComponentPort();
        clone.setIdentification(port.getIdentification());
        addReusage(port, clone);
        return clone;
    }

    private <T extends VecExtendableElement> void addReusage(final T template,
                                                             final T reusage) {
        final VecReusage vecReusage = new VecReusage();
        vecReusage.setTemplate(template);
        vecReusage.setUsage(reusage);
        reusageSpecification.getReusages().add(vecReusage);
    }
}
