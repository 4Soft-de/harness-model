package com.foursoft.harness.kbl2vec.transform;

import com.foursoft.harness.kbl.v25.KblPart;
import com.foursoft.harness.kbl2vec.convert.Converter;
import com.foursoft.harness.kbl2vec.convert.StringToLocalizedStringConverter;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecDocumentVersion;
import com.foursoft.harness.vec.v2x.VecGeneralTechnicalPartSpecification;
import com.foursoft.harness.vec.v2x.VecLocalizedString;
import com.foursoft.harness.vec.v2x.VecPartVersion;

import java.util.Optional;

public class PartMasterDocumentVersionTransformer implements
        Transformer<KblPart, VecDocumentVersion> {

    private final Converter<String, Optional<VecLocalizedString>> stringConverter;

    public PartMasterDocumentVersionTransformer(final TransformationContext context) {
        stringConverter = new StringToLocalizedStringConverter(context.getConversionProperties()
                                                                       .getDefaultLanguageCode());
    }

    @Override
    public TransformationResult<VecDocumentVersion> transform(final KblPart source) {
        final VecDocumentVersion documentVersion = new VecDocumentVersion();

        documentVersion.setCompanyName(source.getCompanyName());
        documentVersion.setDocumentNumber(source.getPartNumber());
        documentVersion.setDocumentVersion(source.getVersion());
        //TODO: Enums/Consts for OpenEnums.
        documentVersion.setDocumentType("PartMaster");
        stringConverter.convert(source.getAbbreviation())
                .ifPresent(documentVersion.getAbbreviations()::add);
        stringConverter.convert(source.getDescription())
                .ifPresent(documentVersion.getDescriptions()::add);

        return TransformationResult.from(documentVersion)
                .downstreamTransformation(KblPart.class, VecGeneralTechnicalPartSpecification.class, Query.of(source),
                                          documentVersion::getSpecifications)
                .withLinker(source, VecPartVersion.class, documentVersion::getReferencedPart)
                .build();
    }
}
