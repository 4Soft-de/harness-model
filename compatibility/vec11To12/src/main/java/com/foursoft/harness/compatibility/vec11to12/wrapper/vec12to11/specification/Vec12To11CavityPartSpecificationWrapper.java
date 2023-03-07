package com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11.specification;

import com.foursoft.harness.compatibility.core.Context;
import com.foursoft.harness.compatibility.core.util.IdCreator;
import com.foursoft.harness.compatibility.vec11to12.Constants;
import com.foursoft.harness.vec.v113.VecNumericalValue;
import com.foursoft.harness.vec.v113.VecSize;
import com.foursoft.harness.vec.v113.VecValueRange;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v12x.VecCavityPartSpecification}
 * to {@link com.foursoft.harness.vec.v113.VecCavityPartSpecification}.
 */
public class Vec12To11CavityPartSpecificationWrapper extends AbstractSpecificationReflectionBasedWrapper {

    private VecValueRange valueRange;

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec12To11CavityPartSpecificationWrapper(final Context context, final Object target) {
        super(context, target);
    }

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        final String methodName = method.getName();

        if (Constants.GET_CAVITY_DIAMETER.equals(methodName)) {
            if (valueRange == null) {
                valueRange = createDiameter(allArguments);
            }
            return valueRange;
        }

        if (Constants.SET_CAVITY_DIAMETER.equals(methodName) && allArguments.length == 1) {
            valueRange = (VecValueRange) allArguments[0];
            return null;
        }

        if (GET_SEALING_CLASSES.equals(method.getName())) {
            return getSealingClasses();
        }

        return super.wrapObject(obj, method, allArguments);
    }

    private VecValueRange createDiameter(final Object[] allArguments) {
        final List<VecSize> vecSizes = wrapList(Constants.GET_CAVITY_DIMENSIONS, VecSize.class, allArguments);

        if (vecSizes.isEmpty()) {
            return null;
        }
        final VecSize vecSize = vecSizes.get(0);
        final VecValueRange diameter = new VecValueRange();
        diameter.setXmlId(IdCreator.generateXmlId(VecValueRange.class));
        Optional.of(vecSize.getHeight())
                .ifPresentOrElse(height -> setValues(diameter, height),
                                 () -> setValues(diameter, vecSize.getWidth()));
        return diameter;
    }

    private void setValues(final VecValueRange diameter,
                           final VecNumericalValue numericalValue) {
        if (numericalValue == null) {
            return;
        }
        diameter.setXmlId(IdCreator.generateXmlId(VecValueRange.class));
        diameter.setMinimum(numericalValue.getValueComponent());
        diameter.setMaximum(numericalValue.getValueComponent());
        diameter.setUnitComponent(numericalValue.getUnitComponent());
    }

}
