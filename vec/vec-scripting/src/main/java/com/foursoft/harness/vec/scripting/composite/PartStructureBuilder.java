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
package com.foursoft.harness.vec.scripting.composite;

import com.foursoft.harness.vec.scripting.Locator;
import com.foursoft.harness.vec.scripting.core.PartOrUsageRelatedSpecificationBuilder;
import com.foursoft.harness.vec.v2x.VecOccurrenceOrUsage;
import com.foursoft.harness.vec.v2x.VecPartStructureContentTypeLiteral;
import com.foursoft.harness.vec.v2x.VecPartStructureSpecification;

import java.util.Arrays;

/**
 * Builder for the bill of material of a composite part (e.g. an assembly): a {@link VecPartStructureSpecification}
 * that references contents of the part, which have to be defined before (see {@link CompositePartDefinition}).
 */
public class PartStructureBuilder extends PartOrUsageRelatedSpecificationBuilder<VecPartStructureSpecification> {

    private final VecPartStructureSpecification partStructureSpecification;
    private final Locator<? extends VecOccurrenceOrUsage> contentLocator;

    public PartStructureBuilder(final String partNumber, final Locator<? extends VecOccurrenceOrUsage> contentLocator) {
        this.contentLocator = contentLocator;
        this.partStructureSpecification = initializeSpecification(VecPartStructureSpecification.class, partNumber);
    }

    public PartStructureBuilder withContent(final VecPartStructureContentTypeLiteral content) {
        partStructureSpecification.setContentLiteral(content);
        return this;
    }

    /**
     * References the given occurrences or usages as contents of the bill of material ({@code InBillOfMaterial}).
     */
    public PartStructureBuilder withContents(final String... identifications) {
        Arrays.stream(identifications)
                .map(contentLocator::locate)
                .forEach(partStructureSpecification.getInBillOfMaterial()::add);
        return this;
    }

    @Override
    public VecPartStructureSpecification build() {
        return partStructureSpecification;
    }
}
