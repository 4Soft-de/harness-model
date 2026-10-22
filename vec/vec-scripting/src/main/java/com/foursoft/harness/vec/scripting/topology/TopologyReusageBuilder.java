/*-
 * ========================LICENSE_START=================================
 * VEC 2.x Scripting API (Experimental)
 * %%
 * Copyright (C) 2020 - 2026 4Soft GmbH
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
package com.foursoft.harness.vec.scripting.topology;

import com.foursoft.harness.vec.scripting.Builder;
import com.foursoft.harness.vec.scripting.DefaultValues;
import com.foursoft.harness.vec.scripting.VecScriptingException;
import com.foursoft.harness.vec.v2x.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Reuses elements of the topology of another document (the template, e.g. the topology of an assembly in its part
 * master data) in a topology (the usage, e.g. the topology of a harness). The reused elements are copied into the
 * usage, the traceability to the template is defined by a {@link VecReusageSpecification}.
 */
public class TopologyReusageBuilder implements Builder<VecReusageSpecification> {

    private final TopologySpecificationQueries template;
    private final VecTopologySpecification usage;
    private final VecReusageSpecification reusageSpecification = new VecReusageSpecification();
    private final Map<VecTopologyNode, VecTopologyNode> reusedNodes = new HashMap<>();

    public TopologyReusageBuilder(final String templateDocumentNumber, final TopologySpecificationQueries template,
                                  final VecTopologySpecification usage) {
        this.template = template;
        this.usage = usage;
        reusageSpecification.setIdentification(
                "RS-" + templateDocumentNumber + "-" + DefaultValues.TOPO_SPEC_IDENTIFICATION);
    }

    public TopologyReusageBuilder withIdentification(final String identification) {
        reusageSpecification.setIdentification(identification);
        return this;
    }

    public TopologyReusageBuilder reuseNode(final String templateIdentification, final String usageIdentification) {
        final VecTopologyNode templateNode = template.findTopologyNode(templateIdentification);

        final VecTopologyNode node = new VecTopologyNode();
        node.setIdentification(usageIdentification);
        node.setNodeType(templateNode.getNodeType());

        usage.getTopologyNodes().add(node);
        reusedNodes.put(templateNode, node);
        addReusage(templateNode, node);

        return this;
    }

    /**
     * Reuses a segment of the template. The start and end node of the segment have to be reused before.
     */
    public TopologyReusageBuilder reuseSegment(final String templateIdentification,
                                               final String usageIdentification) {
        final VecTopologySegment templateSegment = template.findTopologySegment(templateIdentification);

        final VecTopologySegment segment = new VecTopologySegment();
        segment.setIdentification(usageIdentification);
        segment.setForm(templateSegment.getForm());
        segment.setStartNode(reusedNode(templateSegment.getStartNode()));
        segment.setEndNode(reusedNode(templateSegment.getEndNode()));
        templateSegment.getLengthInformations()
                .stream()
                .map(TopologyReusageBuilder::copy)
                .forEach(segment.getLengthInformations()::add);

        usage.getTopologySegments().add(segment);
        addReusage(templateSegment, segment);

        return this;
    }

    @Override
    public VecReusageSpecification build() {
        return reusageSpecification;
    }

    private VecTopologyNode reusedNode(final VecTopologyNode templateNode) {
        final VecTopologyNode node = reusedNodes.get(templateNode);
        if (node == null) {
            throw new VecScriptingException(
                    "The TopologyNode '" + templateNode.getIdentification() + "' has to be reused first.");
        }
        return node;
    }

    private void addReusage(final VecExtendableElement templateElement, final VecExtendableElement usageElement) {
        final VecReusage reusage = new VecReusage();
        reusage.setTemplate(templateElement);
        reusage.setUsage(usageElement);
        reusageSpecification.getReusages().add(reusage);
    }

    private static VecSegmentLength copy(final VecSegmentLength templateLength) {
        final VecNumericalValue length = new VecNumericalValue();
        length.setValueComponent(templateLength.getLength().getValueComponent());
        length.setUnitComponent(templateLength.getLength().getUnitComponent());

        final VecSegmentLength result = new VecSegmentLength();
        result.setLength(length);
        result.setClassification(templateLength.getClassification());
        return result;
    }
}
