package com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12.specification;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.Context;
import com.foursoft.harness.compatibility.core.util.IdCreator;
import com.foursoft.harness.compatibility.core.wrapper.ReflectionBasedWrapper;
import com.foursoft.harness.compatibility.vec11to12.Constants;
import com.foursoft.harness.compatibility.vec11to12.util.WrapperUtils;
import com.foursoft.harness.vec.v113.VecValueRange;
import com.foursoft.harness.vec.v12x.VecNumericalValue;
import com.foursoft.harness.vec.v12x.VecSize;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v113.VecCavityPartSpecification}
 * to {@link com.foursoft.harness.vec.v12x.VecCavityPartSpecification}.
 */
public class Vec11To12CavityPartSpecificationWrapper extends ReflectionBasedWrapper {

    private List<String> compatibleCavityGeometries;
    private List<VecSize> vecSize;

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec11To12CavityPartSpecificationWrapper(final CompatibilityContext context, final Object target) {
        super(context, target);
    }

    public List<VecSize> createVecSize(final VecValueRange range, final Context context) {
        final List<VecSize> tmpVecSizes = new ArrayList<>();
        tmpVecSizes.add(createSize(range, range.getMinimum(), context));
        tmpVecSizes.add(createSize(range, range.getMaximum(), context));
        return tmpVecSizes;
    }

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        final String methodName = method.getName();

        if (Constants.GET_CAVITY_DIMENSIONS.equals(methodName)) {
            if (vecSize == null) {
                vecSize = getVecSizes();
            }
            return vecSize;
        }

        if ("getCompatibleCavityGeometries".equals(methodName)) {
            if (compatibleCavityGeometries == null) {
                compatibleCavityGeometries = new ArrayList<>();
            }
            return compatibleCavityGeometries;
        }

        return super.wrapObject(obj, method, allArguments);
    }

    private VecSize createSize(final VecValueRange range, final double value, final Context context) {
        final VecSize tempVecSize = new VecSize();
        tempVecSize.setXmlId(IdCreator.generateXmlId(tempVecSize));

        final VecNumericalValue vecNumericalValue = new VecNumericalValue();
        vecNumericalValue.setXmlId(IdCreator.generateXmlId(vecNumericalValue));
        vecNumericalValue.setUnitComponent(
                context.getWrapperProxyFactory().createProxy(range.getUnitComponent()));
        vecNumericalValue.setValueComponent(value);

        tempVecSize.setHeight(vecNumericalValue);
        tempVecSize.setWidth(WrapperUtils.copyVec12xNumericalValue(vecNumericalValue));
        return tempVecSize;
    }

    private List<VecSize> getVecSizes() {
        return getResultObject(Constants.GET_CAVITY_DIAMETER, VecValueRange.class)
                .map(c -> createVecSize(c, getContext()))
                .orElseGet(ArrayList::new);
    }

}
