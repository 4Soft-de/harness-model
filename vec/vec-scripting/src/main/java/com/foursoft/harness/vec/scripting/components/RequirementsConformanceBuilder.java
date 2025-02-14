package com.foursoft.harness.vec.scripting.components;

import com.foursoft.harness.vec.scripting.DefaultValues;
import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.core.PartOrUsageRelatedSpecificationBuilder;
import com.foursoft.harness.vec.v2x.VecDocumentVersion;
import com.foursoft.harness.vec.v2x.VecRequirementsConformanceSpecification;
import com.foursoft.harness.vec.v2x.VecRequirementsConformanceStatement;

import java.util.Optional;

public class RequirementsConformanceBuilder extends
        PartOrUsageRelatedSpecificationBuilder<VecRequirementsConformanceSpecification> {
    private final VecSession session;
    private final String partNumber;
    private final VecRequirementsConformanceSpecification element;

    public RequirementsConformanceBuilder(final VecSession session, final String partNumber) {
        this.session = session;
        this.partNumber = partNumber;

        element = this.initializeSpecification(VecRequirementsConformanceSpecification.class, partNumber);
    }

    public RequirementsConformanceBuilder addConformanceWith(final String documentNumber, final String companyName) {
        Optional<VecDocumentVersion> requirements = session.findDocument(documentNumber, companyName);

        if (requirements.isEmpty()) {
            session.document(documentNumber, "", doc ->
                    doc.documentType(DefaultValues.REQUIREMENTS_DESCRIPTION)
                            .companyName(companyName)
            );
            requirements = session.findDocument(documentNumber, companyName);
        }

        final VecRequirementsConformanceStatement requirementsStatement = new VecRequirementsConformanceStatement();
        requirementsStatement.setSatisfies(true);
        requirementsStatement.setDocumentVersion(requirements.orElseThrow());
        element.getConformanceStatements().add(requirementsStatement);
        return this;
    }

    @Override public VecRequirementsConformanceSpecification build() {
        return element;
    }
}
