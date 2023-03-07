package com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11.specification;

import com.foursoft.harness.compatibility.core.Context;
import com.foursoft.harness.compatibility.core.util.IdCreator;
import com.foursoft.harness.compatibility.core.wrapper.ReflectionBasedWrapper;
import com.foursoft.harness.compatibility.vec11to12.util.Predicates;
import com.foursoft.harness.vec.common.util.StreamUtils;
import com.foursoft.harness.vec.v113.VecSealingClass;
import com.foursoft.harness.vec.v12x.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract class for Specification wrapper.
 */
public abstract class AbstractSpecificationReflectionBasedWrapper extends ReflectionBasedWrapper {

    protected static final String GET_SEALING_CLASSES = "getSealingClasses";

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSpecificationReflectionBasedWrapper.class);

    private static final String SEALING_CLASS = "SealingClass";

    protected AbstractSpecificationReflectionBasedWrapper(final Context context,
                                                          final Object target) {
        super(context, target);
    }

    protected VecGeneralTechnicalPartSpecification getGeneralTechnicalPartSpecification() {
        final VecDocumentVersion parentDocumentVersion = getParentDocumentVersion();
        if (parentDocumentVersion == null) {
            LOGGER.debug("There was no parent document version found!");
            return null;
        }
        return parentDocumentVersion.getSpecifications().stream()
                .filter(VecGeneralTechnicalPartSpecification.class::isInstance)
                .map(VecGeneralTechnicalPartSpecification.class::cast)
                .collect(StreamUtils.findOneOrNone())
                .orElse(null);
    }

    protected List<VecSealingClass> getSealingClasses() {
        final VecGeneralTechnicalPartSpecification gtps = getGeneralTechnicalPartSpecification();
        if (gtps == null) {
            LOGGER.debug("There was no VecGeneralTechnicalPartSpecification found!");
            return new ArrayList<>();
        }

        return gtps.getRobustnessProperties().stream()
                .filter(c -> SEALING_CLASS.equalsIgnoreCase(c.getClassKey()))
                .map(this::createVecSealingClass)
                .collect(Collectors.toList());
    }

    private VecSealingClass createVecSealingClass(final VecRobustnessProperties c) {
        final VecSealingClass sealingClass = new VecSealingClass();
        sealingClass.setXmlId(IdCreator.generateXmlId(VecSealingClass.class));
        sealingClass.setClassKey(c.getClassKey());
        sealingClass.setClazz(c.getClazz());
        sealingClass.setClassReferenceSystem(c.getClassReferenceSystem());
        return sealingClass;
    }

    private VecDocumentVersion getParentDocumentVersion() {
        final VecSpecification targetSpecification = (VecSpecification) getTarget();
        if (targetSpecification.getParentDocumentVersion() != null) {
            return targetSpecification.getParentDocumentVersion();
        } else {
            return ((VecContent) getContext().getContent()).getDocumentVersions().stream()
                    .filter(Predicates.partMasterV12())
                    .filter(c -> c.getSpecifications().contains(targetSpecification))
                    .collect(StreamUtils.findOneOrNone())
                    .orElse(null);
        }
    }

}
