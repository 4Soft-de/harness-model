package com.foursoft.harness.vec.scripting;

import com.foursoft.harness.vec.v2x.VecDocumentVersion;

public class WireSingleCoreBuilder extends AbstractChildBuilder<ComponentMasterDataBuilder> {

    private final VecDocumentVersion partMasterDocument;
    private final CoreSpecificationBuilder coreSpecificationBuilder;
    private final WireSpecificationBuilder wireSpecificationBuilder;
    private final WireElementBuilder<WireSpecificationBuilder> wireElementBuilder;
    private final InsulationSpecificationBuilder<WireElementBuilder<WireSpecificationBuilder>>
            insulationSpecificationBuilder;

    public WireSingleCoreBuilder(final ComponentMasterDataBuilder parent, final String partNumber,
                                 final VecDocumentVersion partMasterDocument) {
        super(parent);
        this.partMasterDocument = partMasterDocument;

        String coreId = "Core-" + partNumber;
        coreSpecificationBuilder = parent.addCoreSpecification(coreId);
        wireSpecificationBuilder = parent.addWireSpecification();

        wireElementBuilder = wireSpecificationBuilder.withWireElement("1").withCoreSpecification(coreId);
        insulationSpecificationBuilder = wireElementBuilder.addInsulationSpecification(
                "Ins-" + partNumber);

    }

    public WireSingleCoreBuilder withCSA(double csa) {
        this.coreSpecificationBuilder.withCSA(csa);
        return this;
    }

    public WireSingleCoreBuilder withOutsideDiameter(double diameter, double lowerTolerance, double upperTolerance) {
        this.wireElementBuilder.withOutsideDiameter(diameter, lowerTolerance, upperTolerance);
        return this;
    }

    public WireSingleCoreBuilder withInsulationThickness(double thickness) {
        this.insulationSpecificationBuilder.withThickness(thickness);
        return this;
    }

    public WireSingleCoreBuilder withDin76722WireType(String wireType) {
        this.wireElementBuilder.withDin76722WireType(wireType);
        return this;
    }

    public WireSingleCoreBuilder withNumberOfStrands(final int numberOfStrands) {
        this.coreSpecificationBuilder.withNumberOfStrands(numberOfStrands);
        return this;
    }

    public WireSingleCoreBuilder withStrandDiameter(final double diameter) {
        this.coreSpecificationBuilder.withStrandDiameter(diameter);
        return this;
    }
}
