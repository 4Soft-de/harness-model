/*-
 * ========================LICENSE_START=================================
 * Compatibility VEC 1.2.X To VEC 2.X.X
 * %%
 * Copyright (C) 2020 - 2023 4Soft GmbH
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
package com.foursoft.harness.compatibility.vec12to20;

import com.foursoft.harness.compatibility.core.PropertyAddition;
import com.foursoft.harness.compatibility.core.PropertyAdditionProvider;
import com.foursoft.harness.compatibility.core.PurePropertyAdditions;
import com.foursoft.harness.compatibility.core.mapping.NameBasedClassMapper;
import com.foursoft.harness.compatibility.core.util.ClassUtils;
import com.foursoft.harness.vec.v12x.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static com.foursoft.harness.compatibility.core.PropertyAddition.*;

/**
 * Class responsible for mapping VEC 1.2.X classes to VEC 2.X.X <b>and vice versa</b>.
 */
public class Vec12XTo20XClassMapper extends NameBasedClassMapper implements PropertyAdditionProvider {

    private final Map<Class<?>, Class<?>> explicitClassMappings;
    private final PurePropertyAdditions propertyAdditions;

    /**
     * Creates a VEC 1.1 to VEC 1.2 Class Mapper.
     */
    public Vec12XTo20XClassMapper() {
        super(Constants.PACKAGE_VEC12X, Constants.PACKAGE_VEC20X);
        explicitClassMappings = new HashMap<>();

        propertyAdditions = new PurePropertyAdditions()
                .register(VecApplicationConstraint.class, value("fromEffectivityControlKey"),
                          value("toEffectivityControlKey"),
                          backRef("refConfigurationConstraint"))
                .register(VecBoltMountedFixingSpecification.class,
                          value("boltShape")
                )
                .register(VecBoundingBox.class, extendableElementPropertiesPlus())
                .register(VecBridgeTerminalRole.class, list("connection"))
                .register(VecBuildingBlockPositioning2D.class, value("identification"))
                .register(VecBuildingBlockPositioning3D.class, value("identification"))
                .register(VecCableLeadThroughReference.class,
                          extendableElementPropertiesPlus(list("cableLeadThroughOutletReferences"))
                )
                .register(VecCartesianPoint2D.class, value("parentConnectionViewSpecification"),
                          value("parentNetViewSpecification")
                )
                .register(VecCartesianPoint3D.class, value("parentCavityPositionDetail")
                )
                .register(VecCartesianVector.class,
                          extendableElementPropertiesPlus(
                                  value("parentCavityPositionDetail"))
                )
                .register(VecCavity.class, value("parentCavityLayout"),
                          value("positionDetail"),
                          backRef("refCableSealEntry"),
                          backRef("refSealingPin")
                )
                .register(VecCavityAddOn.class, extendableElementPropertiesPlus())
                .register(VecCavityMapping.class, extendableElementPropertiesPlus())
                .register(VecCavityReference.class, list("componentPort"))
                .register(VecCavitySpecification.class, value("cavitySealingLength"))
                .register(VecComponentConnector.class,
                          backRef("refConnectorHousingRole"),
                          backRef("refHousingComponent"))
                .register(VecComponentNode.class,
                          backRef("refComponentNodeViewItem"),
                          backRef("refConnectionNodeViewItem"),
                          backRef("refEEComponentSpecification")
                )
                .register(VecComponentPort.class,
                          list("aliasIds"),
                          backRef("refComponentPortViewItem"),
                          backRef("refPinComponent")
                )
                .register(VecConductorCurrentInformation.class, extendableElementPropertiesPlus())
                .register(VecConductorMaterial.class, extendableElementPropertiesPlus(
                        value("parentTerminalProtectionSpecification")
                ))
                .register(VecConductorSpecification.class,
                          value("transmissionMedium"),
                          list("wireTypes"),
                          backRef("refCoreCrimpDetail")
                )
                .register(VecConfigurableElement.class,
                          list("configurationConstraints"),
                          backRef("refConfigurationConstraint"),
                          backRef("refSignalGraphNode"))
                .register(VecConnection.class, backRef("refSwitchingState"))
                .register(VecConnectorHousingRole.class,
                          value("cpaState"),
                          value("lockingState")
                )
                .register(VecConnectorHousingSpecification.class,
                          value("connectorPositionAssuranceType"),
                          value("lockingType"),
                          list("modularSlotConfigurations"),
                          value("referenceSurfaceDefinition")
                )
                .register(VecContent.class, list("quantityKinds"), list("resourceVersions"))
                .register(VecDefaultDimension.class, extendableElementPropertiesPlus())
                .register(VecDefaultDimensionSpecification.class, list("defaultDimensions"))
                .register(VecDocumentVersion.class, value("confidentialityLevel"),
                          list("documentClassifications"))
                .register(VecFixingSpecification.class, value("maximumWidthA"),
                          value("maximumWidthB"))
                .register(VecEEComponentRole.class, list("componentNode"))
                .register(VecEEComponentSpecification.class,
                          value("voltageRating"), value("componentNode"))
                .register(VecExtendableElement.class, backRef("refReusage"))
                .register(VecFunctionalRequirement.class, extendableElementPropertiesPlus())
                .register(VecFunctionalStructureNode.class, extendableElementPropertiesPlus())
                .register(VecFuseComponent.class, extendableElementPropertiesPlus())
                .register(VecFuseSpecification.class, value("characteristic"))
                .register(VecGeneralTechnicalPartSpecification.class,
                          list("applicationTypes"),
                          list("materialCompositions"),
                          list("voltageLevelCompliances"),
                          value("voltageRating")
                )
                .register(VecGrommetSpecification.class, value("mountingType"))
                .register(VecHousingComponent.class, configurableElementPropertiesPlus(list("segmentConnectionPoints"))
                )
                .register(VecHousingComponentReference.class, list("componentConnector"))
                .register(VecInsulationSpecification.class,
                          list("allowedLabelingTechnologies"),
                          value("labelingTechnology"),
                          list("wireTypes"),
                          backRef("refInsulationCrimpDetail"))
                .register(VecItemVersion.class, list("changeRestrictions"))
                .register(VecMapping.class,
                          value("identificationA"),
                          value("identificationB"))
                .register(VecMatingDetail.class, list("connection"))
                .register(VecMatingPoint.class, list("connection"))
                .register(VecMeasurementPoint.class, extendableElementPropertiesPlus())
                .register(VecMeasurePointPosition.class, extendableElementPropertiesPlus())
                .register(VecModularSlot.class, list("layouts"))
                .register(VecModularSlotAddOn.class, extendableElementPropertiesPlus())
                .register(VecModuleFamily.class, configurableElementPropertiesPlus())
                .register(VecModuleList.class, configurableElementPropertiesPlus())
                .register(VecNetType.class, extendableElementPropertiesPlus())
                .register(VecNetworkNode.class, backRef("refNetworkNodeViewItem"))
                .register(VecNetworkPort.class,
                          list("aliasIds"),
                          backRef("refNetworkPortViewItem"))
                .register(VecNodeMapping.class, extendableElementPropertiesPlus())
                .register(VecOpenCavitiesAssignment.class, extendableElementPropertiesPlus())
                .register(VecOccurrenceOrUsage.class,
                          value("quantity"),
                          backRef("refCableLeadThroughOutletReference"),
                          backRef("refPlacementPointReference"),
                          backRef("refSlotReference"))
                .register(VecPartRelation.class, extendableElementPropertiesPlus(
                        value("parentWireTupleSpecification"),
                        backRef("refCableLeadThroughOutlet"),
                        backRef("refPlacementPoint")
                ))
                .register(VecPartSubstitutionSpecification.class,
                          value("specialPartType"),
                          list("describedPart"),
                          backRef("refSpecificRole"))
                .register(VecPartVersion.class,
                          value("partNumberType"),
                          backRef("refTerminalCurrentInformation"))
                .register(VecPerson.class, extendableElementPropertiesPlus())
                .register(VecPinComponent.class, configurableElementPropertiesPlus(
                        value("componentPort")
                ))
                .register(VecPinComponentBehavior.class, list("aliasIds"))
                .register(VecPinCurrentInformation.class, extendableElementPropertiesPlus(
                        value("determinationType")
                ))
                .register(VecPinOpticalInformation.class, extendableElementPropertiesPlus())
                .register(VecPinTiming.class, extendableElementPropertiesPlus())
                .register(VecPinVoltageInformation.class, extendableElementPropertiesPlus(
                        value("determinationType")
                ))
                .register(VecPinWireMappingPoint.class, extendableElementPropertiesPlus())
                .register(VecPinWireMappingSpecification.class, list("pinWireMappingPoints"))
                .register(VecTerminalRole.class, list("componentPort"))
                .register(VecTerminalSpecification.class,
                          value("connectionALength"),
                          list("minimumDistances"),
                          value("overallLength"),
                          value("transmissionMedium"))
                .register(VecUnit.class, extendableElementPropertiesPlus(
                        value("unEceCode"),
                        backRef("refCavityPositionDetail"),
                        backRef("refQuantityKind")
                ))
                .register(VecZoneAssignment.class, extendableElementPropertiesPlus())
                .register(VecZoneCoverage.class, extendableElementPropertiesPlus())

                // ── additions discovered via Vec12To20ProxyCoverageTest ─────────────────────────────
                // extendableElement classes not yet covered
                .register(VecPlacementPointPosition.class, extendableElementPropertiesPlus())
                .register(VecPowerConsumption.class, extendableElementPropertiesPlus())
                .register(VecRequirementsConformanceStatement.class, extendableElementPropertiesPlus())
                .register(VecSealedCavitiesAssignment.class, extendableElementPropertiesPlus())
                .register(VecSegmentMapping.class, extendableElementPropertiesPlus())
                .register(VecSlotMapping.class, extendableElementPropertiesPlus())
                .register(VecTerminalReceptionReference.class, extendableElementPropertiesPlus())
                .register(VecTopologyBendingRestriction.class, extendableElementPropertiesPlus())
                .register(VecWireReceptionAddOn.class, extendableElementPropertiesPlus())
                .register(VecWireReceptionReference.class, extendableElementPropertiesPlus())

                // back-references
                .register(VecPlacementPoint.class,
                          backRef("refCableLeadThroughOutlet"),
                          backRef("refSegmentConnectionPointHC"),
                          list("supplementaryParts"))
                .register(VecSegmentConnectionPoint.class, backRef("refSegmentConnectionPointHC"))
                .register(VecVariantConfiguration.class, backRef("refConfigurationConstraint"))

                // list additions
                .register(VecPlacementPointReference.class, list("usedSupplementaryParts"))
                .register(VecPluggableTerminalSpecification.class, list("extractionTool"))
                .register(VecSwitchingState.class, list("switchedLogicalConnections"))
                .register(VecTapeRole.class, list("materialLengths"), value("numberOfTurns"))
                .register(VecTerminalCurrentInformation.class, list("validForMatingTerminal"))
                .register(VecTerminalReceptionSpecification.class,
                          value("contactRangeLength"), value("testingPullOutForce"))
                .register(VecWireElementReference.class,
                          list("connection"), list("connectionGroup"),
                          value("labelPosition"), value("labelType"), value("labelValue"),
                          value("labelingTechnology"), backRef("refCableSealEntryReference"))
                .register(VecWireEnd.class,
                          list("connectionEnd"), value("cutBackLength"), value("insulationPullbackLength"),
                          value("strippingLength"), backRef("refWireTupleTermination"))
                .register(VecWireGrouping.class, list("connectionGroup"))
                .register(VecWireProtectionRole.class, list("materialLengths"))

                // scalar / reference value additions
                .register(VecPluggableTerminalSpecification.class,
                          value("maximumInsertionForce"), value("testingPullOutForce"))
                .register(VecPotentialDistributorSpecification.class, value("boltNominalSize"))
                .register(VecSheetOrChapter.class, value("sheetSize"))
                .register(VecShieldSpecification.class, value("opticalCoverage"))
                .register(VecSignal.class, value("wireTupleRequirements"))
                .register(VecSlotMapping.class, value("layoutIdentificationA"), value("layoutIdentificationB"))
                .register(VecSlotReference.class, value("tpaState"), list("usedSupplementaryParts"))
                .register(VecSlotSpecification.class, value("secondaryLockingType"))
                .register(VecTerminalCurrentInformation.class, value("environmentTemperature"))
                .register(VecTerminalPairing.class, value("matingCycles"))
                .register(VecTerminalReceptionSpecification.class, value("contactRangeLength"))
                .register(VecTransformation2D.class,
                          value("parentComponentNodeViewItem"),
                          value("parentConnectionNodeViewItem"),
                          value("parentNetworkNodeViewItem"))
                .register(VecUsageConstraint.class,
                          value("fromEffectivityControlKey"), value("toEffectivityControlKey"))
                .register(VecWireElement.class, value("index"), value("layer"))
                .register(VecWireElementSpecification.class, value("transmissionMediumType"))
                .register(VecWireMountingDetail.class,
                          value("absoluteSealPosition"), value("coreCrimpSize"),
                          value("corePullOffForce"), value("insulationCrimpSize"),
                          value("insulationPullOffForce"), value("pullOffForce"),
                          value("wireReceptionType"), value("wireTipProtrusion"))
                .register(VecWireProtectionRole.class,
                          list("materialLengths"), backRef("refWireProtectionGroup"))
                .register(VecWireReception.class, value("rotation"))
                .register(VecWireReceptionSpecification.class,
                          value("conductorCrimpLegHeight"), value("conductorCrimpLength"),
                          value("conductorCrimpShape"), value("connectionBLength"),
                          value("crimpBarrelType"), value("crimpConnectionLength"),
                          value("cutOffTabLength"), value("frontBellMouthLength"),
                          value("insulationCrimpLegHeight"), value("insulationCrimpLength"),
                          value("insulationCrimpShape"), value("rearBellMouthLength"),
                          value("sheetThickness"), value("wireTipProtrusion"),
                          list("coreCrimpDetails"), list("wireReceptionTypes"))
        ;
    }

    private static PropertyAddition[] configurableElementPropertiesPlus(final PropertyAddition... additions) {
        final PropertyAddition[] extendableElementProperties = {
                list("applicationConstraint"),
                list("configurationConstraints"),
                list("associatedAssignmentGroups"),
                value("componentConnector"),
                value("configInfo"),
                backRef("refConfigurationConstraint"),
                backRef("refSignalGraphNode")
        };
        return Stream.concat(Arrays.stream(extendableElementProperties), Arrays.stream(additions)).toArray(
                PropertyAddition[]::new);
    }

    private static PropertyAddition[] extendableElementPropertiesPlus(final PropertyAddition... additions) {
        final PropertyAddition[] extendableElementProperties = {
                list("referencedExternalDocuments"),
                list("customProperties"),
                backRef("refExternalMapping"),
                backRef("refReusage")
        };
        return Stream.concat(Arrays.stream(extendableElementProperties), Arrays.stream(additions)).toArray(
                PropertyAddition[]::new);
    }

    @Override
    public PurePropertyAdditions getPropertyAdditions() {
        return propertyAdditions;
    }

    @Override
    public Class<?> map(final Class<?> clazz) {
        return resolveExplicitClassMappings(clazz).orElseGet(() -> super.map(clazz));
    }

    private Optional<Class<?>> resolveExplicitClassMappings(final Class<?> clazz) {
        return Optional.<Class<?>>ofNullable(explicitClassMappings.get(clazz))
                .or(() -> Optional.ofNullable(explicitClassMappings.get(ClassUtils.getNonProxyClass(clazz))));
    }

}
