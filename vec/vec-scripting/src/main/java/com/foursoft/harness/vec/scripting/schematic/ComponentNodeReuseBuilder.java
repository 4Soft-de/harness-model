/*-
 * ========================LICENSE_START=================================
 * VEC 2.x Scripting API (Experimental)
 * %%
 * Copyright (C) 2020 - 2025 4Soft GmbH
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * =========================LICENSE_END==================================
 */
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
