package com.foursoft.harness.kbl2vec.transform.harness;

import com.foursoft.harness.kbl.v25.KblHarness;
import com.foursoft.harness.kbl.v25.KblModule;
import com.foursoft.harness.kbl.v25.KblModuleConfiguration;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecConfigurationConstraint;
import com.foursoft.harness.vec.v2x.VecConfigurationConstraintSpecification;

import java.util.stream.Stream;

import static com.foursoft.harness.kbl2vec.transform.Fragments.abbreviatedClassName;

public class ConfigurationConstraintSpecificationTransformer
        implements Transformer<KblHarness, VecConfigurationConstraintSpecification> {
    @Override
    public TransformationResult<VecConfigurationConstraintSpecification> transform(final TransformationContext context,
                                                                                   final KblHarness source) {
        final VecConfigurationConstraintSpecification element = new VecConfigurationConstraintSpecification();
        element.setIdentification(
                abbreviatedClassName(element.getClass()) + "-" + source.getPartNumber());
        return TransformationResult.from(element)
                .withDownstream(KblModuleConfiguration.class, VecConfigurationConstraint.class,
                                moduleConfigurationQuery(source),
                                VecConfigurationConstraintSpecification::getConfigurationConstraints)
                .build();
    }

    Query<KblModuleConfiguration> moduleConfigurationQuery(final KblHarness source) {
        return () -> {
            final Stream<KblModuleConfiguration> moduleModuleConfigurations = source.getModules().stream().map(
                    KblModule::getModuleConfiguration);
            final Stream<KblModuleConfiguration> standAloneModuleConfigurations =
                    source.getModuleConfigurations().stream();

            return Stream.concat(standAloneModuleConfigurations, moduleModuleConfigurations)
                    .toList();
        };
    }

}
