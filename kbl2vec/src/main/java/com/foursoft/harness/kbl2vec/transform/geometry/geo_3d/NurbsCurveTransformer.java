package com.foursoft.harness.kbl2vec.transform.geometry.geo_3d;

import com.foursoft.harness.kbl.v25.KblBSplineCurve;
import com.foursoft.harness.kbl.v25.KblCartesianPoint;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.kbl2vec.transform.geometry.KnotVector;
import com.foursoft.harness.vec.v2x.VecNURBSControlPoint;
import com.foursoft.harness.vec.v2x.VecNURBSCurve;

import java.util.List;

public class NurbsCurveTransformer implements Transformer<KblBSplineCurve, VecNURBSCurve> {

    @Override
    public TransformationResult<VecNURBSCurve> transform(final TransformationContext context,
                                                         final KblBSplineCurve source) {
        final VecNURBSCurve destination = new VecNURBSCurve();
        destination.getKnots().addAll(deriveKnots(source, context));

        return TransformationResult.from(destination)
                .withDownstream(KblCartesianPoint.class, VecNURBSControlPoint.class,
                                Query.fromLists(source.getControlPoints()), VecNURBSCurve::getControlPoints)
                .build();
    }

    private List<Double> deriveKnots(final KblBSplineCurve source, final TransformationContext context) {
        final int degree = source.getDegree().intValue();
        final int numberOfControlPoints = source.getControlPoints().size();
        final int order = degree + 1;
        final String clamping = context.getConversionProperties().getDefault3DCurveClamping();
        final KnotVector knotVector = new KnotVector(degree, order, numberOfControlPoints, clamping);
        return knotVector.getKnots();
    }
}
