package com.foursoft.harness.vec.scripting;

import com.foursoft.harness.vec.scripting.core.PartUsageBuilder;
import com.foursoft.harness.vec.scripting.core.SpecificationLocator;
import com.foursoft.harness.vec.scripting.schematic.ConnectionLookup;
import com.foursoft.harness.vec.v2x.VecPartStructureSpecification;
import com.foursoft.harness.vec.v2x.VecPartUsage;
import com.foursoft.harness.vec.v2x.VecPartUsageSpecification;

public class VirtualPartStructureBuilder implements Builder<VirtualPartStructureBuilder.VirtualPartStructureResult> {

    private final VecPartStructureSpecification partStructureSpecification;
    private final VecPartUsageSpecification partUsageSpecification;
    private final VecSession session;
    private final SpecificationLocator specificationLocator;
    private final ConnectionLookup connectionLookup;

    public VirtualPartStructureBuilder(VecSession session, SpecificationLocator specificationLocator,
                                       ConnectionLookup connectionLookup) {
        this.session = session;
        this.specificationLocator = specificationLocator;
        this.connectionLookup = connectionLookup;
        partStructureSpecification = initializePartStructureSpecification();
        partUsageSpecification = initalizePartUsageSpecification();
    }

    @Override public VirtualPartStructureResult build() {
        return new VirtualPartStructureResult(partStructureSpecification, partUsageSpecification);
    }

    public VirtualPartStructureBuilder addPartUsage(String identification, Customizer<PartUsageBuilder> customizer) {
        PartUsageBuilder builder = new PartUsageBuilder(session, identification, specificationLocator,
                                                        connectionLookup);

        customizer.customize(builder);

        VecPartUsage result = builder.build();

        partUsageSpecification.getPartUsages().add(result);
        partStructureSpecification.getInBillOfMaterial().add(result);

        return this;
    }

    private VecPartUsageSpecification initalizePartUsageSpecification() {
        VecPartUsageSpecification result = new VecPartUsageSpecification();
        result.setIdentification("VIRTUAL COMPONENTS");
        return result;
    }

    private VecPartStructureSpecification initializePartStructureSpecification() {
        VecPartStructureSpecification result = new VecPartStructureSpecification();
        result.setIdentification("STRUCTURE");
        return result;
    }

    public record VirtualPartStructureResult(VecPartStructureSpecification bom, VecPartUsageSpecification usages) {
    }

}
