package com.foursoft.harness.vec.scripting.core;

import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.v2x.VecDocumentVersion;
import com.foursoft.harness.vec.v2x.VecPartVersion;
import com.foursoft.harness.vec.v2x.VecSpecification;

import java.util.List;
import java.util.Optional;

public class DocumentVersionBuilder {

    private final VecDocumentVersion documentVersion;

    public DocumentVersionBuilder(final VecSession session, final String documentNumber, String version) {
        documentVersion = new VecDocumentVersion();
        session.getVecContentRoot().getDocumentVersions().add(documentVersion);

        documentVersion.setDocumentNumber(documentNumber);
        documentVersion.setCompanyName(session.getDefaultValues().getCompanyName());
        documentVersion.setDocumentVersion(version);
    }

    public DocumentVersionBuilder documentType(String documentType) {
        documentVersion.setDocumentType(documentType);
        return this;
    }

    public DocumentVersionBuilder addSpecification(VecSpecification specification) {
        documentVersion.getSpecifications().add(specification);
        return this;
    }

    public DocumentVersionBuilder addReferencedPart(VecPartVersion partVersion) {
        this.documentVersion.getReferencedPart().add(partVersion);
        return this;
    }

    public <T extends VecSpecification> Optional<T> getSpecificationWith(final Class<T> type,
                                                                         final String identification) {
        return this.documentVersion.getSpecificationWith(type, identification);
    }

    public List<VecPartVersion> getReferencedPart() {
        return documentVersion.getReferencedPart();
    }
}
