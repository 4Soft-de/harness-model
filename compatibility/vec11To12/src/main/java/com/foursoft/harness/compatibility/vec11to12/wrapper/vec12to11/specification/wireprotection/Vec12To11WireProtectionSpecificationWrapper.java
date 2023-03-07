package com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11.specification.wireprotection;

import com.foursoft.harness.compatibility.core.Context;
import com.foursoft.harness.compatibility.core.util.IdCreator;
import com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11.specification.AbstractSpecificationReflectionBasedWrapper;
import com.foursoft.harness.vec.v113.VecAbrasionResistanceClass;
import com.foursoft.harness.vec.v12x.VecGeneralTechnicalPartSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v12x.VecWireProtectionSpecification}
 * to {@link com.foursoft.harness.vec.v113.VecWireProtectionSpecification}.
 */
public class Vec12To11WireProtectionSpecificationWrapper extends AbstractSpecificationReflectionBasedWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(Vec12To11WireProtectionSpecificationWrapper.class);

    private static final String ABRASION_RESISTANCE_CLASS = "AbrasionResistanceClass";

    private List<VecAbrasionResistanceClass> abrasionResistanceClasses;

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec12To11WireProtectionSpecificationWrapper(final Context context, final Object target) {
        super(context, target);
    }

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments)
            throws Throwable {

        if ("getAbrasionResistanceClasses".equals(method.getName())) {
            abrasionResistanceClasses = createAbrasionResistanceClasses();
            return abrasionResistanceClasses;
        }

        if ("setAbrasionResistanceClasses".equals(method.getName())) {
            setAbrasionResistanceClasses(
                    (List<com.foursoft.harness.vec.v113.VecAbrasionResistanceClass>) allArguments[0]);
            return abrasionResistanceClasses;
        }

        return super.wrapObject(obj, method, allArguments);
    }

    private void setAbrasionResistanceClasses(
            final List<com.foursoft.harness.vec.v113.VecAbrasionResistanceClass> allArgument) {
        abrasionResistanceClasses = allArgument;
    }

    private List<VecAbrasionResistanceClass> createAbrasionResistanceClasses() {
        final VecGeneralTechnicalPartSpecification gtps = getGeneralTechnicalPartSpecification();
        if (gtps == null) {
            return new ArrayList<>();
        }

        return gtps.getRobustnessProperties().stream()
                .filter(c -> ABRASION_RESISTANCE_CLASS.equalsIgnoreCase(c.getClassKey()))
                .map(c -> {
                    final VecAbrasionResistanceClass abrasionResistanceClass = new VecAbrasionResistanceClass();
                    abrasionResistanceClass.setXmlId(IdCreator.generateXmlId(VecAbrasionResistanceClass.class));
                    abrasionResistanceClass.setClassKey(c.getClassKey());
                    abrasionResistanceClass.setReferenceSystem(c.getClassReferenceSystem());
                    return abrasionResistanceClass;
                })
                .collect(Collectors.toList());
    }

}
